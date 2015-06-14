package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;

public class USER extends COMM {

	@Override
	public void excute(FtpConnection conn, String in)
			throws CommandSyntaxException {
		// if( in == "" || in == null ) No input
		conn.getAccount().setUser(in);
		conn.output("331 Please Specified Password"); // Sending Message
	}
}