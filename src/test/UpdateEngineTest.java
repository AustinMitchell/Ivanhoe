package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import network.Flag;

import org.junit.Before;
import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
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
		assertEquals(Flag.CARD + ":2", UpdateEngine.playValueCard(game, 2));
	}
	
	@Test 
	public void unhorseTest() {
		assertEquals(Flag.CARD + ":3:1", UpdateEngine.unhorse(game, 3, Type.RED));
	}
	
	@Test 
	public void changeWeaponTest() {
		assertEquals(Flag.CARD + ":4:2", UpdateEngine.changeWeapon(game, 4, Type.YELLOW));
	}
	
	@Test 
	public void dropWeaponTest() {
		assertEquals(Flag.CARD + ":3:4", UpdateEngine.unhorse(game, 3, Type.GREEN));
	}
	
	@Test
	public void endTurnTest() {
		assertEquals(Flag.END_TURN, UpdateEngine.endTurn(game));
	}

}
