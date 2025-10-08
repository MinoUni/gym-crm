CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS trainees
(
    id         BIGINT,
    birth_date DATE,
    address    VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_trainee_user FOREIGN KEY (id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS training_types
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY,
    name   VARCHAR(255) NOT NULL,
    values TEXT[] DEFAULT ARRAY ['remote', 'onsite', 'hybrid'],
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS trainers
(
    id             BIGINT,
    specialization BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_trainer_user FOREIGN KEY (id) REFERENCES users (id),
    CONSTRAINT fk_trainer_training_type FOREIGN KEY (specialization) REFERENCES training_types (id)
);

CREATE TABLE IF NOT EXISTS trainees_trainers
(
    trainee_id BIGINT,
    trainer_id BIGINT,
    PRIMARY KEY (trainee_id, trainer_id),
    CONSTRAINT fk_trainees_trainers_trainee FOREIGN KEY (trainee_id) REFERENCES trainees (id),
    CONSTRAINT fk_trainees_trainers_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id)
);

CREATE TABLE IF NOT EXISTS trainings
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY,
    name             VARCHAR(255) NOT NULL,
    training_date    DATE         NOT NULL,
    duration         SMALLINT     NOT NULL,
    training_type_id BIGINT       NOT NULL,
    trainee_id       BIGINT,
    trainer_id       BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_trainings_training_type FOREIGN KEY (training_type_id) REFERENCES training_types (id),
    CONSTRAINT fk_trainings_trainee FOREIGN KEY (trainee_id) REFERENCES trainees (id),
    CONSTRAINT fk_trainings_trainer FOREIGN KEY (trainer_id) REFERENCES trainers (id)
);
