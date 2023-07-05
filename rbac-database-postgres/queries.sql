-- DROP TABLE IF EXISTS USER_TYPE CASCADE;
-- CREATE TABLE USER_TYPE(
--     userType CHAR(13),
--     PRIMARY KEY (userType)
-- );

-- DROP TABLE IF EXISTS USERS CASCADE;
-- CREATE TABLE USERS(
--     userId SERIAL PRIMARY KEY,
--     login TEXT NOT NULL UNIQUE,
--     password TEXT NOT NULL,
--     type CHAR(13) NOT NULL,
--     idOriginal INTEGER
-- );
-- ALTER TABLE USERS ADD CONSTRAINT fk_user_type FOREIGN KEY (type) REFERENCES USER_TYPE(userType);

-- INSERT INTO USER_TYPE (userType)
-- VALUES ('ADMINISTRADOR');
-- INSERT INTO USER_TYPE (userType)
-- VALUES ('PILOTO');
-- INSERT INTO USER_TYPE (userType)
-- VALUES ('ESCUDERIA');

-- DROP TABLE IF EXISTS LOG_TABLE CASCADE;
-- CREATE TABLE Log_Table (
--   id SERIAL PRIMARY KEY,
--   userid INTEGER,
--   datetime TIMESTAMP
-- );

-- DROP TABLE IF EXISTS LOG_TABLE CASCADE;
-- CREATE TABLE LOG_TABLE (
--   id SERIAL PRIMARY KEY,
--   userid INTEGER,
--   datetime TIMESTAMP
-- );

-- ALTER TABLE LOG_TABLE ADD CONSTRAINT fk_user_id FOREIGN KEY (userid) REFERENCES USERS(userid);

-- CREATE OR REPLACE FUNCTION InsertConstructorsAndDriversAsUsers()
-- RETURNS VOID AS $$
-- DECLARE
--   constructor_row Constructors%ROWTYPE;
--   driver_row Driver%ROWTYPE;
-- BEGIN
--   -- Inserir registros da tabela Constructors
--   FOR constructor_row IN SELECT * FROM Constructors LOOP
--     INSERT INTO users (login, password, type, IdOriginal)
--     VALUES (constructor_row.constructorref || '_c', MD5(constructor_row.constructorref), 'ESCUDERIA', constructor_row.ConstructorId);
--   END LOOP;

--   -- Inserir registros da tabela Driver
--   FOR driver_row IN SELECT * FROM Driver LOOP
--     INSERT INTO users (login, password, type, IdOriginal)
--     VALUES (driver_row.driverref || '_d', MD5(driver_row.driverref), 'PILOTO', driver_row.DriverId);
--   END LOOP;
-- END;
-- $$ LANGUAGE plpgsql;

-- SELECT InsertConstructorsAndDriversAsUsers();



-- -- Trigger para a tabela Driver: assim que insere Driver, insere User
-- CREATE TRIGGER InsertDriversTrigger
-- AFTER INSERT ON Driver
-- FOR EACH ROW
-- EXECUTE FUNCTION InsertDriversAsUsers();

-- -- Função para inserir registros da tabela Driver na tabela users (pelo trigger)
-- CREATE OR REPLACE FUNCTION InsertDriversAsUsers()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   INSERT INTO users (login, password, type, IdOriginal)
--   VALUES (NEW.driverref || '_d', MD5(NEW.driverref), 'PILOTO', NEW.DriverId);
--   RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- -- Trigger de insert para a tabela Constructors para inserir em Users
-- CREATE TRIGGER InsertConstructorsTrigger
-- AFTER INSERT ON Constructors
-- FOR EACH ROW
-- EXECUTE FUNCTION InsertConstructorsAsUsers();

-- -- Função para inserir registros da tabela Constructors na tabela users
-- CREATE OR REPLACE FUNCTION InsertConstructorsAsUsers()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   INSERT INTO users (login, password, type, IdOriginal)
--   VALUES (NEW.constructorref || '_c', MD5(NEW.constructorref), 'ESCUDERIA', NEW.ConstructorId);
--   RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- -- Trigger e function para atualizar registros na tabela "users" quando um registro em "Driver" for modificado
-- CREATE OR REPLACE FUNCTION UpdateUsersOnDriverUpdate()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   UPDATE users
--   SET login = NEW.driverref || '_d',
--       password = MD5(NEW.driverref)
--   WHERE IdOriginal = NEW.DriverId AND type = 'PILOTO';

--   RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE OR REPLACE TRIGGER UpdateUsersOnDriverUpdateTrigger
-- AFTER UPDATE ON Driver
-- FOR EACH ROW
-- EXECUTE FUNCTION UpdateUsersOnDriverUpdate();

