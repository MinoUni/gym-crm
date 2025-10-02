package com.epam.learn.trsessionservice.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionYear {

  private Integer year;

  @Builder.Default private List<TrainingSessionMonth> months = new ArrayList<>();

  public TrainingSessionYear(Integer year) {
    this.year = year;
    this.months = new ArrayList<>();
  }

  public void addMonth(TrainingSessionMonth month) {
    if (months == null) {
      return;
    }
    if (months.contains(month)) {
      months.stream()
          .filter(m -> m.equals(month))
          .findFirst()
          .ifPresent(m -> m.setWorkload(m.getWorkload() + month.getWorkload()));
      return;
    }
    months.add(month);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TrainingSessionYear other)) return false;
    return Objects.equals(year, other.year);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(year);
  }
}
