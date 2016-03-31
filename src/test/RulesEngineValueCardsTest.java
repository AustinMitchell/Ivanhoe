package test;

import static org.junit.Assert.*;
import rulesengine.Type;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;

public class RulesEngineValueCardsTest {
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
	public void playingValueCardTest() {
		String returnString = "";
		for(int i = 0; i < game.getAllPlayers().get(0).getHand().deckSize(); i++) {
			if(!(game.getAllPlayers().get(game.getTurn()).getHand().getCard(i).getCardType() == Type.ACTION)) {
				int cardPos = i;
				returnString = ("card:" + cardPos);
				assertEquals(returnString, RulesEngine.playValueCard(game, i));
				break;
			}
		}
	}
}
