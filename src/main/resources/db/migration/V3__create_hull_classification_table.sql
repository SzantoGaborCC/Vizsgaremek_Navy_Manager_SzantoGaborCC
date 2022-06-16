CREATE TABLE hull_classification (
    id BIGSERIAL PRIMARY KEY NOT NULL,
	abbreviation VARCHAR(4) UNIQUE NOT NULL CHECK (LENGTH(abbreviation) > 0),
	designation VARCHAR(255) UNIQUE NOT NULL CHECK (LENGTH(designation) > 0),
	minimum_rank_id BIGINT NOT NULL
);
ALTER TABLE public.ship_class ADD CONSTRAINT ship_class_hull_classification_fk FOREIGN KEY (hull_classification_id) REFERENCES hull_classification(id);