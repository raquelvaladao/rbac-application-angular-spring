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