package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
	RulesEngine engine = new RulesEngine();
	int purples;

	
	//@Before
	public void setupPlayers() {
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
		
		game = new GameState(players);
		
		player1 = new Player("Ahmed");
		player1.enterTournament();
		player2 = new Player("Nick");
		player2.enterTournament();
		players = new ArrayList();
		players.add(player1);
		players.add(player2);
		game = new GameState(players);
		
		engine.drawToken(game);
		purples = 0;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).checkToken(0) == true) {
				purples++;
			}
		}
	}
	
	
	@Test
	public void twoPlayerStartTournamentTest() {
		assertEquals(1, purples);
		
		
	}
}
