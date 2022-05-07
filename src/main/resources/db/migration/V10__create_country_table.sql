CREATE TABLE public.country (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	name VARCHAR(100) NOT NULL,
	flag_image_url VARCHAR(200)
);
ALTER TABLE public.gun ADD CONSTRAINT gun_country_fk FOREIGN KEY (country_id) REFERENCES public.country(id);
ALTER TABLE public.ship_class ADD CONSTRAINT ship_class_country_fk FOREIGN KEY (country_id) REFERENCES public.country(id);
ALTER TABLE public.officer ADD CONSTRAINT officer_country_fk FOREIGN KEY (country_id) REFERENCES public.country(id);
ALTER TABLE public.ship ADD CONSTRAINT ship_country_fk FOREIGN KEY (country_id) REFERENCES public.country(id);
ALTER TABLE public.fleet ADD CONSTRAINT fleet_country_fk FOREIGN KEY (country_id) REFERENCES public.country(id);
