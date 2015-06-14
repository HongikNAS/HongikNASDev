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

public class FtpConnection implements Runnable {
	public final static int NOT_EXIST_CONNECTED_SOCKET = 0;

	private Socket incoming;
	private Login account;
	private PrintWriter outflow;
	private BufferedReader inflow;
	private boolean isRunning;
	private final int debug = 1;

	private Socket dataClientSocket;
	private String dataClientAddress;
	private Integer dataClientPort = NOT_EXIST_CONNECTED_SOCKET;

	private final String ROOTPATH; // never changed
	private String currentPath;

	/*
	 * public FtpConnection(Socket i) {
	 * 
	 * System.out.println("Conn Setting Up"); incoming = i;
	 * System.out.println("Conn Set up Done"); }
	 */

	public FtpConnection(Socket i, String rPATH) {

		incoming = i;
		account = new Login();
		ROOTPATH = rPATH; // root PATH
	}

	/******* setter and getter *******/
	public Socket getDataSocket() {
		// TODO check passive mode

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

	public void setDataClientPort(Integer port) {
		dataClientPort = port;
	}

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
		return isRunning;
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
		isRunning = false;
	}

	public void dataSocketClose() {
		dataClientSocket = null;
		dataClientAddress = "";
		dataClientPort = NOT_EXIST_CONNECTED_SOCKET;
	}

	public void printReceviceMessage(String str) {
		if (debug == 1)
			System.out.println(str);
	}

	public void start(boolean isConnect) {
		try {
			outflow = new PrintWriter(new OutputStreamWriter(
					incoming.getOutputStream()), true);
			inflow = new BufferedReader(new InputStreamReader(
					incoming.getInputStream())); // BufferedReader?
		} catch (Exception e) {

			System.err.println("Stream ERROR");
		}

		System.out.println("FTP CONNECTION ESTABLISHED");
		output("220 (login HongikNAS)"); // connection established
		System.out.println(isConnect);
		if (isConnect) { // true

			isRunning = true;
			new Thread(this).start();
		} else {
			output("221 Too Many User");
			System.err.println("Too many user");
			//TODO remove
			System.exit(1);
		}
	}

	public void run() {

		String recMessage;
		// control channel
		while (isRun()) {
			try {
				System.out.println("Waiting For Message");
				recMessage = inflow.readLine();
				printReceviceMessage(recMessage);
				FtpCommand.analyzer(recMessage, this);

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

/*
 * public boolean ftpLogin() {
 * 
 * String recMessage;// , id, password; // id = password = ""; try { recMessage
 * = inflow.readLine(); printRecMessage(recMessage);
 * FtpCommand.analyzer(recMessage, this); // System.out.println(id);
 * 
 * output("331 Please specify the password"); recMessage = inflow.readLine();
 * printRecMessage(recMessage); FtpCommand.analyzer(recMessage, this); //
 * System.out.println(password);
 * 
 * } catch (IOException e) { // IOException??
 * 
 * output("530 user information not come properly"); connectionClose(); return
 * false; } // read id and password
 * 
 * if (account.isAuthorized()) { output("230 User logged in"); return true; }
 * else { output("530 Login incorrect"); return false; }
 * 
 * }
 */
