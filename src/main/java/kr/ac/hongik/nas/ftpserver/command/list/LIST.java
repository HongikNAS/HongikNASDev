package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.command.*;
import kr.ac.hongik.nas.ftpserver.FtpConnection;

import java.net.Socket;
import java.io.*;
import kr.ac.hongik.nas.ftpserver.FtpDataConnection;
public class LIST extends COMM{

	public void excute(FtpConnection conn, String in) { // TODO add Exception 
		
		FtpDataConnection dataConn = conn.getFtpDataConnection();
		//OutputStreamWriter dataOutFlow = null;
		//BufferedReader dataInFlow;
		String path, out="";
		if( !dataConn.isConnected() ) {
			System.err.println("Data Socket is NULL");
			conn.controlConnOutput("425 Can't open data Connection");
			return;
		}
		conn.controlConnOutput("150 data connection for LIST Command");
		
		path = conn.getRootPath()+conn.getCurrentPath();
		try { 
			File dir = new File(path);
			File[] fileList = dir.listFiles();
			for(int i=0;i<fileList.length; i++ ){
				
				out = out + fileList[i].getName() + '\n';
			}
		}catch(Exception e){
		}
		
		dataConn.dataConnOutput(out);
		dataConn.disconnect();
		
		conn.controlConnOutput("226 LIST transfer complete");
	}
	
}
