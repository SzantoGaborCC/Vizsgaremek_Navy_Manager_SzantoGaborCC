CREATE TABLE hull_classification (
	abbreviation VARCHAR(3) PRIMARY KEY NOT NULL,
	designation VARCHAR(100) NOT NULL,
	minimum_rank_precedence INTEGER NOT NULL
);
ALTER TABLE public.ship_class ADD CONSTRAINT ship_class_hull_classification_fk FOREIGN KEY (hull_classification) REFERENCES hull_classification(abbreviation);