package kr.ac.hongik.nas.ftpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import kr.ac.hongik.nas.ftpserver.command.*;
import kr.ac.hongik.nas.ftpserver.exception.CommandNullException;
import kr.ac.hongik.nas.ftpserver.util.Login;
import kr.ac.hongik.nas.HongikFtpServer; // check for HongikFtpServer.isDebugMode
/**
 * 
 * @author arubirate
 *
 */
public class FtpConnection implements Runnable {
	
	public final static int NOT_EXIST_CONNECTED_SOCKET = 0;
	
	private final String ROOTPATH; // never changed
	private String currentPath;
	
	private Socket controlConnection;
	private PrintWriter controlConnOutstream;
	private BufferedReader controlConnInstream;
	
	private Login account;
	private boolean running;

	private Socket dataClientSocket;
	private String dataClientAddress;
	private Integer dataClientPort = NOT_EXIST_CONNECTED_SOCKET;


	/**
	 * Constructor
	 * @param Socket, rPATH
	 **/
	public FtpConnection(Socket i, String rPATH) {

		controlConnection = i;
		account = new Login();
		ROOTPATH = rPATH; // root PATH
		dataClientPort = NOT_EXIST_CONNECTED_SOCKET;
		
		if ( setControlConnOutstream(controlConnection) && 
				setControlConnInstream(controlConnection) ) {
			running = true;
		} else {
			if( HongikFtpServer.isDebugMode )
				System.err.println("FtpConnection ERROR : Get Stream");
			running = false;
		}
	}

	/**
	 * get controlConnOutstream
	 * @param Socket
	 * @return boolean
	 */
	public boolean setControlConnOutstream(Socket controlConn) {
		
		try { 
			controlConnOutstream = new PrintWriter(new OutputStreamWriter(
					controlConn.getOutputStream()), true);
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * get controlConnInstream
	 * @param controlConn
	 * @return boolean
	 */
	public boolean setControlConnInstream(Socket controlConn) {
		
		try {
			controlConnInstream = new BufferedReader(new InputStreamReader(
					controlConn.getInputStream())); 
		} catch(Exception e) {
			return false;
		}
		return true;
	}
	
	
	/*
	 * Make DataSocket, and return DataSocket.
	 * if it is existed, return null;
	 */
	public Socket getDataSocket() {
		// TODO check passive mode

		System.out.println(dataClientPort);
		if (dataClientPort != NOT_EXIST_CONNECTED_SOCKET) { // Port is set

			try {
				System.out.println(dataClientAddress + ' '
						+ dataClientPort.toString());
				dataClientSocket = new Socket(dataClientAddress, dataClientPort);
			} catch (IOException e) {
				System.err.println("getDataSocket() : return Null");
				return null;
			}

			return dataClientSocket;
		}
		
		return null;
	}
	/**
	 * set DataSocketPort
	 * @param Integer port
	 */
	public void setDataClientPort(Integer port) {
		dataClientPort = port;
	}

	/*
	 * get DataSocketPort
	 */
	public int getDataClientPort() {
		return dataClientPort.intValue();
	}

	public void setDataClientAddress(String address) {
		dataClientAddress = address;
	}

	public String getDataClientAddress() {
		return dataClientAddress;
	}

	public String getRootPath() {
		return ROOTPATH;
	}

	public void setCurrentPath(String in) {
		currentPath = in;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public Login getAccount() {
		return account;
	}

	public boolean getRunning() {
		return running;
	}

	/******* end of setter and getter *******/

	/**
	 * controlConnOutput
	 * Send server code by controlConnectionOutstream
	 * @param out
	 */
	public void controlConnOutput(String message) {
		try {
			
			controlConnOutstream.println(message);
			if( HongikFtpServer.isDebugMode )
				System.out.println("Send to Cient -> " + message);
		} catch (Exception e) {
			
			if( HongikFtpServer.isDebugMode )
				System.err.println("Error printing to controlConnOutstream");
		}
	}
	/**
	 * controlConnInput 
	 * Read code send by client from controlConnectionInstream
	 * @return String
	 * @exception IOException();
	 */
	public String controlConnInput() throws IOException {
		
		try { 
			String message = controlConnInstream.readLine();
			return message;
		} catch(Exception e) {
			throw new IOException();
		}
	}

	
	public void connectionClose() {
		running = false;
		dataSocketClose();
		
		try { 
			controlConnOutstream.close();
			controlConnInstream.close();
			controlConnection.close();
		} catch( IOException e ) {
			
			if( HongikFtpServer.isDebugMode ) 
				System.err.println("FtpConnection ERROR : Unable to close controlConnection");
		}
	}

	public void dataSocketClose() {
		dataClientSocket = null;
		dataClientAddress = "";
		dataClientPort = NOT_EXIST_CONNECTED_SOCKET;
	}

	public void printReceviceMessage(String str) {
		System.out.println(str);
	}


	public void connectionDeny(String reason) {
		
		controlConnOutput("221 " + reason);
		if( HongikFtpServer.isDebugMode ) 
			System.err.println(reason);
	}
	public void run() {

		String receiveMessage;
		
		if( getRunning() ) {
			if( HongikFtpServer.isDebugMode ) 
				System.out.println("FtpConnection Msg : Connection Established");
			controlConnOutput("220 (login HongikNAS)"); // connection established
		} else {
			connectionDeny("Unknown Error");
			connectionClose();
		}
		
		while ( getRunning() ) {
			try {
				if( HongikFtpServer.isDebugMode ) 
					System.out.println("Waiting For Message");
				
				receiveMessage = controlConnInput();
				
				if ( HongikFtpServer.isDebugMode ) 
					printReceviceMessage(receiveMessage);
				
				FtpCommand.analyzer(receiveMessage, this);
			
			} catch(IOException e) {
				
				controlConnOutput("421 Read null Command - Connection Close");
				if( HongikFtpServer.isDebugMode )
					System.err.println("FtpConnection ERROR : Read Null Message");
				connectionClose();
				
			} catch (CommandNullException e) {
				controlConnOutput("421 No Command - Connection Close");
				if( HongikFtpServer.isDebugMode ) 
					System.err.println("FtpConnection ERROR : No Command");
				connectionClose();
			
			} catch (Exception e) {

				controlConnOutput("421 Unknown Error Occured");
				if( HongikFtpServer.isDebugMode ) 
					System.err.println("FtpConnection ERROR : Unknown ERROR");
				connectionClose();
			}
		}
	}
}

