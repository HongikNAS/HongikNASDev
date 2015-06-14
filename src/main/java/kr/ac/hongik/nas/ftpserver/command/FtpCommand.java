package kr.ac.hongik.nas.ftpserver.command;

import java.util.HashMap;
import java.util.StringTokenizer;

import kr.ac.hongik.nas.ftpserver.FtpConnection;
import kr.ac.hongik.nas.ftpserver.command.list.*;
import kr.ac.hongik.nas.ftpserver.exception.CommandNullException;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;
/**
 * 
 * @author Arubirate
 *
 */
public class FtpCommand {

	static private final HashMap<String, COMM> CommandList;
	static {
		CommandList = new HashMap<String, COMM>();
		CommandList.put("USER", new USER());
		CommandList.put("PASS", new PASS());
		CommandList.put("SYST", new SYST());
		CommandList.put("QUIT", new QUIT());
		CommandList.put("PORT", new PORT());
		CommandList.put("LIST", new LIST());
		CommandList.put("UNKNOWN", new Unknown());
	}

	static public void analyzer(String in, FtpConnection conn) 
			throws CommandNullException, CommandSyntaxException {

		StringTokenizer st = new StringTokenizer(in);
		COMM func;
		String funcName="", inData="";
		
		if( st.hasMoreTokens() ) { // multithread enviroment (is it ok?) 
			funcName = st.nextToken();
			if( st.hasMoreTokens() )
				inData = st.nextToken();
		}
		
		// Should check login Option
		
		if( funcName != "" ) {
			if( (func = CommandList.get(funcName)) == null  ) { 
				func = CommandList.get("UNKNOWN");
			}
			try { 
				func.excute(conn, inData);
			} catch(CommandSyntaxException e) {
				conn.output(e.errorMessage());
			}
			
		}else {
			throw new CommandNullException();
		}
	}
}
