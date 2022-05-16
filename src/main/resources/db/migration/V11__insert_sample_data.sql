INSERT INTO public.country ("name")
VALUES ('Austria-Hungary');
INSERT INTO public.country ("name")
VALUES ('Germany');

INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('30.5 cm L45 Škoda', 305, 450, 22000, 13000, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('24 cm L45 Škoda', 238, 215, 12000, 7000, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('24 cm L40 Krupp', 238, 215, 7000, 7000, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('15 cm L50 Škoda', 149, 45, 15000, 2500, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('19 cm L42 Škoda', 190, 97, 12000, 6000, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('15 cm L40 Krupp', 149, 40, 13700, 2500, 1);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('10 cm L47 Škoda', 100, 14, 11000, 1000, 1);

INSERT INTO "rank"
(designation, precedence)
VALUES('Ensign', 1);
INSERT INTO "rank"
(designation, precedence)
VALUES('Sublieutenant', 2);
INSERT INTO "rank"
(designation, precedence)
VALUES('Lieutenant Commander', 3);
INSERT INTO "rank"
(designation, precedence)
VALUES('Commander', 4);
INSERT INTO "rank"
(designation, precedence)
VALUES('Captain', 5);
INSERT INTO "rank"
(designation, precedence)
VALUES('Commodore', 6);
INSERT INTO "rank"
(designation, precedence)
VALUES('Rear Admiral', 7);
INSERT INTO "rank"
(designation, precedence)
VALUES('Vice Admiral', 8);
INSERT INTO "rank"
(designation, precedence)
VALUES('Admiral', 9);
INSERT INTO "rank"
(designation, precedence)
VALUES('Admiral of the Navy', 10);

INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Wilhelm Student', '1882-12-12', 8, 1);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Sigmund von Bülow', '1877-11-11', 9, 1);

INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('BB', 'Battleship', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('CA', 'Heavy Cruiser', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('CL', 'Light Cruiser', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('DD', 'Destroyer', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('DE', 'Destroyer Escort', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('SS', 'Submarine', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('CV', 'Aircraft Carrier', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('CVL', 'Light Aircraft Carrier', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_precedence)
VALUES('CVE', 'Escort Aircraft Carrier', 5);

INSERT INTO ship_class
("name", displacement_in_tons, hull_classification, armor_belt_in_cms, armor_turret_in_cms, armor_deck_in_cms, speed_in_kmh, country_id)
VALUES('Viribus Unitis', 20000, 'BB', 28, 28, 5, 37, 1);

INSERT INTO ship
("name", ship_class_id, captain_id, country_id)
VALUES('Szent István', 1, 1, 1);

INSERT INTO ship_classes_and_guns
(ship_class_id, gun_id, gun_quantity)
VALUES(1, 1, 12);

INSERT INTO fleet
(designation, commander_id, country_id, minimum_rank_precedence)
VALUES('1st Battleship Fleet', 2, 1, 8);
