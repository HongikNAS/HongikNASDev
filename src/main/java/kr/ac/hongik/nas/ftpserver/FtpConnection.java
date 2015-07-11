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
/**
 * 
 * @author arubirate
 *
 */
public class FtpConnection implements Runnable {
	
	public final static int NOT_EXIST_CONNECTED_SOCKET = 0;
	private final String ROOTPATH; // never changed
	
	private Socket incoming;
	private Login account;
	private PrintWriter outflow;
	private BufferedReader inflow;
	private boolean running;
	private final boolean debug = true;

	private Socket dataClientSocket;
	private String dataClientAddress;
	private Integer dataClientPort = NOT_EXIST_CONNECTED_SOCKET;

	private String currentPath;

	/*
	 * Constructor
	 */
	public FtpConnection(Socket i, String rPATH) {

		running = true;
		incoming = i;
		account = new Login();
		ROOTPATH = rPATH; // root PATH
		dataClientPort = NOT_EXIST_CONNECTED_SOCKET;
		try {
			outflow = new PrintWriter(new OutputStreamWriter(
					incoming.getOutputStream()), true);
			inflow = new BufferedReader(new InputStreamReader(
					incoming.getInputStream())); 
		} catch (Exception e) {
			System.err.println("Stream ERROR");
			running = false;
		}
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
	/*
	 * set DataSocketPort
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

	public boolean isRun() {
		return running;
	}

	/******* end of setter and getter *******/

	public void output(String out) {
		try {
			outflow.println(out);
			System.out.println("Send to Cient -> " + out);
		} catch (Exception e) {
			System.out.println("Error printing to outflow");
		}
	}

	public void connectionClose() {
		running = false;
		dataSocketClose();
	}

	public void dataSocketClose() {
		dataClientSocket = null;
		dataClientAddress = "";
		dataClientPort = NOT_EXIST_CONNECTED_SOCKET;
	}

	public void printReceviceMessage(String str) {
		if (debug)
			System.out.println(str);
	}


	public void deny(String reason) {
		
		output("221 " + reason);
		System.err.println(reason);
	}
	public void run() {

		String receiveMessage;
		
		if( isRun() ) {
			System.out.println("FTP CONNECTION ESTABLISHED");
			output("220 (login HongikNAS)"); // connection established
		} else {
			deny("Unknown Error");
		}
		
		while (isRun() ) {
			try {
				System.out.println("Waiting For Message");
				receiveMessage = inflow.readLine();
				printReceviceMessage(receiveMessage);
				FtpCommand.analyzer(receiveMessage, this);
			
			} catch (CommandNullException e) {
				output("421 No Command - Connection Close");
				System.out.println("No Command");
				connectionClose();
			
			} catch (Exception e) {

				output("421 Unknown Error Occured");
				System.out.println("ERROR");
				connectionClose();
			}
		}
	}
}

