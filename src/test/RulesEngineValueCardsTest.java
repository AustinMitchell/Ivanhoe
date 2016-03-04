package test;

import static org.junit.Assert.*;
import rulesengine.Type;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import models.*;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;

public class RulesEngineValueCardsTest {
	Player player1;
	Player player2;
	ArrayList<Player> players;
	GameState game;
	RulesEngine engine;
	
	
	@Before
	public void setupGameState() {
		players = new ArrayList();
		player1 = new Player("Nick");
		player1.enterTournament();
		player2 = new Player("Austin");
		player2.enterTournament();
		players.add(player1);
		players.add(player2);
		game = new GameState();
		game.initializeServer(players);
		game.setTournamentColour(Type.PURPLE);
		engine = new RulesEngine();
		
	}
	@Test
	public void playingValueCardTest() {
		String returnString = "";
		for(int i = 0; i < game.getAllPlayers().get(0).getHand().deckSize(); i++) {
			if(!(game.getAllPlayers().get(game.getTurn()).getHand().getCard(i).getCardType() == Type.ACTION)) {
				int playerPos = game.getTurn();
				int cardType = game.getAllPlayers().get(game.getTurn()).getHand().getCard(i).getCardType();
				int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(i).getCardValue();
				int cardPos = i;
				returnString = ("card:" + playerPos + ":" + cardType + ":" + cardValue + ":" + cardPos);
				assertEquals(returnString, engine.playValueCard(game, i));
				break;
			}
		}
	}
}
