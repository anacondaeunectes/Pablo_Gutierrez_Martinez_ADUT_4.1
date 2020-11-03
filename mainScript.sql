/*
*CREACION DE TIPOS
*/

/*TIPO CURSO*/

    /*Declaracion y cuerpo del tipo CURSO*/
    create or replace TYPE CURSO_TYPE AS OBJECT(
        ID_CURSO NUMBER(4),
        DESCRIPCION VARCHAR2(60),
        NIVEL VARCHAR2(30),
        TURNO CHAR,
        CONSTRUCTOR FUNCTION CURSO_TYPE(ID_CURSO NUMBER, DESCRIPCION VARCHAR2, NIVEL VARCHAR2, TURNO CHAR) RETURN SELF AS RESULT,
        MEMBER PROCEDURE VER_CURSO
    );
    
    create or replace TYPE BODY CURSO_TYPE AS
        CONSTRUCTOR FUNCTION CURSO_TYPE(ID_CURSO NUMBER, DESCRIPCION VARCHAR2, NIVEL VARCHAR2, TURNO CHAR) RETURN SELF AS RESULT AS
        BEGIN
            SELF.ID_CURSO := ID_CURSO;
            SELF.DESCRIPCION := DESCRIPCION;
            SELF.NIVEL := NIVEL;
            SELF.TURNO := TURNO;
            RETURN;
        END;
    
        MEMBER FUNCTION VER_CURSO RETURN VARCHAR2 IS
        BEGIN
            RETURN 'ID CURSO: ' || ID_CURSO || '\nDESCRIPCION: ' || DESCRIPCION || '\nNIVEL: ' || NIVEL || '\nTURNO: ' || TURNO;
        END;
    END;

/*TIPO ASIGNATURA*/

    /*Declaracion y cuerpo del tipo ASIGNATURA*/
    create or replace TYPE ASIGNATURA_TYPE AS OBJECT(
        COD_ASIG NUMBER(4),
        NOMBRE VARCHAR2(80),
        TIPO CHAR,
        MEMBER FUNCTION VER_ASIGNATURA RETURN VARCHAR2
    );
    
    create or replace TYPE BODY ASIGNATURA_TYPE AS
    
        MEMBER FUNCTION VER_ASIGNATURA RETURN VARCHAR2 IS
        BEGIN
            RETURN 'CODIGO ASIGNATURA: ' || COD_ASIG || '\nNOMBRE: ' || NOMBRE || '\nTIPO: ' || TIPO;
        END;
    END;

/*TIPO NOTA*/

    /*Declaracion del tipo EVALUACION*/
    CREATE OR REPLACE TYPE EVALUACION_VARRAY AS VARRAY(5) OF NUMBER(4,2);
    
    /*Declaracion y cuerpo del tipo NOTA*/
    CREATE OR REPLACE TYPE NOTA_TYPE AS OBJECT(
        ASIG REF ASIGNATURA_TYPE,
        EVALUACION EVALUACION_VARRAY,
        MEMBER FUNCTION MEDIA_FINAL_JUNIO(COD_ASIG NUMBER) RETURN NUMBER
    );
    
    create or replace TYPE BODY NOTA_TYPE AS
    
        MEMBER FUNCTION MEDIA_FINAL_JUNIO RETURN NUMBER IS
        BEGIN
            RETURN (EVALUACION(1) + EVALUACION(2) + EVALUACION(3))/3;
        END;
    END;


