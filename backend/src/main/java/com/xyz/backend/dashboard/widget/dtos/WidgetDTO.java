package com.xyz.backend.dashboard.widget.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WidgetDTO {
  private String uuid;
  private String widgetStoreUrl;
  private String widgetId;

  private int x;
  private int y;
  private int width;
  private int height;
}
