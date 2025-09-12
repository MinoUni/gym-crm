--liquibase formatted sql

-- changeset maksym-reva:20250711-01-init-dataset
-- init database sample dataset
INSERT INTO users(first_name, last_name, username, password, active)
VALUES
('John', 'Doe', 'John.Doe', 'vmSe2McxAS23', TRUE),
('Kevin', 'Kaslana', 'Kevin.Kaslana', 'vmSe2McxAS23', TRUE),
('Markus', 'Roderick', 'Markus.Roderick', 'vmSe2McxAS23', TRUE),
('Jane', 'Doe', 'Jane.Doe', 'vmSe2McxAS23', TRUE);

INSERT INTO trainees(id, birth_date, address)
VALUES
(1, '1978-02-05', 'Address'),
(3, '1975-01-01', 'Address');

INSERT INTO training_types(name)
VALUES ('Training_Type-1');

INSERT INTO trainers(id, specialization)
VALUES (2, 1), (4, 1);

INSERT INTO trainees_trainers(trainee_id, trainer_id)
VALUES (1, 2), (1, 4), (3, 2);

INSERT INTO trainings(name, training_date, duration, training_type_id, trainee_id, trainer_id)
VALUES
('TR-1', CURRENT_DATE, 45, 1, 1, 2),
('TR-2', CURRENT_DATE, 60, 1, 1, 4),
('TR-3', CURRENT_DATE, 30, 1, 3, 2);
