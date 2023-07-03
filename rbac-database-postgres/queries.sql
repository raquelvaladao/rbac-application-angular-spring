DROP TABLE IF EXISTS USER_TYPE CASCADE;
CREATE TABLE USER_TYPE(
    userType CHAR(13),
    PRIMARY KEY (userType)
);

DROP TABLE IF EXISTS USERS CASCADE;
CREATE TABLE USERS(
    userId SERIAL PRIMARY KEY,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    type CHAR(13) NOT NULL,
    idOriginal INTEGER
);
ALTER TABLE USERS ADD CONSTRAINT fk_user_type FOREIGN KEY (type) REFERENCES USER_TYPE(userType);

INSERT INTO USER_TYPE (userType)
VALUES ('ADMINISTRADOR');
INSERT INTO USER_TYPE (userType)
VALUES ('PILOTO');
INSERT INTO USER_TYPE (userType)
VALUES ('ESCUDERIA');

DROP TABLE IF EXISTS LOG_TABLE CASCADE;
CREATE TABLE Log_Table (
  id SERIAL PRIMARY KEY,
  userid INTEGER,
  datetime TIMESTAMP
);

DROP TABLE IF EXISTS LOG_TABLE CASCADE;
CREATE TABLE LOG_TABLE (
  id SERIAL PRIMARY KEY,
  userid INTEGER,
  datetime TIMESTAMP
);

ALTER TABLE LOG_TABLE ADD CONSTRAINT fk_user_id FOREIGN KEY (userid) REFERENCES USERS(userid);

CREATE OR REPLACE FUNCTION InsertConstructorsAndDriversAsUsers()
RETURNS VOID AS $$
DECLARE
  constructor_row Constructors%ROWTYPE;
  driver_row Driver%ROWTYPE;
BEGIN
  -- Inserir registros da tabela Constructors
  FOR constructor_row IN SELECT * FROM Constructors LOOP
    INSERT INTO users (login, password, type, IdOriginal)
    VALUES (constructor_row.constructorref || '_c', MD5(constructor_row.constructorref), 'ESCUDERIA', constructor_row.ConstructorId);
  END LOOP;

  -- Inserir registros da tabela Driver
  FOR driver_row IN SELECT * FROM Driver LOOP
    INSERT INTO users (login, password, type, IdOriginal)
    VALUES (driver_row.driverref || '_d', MD5(driver_row.driverref), 'PILOTO', driver_row.DriverId);
  END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT InsertConstructorsAndDriversAsUsers();



-- Trigger para a tabela Driver: assim que insere Driver, insere User
CREATE TRIGGER InsertDriversTrigger
AFTER INSERT ON Driver
FOR EACH ROW
EXECUTE FUNCTION InsertDriversAsUsers();

-- Função para inserir registros da tabela Driver na tabela users (pelo trigger)
CREATE OR REPLACE FUNCTION InsertDriversAsUsers()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO users (login, password, type, IdOriginal)
  VALUES (NEW.driverref || '_d', MD5(NEW.driverref), 'PILOTO', NEW.DriverId);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger de insert para a tabela Constructors para inserir em Users
CREATE TRIGGER InsertConstructorsTrigger
AFTER INSERT ON Constructors
FOR EACH ROW
EXECUTE FUNCTION InsertConstructorsAsUsers();

-- Função para inserir registros da tabela Constructors na tabela users
CREATE OR REPLACE FUNCTION InsertConstructorsAsUsers()
RETURNS TRIGGER AS $$
BEGIN
  INSERT INTO users (login, password, type, IdOriginal)
  VALUES (NEW.constructorref || '_c', MD5(NEW.constructorref), 'ESCUDERIA', NEW.ConstructorId);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger e function para atualizar registros na tabela "users" quando um registro em "Driver" for modificado
CREATE OR REPLACE FUNCTION UpdateUsersOnDriverUpdate()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE users
  SET login = NEW.driverref || '_d',
      password = MD5(NEW.driverref)
  WHERE IdOriginal = NEW.DriverId AND type = 'PILOTO';

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER UpdateUsersOnDriverUpdateTrigger
AFTER UPDATE ON Driver
FOR EACH ROW
EXECUTE FUNCTION UpdateUsersOnDriverUpdate();

-- Trigger para excluir registros da tabela "users" quando um registro em "Driver" for excluído
CREATE OR REPLACE FUNCTION DeleteUsersOnDriverDelete()
RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM users WHERE IdOriginal = OLD.DriverId  AND type = 'PILOTO';

  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER DeleteUsersOnDriverDeleteTrigger
AFTER DELETE ON Driver
FOR EACH ROW
EXECUTE FUNCTION DeleteUsersOnDriverDelete();

-- Trigger para atualizar registros na tabela "users" quando um registro em "Constructors" for modificado
CREATE OR REPLACE FUNCTION UpdateUsersOnConstructorsUpdate()
RETURNS TRIGGER AS $$
BEGIN
  UPDATE users
  SET login = NEW.constructorref || '_c',
      password = MD5(NEW.constructorref)
  WHERE IdOriginal = NEW.ConstructorId AND type = 'ESCUDERIA';

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER UpdateUsersOnConstructorsUpdateTrigger
AFTER UPDATE ON Constructors
FOR EACH ROW
EXECUTE FUNCTION UpdateUsersOnConstructorsUpdate();

