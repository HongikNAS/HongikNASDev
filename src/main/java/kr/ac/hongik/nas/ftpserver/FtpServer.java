package kr.ac.hongik.nas.ftpserver;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Arubirate
 *
 */
public class FtpServer implements Runnable {

	boolean isRun;
	Socket connection;
	ServerSocket server;
	final int debug = 1;

	FtpConnection[] ftpConnState;
	private FtpConfig config;

	// FtpConfig
	// Connection Limit ����
	// Server Port ����

	public FtpServer() {
		config = new FtpConfig(); // read config;
		ftpConnState = new FtpConnection[config.getConnectionLimit()];
		try {
			server = new ServerSocket(config.getPort());
		} catch (Exception e) {
			System.err.println("SERVER PORT IS NOT SET");
			System.err.println(e);
			System.exit(1); // TODO should remove
		}
	}

	public boolean running() {
		return isRun;
	}

	public static void main(String[] args) {
		FtpServer ftpServer = new FtpServer();
		Thread serverThread = new Thread(ftpServer);
		serverThread.start();
	}

	public void run() {
		// TODO Auto-generated method stub
		isRun = true;
		System.out.println("FTP Server by HongikNAS");
		while (running()) {
			boolean isConnected = false;
			try {
				connection = server.accept();
				if (debug == 1)
					System.out.println("FTP CONNECTION REQUEST");
				
				//TODO define findEmptyConnect()
				for (int j = 0; j < config.getConnectionLimit(); j++) {

					if (ftpConnState[j] == null || ftpConnState[j].isRun() == false) {
						
						ftpConnState[j] = new FtpConnection(connection, config.getRootPath());
						ftpConnState[j].start(true);
						isConnected = true;
						break;
					}
				}
				if (isConnected == false) {
					System.err.println("Error : Too many Connection");
					FtpConnection ftpTemp = new FtpConnection(connection, config.getRootPath());
					ftpTemp.start(false); // just send 221 code
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("FTP SERVER IS GONE");
				// TODO request remove
				System.exit(1);
			}
		}
	}
}