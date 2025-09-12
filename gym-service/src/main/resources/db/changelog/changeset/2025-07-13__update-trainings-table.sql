--liquibase formatted sql

-- changeset maksym-reva:20250713-01-update-constraints
-- drop `trainer_id`, not null constraint
ALTER TABLE trainings
ALTER COLUMN trainer_id
DROP NOT NULL;

-- changeset maksym-reva:20250713-02-update-constraints
-- drop `trainee_id` not null constraint
ALTER TABLE trainings
ALTER COLUMN trainee_id
DROP NOT NULL;
