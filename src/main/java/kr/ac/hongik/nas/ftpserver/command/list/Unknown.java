package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;

public class Unknown extends COMM {
	public void excute(FtpConnection conn, String in)
	{
		conn.output("400 Requested action aborted. It is not defined yet.");
		// Devloping is Hard! :(...
	}
}
