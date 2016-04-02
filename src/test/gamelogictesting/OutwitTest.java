package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import network.Flag;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class OutwitTest {
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
		Card outwit = new Card(Type.ACTION, Card.OUTWIT);
		
		//Give players custom displays
		game.getDisplay(0).add(redCard);
		game.getDisplay(0).add(purpleCard);
		game.getDisplay(0).add(blueCard);
		game.getHand(0).add(outwit); //Give player an outwit card
		
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(yellowCard);
		game.getDisplay(1).add(maiden);
	}
	
	//Setup a game where the target's display has only 1 card and has a shield
	public void targetWithShield() {
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
		Card blueCard = new Card(Type.BLUE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card shield = new Card(Type.ACTION, Card.SHIELD);
		
		
		//Give one of the players a outwit card to play
		Card outwit = new Card(Type.ACTION, Card.OUTWIT);
		game.getPlayer(0).getHand().add(outwit);
		
		//Give target player a custom display
		game.getDisplay(0).add(blueCard);
		game.getDisplay(0).add(greenCard);
		
		game.getShield(1).add(shield); // give the target a shield
		game.getDisplay(1).add(redCard);
	}

	
	//Setup a game where the target's display has only 1 card and has a shield
	public void targetWithStun() {
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
		Card blueCard = new Card(Type.BLUE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card stun = new Card(Type.ACTION, Card.STUNNED);
		
		
		//Give one of the players a outwit card to play
		Card outwit = new Card(Type.ACTION, Card.OUTWIT);
		game.getPlayer(0).getHand().add(outwit);
		
		//Give target player a custom display
		game.getDisplay(0).add(blueCard);
		game.getDisplay(0).add(greenCard);
		
		game.getStun(1).add(stun); // give the target a stun
		game.getDisplay(1).add(redCard);
	}

	//Setup a game where the target's display has only 1 card and has a shield
	public void displayWithOneCard() {
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
		Card blueCard = new Card(Type.BLUE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card stun = new Card(Type.ACTION, Card.STUNNED);
		
		
		//Give one of the players a outwit card to play
		Card outwit = new Card(Type.ACTION, Card.OUTWIT);
		game.getPlayer(0).getHand().add(outwit);
		
		//Give target player a custom display
		game.getDisplay(0).add(blueCard);
		game.getDisplay(0).add(greenCard);
		
		game.getStun(1).add(stun); // give the target a stun
		game.getDisplay(1).add(redCard);
	}

	
	@Test
	public void outwitWithMultipleCardsDisplayTest() {
		DisplayWithMultiTypes();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has a maiden at the 4th position
		*/
		assertEquals(game.getDisplay(1).getCard(3).getCardType(), Type.WHITE);
		assertEquals(game.getDisplay(1).getCard(3).getCardValue(), 6);
		
		//test the size of discard deck before playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		assertEquals(game.getDisplay(0).deckSize(), 3);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 4);
		
		//play outwit
		RulesEngine.outwit(game, cardPos, Flag.DISPLAY, 2, 1, Flag.DISPLAY, 3);
		
		//test the size of discard deck after playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 1);
		
		//Test to make sure both displays are still the same size
		assertEquals(game.getDisplay(0).deckSize(), 3);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 4);

		/*
		 * test to make sure the target does not have a maiden at the 4th position anymore
		*/
		assertFalse(game.getDisplay(1).getCard(3).getCardType() == Type.WHITE);
		assertFalse(game.getDisplay(1).getCard(3).getCardValue() == 6);
		
		/*
		 * test to make sure the target has a blue at the 4th position
		*/
		assertEquals(game.getDisplay(1).getCard(3).getCardType(), Type.BLUE);
		assertEquals(game.getDisplay(1).getCard(3).getCardValue(), 3);
		
		/*
		 * test to make sure the player's display has maiden
		*/
		assertEquals(game.getDisplay(0).getCard(2).getCardType(), Type.WHITE);
		assertEquals(game.getDisplay(0).getCard(2).getCardValue(), 6);
		
		/*
		 * test to make sure the first card in the discard deck is a outwit
		*/
		assertEquals(game.getDiscardDeck().getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getDiscardDeck().getCard(0).getCardValue(), Card.OUTWIT);
	}

	
	@Test
	public void outwitWithShieldTest() {
		targetWithShield();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		
		//test the size of discard deck before playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//test the size of the target's display
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		//test the size of the target's shield deck
		assertEquals(game.getShield(targetPlayer).deckSize(), 1);
		
		//test to make sure the target has a shield in his shield deck
		assertEquals(game.getShield(targetPlayer).getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getShield(targetPlayer).getCard(0).getCardValue(), Card.SHIELD);
		
		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 2);
		
		//play outwit
		RulesEngine.outwit(game, cardPos, Flag.DISPLAY, 1, 1, Flag.SHIELD, 0);

		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		//test the size of discard deck after playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 1);

		//test the size of the player's shield deck
		assertEquals(game.getShield(0).deckSize(), 1);
		
		//test to make sure the player has a shield in his shield deck
		assertEquals(game.getShield(0).getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getShield(0).getCard(0).getCardValue(), Card.SHIELD);

		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 1);

		//test the size of the target's display and make sure they have a green card
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 2);
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardType() == Type.GREEN);
	}

	
	@Test
	public void outwitWithStunTest() {
		targetWithStun();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		
		//test the size of discard deck before playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//test the size of the target's display
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		//test the size of the target's stun deck
		assertEquals(game.getStun(targetPlayer).deckSize(), 1);
		
		//test to make sure the target has a stun in his stun deck
		assertEquals(game.getStun(targetPlayer).getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getStun(targetPlayer).getCard(0).getCardValue(), Card.STUNNED);
		
		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 2);
		
		//play outwit
		RulesEngine.outwit(game, cardPos, Flag.DISPLAY, 1, 1, Flag.STUN, 0);

		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		//test the size of discard deck after playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 1);

		//test the size of the player's stun deck
		assertEquals(game.getStun(0).deckSize(), 1);
		
		//test to make sure the player has a stun in his stun deck
		assertEquals(game.getStun(0).getCard(0).getCardType(), Type.ACTION);
		assertEquals(game.getStun(0).getCard(0).getCardValue(), Card.STUNNED);

		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 1);

		//test the size of the target's display and make sure they have a green card
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 2);
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardType() == Type.GREEN);
	}

	
	@Test
	public void outwitWithOneCardTest() {
		displayWithOneCard();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardValue() == 4);
		
		//test the size of discard deck before playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//test the size of the target's display
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		
		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 2);
		
		//play outwit
		RulesEngine.outwit(game, cardPos, Flag.DISPLAY, 1, 1, Flag.DISPLAY, 0);

		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		//test the size of discard deck after playing outwit
		assertEquals(game.getDiscardDeck().deckSize(), 0);

		//test the size of the player's display
		assertEquals(game.getDisplay(0).deckSize(), 2);

		//test the size of the target's display and make sure they have a green card
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardValue() == 4);
	}

}
