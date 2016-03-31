package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class DisgraceTest {
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
		game.setTournamentColour(Type.PURPLE);
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card greenOne = new Card(Type.GREEN, 1);
		Card blueThree = new Card(Type.BLUE, 3);
		Card redFour = new Card(Type.RED, 4);
		Card squireThree = new Card(Type.WHITE, 3);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a disgrace card to play
		Card disgrace = new Card(Type.ACTION, Card.DISGRACE);
		game.getAllPlayers().get(0).getHand().add(disgrace);
		
		//Give target player a custom display
		game.getDisplay(0).add(squireThree);
		game.getDisplay(0).add(purpleSeven);
		game.getDisplay(0).add(greenOne);

		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(1).add(squireTwo);
		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(purpleSeven);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(1).add(greenOne);
		
		game.getDisplay(2).add(purpleSeven);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
		game.getDisplay(2).add(greenOne);
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
		game.setTournamentColour(Type.PURPLE);
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleThree = new Card(Type.PURPLE, 3);
		Card yellowFour = new Card(Type.YELLOW, 4);
		Card blueThree = new Card(Type.BLUE, 3);
		Card purpleSix = new Card(Type.PURPLE, 6);
		Card redFour = new Card(Type.RED, 4);
		Card squireThree = new Card(Type.WHITE, 3);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a disgrace card to play
		Card disgrace = new Card(Type.ACTION, Card.DISGRACE);
		game.getAllPlayers().get(0).getHand().add(disgrace);
		
		//Give target player a custom display
		game.getDisplay(0).add(squireTwo);
		game.getDisplay(0).add(yellowFour);
		
		game.getDisplay(1).add(squireThree);
		
		game.getDisplay(2).add(purpleSix);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
		game.getDisplay(2).add(purpleThree);
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
		game.setTournamentColour(Type.PURPLE);
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card squireThree = new Card(Type.WHITE, 3);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a disgrace card to play
		Card disgrace = new Card(Type.ACTION, Card.DISGRACE);
		game.getAllPlayers().get(0).getHand().add(disgrace);
		
		//Give target player a custom display
		game.getDisplay(0).add(maiden);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(2).add(squireTwo);
	}
	
	
	//Setup a game where one of the targets has a shield
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
		game.setTournamentColour(Type.PURPLE);
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card greenOne = new Card(Type.GREEN, 1);
		Card blueThree = new Card(Type.BLUE, 3);
		Card redFour = new Card(Type.RED, 4);
		Card squireThree = new Card(Type.WHITE, 3);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		Card shield = new Card (Type.ACTION, Card.SHIELD);
		
		//Give one of the players a disgrace card to play
		Card disgrace = new Card(Type.ACTION, Card.DISGRACE);
		game.getAllPlayers().get(0).getHand().add(disgrace);
		
		//Give target player a custom display
		game.getDisplay(0).add(squireThree);
		game.getDisplay(0).add(purpleSeven);
		game.getDisplay(0).add(greenOne);

		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(purpleFive);
		game.getDisplay(1).add(squireTwo);
		game.getDisplay(1).add(greenOne);
		game.getDisplay(1).add(purpleSeven);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(1).add(greenOne);
		game.getShield(1).add(shield); // give the player a shield
		
		game.getDisplay(2).add(purpleSeven);
		game.getDisplay(2).add(blueThree);
		game.getDisplay(2).add(redFour);
		game.getDisplay(2).add(maiden);
		game.getDisplay(2).add(greenOne);
	}

		
	
	//A test where the targets' displays have multiple cards 
	@Test
	public void disgraceValidTargetsTest() {
		displaysWithMultiCards();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 3);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 3);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 7);
		assertTrue(game.getDisplay(1).getCard(2).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(2).getCardValue() == 2);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 5);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 6);
		
		//test the size of discard deck before playing disgrace
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play disgrace
		RulesEngine.disgrace(game, cardPos);
		
		
		//test the size of discard deck after playing disgrace
		//a total of 3 cards should have been discarded (1 disgrace card + 1 player supporter card + 3 opponent supporter cards)
		assertEquals(game.getDiscardDeck().deckSize(), 5);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 7);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 5);
		assertTrue(game.getDisplay(1).getCard(2).getCardType() == Type.GREEN);
		assertTrue(game.getDisplay(1).getCard(2).getCardValue() == 1);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.GREEN);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 1);
	}
	

	//A test where some targets have only one card in their display 
	@Test
	public void disgraceSomeValidTargetsTest() {
		oneDisplayWithOneCard();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 2);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 5);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 6);
		
		//test the size of discard deck before playing disgrace
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play disgrace
		RulesEngine.disgrace(game, cardPos);
		
		
		//test the size of discard deck after playing disgrace
		//a total of 2 cards should have been discarded (1 disgrace card + 1 opponent supporter card + 1 player supporter card)
		assertEquals(game.getDiscardDeck().deckSize(), 3);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 1);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.YELLOW);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 4);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 3);
	}
	
	
	//A test where the targets' displays have one card each 
	@Test
	public void disgraceInvalidTargetsTest() {
		allDisplaysWithOneCard();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 1);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 6);
		
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 1);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 2);
		
		//test the size of discard deck before playing disgrace
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play disgrace
		RulesEngine.disgrace(game, cardPos);
		
		
		//test the size of discard deck after playing disgrace
		//a total of 0 card should have been discarded
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 1);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(0).getCard(playerSize-1).getCardValue() == 6);
		
		
		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 1);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(firstTargetSize-1).getCardValue() == 3);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 1);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(secondTargetSize-1).getCardValue() == 2);
	}

	
	//A test where one of the targets has a shield
	@Test
	public void disgraceShieldTest() {
		displayWithShield();
		int cardPos = game.getHand(0).deckSize()-1;
		
		
		/*
		 * test to make sure the player and targets have the proper display size and proper
		 * cards as the last played cards
		*/
		int playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 3);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 3);
		
		int firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 7);
		assertTrue(game.getDisplay(1).getCard(2).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(2).getCardValue() == 2);

		int secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 5);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 6);
		
		//test the size of discard deck before playing disgrace
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play disgrace
		RulesEngine.disgrace(game, cardPos);
		
		
		//test the size of discard deck after playing disgrace
		//a total of 3 cards should have been discarded (1 disgrace card + 1 player supporter card + 3 opponent supporter cards)
		assertEquals(game.getDiscardDeck().deckSize(), 3);
		
		/*
		 * test to make sure the player and targets have the right size display and proper cards in their display
		 * as their last played cards
		*/
		playerSize = game.getDisplay(0).deckSize();
		assertTrue(playerSize == 2);
		assertTrue(game.getDisplay(0).getCard(0).getCardType() == Type.PURPLE);
		assertTrue(game.getDisplay(0).getCard(0).getCardValue() == 7);
		

		firstTargetSize = game.getDisplay(1).deckSize();
		assertTrue(firstTargetSize == 7);
		assertTrue(game.getDisplay(1).getCard(2).getCardType() == Type.WHITE);
		assertTrue(game.getDisplay(1).getCard(2).getCardValue() == 2);
		

		secondTargetSize = game.getDisplay(2).deckSize();
		assertTrue(secondTargetSize == 4);
		assertTrue(game.getDisplay(2).getCard(3).getCardType() == Type.GREEN);
		assertTrue(game.getDisplay(2).getCard(3).getCardValue() == 1);
	}
	

}
