import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';
import {AuthInterceptor} from './auth.interceptor';
import {AuthService} from './auth.service';

describe('AuthInterceptor', () => {
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: AuthInterceptor,
          multi: true,
        },
        AuthService,
      ],
    });

    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
    authService = TestBed.inject(AuthService);
  });

  it('should add Authorization header when token is present', () => {
    authService.setToken('mock-token');

    httpClient.get('/test').subscribe();

    const req = httpMock.expectOne('/test');
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe('mock-token');
  });

  it('should not add Authorization header when token is absent', () => {
    authService.clearToken();

    httpClient.get('/test').subscribe();

    const req = httpMock.expectOne('/test');
    expect(req.request.headers.has('Authorization')).toBe(false);
  });

  it('should handle error responses', () => {
    authService.setToken('mock-token');

    httpClient.get('/test').subscribe({
      next: () => fail('should have failed with 500 error'),
      error: (error) => {
        expect(error.status).toBe(500);
        expect(error.statusText).toBe('Server Error');
      }
    });

    const req = httpMock.expectOne('/test');
    req.flush('Server Error', {status: 500, statusText: 'Server Error'});
  });


  it('should correctly forward the response', () => {
    authService.setToken('mock-token');

    httpClient.get('/test').subscribe(response => {
      expect(response).toEqual({data: 'test data'});
    });

    const req = httpMock.expectOne('/test');
    req.flush({data: 'test data'});
  });

  afterEach(() => {
    httpMock.verify();
  });
});
