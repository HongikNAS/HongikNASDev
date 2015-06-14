package kr.ac.hongik.nas.ftpserver.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Arubirate
 *
 */
public final class DBController {
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	//final int debug = 1;

	public DBController() {
	}

	/**
	 * try to connect mysql Server
	 * 
	 * @return Connection
	 */
	public void close() {

		try {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			System.err.println("Database Close ERROR");
			System.exit(1);
		}
	}

	public void getConn() {
		// basic option : mysql

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection
					//("jdbc:mysql://localhost:3306/dbname?user=root&password=1234");
					("jdbc:mysql://localhost:3306/hongikNAS", "root", "1234");
		} catch (SQLException e) {
			System.err.println("Database Connection ERROR");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			System.err.println("Class Not Found");
			System.exit(1);
		}

	}

	public void excuteSelect(String strQuery) {

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);

		} catch (SQLException e) {
			System.err.println("Database SELECT QUERY ERROR");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean resultSetNext() {

		boolean res = false;
		try {
			res = rs.next();
		} catch (SQLException e) {
			System.err.println("Database ResultSetNext ERROR");
			System.exit(1);
		}

		return res;
	}

	public int resultSetGetInt(String column) {
		int res = 0;
		try {
			res = rs.getInt(column);
		} catch (SQLException e) {
			System.err.println("Database GetInt ERROR");
			System.exit(1);
		}
		return res;
	}
	// executeSelect -> ResultSet
	// executeDelete
	// executeUpdate

}
