CREATE FUNCTION elimina_alimento_dieta(momento VARCHAR, usuario VARCHAR)
 RETURNS void AS $$
    DECLARE
        fecha_actual DATE;
    BEGIN
	fecha_actual := current_date;
        DELETE FROM DIET
        WHERE momentdescription = momento and DIETDATETIME = fecha_actual;
    END; $$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_alimentos_person(usuario VARCHAR)
  RETURNS TABLE(nombre_comida VARCHAR, peso_comida INT, calorias_comida INT)
   AS $$
DECLARE
   var_r	record;
   fecha_actual	DATE;
BEGIN
   fecha_actual := (SELECT current_date);
   FOR var_r IN(SELECT  FOODNAME, FOODWEIGHT, FOODCALORIE
		FROM PERSON inner join  DIET on personid = fk_personid inner join FOOD on fk_foodid = foodid
		WHERE FOODPERSONALIZED = TRUE AND personusername = usuario AND DIETDATETIME = fecha_actual)
   LOOP
	nombre_comida := var_r.FOODNAME;
	peso_comida := var_r.FOODWEIGHT;
	calorias_comida := var_r.FOODCALORIE;
	RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_calorias_fecha(
    fecha DATE,
    usuario VARCHAR)
  RETURNS TABLE(calorias integer) 
  AS $$
DECLARE
   var_r record;
BEGIN
   FOR var_r IN (SELECT SUM(DIETCALORIE) AS suma
	       FROM PERSON, DIET
	       WHERE PERSONID = FK_PERSONID AND DIETDATETIME = fecha AND PERSONUSERNAME = usuario)
   LOOP
          calorias := var_r.suma;
	  RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_comida_momento(
     momento VARCHAR,
     fecha DATE,
     usuario VARCHAR)
  RETURNS TABLE(nombre character varying, calorias integer) 
  AS $$
DECLARE
  var_r	record;
BEGIN
   FOR var_r IN(SELECT FOODNAME, DIETCALORIE
             FROM moment, PERSON inner join  DIET on personid = fk_personid inner join FOOD on fk_foodid = foodid
	     WHERE fk_momentid = momentid and momentdescription = momento and DIETDATETIME = fecha and
	     PERSONUSERNAME = usuario)
   LOOP
	nombre := var_r.FOODNAME;
	calorias := var_r.DIETCALORIE;
	RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION inserta_alim_person(
    nombre VARCHAR,
    peso INT,
    calorias INT)
  RETURNS void 
  AS $$
    BEGIN
        INSERT INTO FOOD (FOODNAME, FOODWEIGHT, FOODCALORIE, FOODPERSONALIZED) VALUES (nombre, peso, calorias, true);
    END; $$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION inserta_dieta(
    caloria INT,
    nombre_alimento VARCHAR,
    momento VARCHAR,
    ususario VARCHAR)
  RETURNS void
   AS $$
    DECLARE
        fecha_actual DATE;
    BEGIN
	fecha_actual  := current_date;
        INSERT INTO DIET (DIETCALORIE, DIETDATETIME, fk_foodid, fk_momentid, fk_personid) VALUES
        (caloria, fecha_actual, (SELECT FOODID FROM FOOD WHERE FOODNAME = nombre_alimento), 
        (SELECT MOMENTID FROM MOMENT WHERE MOMENTDESCRIPTION = momento), 
        (SELECT PERSONID FROM PERSON WHERE personusername = usuario) );
    END; $$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_calorias_dia(
usuario VARCHAR)
  RETURNS TABLE(calorias INT) 
  AS $$
DECLARE
   fecha_inicio date;
   fecha_fin	date;
   var_r record;
BEGIN
   fecha_fin := current_date;
   fecha_inicio := fecha_fin - 6;
   FOR var_r IN (SELECT SUM(DIETCALORIE) AS suma
	       FROM PERSON, DIET
	       WHERE PERSONID = FK_PERSONID AND DIETDATETIME  BETWEEN fecha_inicio AND fecha_fin AND PERSONUSERNAME = usuario)
   LOOP
          calorias := var_r.suma;
	  RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql

