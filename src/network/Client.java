package network;

import models.*;
import org.apache.log4j.Logger;
import rulesengine.*;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class Client implements Runnable {
	public static final String NO_MESSAGE = "";
	public static final String CONNECT_PASSED = "pass";
	public static final String CONNECT_FAILED = "fail";
	
	private GameState game;
	private BufferedReader in;
	private OutputThread out;
	private int id;
	private String updateIn;
	
	private String serverName;
	private String username;
	
	private String connectStatus;
	static final Logger log = Logger.getLogger("Client");
	
	public int getID() {
		return id;
	}

	// constructor
	public Client() {
		guiFlags = new LinkedList<String>();
		game = new GameState();
		updateIn = "";
		connectStatus = "";
	}

	public GameState getGame() {
		synchronized (game) {
			return game;
		}
	}
	

	public void sendMessage(String message) {
		out.sendMessage(message);
	}

	public boolean waitingForConnection() {
		synchronized(connectStatus) {
			return connectStatus == NO_MESSAGE;
		}
	}
	public boolean connectPassed() {
		synchronized(connectStatus) {
			boolean result = (connectStatus == CONNECT_PASSED);
			connectStatus = NO_MESSAGE;
			return result;
		}
	}
	
	public void connect(String serverName, String username) {
		this.serverName = serverName;
		this.username = username;
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		String serverName = "localhost";
		int port = Server.PORT;
		try {
			System.out.println("Connecting to " + serverName + " on port "
					+ port);
			Socket socket = new Socket(serverName, port);
			synchronized(connectStatus) {
				connectStatus = CONNECT_PASSED;
			}
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());

			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new OutputThread(new PrintWriter(socket.getOutputStream(), true));

			id = Integer.parseInt(in.readLine());
			log.info(id +": " + username);
			out.sendMessage(username);

			for (;;) {
				updateIn = in.readLine();
				Parser.networkSplitter(updateIn, game);
				synchronized (guiFlags) {
					Parser.guiSplitter(guiFlags, updateIn);
				}
			}
			// socket.close();
		} catch (Exception e) {
			synchronized(connectStatus) {
				connectStatus = CONNECT_FAILED;
			}
		}
	}

	/*
	 * ==========================================================================
	 * =========== Creating guiFlags using a Queue where the flags will be
	 * stored
	 */

	private Queue<String> guiFlags;

	// check if the flag queue is empty
	// this is used by the GUI to figure out whether there is a need
	// to update the UI or not
	public boolean hasFlags() {
		synchronized (guiFlags) {
			return !guiFlags.isEmpty();
		}
	}

	
	//return guiFlags queue for testing purposes 
	public Queue<String> getGuiFlags() {
		synchronized(guiFlags) {
			return guiFlags;
		}
	}
	
	public String readGuiFlag() {
		synchronized (guiFlags) {
			String flag = guiFlags.remove();
			return flag;
		}
	}
	
	
	/*
	 * End of Flags preparation
	 * ==================================================
	 * ====================================
	 */

}
