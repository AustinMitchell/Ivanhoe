package network;

import models.*;
import rulesengine.*; // does contain parser, etc?
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server{
	public static final int   NUM_PLAYERS = 2;
	public static final int   PORT = 5000;
	private BufferedReader    streamIn = null;
	private BufferedWriter    streamOut = null;
	private ServerSocket      serverSocket;
	private RulesEngine       rules;
	private GameState         game;
	private int          	  numOfPlayers;
	private ArrayList<Player> players;
	private ArrayList<Socket> sockets;

	//private ArrayList<OutputStream> toClientStreams;
	//private ArrayList<InputStream> fromClientStreams;
	//private ArrayList<BufferedReader> fromClientStreams;
	
    private ArrayList<PrintWriter> out;
    private ArrayList<BufferedReader> in;

	private String updateString;
	
	public Server(int port, int maxPlayers) throws IOException {
		numOfPlayers = maxPlayers;
		sockets = new ArrayList<Socket>();
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(1000000);
		players = new ArrayList<Player>();
		in  = new ArrayList<BufferedReader>();
		out = new ArrayList<PrintWriter>();
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
			BufferedReader playerIn = new BufferedReader (new InputStreamReader(pSocket.getInputStream()));
			PrintWriter playerOut = new PrintWriter(pSocket.getOutputStream(), true);
			
			in.add(playerIn);
			out.add(playerOut);
			
			playerOut.println(players.size());
            String name = playerIn.readLine();
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

	public void setupUpdateStreams() throws IOException {
		for(int i = 0; i < players.size(); i++) {
         	in.add(new BufferedReader(new InputStreamReader(sockets.get(i).getInputStream())));
         	out.add(new PrintWriter(sockets.get(i).getOutputStream()));
		}
	}

	public String getUpdate(int turn) throws IOException {
		String update;
        update = in.get(game.getTurn()).readLine();
		return update;
	}

	public void updateClients(String update) throws IOException {
		for(int i = 0; i < players.size(); i++) {
         	out.get(i).println(update);
		}
	}
	
	public String setupGame() {
		
		//players = new ArrayList<Player>();
		int i;
		for(i = 0; i < numOfPlayers; i++) {
			acceptPlayer();
		}
		game = new GameState();
		updateString = game.initializeServer(players); // make initializeServer return shuffled array of cards as string
		return updateString;
	}
	
	public void runGame() throws IOException {
		RulesEngine.startGame(game); // this should call static rules engine method and start first tournament
		while(true) {
			updateClients(updateString);
			updateString = getUpdate(game.getTurn());
			updateString = Parser.networkSplitter(updateString, game);
			System.out.println("Message to clients: " + updateString);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			Server server = new Server(PORT, NUM_PLAYERS);
			server.updateString = server.setupGame();
			//server.setupUpdateStreams();
			server.runGame();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		
	}
	
}

