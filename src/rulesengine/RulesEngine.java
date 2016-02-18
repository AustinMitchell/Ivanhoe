package rulesengine;

import java.util.ArrayList;

import models.Card;
import models.GameState;

public class RulesEngine {
	
	public String playValueCard(GameState game, int pos) {
		String returnString;
		
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(game.getTurn()).getHand().getCard(pos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(pos).getCardValue();
		returnString = ("" + playerPos + ":" + cardType + ":" + cardValue + ":" + pos);
		
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(pos, game.getAllPlayers().get(playerPos).getDisplay());
		
		return returnString;
	}
}
