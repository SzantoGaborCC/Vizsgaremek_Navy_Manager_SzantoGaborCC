CREATE TABLE public."rank" (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(100) NOT NULL,
	precedence BIGINT NOT NULL
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank_id) REFERENCES public."rank"(id);