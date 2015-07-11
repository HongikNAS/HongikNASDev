package kr.ac.hongik.nas.ftpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//import java.util.Vector;

/**
 * 
 * @author Arubirate
 *
 */
public class FtpServer {

	private Socket connection;
	private ServerSocket serverSocket;

	private Thread[] connectionSlot;
	private FtpConfig ftpConfig;

	public FtpServer(FtpConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}
	
	public void start() {
		
		connectionSlot = new Thread[ftpConfig.getConnectionLimit()];
		serverSocket = createServerSocket(ftpConfig.getPort());
		
		if(serverSocket == null){
			//TODO : need to error log management class
			System.err.println("ERROR : Server initalization is fail (cannot bind port)");
			return;
		}
		
		System.out.println("FTP Server by HongikNAS");
		
		while (true) {
			try {
				connection = serverSocket.accept();
				
				System.out.println("FTP CONNECTION REQUEST");
				System.out.println(connection.getRemoteSocketAddress());
				
				int slotIndex = findEmptyConnectionSlotIndex();
				if(slotIndex == -1){
					//TODO :  need to error log management class
					System.err.println("Error : Too many Connection");
					connectDeny();
				}
				else {
					addSlot(slotIndex);
				}
			
			} catch (IOException e1) {
				//TODO : need to error log management class
				System.err.println("ERROR : FTP SERVER IS GONE");
				return;
			}
		}
	}
	
	/**
	 * Find empty slot from connectionSlot
	 * If connectionSlot[index] is null or state is "run", slot is empty.
	 * 
	 * @return int
	 */
	private int findEmptyConnectionSlotIndex(){
		int emptyIndex = -1;
		
		for (int i = 0; i < connectionSlot.length; i++) {
			if (connectionSlot[i] == null || connectionSlot[i].isAlive() == false) {
				emptyIndex = i;
				break;
			}
		}
		
		return emptyIndex;
	}
	
	/**
	 * Add Connection to connectionSlot
	 * 
	 * @param slotIndex
	 */
	private void addSlot(int slotIndex){
		FtpConnection newConnection = new FtpConnection(connection, ftpConfig.getRootPath());
		connectionSlot[slotIndex] = new Thread(newConnection);
		connectionSlot[slotIndex].start();
	}
	
	/**
	 * If you need to deny connection request, use this function.
	 */
	private void connectDeny(){
		FtpConnection newConnection = new FtpConnection(connection, ftpConfig.getRootPath());
		newConnection.deny("Too many User"); // just send 221 code
	}
	
	/**
	 * Create Server Socket
	 * If failure of creation, return null.
	 * 
	 * @param port
	 * @return ServerSocket 
	 */
	private ServerSocket createServerSocket(int port){
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			//do nothing
		}
		return serverSocket;
	}
}