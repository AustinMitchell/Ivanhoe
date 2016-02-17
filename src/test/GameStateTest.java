package test;

import models.GameState;
import models.Player;
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
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		players = new ArrayList();
		
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		game = new GameState(players);
	}
	
	@Test
	public void testStartingHand() {
		assertEquals(8, game.getPlayer("Ahmed").getHand().deckSize());
	}
	
	@Test
	public void testDrawDeck() {
		assertEquals(game.getDrawDeck(), 86);
	}
	
	@Test
	public void testRenewDeck() {
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.getDrawDeck().draw(game.getDiscardDeck());
		game.renewDrawDeck();
		assertEquals(3, game.getDrawDeck().deckSize());
		
	}

}
