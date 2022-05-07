CREATE TABLE public.fleets_and_ships (
	fleet_id BIGINT NOT NULL,
	ship_id BIGINT NOT NULL,
	CONSTRAINT fleets_and_ships_pk PRIMARY KEY (fleet_id,ship_id),
	CONSTRAINT fleets_and_ships_fleet_fk FOREIGN KEY (fleet_id) REFERENCES public.fleet(id),
	CONSTRAINT fleets_and_ships_ship_fk FOREIGN KEY (ship_id) REFERENCES public.ship(id)
);