CREATE OR REPLACE FUNCTION get_calorias_mes(
    IN usuario character varying,
    IN fecha_inicio date,
    IN fecha_fin date)
  RETURNS TABLE(calorias integer) AS
$BODY$
DECLARE
   var_r record;
BEGIN
   FOR var_r IN (SELECT SUM(DIETCALORIE) AS suma
	       FROM PERSON as persona, DIET as dieta
	       WHERE persona.PERSONID = dieta.FK_PERSONID AND 
	       dieta.DIETDATETIME BETWEEN fecha_inicio AND fecha_fin AND persona.PERSONUSERNAME = usuario)
   LOOP
          calorias := var_r.suma;
	  RETURN NEXT;
   END LOOP;
END; $BODY$
  LANGUAGE plpgsql

CREATE OR REPLACE FUNCTION get_calorias_semana(
    usuario VARCHAR)
  RETURNS TABLE(calorias integer) 
  AS $$
DECLARE
   fecha_actual date;
   semana int;
   semana_atras int;
   var_r record;
BEGIN
   fecha_actual := current_date;
   semana := extract(week from fecha_actual);
   semana_atras := semana -4;
   FOR var_r IN (SELECT SUM(DIETCALORIE) AS suma
	       FROM PERSON, DIET
	       WHERE extract(week from DIET.DIETDATETIME) BETWEEN semana_atras AND semana AND
	        PERSONID = FK_PERSONID AND PERSONUSERNAME = usuario)
   LOOP
          calorias := var_r.suma;
	  RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION get_todos_alimentos(usuario VARCHAR)
  RETURNS TABLE(nombre_comida VARCHAR, peso_comida INT, calorias_comida INT)
   AS $$
DECLARE
   var_r	record;
   fecha_actual	DATE;
BEGIN
   FOR var_r IN(SELECT  FOODNAME, FOODWEIGHT, FOODCALORIE
		FROM PERSON inner join  DIET on personid = fk_personid inner join FOOD on fk_foodid = foodid
		WHERE personusername = usuario)
   LOOP
	nombre_comida := var_r.FOODNAME;
	peso_comida := var_r.FOODWEIGHT;
	calorias_comida := var_r.FOODCALORIE;
	RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION act_alimento_person(nombre_alimento VARCHAR, peso_alimento VARCHAR, caloria_alimento INT)
  RETURNS void 
   AS $$ 
DECLARE
	id_alimento	int;
BEGIN
	id_alimento := (SELECT FOODID FROM FOOD WHERE FOODNAME = nombre_alimento);
	UPDATE FOOD SET FOODNAME = nombre_alimento , FOODWEIGHT = peso_alimento , FOODCALORIE = caloria_alimento
	WHERE FOODID = id_alimento;
END; $$
  LANGUAGE plpgsql;




CREATE OR REPLACE FUNCTION elimina_alimento_person(nombre_alimento VARCHAR)
  RETURNS void 
   AS $$ 
DECLARE
	id_alimento	int;
BEGIN
	id_alimento := (SELECT FOODID FROM FOOD WHERE FOODNAME = nombre_alimento);
	DELETE FROM DIET
	WHERE FK_FOODID = id_alimento and DIETDATETIME = current_date;
	DELETE FROM FOOD
	WHERE FOODID = id_alimento;
END; $$
  LANGUAGE plpgsql;

  CREATE OR REPLACE FUNCTION get_alimentos_sugerencia(usuario VARCHAR)
  RETURNS TABLE(nombre_comida VARCHAR, peso_comida INT, calorias_comida INT)
   AS $$
DECLARE
   var_r  record;
BEGIN
   FOR var_r IN(SELECT  FOODNAME, FOODWEIGHT, FOODCALORIE
    FROM PERSON inner join  DIET on personid = fk_personid inner join FOOD on fk_foodid = foodid
    WHERE personusername = usuario AND FOODDINNER = TRUE)
   LOOP
  nombre_comida := var_r.FOODNAME;
  peso_comida := var_r.FOODWEIGHT;
  calorias_comida := var_r.FOODCALORIE;
  RETURN NEXT;
   END LOOP;
END; $$
  LANGUAGE plpgsql;


