CREATE TABLE training_sessions
(
    id                 BIGINT       NOT NULL,
    trainer_username   VARCHAR(255) NOT NULL,
    trainer_first_name VARCHAR(255) NULL,
    trainer_last_name  VARCHAR(255) NULL,
    is_active          BIT(1)       NOT NULL,
    training_date      date         NOT NULL,
    training_duration  time         NOT NULL,
    CONSTRAINT pk_training_sessions PRIMARY KEY (id)
);
