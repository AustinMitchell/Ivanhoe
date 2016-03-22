package rulesengine;

import java.util.ArrayList;
import java.util.Random;

import models.Card;
import models.GameState;
import rulesengine.Type;

public class UpdateEngine {

	/*public void drawToken(GameState game) {
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
	}*/
	
	/*public void setInitialTurn(GameState game) {
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).getDrawnToken() == Type.PURPLE) {
				game.setTurn(i);
			}
		}
	}*/
	
	/*public void startGame(GameState game) {
		//drawToken(game);
		startTournament(game);
	}*/
	
	
	
	/*public boolean isTournamentOver(GameState game) {
		int playersInTournament = 0;
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getAllPlayers().get(i).isInTournament()) {
				playersInTournament++;
			}
		}
		if(playersInTournament == 1) return true;
		else return false;
	}*/
	
	
	
	//Might need to comment out this function later on
	public static String endTournament(GameState game) {
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
		}
		int playerPos = game.getTurn();
		String result = "endTournament:" + playerPos;
		return result;
	}
	
	
	/*public boolean endGame(GameState game) {
		return game.isGameOver();
	}*/
	
	
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
	
	
	
	
	public static String endTurn(GameState game, String withdrawState) {
		return ("endTurn:" + withdrawState);
	}
	
	
	/*public void withdraw(GameState game) {
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).exitTournament();
	}*/
	
	/*public void startTournament(GameState game) {
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).enterTournament();
		}
		game.incrementTournamentNumber();
	}*/
	
	/*public void setColour(GameState game, String colour) {
		if(colour.equals("-1")) endTurn(game, "false");
		else game.setTournamentColour(Integer.valueOf(colour));
	}*/
	
	/*public boolean remainInTournament(GameState game) {
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getTurn() != i) {
				if(game.getAllPlayers().get(game.getTurn()).getDisplayValue(game.getTournamentColour()) <= 
				game.getAllPlayers().get(i).getDisplayValue(game.getTournamentColour())) {
					return false;
				}
			}
		}
		return true;
	}*/
	
	public static String playValueCard(GameState game, int cardPos) {
		String returnString;
		returnString = ("card:" + cardPos);
		
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) {
		String returnString;
		returnString = ("card:" + cardPos + ":" + colour);
		
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		returnString = ("card:"  + cardPos + ":" + colour);
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		String returnString;
		returnString = ("card:" + cardPos);
		return returnString;
	}
	
	public static String breakLance(GameState game, int cardPos, int targetPos) {
		String returnString = ("card:" + cardPos + ":" + targetPos);
		return returnString;
	}
	
	public static String riposte(GameState game, int cardPos, int targetPos) {
		String returnString = ("card:" + cardPos + ":" + targetPos);
		return returnString;
	}
	
	public static String unimplementedActionCard(GameState game, int cardPos) {
		String result = ("card:" + cardPos);
		return result;
	}
	
	public static String dodge(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = "card:" + cardPos + targetPos + targetCardPos;
		return returnString;
	}
	
	public static String retreat(GameState game, int cardPos, int targetCardPos) {
		String returnString = "card:" + cardPos + ":" + targetCardPos;
		return returnString;
	}
	
	public static String knockdown(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = "card:" + cardPos + targetPos + targetCardPos;
		return returnString;
	}
	
	public static String outmaneuver(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		return returnString;
	}

	//Process Charge
	public static String charge(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		return returnString;
	}
	
	//Process Counterharge
	public static String countercharge(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		return returnString;
	}
	
}
