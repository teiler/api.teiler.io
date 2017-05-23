TRUNCATE TABLE "group" CASCADE;

-- test-data for groups
INSERT INTO "group" VALUES ('gettgrup', 'GetGroup', 'CHF', NOW(), NOW());
INSERT INTO "group" VALUES ('puttgrup', 'EditGroup', 'CHF', NOW(), NOW());
INSERT INTO "group" VALUES ('deltgrup', 'DeleteGroup', 'CHF', NOW(), NOW());

-- test-data for people
INSERT INTO "group" VALUES ('persgrup', 'PersonGroup', 'CHF', NOW(), NOW());
INSERT INTO person VALUES (1, 'GetPerson', 'persgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (2, 'PutPerson', 'persgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (3, 'DeletePerson', 'persgrup', TRUE, NOW(), NOW());

INSERT INTO "group" VALUES ('piplgrup', 'PeopleGroup', 'CHF', NOW(), NOW());
INSERT INTO person VALUES (7, 'PeoplePerson1', 'piplgrup', TRUE, NOW(), NOW());
INSERT INTO person VALUES (8, 'PeoplePerson2', 'piplgrup', TRUE, NOW(), NOW());

INSERT INTO "group" VALUES ('spargrup', 'Spartacus', 'CHF', NOW(), NOW());
INSERT INTO person VALUES (9, 'Spartacus', TRUE, NOW(), NOW());

SELECT SETVAL('person_id_seq', (SELECT MAX(id) FROM "person")); -- DO NOT REMOVE, ELSE: TRAVIS => BUMM