-- -- Trigger para excluir registros da tabela "users" quando um registro em "Driver" for excluído
-- CREATE OR REPLACE FUNCTION DeleteUsersOnDriverDelete()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   DELETE FROM users WHERE IdOriginal = OLD.DriverId  AND type = 'PILOTO';

--   RETURN OLD;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE OR REPLACE TRIGGER DeleteUsersOnDriverDeleteTrigger
-- AFTER DELETE ON Driver
-- FOR EACH ROW
-- EXECUTE FUNCTION DeleteUsersOnDriverDelete();

-- -- Trigger para atualizar registros na tabela "users" quando um registro em "Constructors" for modificado
-- CREATE OR REPLACE FUNCTION UpdateUsersOnConstructorsUpdate()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   UPDATE users
--   SET login = NEW.constructorref || '_c',
--       password = MD5(NEW.constructorref)
--   WHERE IdOriginal = NEW.ConstructorId AND type = 'ESCUDERIA';

--   RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE OR REPLACE TRIGGER UpdateUsersOnConstructorsUpdateTrigger
-- AFTER UPDATE ON Constructors
-- FOR EACH ROW
-- EXECUTE FUNCTION UpdateUsersOnConstructorsUpdate();

-- -- Trigger para excluir registros da tabela "users" quando um registro em "Constructors" for excluído
-- CREATE OR REPLACE FUNCTION DeleteUsersOnConstructorsDelete()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   DELETE FROM users WHERE IdOriginal = OLD.ConstructorId AND type = 'ESCUDERIA';

--   RETURN OLD;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE OR REPLACE TRIGGER DeleteUsersOnConstructorsDeleteTrigger
-- AFTER DELETE ON Constructors
-- FOR EACH ROW
-- EXECUTE FUNCTION DeleteUsersOnConstructorsDelete();

-- INSERT INTO USERS (login, password, type, IdOriginal) VALUES ('admin', MD5('admin'), 'ADMINISTRADOR', null);

-- CREATE OR REPLACE FUNCTION atualizar_serial(seq regclass, nome_tabela regclass, nome_coluna_id text)
-- RETURNS VOID AS $$
-- DECLARE
--     id_maximo INTEGER;
-- BEGIN
--     EXECUTE format('SELECT MAX(%I) FROM %I', nome_coluna_id, nome_tabela) INTO id_maximo;
--     EXECUTE format('SELECT setval(%L, %s)', seq, id_maximo + 1);
-- END;
-- $$ LANGUAGE plpgsql;


-- select atualizar_serial('driver_driverid_seq', 'driver', 'driverid');
-- select atualizar_serial('constructors_constructorid_seq', 'constructors', 'constructorid');

-- TEAM SEARCH
-- SELECT CONCAT(D.Forename, ' ', D.Surname) AS full_name, D.DOB AS date_of_birth, D.Nationality
-- FROM Driver D
-- JOIN Results R ON D.DriverId = R.DriverId
-- JOIN Constructors C ON R.ConstructorId = C.ConstructorId
-- WHERE D.Forename = :FORENAME
--   AND C.ConstructorId = :TEAM;

-- -- TEAM REPORTS  -- escuderia com contagem 0
-- SELECT
-- CONCAT(D.Forename, ' ', D.Surname) AS pilot_name,
-- COUNT(CASE WHEN R.Position = 1 THEN 1 ELSE NULL END) AS victories
-- FROM Driver D
-- JOIN Results R ON D.DriverId = R.DriverId
-- WHERE R.ConstructorId = :TEAM_ID
-- GROUP BY D.DriverId, D.Forename, D.Surname
-- ORDER BY victories DESC;

-- SELECT
-- 	S.Status as status, 
-- 	COUNT(*) AS quantity
-- FROM Results R
-- JOIN Status S ON R.StatusId = S.StatusId	
-- WHERE R.ConstructorId = :TEAM_ID
-- GROUP BY S.Status
-- ORDER BY quantity DESC;

-- team overview
-- CREATE OR REPLACE FUNCTION team_victories_quantity(id_escuderia INT)
--   RETURNS INT AS
-- $$
-- DECLARE
--   victories INT;
-- BEGIN
--   SELECT COUNT(*) INTO victories
--   FROM Results
--   WHERE ConstructorId = id_escuderia AND Position = 1;

--   RETURN victories;
-- END;
-- $$
-- LANGUAGE plpgsql;

-- CREATE OR REPLACE FUNCTION disctinct_pilots_quantity(id_escuderia INT)
--   RETURNS INT AS
-- $$
-- DECLARE
--   quantity INT;
-- BEGIN
--   SELECT COUNT(DISTINCT DriverId) INTO quantity
--   FROM Results
--   WHERE ConstructorId = id_escuderia;

--   RETURN quantity;
-- END;
-- $$
-- LANGUAGE plpgsql;

