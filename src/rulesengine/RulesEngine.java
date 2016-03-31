package rulesengine;

import java.util.ArrayList;
import java.util.Random;

import network.Flag;
import models.Card;
import models.Deck;
import models.GameState;
import models.Player;
import rulesengine.Type;

public class RulesEngine {	
	
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
		return(Flag.SET_TURN + ":" + game.getTurn());
	}
	
	
	public static String startGame(GameState game) {
		//drawToken(game);
		startTournament(game);
		return Flag.START_GAME;
	}
	
	public static String drawCard(GameState game) {
		String result = "";
		Deck drawDeck = game.getDrawDeck();
		if(drawDeck.deckSize() == 0) {
			game.renewDrawDeck();
			result += Flag.RENEW_DECK;
			for (int i = 0; i < game.getDrawDeck().deckSize(); i++) {
				int type = game.getDrawDeck().getCard(i).getCardType();
				int value = game.getDrawDeck().getCard(i).getCardValue();
				result += ":" + type + ":" + value;
			}
			result += Flag.NEW_COM;
		}
		
		game.getDrawDeck().draw(game.getAllPlayers().get(game.getTurn()).getHand());
		
		return result + Flag.DRAW_CARD + ":" + game.getTurn();
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
		String result = Flag.END_TOURNAMENT + ":" + winningColour;
		game.setTournamentColour(-1);
		game.setTournamentStarted(false);
		//int playerPos = game.getTurn();
		if(endGame(game)) {
			result += Flag.NEW_COM + Flag.END_GAME;
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
			result = Flag.END_TURN + ":" + withdrawState; 
		}
		else {
			game.nextTurn();
			String endTournamentCommand = endTournament(game);
			result = Flag.END_TURN + ":" + withdrawState + Flag.NEW_COM + endTournamentCommand;
		}
		return result;
	}
	
	public static void withdraw(GameState game) {
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).exitTournament();
		game.getAllPlayers().get(game.getTurn()).getDisplay().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getStunDeck().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getShieldDeck().emptyDeck(game.getDiscardDeck());
	}
	
	public static String startTournament(GameState game) {
		for(models.Player p: game.getAllPlayers()) {
			p.enterTournament();
		}
		
		game.incrementTournamentNumber();
		String result;
		result = (Flag.START_TOURNAMENT + ":" + game.getTurn());
		return result;
		
	}
	
	public static String setColour(GameState game, String colour) {
		game.setTournamentStarted(true);
		game.setTournamentColour(Integer.valueOf(colour));
		
		return(Flag.SET_COLOUR + ":"  + colour);
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
		returnString = (Flag.CARD + ":" + cardPos);
		
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getAllPlayers().get(playerPos).getDisplay());
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) { //TODO: CLEAN UP THIS SHIT LIKE WE DID IN PLAY VALUE CARDS
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + colour);
		game.setTournamentColour(colour);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		String returnString;
		int playerPos = game.getTurn();
		int cardType = game.getAllPlayers().get(playerPos).getHand().getCard(cardPos).getCardType();
		int cardValue = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos).getCardValue();
		returnString = (Flag.ACTION_CARD + ":" + cardPos);
		game.setTournamentColour(Type.GREEN);
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return returnString;
	}
	
	public static String ivanhoe(GameState game, int actionCardPos, int ivanhoePlayer) {
		String result = (Flag.IVANHOE_PLAYED + ":" + actionCardPos + ":" + ivanhoePlayer);
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(actionCardPos, game.getDiscardDeck());
		
		int ivanhoeCardPos = -1;
		for (Card c: game.getAllPlayers().get(ivanhoePlayer).getHand().getAllCards()) {
			ivanhoeCardPos++;
			if (c.getCardType() == Type.ACTION && c.getCardValue() == Card.IVANHOE) {
				break;
			}
		}
		game.getAllPlayers().get(ivanhoePlayer).getHand().moveCardTo(ivanhoeCardPos, game.getDiscardDeck());
		
		return result;
	}
	
	public static String unimplementedActionCard(GameState game, int cardPos) {
		String result = (Flag.ACTION_CARD + ":" + cardPos);
		int playerPos = game.getTurn();
		game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
		return result;
	}
}
