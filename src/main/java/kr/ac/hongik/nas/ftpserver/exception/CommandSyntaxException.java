package kr.ac.hongik.nas.ftpserver.exception;

public class CommandSyntaxException extends Exception {

	public String errorMessage() {
		return "500 Syntax error";
	}
}
