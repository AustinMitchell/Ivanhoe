package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.Card;
import model.GameState;
import model.Player;
import model.Type;

public class OutmaneuverTest {
	Player player1;
	Player player2;
	Player player3;
	ArrayList<Player> players;
	GameState game;


	//Setup a game where the targets' displays have multiple cards
	public void displaysWithMultiCards() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleThree = new Card(Type.PURPLE, 3);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card yellowFour = new Card(Type.YELLOW, 4);
		Card greenOne = new Card(Type.GREEN, 1);
		Card blueFour = new Card(Type.BLUE, 4);
		Card blueThree = new Card(Type.BLUE, 3);
		Card redFour = new Card(Type.RED, 4);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a outmaneuver card to play
		Card outmaneuver = new Card(Type.ACTION, Card.OUTMANEUVER);
		game.getAllPlayers().get(0).getHand().add(outmaneuver);
		
		//Give target player a custom display
		game.getDisplay(0).add(purpleThree);
		game.getDisplay(0).add(yellowFour);
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(blueFour);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(2).add(purpleSeven);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
	}

	
	//Setup a game where one of the targets have only one card in their display
	public void oneDisplayWithOneCard() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleThree = new Card(Type.PURPLE, 3);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card yellowFour = new Card(Type.YELLOW, 4);
		Card blueThree = new Card(Type.BLUE, 3);
		Card redFour = new Card(Type.RED, 4);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a outmaneuver card to play
		Card outmaneuver = new Card(Type.ACTION, Card.OUTMANEUVER);
		game.getAllPlayers().get(0).getHand().add(outmaneuver);
		
		//Give target player a custom display
		game.getDisplay(0).add(purpleThree);
		game.getDisplay(0).add(yellowFour);
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(2).add(purpleSeven);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
	}

	
	//Setup a game where all target displays have only one card
	public void allDisplaysWithOneCard() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleThree = new Card(Type.PURPLE, 3);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card yellowFour = new Card(Type.YELLOW, 4);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a outmaneuver card to play
		Card outmaneuver = new Card(Type.ACTION, Card.OUTMANEUVER);
		game.getAllPlayers().get(0).getHand().add(outmaneuver);
		
		//Give target player a custom display
		game.getDisplay(0).add(purpleThree);
		game.getDisplay(0).add(yellowFour);
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(2).add(maiden);
	}
	
	
	//Setup a game where the targets has a shield
	public void displayWithShield() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleThree = new Card(Type.PURPLE, 3);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card yellowFour = new Card(Type.YELLOW, 4);
		Card greenOne = new Card(Type.GREEN, 1);
		Card blueFour = new Card(Type.BLUE, 4);
		Card blueThree = new Card(Type.BLUE, 3);
		Card redFour = new Card(Type.RED, 4);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		Card shield = new Card(Type.ACTION, Card.SHIELD);
		
		//Give one of the players a outmaneuver card to play
		Card outmaneuver = new Card(Type.ACTION, Card.OUTMANEUVER);
		game.getAllPlayers().get(0).getHand().add(outmaneuver);
		
		//Give target player a custom display
		game.getDisplay(0).add(purpleThree);
		game.getDisplay(0).add(yellowFour);
		
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(blueFour);
		game.getShield(1).add(shield); //give player a shield
		
		game.getDisplay(1).add(squireThree);
		game.getDisplay(2).add(purpleSeven);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
	}

	
	//A test where the targets' displays have multiple cards 
	@Test
	public void outmaneuverValidTargetsTest() {
		displaysWithMultiCards();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 4);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 6);
		
		//test the size of discard deck before playing outmaneuver
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play outmaneuver
		RulesEngine.outmaneuver(game, cardPos);
		
		
		//test the size of discard deck after playing outmaneuver
		//a total of 3 cards should have been discarded (1 outmaneuver card + 2 opponent cards)
		assertEquals(game.getDiscardDeck().deckSize(), 3);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 3);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.BLUE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 4);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 3);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.RED);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 4);
	}
	

	//A test where some targets have only one card in their display 
	@Test
	public void outmaneuverSomeValidTargetsTest() {
		oneDisplayWithOneCard();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 5);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 6);
		
		//test the size of discard deck before playing outmaneuver
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play outmaneuver
		RulesEngine.outmaneuver(game, cardPos);
		
		
		//test the size of discard deck after playing outmaneuver
		//a total of 2 cards should have been discarded (1 outmaneuver card + 1 opponent card)
		assertEquals(game.getDiscardDeck().deckSize(), 2);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 5);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 3);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.RED);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 4);
	}
	
	
	//A test where the targets' displays have one card each 
	@Test
	public void outmaneuverInvalidTargetsTest() {
		allDisplaysWithOneCard();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 5);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 1);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 6);
		
		//test the size of discard deck before playing outmaneuver
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play outmaneuver
		RulesEngine.outmaneuver(game, cardPos);
		
		
		//test the size of discard deck after playing outmaneuver
		//a total of 0 card should have been discarded 
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 5);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 1);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 6);
	}
	
	
	//A test where the target has a shield
	@Test
	public void outmaneuverShieldTest() {
		displayWithShield();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 4);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 6);
		
		//test the size of discard deck before playing outmaneuver
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play outmaneuver
		RulesEngine.outmaneuver(game, cardPos);
		
		
		//test the size of discard deck after playing outmaneuver
		//a total of 3 cards should have been discarded (1 outmaneuver card + 1 opponent cards)
		assertEquals(game.getDiscardDeck().deckSize(), 2);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 4);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 4);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 3);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.RED);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 4);
	}
	
	
}
