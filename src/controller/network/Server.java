package controller.network;

import java.net.*;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

import controller.rulesengine.*;
import model.*;

import java.io.*;

public class Server{
	public enum ServerState {
		WAITING_FOR_FIRST, WAITING_FOR_ALL, CREATE_GAME, BEGIN_DRAW_TOKEN, IN_GAME, IVANHOE
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
	
	private ArrayList<Integer>      beginDrawTokens;
	private int                     beginContinueResponses;

	// Ivanhoe action card specific stuff
	private String                  currentActionCardCommand;
	private int                     ivanhoePlayed;
	private int                     ivanhoeResponsesNeeded;
	
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
			
			playerOut.sendMessage(Flag.SERVER_ACCEPT);
			
			String name = null;
			try {
				while(!playerIn.hasMessage()) {}
				name = playerIn.readMessage();
			} catch (Exception e) {
				throw new RuntimeException("Input thread has died.");
			}
			
			if (name.equals(Flag.CLIENT_DISCONNECT)) {
				in.remove(in.size()-1);
				out.remove(out.size()-1);
				playerIn.killThread();
				playerOut.killThread();
				throw new IOException("Client disconnected");
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
				Thread.sleep(10);
				for (int i=0; i<in.size(); i++) {
					if (in.get(i).hasMessage()) {
						update = new Object[2];
						update[0] = i;
						update[1] = in.get(i).readMessage();
						break;
					}
				}
			}
			System.out.println("Message from client " + (int)update[0] + ": " + (String)update[1]);
		} catch (Exception e) {
			killServer();
			throw new RuntimeException("Input thread has died.");
		}
		
		return update;
	}

	public void updateClients(String update) throws IOException {
		System.out.println("Message to clients: " + update);
		for(int i = 0; i < out.size(); i++) {
			log.info("Sending to client " + i +": " + update);
         	out.get(i).sendMessage(update);
		}
	}
	
	// Watis to accept a new player
	public void waitForPlayer() {
		while (acceptPlayer() == null);
		
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
		
		serverState = ServerState.BEGIN_DRAW_TOKEN;
		beginContinueResponses = players.size();
		// starts arraylist with some values from 1 to 4, and definitely 0 (i.e. Type.PURPLE)
		beginDrawTokens = new ArrayList<Integer>() {{ 
			for (int i=1; i<players.size(); i++) add(i); 
			add(Type.PURPLE);
		}};
		Collections.shuffle(beginDrawTokens);
	}
	
	public void gameIteration() throws IOException {
		updateString = (String)getUpdate()[1];
		
		String[] splitString = updateString.split(":");
		// Checks if the given flag was a card 
		if (splitString[0].equals(Flag.CARD)) {
			int cardType = game.getAllPlayers().get(game.getTurn()).getHand().getCard(Integer.parseInt(splitString[1])).getCardType();
			// Checks the next argument to see if it's an action card
			if (cardType == Type.ACTION) {
				// Stores what the desired action is
				currentActionCardCommand = updateString;
				// Number of responses to Ivanhoe
				ivanhoeResponsesNeeded = players.size()-1;
				// If a player plays ivanhoe, it will be indicated through this
				ivanhoePlayed = -1;
				// Changes the state to handle ivanhoe messages
				serverState = ServerState.IVANHOE;
			}
		}
		
		updateString = Parser.networkSplitter(updateString, game);
		updateClients(updateString);
	}
	
	public void beginDrawTokenIteration() throws IOException {
		Object[] update = getUpdate();
		String updateString = (String)update[1];
		String[] splitString = updateString.split(":");
		
		if (splitString[0].equals(Flag.DRAW_TOKEN)) {
			updateString = Flag.GET_TOKEN + ":" + (Integer)update[0] + ":" + beginDrawTokens.remove(0);
			updateClients(updateString);
			Parser.networkSplitter(updateString, game);
		} else if (splitString[0].equals(Flag.BEGIN_TOKEN_DRAW_CONTINUE)) {
			beginContinueResponses -= 1;
			if (beginContinueResponses == 0) {
				serverState = ServerState.IN_GAME;
				updateClients(Flag.START_GAME);
			}
		}
	}
	
	public void ivanhoeIteration() throws IOException {
		Object[] update = getUpdate();
		String updateString = (String)update[1];
		String[] splitString = updateString.split(":");
		
		// If it's ivanhoe, this means the player resolved an overlay to use ivanhoe or not
		if (splitString[0].equals(Flag.IVANHOE_RESPONSE)) {
			// If someone played Ivanhoe, update the value to reflect the player who played it
			if (splitString[1].equals("true")) {
				ivanhoePlayed = (int)update[0];
			}
			ivanhoeResponsesNeeded--;
			// If we receive responses equal to 1 less than the number of players (as the instigator does not give input)
			if (ivanhoeResponsesNeeded == 0) {
				String[] actionCommand = currentActionCardCommand.split(":");
				if (ivanhoePlayed == -1) {
					// Takes the original command and replaces Flag.CARD with Flag.ACTION_CARD.
					// Flag.ACTION_CARD signifies to make the action card actually perform an action
					actionCommand[0] = Flag.ACTION_CARD;
					updateClients(Parser.networkSplitter(String.join(":", actionCommand), game));
				} else {
					updateClients(Parser.networkSplitter(Flag.IVANHOE_PLAYED + ":" + actionCommand[1] + ":" + (int)update[0], game));
				}
				// Go back to the game
				serverState = ServerState.IN_GAME;
			}
		// Any other message, pass it off as normal.
		} else {
			updateClients(Parser.networkSplitter(updateString, game));
		}
	}
	
	//function to return game instance. purely for testing purposes
	public GameState getGame() {
		return game;
	}
	
	public void handleState(ServerState st) throws IOException {
		switch (st) {
			case BEGIN_DRAW_TOKEN:
				beginDrawTokenIteration();
				break;
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
			case IVANHOE:
				ivanhoeIteration();
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

