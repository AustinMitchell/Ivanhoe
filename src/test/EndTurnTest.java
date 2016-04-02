/*
 * This Test needs to be modified in order to pass since the functionality of end turn has changed.
 * Take a look at how endTurn in the rules engine works.
 * 
 * This test file is actually redundant now anyway 
 */


package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.Card;
import model.Flag;
import model.GameState;
import model.Player;

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
		
		// sets everyones hands to a series of blue cards
		for (Player p: players) {
			Card[] cards = new Card[8];
			for (int i=0; i<cards.length; i++) {
				cards[i] = new Card(3, 3);
			}
			p.setHand(cards);
		}
		String BLUE = "3";
		// sets tournament colour to BLUE
		RulesEngine.setColour(game, BLUE);
		// starts the tournament
		RulesEngine.startTournament(game);
		
		// Ends the first players turn
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		// Player 1 ends turn, withdraws with a lower display, and ends tournament
		// Expecting END TURN, WITHDRAW with the player position, and an END_TOURNAMENT with the tournament colour
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":1" + Flag.NEW_COM + Flag.END_TOURNAMENT + ":" + BLUE, 
			         RulesEngine.endTurn(game));
	}
	
	
	@Test
	public void threePlayerEndTurnTest() {
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		
		// sets everyones hands to a series of blue cards
		for (Player p: players) {
			Card[] cards = new Card[8];
			for (int i=0; i<cards.length; i++) {
				cards[i] = new Card(3, 3);
			}
			p.setHand(cards);
		}
		String BLUE = "3";
		// sets tournament colour to BLUE
		RulesEngine.setColour(game, BLUE);
		// starts the tournament
		RulesEngine.startTournament(game);
		
		//ending turn after starting from index 0 (which is the current turn)
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":1", RulesEngine.endTurn(game));
		
		//player at index 2 ended their turn normally
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		// Player 0 ends turn, withdraws with a lower display, and ends tournament
		// Expecting END TURN, WITHDRAW with the player position, and an END_TOURNAMENT with the tournament colour
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":0" + Flag.NEW_COM + Flag.END_TOURNAMENT + ":" + BLUE, 
			         RulesEngine.endTurn(game));
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
		
		// sets everyones hands to a series of blue cards
		for (Player p: players) {
			Card[] cards = new Card[8];
			for (int i=0; i<cards.length; i++) {
				cards[i] = new Card(3, 3);
			}
			p.setHand(cards);
		}
		String BLUE = "3";
		// sets tournament colour to BLUE
		RulesEngine.setColour(game, BLUE);
		// starts the tournament
		RulesEngine.startTournament(game);
		
		//ending turn after starting from index 0 (which is the current turn)
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":1", RulesEngine.endTurn(game));
		
		//player at index 2 ended their turn normally
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//player at index 3 withdrew from the game
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":3", RulesEngine.endTurn(game));
		
		//player at index 0 ends their turn normally
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		// Player 2 ends turn, withdraws with a lower display, and ends tournament
		// Expecting END TURN, WITHDRAW with the player position, and an END_TOURNAMENT with the tournament colour
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":2" + Flag.NEW_COM + Flag.END_TOURNAMENT + ":" + BLUE, 
		         RulesEngine.endTurn(game));
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
		
		
		// sets everyones hands to a series of blue cards
		for (Player p: players) {
			Card[] cards = new Card[8];
			for (int i=0; i<cards.length; i++) {
				cards[i] = new Card(3, 3);
			}
			p.setHand(cards);
		}
		String BLUE = "3";
		// sets tournament colour to BLUE
		RulesEngine.setColour(game, BLUE);
		// starts the tournament
		RulesEngine.startTournament(game);
		
		//ending turn after starting from index 0 (which is the current turn)
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//ending a turn with the withdrawal of index 1 (which is the current turn)
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":1", RulesEngine.endTurn(game));
		
		//player at index 2 ended their turn normally
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//player at index 3 withdraws from tournament
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":3", RulesEngine.endTurn(game));
		
		//player at index 4 ended their turn normally
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		assertEquals(Flag.END_TURN, RulesEngine.endTurn(game));
		
		//player at index 0 withdrew from the game
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":0", RulesEngine.endTurn(game));
		
		// Player 2 ends turn, withdraws with a lower display, and ends tournament
		// Expecting END TURN, WITHDRAW with the player position, and an END_TOURNAMENT with the tournament colour
		assertEquals(Flag.END_TURN + Flag.NEW_COM + Flag.WITHDRAW + ":2" + Flag.NEW_COM + Flag.END_TOURNAMENT + ":" + BLUE, 
		         RulesEngine.endTurn(game));
	}
}
