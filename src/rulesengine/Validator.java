package rulesengine;
import rulesengine.*;
import models.*;

public class Validator {
	
	public static boolean canStartTournament(GameState game) {
		Deck hand = game.getAllPlayers().get(game.getTurn()).getHand();
		for(int i = 0; i < hand.deckSize(); i++) {
			int type = hand.getCard(i).getCardType();
			if (type >= 1 && type <= 5) {
				return true;
			}
			else if (type == 0 && game.getPrevTournamentColour() != 0) {
				return true;
			}
		}
		return false;
	}
	
	
	public boolean[] cardsAbleToStart(GameState game) {
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
	
	
	public boolean[] cardsAbleToPlay(GameState game) {
		Deck hand = game.getAllPlayers().get(game.getTurn()).getHand();
		int tournamentColour = game.getTournamentColour();
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
	
	
	public static boolean isGameOver(GameState game) {
		for(Player p: game.getAllPlayers()) {
			if(p.hasWon()) {
				game.setGameOver(true);
				return true;
			}
			else {
				game.setGameOver(false);
				return false;
			}
		}
		return false;
	}
	
	
	public static boolean validateValueCard (GameState game, int cardPos) {
		Card card = game.getAllPlayers().get(game.getTurn()).getHand().getCard(cardPos);
		int colour = card.getCardType();
		Player player = game.getAllPlayers().get(game.getTurn());
		if(colour == Type.WHITE) {
			if(card.isMaiden() && player.displayHasMaiden()) {
				return false;
			}
			return true;
		}
		else if(colour == game.getTournamentColour()) {
			return true;
		}
		return false;
	}
	
	
	//Picking the colour can be done in the update engine. Restrict the GUI to only show RED, BLUE, YELLOW
	public static boolean validateUnhorse(GameState game) {
		if(game.getTournamentColour() == Type.PURPLE) {
			return true;
		}
		return false;
	}
	

	//Picking the colour can be done in the update engine. Restrict the GUI to only show RED, BLUE, YELLOW
	//and grey out which ever colour that is already the tournament colour 
	public static boolean validateChangeWeapon(GameState game) {
		if(game.getTournamentColour() == Type.PURPLE || game.getTournamentColour() == Type.GREEN) {
			return false;
		}
		return true;
	}
	
	
	public static boolean validateDropWeapon(GameState game) {
		if(game.getTournamentColour() == Type.PURPLE || game.getTournamentColour() == Type.GREEN) {
			return false;
		}
		return true;
	}
	
	
	
	

}