-- CREATE OR REPLACE FUNCTION team_first_last_year_data(id_escuderia INT)
--   RETURNS TEXT AS
-- $$
-- DECLARE
--   first_year INT;
--   last_year INT;
-- BEGIN
--   SELECT MIN(Races.Year), MAX(Races.Year) 
--   INTO first_year, last_year
--   FROM Races
--   JOIN Results ON Races.RaceId = Results.RaceId
--   WHERE Results.ConstructorId = id_escuderia;

--   RETURN CONCAT(first_year, ',', last_year);
-- END;
-- $$
-- LANGUAGE plpgsql;


-- -- PILOT REPORTS

-- -- o rollup usa o nome da corrida ao invés de id pois corridas sem nome não interessam pro nosso sistema
-- --pusemos YEAR como primeiro parâmetro do rollup, assim conseguimos todas as vitórias por ano (ano fixado), o número total e o número daquele ano e corrida (que é sempre 1)
-- SELECT
-- 	COUNT(*) AS quantidade,
-- 	YEAR as ano,
-- 	R.Name as corrida,
-- FROM Results RS
-- JOIN Races R ON RS.RaceId = R.RaceId
-- JOIN driver d ON RS.driverid = D.driverid
-- WHERE
--     RS.DriverId = :DRIVER_ID
--     AND RS.PositionText = '1'
-- GROUP BY ROLLUP (R.Year, R.Name) 
-- ORDER BY R.Year ASC;

-- SELECT S.status, COUNT(*) AS quantidade_resultados
-- FROM RESULTS
-- JOIN STATUS S ON S.STATUSID = RESULTS.STATUSID
-- WHERE driverId = :DRIVER_ID
-- GROUP BY S.statusId
-- ORDER BY quantidade_resultados DESC;

-- overview pilot

-- CREATE OR REPLACE FUNCTION get_first_and_last_race_years_from_pilot(idParam INTEGER)
-- RETURNS TEXT AS $$
-- DECLARE
--   firstYear INTEGER;
--   lastYear INTEGER;
-- BEGIN
--   SELECT MIN(year), MAX(year)
--   INTO firstYear, lastYear
--   FROM results
-- 	JOIN races ON results.raceid = races.raceid
-- 	WHERE results.driverid = idParam;

--   RETURN CONCAT(firstYear, ',', lastYear);
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE OR REPLACE FUNCTION get_pilot_victories(idParam INTEGER)
-- RETURNS INTEGER AS $$
-- DECLARE
--   quantity INTEGER;
-- BEGIN
--   SELECT COUNT(*)
--   INTO quantity
--   FROM RESULTS
--   WHERE driverId = idParam
--     AND rank = 1; 

--   RETURN quantity;
-- END;
-- $$ LANGUAGE plpgsql;

-- SELECT S.status, COUNT(*) AS quantidade_resultados
-- FROM RESULTS
-- JOIN STATUS S ON S.STATUSID = RESULTS.STATUSID
-- WHERE driverId = :DRIVER_ID 
-- GROUP BY S.statusId
-- ORDER BY quantidade_resultados DESC;


-- -- ADMIN OPERATIONS - INSERT NEW USER
-- INSERT INTO USERS (login, password, type, idOriginal) VALUES (:login, MD5(:password), :type, null);

-- -- ADMIN OVERVIEW
-- -- ADMIN_OVERVIEW_TOTAL_PILOTS
-- SELECT COUNT(*) AS total_pilotos FROM Driver;

-- -- ADMIN_OVERVIEW_TOTAL_TEAMS
-- SELECT COUNT(*) AS total_escuderias FROM Constructors;

-- --ADMIN_OVERVIEW_TOTAL_RACES
-- SELECT COUNT(*) AS total_corridas FROM Races;

-- --ADMIN_OVERVIEW_TOTAL_SEASONS
-- SELECT COUNT(DISTINCT year) AS total_temporadas FROM Races;

-- -- ADMIN REPORTS
-- SELECT S.Status as status, COUNT(*) AS quantity
-- FROM Status S
-- LEFT JOIN Results R ON R.StatusId = S.StatusId	 --aparecer tbm os que não tem, com LEFT join
-- GROUP BY S.Status
-- ORDER BY quantity DESC;

-- SELECT C.Name AS city_name,
--        A.IATACode AS iata_code,
--        A.Name AS airport_name,
--        A.Type AS airport_type,
--        ROUND( 
-- 		   CAST((earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) / 1000) as numeric), 2
-- 	   ) AS distance_km_rounded
-- FROM Airports A
-- JOIN GeoCities15K C ON 
-- 	ROUND( 
-- 		   CAST((earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) / 1000) as numeric), 2
-- 	   ) <= 100
-- WHERE C.Name = :CITY_NAME AND
--       A.Type IN ('medium_airport', 'large_airport') 
-- ORDER BY distance_km_rounded;