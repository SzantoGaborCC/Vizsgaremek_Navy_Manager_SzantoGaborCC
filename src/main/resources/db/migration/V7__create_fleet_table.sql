CREATE TABLE public.fleet (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(255) NOT NULL,
	minimum_rank_precedence INTEGER NOT NULL,
	commander_id BIGINT NOT NULL,
	country_id BIGINT NOT NULL,
	CONSTRAINT fleet_commander_fk FOREIGN KEY (commander_id) REFERENCES public.officer(id),
    CONSTRAINT fleet_minimum_rank_precedence_fk FOREIGN KEY (minimum_rank_precedence) REFERENCES public.rank(precedence)
);