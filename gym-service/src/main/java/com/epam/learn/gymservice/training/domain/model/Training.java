package com.epam.learn.gymservice.training.domain.model;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainings")
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "training_date", nullable = false)
  private LocalDate trainingDate;

  @Column(columnDefinition = "smallint", nullable = false)
  private Duration duration;

  @ToString.Exclude
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "training_type_id")
  private TrainingType type;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  public void setType(TrainingType type) {
    type.addTraining(this);
    this.type = type;
  }

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
    Training training = (Training) other;
    return getId() != null && Objects.equals(getId(), training.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy t
        ? t.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
