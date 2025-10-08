package com.epam.learn.gymservice.trainee.domain.model;

import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.training.domain.model.Training;
import com.epam.learn.gymservice.user.domain.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
@Table(name = "trainees")
public class Trainee {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column private String address;

  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  private User user;

  @ToString.Exclude
  @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  @ToString.Exclude
  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable(
      name = "trainees_trainers",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id"))
  private Set<Trainer> trainers = new HashSet<>();

  public Trainee(
      Long id,
      LocalDate birthDate,
      String address,
      String firstName,
      String lastName,
      String username,
      boolean isActive,
      List<Training> trainings,
      Set<Trainer> trainers) {
    this.id = id;
    this.birthDate = birthDate;
    this.address = address;
    this.user = new User(id, firstName, lastName, username, isActive);
    if (trainings != null) {
      trainings.forEach(this::addTraining);
    }
    if (trainers != null) {
      trainers.forEach(this::addTrainer);
    }
  }

  public Trainee(LocalDate birthDate, String address, User user) {
    this.birthDate = birthDate;
    this.address = address;
    this.user = user;
  }

  public Trainee(
      LocalDate birthDate,
      String address,
      User user,
      List<Training> trainings,
      Set<Trainer> trainers) {
    this.birthDate = birthDate;
    this.address = address;
    this.user = user;
    if (trainings != null) {
      trainings.forEach(this::addTraining);
    }
    if (trainers != null) {
      trainers.forEach(this::addTrainer);
    }
  }

  public void addTraining(Training training) {
    training.setTrainee(this);
    trainings.add(training);
  }

  public void removeTraining(Training training) {
    trainings.remove(training);
    training.setTrainee(null);
  }

  public void addTrainer(Trainer trainer) {
    trainers.add(trainer);
    trainer.getTrainees().add(this);
  }

  public void removeTrainer(Trainer trainer) {
    trainers.remove(trainer);
    trainer.getTrainees().remove(this);
  }

  public List<Training> getTrainings() {
    return Collections.unmodifiableList(trainings);
  }

  public Set<Trainer> getTrainers() {
    return Collections.unmodifiableSet(trainers);
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
    Trainee trainee = (Trainee) other;
    return getId() != null && Objects.equals(getId(), trainee.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy t
        ? t.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
