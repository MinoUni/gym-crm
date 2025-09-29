package com.epam.learn.gymservice.trainer.adapter.api.rest;

import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerCreateRequest;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerProfileResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerTrainingResponse;
import com.epam.learn.gymservice.trainer.adapter.api.rest.dto.TrainerUpdateRequest;
import com.epam.learn.gymservice.trainer.adapter.spi.persistence.TrainerMapper;
import com.epam.learn.gymservice.trainer.application.usecase.CreateTrainer;
import com.epam.learn.gymservice.trainer.application.usecase.GetTrainerProfile;
import com.epam.learn.gymservice.trainer.application.usecase.GetTrainerTrainings;
import com.epam.learn.gymservice.trainer.application.usecase.UpdateTrainerProfile;
import com.epam.learn.gymservice.trainer.domain.model.Trainer;
import com.epam.learn.gymservice.user.application.usecase.UserProfileStatusUpdateUseCase;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/trainers")
public class TrainerController {

  private final TrainerMapper mapper;
  private final CreateTrainer createTrainer;
  private final GetTrainerProfile getTrainerProfile;
  private final GetTrainerTrainings getTrainerTrainings;
  private final UpdateTrainerProfile updateTrainerProfile;
  private final UserProfileStatusUpdateUseCase userProfileStatusUpdate;

  @GetMapping("/{username}/trainings")
  @ResponseStatus(HttpStatus.OK)
  public List<TrainerTrainingResponse> getTrainerTrainings(
      @PathVariable("username") String username,
      @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate from,
      @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate to,
      @RequestParam(name = "traineeNameLike", required = false) String traineeName) {
    return getTrainerTrainings.execute(username, from, to, traineeName);
  }

  @GetMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public TrainerProfileResponse getTrainerProfile(@PathVariable("username") String username) {
    Trainer trainerProfile = getTrainerProfile.execute(username);
    return mapper.toProfileResponse(trainerProfile);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createTrainer(@Valid @RequestBody TrainerCreateRequest request) {
    createTrainer.execute(request);
  }

  @PutMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public TrainerProfileResponse updateTrainer(
      @PathVariable("username") String username, @Valid @RequestBody TrainerUpdateRequest request) {
    Trainer trainerProfile = updateTrainerProfile.execute(username, request);
    return mapper.toProfileResponse(trainerProfile);
  }

  @PatchMapping("/{username}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeTraineeProfileStatus(
      @PathVariable("username") String username, @RequestParam("isActive") boolean isActive) {
    userProfileStatusUpdate.execute(username, isActive);
  }
}
