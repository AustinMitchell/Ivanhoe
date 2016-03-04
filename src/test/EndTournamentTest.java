package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class EndTournamentTest {
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
		//player1.enterTournament();
		player2 = new Player("Nick");
		//player2.enterTournament();
		player3 = new Player("Austin");
		//player3.enterTournament();
		player4 = new Player("Mike");
		//player4.enterTournament();
		player5 = new Player("Malik");
		//player5.enterTournament();
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		
		game = new GameState();
		game.initializeServer(players);
		engine.startGame(game);
		
	}
	
	@Test
	public void twoPlayerDrawTokenTest() {
		player1 = new Player("Ahmed");
		player1.enterTournament();
		player2 = new Player("Nick");
		player2.enterTournament();
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		
		engine.startGame(game);
		int playerIndex = -1;
		int purples = 0;
		
		assertEquals("endTurn:1", engine.endTurn(game, "false"));
		assertEquals("endTournament:1", engine.endTournament(game));
	}
	
	@Test
	public void threePlayerDrawTokenTest() {
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
		
		engine.drawToken(game);
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
		player1.enterTournament();
		player2 = new Player("Nick");
		player2.enterTournament();
		player3 = new Player("Austin");
		player3.enterTournament();
		player4 = new Player("Mike");
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		game = new GameState();
		game.initializeServer(players);
		
		engine.drawToken(game);
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
		player1.enterTournament();
		player2 = new Player("Nick");
		player2.enterTournament();
		player3 = new Player("Austin");
		player3.enterTournament();
		player4 = new Player("Mike");
		player4.enterTournament();
		player5 = new Player("Malik");
		player5.enterTournament();
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState();
		game.initializeServer(players);
		
		engine.drawToken(game);
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
