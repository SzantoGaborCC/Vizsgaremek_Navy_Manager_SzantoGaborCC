CREATE TABLE hull_classification (
    id BIGSERIAL PRIMARY KEY NOT NULL,
	abbreviation VARCHAR(3) UNIQUE NOT NULL,
	designation VARCHAR(100) UNIQUE NOT NULL,
	minimum_rank_id BIGINT NOT NULL
);
ALTER TABLE public.ship_class ADD CONSTRAINT ship_class_hull_classification_fk FOREIGN KEY (hull_classification_id) REFERENCES hull_classification(id);