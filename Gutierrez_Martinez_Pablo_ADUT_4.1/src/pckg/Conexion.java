package pckg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

	private Connection conex = null;
	private String url = null;
	private String port = null;
    private String db = null;
    private String user = null;
    private String password = null;
    
	public Conexion(String url, String port, String db, String user, String password) {
		this.url = url;
		this.port = port;
		this.db = db;
		this.user = user;
		this.password = password;
		
	}
	
	public boolean connect() {
		boolean flag = true;
		try {
            conex = DriverManager.getConnection( "jdbc:oracle:thin:@" + this.url+ ":" + this.port + ":" + this.db, this.user , this.password );
		} catch (SQLException e) {
			flag = false;
		}
		
		return flag;
	}
    
	public Connection getConnection() {
		return conex;
	}
	
	public void closeConexion(){
		
       try {
           System.out.println("Conex cerrada");
           this.conex.close();
           
       } catch (SQLException e) {
           System.err.println(e.getMessage());
       } 
	}
    

}