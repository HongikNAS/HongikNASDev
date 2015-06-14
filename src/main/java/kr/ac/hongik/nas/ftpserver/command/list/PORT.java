package kr.ac.hongik.nas.ftpserver.command.list;

import java.util.StringTokenizer;

import kr.ac.hongik.nas.ftpserver.command.*;
import kr.ac.hongik.nas.ftpserver.exception.CommandSyntaxException;
import kr.ac.hongik.nas.ftpserver.FtpConnection;

public class PORT extends COMM {

	public void excute(FtpConnection conn, String in) throws CommandSyntaxException {
		
		StringTokenizer st = new StringTokenizer(in, ",");
		String ipAddress="";
		Integer port=0;
		int countToken=0;
		
		// ip 4 times
		for(int i=0;i<4 && st.hasMoreTokens();i++) {
			if( i != 0 ) ipAddress = ipAddress + ".";
			ipAddress = ipAddress + st.nextToken();
			countToken += 1;
		}
		// port 2 times
		for(int i=0;i<2 && st.hasMoreTokens(); i++) {
			if( i != 0 ) port = (port.intValue() << 8);
			port += new Integer(st.nextToken()).intValue();
			countToken += 1;
		}
		if( countToken != 6 || (port.intValue() < 0 || port.intValue() > 65536))
			throw new CommandSyntaxException() ; // port number too low or too high 
		
		conn.setDataClientAddress(ipAddress);
		conn.setDataClientPort(port);
		
		conn.output("200 Port Setting Complete");
	}
}
