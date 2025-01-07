package com.xyz.backend;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class BackendApplicationTest {
  private final MockMvc mockMvc;
  private final String endpoint;
  private final ObjectMapper objectMapper;

  public BackendApplicationTest(MockMvc mockMvc, String endpoint, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.endpoint = endpoint;
    this.objectMapper = objectMapper;
  }

  protected ResultActions testEndpoint(HttpMethod method, String session, Object body,
      ResultMatcher... matchers) throws Exception {
    return testEndpoint(method, MediaType.APPLICATION_JSON, session, endpoint, body, matchers,
        new Object[0], new Object[0]);
  }

  protected ResultActions testEndpoint(HttpMethod method, String session, Object body,
      Object[] urlParams, ResultMatcher... matchers) throws Exception {
    return testEndpoint(method, MediaType.APPLICATION_JSON, session, endpoint, body, matchers,
        urlParams, new Object[0]);
  }

  protected ResultActions testEndpoint(HttpMethod method, String session, Object body,
      Object[] urlParams, Object[] requestParams,
      ResultMatcher... matchers) throws Exception {
    return testEndpoint(method, MediaType.APPLICATION_JSON, session, endpoint, body, matchers,
        urlParams, requestParams);
  }

  protected ResultActions testEndpoint(HttpMethod method, MediaType type, String session,
      String url, Object body,
      ResultMatcher[] matchers, Object[] urlParams) throws Exception {
    return testEndpoint(method, type, session, url, body, matchers, urlParams, new Object[0]);
  }

  protected ResultActions testEndpoint(HttpMethod method, MediaType type, String session,
      String url, Object body,
      ResultMatcher[] matchers, Object[] urlParams, Object[] requestParams) throws Exception {
    MockHttpServletRequestBuilder builder;
    if (method.equals(GET)) {
      builder = get(url, urlParams);
    } else if (method.equals(POST)) {
      builder = post(url, urlParams);
    } else if (method.equals(PUT)) {
      builder = put(url, urlParams);
    } else if (method.equals(DELETE)) {
      builder = delete(url, urlParams);
    } else {
      throw new UnsupportedOperationException(
          "method: " + method.name() + " not supported");
    }

    for (int i = 0; i < requestParams.length; i += 2) {
      builder = builder.param((String) requestParams[i], requestParams[i + 1].toString());
    }

    if (body == null) {
      body = "";
    }

    if (type.equals(MediaType.APPLICATION_JSON)) {
      body = objectMapper.writeValueAsString(body);
    }

    builder = builder.contentType(type)
        .header("Authorization", session)
        .contentType(type)
        .content((String) body);

    return mockMvc.perform(builder).andExpectAll(matchers);
  }
}