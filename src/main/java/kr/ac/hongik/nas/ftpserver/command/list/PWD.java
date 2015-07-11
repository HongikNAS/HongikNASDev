package kr.ac.hongik.nas.ftpserver.command.list;

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.Socket;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;

public class PWD extends COMM {

	public void excute(FtpConnection conn, String in)
			throws CommandSyntaxException {
		
		String path =conn.getRootPath() + conn.getCurrentPath();
		System.out.println(path);
		
		
		conn.output("257 \"" + path + "\" is current directory" );
	}
}
