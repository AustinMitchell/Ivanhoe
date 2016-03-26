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
		int playerPos = game.getTurn();
		//Discard the Break Lance card
		
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Discard all purple cards from the target's display while making sure at least one card remains
		for(int i = 0; i < game.getDisplay(targetPos).deckSize(); i++) {
			int type = game.getDisplay(targetPos).getCard(i).getCardType();
			if(type == Type.PURPLE && game.getDisplay(targetPos).deckSize() > 1 && !game.getPlayer(targetPos).hasShield()) {
				game.getDisplay(targetPos).moveCardTo(i, game.getDiscardDeck());
			}
		}
		return returnString;
	}
	
	//Process Riposte card
	public static String riposte(GameState game, int cardPos, int targetPos) {
		String returnString = ("card:" + cardPos + targetPos);
		int playerPos = game.getTurn();
		//Discard the riposte card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		/*
		 * remove the last card in a target's display and add to player's display
		 * perform the procedure ONLY if the target doesn't end up with an empty display
		 */
		int cardToRemove = game.getDisplay(targetPos).deckSize()-1;
		if(cardToRemove > 0 && !game.getPlayer(targetPos).hasShield()) {
			game.getDisplay(targetPos).moveCardTo(cardToRemove, game.getDisplay(playerPos));
		}
		return returnString;
	}


	
	//Process Dodge
	public static String dodge(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = "card:" + cardPos + targetPos + targetCardPos;
		int playerPos = game.getTurn();
		//Discard the dodge card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Discard the target's card only if the display's size is bigger than 1 and if the target doesn't have a shield
		if(game.getDisplay(targetPos).deckSize() > 1 && !game.getPlayer(targetPos).hasShield()) {
			game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDiscardDeck());
		}
		return returnString;
	}
	
	
	//Process Retreat
	public static String retreat(GameState game, int cardPos, int targetCardPos) {
		String returnString = "card:" + cardPos + ":" + targetCardPos;
		int playerPos = game.getTurn();
		//Discard the retreat card
		game.getHand(game.getTurn()).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Move card from player's display to their hand
		if(game.getDisplay(game.getTurn()).deckSize() > 1) {
			game.getDisplay(game.getTurn()).moveCardTo(targetCardPos, game.getHand(playerPos));
		}
		return returnString;
	}
	
	//Process KnockDown
	public static String knockdown(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = "card:" + cardPos + targetPos + targetCardPos;
		int playerPos = game.getTurn();
		//Discard the knockdown card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Take the target's card and add it to player's hand
		if (game.getHand(targetPos).deckSize() > 0) {
			game.getHand(targetPos).moveCardTo(targetCardPos, game.getHand(playerPos));
		}
		return returnString;
	}
	
	//Process Outmaneuver
	public static String outmaneuver(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		int playerPos = game.getTurn();
		//Discard the outmaneuver card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//loop through all players
		for (int i = 0; i < game.getAllPlayers().size(); i++) {
			//discard the last card in the display if it is not the player's display
			// and if the display has more than 1 card
			// and if the target does not have a shield
			if(i != playerPos && game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				int displaySize = game.getDisplay(i).deckSize();
				game.getDisplay(i).moveCardTo(displaySize-1, game.getDiscardDeck());
			}
		}
		return returnString;
	}
	
	//Process Charge
	public static String charge(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		int playerPos = game.getTurn();
		//Discard the charge card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//variable to keep track of the lowest value card
		int lowest = 100;
		
		//Loop through all cards in all displays to identify the lowest value card
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
				if(lowest > game.getDisplay(i).getCard(j).getCardValue()) {
					lowest =  game.getDisplay(i).getCard(j).getCardValue();
				}
			}
		}
		
		/*loop through opponents' displays and discard the identified value cards
		 * provided it isn't the player's display and the opponent has no shield
		 */
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(!game.getPlayer(i).hasShield() && game.getDisplay(i).deckSize() > 1) {
				for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
					if(game.getDisplay(i).getCard(j).getCardValue() == lowest) {
						game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Counterharge
	public static String countercharge(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		int playerPos = game.getTurn();
		//Discard the countercharge card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		//variable to keep track of the lowest value card
		int highest = 0;
		
		//Loop through all cards in all displays to identify the lowest value card
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
				if(highest < game.getDisplay(i).getCard(j).getCardValue()) {
					highest =  game.getDisplay(i).getCard(j).getCardValue();
				}
			}
		}
		
		/*loop through opponents' displays and discard the identified value cards
		 * provided it isn't the player's display and the opponent has no shield
		 */
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(!game.getPlayer(i).hasShield() && game.getDisplay(i).deckSize() > 1) {
				for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
					if(game.getDisplay(i).getCard(j).getCardValue() == highest) {
						game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Disgrace
	public static String disgrace(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		int playerPos = game.getTurn();
		//Discard the disgrace card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//discard all supporter cards given it isn't the player and the target doesn't have a shield
		for (int i = 0; i < game.getAllPlayers().size(); i++) {
			if(!game.getPlayer(i).hasShield() && game.getDisplay(i).deckSize() > 1) {
				for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
					if(game.getDisplay(i).getCard(j).getCardType() == Type.WHITE) {
						game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Outwit
	public static String outwit(GameState game, int cardPos, String playerDeck, int playerCardPos, int targetPos, String targetDeck, int targetCardPos) {
		String returnString = "card:" + cardPos + ":" + playerDeck + ":" + playerCardPos + ":" + targetPos + ":" + targetDeck + ":" + targetCardPos;
		int playerPos = game.getTurn();
		Card playerCard;
		int playerCardType;
		int playerCardValue;
		
		//Discard the outwit card
		game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
		
		//Check if the player a card in their display to swap
		if (playerDeck.equalsIgnoreCase("display")) {
			playerCard = game.getDisplay(playerPos).getCard(playerCardPos);
			playerCardType = playerCard.getCardType();
			playerCardValue = playerCard.getCardValue();
			
			if (targetDeck.equalsIgnoreCase("display")) {
				//move the target's card to player's display
				game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDisplay(playerPos));
				//loop to find the player's card and move it to target's display
				for(int i = 0; i < game.getDisplay(playerPos).deckSize(); i++) {
					if(game.getDisplay(playerPos).getCard(i).getCardType() == playerCardType &&
							game.getDisplay(playerPos).getCard(i).getCardValue() == playerCardValue) {
						game.getDisplay(playerPos).moveCardTo(i, game.getDisplay(targetPos));
						break;
					}
				}
			} else {
				//player gets the target's shield and give's the target their display card
				game.getShield(targetPos).moveCardTo(targetCardPos, game.getShield(playerPos));
				game.getDisplay(playerPos).moveCardTo(playerCardPos, game.getDisplay(targetPos));
			}
			
		} else {
			if (targetDeck.equalsIgnoreCase("display")) {
				//move the target's card to player's display and give player's stun to target
				game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDisplay(playerPos));
				game.getStun(playerPos).moveCardTo(playerCardPos, game.getStun(targetPos));
			} else {
				//player gets the target's shield and gives their stun to the target
				game.getShield(targetPos).moveCardTo(targetCardPos, game.getShield(playerPos));
				game.getStun(playerPos).moveCardTo(playerCardPos, game.getStun(targetPos));
			}
		}
		return returnString;
	}
	
	//Process Shield
	public static String shield(GameState game, int cardPos) {
		String returnString = "card:" + cardPos;
		int playerPos = game.getTurn();
		game.getHand(playerPos).moveCardTo(cardPos, game.getShield(playerPos));		
		return returnString;
	}
	
	//Process Stun
	public static String stun() {
		
		return "";
	}
	
	public static String unimplementedActionCard(GameState game, int cardPos) {
		String result = ("card:" + cardPos);
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return result;
	}
}
