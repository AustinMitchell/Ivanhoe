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
	 * Function to validate break lance. there are no restrictions on this card for now.
	 * It's function in the rules engine does not fully empty the display 
	 */
	public static ArrayList<Boolean> validateBreakLance(GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			if(i == game.getTurn()) {
				availableTargets.add(false);
			}
			else {
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
	 * Function to validate riposte. there are no restrictions on this card for now.
	 * It's function in the rules engine does not fully empty the display 
	 */
	public static ArrayList<Boolean> validateRiposte(GameState game) {
		ArrayList<Boolean> availableTargets = new ArrayList<Boolean>();
		for(int i = 0; i < game.getAllPlayers().size(); i++) {
			
			if(i != game.getTurn() && game.getDisplay(i).deckSize() > 1) {
				availableTargets.add(true);
			}
			else {
				availableTargets.add(false);
			}
		}
		return availableTargets;
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
