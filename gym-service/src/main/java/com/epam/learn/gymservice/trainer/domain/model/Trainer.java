package com.epam.learn.gymservice.trainer.domain.model;

import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.training.domain.model.TrainingType;
import com.epam.learn.gymservice.user.domain.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.AccessLevel;
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
@Table(name = "trainers")
public class Trainer {

  @Id private Long id;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  private User user;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "specialization", referencedColumnName = "id")
  private TrainingType specialization;

  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  @ManyToMany(mappedBy = "trainers")
  private Set<Trainee> trainees = new HashSet<>();

  public Trainer(User user, TrainingType specialization) {
    this.user = user;
    this.specialization = specialization;
  }

  public void addTraining(Training training) {
    trainings.add(training);
    training.setTrainer(this);
  }

  public void removeTraining(Training training) {
    trainings.remove(training);
    training.setTrainer(null);
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
    Trainer trainer = (Trainer) other;
    return getId() != null && Objects.equals(getId(), trainer.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy t
        ? t.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
