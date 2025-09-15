package com.epam.learn.trsessionservice.adapter.spi.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training_sessions")
public class TrainingSessionEntity {

  @Id private Long id;

  @Column(nullable = false)
  private String trainerUsername;

  @Column private String trainerFirstName;

  @Column private String trainerLastName;

  @Column(nullable = false)
  private Boolean isActive;

  @Column(nullable = false)
  private LocalDate trainingDate;

  @Column(nullable = false)
  private LocalTime trainingDuration;

  @Override
  public final boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || this.getClass() != other.getClass()) return false;
    Class<?> oEffectiveClass =
        other instanceof HibernateProxy o
            ? o.getHibernateLazyInitializer().getPersistentClass()
            : other.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy t
            ? t.getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    TrainingSessionEntity that = (TrainingSessionEntity) other;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy t
        ? t.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
