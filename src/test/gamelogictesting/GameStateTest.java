package test.gamelogictesting;

import models.GameState;
import models.Player;
import rulesengine.Type;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class GameStateTest {
	Player player1;
	Player player2;
	Player player3;
	ArrayList<Player> players;
	
	GameState game;

	
	@Before
	public void setupPlayers() {
		player1 = new Player("Ahmed");
		player1.enterTournament();
		player2 = new Player("Nick");
		player2.enterTournament();
		player3 = new Player("Austin");
		player3.enterTournament();
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		game = new GameState();
		game.initializeServer(players);
	}
	
	/*
	 * Test the right size of each player's hand
	 */
	@Test
	public void testStartingHand() {
		assertEquals(8, game.getHand(0).deckSize());
		assertEquals(8, game.getHand(1).deckSize());
		assertEquals(8, game.getHand(2).deckSize());
	}
	
	/*
	 * Test the right size of the draw deck after giving out hands to players
	 * Since we have given out 24 cards to 3 players (see above test), the draw deck should have 
	 * 86 remaining cards
	 */
	@Test
	public void testDrawDeck() {
		assertEquals(game.getDrawDeck().deckSize(), 86);
	}
	
	/*
	 * Test the functionality of renewing an empty draw deck using the discard deck
	 */
	@Test
	public void testRenewDeck() {
		game.getDrawDeck().emptyDeck(game.getDiscardDeck());
		assertEquals(0, game.getDrawDeck().deckSize());
		assertEquals(86, game.getDiscardDeck().deckSize());
		game.renewDrawDeck();
		assertEquals(86, game.getDrawDeck().deckSize());
		assertEquals(0, game.getDiscardDeck().deckSize());
	}
	
	@Test
	public void playerTurnTest() {
		for(int i = 0; i <= game.getAllPlayers().size(); i++) {
			if(i == game.getAllPlayers().size()) {
				assertEquals(0, game.getTurn());
			}
			else assertEquals(i, game.getTurn());
			game.nextTurn();
		}
	}
	

	@Test
	public void setTournamentRedColourTest() {
		game.setTournamentColour(Type.RED);
		assertEquals(game.getTournamentColour(), Type.RED);
		
	}
	
	@Test
	public void setTournamentBlueColourTest() {
		game.setTournamentColour(Type.BLUE);
		assertEquals(game.getTournamentColour(), Type.BLUE);
		
	}
	
	@Test
	public void setTournamentYellowColourTest() {
		game.setTournamentColour(Type.YELLOW);
		assertEquals(game.getTournamentColour(), Type.YELLOW);
		
	}
	
	@Test
	public void setTournamentGreenColourTest() {
		game.setTournamentColour(Type.GREEN);
		assertEquals(game.getTournamentColour(), Type.GREEN);
		
	}
	
	@Test
	public void setTournamentPurpleColourTest() {
		game.setTournamentColour(Type.PURPLE);
		assertEquals(game.getTournamentColour(), Type.PURPLE);
		
	}
}
