CREATE TABLE public.ship (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(100) NOT NULL,
	ship_class_id BIGINT NOT NULL,
	captain_id BIGINT NOT NULL,
	country_id BIGINT NOT NULL
);
ALTER TABLE public.ship ADD CONSTRAINT ship_ship_class_fk FOREIGN KEY (ship_class_id) REFERENCES public.ship_class(id);
ALTER TABLE public.ship ADD CONSTRAINT ship_captain_fk FOREIGN KEY (captain_id) REFERENCES public.officer(id);