package com.epam.learn.gymservice.training.domain.model;

import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@Table(name = "training_types")
public class TrainingType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Setter(AccessLevel.NONE)
  @Type(ListArrayType.class)
  @Column(columnDefinition = "TEXT[]", updatable = false)
  private List<String> values;

  @Setter(AccessLevel.NONE)
  @ToString.Exclude
  @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  @Setter(AccessLevel.NONE)
  @ToString.Exclude
  @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Trainer> trainers = new ArrayList<>();

  public TrainingType(Long id, String name, List<String> values) {
    this.id = id;
    this.name = name;
    this.values = values;
  }

  public List<String> getValues() {
    return values == null ? List.of() : Collections.unmodifiableList(values);
  }

  public void addTraining(Training training) {
    trainings.add(training);
  }

  public void removeTraining(Training training) {
    trainings.remove(training);
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
    TrainingType type = (TrainingType) other;
    return getId() != null && Objects.equals(getId(), type.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy t
        ? t.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
