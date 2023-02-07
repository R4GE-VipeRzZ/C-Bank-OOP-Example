package bankTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.ResultSetMetaData;

public class JDBC {
	
	private static String url = "jdbc:mysql://localhost:3306/";		//This stores the url of the database
	private static String userName = "ChangeToYourUsername";	//This stores the username of the mysql database
	private static String password = "ChangeToYourPassword";	//This stores the password of the mysql database
	
	private static void checkMysqlDrivers() {
		try {		//Checks that the mysql driver exists
			Class.forName("com.mysql.cj.jdbc.Driver");
		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public static void setDB(String dbName) {	//This method changes the database url depending on the database name that has been passed to it
		JDBC.url = JDBC.url + dbName;
	}
	
	public static ArrayList<String> readDB(String query){		//This method executes the given mysql query and returns the result
		checkMysqlDrivers();		//Checks for the mysql drivers
		
		ArrayList<String> resultArrayList = new ArrayList<String>();
		Connection con = null;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			con = DriverManager.getConnection(url, userName, password);
			statement = con.createStatement();
			result = statement.executeQuery(query);
			

			ResultSetMetaData resultMeta = result.getMetaData();		//Created so that the number of columns can be calculated
			while (result.next()) {
				for (int i = 1; i <= (resultMeta.getColumnCount()); i++) {	//Adds the value from each column to the arraylist
					resultArrayList.add(result.getString(i));
				}			
			}
			
		}catch (SQLException e) {
			//e.printStackTrace();
			resultArrayList.add("error");	//Adds the exception to the arraylist
		}finally {		//Used to close all the connections that were created
			if (result != null) {	//Checks a result has been created
				try {
					result.close();	//Closes the result
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (statement != null) {	//Checks a statment has been created
				try {
					statement.close();	//Closes the statment
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {		//Checks a connection has been made
				try {
					con.close();	//Closes the connection
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return resultArrayList;
	}
	
	public static void writeDB(String query) {
		checkMysqlDrivers();		//Checks for the mysql drivers
		Connection con = null;
		Statement statement = null;
		
		try {
			con = DriverManager.getConnection(url, userName, password);
			statement = con.createStatement();
			statement.executeUpdate(query);	
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {		//Used to close all the connections that were created
			if (statement != null) {	//Checks a statment has been created
				try {
					statement.close();	//Closes the statment
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {		//Checks a connection has been made
				try {
					con.close();	//Closes the connection
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