/*TIPO ALUMNO*/

    /*Declaracion  del varray CONTACTO (contiene hasta dos telefonos)*/
    CREATE OR REPLACE TYPE CONTACTO_VARRAY AS VARRAY(2) OF VARCHAR2(15);
    
    /*Declaracion y cuerpo del tipo DOMICILIO (tipo con la info relativa a una vivienda)*/
    create or replace TYPE DOMICILIO_TYPE AS OBJECT(
        DIRECCION VARCHAR2(50),
        POBLACION VARCHAR2(50),
        CODPOSTAL NUMBER(5),
        PROVINCIA VARCHAR2(40),
        MEMBER FUNCTION VER_DOMICILIO RETURN VARCHAR2
    );
    
    create or replace TYPE BODY DOMICILIO_TYPE AS
    
        MEMBER FUNCTION VER_DOMICILIO RETURN VARCHAR2 IS
        BEGIN
            RETURN 'DIRECCION: ' || DIRECCION || 'POBLACION: ' || POBLACION || 'COD. POSTAL: ' || CODPOSTAL || 'PROVINCIA: ' || PROVINCIA;
        END;
    END;
    
    /*Declaracion y cuerpo del tipo PERSONA (ALUMNO heredara de este tipo)*/
    create or replace TYPE PERSONA_TYPE AS OBJECT(
        NOMBRE VARCHAR2(50),
        DOMICILIO DOMICILIO_TYPE,
        CONTACTO CONTACTO_VARRAY,
        FECHA_NAC DATE,
        MEMBER FUNCTION VER_DESCRIPCION RETURN VARCHAR2
    )NOT FINAL;
    
    create or replace TYPE BODY PERSONA_TYPE AS
    
        MEMBER FUNCTION VER_DESCRIPCION RETURN VARCHAR2 IS
        BEGIN
            RETURN 'NOMBRE: ' || NOMBRE || '\nDOMICILIO: ' || DOMICILIO.VER_DOMICILIO;
        END;
    END;
    
    /*Declaracion del tipo TABLA_ANID_NOTA*/
    CREATE TYPE TABLA_ANID_NOTA AS TABLE OF NOTA_TYPE;
    
    /*Declaracion del tipo ALUMNO*/
    create or replace TYPE ALUMNO_TYPE UNDER PERSONA_TYPE(
        DNI VARCHAR2(10),
        CURSO REF CURSO_TYPE,
        NOTA TABLA_ANID_NOTA,
        OVERRIDING MEMBER FUNCTION VER_DESCRIPCION RETURN VARCHAR2
    );
    
    create or replace TYPE BODY ALUMNO_TYPE AS
    
        OVERRIDING MEMBER FUNCTION VER_DESCRIPCION RETURN VARCHAR2 IS
         
            cadena VARCHAR2(500);
            evaluacion VARCHAR2(20);
            CURSOR cursor_1 IS SELECT * FROM TABLE(NOTA);
            cont NUMBER := 1;
            nombre_asignatura VARCHAR2(200);
            ref_asignatura REF ASIGNATURA_TYPE;
                
        BEGIN
            cadena := 'NOMBRE: ' || NOMBRE || '\nDNI: '|| DNI || '\nDOMICILIO: ' || DOMICILIO.VER_DOMICILIO || '\nNOTAS: ';  
            FOR i IN cursor_1 LOOP
            SELECT  NOMBRE INTO nombre_asignatura FROM ASIGNATURA_TABLA A WHERE i.asig = REF(A); 
            cadena := CONCAT(cadena, '\n\n' || UPPER(nombre_asignatura) || ':');
                FOR j IN 1.. i.evaluacion.count LOOP
                    CASE j
                        WHEN 1 THEN evaluacion := 'Primera';
                        WHEN 2 THEN evaluacion := 'Segunda';
                        WHEN 3 THEN evaluacion := 'Tercera';
                        WHEN 4 THEN evaluacion := 'Final';
                        ELSE evaluacion := 'Septiembre';
                    END CASE;     
                   cadena := CONCAT(cadena, '\n\t' || evaluacion || ': ' || i.evaluacion(j));
                   cont := cont + 1;
                END LOOP;
                cont := 1;
            END LOOP;
            RETURN cadena;
        END;
    END;


/*
*CREACION DE TABLAS DE OBJETOS
*/

    /*TABLA CURSO*/
    CREATE TABLE CURSO_TABLA OF CURSO_TYPE(
        ID_CURSO NOT NULL PRIMARY KEY,
        TURNO DEFAULT 'm'
    );

    /*TABLA ALUMNO*/
    CREATE TABLE ALUMNO_TABLA OF ALUMNO_TYPE(
        DNI PRIMARY KEY
    )NESTED TABLE NOTA STORE AS NOTA_ANIDADA;

    /*TABLA ASIGNATURA*/
    CREATE TABLE ASIGNATURA_TABLA OF ASIGNATURA_TYPE(
        COD_ASIG NOT NULL PRIMARY KEY,
        TIPO DEFAULT 'f'
    );


