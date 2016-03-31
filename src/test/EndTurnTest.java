package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.GameState;
import models.Player;
import rulesengine.RulesEngine;

public class EndTurnTest {
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;
	ArrayList<Player> players;
	GameState game;

	
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
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		//since there are only 2 players, the winning player is at index 0
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
	}
	
	
	@Test
	public void threePlayerEndTurnTest() {
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//player at index 0 withdrew from the game leaving only 1 winner at index 2
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
	}
	
	
	@Test
	public void fourPlayerEndTurnTest() {
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		game = new GameState();
		game.initializeServer(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//player at index 3 withdrew from the game
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 0 ends their turn normally
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//player at index 2 withdrew from the game leaving only 1 winner at index 0
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
	}

	
	@Test
	public void fivePlayerEndTurnTest() {
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState();
		game.initializeServer(players);
		
		//ending turn after starting from index 0 (which is the current turn)
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 2 ended their turn normally
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//player at index 3 withdraws from tournament
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 4 ended their turn normally
		assertEquals("endTurn:false", RulesEngine.endTurn(game, "false"));
		
		//player at index 0 withdrew from the game
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
		
		//player at index 2 withdrew from the game leaving winning player at index 4
		assertEquals("endTurn:true", RulesEngine.endTurn(game, "true"));
	}
}
