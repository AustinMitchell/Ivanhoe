package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.GameState;
import model.Player;
import model.Type;


public class DrawTokenTest {
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;
	ArrayList<Player> players;
	GameState game;

	
	@Test
	public void twoPlayerDrawTokenTest() {
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		
		RulesEngine.drawToken(game);
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			assertTrue(game.getAllPlayers().get(i).isInTournament());
		}
		
		int playerIndex = -1;
		int purples = 0;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				purples++;
				playerIndex = i;
			}
		}
		assertEquals(1, purples);
		assertEquals(playerIndex, game.getTurn());
	}
	
	@Test
	public void threePlayerDrawTokenTest() {
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			assertTrue(game.getAllPlayers().get(i).isInTournament());
		}
		
		RulesEngine.drawToken(game);
		int playerIndex = -1;
		int purples = 0;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				purples++;
				playerIndex = i;
			}
		}
		assertEquals(1, purples);
		assertEquals(playerIndex, game.getTurn());
	}
	
	@Test
	public void fourPlayerDrawTokenTest() {
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		player4 = new Player("Mike");
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		game = new GameState();
		game.initializeServer(players);
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			assertTrue(game.getAllPlayers().get(i).isInTournament());
		}
		
		RulesEngine.drawToken(game);
		int playerIndex = -1;
		int purples = 0;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				purples++;
				playerIndex = i;
			}
		}
		assertEquals(1, purples);
		assertEquals(playerIndex, game.getTurn());
	}

	@Test
	public void fivePlayerDrawTokenTest() {
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		player4 = new Player("Mike");
		player5 = new Player("Malik");
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState();
		game.initializeServer(players);
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			assertTrue(game.getAllPlayers().get(i).isInTournament());
		}
		
		RulesEngine.drawToken(game);
		int playerIndex = -1;
		int purples = 0;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				purples++;
				playerIndex = i;
			}
		}
		assertEquals(1, purples);
		assertEquals(playerIndex, game.getTurn());
	}

}
