package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class RiposteTest {
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
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 2);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a riposte card to play
		Card riposte = new Card(Type.ACTION, Card.RIPOSTE);
		game.getAllPlayers().get(0).getHand().add(riposte);
		
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
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card redCard = new Card(Type.RED, 4);
		
		//Give one of the players a riposte card to play
		Card riposte = new Card(Type.ACTION, Card.RIPOSTE);
		game.getAllPlayers().get(0).getHand().add(riposte);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
	}

	
	//Setup a game where the targets has a shield
	public void DisplayWithShield() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 2);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		Card shield = new Card(Type.ACTION, Card.SHIELD);
		
		//Give one of the players a riposte card to play
		Card riposte = new Card(Type.ACTION, Card.RIPOSTE);
		game.getAllPlayers().get(0).getHand().add(riposte);
		
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
	public void riposteWithMultipleCardsDisplayTest() {
		DisplayWithMultiTypes();
		
		int cardPos = 0;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a maiden as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).isMaiden());
		
		/*
		 * test to make sure the player's display is still empty
		 */
		assertTrue(game.getDisplay(0).deckSize() == 0);
		
		
		//assertFalse(game.getDisplay(0).getCard(targetDisplaySize-1).isMaiden());
		
		//test the size of discard deck before playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 7);
		
		//play Riposte
		RulesEngine.riposte(game, cardPos, targetPlayer);
		
		//test the size of discard deck after playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 1);
		
		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		/*
		 * test to make sure the target's last played card is no longer a maiden
		*/
		assertFalse(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).isMaiden());
		
		/*
		 * test to make sure the player's last played card is now a maiden
		*/
		assertTrue(game.getDisplay(0).getCard(0).isMaiden());
	}

	
	@Test
	public void RiposteWithOneCardDisplayTest() {
		DisplayWithOneCard();
		
		int cardPos = 0;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a red card as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		
		/*
		 * test to make sure the player's display is still empty
		 */
		assertTrue(game.getDisplay(0).deckSize() == 0);
		
		
		//test the size of discard deck before playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//test the size of the target's display
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 1);
		
		//play Riposte
		RulesEngine.riposte(game, cardPos, targetPlayer);
		
		//test the size of discard deck after playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		/*
		 * test to make sure the target's last played card is still the single red card
		*/
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).getCardType() == Type.RED);
		
		/*
		 * test to make sure the player's display is still empty
		*/
		assertEquals(game.getDisplay(0).deckSize(), 0);
	}

	

	//A test where the target has a shield
	@Test
	public void riposteShieldTest() {
		DisplayWithShield();
		
		int cardPos = 0;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a maiden as the last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).isMaiden());
		
		/*
		 * test to make sure the player's display is still empty
		 */
		assertTrue(game.getDisplay(0).deckSize() == 0);
		
		
		//assertFalse(game.getDisplay(0).getCard(targetDisplaySize-1).isMaiden());
		
		//test the size of discard deck before playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 7);
		
		//play Riposte
		RulesEngine.riposte(game, cardPos, targetPlayer);
		
		//test the size of discard deck after playing Riposte
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//Update this variable after a card has been removed from the display
		targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		
		/*
		 * test to make sure the target's last played card is still a maiden
		*/
		assertTrue(game.getDisplay(targetPlayer).getCard(targetDisplaySize-1).isMaiden());
		
		/*
		 * test to make sure the player's display is still empty
		*/
		assertTrue(game.getDisplay(0).deckSize() == 0);
	}

	
}
