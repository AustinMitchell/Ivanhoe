package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

public class AdaptTest {
	Player player1;
	Player player2;
	Player player3;
	ArrayList<Player> players;
	GameState game;
	
	//Setup a game where the target's display has multiple coloured cards
	public void DisplayWithMultiTypes() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card redThree = new Card(Type.RED, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowThree = new Card(Type.YELLOW, 3);
		Card blueFive = new Card(Type.BLUE, 5);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card redFour = new Card(Type.RED, 4);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		Card adapt = new Card(Type.ACTION, Card.ADAPT);
		
		//Give players custom displays
		game.getDisplay(0).add(redThree);
		game.getDisplay(0).add(yellowThree);
		game.getDisplay(0).add(redFour);
		game.getDisplay(0).add(purpleFive);
		game.getDisplay(0).add(blueFive);
		game.getHand(0).add(adapt); //Give player an adapt card
		
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(1).add(squireTwo);
		game.getDisplay(1).add(maiden);
		
		game.getDisplay(2).add(greenCard);
		game.getDisplay(2).add(squireThree);
		game.getDisplay(2).add(blueFive);
		game.getDisplay(2).add(maiden);
	}
	
	//Setup a game where the target's display has only 1 card and has a shield
	public void DisplayWithShield() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card redThree = new Card(Type.RED, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowThree = new Card(Type.YELLOW, 3);
		Card blueFive = new Card(Type.BLUE, 5);
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card redFour = new Card(Type.RED, 4);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		Card adapt = new Card(Type.ACTION, Card.ADAPT);
		Card shield = new Card(Type.ACTION, Card.SHIELD);
		
		//Give players custom displays
		game.getDisplay(0).add(redThree);
		game.getDisplay(0).add(yellowThree);
		game.getDisplay(0).add(redFour);
		game.getDisplay(0).add(purpleFive);
		game.getDisplay(0).add(blueFive);
		game.getHand(0).add(adapt); //Give player an adapt card
		
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(1).add(squireTwo);
		game.getDisplay(1).add(maiden);
		game.getShield(1).add(shield); //give target a shield
		
		game.getDisplay(2).add(greenCard);
		game.getDisplay(2).add(squireThree);
		game.getDisplay(2).add(blueFive);
		game.getDisplay(2).add(maiden);
	}

	//Setup a game where the targets have no duplicate values in displays
	public void DisplaysWithNoDoubles() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player2 = new Player("Ausitn");
		player3 = new Player("Ahmed");
		players.add(player1);
		players.add(player2);
		players.add(player3);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		game.setTurn(0);
		
		
		//create cards to be added to player's hand and target's display
		Card redThree = new Card(Type.RED, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card blueFive = new Card(Type.BLUE, 5);
		Card redFour = new Card(Type.RED, 4);
		Card squireTwo = new Card(Type.WHITE, 2);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		Card adapt = new Card(Type.ACTION, Card.ADAPT);
		
		//Give players custom displays
		game.getDisplay(0).add(redThree);
		game.getDisplay(0).add(redFour);
		game.getDisplay(0).add(greenCard);
		game.getDisplay(0).add(blueFive);
		game.getHand(0).add(adapt); //Give player an adapt card
		
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squireThree);
		game.getDisplay(1).add(squireTwo);
		game.getDisplay(1).add(maiden);
		
		game.getDisplay(2).add(greenCard);
		game.getDisplay(2).add(squireThree);
		game.getDisplay(2).add(blueFive);
		game.getDisplay(2).add(maiden);
	}
	
	//Test adapt against valid targets
	@Test
	public void adaptWithMultipleCardsDisplayTest() {
		DisplayWithMultiTypes();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetPlayer2 = 2;
		
		/*
		 * test to make sure the player has cards of values 3 and 5 at the proper positions
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(0).getCard(1).getCardValue(), 3);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardValue(), 5);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardType(), Type.BLUE);
		assertEquals(game.getDisplay(0).deckSize(), 5);
		
		/*
		 * test to make sure target1 has a card value 1 at the proper position.
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer).getCard(1).getCardValue(), 1);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 5);
		
		/*
		 * test to make sure player 2 display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
		
		//test the size of discard deck before playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//play adapt
		RulesEngine.adapt(game, cardPos);
		
		//test the size of discard deck after playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 4);
		
		/*
		 * test to make sure the player's display has changed accordingly
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(0).getCard(1).getCardValue(), 4);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardValue(), 5);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardType(), Type.BLUE);
		assertEquals(game.getDisplay(0).deckSize(), 3);
		
		/*
		 * test to make sure target1 has a card value 3 at the proper position in place of the discarded card
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer).getCard(1).getCardValue(), 3);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 4);
		
		/*
		 * test to make sure player 2 display size is correct and unaffected
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
	}


	//Test adapt against shielded target
	@Test
	public void adaptWithShieldTest() {
		DisplayWithShield();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetPlayer2 = 2;
		/*
		 * test to make sure the player has cards of values 3 and 5 at the proper positions
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(0).getCard(1).getCardValue(), 3);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardValue(), 5);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardType(), Type.BLUE);
		assertEquals(game.getDisplay(0).deckSize(), 5);
		
		/*
		 * test to make sure target1 has a card value 1 at the proper position.
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer).getCard(1).getCardValue(), 1);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 5);
		
		/*
		 * test to make sure player 2 display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
		
		//test the size of discard deck before playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//play adapt
		RulesEngine.adapt(game, cardPos);
		
		//test the size of discard deck after playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 3);
		
		/*
		 * test to make sure the player's display has changed accordingly
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(0).getCard(1).getCardValue(), 4);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardValue(), 5);
		assertEquals(game.getDisplay(0).getCard(game.getDisplay(0).deckSize()-1).getCardType(), Type.BLUE);
		assertEquals(game.getDisplay(0).deckSize(), 3);
		
		/*
		 * test to make sure target1 has a card value 1 at the proper position.
		 * also test to make sure their display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer).getCard(1).getCardValue(), 1);
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 5);
		
		/*
		 * test to make sure player 2 display size is correct and unaffected
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
	}

	
	//Test adapt against displays with no duplicate values (invalid targets)
	@Test
	public void adaptWithInvalidTargetsTest() {
		DisplaysWithNoDoubles();
		
		int cardPos = game.getHand(0).deckSize()-1;
		int targetPlayer = 1;
		int targetPlayer2 = 2;
		/*
		 * make sure the player's display is the right size
		*/
		assertEquals(game.getDisplay(0).deckSize(), 4);
		
		/*
		 * make sure the target's display is the right size
		*/
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 4);
		
		/*
		 * test to make sure player 2 display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
		
		//test the size of discard deck before playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		//play adapt
		RulesEngine.adapt(game, cardPos);
		
		//test the size of discard deck after playing adapt
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		
		/*
		 * make sure the player's display is the right size
		*/
		assertEquals(game.getDisplay(0).deckSize(), 4);
		
		/*
		 * make sure the target's display is the right size
		*/
		assertEquals(game.getDisplay(targetPlayer).deckSize(), 4);
		
		/*
		 * test to make sure player 2 display size is correct
		*/
		assertEquals(game.getDisplay(targetPlayer2).deckSize(), 4);
		
		/*
		 * make sure the player still has the adapt card since there were no valid targets
		 */
		assertEquals(game.getHand(0).getCard(game.getHand(0).deckSize()-1).getCardType(), Type.ACTION);
		assertEquals(game.getHand(0).getCard(game.getHand(0).deckSize()-1).getCardValue(), Card.ADAPT);
	}

}
