package edu.cis.uab.shams.dsl.dal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**	
 * A singleton class to ensure one connection 
 * object through out the application life cycle
 * @author shams
 *
 */

public class DBConnection {
	
	private Connection DB;
	private static DBConnection DBConn;
	private DBConnection(String database, String username, String password)
	{
		try {
			Class.forName("org.postgresql.Driver");
			DB = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+database,
                    username,
                    password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	    
	}
	
	public static Connection DB(String database, String username, String password)
	{
		if(DBConn == null) 
			DBConn = new DBConnection(database, username, password);
		return DBConn.DB;
	}
}
