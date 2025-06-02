-- SCHEMA: assessment

-- DROP SCHEMA IF EXISTS assessment ;

CREATE SCHEMA IF NOT EXISTS assessment
    AUTHORIZATION postgres;

-- Type: task_priority

-- DROP TYPE IF EXISTS assessment.task_priority;

CREATE TYPE assessment.task_priority AS ENUM
    ('LOW', 'MEDIUM', 'HIGH');

ALTER TYPE assessment.task_priority
    OWNER TO postgres;

-- Table: assessment.tasks

-- DROP TABLE IF EXISTS assessment.tasks;

CREATE TABLE IF NOT EXISTS assessment.tasks
(
    id uuid NOT NULL,
    title character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description text COLLATE pg_catalog."default",
    due_date date NOT NULL,
    completed boolean NOT NULL DEFAULT false,
    priority assessment.task_priority,
    CONSTRAINT tasks_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS assessment.tasks
    OWNER to postgres;