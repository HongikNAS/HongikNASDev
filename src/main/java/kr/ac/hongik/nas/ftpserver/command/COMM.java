package kr.ac.hongik.nas.ftpserver.command;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;

abstract public class COMM {

	abstract public void excute(FtpConnection conn, String in) throws CommandSyntaxException;
}
