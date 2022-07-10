CREATE TABLE public.ship (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	ship_class_id BIGINT NOT NULL,
	captain_id BIGINT,
	country_id BIGINT NOT NULL,
	fleet_id BIGINT,
    CONSTRAINT each_ship_class_unique_ship_name UNIQUE ("name", ship_class_id)
);
ALTER TABLE public.ship ADD CONSTRAINT ship_ship_class_fk FOREIGN KEY (ship_class_id) REFERENCES public.ship_class(id) ON DELETE CASCADE;
ALTER TABLE public.ship ADD CONSTRAINT ship_captain_fk FOREIGN KEY (captain_id) REFERENCES public.officer(id) ON DELETE SET NULL;