package rulesengine;
import rulesengine.*;

import java.util.ArrayList;

import models.*;

public class Validator {
	
	public static boolean canStartTournament(GameState game) {
		Deck hand = game.getAllPlayers().get(game.getTurn()).getHand();
		for(int i = 0; i < hand.deckSize(); i++) {
			int type = hand.getCard(i).getCardType();
			if (type >= 1 && type <= 5) {
				return true;
			}
			else if (type == Type.PURPLE && game.getPrevTournamentColour() != Type.PURPLE) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean[] cardsAbleToStart(GameState game) {
		Deck hand = game.getAllPlayers().get(game.getTurn()).getHand();
		boolean[] playableCards = new boolean[hand.deckSize()];
		
		for(int i = 0; i < hand.deckSize(); i++) {
			if(hand.getCard(i).getCardType() == game.getTournamentColour() || 
					hand.getCard(i).getCardType() == Type.WHITE) {
				playableCards[i] = true;
			}
			else {
				playableCards[i] = false;
			}
		}
		return playableCards;
	}
	
	
	
	public static boolean[] cardsAbleToPlay(GameState game) {
		Deck hand = game.getAllPlayers().get(game.getTurn()).getHand();
		Card card;
		boolean displayHasMaiden = game.getAllPlayers().get(game.getTurn()).displayHasMaiden();
		boolean[] playableCards = new boolean[hand.deckSize()];
		
		for(int i = 0; i < hand.deckSize(); i++) {
			card = hand.getCard(i);
			
			if(card.getCardType() != Type.ACTION) {
				playableCards[i] = validateValueCard(game, card, displayHasMaiden);
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.UNHORSE) {
				playableCards[i] = validateUnhorse(game);
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.CHANGE_WEAPON) {
				boolean[] availableCards = validateChangeWeapon(game);
				for(int j = 0; j < availableCards.length; j++) {
					if(availableCards[j]) {
						playableCards[i] = true;
						break;
					}
					else {
						playableCards[i] = false;
					}
				}
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.DROP_WEAPON) {
				playableCards[i] = validateDropWeapon(game);
			}
			
			else if(card.getCardType() == Type.ACTION && card.getCardValue() == Card.BREAK_LANCE) {
				ArrayList<Boolean> availableTargets = validateBreakLance(game);
				for(int j = 0; j < availableTargets.size(); j++) {
					if(availableTargets.get(j)) {
						playableCards[i] = true;
						break;
					}
					else {
						playableCards[i] = false;
					}
				}
			}
			
			else if(card.getCardType() == Type.ACTION && card.getCardValue() == Card.RIPOSTE) {
				ArrayList<Boolean> availableTargets = validateRiposte(game);
				for(int j = 0; j < availableTargets.size(); j++) {
					if(availableTargets.get(j)) {
						playableCards[i] = true;
						break;
					}
					else {
						playableCards[i] = false;
					}
				}
			}
			
			else if(card.getCardType() == Type.ACTION && card.getCardValue() == Card.DODGE) {
				ArrayList<Boolean> availableTargets = validateDodge(game);
				for(int j = 0; j < availableTargets.size(); j++) {
					if(availableTargets.get(j)) {
						playableCards[i] = true;
						break;
					}
					else {
						playableCards[i] = false;
					}
				}
			}
			
			else if(card.getCardType() == Type.ACTION && card.getCardValue() == Card.RETREAT) {
				playableCards[i] = validateRetreat(game);
			}
			
			else if(card.getCardType() == Type.ACTION && card.getCardValue() == Card.KNOCKDOWN) {
				ArrayList<Boolean> availableTargets = validateKnockdown(game);
				for(int j = 0; j < availableTargets.size(); j++) {
					if(availableTargets.get(j)) {
						playableCards[i] = true;
						break;
					}
					else {
						playableCards[i] = false;
					}
				}
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.OUTMANEUVER) {
				ArrayList<Boolean> availableTargets = validateOutmaneuver(game);
				if(availableTargets.size() > 0) {
					playableCards[i] = true;
				}
				else {
					playableCards[i] = false;
				}
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.CHARGE) {
				ArrayList<Boolean> availableTargets = validateCharge(game);
				if(availableTargets.size() > 0) {
					playableCards[i] = true;
				}
				else {
					playableCards[i] = false;
				}
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.COUNTERCHARGE) {
				ArrayList<Boolean> availableTargets = validateCountercharge(game);
				if(availableTargets.size() > 0) {
					playableCards[i] = true;
				}
				else {
					playableCards[i] = false;
				}
			}
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.DISGRACE) {
				ArrayList<Boolean> availableTargets = validateDisgrace(game);
				if(availableTargets.size() > 0) {
					playableCards[i] = true;
				}
				else {
					playableCards[i] = false;
				}
			}
			
			
			
			else if (card.getCardType() == Type.ACTION && card.getCardValue() == Card.SHIELD) {
				playableCards[i] = validateShield(game);
			}
			
			
			else {
				playableCards[i] = true;
			}
		}
		return playableCards;
	}
	
	public static boolean validateValueCard(GameState game, Card card, boolean displayHasMaiden) {
		if(card.getCardType() == game.getTournamentColour()) {
			return true;
		}
		
		else if (card.getCardType() == Type.WHITE && !card.isMaiden()) {
			return true;
		}
		
		else if (card.getCardType() == Type.WHITE && card.isMaiden()) {
			if(displayHasMaiden) {
				return false;
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	public static boolean validateUnhorse(GameState game) {
		
		if(game.getTournamentColour() == Type.PURPLE) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public static boolean[] validateChangeWeapon(GameState game) {
		boolean[] validColours = new boolean[5];
		validColours[Type.PURPLE] = false;
		validColours[Type.RED] = false;
		validColours[Type.YELLOW] = false;
		validColours[Type.BLUE] = false;
		validColours[Type.GREEN] = false;
		
		if(game.getTournamentColour() == Type.RED) {
			validColours[Type.YELLOW] = true;
			validColours[Type.BLUE] = true;
		}
		else if(game.getTournamentColour() == Type.YELLOW) {
			validColours[Type.RED] = true;
			validColours[Type.BLUE] = true;
		}
		else if(game.getTournamentColour() == Type.BLUE) {
			validColours[Type.RED] = true;
			validColours[Type.YELLOW] = true;
		}
		
		return validColours;
	}
	
	public static boolean validateDropWeapon(GameState game) {
		if (game.getTournamentColour() != Type.PURPLE && game.getTournamentColour() != Type.GREEN) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * Function to validate break lance.
	 */
	public static ArrayList<Boolean> validateBreakLance(GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if (game.getPlayer(i).hasShield()) {
				availableTargets.add(false);
			}
			else if(i == game.getTurn()) {
				availableTargets.add(false);
			}
			else{
				boolean hasPurple = false;
				for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
					if(game.getDisplay(i).getCard(j).getCardType() == Type.PURPLE && game.getDisplay(i).deckSize() > 1) {
						hasPurple = true;
						break;
					}
				}
				availableTargets.add(hasPurple);
			}
		}
		return availableTargets;
	}

	
	/*
	 * Function to validate riposte.
	 */
	public static ArrayList<Boolean> validateRiposte(GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			
			if(i != game.getTurn() && game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				availableTargets.add(true);
			}
			else {
				availableTargets.add(false);
			}
		}
		return availableTargets;
	}
	
	/*
	 * Function to validate Dodge. 
	 */
	public static ArrayList<Boolean> validateDodge (GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			
			if(i != game.getTurn() && game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				availableTargets.add(true);
			}
			else {
				availableTargets.add(false);
			}
		}
		return availableTargets;
	}
	
	/*
	 * Function to validate Retreat
	 */
	public static boolean validateRetreat(GameState game) {
		boolean valid = false;
		if (game.getDisplay(game.getTurn()).deckSize() > 1) {
			valid = true;
		}
		return valid;
	}
	
	/*
	 * Function to validate Knockdown. 
	 */
	public static ArrayList<Boolean> validateKnockdown (GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			
			if(i != game.getTurn() && game.getHand(i).deckSize() > 0 && !game.getPlayer(i).hasShield()) {
				availableTargets.add(true);
			}
			else {
				availableTargets.add(false);
			}
		}
		return availableTargets;
	}
	
	/*
	 * Function to validate outmaneuver
	 */
	public static ArrayList<Boolean> validateOutmaneuver(GameState game) {
		ArrayList<Boolean> validTargets = new ArrayList<Boolean>();
		int playerPos = game.getTurn();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(i != playerPos && game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				validTargets.add(true);
			}
		}
		return validTargets;
	}
	
	
	/*
	 * Function to validate charge
	 */
	public static ArrayList<Boolean> validateCharge(GameState game) {
		ArrayList<Boolean> validTargets = new ArrayList<Boolean>();
		int playerPos = game.getTurn();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				validTargets.add(true);
			}
		}
		return validTargets;
	}
	
	
	/*
	 * Function to validate countercharge
	 */
	public static ArrayList<Boolean> validateCountercharge(GameState game) {
		ArrayList<Boolean> validTargets = new ArrayList<Boolean>();
		int playerPos = game.getTurn();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				validTargets.add(true);
			}
		}
		return validTargets;
	}
	
	
	/*
	 * Function to validate disgrace
	 */
	public static ArrayList<Boolean> validateDisgrace(GameState game) {
		ArrayList<Boolean> validTargets = new ArrayList<Boolean>();
		//int playerPos = game.getTurn();
		for (int i = 0; i < game.getAllPlayers().size(); i++) {
			if(game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				for(int j = 0; j < game.getDisplay(i).deckSize(); j++) {
					if(game.getDisplay(i).getCard(j).getCardType() == Type.WHITE) {
						validTargets.add(true);
						break;
					}
				}
			}
		}
		return validTargets;
	}
	
	
	/*
	 * Function to validate Shield
	 */
	public static boolean validateShield(GameState game) {
		
		return true;
	}

	
	public static boolean isGameOver(GameState game) {
		for(Player p: game.getAllPlayers()) {
			if(p.hasWon()) {
				game.setGameOver(true);
				return true;
			}
			else {
				game.setGameOver(false);
			}
		}
		return false;
	}

}
