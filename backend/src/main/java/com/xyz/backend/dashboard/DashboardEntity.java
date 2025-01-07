package com.xyz.backend.dashboard;

import com.xyz.backend.authentication.user.DashUserDetails;
import com.xyz.backend.dashboard.dtos.DashboardDTO;
import com.xyz.backend.dashboard.widget.WidgetEntity;
import com.xyz.backend.dashboard.widget.dtos.WidgetDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "dashboards")
@Entity
public class DashboardEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  private DashUserDetails owner;

  @OneToMany(fetch = FetchType.EAGER)
  private List<WidgetEntity> widgets = List.of();

  public DashboardDTO toDTO() {
    return new DashboardDTO(uuid.toString(), name, owner.toDTO(), widgets.stream()
        .map(WidgetEntity::toDTO)
        .toArray(WidgetDTO[]::new));
  }
}
