package test.gamelogictesting;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import controller.network.Client;
import controller.network.Server;
import controller.rulesengine.UpdateEngine;
import model.Card;
import model.Flag;
import model.GameState;
import model.Type;

public class IvanhoeTest {
	public static final String LOCAL = "localhost";
	public static int PORT = Server.PORT-1000;
	
	Server server;
	ArrayList<Client> client;
	
	private void setup(int numPlayers) throws IOException {
		server = new Server(PORT);
		server.setupServer();
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
	public void ivanhoeTest() {
		Card ivanhoe = new Card(Type.ACTION, Card.IVANHOE);
		Card unhorse = new Card(Type.ACTION, Card.UNHORSE);
		int purple = Type.PURPLE;
		
		try {
			final int NUM_PLAYERS = 2; 
			
			setup(NUM_PLAYERS);
			
			for (int i=0; i<NUM_PLAYERS; i++) {
				// Connects client to server on localhost
				client.get(i).connect(LOCAL, "Player " + (i+1));
				// Allow server to accept new client
				server.acceptPlayer();
				// Checks that the server sent back a message with the number of players
				while(!client.get(i).hasFlags());
			}
			/*client.get(0).getGame().getHand(0).add(unhorse);
			int player1HandSize = client.get(0).getGame().getHand(0).deckSize();
			client.get(0).getGame().setTournamentColour(purple);
			GameState game = client.get(0).getGame();
			
			client.get(1).getGame().getHand(1).add(ivanhoe);
			int player2HandSize = client.get(1).getGame().getHand(1).deckSize();
			client.get(0).getGame().setTournamentColour(purple);
			GameState game2 = client.get(1).getGame();
			*/
			
			server.createGame();
			
			/*client.get(0).getGame().initializeClient(newPlayers, cardData);
			int player1HandSize = client.get(0).getGame().getDrawDeck().deckSize();*/
			
			server.getGame().setTournamentColour(purple);
			server.getGame().getHand(0).add(unhorse);
			server.getGame().getHand(1).add(ivanhoe);
			
			GameState game1 = client.get(0).getGame();
			GameState game2 = client.get(1).getGame();
			

			client.get(0).sendMessage(Flag.BEGIN_TOKEN_DRAW_CONTINUE);
			client.get(1).sendMessage(Flag.BEGIN_TOKEN_DRAW_CONTINUE);
			server.handleState();
			server.handleState();
			
			int player1HandSize = server.getGame().getHand(0).deckSize();
			client.get(0).sendMessage(UpdateEngine.unhorse(server.getGame(), player1HandSize-1, Type.BLUE));
			server.handleState();
			assertEquals(server.getServerState(), Server.ServerState.IVANHOE);
			client.get(1).sendMessage(Flag.IVANHOE_RESPONSE + ":" + "true");
			server.handleState();
			assertEquals(server.getGame().getTournamentColour(), Type.PURPLE);
			assertEquals(server.getGame().getDiscardDeck().deckSize(), 2);
			
			
			
			cleanup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cleanup();
			fail("Connection failed");
		}
		
	}
	
}
