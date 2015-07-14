package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;
import kr.ac.hongik.nas.ftpserver.util.Login;
import java.io.*;

public class PASS extends COMM {

	public void excute(FtpConnection conn, String in) {

		// if( in == "" || in == null ) No input
		Login account = conn.getAccount();
		account.setPass(in);
		
		if (account.isAuthorized()) {
			conn.controlConnOutput("230 User logged in");
			
			//TODO : 나중에 이동해야함
			conn.setCurrentPath( File.separator+account.getUser() ); // check whether Directory exist
			
			String path = conn.getRootPath() + conn.getCurrentPath();
			File dir = new File(path);
			if( dir.isDirectory() == false ) {
				dir.mkdirs(); // if user folder is not exist, then make it
			}
			
		} else {
			conn.controlConnOutput("530 Login incorrect");
		}
	}
}
