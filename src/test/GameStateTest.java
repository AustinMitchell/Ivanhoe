package test;

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
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		game = new GameState();
		game.initializeServer(players);
	}
	
	@Test
	public void testStartingHand() {
		assertEquals(8, game.getPlayer("Ahmed").getHand().deckSize());
	}
	
	@Test
	public void testDrawDeck() {
		assertEquals(game.getDrawDeck().deckSize(), 86);
	}
	
	@Test
	public void testRenewDeck() {
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.renewDrawDeck();
		assertEquals(3, game.getDrawDeck().deckSize());
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
