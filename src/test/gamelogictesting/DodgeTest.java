package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.Card;
import model.GameState;
import model.Player;
import model.Type;

public class DodgeTest {
	Player player1;
	Player player2;
	ArrayList<Player> players;
	GameState game;
	
	//Setup a game where the target's display has multiple coloured cards
	public void DisplayWithMultiTypes() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 3);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a dodge card to play
		Card dodge = new Card(Type.ACTION, Card.DODGE);
		game.getAllPlayers().get(0).getHand().add(dodge);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
		game.getDisplay(1).add(purpleCard);
		game.getDisplay(1).add(blueCard);
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(yellowCard);
		game.getDisplay(1).add(maiden);
	}


	//Setup a game where the target's display has only 1 card
	public void DisplayWithOneCard() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card redCard = new Card(Type.RED, 4);
		
		//Give one of the players a dodge card to play
		Card dodge = new Card(Type.ACTION, Card.DODGE);
		game.getAllPlayers().get(0).getHand().add(dodge);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
	}

	
	//Setup a game where the target has a shield
	public void DisplayWithShield() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 3);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		Card shield = new Card(Type.ACTION, Card.SHIELD);
		
		//Give one of the players a dodge card to play
		Card dodge = new Card(Type.ACTION, Card.DODGE);
		game.getAllPlayers().get(0).getHand().add(dodge);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
		game.getDisplay(1).add(purpleCard);
		game.getDisplay(1).add(blueCard);
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(yellowCard);
		game.getDisplay(1).add(maiden);
		game.getShield(1).add(shield); //give player a shield
	}



	@Test
	public void dodgeWithMultipleCardsDisplayTest() {
		DisplayWithMultiTypes();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has a squire at the 5th position
		*/
		assertEquals(game.getDisplay(1).getCard(4).getCardType(), Type.WHITE);
		assertEquals(game.getDisplay(1).getCard(4).getCardValue(), 2);
		
		//test the size of discard deck before playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 7);
		
		//play dodge
		RulesEngine.dodge(game, cardPos, targetPlayer, 4);
		
		//test the size of discard deck after playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 2);

		/*
		 * test to make sure the target does not have a squire at the 5th position anymore
		*/
		assertFalse(game.getDisplay(1).getCard(4).getCardType() == Type.WHITE);
		assertFalse(game.getDisplay(1).getCard(4).getCardValue() == 2);
		
		/*
		 * test to make sure the last card in the discard deck is a squire
		*/
		assertEquals(game.getDiscardDeck().getCard(1).getCardType(), Type.WHITE);
		assertEquals(game.getDiscardDeck().getCard(1).getCardValue(), 2);
		
		/*
		 * test to make sure the first card in the discard deck is a dodge
		*/
		assertEquals(game.getDiscardDeck().getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getDiscardDeck().getCard(0).getCardValue(), Card.DODGE);
	}

	
	@Test
	public void dodgeWithOneCardDisplayTest() {
		DisplayWithOneCard();
		
		int cardPos = 0;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		
		//test the size of discard deck before playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//test the size of the target's display
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		//play dodge
		RulesEngine.dodge(game, cardPos, targetPlayer, 0);
		
		//test the size of discard deck after playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		/*
		 * test to make sure the target's last played card is still the single red card
		*/
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardType() == Type.RED);
	}

	
	//A test where the target has a shield
	@Test
	public void dodgeShieldTest() {
		DisplayWithShield();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has a squire at the 5th position
		*/
		assertEquals(game.getDisplay(1).getCard(4).getCardType(), Type.WHITE);
		assertEquals(game.getDisplay(1).getCard(4).getCardValue(), 2);
		
		//test the size of discard deck before playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 7);
		
		//play dodge
		RulesEngine.dodge(game, cardPos, targetPlayer, 4);
		
		//test the size of discard deck after playing dodge
		assertEquals(game.getDiscardDeck().deckSize(), 0);

		/*
		 * test to make sure the target does not have a squire at the 5th position anymore
		*/
		assertTrue(game.getDisplay(1).getCard(4).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(4).getCardValue() == 2);
	}


}
