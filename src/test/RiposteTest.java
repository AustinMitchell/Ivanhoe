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
		game.setTournamentColour(Type.PURPLE);
		
		game.setTurn(1);
		
		
		//create cards to be added to player's hand and target's display
		Card purpleCard = new Card(Type.PURPLE, 3);
		Card greenCard = new Card(Type.GREEN, 1);
		Card yellowCard = new Card(Type.YELLOW, 2);
		Card blueCard = new Card(Type.BLUE, 3);
		Card redCard = new Card(Type.RED, 4);
		Card squire = new Card(Type.WHITE, 2);
		Card maiden = new Card (Type.WHITE, 6);
		
		//Give one of the players a break lance card to play
		Card breakLance = new Card(Type.ACTION, Card.BREAK_LANCE);
		game.getAllPlayers().get(0).getHand().add(breakLance);
		
		//Give target player a custom display
		game.getDisplay(1).add(redCard);
		game.getDisplay(1).add(purpleCard);
		game.getDisplay(1).add(blueCard);
		game.getDisplay(1).add(greenCard);
		game.getDisplay(1).add(squire);
		game.getDisplay(1).add(yellowCard);
		game.getDisplay(1).add(maiden);
	}

	
	
	
	
	@Test
	public void riposteWithMultipleCardsDisplayTest() {
		DisplayWithMultiTypes();
		
		int cardPos = 0;
		int targetPlayer = 1;
		int targetDisplaySize = game.getDisplay(targetPlayer).deckSize();
		/*
		 * test to make sure the target has a maiden as his last played card
		*/
		assertTrue(game.getDisplay(1).getCard(targetDisplaySize-1).isMaiden());
		
		/*
		 * test to make sure the player's display is still empty
		 */
		assertTrue(game.getDisplay(0).deckSize() == 0);
		//assertFalse(game.getDisplay(0).getCard(targetDisplaySize-1).isMaiden());
		
		//test the size of discard deck before playing break lance
		assertEquals(game.getDiscardDeck().deckSize(), 0);
		//play break lance
		RulesEngine.riposte(game, cardPos, targetPlayer);
		
		//test the size of discard deck after playing break lance
		//a total of 4 cards should have been discarded (1 break lance card + 3 purple cards)
		assertEquals(game.getDiscardDeck().deckSize(), 1);
		
		/*
		 * test to make sure the target has no purple cards. In this case we are testing the 2nd card in display
		 * which used to be purple but isn't anymore.
		*/
		assertTrue(game.getDisplay(1).getCard(1).getCardType() != Type.PURPLE);
	}

}
