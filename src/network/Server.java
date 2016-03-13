package network;

import models.*;
import rulesengine.*; // does contain parser, etc?

import java.net.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import java.io.*;

public class Server{
	private enum ServerState {
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
    private int          	        numOfPlayers;
	private String                  updateString;
	private ServerState             serverState;
	
	public Server(int port, int maxPlayers) throws IOException {
		numOfPlayers = maxPlayers;
		sockets = new ArrayList<Socket>();
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(1000000);
		players = new ArrayList<Player>();
		in  = new ArrayList<InputThread>();
		out = new ArrayList<OutputThread>();
		serverState = ServerState.WAITING_FOR_ALL;
	}
	
	public int getNumOfPlayers() {
		return numOfPlayers;
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
	
	public InetAddress acceptPlayer() {
		InetAddress result;
		try {
			Socket pSocket = serverSocket.accept();
			InputThread playerIn = new InputThread(new BufferedReader (new InputStreamReader(pSocket.getInputStream())));
			OutputThread playerOut = new OutputThread (new PrintWriter(pSocket.getOutputStream(), true));
			
			in.add(playerIn);
			out.add(playerOut);
			
			for (OutputThread o: out) {
				o.sendMessage(""+players.size());
			}
			
			String name = null;
			try {
				while(!playerIn.hasMessage()) {}
				name = playerIn.readMessage();
			} catch (Exception e) {
				throw new RuntimeException("Input thread has died.");
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

	private Object[] getUpdate() {
		Object[] update = null;
		try {
			while(update == null) {
				for (int i=0; i<numOfPlayers; i++) {
					if (in.get(i).hasMessage()) {
						update = new Object[2];
						update[0] = i;
						update[1] = in.get(i).readMessage();
						break;
					}
				}
			}
		} catch (Exception e) {
			for (int i=0; i<numOfPlayers; i++) {
				out.get(i).killThread();
				in.get(i).killThread();
			}
			throw new RuntimeException("Input thread has died.");
		}
		
		return update;
	}

	private void updateClients(String update) throws IOException {
		for(int i = 0; i < players.size(); i++) {
			log.info("Sending to client " + i +": " + update);
         	out.get(i).sendMessage(update);
		}
	}
	
	private String waitForPlayer() {
		acceptPlayer();
		
		if (players.size() == numOfPlayers) {
			serverState = ServerState.CREATE_GAME;
		}
		
		return updateString;
	}
	
	private void createGame() throws IOException {
		game = new GameState();
		updateString = game.initializeServer(players); // make initializeServer return shuffled array of cards as string
		updateClients(updateString);
		RulesEngine.startGame(game); // this should call static rules engine method and start first tournament
		serverState = ServerState.IN_GAME;
	}
	
	private void gameIteration() throws IOException {
		updateString = (String)getUpdate()[1];
		updateString = Parser.networkSplitter(updateString, game);
		System.out.println("Message to clients: " + updateString);
		
		updateClients(updateString);
	}
	
	private void handleState(ServerState st) throws IOException {
		switch (st) {
			case WAITING_FOR_FIRST:
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

	private void serverLoop() throws IOException {
		while(true) {
			handleState(serverState);
		}
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(PORT, MAX_PLAYERS);
			server.serverLoop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

