CREATE TABLE public."rank" (
    precedence INTEGER PRIMARY KEY NOT NULL,
	designation VARCHAR(100) NOT NULL
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank) REFERENCES public."rank"(precedence) ;
ALTER TABLE public.hull_classification ADD CONSTRAINT hull_classification_minimum_rank_precedence_fk
    FOREIGN KEY (minimum_rank_precedence) REFERENCES public."rank"(precedence);