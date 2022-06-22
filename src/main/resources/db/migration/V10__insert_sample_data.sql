INSERT INTO public.country ("name")
VALUES ('Austria-Hungary');
INSERT INTO public.country ("name")
VALUES ('Germany');
INSERT INTO public.country ("name")
VALUES ('United Kingdom');

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
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('38 cm L45 SK', 380, 750, 23200, 28000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('30.5 cm L50 SK', 305, 405, 20400, 24000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('28 cm L50 SK', 280, 300, 20900, 12000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('28 cm L45 SK', 280, 300, 18900, 12000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('28 cm L40 SK', 280, 240, 18800, 12000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('24 cm L40 SK', 240, 140, 16900, 7000, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('21 cm L40 SK', 209, 107, 16300, 6500, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('15 cm L45 SK', 149, 45, 17600, 2500, 2);
INSERT INTO gun
(designation, caliber_in_mms, projectile_weight_in_kgs, range_in_meters, minimum_ship_displacement_in_tons, country_id)
VALUES('10.5 cm L45 SK', 105, 25, 12700, 1000, 2);

INSERT INTO "rank"
(precedence, designation)
VALUES(1, 'Ensign');
INSERT INTO "rank"
(precedence, designation)
VALUES(2, 'Sublieutenant');
INSERT INTO "rank"
(precedence, designation)
VALUES(3, 'Lieutenant Commander');
INSERT INTO "rank"
(precedence, designation)
VALUES(4, 'Commander');
INSERT INTO "rank"
(precedence, designation)
VALUES(5, 'Captain');
INSERT INTO "rank"
(precedence, designation)
VALUES(6, 'Commodore');
INSERT INTO "rank"
(precedence, designation)
VALUES(7, 'Rear Admiral');
INSERT INTO "rank"
(precedence, designation)
VALUES(8, 'Vice Admiral');
INSERT INTO "rank"
(precedence, designation)
VALUES(9, 'Admiral');
INSERT INTO "rank"
(precedence, designation)
VALUES(10, 'Admiral of the Navy');

INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Sigmund von Bülow', '1872-11-12', 9, 1);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Wilhelm Student', '1875-03-11', 5, 1);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Erich Falkenstein', '1877-03-11', 10, 1);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Robert Lichter', '1857-06-11', 4, 1);

INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Wolfram Feld', '1862-11-22', 10, 2);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Kurt Möller', '1871-03-14', 6, 2);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Wilmar Dreher', '1873-03-19', 8, 2);
INSERT INTO officer
("name", date_of_birth, rank_id, country_id)
VALUES('Heinz Wegener', '1854-06-21', 7, 2);


INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('BB', 'Battleship', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('CA', 'Heavy Cruiser', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('CL', 'Light Cruiser', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('DD', 'Destroyer', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('DE', 'Destroyer Escort', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('SS', 'Submarine', 4);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('CV', 'Aircraft Carrier', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('CVL', 'Light Aircraft Carrier', 5);
INSERT INTO hull_classification
(abbreviation, designation, minimum_rank_id)
VALUES('CVE', 'Escort Aircraft Carrier', 5);

INSERT INTO ship_class
("name", displacement_in_tons, hull_classification_id, armor_belt_in_cms, armor_turret_in_cms, armor_deck_in_cms, speed_in_kmh, country_id)
VALUES('Viribus Unitis', 20000, 1, 28, 28, 5, 37, 1);
INSERT INTO ship_class
("name", displacement_in_tons, hull_classification_id, armor_belt_in_cms, armor_turret_in_cms, armor_deck_in_cms, speed_in_kmh, country_id)
VALUES('Radetzky', 14500, 1, 23, 25, 5, 38, 1);

INSERT INTO ship_class
("name", displacement_in_tons, hull_classification_id, armor_belt_in_cms, armor_turret_in_cms, armor_deck_in_cms, speed_in_kmh, country_id)
VALUES('Kaiser', 24700, 1, 35, 30, 8, 39, 2);
INSERT INTO ship_class
("name", displacement_in_tons, hull_classification_id, armor_belt_in_cms, armor_turret_in_cms, armor_deck_in_cms, speed_in_kmh, country_id)
VALUES('Braunschweig', 13200, 1, 25, 25, 4, 33, 2);

INSERT INTO ship
("name", ship_class_id, captain_id, country_id)
VALUES('Viribus Unitis', 1, 4, 1);
INSERT INTO ship
("name", ship_class_id, country_id)
VALUES('Prinz Eugen', 1, 1);
INSERT INTO ship
("name", ship_class_id, captain_id, country_id)
VALUES('Tegethoff', 1, 3, 1);
INSERT INTO ship
("name", ship_class_id, country_id)
VALUES('Szent István', 1, 1);
INSERT INTO ship
("name", ship_class_id, country_id)
VALUES('Radetzky', 2, 1);

INSERT INTO ship
("name", ship_class_id, captain_id, country_id)
VALUES('Prinzregent Luitpold', 1, 6, 2);
INSERT INTO ship
("name", ship_class_id, country_id)
VALUES('Lothringen', 2, 2);

INSERT INTO fleet
(designation, commander_id, country_id, minimum_rank_id)
VALUES('1st Battleship Fleet Pula', 1, 1, 8);

INSERT INTO fleet
(designation, commander_id, country_id, minimum_rank_id)
VALUES('1st Battleship Fleet Kiel', 5, 2, 8);

INSERT INTO gun_installation
(ship_class_id, gun_id, gun_quantity)
VALUES(1, 1, 12);

INSERT INTO gun_installation
(ship_class_id, gun_id, gun_quantity)
VALUES(3, 9, 10);

