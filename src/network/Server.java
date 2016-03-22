package network;

import models.*;
import rulesengine.*; // does contain parser, etc?

import java.net.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import java.io.*;

public class Server{
	public enum ServerState {
		WAITING_FOR_FIRST, WAITING_FOR_ALL, CREATE_GAME, IN_GAME
	}
	
	static final Logger             log = Logger.getLogger("Server");
	public static final int         MAX_PLAYERS = 2;
	public static final int         PORT = 5000;
	private ServerSocket            serverSocket;
	private GameState               game;
	private ArrayList<Player>       players;
	private ArrayList<Socket>       sockets;
    private ArrayList<OutputThread> out;
    private ArrayList<InputThread>  in;
    private int          	        maxPlayers;
	private String                  updateString;
	private ServerState             serverState;
	
	public Server(int port) throws IOException {
		this.maxPlayers = 1;
		sockets = new ArrayList<Socket>();
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(1000000);
		players = new ArrayList<Player>();
		in  = new ArrayList<InputThread>();
		out = new ArrayList<OutputThread>();
		serverState = ServerState.WAITING_FOR_FIRST;
	}
	
	public ServerState getServerState() {
		return serverState;
	}
	
	public int getCurrentNumPlayers() {
		return players.size();
	}
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public int getPlayerPort(int player) {
		return sockets.get(player).getLocalPort();
	}
	
	public InetAddress getPlayerInetAddress(int player) {
		return sockets.get(player).getLocalAddress();
	}
	
	public int getPlayerClientPort(int player) {
		return sockets.get(player).getPort();
	}
	
	public InetAddress getPlayerClientInetAddress(int player) {
		return sockets.get(player).getInetAddress();
	}
	
	public void killServer() {
		for (int i=0; i<players.size(); i++) {
			out.get(i).killThread();
			in.get(i).killThread();
		}
	}
	
	public void setMaxPlayers(int maxPlayers) throws IndexOutOfBoundsException {
		if (maxPlayers < 2 || maxPlayers > 5) {
			throw new IndexOutOfBoundsException("Number of players must be from 2 to 5");
		}
		this.maxPlayers = maxPlayers;
	}
	
	public InetAddress acceptPlayer() {
		InetAddress result;
		try {
			Socket pSocket = serverSocket.accept();
			InputThread playerIn = new InputThread(new BufferedReader (new InputStreamReader(pSocket.getInputStream())));
			OutputThread playerOut = new OutputThread (new PrintWriter(pSocket.getOutputStream(), true));
			
			in.add(playerIn);
			out.add(playerOut);
			
			String name = null;
			try {
				while(!playerIn.hasMessage()) {}
				name = playerIn.readMessage();
			} catch (Exception e) {
				throw new RuntimeException("Input thread has died.");
			}
			
			updateClients(Flag.PLAYER_ID + ":" + in.size());
			// This info will get sent out later for the first player, in waitForFirstPlayerSetupInfo()
			if (in.size() > 1) {
				updateClients(Flag.MAX_PLAYERS + ":" + maxPlayers);
				updateClients(Flag.CURRENT_NUM_PLAYERS + ":" + in.size());
			}
			
            Player p = new Player(name);
            System.out.println(name);
            players.add(p);
            sockets.add(pSocket);
            result = getPlayerClientInetAddress(players.size()-1);
            return result;
		}
		catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
		}
		catch(IOException e) {
            e.printStackTrace();
		}
		return null;
		
	}

	public Object[] getUpdate() {
		Object[] update = null;
		try {
			while(update == null) {
				for (int i=0; i<in.size(); i++) {
					if (in.get(i).hasMessage()) {
						update = new Object[2];
						update[0] = i;
						update[1] = in.get(i).readMessage();
						break;
					}
				}
			}
		} catch (Exception e) {
			killServer();
			throw new RuntimeException("Input thread has died.");
		}
		
		return update;
	}

	public void updateClients(String update) throws IOException {
		for(int i = 0; i < out.size(); i++) {
			log.info("Sending to client " + i +": " + update);
         	out.get(i).sendMessage(update);
		}
	}
	
	public void waitForPlayer() {
		acceptPlayer();
		
		if (players.size() == maxPlayers) {
			serverState = ServerState.CREATE_GAME;
		}		
	}
	
	public boolean waitForFirstPlayerSetupInfo() {
		System.out.println("Waiting for setup...");
		Object[] update = getUpdate();
		int player = (int)update[0];
		String[] info = ((String)update[1]).split(":");
	
		if (!info[0].equals(Flag.MAX_PLAYERS_SET)) {
			return false;
		}
		setMaxPlayers(Integer.parseInt(info[1]));
		
		out.get(0).sendMessage(Flag.MAX_PLAYERS_SET + ":" + maxPlayers);
		out.get(0).sendMessage(Flag.MAX_PLAYERS + ":" + maxPlayers);
		out.get(0).sendMessage(Flag.CURRENT_NUM_PLAYERS + ":" + in.size());
		
		serverState = ServerState.WAITING_FOR_ALL;
		
		System.out.println("Server set up for " + maxPlayers + " players.");
		
		return true;
	}
		
	public void createGame() throws IOException {
		game = new GameState();
		updateString = game.initializeServer(players); // make initializeServer return shuffled array of cards as string
		updateClients(updateString);
		RulesEngine.startGame(game); // this should call static rules engine method and start first tournament
		serverState = ServerState.IN_GAME;
	}
	
	public void gameIteration() throws IOException {
		updateString = (String)getUpdate()[1];
		updateString = Parser.networkSplitter(updateString, game);
		System.out.println("Message to clients: " + updateString);
		
		updateClients(updateString);
	}
	
	public void handleState(ServerState st) throws IOException {
		switch (st) {
			case WAITING_FOR_FIRST:
				waitForPlayer();
				while(!waitForFirstPlayerSetupInfo()) {}
				break;
			case WAITING_FOR_ALL:
				waitForPlayer();
				break;
			case CREATE_GAME:
				createGame();
				break;
			case IN_GAME:
				gameIteration();
				break;
		}
	}

	public void serverLoop() throws IOException {
		while(true) {
			handleState(serverState);
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(PORT);
			server.serverLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

