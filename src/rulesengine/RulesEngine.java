package rulesengine;

import java.util.ArrayList;
import java.util.Random;

import models.Card;
import models.Deck;
import models.GameState;
import models.Player;
import rulesengine.Type;

public class RulesEngine {
	public static final String NEW_COM = "~";
	
	
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
		startTournament(game);
		return ("startGame");
	}
	
	public static String drawCard(GameState game) {
		String result = "";
		Deck drawDeck = game.getDrawDeck();
		if(drawDeck.deckSize() == 0) {
			game.renewDrawDeck();
			result += "renewDeck";
			for (int i = 0; i < game.getDrawDeck().deckSize(); i++) {
				int type = game.getDrawDeck().getCard(i).getCardType();
				int value = game.getDrawDeck().getCard(i).getCardValue();
				result += ":" + type + ":" + value;
			}
			result += NEW_COM;
		}
		
		game.getDrawDeck().draw(game.getAllPlayers().get(game.getTurn()).getHand());
		
		return result + "drawCard:" + game.getTurn();
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
	
	public static String endTournament(GameState game) { 
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getStunDeck().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getShieldDeck().emptyDeck(game.getDiscardDeck());
		}
		game.setPrevTournamentColour(game.getTournamentColour());
		int winningColour = game.getTournamentColour();
		String result = "endTournament:" + winningColour;
		game.setTournamentColour(-1);
		game.setTournamentStarted(false);
		//int playerPos = game.getTurn();
		if(endGame(game)) {
			result += NEW_COM + "endGame";
		}
		return result;
	}
	
	
	
	public static boolean endGame(GameState game) {
		return Validator.isGameOver(game);
	}
	
	public static String endTurn(GameState game, String withdrawState) {
		String result = "";
		if(withdrawState.equalsIgnoreCase("true")) withdraw(game);
		if(!isTournamentOver(game) || !game.hasTournamentStarted()) {
			game.nextTurn();
			result = "endTurn:" + withdrawState; 
		}
		else {
			game.nextTurn();
			String endTournamentCommand = endTournament(game);
			result = "endTurn:" + withdrawState + NEW_COM + endTournamentCommand;
		}
		return result;
	}
	
	public static void withdraw(GameState game) {
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).exitTournament();
		game.getAllPlayers().get(game.getTurn()).getDisplay().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getStunDeck().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getShieldDeck().emptyDeck(game.getDiscardDeck());
		//return("withdraw:" + playerPos);
	}
	
	public static String startTournament(GameState game) {
		for(models.Player p: game.getAllPlayers()) {
			p.enterTournament();
		}
		
		game.incrementTournamentNumber();
		String result;
		result = ("startTournament:" + game.getTurn());
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
		
		int playerPos = game.getTurn();
		returnString = ("card:" + cardPos);
		
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getAllPlayers().get(playerPos).getDisplay());
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = ("card:" + cardPos + ":" + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = ("card:" + cardPos + ":" + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = ("card:" + cardPos);
		game.setTournamentColour(Type.GREEN);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	
	//Process break lance card
	public static String breakLance(GameState game, int cardPos, int targetPos) {
		String returnString = ("card:" + cardPos + targetPos);
		int turn = game.getTurn();
		//Discard the Break Lance card
		
		game.getHand(turn).moveCardTo(cardPos, game.getDiscardDeck());
		//game.getAllPlayers().get(turn).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		
		//Discard all purple cards from the target's display while making sure at least one card remains
		for(int i = 0; i < game.getDisplay(targetPos).deckSize(); i++) {
			int type = game.getDisplay(targetPos).getCard(i).getCardType();
			if(type == Type.PURPLE && game.getDisplay(targetPos).deckSize() > 1) {
				game.getDisplay(targetPos).moveCardTo(i, game.getDiscardDeck());
			}
		}
		return returnString;
	}
	
	//Process Riposte card
	public static String riposte(GameState game, int cardPos, int targetPos) {
		String returnString = ("card:" + cardPos + targetPos);
		int turn = game.getTurn();
		//Discard the riposte card
		game.getHand(turn).moveCardTo(cardPos, game.getDiscardDeck());
		/*
		 * remove the last card in a target's display and add to player's display
		 * perform the procedure ONLY if the target doesn't end up with an empty display
		 */
		int cardToRemove = game.getDisplay(targetPos).deckSize()-1;
		if(cardToRemove > 0) {
			game.getDisplay(targetPos).moveCardTo(cardToRemove, game.getDisplay(turn));
		}
		return returnString;
	}


	
	//Process Dodge
	public static String dodge(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = "card:" + cardPos + targetPos + targetCardPos;
		int turn = game.getTurn();
		//Discard the riposte card
		game.getHand(turn).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Discard the target's card only if the display's size is bigger than 1
		if(game.getDisplay(targetPos).deckSize() > 1) {
			game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDiscardDeck());
		}
		return returnString;
	}
	
	
	
	public static String unimplementedActionCard(GameState game, int cardPos) {
		String result = ("card:" + cardPos);
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return result;
	}
}