/*
* PL/SQL GLOBAL
*/

/*INSERCCIONES*/

    /*Insert para la tabla curso*/
    CREATE PROCEDURE CREATE_CURSO (ID_CURSO NUMBER, DESCRIPCION VARCHAR2, NIVEL VARCHAR2, TURNO CHAR) AS
    BEGIN
        INSERT INTO curso_tabla VALUES(CURSO_TYPE(id_curso=>ID_CURSO, descripcion=>DESCRIPCION, nivel=>NIVEL,turno=>TURNO));
    END;
    
    /*Insert para la tabla asignatura*/
    create or replace PROCEDURE CREATE_ASIGNATURA (COD_ASIG NUMBER, NOMBRE VARCHAR2, TIPO CHAR) AS
    BEGIN
        INSERT INTO asignatura_tabla (COD_ASIG, NOMBRE, TIPO) VALUES (COD_ASIG, NOMBRE, TIPO);
    END;
    
    /*Insert para la tabla alumno*/
    
        /*Insert de un alumno sin notas asociadas*/
        create or replace PROCEDURE CREATE_ALUMNO_SIMPLE (NOMBRE VARCHAR2, DIRECCION VARCHAR2, POBLACION VARCHAR2, CODPOSTAL NUMBER, PROVINCIA VARCHAR2, CONTACTO_1 VARCHAR2, CONTACTO_2 VARCHAR2, FECHA_NAC VARCHAR2, DNI VARCHAR2, IDCURSO NUMBER) AS
            pers PERSONA_TYPE := PERSONA_TYPE(NOMBRE, DOMICILIO_TYPE(DIRECCION, POBLACION, CODPOSTAL, PROVINCIA), CONTACTO_VARRAY(CONTACTO_1, CONTACTO_2),TO_DATE(FECHA_NAC, 'dd/mm/yyyy'));
            
        BEGIN
            INSERT INTO alumno_tabla VALUES (ALUMNO_TYPE(persona=>pers,dni=>DNI,curso=>(SELECT REF(T) FROM curso_tabla T WHERE ID_CURSO = IDCURSO),nota=>TABLA_ANID_NOTA()));
        END;
    
        /*Insert de un objeto nota_type en la tabla anidada que almacena las notas de un alumno. Puede contener la nota de septiembre o no*/
        create or replace PROCEDURE ASIGNAR_NOTA_ALUMNO(DNI_ALUM VARCHAR2, COD_ASIGNATURA NUMBER, NOTA_1 NUMBER, NOTA_2 NUMBER, NOTA_3 NUMBER, NOTA_FINAL NUMBER, NOTA_SEPTIEMBRE NUMBER) AS
            evaluacion EVALUACION_VARRAY;
        BEGIN
            IF NOTA_SEPTIEMBRE IS NOT NULL THEN
                evaluacion := EVALUACION_VARRAY(NOTA_1, NOTA_2, NOTA_3, NOTA_FINAL, NOTA_SEPTIEMBRE);
            ELSE
                evaluacion := EVALUACION_VARRAY(NOTA_1, NOTA_2, NOTA_3, NOTA_FINAL);
            END IF;
            
            INSERT INTO TABLE(SELECT NOTA FROM alumno_tabla WHERE DNI = DNI_ALUM) VALUES(NOTA_TYPE((SELECT REF(B) FROM ASIGNATURA_TABLA B WHERE COD_ASIG = COD_ASIGNATURA), EVALUACION)); 
        END;

