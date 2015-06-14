package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.command.*;
import kr.ac.hongik.nas.ftpserver.FtpConnection;

import java.net.Socket;
import java.io.*;

public class LIST extends COMM{

	public void excute(FtpConnection conn, String in) { // TODO add Exception 
		
		Socket dataSocket = conn.getDataSocket();
		OutputStreamWriter dataOutFlow = null;
		//BufferedReader dataInFlow;
		String path, out="";
		if( dataSocket == null ) {
			System.err.println("Data Socket is NULL");
			conn.output("425 Can't open data Connection");
			return;
		}
		conn.output("150 data connection for LIST Command");
		
		try {
			dataOutFlow = new OutputStreamWriter(
					dataSocket.getOutputStream());
		} catch (Exception e) {
			System.err.println("DataConnectionStream ERROR");
			conn.output("425 Can't open data Connection");
		}
		path = conn.getRootPath()+conn.getCurrentPath();
		try { 
			File dir = new File(path);
			File[] fileList = dir.listFiles();
			for(int i=0;i<fileList.length; i++ ){
				
				out = out + fileList[i].getName() + '\n';
			}
		}catch(Exception e){
		}
		try { 
			dataOutFlow.write(out, 0, out.length() );
			//dataOutFlow.flush();
			dataOutFlow.close();
		}catch(Exception e) {
			
		}
		conn.dataSocketClose();
		
		conn.output("226 LIST transfer complete");
	}
	
	/*public void output(String out) {
		try {
			dataOutFlow.println(out);
			//System.out.println("Send to Cient -> " + out);
		} catch (Exception e) {
			//System.out.println("Error printing to outflow");
		}
	}*/
}
