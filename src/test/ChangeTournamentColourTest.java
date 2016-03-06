package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;

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
		game.setTournamentColour(Type.PURPLE);
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
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + cardPos + ":" + Type.RED);
		assertEquals(Type.PURPLE, game.getTournamentColour());
		assertEquals(returnString, RulesEngine.unhorse(game, cardPos, Type.RED));
		assertEquals(Type.RED, game.getTournamentColour());
	}
	
	@Test
	public void changeWeaponTest() {
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
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + cardPos + ":" + Type.YELLOW);
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
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + cardPos);
		assertEquals(Type.BLUE, game.getTournamentColour());
		assertEquals(returnString, RulesEngine.dropWeapon(game, cardPos));
		assertEquals(Type.GREEN, game.getTournamentColour());
	}

}
