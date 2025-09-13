package com.epam.learn.gymservice.trainee.adapter.api.rest;

import com.epam.learn.gymservice.auth.adapter.api.rest.dto.AuthCredentials;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeCreateRequest;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeProfileResponse;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeTrainingResponse;
import com.epam.learn.gymservice.trainee.adapter.api.rest.dto.TraineeUpdateRequest;
import com.epam.learn.gymservice.trainee.adapter.spi.persistence.TraineeMapper;
import com.epam.learn.gymservice.trainee.application.usecase.CreateTrainee;
import com.epam.learn.gymservice.trainee.application.usecase.DeleteTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.GetAvailableTrainersForTrainee;
import com.epam.learn.gymservice.trainee.application.usecase.GetTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.GetTraineeTrainings;
import com.epam.learn.gymservice.trainee.application.usecase.UpdateTraineeProfile;
import com.epam.learn.gymservice.trainee.application.usecase.UpdateTraineeTrainers;
import com.epam.learn.gymservice.trainee.domain.model.Trainee;
import com.epam.learn.gymservice.user.application.usecase.UserProfileStatusUpdateUseCase;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/trainees")
public class TraineeController {

  private final CreateTrainee createTrainee;
  private final GetTraineeProfile getTraineeProfile;
  private final GetAvailableTrainersForTrainee getAvailableTrainersForTrainee;
  private final UpdateTraineeProfile updateTraineeProfile;
  private final UpdateTraineeTrainers updateTraineeTrainers;
  private final DeleteTraineeProfile deleteTraineeProfile;
  private final GetTraineeTrainings getTraineeTrainings;
  private final UserProfileStatusUpdateUseCase userProfileStatusUpdate;
  private final TraineeMapper mapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AuthCredentials createTrainee(@Valid @RequestBody TraineeCreateRequest request) {
    Trainee trainee = createTrainee.execute(request);
    return AuthCredentials.of(trainee.getUser());
  }

  @GetMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public TraineeProfileResponse getTraineeProfile(@PathVariable("username") String username) {
    Trainee trainee = getTraineeProfile.execute(username);
    return mapper.toProfileResponse(trainee);
  }

  @GetMapping("/{username}/trainings")
  @ResponseStatus(HttpStatus.OK)
  public List<TraineeTrainingResponse> getTraineeTrainings(
      @PathVariable("username") String username,
      @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate from,
      @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate to,
      @RequestParam(name = "trainerName", required = false) String trainerName,
      @RequestParam(name = "trainingType", required = false) String trainingType) {
    return getTraineeTrainings.execute(username, from, to, trainerName, trainingType);
  }

  @GetMapping("/{username}/trainers/available")
  @ResponseStatus(HttpStatus.OK)
  public List<TraineeProfileResponse.TrainerProfile> getAvailableTrainers(
      @PathVariable("username") String username) {
    return getAvailableTrainersForTrainee.execute(username).stream()
        .map(mapper::toTrainerProfile)
        .toList();
  }

  @PutMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public TraineeProfileResponse updateTrainee(
      @PathVariable("username") String username, @Valid @RequestBody TraineeUpdateRequest request) {
    Trainee trainee = updateTraineeProfile.execute(username, request);
    return mapper.toProfileResponse(trainee);
  }

  @PutMapping("/{username}/trainers")
  @ResponseStatus(HttpStatus.OK)
  public List<TraineeProfileResponse.TrainerProfile> updateTraineeTrainers(
      @PathVariable("username") String username,
      @RequestParam("trainerUsernames") List<String> trainerUsernames) {
    return updateTraineeTrainers.execute(username, trainerUsernames).stream()
        .map(mapper::toTrainerProfile)
        .toList();
  }

  // ? replace [PATCH, DELETE] with <204 NO_CONTENT>
  @PatchMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public void changeTraineeProfileStatus(
      @PathVariable("username") String username, @RequestParam("isActive") boolean isActive) {
    userProfileStatusUpdate.execute(username, isActive);
  }

  @DeleteMapping("/{username}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteTrainee(@PathVariable("username") String username) {
    deleteTraineeProfile.execute(username);
  }
}
