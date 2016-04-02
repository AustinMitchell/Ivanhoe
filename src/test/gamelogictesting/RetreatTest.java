package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class RetreatTest {

	static Player player1;
	static Player player2;
	static ArrayList<Player> players;
	static GameState game;
	
	//Setup a game where the target's display has multiple cards
	@BeforeClass
	public static void setupGame() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		game.setTurn(0);
		
		//Empty first player's hand
		game.getHand(0).emptyDeck(game.getDiscardDeck());
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card redCard = new Card(Type.RED, 4);
		
		//Give one player 2 retreat cards for testing purposes
		Card retreat = new Card(Type.ACTION, Card.RETREAT);
		game.getHand(0).add(retreat);
		game.getHand(0).add(retreat);
		
		//Give target player a custom display
		game.getDisplay(0).add(redCard);
		game.getDisplay(0).add(purpleCard);
	}


	//Test Retreat card with more than 1 card in the display
	@Test
	public void retreatWithMultipleCardsDisplayTest() {
		
		//test the size of discard deck before playing retreat
		assertEquals(game.getDiscardDeck().deckSize(), 8);
		
		//test the size of player's display before playing retreat
		assertEquals(game.getDisplay(0).deckSize(), 2);
		
		//play retreat
		RulesEngine.retreat(game, 0, 0);
		
		//test the size of discard deck after playing retreat
		assertEquals(game.getDiscardDeck().deckSize(), 9);
		
		/*
		 * test to make sure the player has a red card in his hand along with the hand's size
		*/
		assertEquals(game.getHand(0).getCard(1).getCardType(), Type.RED);
		assertEquals(game.getHand(0).deckSize(), 2);

		/*
		 * test to make sure there is a purple in the display along with the display's size
		 */
		assertEquals(game.getDisplay(0).getCard(0).getCardType(), Type.PURPLE);
		assertEquals(game.getDisplay(0).deckSize(), 1);
		
	}


	//Test Retreat card with 1 card in the display
	@Test
	public void retreatWithOneCardDisplayTest() {
		
		//test the size of discard deck before playing retreat
		assertEquals(game.getDiscardDeck().deckSize(), 9);
		
		//test the size of player's display before playing retreat
		assertEquals(game.getDisplay(0).deckSize(), 1);
		
		//play retreat
		RulesEngine.retreat(game, 0, 0);
		
		//test the size of discard deck after playing retreat
		assertEquals(game.getDiscardDeck().deckSize(), 9);
		
		/*
		 * test to make sure the player still has a retreat card in his hand
		*/
		assertEquals(game.getHand(0).getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getHand(0).getCard(0).getCardValue(), Card.RETREAT);
		assertEquals(game.getHand(0).deckSize(), 2);
		
		/*
		 * test to make sure there is a purple in the display along with the display's size
		 */
		assertEquals(game.getDisplay(0).getCard(0).getCardType(), Type.PURPLE);
		assertEquals(game.getDisplay(0).deckSize(), 1);
		
	}
	
}
