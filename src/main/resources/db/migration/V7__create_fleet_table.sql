CREATE TABLE public.fleet (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(255) NOT NULL CONSTRAINT check_designation_minimum_length CHECK (char_length(designation) >= 1),
	commander_id BIGINT,
	country_id BIGINT NOT NULL,
	CONSTRAINT fleet_commander_fk FOREIGN KEY (commander_id) REFERENCES public.officer(id)
);
ALTER TABLE public.ship ADD CONSTRAINT ship_fleet_fk FOREIGN KEY (fleet_id) REFERENCES public.fleet(id);