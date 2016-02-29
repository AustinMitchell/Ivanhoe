package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class EndTurnTest {
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;
	ArrayList<Player> players;
	GameState game;
	RulesEngine engine = new RulesEngine();

	
	@Before
	public void setupPlayers() {
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		player4 = new Player("Mike");
		player5 = new Player("Malik");
		
	}
	
	@Test
	public void twoPlayerEndTurnTest() {
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		game = new GameState(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:1", engine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		//since there are only 2 players, the winning player is at index 0
		assertEquals("endTournament:0", engine.endTurn(game, "true"));
	}
	
	
	@Test
	public void threePlayerEndTurnTest() {
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:1", engine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:2", engine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:0", engine.endTurn(game, "false"));
		
		//player at index 0 withdrew from the game leaving only 1 winner at index 2
		assertEquals("endTournament:2", engine.endTurn(game, "true"));
	}
	
	
	@Test
	public void fourPlayerEndTurnTest() {
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		game = new GameState(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:1", engine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:2", engine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:3", engine.endTurn(game, "false"));
		
		//player at index 3 withdrew from the game
		assertEquals("endTurn:0", engine.endTurn(game, "true"));
		
		//player at index 0 ends their turn normally
		assertEquals("endTurn:2", engine.endTurn(game, "false"));
		
		//player at index 2 withdrew from the game leaving only 1 winner at index 0
		assertEquals("endTournament:0", engine.endTurn(game, "true"));
	}

	
	@Test
	public void fivePlayerEndTurnTest() {
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:1", engine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:2", engine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:3", engine.endTurn(game, "false"));
		
		//player at index 3 withdraws from tournament
		assertEquals("endTurn:4", engine.endTurn(game, "true"));
		
		//player at index 4 ended their turn normally
		assertEquals("endTurn:0", engine.endTurn(game, "false"));
		
		//player at index 0 withdrew from the game
		assertEquals("endTurn:2", engine.endTurn(game, "true"));
		
		//player at index 2 withdrew from the game leaving winning player at index 4
		assertEquals("endTournament:4", engine.endTurn(game, "true"));
	}
}
