CREATE TABLE public.ships_and_guns (
	ship_id BIGINT NOT NULL,
	gun_id BIGINT NOT NULL,
	gun_quantity INTEGER NOT NULL,
	CONSTRAINT ships_and_guns_pk PRIMARY KEY (ship_id,gun_id),
	CONSTRAINT ships_and_guns_ship_fk FOREIGN KEY (ship_id) REFERENCES public.ship(id),
	CONSTRAINT ship_and_guns_gun_fk FOREIGN KEY (gun_id) REFERENCES public.gun(id)
);