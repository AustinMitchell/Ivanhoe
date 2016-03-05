package rulesengine;

import java.util.ArrayList;
import java.util.Random;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.Type;

public class RulesEngine {
	public static final String NEW_COM = "$";
	
	
	public static void drawToken(GameState game) {
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
	
	
	
	
	public static String setInitialTurn(GameState game) {
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				game.setTurn(i);
			}
		}
		return("setTurn:" + game.getTurn());
	}
	
	
	public static String startGame(GameState game) {
		//drawToken(game);
		String startTournament = startTournament(game);
		return ("startGame");
	}
	
	public static String drawCard(GameState game) {
		game.getDrawDeck().draw(game.getAllPlayers().get(game.getTurn()).getHand());
		return("drawCard:" + game.getTurn());
	}
	
	public static boolean isTournamentOver(GameState game) {
		int playersInTournament = 0;
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).isInTournament()) {
				playersInTournament++;
			}
		}
		if(playersInTournament == 1) return true;
		else return false;
	}
	
	public static void endTournament(GameState game) {
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getStunDeck().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getShieldDeck().emptyDeck(game.getDiscardDeck());
		}
		game.setPrevTournamentColour(game.getTournamentColour());
		game.setTournamentColour(-1);
		game.setTournamentStarted(false);
		//int playerPos = game.getTurn();
		//String result = "endTournament:" + playerPos;
		//return result;
	}
	
	
	
	public static boolean endGame(GameState game) {
		return Validator.isGameOver(game);
	}
	
	public static String endTurn(GameState game, String withdrawState) {
		if(withdrawState.equalsIgnoreCase("true")) withdraw(game);
		if(!isTournamentOver(game)) {
			game.nextTurn();
		}
		else {
			game.nextTurn();
			endTournament(game);
		}
		return "endTurn:" + withdrawState;
	}
	
	public static void withdraw(GameState game) {
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).exitTournament();
		//return("withdraw:" + playerPos);
	}
	
	public static String startTournament(GameState game) {
		for(models.Player p: game.getAllPlayers()) {
			p.enterTournament();
		}
		
		game.incrementTournamentNumber();
		String result;
		if(game.getTournamentNumber() > 0) {
			result = ("startTournament:" + game.getTurn());
		}
		
		else
			result = ("startTournament:-1");
		return result;
		
	}
	
	public static String setColour(GameState game, String colour) {
		game.setTournamentStarted(true);
		game.setTournamentColour(Integer.valueOf(colour));
		
		return("setColour:"  + colour);
	}
	
	public static boolean remainInTournament(GameState game) {
		Player player = game.getAllPlayers().get(game.getTurn());
		for(Player p:game.getAllPlayers()) {
			
			if(p != player) {
				if(player.getDisplayValue(game.getTournamentColour()) <= 
						p.getDisplayValue(game.getTournamentColour())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static String playValueCard(GameState game, int cardPos) {
		String returnString;
		Card card = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos);
		
		int playerPos = game.getTurn();
		int cardType = card.getCardType();
		int cardValue = card.getCardValue();
		returnString = ("card:" + cardPos);
		
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getAllPlayers().get(playerPos).getDisplay());
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) { //TODO: CLEAN UP THIS SHIT LIKE WE DID IN PLAY VALUE CARDS
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("unhorseCard:" + cardPos + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("changeWeaponCard:" + cardPos + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = ("dropWeaponCard:" + cardPos);
		game.setTournamentColour(Type.GREEN);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
}
