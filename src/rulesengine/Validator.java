package rulesengine;
import rulesengine.*;
import models.*;

public class Validator {
	
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
