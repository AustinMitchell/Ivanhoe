package rulesengine;

import java.util.ArrayList;
import java.util.Random;
import models.Card;
import models.GameState;
import rulesengine.Type;

public class RulesEngine {
	
	
	public void drawToken(GameState game) {
		ArrayList tokens = new ArrayList();
		tokens.add(Type.PURPLE);
		tokens.add(Type.RED);
		tokens.add(Type.BLUE);
		tokens.add(Type.YELLOW);
		tokens.add(Type.GREEN);
		
		while(tokens.size() > game.getAllPlayers().size()) {
			tokens.remove(tokens.size()-1);
		}
		Random rand = new Random();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			int randomToken = rand.nextInt((tokens.size()));
			game.getAllPlayers().get(i).setToken(Integer.valueOf(tokens.get(randomToken).toString()), true);
			System.out.println("Random Token Number is: " + randomToken);
			tokens.remove(randomToken);
		}
	}
	
	public String playValueCard(GameState game, int cardPos) {
		String returnString;
		
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + playerPos + ":" + cardType + ":" + cardValue + ":" + cardPos);
		
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getAllPlayers().get(playerPos).getDisplay());
		
		return returnString;
	}
	
	
	public String unhorse(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + playerPos + ":" + cardType + ":" + cardValue + ":" + cardPos);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + playerPos + ":" + cardType + ":" + cardValue + ":" + cardPos);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public String dropWeapon(GameState game, int cardPos) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("card:" + playerPos + ":" + cardType + ":" + cardValue + ":" + cardPos);
		game.setTournamentColour(Type.GREEN);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
}
