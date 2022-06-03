CREATE TABLE public.ship_classes_and_guns (
    id BIGSERIAL PRIMARY KEY,
	ship_class_id BIGINT NOT NULL,
	gun_id BIGINT NOT NULL,
	gun_quantity INTEGER NOT NULL,
	UNIQUE (ship_class_id,gun_id),
	CONSTRAINT ship_classes_and_guns_ship_class_fk FOREIGN KEY (ship_class_id) REFERENCES public.ship_class(id),
	CONSTRAINT ship_classes_and_guns_gun_fk FOREIGN KEY (gun_id) REFERENCES public.gun(id)
);