/*LECTURA*/

    /*Asignatura*/
        /*Informacion completa de una asignatura*/
        create or replace FUNCTION READ_ASIGNATURA(COD_ASIGNATURA NUMBER)RETURN VARCHAR2 IS
            asig ASIGNATURA_TYPE;
        BEGIN
            SELECT VALUE(T) INTO asig FROM ASIGNATURA_TABLA T WHERE COD_ASIG = COD_ASIGNATURA;
            RETURN asig.ver_asignatura; 
        END;
        
        /*Listado de todas las asignaturas y su clave*/
        create or replace FUNCTION READ_ALL_ASIGNATURA RETURN VARCHAR2 IS
            cadena VARCHAR2(4000);
            CURSOR c_asignatura IS SELECT * FROM asignatura_tabla;
        BEGIN
            FOR i IN c_asignatura LOOP
                cadena := CONCAT(cadena, chr(10) || '{Codigo: ' || i.COD_ASIG || ', ' || 'Nombre: ' || i.NOMBRE || '}');
            END LOOP;
            RETURN cadena;
        END;
    
    /*CURSO*/
    
        /*Informacion completa de un curso*/
        create or replace FUNCTION READ_CURSO(IDCURSO NUMBER) RETURN VARCHAR2 IS
            curso CURSO_TYPE;
        BEGIN
            SELECT VALUE(B) INTO curso FROM CURSO_TABLA B WHERE ID_CURSO = IDCURSO;
            RETURN curso.ver_curso;
        END;
        
        /*Listado de todos los cursos y sus claves*/
        create or replace FUNCTION READ_ALL_CURSO RETURN VARCHAR2 IS
            cadena VARCHAR2(4000);
            CURSOR c_curso IS SELECT * FROM curso_tabla;
        BEGIN
            FOR i IN c_curso LOOP
                cadena := CONCAT(cadena, chr(10) || '{Codigo: ' || i.ID_CURSO || ', ' || 'Nivel: ' || i.NIVEL || ', ' || 'Turno: ' || i.TURNO || '}');
            END LOOP;
            RETURN cadena;
        END;
        
    /*ALUMNO*/    
    
        /*Informacion completa de un alumno*/
        create or replace FUNCTION READ_ALUMNO(ALUM_DNI VARCHAR2) RETURN VARCHAR2 IS
            alum ALUMNO_TYPE;
        BEGIN
            SELECT VALUE(C) INTO alum FROM ALUMNO_TABLA C WHERE DNI = ALUM_DNI;
            RETURN alum.ver_descripcion;
        END;
    
        /*Listado de todas las asignaturas y su clave*/
        create or replace FUNCTION READ_ALL_ALUMNO RETURN VARCHAR2 IS
            cadena VARCHAR2(4000);
            CURSOR c_alumno IS SELECT * FROM alumno_tabla;
        BEGIN
            FOR i IN c_alumno LOOP
                cadena := CONCAT(cadena, chr(10) || '{DNI: ' || i.DNI || ', ' || 'Nombre: ' || i.NOMBRE || '}');
            END LOOP;
            RETURN cadena;
        END;

/*BORRADO*/

    /*Borrado de una asignatura*/
    create or replace PROCEDURE DELETE_ASIGNATURA(COD_ASIGNATURA NUMBER) AS
    BEGIN
        DELETE FROM ASIGNATURA_TABLA WHERE COD_ASIG = COD_ASIGNATURA;
    END;
    
    /*Borrado de un curso*/
    create or replace PROCEDURE DELETE_CURSO(IDCURSO NUMBER) AS
    BEGIN
        DELETE FROM CURSO_TABLA WHERE ID_CURSO = IDCURSO;
    END;
    
    /*Borrado de un alumno*/
    create or replace PROCEDURE DELETE_ALUMNO(DNI_ALUM VARCHAR2) AS
    BEGIN
        DELETE FROM ALUMNO_TABLA WHERE DNI = DNI_ALUM;
    END;
    
