INSERT INTO "group" VALUES ('gettgrup', 'GetGroup', 'CHF', NOW(), NOW());
INSERT INTO "group" VALUES ('puttgrup', 'EditGroup', 'CHF', NOW(), NOW());
INSERT INTO "group" VALUES ('deltgrup', 'DeleteGroup', 'CHF', NOW(), NOW());

INSERT INTO "group" VALUES ('persgrup', 'PersonGroup', 'CHF', NOW(), NOW());
INSERT INTO person VALUES (1, 'GetPerson', 'persgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (2, 'PutPerson', 'persgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (3, 'DeletePerson', 'persgrup', TRUE, NOW(), NOW());

INSERT INTO "group" VALUES ('piplgrup', 'PeopleGroup', 'CHF', NOW(), NOW());
INSERT INTO person VALUES (7, 'PeoplePerson1', 'piplgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (8, 'PeoplePerson2', 'piplgrup', TRUE, NOW(), NOW());
