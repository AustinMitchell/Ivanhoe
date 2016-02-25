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
			game.getAllPlayers().get(i).setDrawnToken(Integer.valueOf(tokens.get(randomToken).toString()));
			//game.getAllPlayers().get(i).setToken(Integer.valueOf(tokens.get(randomToken).toString()), true);
			System.out.println("Random Token Number is: " + randomToken);
			tokens.remove(randomToken);
		}
		setInitialTurn(game);
	}
	
	public void setInitialTurn(GameState game) {
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				game.setTurn(i);
			}
		}
	}
	
	public void startGame(GameState game) {
		drawToken(game);
		startTournament(game);
	}
	
	
	
	public boolean isTournamentOver(GameState game) {
		int playersInTournament = 0;
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).isInTournament()) {
				playersInTournament++;
			}
		}
		if(playersInTournament == 1) return true;
		else return false;
	}
	
	public String endTournament(GameState game) {
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
		}
		int playerPos = game.getTurn();
		String result = "endTournament:" + playerPos;
		return result;
	}
	
	
	/*public void endTournament(GameState game) {
		if(isTournamentOver(game)) {
			for(int i = 0; i< game.getAllPlayers().size(); i++) {
				if(game.getAllPlayers().get(i).isInTournament()) {
					game.setTurn(i);
					
					if(game.getTournamentColour() == Type.PURPLE) {
						//IMPLEMENT FEATURE FOR CHOOSING A TOKEN
					} else {
						game.getAllPlayers().get(i).setToken(game.getTournamentColour(), true);
						if(!game.isGameOver()) {
							for(int j = 0; j < game.getAllPlayers().size(); j++) {
								game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
								game.getAllPlayers().get(j).enterTournament();
							}
						}
					}
					break;
				}
			}
		}
	}*/
	
	
	public String endTurn(GameState game, String withdrawState) {
		if(withdrawState.equalsIgnoreCase("true")) withdraw(game);
		if(!isTournamentOver(game)) {
			game.nextTurn();
			return "endTurn:" + game.getTurn();
		}
		else{
			game.incrementTournamentNumber();
			return endTournament(game);
		}
	}
	
	public void withdraw(GameState game) {
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).exitTournament();
	}
	
	public void startTournament(GameState game) {
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).enterTournament();
		}
	}
	
	public void setColour(GameState game, String colour) {
		if(colour.equals("-1")) endTurn(game, "false");
		game.setTournamentColour(Integer.valueOf(colour));
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
