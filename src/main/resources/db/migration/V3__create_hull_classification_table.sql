CREATE TABLE hull_classification (
    id BIGSERIAL PRIMARY KEY NOT NULL,
	abbreviation VARCHAR(4) CONSTRAINT abbreviation_must_be_unique UNIQUE NOT NULL CHECK (LENGTH(abbreviation) > 0),
	designation VARCHAR(255) CONSTRAINT hc_designation_must_be_unique UNIQUE NOT NULL CHECK (LENGTH(designation) > 0)
);
ALTER TABLE public.ship_class ADD CONSTRAINT ship_class_hull_classification_fk FOREIGN KEY (hull_classification_id) REFERENCES hull_classification(id);