package kr.ac.hongik.nas.ftpserver.util;

/**
 * 
 * @author Arubirate
 *TODO Login to user
 */  
public class Login {

	boolean authorized;
	String userName;
	String password;
	DBController db;

	public Login() {
		userName = "";
		password = "";
		authorized = false;
		db = new DBController();
	}

	public Login(String inputuserName, String inputPassword) {
		userName = inputuserName;
		password = inputPassword;
		authorized = false;
		db = new DBController();
	}

	public String getUser() {
		return userName;
	}

	public String getPass() {
		return password;
	}

	public void setUser(String str) {
		userName = str;
	}

	public void setPass(String str) {
		password = str;
	}

	public boolean tryToAuthorize() {		
		if( getPass() == "" || getUser() == "" )
			return false;
		String sql = null;
		int cnt = 0;

		// mysql Version
		db.getConn();

		sql = "SELECT count(*) as cnt FROM IDENTITY  ";
		sql = sql + "WHERE user = '" + userName + "' ";
		sql = sql + "AND password = '" + password + "'; ";

		db.excuteSelect(sql);
		while (db.resultSetNext()) {
			cnt = db.resultSetGetInt("cnt");
		}
		if (cnt == 1)
			authorized = true;
		db.close();

		return authorized;
	}

	public boolean isAuthorized() {
		if (authorized == false)
			authorized = tryToAuthorize();

		return authorized;
	}
}
