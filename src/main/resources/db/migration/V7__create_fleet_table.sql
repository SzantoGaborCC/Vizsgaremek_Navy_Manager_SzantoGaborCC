CREATE TABLE public.fleet (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(255) NOT NULL CONSTRAINT check_designation_minimum_length CHECK (char_length(designation) >= 1),
	minimum_rank_id BIGINT NOT NULL,
	commander_id BIGINT,
	country_id BIGINT NOT NULL,
	CONSTRAINT fleet_commander_fk FOREIGN KEY (commander_id) REFERENCES public.officer(id),
    CONSTRAINT fleet_minimum_rank_fk FOREIGN KEY (minimum_rank_id) REFERENCES public.rank(id)
);
ALTER TABLE public.ship ADD CONSTRAINT ship_fleet_fk FOREIGN KEY (fleet_id) REFERENCES public.fleet(id);