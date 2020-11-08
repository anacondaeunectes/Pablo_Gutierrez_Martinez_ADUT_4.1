package pckg;

import java.sql.SQLException;

import utilityPackage.EntradaDatos;

public class App {
	
	private static EntradaDatos entradaDatos = EntradaDatos.getInstance();
	
	private static Modelo modelo;

	public static void main(String[] args) {
		
		System.out.println("¡Bienvenido!. A continuacion se le pediran los datos para la conexion con la base de datos:");
		
		while (!conectarBBDD()) {
			System.out.println("Error al conectar con la base de datos.¿Quiere volver a intentar la conexion? (Y/n)");
			if (entradaDatos.pedirYesNo() == 0) {
				System.out.println("Saliendo del programa...");
				System.exit(0);
			}
		}
		
		int num = 1;
		
		while (num != 77) {
			
			System.out.println("\nMENU BBDD COLEGIO");
			System.out.println("=-=-=-=-=-=-=-=-=-=");
			System.out.println("1. Visualizar datos");
			System.out.println("2. Insertar datos");
			System.out.println("3. Asignar notas a alumnos");
			System.out.println("4. Actualizar datos");
			System.out.println("5. Eliminar datos");
			System.out.println("0. Salir");
			System.out.println("\nIntroduzca la opcion deseada:");
		
			switch (num = entradaDatos.pedirInt()) {
			
			case 1:
				System.out.println("1. Visualizar alumno");
				System.out.println("2. Visualizar curso");
				System.out.println("3. Visualizar asignatura");
				System.out.println("0. Volver al menu");
				
				switch (num = entradaDatos.pedirInt_Parametros(0, 3)) {
				case 1:
					System.out.println("1. Listado general alumnos");
					System.out.println("2. Informacion completa alumno especifico");
					System.out.println("0. Volver al menu");
					
					switch (num = entradaDatos.pedirInt_Parametros(0, 2)) {
					case 1:
						modelo.read_all_alumno();
						break;

					case 2:
						System.out.println("Introduzca el DNI del alumno a consultar:");
						modelo.read_alumno(entradaDatos.pedirString());
						break;
					
					case 0:
						break;
					}
					break;

				case 2:
					System.out.println("1. Listado general cursos");
					System.out.println("2. Informacion completa curso especifico");
					System.out.println("0. Volver al menu");
					
					switch (num = entradaDatos.pedirInt_Parametros(0, 2)) {
					case 1:
						modelo.read_all_curso();
						break;

					case 2:
						System.out.println("Introduzca el ID del curso a consultar:");
						modelo.read_curso(entradaDatos.pedirInt());
						break;
					
					case 0:
						break;
					}
					break;
					
				case 3:
					System.out.println("1. Listado general asignaturas");
					System.out.println("2. Informacion completa asignatura especifica");
					System.out.println("0. Volver al menu");
					
					switch (num = entradaDatos.pedirInt_Parametros(0, 2)) {
					case 1:
						modelo.read_all_asignatura();
						break;

					case 2:
						System.out.println("Introduzca el codido de la asignatura a consultar:");
						modelo.read_asignatura(entradaDatos.pedirInt());
						break;
					
					case 0:
						break;
					}
					break;
				}
				break;
			case 2:
				System.out.println("1. Insertar alumno");
				System.out.println("2. Insertar curso");
				System.out.println("3. Insertar asignatura");
				System.out.println("0. Volver al menu");
				switch (num = entradaDatos.pedirInt_Parametros(0, 3)) {
				case 1:
					String nombre=null, direccion=null, poblacion=null, provincia=null, contacto_1=null, contacto_2=null, fecha_nac=null, dni=null;
					int codpostal, idcurso;
					
					System.out.println("Introduzca el nombre del alumno:");
					nombre = entradaDatos.pedirString();
					
					System.out.println("Introduzca la direccion del alumno:");
					direccion = entradaDatos.pedirString();

					System.out.println("Introduzca la poblacion del alumno:");
					poblacion = entradaDatos.pedirString();
					
					System.out.println("Introduzca el codigo postal del alumno:");
					codpostal = entradaDatos.pedirInt();
					
					System.out.println("Introduzca la provincia del alumno:");
					provincia = entradaDatos.pedirString();
					
					System.out.println("¿Posee telefono de contacto?");
					if (entradaDatos.pedirYesNo() == 1) {
						System.out.println("Introduzca el numero de telefono:");
						contacto_1 = entradaDatos.pedirString();
					}
					
					System.out.println("¿Desea agregar un segundo numero de contacto?");
					if (entradaDatos.pedirYesNo() == 1) {
						System.out.println("Introduzca el numero de telefono:");
						contacto_2 = entradaDatos.pedirString();
					}
					
					System.out.println("Introduzca el dni del alumno:");
					dni = entradaDatos.pedirString();
					
					System.out.println("Introduzca el ID del curso del alumno(Se le presenta un listado con los cursos actuales):");
					modelo.read_all_curso();
					idcurso = entradaDatos.pedirInt();
					
					modelo.create_alumno(nombre, direccion, poblacion, codpostal, provincia, contacto_1, contacto_2, fecha_nac, dni, idcurso);
					break;

				case 2:
					int id;
					String descripcion, nivel;
					char turno;
					
					System.out.println("Introduzca el ID del curso:");
					id = entradaDatos.pedirInt();
					
					System.out.println("Introduzca una descripcion para el curso:");
					descripcion = entradaDatos.pedirString();
					
					System.out.println("Introduzca el nivel del curso:");
					nivel = entradaDatos.pedirString();
					
					System.out.println("Introduzca el turno del curso(mañana (m), tarde(t)):");
					turno = entradaDatos.pedirString().charAt(0);
					
					modelo.create_curso(id, descripcion, nivel, turno);
					break;
					
				case 3:
					int codigo;
					char tipo;
					
					System.out.println("Introduzca el codigo de la asignatura:");
					codigo = entradaDatos.pedirInt();
					
					System.out.println("Introduzca el nombre de la asignatura:");
					nombre = entradaDatos.pedirString();
					
					System.out.println("Introduzca el tipo de asignatura(Formacion Básica(f), obligarotias(o), optativas(p), prácticas externas(x) o trabajo fin titulación(t)):");
					tipo = entradaDatos.pedirString().charAt(0);
					
					modelo.create_asignatura(codigo, nombre, tipo);
					break;
				}

				break;
				
			case 3:
				String dni = null;
				int codigoAsignatura;
				double nota_1, nota_2, nota_3, nota_final, nota_septiembre = 0;
				boolean flagSeptiembre = false;
				
				System.out.println("Inserte el DNI del alumno al que asignar las notas de una asignatura:");
				dni = entradaDatos.pedirString();
				
				System.out.println("Inserte el codigo de la asignatura a la que asignar las notas:");
				codigoAsignatura = entradaDatos.pedirInt();
				
				System.out.println("Inserte la nota de la primera evaluacion:");
				nota_1 = entradaDatos.pedirDouble();
				
				System.out.println("Inserte la nota de la segunda evaluacion:");
				nota_2 = entradaDatos.pedirDouble();
				
				System.out.println("Inserte la nota de la tercera evaluacion:");
				nota_3 = entradaDatos.pedirDouble();
				
				System.out.println("Inserte la nota de la evaluacion final:");
				nota_final = entradaDatos.pedirDouble();
				
				System.out.println("¿Requiere agregar la nota dela evaluacion de Septiembre?");
				if (entradaDatos.pedirYesNo() == 1) {
					flagSeptiembre = true;
					System.out.println("Inserte la nota de la evaluacion de Septiembre:");
					nota_septiembre = entradaDatos.pedirDouble();
				}
				modelo.asignar_nota_alumno(dni, codigoAsignatura, nota_1, nota_2, nota_3, nota_final, nota_septiembre, flagSeptiembre);
				
				break;
				
			case 4:
				System.out.println("1. Actualizar alumno");
				System.out.println("2. Actualizar curso");
				System.out.println("3. Actualizar asignatura");
				System.out.println("0. Volver al menu");
				switch (num = entradaDatos.pedirInt_Parametros(0, 3)) {
				
				case 1:
					String nombre=null, direccion=null, poblacion=null, provincia=null, contacto_1=null, contacto_2=null, fecha_nac=null, DNI=null;
					int codpostal, idcurso;
					
					System.out.println("Introduzca el DNI del alumno a actualizar:");
					DNI = entradaDatos.pedirString();
					
					System.out.println("Introduzca el nombre del alumno:");
					nombre = entradaDatos.pedirString();
					
					System.out.println("Introduzca la direccion del alumno:");
					direccion = entradaDatos.pedirString();

					System.out.println("Introduzca la poblacion del alumno:");
					poblacion = entradaDatos.pedirString();
					
					System.out.println("Introduzca el codigo postal del alumno:");
					codpostal = entradaDatos.pedirInt();
					
					System.out.println("Introduzca la provincia del alumno:");
					provincia = entradaDatos.pedirString();
					
					System.out.println("¿Posee telefono de contacto?");
					if (entradaDatos.pedirYesNo() == 1) {
						System.out.println("Introduzca el numero de telefono:");
						contacto_1 = entradaDatos.pedirString();
					}
					
					System.out.println("¿Desea agregar un segundo numero de contacto?");
					if (entradaDatos.pedirYesNo() == 1) {
						System.out.println("Introduzca el numero de telefono:");
						contacto_2 = entradaDatos.pedirString();
					}
					
					System.out.println("Introduzca el ID del curso del alumno(Se le presenta un listado con los cursos actuales):");
					modelo.read_all_curso();
					idcurso = entradaDatos.pedirInt();
					
					modelo.update_alumno(nombre, direccion, poblacion, codpostal, provincia, contacto_1, contacto_2, fecha_nac, DNI, idcurso);
					break;
					
				case 2:
					int id;
					String descripcion, nivel;
					char turno;
					
					System.out.println("Introduzca el ID del curso a actualizar:");
					id = entradaDatos.pedirInt();
					
					System.out.println("Introduzca una descripcion para el curso:");
					descripcion = entradaDatos.pedirString();
					
					System.out.println("Introduzca el nivel del curso:");
					nivel = entradaDatos.pedirString();
					
					System.out.println("Introduzca el turno del curso(mañana (m), tarde(t)):");
					turno = entradaDatos.pedirString().charAt(0);
					
					modelo.update_curso(id, descripcion, nivel, turno);
					break;
				
				case 3:
					int codigo;
					char tipo;
					
					System.out.println("Introduzca el codigo de la asignatura a actualizar:");
					codigo = entradaDatos.pedirInt();
					
					System.out.println("Introduzca el nombre de la asignatura:");
					nombre = entradaDatos.pedirString();
					
					System.out.println("Introduzca el tipo de asignatura(Formacion Básica(f), obligarotias(o), optativas(p), prácticas externas(x) o trabajo fin titulación(t)):");
					tipo = entradaDatos.pedirString().charAt(0);
					
					modelo.update_asignatura(codigo, nombre, tipo);
					break;
				}
				break;
				
			case 5:
				System.out.println("1. Eliminar alumno");
				System.out.println("2. Eliminar curso");
				System.out.println("3. Eliminar asignatura");
				System.out.println("0. Volver al menu");
				switch (num = entradaDatos.pedirInt_Parametros(0, 3)) {
				
				case 1:
					System.out.println("Introduzca el DNI del alumno a eliminar");
					modelo.delete_alumno(entradaDatos.pedirString());
					break;
					
				case 2:
					System.out.println("Introduzca el ID del curso a eliminar");
					modelo.delete_curso(entradaDatos.pedirInt());
					break;
					
				case 3:
					System.out.println("Introduzca el codigo de la asignatura a eliminar");
					modelo.delete_asignatura(entradaDatos.pedirInt());
					break;
				}

				break;
				
			case 0:
				System.out.println("¡Adios! Saliendo del programa...");
				num = 77;
			}
		}
		
		
	}
	
	private static boolean conectarBBDD(){
		
		String url;
		String port;
	    String db;
	    String user;
	    String password;
		
		System.out.println("Introduzca la url de su BBDD:");
		url = entradaDatos.pedirString();
		
		System.out.println("Introduzca el puerto para la conexion con su BBDD(Oracle suele usar por defecto el puerto 1521):");
		port = entradaDatos.pedirString();
		
		System.out.println("Introduzca el nombre de la base de datos a la que desea conectarse:");
		db = entradaDatos.pedirString();
		
		System.out.println("Introduzca el usuario de la BBDD seleccionada:");
		user = entradaDatos.pedirString();
		
		System.out.println("Introduzca la contraseña de la BBDD seleccionada:");
		password = entradaDatos.pedirString();
		
		modelo = new Modelo(url, port, db, user, password);
		
		return modelo.connect() ? true : false;
		
	}

}
