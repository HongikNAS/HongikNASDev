package kr.ac.hongik.nas;

import kr.ac.hongik.nas.ftpserver.FtpConfig;
import kr.ac.hongik.nas.ftpserver.FtpServer;

public class HongikFtpServer {
	public static void main(String[] args) {
		FtpConfig ftpConfig = new FtpConfig();
		FtpServer ftpServer = new FtpServer(ftpConfig);
		
		ftpServer.start();
	}
}
