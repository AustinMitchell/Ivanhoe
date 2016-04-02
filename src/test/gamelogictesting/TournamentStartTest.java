package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;
import rulesengine.Validator;

public class TournamentStartTest {
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
		players = new ArrayList<Player>();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		
		game = new GameState();
		game.initializeServer(players);

		//Discard player hands to replace with customized hands for testing purposes
		game.getHand(0).emptyDeck(game.getDiscardDeck());
		game.getHand(1).emptyDeck(game.getDiscardDeck());
		game.getHand(2).emptyDeck(game.getDiscardDeck());
		
		//set tournament colour to blue
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		//set turn to the first player
		game.setTurn(0);
		
		//cards to be used
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 3);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		Card ivanhoe = new Card(Type.ACTION, Card.IVANHOE);
		Card charge = new Card(Type.ACTION, Card.CHARGE);
		Card countercharge = new Card(Type.ACTION, Card.COUNTERCHARGE);
		Card dropWeapon = new Card(Type.ACTION, Card.DROP_WEAPON);
		
		game.getHand(0).add(ivanhoe);
		game.getHand(0).add(charge);
		game.getHand(0).add(countercharge);
		game.getHand(0).add(dropWeapon);
		
		game.getHand(1).add(blueCard);
		game.getHand(1).add(purpleCard);
		game.getHand(1).add(squire);
		game.getHand(1).add(yellowCard);
		
		game.getHand(2).add(redCard);
		game.getHand(2).add(blueCard);
		game.getHand(2).add(greenCard);
		game.getHand(2).add(maiden);
	}
	
	
	/*
	 * Test where the player is able to start the tournament
	 */
	@Test
	public void playerCanStartTest() {
		Card redCard = new Card(Type.RED, 4);
		//Give the player a value card 
		game.getHand(0).add(redCard);
		//Test whether the current player can start the tournament or not
		assertTrue(Validator.canStartTournament(game));
	}
	
	/*
	 * the player who should start cannot start a tournament
	 */
	@Test
	public void playerCannotStartTest() {
		//Test whether to make sure the player cannot start a tournament since he only has action cards
		assertFalse(Validator.canStartTournament(game));
	}


	/*
	 * last tournament was purple, cannot be purple again
	 */
	@Test
	public void consecutivePurpleTournamentsTest() {
		//setting the tournament to purple after a blue tournament
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		//test to make sure the tournament colour is indeed purple
		assertTrue(game.getTournamentColour() == Type.PURPLE);
		
		//setting the tournament to purple after a purple tournament
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		assertTrue(game.getTournamentColour() == Type.PURPLE);		
		
		
	}
}