/*UPDATE*/

    /*Actualizacion Asignatura*/
    create or replace PROCEDURE UPDATE_ASIGNATURA(COD_ASIGNATURA NUMBER, NOMBRE_ASIG VARCHAR2, TIPO_ASIG CHAR) AS
    BEGIN
        
        IF (NOMBRE_ASIG IS NOT NULL) THEN
            UPDATE ASIGNATURA_TABLA SET NOMBRE = NOMBRE_ASIG WHERE COD_ASIG = COD_ASIGNATURA;
        END IF;
        
        IF (TIPO_ASIG IS NOT NULL) THEN
            UPDATE ASIGNATURA_TABLA SET TIPO = TIPO_ASIG WHERE COD_ASIG = COD_ASIGNATURA;
        END IF;
        
    END;
    
    /*Actualizacion Curso*/
    create or replace PROCEDURE UPDATE_CURSO(IDCURSO NUMBER, DESCRIPCION_CURSO VARCHAR2, NIVEL_CURSO VARCHAR2, TURNO_CURSO CHAR) AS
    BEGIN
        IF (DESCRIPCION_CURSO IS NOT NULL) THEN
            UPDATE CURSO_TABLA SET DESCRIPCION = DESCRIPCION_CURSO WHERE ID_CURSO = IDCURSO;
        END IF;
        
        IF (NIVEL_CURSO IS NOT NULL) THEN
            UPDATE CURSO_TABLA SET NIVEL = NIVEL_CURSO WHERE ID_CURSO = IDCURSO;
        END IF;
        
        IF (TURNO_CURSO IS NOT NULL) THEN
            UPDATE CURSO_TABLA SET TURNO = TURNO_CURSO WHERE ID_CURSO = IDCURSO;
        END IF;
    END;
    
    /*Actualizacion Alumno*/
    create or replace PROCEDURE UPDATE_ALUMNO(NOMBRE_ALUM VARCHAR2, DIRECCION_ALUM VARCHAR2, POBLACION_ALUM VARCHAR2, CODPOSTAL_ALUM NUMBER, PROVINCIA_ALUM VARCHAR2, CONTACTO_1_ALUM VARCHAR2, CONTACTO_2_ALUM VARCHAR2, FECHA_NAC_ALUM VARCHAR2, DNI_ALUM VARCHAR2, IDCURSO_ALUM NUMBER) AS
        dom DOMICILIO_TYPE;
        contact CONTACTO_VARRAY;
    BEGIN
        SELECT DOMICILIO INTO dom FROM ALUMNO_TABLA WHERE DNI = DNI_ALUM;
        
        SELECT CONTACTO INTO contact FROM ALUMNO_TABLA WHERE DNI = DNI_ALUM;

        IF (NOMBRE_ALUM IS NOT NULL) THEN
            UPDATE ALUMNO_TABLA SET NOMBRE = NOMBRE_ALUM WHERE DNI = DNI_ALUM;
        END IF;
        
        IF (DIRECCION_ALUM IS NOT NULL) THEN
            dom.DIRECCION := DIRECCION_ALUM;
        END IF;
        
        IF (POBLACION_ALUM IS NOT NULL) THEN
            dom.POBLACION := POBLACION_ALUM;
        END IF;
        
        IF (CODPOSTAL_ALUM IS NOT NULL) THEN
            dom.CODPOSTAL := CODPOSTAL_ALUM;
        END IF;
        
        IF (PROVINCIA_ALUM IS NOT NULL) THEN
            dom.PROVINCIA := PROVINCIA_ALUM;
        END IF;
        
        UPDATE ALUMNO_TABLA SET DOMICILIO = dom WHERE DNI = DNI_ALUM;
        
        IF (CONTACTO_1_ALUM IS NOT NULL) THEN
            contact(1) := CONTACTO_1_ALUM;
        END IF;
        
        IF (CONTACTO_2_ALUM IS NOT NULL) THEN
            contact(2) := CONTACTO_2_ALUM;
        END IF;
        
        UPDATE ALUMNO_TABLA SET CONTACTO = contact WHERE DNI = DNI_ALUM;
        
        IF (FECHA_NAC_ALUM IS NOT NULL) THEN
            UPDATE ALUMNO_TABLA SET FECHA_NAC = TO_DATE(FECHA_NAC_ALUM,'DD-MON-YYYY') WHERE DNI = DNI_ALUM;
        END IF;
        
        IF (IDCURSO_ALUM IS NOT NULL) THEN
            UPDATE ALUMNO_TABLA SET CURSO = (SELECT REF(G) FROM CURSO_TABLA G WHERE ID_CURSO = IDCURSO_ALUM) WHERE DNI = DNI_ALUM;
        END IF;
        
    END;
    
    