-- Trigger para excluir registros da tabela "users" quando um registro em "Constructors" for excluído
CREATE OR REPLACE FUNCTION DeleteUsersOnConstructorsDelete()
RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM users WHERE IdOriginal = OLD.ConstructorId AND type = 'ESCUDERIA';

  RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER DeleteUsersOnConstructorsDeleteTrigger
AFTER DELETE ON Constructors
FOR EACH ROW
EXECUTE FUNCTION DeleteUsersOnConstructorsDelete();

INSERT INTO USERS (login, password, type, IdOriginal) VALUES ('admin', MD5('admin'), 'ADMINISTRADOR', null);

-- TEAM REPORTS
SELECT 
	CONCAT(D.Forename, ' ', D.Surname) AS pilot_name, 
	COUNT(*) AS victories_on_team
FROM Results R
JOIN Driver D ON R.DriverId = D.DriverId
WHERE R.ConstructorId = 1 AND R.Position = 1 -- se já correu pela escuderia e se houve vitória
GROUP BY D.Forename, D.Surname				 -- contagem das vitórias daquele piloto
ORDER BY victories_on_team DESC;

SELECT 
	S.Status as status, 
	COUNT(*) AS quantity
FROM Results R
JOIN Status S ON R.StatusId = S.StatusId	
WHERE R.ConstructorId = 1
GROUP BY S.Status
ORDER BY quantity DESC;


-- PILOT REPORTS

-- o rollup usa o nome da corrida ao invés de id pois corridas sem nome não interessam pro nosso sistema
--pusemos YEAR como primeiro parâmetro do rollup, assim conseguimos todas as vitórias por ano (ano fixado), o número total e o número daquele ano e corrida (que é sempre 1)
SELECT
	COUNT(*) AS quantidade,
	YEAR as ano,
	R.Name as corrida,
	CASE
        WHEN YEAR IS NOT NULL AND R.Name IS NOT NULL THEN CONCAT(YEAR, '/', R.Name, ' (sempre 1)')
        WHEN YEAR IS NOT NULL AND R.Name IS NULL THEN CONCAT('Vitórias por ano')
        WHEN YEAR IS NULL AND R.Name IS NOT NULL THEN CONCAT('Em corridas do tipo ', R.Name)
        ELSE 'Total de vitórias'
    END AS descricao	-- essa é apenas uma descrição do que ocorre no rollup
FROM
    Results RS
    JOIN Races R ON RS.RaceId = R.RaceId
	JOIN driver d ON RS.driverid = D.driverid
WHERE
    RS.DriverId = 1
    AND RS.PositionText = '1'
	AND d.forename is not null
GROUP BY ROLLUP (R.Year, R.Name) 
ORDER BY R.Year ASC;

SELECT 
	S.Status as status, 
	COUNT(*) AS quantity
FROM Results R
JOIN Status S ON R.StatusId = S.StatusId	
WHERE R.DriverId = :DRIVER_ID
GROUP BY S.Status
ORDER BY quantity DESC;

-- ADMIN OPERATIONS - INSERT NEW USER
INSERT INTO USERS (login, password, type, idOriginal) VALUES (:login, MD5(:password), :type, null);

-- ADMIN OVERVIEW
-- ADMIN_OVERVIEW_TOTAL_PILOTS
SELECT COUNT(*) AS total_pilotos FROM Driver;

-- ADMIN_OVERVIEW_TOTAL_TEAMS
SELECT COUNT(*) AS total_escuderias FROM Constructors;

--ADMIN_OVERVIEW_TOTAL_RACES
SELECT COUNT(*) AS total_corridas FROM Races;

--ADMIN_OVERVIEW_TOTAL_SEASONS
SELECT COUNT(DISTINCT year) AS total_temporadas FROM Races;

--ADMIN_REPORT_POSITION_QUANTITY
SELECT position, COUNT(*) AS quantity  FROM Results GROUP BY position ORDER BY position ASC;

-- ADMIN REPORTS
SELECT s.Status, COUNT(*) AS Contagem
FROM Results r
JOIN Status s ON r.StatusId = s.StatusId
GROUP BY s.Status;

CREATE EXTENSION IF NOT EXISTS Cube;
CREATE EXTENSION IF NOT EXISTS EarthDistance;
SELECT C.Name AS city_name,
       A.IATACode AS iata_code,
       A.Name AS airport_name,
       A.Type AS airport_type,
       ROUND( 
		   CAST((earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) / 100) as numeric), 2
	   ) AS distance_km_rounded
FROM Airports A
JOIN GeoCities15K C ON A.City = C.Name
WHERE C.Name = 'Rio de Janeiro' AND
      A.Type IN ('medium_airport', 'large_airport') AND
      earth_distance(ll_to_earth(C.Lat, C.Long), ll_to_earth(A.LatDeg, A.LongDeg)) <= 100000
ORDER BY C.Name;