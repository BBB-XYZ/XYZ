package com.xyz.backend.authentication.dashboard;

import com.xyz.backend.authentication.dashboard.dtos.DashboardDTO;
import com.xyz.backend.authentication.dashboard.widget.WidgetEntity;
import com.xyz.backend.authentication.dashboard.widget.dtos.WidgetDTO;
import com.xyz.backend.authentication.user.DashUserDetails;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @ManyToOne
  private DashUserDetails owner;

  @OneToMany(cascade = CascadeType.REMOVE)
  private List<WidgetEntity> widgets;

  public DashboardDTO toDTO() {
    return new DashboardDTO(uuid.toString(), name, owner.toDTO(), widgets.stream()
        .map(WidgetEntity::toDTO)
        .toArray(WidgetDTO[]::new));
  }
}
