package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;

public class SYST extends COMM{

	public void excute(FtpConnection conn, String in) {
		
		//if( in != "" ) suspicious data 
		
		//conn.controlConnOutput("215 UNIX Type: L8"); // Just Send it..(now.)
		conn.controlConnOutput("215 UNIX Type"); 
	}
}
