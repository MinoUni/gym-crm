package com.epam.learn.trsessionservice.domain.model;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionMonth {

  private String month;
  private Integer workload;

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof TrainingSessionMonth that)) return false;
    return Objects.equals(month, that.month);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(month);
  }
}
