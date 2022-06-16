CREATE TABLE public.gun_installation (
    id BIGSERIAL PRIMARY KEY,
	ship_class_id BIGINT NOT NULL,
	gun_id BIGINT NOT NULL,
	gun_quantity INTEGER NOT NULL CHECK (gun_quantity > 0),
	UNIQUE (ship_class_id,gun_id),
	CONSTRAINT gun_installation_ship_class_fk FOREIGN KEY (ship_class_id) REFERENCES public.ship_class(id),
	CONSTRAINT gun_installation_gun_fk FOREIGN KEY (gun_id) REFERENCES public.gun(id)
);