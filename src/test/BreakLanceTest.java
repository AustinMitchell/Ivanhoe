package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Card;
import models.Deck;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class BreakLanceTest {
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
		game.setTournamentColour(Type.PURPLE);
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard1 = new Card(Type.PURPLE, 3);
		Card purpleCard2 = new Card(Type.PURPLE, 5);
		Card purpleCard3 = new Card(Type.PURPLE, 7);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a break lance card to play
		Card breakLance = new Card(Type.ACTION, Card.BREAK_LANCE);
		game.getAllPlayers().get(0).getHand().add(breakLance);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
		game.getDisplay(1).add(purpleCard1);
		game.getDisplay(1).add(blueCard);
		game.getDisplay(1).add(purpleCard2);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(purpleCard3);
		game.getDisplay(1).add(maiden);
	}

	
	//Setup a game where the target's display contains only purple cards
	public void AllPurpleDisplay() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		
		game.setTurn(0);
		
		//create cards to be added to player's hand and target's display
		Card purpleCard1 = new Card(Type.PURPLE, 3);
		Card purpleCard2 = new Card(Type.PURPLE, 5);
		Card purpleCard3 = new Card(Type.PURPLE, 7);
		
		//Give one of the players a break lance card to play
		Card breakLance = new Card(Type.ACTION, Card.BREAK_LANCE);
		game.getAllPlayers().get(0).getHand().add(breakLance);
		
		//Give target player a custom display
		game.getDisplay(1).add(purpleCard1);
		game.getDisplay(1).add(purpleCard2);
		game.getDisplay(1).add(purpleCard3);
	}

	
	//Setup a game where the target's display contains only purple cards
	public void OnePurpleDisplay() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		
		game.setTurn(0);
		
		//create cards to be added to player's hand and target's display
		Card purpleCard1 = new Card(Type.PURPLE, 3);
		
		//Give one of the players a break lance card to play
		Card breakLance = new Card(Type.ACTION, Card.BREAK_LANCE);
		game.getAllPlayers().get(0).getHand().add(breakLance);
		
		//Give target player a custom display
		game.getDisplay(1).add(purpleCard1);
	}
	

	//Setup a game where the target has a shield
	public void displayWithShield() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard1 = new Card(Type.PURPLE, 3);
		Card purpleCard2 = new Card(Type.PURPLE, 5);
		Card purpleCard3 = new Card(Type.PURPLE, 7);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		Card shield = new Card (Type.ACTION, Card.SHIELD);
		
		//Give one of the players a break lance card to play
		Card breakLance = new Card(Type.ACTION, Card.BREAK_LANCE);
		game.getAllPlayers().get(0).getHand().add(breakLance);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
		game.getDisplay(1).add(purpleCard1);
		game.getDisplay(1).add(blueCard);
		game.getDisplay(1).add(purpleCard2);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(purpleCard3);
		game.getDisplay(1).add(maiden);
		game.getShield(1).add(shield); // give the player a shield
	}

	
	//A test where the target player's display has multiple cards that are both purple and non-purple
	@Test
	public void breakLanceOnMultiTypesTest() {
		DisplayWithMultiTypes();
		int cardPos = 0;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has purple cards. In this case we are testing the 2nd card in display
		 * which we already know to be purple
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() == Type.PURPLE);
		
		//test the size of discard deck before playing break lance
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play break lance
		RulesEngine.breakLance(game, cardPos, targetPlayer);
		
		
		//test the size of discard deck after playing break lance
		//a total of 4 cards should have been discarded (1 break lance card + 3 purple cards)
		assertEquals(game.getDiscardDeck().deckSize(), 4);
		
		/*
		 * test to make sure the target has no purple cards. In this case we are testing the 2nd card in display
		 * which used to be purple but isn't anymore.
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() != Type.PURPLE);
	}
	
	
	//A test where the target player's display contains only purple cards
	@Test
	public void breakLanceOnAllPurpleDisplayTest() {
		AllPurpleDisplay();
		int cardPos = 0;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has purple cards. In this case we are testing the 2nd card in display
		 * which we already know to be purple
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() == Type.PURPLE);
		
		//test the size of discard deck before playing break lance
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play break lance
		RulesEngine.breakLance(game, cardPos, targetPlayer);
		
		/*
		 * test the size of discard deck after playing break lance
		 * a total of 3 cards should have been discarded (1 break lance card + 2 purple cards)
		 */
		assertEquals(game.getDiscardDeck().deckSize(), 3);
		
		/*
		 * test to make sure the target has one purple card left. In this case we are testing the 
		 * 1st card in display to make sure it is still a purple card.
		 * The card was not discarded because it is against the rules to leave an opponent with 
		 * an empty display
		*/
		assertTrue(game.getDisplay(1).getCard(0).getCardType() == Type.PURPLE);
	}
	
	
	//A test where the target player's display contains only one purple card
	@Test
	public void breakLanceOnOnePurpleTest() {
		OnePurpleDisplay();
		int cardPos = 0;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has a purple card. In this case we are testing the 1st card in display
		 * which we already know to be purple
		*/
		assertTrue(game.getDisplay(1).getCard(0).getCardType() == Type.PURPLE);
		
		//test the size of discard deck before playing break lance
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play break lance
		RulesEngine.breakLance(game, cardPos, targetPlayer);
		
		//test the size of discard deck after playing break lance
		//a total of 3 cards should have been discarded (1 break lance card)
		assertEquals(game.getDiscardDeck().deckSize(), 1);
		
		/*
		 * test to make sure the target has one purple card left. In this case we are testing the 
		 * 1st card in display to make sure it is still a purple card.
		 * The card was not discarded because it is against the rules to leave an opponent with 
		 * an empty display
		*/
		assertTrue(game.getDisplay(1).getCard(0).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(1).getCard(0).getCardValue() == 3);

	}

	
	//A test where the target player has a shield
	@Test
	public void breakLanceShieldTest() {
		displayWithShield();
		int cardPos = 0;
		int targetPlayer = 1;
		/*
		 * test to make sure the target has purple cards. In this case we are testing the 2nd card in display
		 * which we already know to be purple
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() == Type.PURPLE);
		
		//test the size of discard deck before playing break lance
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play break lance
		RulesEngine.breakLance(game, cardPos, targetPlayer);
		
		
		//test the size of discard deck after playing break lance
		//a total of 1 card should have been discarded due to the shield (1 break lance card)
		assertEquals(game.getDiscardDeck().deckSize(), 1);
		
		/*
		 * test to make sure the target still has purple cards.
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() == Type.PURPLE);
	}
		
}
