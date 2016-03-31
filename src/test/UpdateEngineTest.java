package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.GameState;
import models.Player;
import rulesengine.Type;
import rulesengine.UpdateEngine;

public class UpdateEngineTest {
	UpdateEngine update;
	GameState game;
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;
	ArrayList<Player> players;
	
	@Before
	public void setupUpdateEngine() {
		players = new ArrayList<Player>();
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		player4 = new Player("Mike");
		player5 = new Player("Malik");
		game = new GameState();
		update = new UpdateEngine();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState();
		game.initializeServer(players);
	}
	

	@Test
	public void playValueCardTest() {
		assertEquals("card:2", UpdateEngine.playValueCard(game, 2));
	}
	
	@Test 
	public void unhorseTest() {
		assertEquals("card:3:1", UpdateEngine.unhorse(game, 3, Type.RED));
	}
	
	@Test 
	public void changeWeaponTest() {
		assertEquals("card:4:2", UpdateEngine.changeWeapon(game, 4, Type.YELLOW));
	}
	
	@Test 
	public void dropWeaponTest() {
		assertEquals("card:3:4", UpdateEngine.unhorse(game, 3, Type.GREEN));
	}
	
	@Test
	public void endTurnTest() {
		assertEquals("endTurn:false", UpdateEngine.endTurn(game, "false"));
		assertEquals("endTurn:true", UpdateEngine.endTurn(game, "true"));
	}

}
