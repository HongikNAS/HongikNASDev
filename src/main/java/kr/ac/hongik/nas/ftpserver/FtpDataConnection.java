package kr.ac.hongik.nas.ftpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import kr.ac.hongik.nas.HongikFtpServer;

/**
 * 
 * @author arubirate handling dataConnection
 */
public class FtpDataConnection {

	public final static int SOCKET_PORT_NOT_EXSIT = 0;
	public final static String SOCKET_ADDRESS_NOT_EXSIT = "";
	public final static Socket SOCKET_NOT_EXSIT = null;
	public final static boolean BINARY_MODE = false;
	public final static boolean ASCII_MODE = true;

	private Socket dataSocket;
	private String dataSocketAddress;
	private int dataSocketPort = 0;
	private boolean isConnected = false;
	private boolean dataMode=BINARY_MODE; // if false binary

	private PrintWriter dataConnOutWriter;
	private OutputStream dataConnOutstream;
	//private BufferedReader dataConnInReader;

	/**
	 * @author arubirate
	 * constructor
	 */
	public FtpDataConnection() {
		isConnected = false;
		dataSocketPort = SOCKET_PORT_NOT_EXSIT;
		dataSocketAddress = SOCKET_ADDRESS_NOT_EXSIT;
		dataSocket = SOCKET_NOT_EXSIT;
	}

	/**
	 * @author arubirate set mode(binary or ascii)
	 * @param mode
	 */
	public void setModeBinary(boolean mode) {
		if (mode) {
			dataMode = BINARY_MODE;
		} else {
			dataMode = ASCII_MODE;
		}
	}

	/**
	 * @author arubirate set mode(binary or ascii)
	 * @param mode
	 */
	public void setModeASCII(boolean mode) {
		if (mode) {
			dataMode = ASCII_MODE;
		} else {
			dataMode = BINARY_MODE;
		}
	}

	/**
	 * @author arubirate set DataClientSocket Port
	 * @param inPort
	 * @throws Exception
	 *             - if dataSocketPort is EXSITED
	 */
	public void setPort(int inPort) throws Exception {
		if (dataSocketPort != SOCKET_PORT_NOT_EXSIT)
			throw new Exception();
		dataSocketPort = inPort;
	}

	/**
	 * @author arubirate return dataSocket Port
	 * 
	 * @return Integer
	 */
	public int getPort() {
		return dataSocketPort;
	}

	/**
	 * @author arubirate set dataSocket Address
	 * 
	 * @param inAddress
	 */
	public void setAddress(String inAddress) throws Exception {
		if (dataSocketAddress != SOCKET_ADDRESS_NOT_EXSIT)
			throw new Exception();
		dataSocketAddress = inAddress;
	}

	/**
	 * @author arubirate return dataSocket Address
	 * 
	 * @return String
	 */
	public String getAddress() {
		return dataSocketAddress;
	}

	/**
	 * @author arubirate setDataSocket, outstream and instream
	 * 
	 * @param inAddress
	 * @param inPort
	 */
	public void setDataConnection(String inAddress, int inPort) throws Exception {

		if (!isConnected) { // don't have connected dataSocket

			try {
				setAddress(inAddress);
				setPort(inPort);

				dataSocket = makeDataSocket();
				if( dataMode == BINARY_MODE ) {
					dataConnOutstream = makeDataConnOutstream();
				} else {
					dataConnOutWriter = makeDataConnOutWriter();
				}
				
				isConnected = true;
				//dataConnInstream = makeDataConnInStream();

			} catch (IOException e) {
				System.err
						.println("FtpDataConnection ERROR : Cannot Connect DataSocket Or Cannot Make Stream");
				throw new Exception();
			} catch (Exception e) {
				disconnect(); // close former connection
				throw new Exception();
			}
		} else {
			throw new Exception();
		}
	}

	/**
	 * @author arubirate
	 * make DatConnOutWriter and return it
	 * @return PrintWriter
	 * @throws IOException
	 */
	public PrintWriter makeDataConnOutWriter() throws IOException {

		PrintWriter pWriter;
		try {
			System.out.println("make Writer");
			pWriter = new PrintWriter(new OutputStreamWriter(
					dataSocket.getOutputStream()), true);
		} catch (IOException e) {
			throw new IOException();
		}
		return pWriter;
	}

	/**
	 * @author arubirate
	 * make DataConnOutstream and return it
	 * @return
	 * @throws IOException
	 */
	public OutputStream makeDataConnOutstream() throws IOException {
		
		OutputStream pStream;
		try { 
			System.out.println("make stream");
			pStream = dataSocket.getOutputStream();
		} catch(IOException e) {
			throw new IOException();
		}
		return pStream;
	}
	
	/**
	 * @author arubirate
	 * generate dataSocket
	 * @return
	 * @throws IOException
	 */
	public Socket makeDataSocket() throws IOException {
		// TODO check passive mode
		Socket sock;
		try {
			if (HongikFtpServer.isDebugMode)
				System.out.println(dataSocketAddress + ' ' + dataSocketPort);
			sock = new Socket(dataSocketAddress, dataSocketPort);
		} catch (IOException e) {
			throw new IOException();
		}

		return sock;
	}

	/**
	 * @author arubirate
	 * send data by dataSocket
	 * @param ptr
	 */
	public void dataConnOutput(String ptr) {
	
		if( dataMode == BINARY_MODE ){
			try { 
				byte[] msg = ptr.getBytes();
				dataConnOutstream.write(msg);
				dataConnOutstream.flush();
			} catch (IOException e) {
				//TODO deal IOException
			}
		} else {
			dataConnOutWriter.println(ptr);
		}
	}
	
	
	public boolean isConnected() {
		return isConnected;
	}
	/**
	 * @author arubirate
	 * disconnect dataSocket
	 */
	public void disconnect() {
		isConnected = false;
		dataSocketAddress = SOCKET_ADDRESS_NOT_EXSIT;
		dataSocketPort = SOCKET_PORT_NOT_EXSIT;
		try {
			//dataConnInstream.close();
			if( dataMode == BINARY_MODE ) {
				dataConnOutstream.close();
			} else {
				dataConnOutWriter.close();
			}
			dataSocket.close();
		} catch (IOException e) {
			if (HongikFtpServer.isDebugMode)
				System.err
						.println("FtpDataConnection ERROR : DataSocket Close Not Properly");
		}
	}
}
