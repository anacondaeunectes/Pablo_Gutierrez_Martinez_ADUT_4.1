package pckg;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Modelo extends Conexion{

	public Modelo(String url, String port, String db, String user, String password) {
		super(url, port, db, user, password);
	}
	
	//INSERCCIONES
	/**
	 * Llama al precedimiento para insertar cursos y le pasa los parametros introducidos por el usuario
	 * @param id
	 * @param descripcion
	 * @param nivel
	 * @param tipo
	 */
	public void create_curso(int id, String descripcion, String nivel, char turno){
		
		CallableStatement call_st;
		try {
			call_st = getConnection().prepareCall("CALL create_curso(?,?,?,?)");
		
			call_st.setInt(1, id);
			call_st.setString(2, descripcion);
			call_st.setString(3, nivel);
			call_st.setString(4, Character.toString(turno));
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al insertar el curso. Por favor revise los datos introducidos.");
		}
	}

	/**
	 * Llama al precedimiento para insertar asignatura y le pasa los parametros introducidos por el usuario
	 * @param codigo
	 * @param nombre
	 * @param tipo
	 */
	public void create_asignatura(int codigo, String nombre, char tipo){
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL create_asignatura(?,?,?)");
			
			call_st.setInt(1, codigo);
			call_st.setString(2, nombre);
			call_st.setString(3, Character.toString(tipo));
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al insertar la asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para insertar alumno y le pasa los parametros introducidos por el usuario
	 * @param nombre
	 * @param direccion
	 * @param poblacion
	 * @param codpostal
	 * @param provincia
	 * @param contacto_1
	 * @param contacto_2
	 * @param fecha_nac
	 * @param dni
	 * @param idcurso
	 */
	public void create_alumno(String nombre, String direccion, String poblacion, int codpostal, String provincia, String contacto_1, String contacto_2, String fecha_nac, String dni, int idcurso) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL create_alumno_simple(?,?,?,?,?,?,?,?,?,?)");
			
			call_st.setString(1, nombre);
			call_st.setString(2, direccion);
			call_st.setString(3, poblacion);
			call_st.setInt(4, codpostal);
			call_st.setString(5, provincia);
			call_st.setString(6, contacto_1);
			call_st.setString(7, contacto_2);
			call_st.setString(8, fecha_nac);
			call_st.setString(9, dni);
			call_st.setInt(10, idcurso);
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al insertar el alumno. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para insertar las notas de una asignatura a un alumno y le pasa los parametros introducidos por el usuario
	 * @param dni
	 * @param codigoAsignatura
	 * @param nota_1
	 * @param nota_2
	 * @param nota_3
	 * @param nota_final
	 * @param nota_septiembre
	 */
	public void asignar_nota_alumno(String dni, int codigoAsignatura, double nota_1, double nota_2, double nota_3, double nota_final, double nota_septiembre, boolean flagSeptiembre) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL asignar_nota_alumno(?,?,?,?,?,?,?)");
			
			call_st.setString(1, dni);
			call_st.setInt(2, codigoAsignatura);
			call_st.setDouble(3, nota_1);
			call_st.setDouble(4, nota_2);
			call_st.setDouble(5, nota_3);
			call_st.setDouble(6, nota_final);
			if (flagSeptiembre) {
				call_st.setDouble(7, nota_septiembre);
			}else {
				call_st.setNull(7, Types.NULL);
			}
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al asignar la nota al alumno. Por favor revise los datos introducidos.");
		}
	}
	
	
	
	//LECTURA
	/**
	 * Llama al precedimiento para leer una asignatura  e imprime el resultado
	 * @param codigo
	 */
	public void read_asignatura(int codigo) {
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_asignatura(?) INTO ?");
			
			call_st.setInt(1, codigo);
			call_st.registerOutParameter(2, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(2));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion de la asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para leer asignaturas e imprime el resultado
	 */
	public void read_all_asignatura() {
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_all_asignatura() INTO ?");
			
			call_st.registerOutParameter(1, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(1));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion de las asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para leer un curso  e imprime el resultado
	 * @param id
	 */
	public void read_curso(int id) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_curso(?) INTO ?");
			
			call_st.setInt(1, id);
			call_st.registerOutParameter(2, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(2));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del curso. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para leer cursos  e imprime el resultado
	 */
	public void read_all_curso() {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_all_curso() INTO ?");
			
			call_st.registerOutParameter(1, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(1));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion de los cursos. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para leer un alumno e imprime el resultado
	 * @param dni
	 */
	public void read_alumno(String dni) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_alumno(?) INTO ?");
			
			call_st.setString(1, dni);
			call_st.registerOutParameter(2, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(2));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion del alumno. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al precedimiento para leer alumnos e imprime el resultado
	 */
	public void read_all_alumno() {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL read_all_alumno() INTO ?");
			
			call_st.registerOutParameter(1, Types.VARCHAR);
			
			call_st.execute();
			
			System.out.println(call_st.getString(1));
		} catch (SQLException e) {
			System.err.println("Error al recuperar la informacion de los alumnos. Por favor revise los datos introducidos.");
		}
	}
	
	//BORRADO
	
	/**
	 * Llama al procedimiento para borrar una asignatura
	 * @param codigo
	 */
	public void delete_asignatura(int codigo) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL delete_asignatura(?)");
	
			call_st.setInt(1, codigo);
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al eliminar la asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al procedimiento para borrar un curso
	 * @param id
	 */
	public void delete_curso(int id) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL delete_curso(?)");
	
			call_st.setInt(1, id);
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al eliminar el curso. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al procedimiento para borrar un alumno
	 * @param dni
	 */
	public void delete_alumno(String dni) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL delete_alumno(?)");
	
			call_st.setString(1, dni);
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al eliminar el alumno. Por favor revise los datos introducidos.");
		}
	}
	
	//ACTUALIZACION
	
	/**
	 * Llama al procedimiento para actualizar una asignatura
	 * @param codigo
	 * @param nombre
	 * @param tipo
	 */
	public void update_asignatura(int codigo, String nombre, char tipo) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL update_asignatura(?,?,?)");
	
			call_st.setInt(1, codigo);
			call_st.setString(2, nombre);
			call_st.setString(3, Character.toString(tipo));
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al actualizar la informacion de la asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al procedimiento para actualizar un curso
	 * @param id
	 * @param descripcion
	 * @param nivel
	 * @param tipo
	 */
	public void update_curso(int id, String descripcion, String nivel, char turno) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL update_curso(?,?,?,?)");
	
			call_st.setInt(1, id);
			call_st.setString(2, descripcion);
			call_st.setString(3, nivel);
			call_st.setString(4, Character.toString(turno));
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al actualizar la informacion del curso. Por favor revise los datos introducidos.");
		}
	}
	
	/**
	 * Llama al procedimiento para actualizar un alumno
	 * @param nombre
	 * @param direccion
	 * @param poblacion
	 * @param codpostal
	 * @param provincia
	 * @param contacto_1
	 * @param contacto_2
	 * @param fecha_nac
	 * @param dni
	 * @param idcurso
	 */
	public void update_alumno(String nombre, String direccion, String poblacion, int codpostal, String provincia, String contacto_1, String contacto_2, String fecha_nac, String dni, int idcurso) {
		
		try {
			CallableStatement call_st = getConnection().prepareCall("CALL update_alumno(?,?,?,?,?,?,?,?,?,?)");
	
			call_st.setString(1, nombre);
			call_st.setString(2, direccion);
			call_st.setString(3, poblacion);
			call_st.setInt(4, codpostal);
			call_st.setString(5, provincia);
			call_st.setString(6, contacto_1);
			call_st.setString(7, contacto_2);
			call_st.setString(8, fecha_nac);
			call_st.setString(9, dni);
			call_st.setInt(10, idcurso);
			
			call_st.execute();
		} catch (SQLException e) {
			System.err.println("Error al actualizar la informacion de la asignatura. Por favor revise los datos introducidos.");
		}
	}
	
	

}
