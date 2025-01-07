package com.xyz.backend.dashboard.widget;

import com.xyz.backend.dashboard.DashboardEntity;
import com.xyz.backend.dashboard.widget.dtos.WidgetDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "widgets")
public class WidgetEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(nullable = false)
  private String widgetStoreUrl;

  @Column(nullable = false)
  private String widgetId;

  @Column(nullable = false)
  private int x;

  @Column(nullable = false)
  private int y;

  @Column(nullable = false)
  private int width;

  @Column(nullable = false)
  private int height;

  @ManyToOne(cascade = CascadeType.REMOVE)
  private DashboardEntity dashboard;

  public WidgetDTO toDTO() {
    return new WidgetDTO(uuid.toString(), widgetStoreUrl, widgetId, x, y, width, height);
  }
}
