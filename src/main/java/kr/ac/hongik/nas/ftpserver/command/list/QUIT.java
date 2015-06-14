package kr.ac.hongik.nas.ftpserver.command.list;

import kr.ac.hongik.nas.ftpserver.command.*;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;
import kr.ac.hongik.nas.ftpserver.FtpConnection;

public class QUIT extends COMM {

	public void excute(FtpConnection conn, String in)  throws CommandSyntaxException {
		conn.output("221 Connection Close");
		conn.connectionClose();
	}
}
