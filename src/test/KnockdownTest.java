package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class KnockdownTest {
	Player player1;
	Player player2;
	ArrayList<Player> players;
	GameState game;

	//Setup a game where the target's hand has 1 card in hand
	public void setupGameWithNonEmptyHand() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		
		//Empty players hands
		game.getHand(0).emptyDeck(game.getDiscardDeck());
		game.getHand(1).emptyDeck(game.getDiscardDeck());
		game.setTurn(0);
		
		
		//create cards to be added to target's hand
		Card purpleCard = new Card(Type.PURPLE, 3);
		
		//Give the players two knockdown cards to play
		Card knockdown = new Card(Type.ACTION, Card.KNOCKDOWN);
		game.getHand(0).add(knockdown);
		game.getHand(0).add(knockdown);
		
		//Give target player a custom hand
		game.getHand(1).add(purpleCard);
	}
	
	//Setup a game where the target's hand is empty
	public void setupGameWithEmptyHand() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		
		//Empty players hands
		game.getHand(0).emptyDeck(game.getDiscardDeck());
		game.getHand(1).emptyDeck(game.getDiscardDeck());
		game.setTurn(0);
		
		//Give the players two knockdown cards to play
		Card knockdown = new Card(Type.ACTION, Card.KNOCKDOWN);
		game.getHand(0).add(knockdown);
		game.getHand(0).add(knockdown);
	}


	//Test knockdown against a target whose hand's isn't empty
	@Test
	public void knockdownWithNonEmptyHandTest() {
		setupGameWithNonEmptyHand();
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		
		//test the size of discard deck before playing knockdown
		assertEquals(game.getDiscardDeck().deckSize(), 16);
		//test the size of player's hand
		assertEquals(game.getHand(0).deckSize(), 2);
		//test the size of target's hand
		assertEquals(game.getHand(1).deckSize(), 1);
		
		//play knockdown
		RulesEngine.knockdown(game, cardPos, targetPlayer, 0);
		
		//test the size of discard deck after playing knockdown
		assertEquals(game.getDiscardDeck().deckSize(), 17);

		/*
		 * test to make sure the player's hand received a purple card
		 */
		assertEquals(game.getHand(game.getTurn()).getCard(1).getCardType(), Type.PURPLE);

		/*
		 * test to make sure the target's hand has no cards
		 */
		assertEquals(game.getHand(1).deckSize(), 0);
	}

	//Test knockdown against a target whose hand's isn't empty
	@Test
	public void knockdownWithEmptyHandTest() {
		setupGameWithEmptyHand();
		int cardPos = 1;
		int targetPlayer = 1;
		
		//test the size of discard deck before playing knockdown
		assertEquals(game.getDiscardDeck().deckSize(), 16);
		//test the size of player's hand
		assertEquals(game.getHand(0).deckSize(), 2);
		//test the size of target's hand
		assertEquals(game.getHand(1).deckSize(), 0);
		
		//play knockdown
		RulesEngine.knockdown(game, cardPos, targetPlayer, 0);
		
		//test the size of discard deck after playing knockdown
		assertEquals(game.getDiscardDeck().deckSize(), 17);

		/*
		 * test the size of player's hand
		 */
		assertEquals(game.getHand(0).deckSize(), 1);

		/*
		 * test to make sure the target's hand has no cards
		 */
		assertEquals(game.getHand(1).deckSize(), 0);
	}


}
