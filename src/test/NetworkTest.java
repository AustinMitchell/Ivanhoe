package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import network.*;

import org.junit.Test;


public class NetworkTest {	
	public static final String LOCAL = "localhost";
	public static int PORT = Server.PORT;
	
	Server server;
	ArrayList<Client> client;
	
	private void setup(int numPlayers) throws IOException {
		server = new Server(PORT);
		client = new ArrayList<Client>();
		for (int i=0; i<numPlayers; i++) {
			client.add(new Client(PORT));
		}
		PORT++;
	}
	private void cleanup() {
		server.killServer();
		for (Client c: client) {
			c.killClient();
		}
	}
	
	@Test
	public void acceptPlayerTest() {
		try {
			final int NUM_PLAYERS = 5;
			
			setup(NUM_PLAYERS);
									
			for (int i=0; i<NUM_PLAYERS; i++) {
				assertEquals(server.getCurrentNumPlayers(), i);
				// Connects client to server on localhost
				client.get(i).connect(LOCAL, "Player " + (i+1));
				// Allow server to accept new client
				server.acceptPlayer();
				// Checks that the server sent back a message with the number of players
				while(!client.get(i).hasFlags());
				assertEquals(client.get(i).readGuiFlag(), Flag.PLAYER_ID+":"+(i+1));
				// Verifies a new player was added to the server
				assertEquals(server.getCurrentNumPlayers(), i+1);
			}	
			
			Client lastClient = new Client(2000, PORT-1);
			lastClient.connect(LOCAL, "last player");
			while (lastClient.waitingForConnection()) {}
			assertEquals(Client.CONNECT_REJECT, lastClient.getConnectMessage());
			
			
			
			cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanup();
			fail("Connection failed");
		}
	}
	
	@Test
	public void serverStateTest() {
		try {
			final int NUM_PLAYERS = 2;
			
			setup(NUM_PLAYERS);
			
			assertEquals(server.getServerState(), Server.ServerState.WAITING_FOR_FIRST);
			
			client.get(0).connect(LOCAL, "Player 1");
			server.waitForPlayer();
			client.get(0).sendMessage(Flag.MAX_PLAYERS_SET + ":2");
			// This will return true if the transaction was successful
			while (!server.waitForFirstPlayerSetupInfo());
			
			assertEquals(server.getServerState(), Server.ServerState.WAITING_FOR_ALL);
			
			client.get(1).connect(LOCAL, "Player 2");
			server.waitForPlayer();
			
			assertEquals(server.getServerState(), Server.ServerState.CREATE_GAME);
			
			server.createGame();
			
			assertEquals(server.getServerState(), Server.ServerState.IN_GAME);
			
			cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanup();
			fail("Connection failed");
		}
	}
	
	@Test
	public void getUpdateTest() {
		try {
			final int NUM_PLAYERS = 2;
			
			setup(NUM_PLAYERS);
			
			client.get(0).connect(LOCAL, "Player 1");
			server.acceptPlayer();
			client.get(1).connect(LOCAL, "Player 2");
			server.acceptPlayer();
			
			client.get(0).sendMessage("test1");
			assertEquals("test1", (String)server.getUpdate()[1]);
			client.get(0).sendMessage("test2");
			assertEquals("test2", (String)server.getUpdate()[1]);
			
			cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanup();
			fail("Connection failed");
		}
	}
}

