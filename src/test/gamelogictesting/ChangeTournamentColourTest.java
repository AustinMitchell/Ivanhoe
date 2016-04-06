package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.Card;
import model.Flag;
import model.GameState;
import model.Player;
import model.Type;

public class ChangeTournamentColourTest {
	Player player1;
	Player player2;
	ArrayList<Player> players;
	GameState game;
	
	
	@Before
	public void setupGameState() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player1.enterTournament();
		player2 = new Player("Austin");
		player2.enterTournament();
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		RulesEngine.setColour(game, String.valueOf(Type.PURPLE));
	}

	@Test
	public void unhorseTest() {
		String returnString = "";
		Card unhorseCard = new Card(Type.ACTION, Card.UNHORSE);
		int playerPos = game.getTurn();
		int cardPos = 0;
		game.getAllPlayers().get(playerPos).getHand().add(unhorseCard);
		for(int i = 0; i < game.getAllPlayers().get(playerPos).getHand().deckSize(); i++) {
			if((game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardType() == Type.ACTION) &&
					(game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardValue() == Card.UNHORSE)) {
						cardPos = i;
						break;
					}
		}
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + Type.RED);
		assertEquals(Type.PURPLE, game.getTournamentColour());
		assertEquals(returnString, RulesEngine.unhorse(game, cardPos, Type.RED));
		assertEquals(Type.RED, game.getTournamentColour());
	}
	
	@Test
	public void changeWeaponTest() {
		String returnString = "";
		int playerPos = game.getTurn();
		Card changeweapon = new Card(Type.ACTION, Card.CHANGE_WEAPON);
		game.setTournamentColour(Type.BLUE);
		game.getAllPlayers().get(playerPos).getHand().add(changeweapon);
		int cardPos = 0;
		for(int i = 0; i < game.getAllPlayers().get(playerPos).getHand().deckSize(); i++) {
			if((game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardType() == Type.ACTION) &&
					(game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardValue() == Card.DROP_WEAPON)) {
						cardPos = i;
					}
		}
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + Type.YELLOW);
		assertEquals(Type.BLUE, game.getTournamentColour());
		assertEquals(returnString, RulesEngine.changeWeapon(game, cardPos, Type.YELLOW));
		assertEquals(Type.YELLOW, game.getTournamentColour());
		//assertEquals(3, game.getDiscardDeck().deckSize());
	}
	
	@Test
	public void dropWeaponTest() {
		String returnString = "";
		int playerPos = game.getTurn();
		Card dropWeaponCard = new Card(Type.ACTION, Card.DROP_WEAPON);
		game.setTournamentColour(Type.BLUE);
		game.getAllPlayers().get(playerPos).getHand().add(dropWeaponCard);
		int cardPos = 0;
		for(int i = 0; i < game.getAllPlayers().get(playerPos).getHand().deckSize(); i++) {
			if((game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardType() == Type.ACTION) &&
					(game.getAllPlayers().get(playerPos).getHand().getCard(i).getCardValue() == Card.DROP_WEAPON)) {
						cardPos = i;
					}
		}
		returnString = (Flag.ACTION_CARD + ":" + cardPos);
		assertEquals(Type.BLUE, game.getTournamentColour());
		assertEquals(returnString, RulesEngine.dropWeapon(game, cardPos));
		assertEquals(Type.GREEN, game.getTournamentColour());
	}

}
