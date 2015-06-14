package kr.ac.hongik.nas.ftpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * @author Arubirate
 *
 */
public class FtpConfig {
	public static final String CONFIG_PATH = "src/main/resource/ftp.config";

	private Scanner input;
	private int port = 21; // default port
	private int connectionLimit = 50; // default limit;
	private String rootPath=""; // if unspecified, program will be terminated;
	
	// public void readConfig()
	// public void setPort(String newPort)
	// public int getPort()
	// public void setConnectionLimit(String newLimit)
	// public int getConnectionLimit()
	
	public FtpConfig() {
		// # is comment in config file
		File config = new File(CONFIG_PATH);
		try {
			input = new Scanner(config);
			readConfig();
		} catch (FileNotFoundException e) {
			System.err.println("Error : Can't open Config file");
			System.err.println("Setting Deafult Values");
			// System.exit(1);
		}

	}

	public void readConfig() {

		try {
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if (line.charAt(0) == '#') // it means just comment
					continue;

				StringTokenizer st = new StringTokenizer(line, "=");
				String key = st.nextToken();
				if (key.compareToIgnoreCase("port") == 0)
					setPort(st.nextToken());
				else if (key.compareToIgnoreCase("connectionLimit") == 0)
					setConnectionLimit(st.nextToken());
				else if (key.compareToIgnoreCase("rootpath") == 0 ) {
					
					setRootPath(st.nextToken());
				}
			}
		} catch (NoSuchElementException e) {
			System.err.println("FTPConfig Error(3) : config file format is wrong");
			System.exit(1);
		} catch (NullPointerException e) {
			System.err.println("FTPConfig Error(2) : Can't read Config file properly");
			System.exit(1);
		} catch (IllegalStateException e) {
			System.err.println("FTPConfig Error(1) : Can't read Config file");
			System.exit(1);
		}
	}

	public void setPort(String newPort) {
		try {
			port = Integer.parseInt(newPort);
		} catch (NumberFormatException e) {
			System.err.println("FTPConfig Error(5) : Can't set Port number");
			port = 21; // default port
		}
	}

	public void setConnectionLimit(String newLimit) {
		try {
			connectionLimit = Integer.parseInt(newLimit);
		} catch (NumberFormatException e) {
			System.err.println("FTPConfig Error(6) : Can't set Limit number");
			connectionLimit = 50;// default value;
		}
	}
	public void setRootPath(String newPath) {
		// Directory가 실제하는지 확인
		File d = new File(newPath);
		if(d.isDirectory()){
			rootPath = newPath;
		}else {
			System.err.println("FTPConfig Error(7) : Can't specified rootPath");
			System.exit(1);
		}
	}
	
	public String getRootPath() {
		
		return rootPath;
	}

	public int getConnectionLimit() {
		return connectionLimit;
	}

	public int getPort() {
		return port;
	}

	public static void main(String[] args) {

		
		FtpConfig a = new FtpConfig();
		System.out.println(a.getConnectionLimit());
		System.out.println(a.getPort());
		System.out.println(a.getRootPath());
	}
}
