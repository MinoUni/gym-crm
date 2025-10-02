package com.epam.learn.trsessionservice.adapter.spi.persistence;

import com.epam.learn.trsessionservice.domain.model.TrainingSessionYear;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "first_last_idx", def = "{'trainer_first_name': 1, 'trainer_last_name': 1}")
@Document(collection = "training_sessions")
public class TrainingSessionEntity {

  @Id private Long id;

  @Indexed(unique = true)
  @Field(name = "trainer_username")
  private String trainerUsername;

  @Field(name = "trainer_first_name")
  private String trainerFirstName;

  @Field(name = "trainer_last_name")
  private String trainerLastName;

  @Field(name = "trainer_status")
  private Boolean trainerStatus;

  private List<TrainingSessionYear> years;
}
