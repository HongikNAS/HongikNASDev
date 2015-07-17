package kr.ac.hongik.nas.ftpserver.command.list;

import java.util.StringTokenizer;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.FtpDataConnection;
import kr.ac.hongik.nas.ftpserver.command.COMM;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;

public class TYPE extends COMM {
	
	public void excute(FtpConnection conn, String in) throws CommandSyntaxException {
		
		StringTokenizer st = new StringTokenizer(in, " ", false);
		FtpDataConnection dataConn = conn.getFtpDataConnection();
		String typeCharater="", secondCharacterType="";
		
		if( st.hasMoreTokens() ) {
			typeCharater = st.nextToken();
		}
		if( st.hasMoreTokens() ) {
			secondCharacterType = st.nextToken();
		}
		
		//TODO FIX STRINGTOKENIZER ERROR
		System.err.println(typeCharater);
		if( typeCharater == "" ){
			
			throw new CommandSyntaxException();
		}
		else if( typeCharater == "I" ) { 
			
			dataConn.setModeBinary(true);
		}
		else if(typeCharater == "A") { 
			
			dataConn.setModeASCII(true);
			if( secondCharacterType == "" ) { 
				throw new CommandSyntaxException();
			}
		}
		else {
		}
		conn.controlConnOutput("200 TYPE request complete");
	}
}