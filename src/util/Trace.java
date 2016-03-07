package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import network.*;


public class Trace {
	
	private static Trace _instance = null;

	private Level level = Level.OFF;
	
	private FileWriter clientLogger = null;
	private FileWriter serverLogger = null;
	
	/*
	 * 
	 */
	
	public Logger getLogger (Object o) {
		return Logger.getLogger(o.getClass().getName());
	}
	
	private Trace() {
		String userDir = System.getProperty("user.dir");
      String configFile = String.format("%s\\%s\\%s.properties",userDir, "properties","log4j");
      PropertyConfigurator.configure(configFile);
      
		try {
			String clientLogFile = String.format("%s//logs//Client.log", userDir);
			String serverLogFile = String.format("%s//logs//Server.log", userDir);

			/** First we need to ensure our log directory exists | if not create it */
			File logDir = new File(String.format("%s//logs", userDir));
			if (!logDir.exists()) logDir.mkdir();

			clientLogger = new FileWriter(clientLogFile);
			serverLogger = new FileWriter(serverLogFile);
	
		} catch (IOException e) {
			System.out.printf("Error while opening log file: %s\n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Trace getInstance() {
		if (_instance == null) {
			synchronized (Trace.class) {
				_instance = new Trace();
			}
		}
		return _instance;
	}

	public void setLevel (Level traceLevel) {
		this.level = traceLevel;
	}
	
	public void write(Object o, String m) {
		try {
			serverLogger.write(format(o,m));
		} catch (IOException e) {
			System.err.printf("Exception %s thrown from %s\n", this, e.getMessage());
		}
		if (level == Level.STDOUT) System.out.println(format(o,m));
	}

	public void writeToClientFile(Object o, String m) {
		try {
			clientLogger.write(format(o,m));
		} catch (IOException e) {
			System.err.printf("Exception %s thrown from %s\n", this, e.getMessage());
		}
		
		if (level == Level.STDOUT) System.out.println(format(o,m));
	}
	
	public void write(Object o, String m, int i) {
		String message = String.format("%s %d\n", m ,i);
		write(o,message);
	}
	
	public void write(Object o, String m, long l) {
		String message = String.format("%s %s\n", m , Long.toString(l));
		write(o,message);
	}
	
	public void write(Object o, String m, Socket socket) {
		String message = String.format("%s : Client Address : [%15s] Client Socket: [%-6d]\n", m, socket.getRemoteSocketAddress(), socket.getPort());
		write (o,message);
	}

	public void exception(Exception e) {
		String message = String.format("Exception thrown : %s \n", e.getMessage());
		//if (Config.PRINT_STACK_TRACE) e.printStackTrace();
		write(this,message);	
	}
	
	public void exception(Object o, Exception e) {
		String message = String.format("Exception thrown : %s \n", e.getMessage());
		//if (Config.PRINT_STACK_TRACE) e.printStackTrace();
		write(o,message);
	}
	
	public void exception(Object o, IOException ioe, int ID) {
		String message = String.format("Exception in client/server thread [%3d] ID [%d]\n",o.getClass().getSimpleName(),ioe,ID);
		write (o,message);
	}

	public void write(Object o, String m, ServerSocket server) {
		String message = format (m,String.format("%s:%d", server.getInetAddress(), server.getLocalPort()));
		write (o,message);
	}
	
	public void close() {
		try {
			clientLogger.close();
			serverLogger.close();
		} catch (IOException e) {
			System.out.printf("Error while closing log file: %s\n", e.getMessage());
		}
	}
	
	private String format (Object o, String message) {
		return String.format ("[Time: %23s] Class: %-12s: %s",getDateTime(),o.getClass().getSimpleName(), message) ;
	}
	
	private String getDateTime () {
		Format formatter = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss:SSS");
		return formatter.format((new Date()).getTime());
	}

}
