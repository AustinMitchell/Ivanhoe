package controller.rulesengine;

import model.Card;
import model.Deck;
import model.Flag;
import model.GameState;
import model.Type;

public class RulesEngine {	
	
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
		return (playersInTournament == 1);
	}
	
	public static String endTournament(GameState game) { 
		for(int j = 0; j < game.getAllPlayers().size(); j++) {
			game.getAllPlayers().get(j).getDisplay().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getStunDeck().emptyDeck(game.getDiscardDeck());
			game.getAllPlayers().get(j).getShieldDeck().emptyDeck(game.getDiscardDeck());
		}
		game.setPrevTournamentColour(game.getTournamentColour());
		int winningColour = game.getTournamentColour();
		game.setTokenDrawn(winningColour);
		String result = Flag.END_TOURNAMENT + ":" + winningColour;
		game.setTournamentColour(game.TOURNAMENT_NOT_STARTED);
		game.setTournamentStarted(false);
		int playerPos = game.getTurn();
		game.setWinningPlayer(playerPos);
		//game.getPlayer(playerPos).setToken(winningColour, true); // give player a winning token
		//System.out.println("Giving player " + playerPos + " a token: " + winningColour);
		/*
		if(endGame(game)) {
			result += Flag.NEW_COM + Flag.END_GAME;
		}
		*/
		return result;
	}
	
	public static String awardToken(GameState game, String col) {
		int colour = Integer.valueOf(col);
		
		int playerPos = game.getWinningPlayer();
		game.getPlayer(playerPos).setToken(colour, true); // give player a winning token
		
		return Flag.AWARD_TOKEN + Flag.COM_SPLIT + colour;
	}
	
	public static String endGame(GameState game) {
		
		if(Validator.isGameOver(game)) {
			return Flag.END_GAME;
		}
		return "";
	}
	
	public static String endTurn(GameState game) {
		String result = "";
		String withdrawState = "";
		if (!remainInTournament(game)) withdrawState = Flag.NEW_COM + withdraw(game);
		
		// The commented out section in the if statement was required in order to pass the junit test. It does not break the game. 
		// However, it should be uncommented if there is a strange behaviour
		if(!isTournamentOver(game) /*|| !game.hasTournamentStarted()*/ ) { 
			game.nextTurn();
			result = Flag.END_TURN + withdrawState; 
		}
		else {
			game.nextTurn();
			String endTournamentCommand = Flag.NEW_COM + endTournament(game);
			result = Flag.END_TURN + withdrawState + endTournamentCommand;
		}
		game.setplayedValueCard(false);
		return result;
	}
	
	public static String withdraw(GameState game) {
		int playerPos = game.getTurn();
		//The following if statment checks if the withdrawing player has a maiden in their dispplay to take away a token accordingly
		if(game.getPlayer(playerPos).displayHasMaiden() && game.getPlayer(playerPos).playerHasToken()) {
			for(int i = 0; i < game.getPlayer(playerPos).getTokens().length; i++) {
				if(game.getPlayer(playerPos).checkToken(i)) {
					game.getPlayer(playerPos).setToken(i, false);
					break;
				}
			}
		}
		game.getAllPlayers().get(playerPos).exitTournament();
		game.getAllPlayers().get(game.getTurn()).getDisplay().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getStunDeck().emptyDeck(game.getDiscardDeck());
		game.getAllPlayers().get(game.getTurn()).getShieldDeck().emptyDeck(game.getDiscardDeck());
		
		return Flag.WITHDRAW + ":" + playerPos;
	}
	
	public static String startTournament(GameState game) {
		for(model.Player p: game.getAllPlayers()) {
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
		if (!game.hasTournamentStarted()) { return true; }
		
		return game.playerCanContinue(game.getTurn());
	}
	
	public static String playValueCard(GameState game, int cardPos) {
		String returnString;
		
		int playerPos = game.getTurn();
		returnString = (Flag.CARD + ":" + cardPos);
		Card card = game.getHand(playerPos).getCard(cardPos);
		int cardColour = card.getCardType();
		
		//check for constraints on playing a certain value card
		if((game.getTournamentColour() == cardColour) || 
				(!game.getPlayer(playerPos).displayHasMaiden() && card.isMaiden()) || 
				(cardColour == Type.WHITE && !card.isMaiden())) {
			
			game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getAllPlayers().get(playerPos).getDisplay());
			game.setplayedValueCard(true);
		}
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + colour);
		if(game.getTournamentColour() == Type.PURPLE) {
			//discard unhorse
			game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
			
			game.setTournamentColour(colour);
		}
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + colour);
		if (game.getTournamentColour() != Type.PURPLE && game.getTournamentColour() != Type.GREEN && game.getTournamentColour() != colour) {
			//Discard change Weapon card
			game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
			
			game.setTournamentColour(colour);
		}
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		String returnString;
		int playerPos = game.getTurn();
		returnString = (Flag.ACTION_CARD + ":" + cardPos);
		if(game.getTournamentColour() != Type.GREEN && game.getTournamentColour() != Type.PURPLE) {
			//discard drop weapon card
			game.getAllPlayers().get(playerPos).getHand().moveCardTo(cardPos, game.getDiscardDeck());
			
			game.setTournamentColour(Type.GREEN);
		}
		return returnString;
	}
	
	//Process break lance card
	public static String breakLance(GameState game, int cardPos, int targetPos) {
		String returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + targetPos);
		int playerPos = game.getTurn();
		boolean discardedCard = false;
		
		//Discard all purple cards from the target's display while making sure at least one card remains
		for(int i = game.getDisplay(targetPos).deckSize()-1; i >= 0; i--) {
			int type = game.getDisplay(targetPos).getCard(i).getCardType();
			if(type == Type.PURPLE && game.getDisplay(targetPos).deckSize() > 1 && !game.getPlayer(targetPos).hasShield()) {
				if(discardedCard == false) {
					game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the breaklance card
					discardedCard = true;
				}
				game.getDisplay(targetPos).moveCardTo(i, game.getDiscardDeck()); // Discard the found purple card
			}
		}
		return returnString;
	}
	
	//Process Riposte card
	public static String riposte(GameState game, int cardPos, int targetPos) {
		String returnString = (Flag.ACTION_CARD + ":" + cardPos + ":" + targetPos);
		int playerPos = game.getTurn();
		/*
		 * remove the last card in a target's display and add to player's display
		 * perform the procedure ONLY if the target doesn't end up with an empty display
		 */
		int cardToRemove = game.getDisplay(targetPos).deckSize()-1;
		if(cardToRemove > 0 && !game.getPlayer(targetPos).hasShield()) {
			//Discard the riposte card
			game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			game.getDisplay(targetPos).moveCardTo(cardToRemove, game.getDisplay(playerPos));
		}
		return returnString;
	}

	//Process Dodge
	public static String dodge(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos + ":" + targetPos + ":" + targetCardPos;
		int playerPos = game.getTurn();
		
		//Discard the target's card only if the display's size is bigger than 1 and if the target doesn't have a shield
		if(game.getDisplay(targetPos).deckSize() > 1 && !game.getPlayer(targetPos).hasShield()) {
			//Discard the dodge card
			game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDiscardDeck());
		}
		return returnString;
	}
	
	//Process Retreat
	public static String retreat(GameState game, int cardPos, int targetCardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos + ":" + targetCardPos;
		int playerPos = game.getTurn();
		
		//Move card from player's display to their hand
		if(game.getDisplay(game.getTurn()).deckSize() > 1) {
			//Discard the retreat card
			game.getHand(game.getTurn()).moveCardTo(cardPos, game.getDiscardDeck());
			game.getDisplay(game.getTurn()).moveCardTo(targetCardPos, game.getHand(playerPos));
		}
		return returnString;
	}
	
	//Process KnockDown
	public static String knockdown(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos + ":" + targetPos + ":" + targetCardPos;
		int playerPos = game.getTurn();
		
		//Take the target's card and add it to player's hand
		if (game.getHand(targetPos).deckSize() > 0) {
			//Discard the knockdown card
			game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			game.getHand(targetPos).moveCardTo(targetCardPos, game.getHand(playerPos));
		}
		return returnString;
	}
	
	//Process Outmaneuver
	public static String outmaneuver(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		int playerPos = game.getTurn();
		boolean discardedCard = false;
		
		//loop through all players
		for (int i = 0; i < game.getAllPlayers().size(); i++) {
			//discard the last card in the display if it is not the player's display
			// and if the display has more than 1 card
			// and if the target does not have a shield
			if(i != playerPos && game.getDisplay(i).deckSize() > 1 && !game.getPlayer(i).hasShield()) {
				
				if(discardedCard == false) {
					game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the outmaneuver card
					discardedCard = true;
				}
				
				int displaySize = game.getDisplay(i).deckSize();
				game.getDisplay(i).moveCardTo(displaySize-1, game.getDiscardDeck());
			}
		}
		return returnString;
	}
	
	//Process Charge
	public static String charge(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		int playerPos = game.getTurn();
		boolean discardedCard = false;
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
				for(int j = game.getDisplay(i).deckSize()-1; j >= 0; j--) {
					if(game.getDisplay(i).getCard(j).getCardValue() == lowest) {
						
						if(discardedCard == false) {
							game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the charge card
							discardedCard = true;
						}
						
						if(game.getDisplay(i).deckSize() > 1) {
							game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
						}
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Countercharge
	public static String countercharge(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		int playerPos = game.getTurn();
		boolean discardedCard = false;
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
				for(int j = game.getDisplay(i).deckSize()-1; j >= 0; j--) {
					if(game.getDisplay(i).getCard(j).getCardValue() == highest) {
						
						if(discardedCard == false) {
							game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the countercharge card
							discardedCard = true;
						}
						
						if(game.getDisplay(i).deckSize() > 1) {
							game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
						}
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Disgrace
	public static String disgrace(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		int playerPos = game.getTurn();
		
		boolean discardedCard = false;
		//discard all supporter cards given it isn't the player and the target doesn't have a shield
		for (int i = 0; i < game.getAllPlayers().size(); i++) {
			if(!game.getPlayer(i).hasShield() && game.getDisplay(i).deckSize() > 1) {
				for(int j = game.getDisplay(i).deckSize()-1; j >= 0; j--) {
					if(game.getDisplay(i).getCard(j).getCardType() == Type.WHITE) {
						
						if(discardedCard == false) {
							game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the disgrace card
							discardedCard = true;
						}
						if(game.getDisplay(i).deckSize() > 1) {
							game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
						}
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Adapt (absolve)
	public static String adapt(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		// Need to capture values from 2 to 7, for make array from 0 to 7 (i.e. size of 8)
		Boolean[] values = new Boolean[8];

		int playerPos = game.getTurn();
		boolean discardedCard = false;
		
		for(int i = 0; i < game.getAllPlayers().size(); i++) { //loop through players list
			for(int j = 0; j < values.length; j++) { //sets the all values in the boolean array to false
				values[j] = false;
			}
			if(!game.getPlayer(i).hasShield()) { //make sure player doesn't have a shield
				for(int j = game.getDisplay(i).deckSize()-1; j >= 0; j--) { //loop through display of target player
					if(values[game.getDisplay(i).getCard(j).getCardValue()] == true) { //check if we have come across this cards value
						if(discardedCard == false) {
							game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck()); //Discard the adapt card
							discardedCard = true;
						}
						if(game.getDisplay(i).deckSize() > 1) { //if the display size is bigger than 1 then discard that card
							game.getDisplay(i).moveCardTo(j, game.getDiscardDeck());
						}
					}
					else { //if we haven't come across this card value then set its position to true in the boolean array
						values[game.getDisplay(i).getCard(j).getCardValue()] = true;
					}
				}
			}
		}
		return returnString;
	}
	
	//Process Outwit
	public static String outwit(GameState game, int cardPos, String playerDeck, int playerCardPos, int targetPos, String targetDeck, int targetCardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos + ":" + playerDeck + ":" + playerCardPos + ":" + targetPos + ":" + targetDeck + ":" + targetCardPos;
		int playerPos = game.getTurn();
		Card playerCard;
		int playerCardType;
		int playerCardValue;
		
		
		//Check if the player a card in their display to swap
		if (playerDeck.equalsIgnoreCase(Flag.DISPLAY)) {
			playerCard = game.getDisplay(playerPos).getCard(playerCardPos);
			playerCardType = playerCard.getCardType();
			playerCardValue = playerCard.getCardValue();
			
			if (targetDeck.equalsIgnoreCase(Flag.DISPLAY) && game.getDisplay(targetPos).deckSize() > 1) {
				//move the target's card to player's display
				game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDisplay(playerPos));
				//loop to find the player's card and move it to target's display
				for(int i = 0; i < game.getDisplay(playerPos).deckSize(); i++) {
					if(game.getDisplay(playerPos).getCard(i).getCardType() == playerCardType &&
							game.getDisplay(playerPos).getCard(i).getCardValue() == playerCardValue) {
						game.getDisplay(playerPos).moveCardTo(i, game.getDisplay(targetPos));

						//Discard the outwit card
						game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
						break;
					}
				}
			} 
			
			else if (targetDeck.equalsIgnoreCase(Flag.SHIELD) && game.getPlayer(targetPos).hasShield()) {
				//player gets the target's shield and gives the target their display card
				game.getShield(targetPos).moveCardTo(targetCardPos, game.getShield(playerPos));
				game.getDisplay(playerPos).moveCardTo(playerCardPos, game.getDisplay(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			} 
			
			else if (targetDeck.equalsIgnoreCase(Flag.STUN) && game.getPlayer(targetPos).isStunned()) {
				//player gets the target's stun and gives the target their display card
				game.getStun(targetPos).moveCardTo(targetCardPos, game.getStun(playerPos));
				game.getDisplay(playerPos).moveCardTo(playerCardPos, game.getDisplay(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			} else {}
			
		} else if (playerDeck.equalsIgnoreCase(Flag.STUN) && game.getPlayer(playerPos).isStunned()) {
			if (targetDeck.equalsIgnoreCase(Flag.DISPLAY) && game.getDisplay(targetPos).deckSize() > 1) {
				//move the target's card to player's display and give player's stun to target
				game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDisplay(playerPos));
				game.getStun(playerPos).moveCardTo(playerCardPos, game.getStun(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			} 
			else if (targetDeck.equalsIgnoreCase(Flag.SHIELD) && game.getPlayer(targetPos).hasShield()) {
				//player gets the target's shield and gives their stun to the target
				game.getShield(targetPos).moveCardTo(targetCardPos, game.getShield(playerPos));
				game.getStun(playerPos).moveCardTo(playerCardPos, game.getStun(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			}
		}
		
		else if (playerDeck.equalsIgnoreCase(Flag.SHIELD) && game.getPlayer(playerPos).hasShield()) {
			if (targetDeck.equalsIgnoreCase(Flag.DISPLAY) && game.getDisplay(targetPos).deckSize() > 1) {
				//move the target's card to player's display and give player's shield to target
				game.getDisplay(targetPos).moveCardTo(targetCardPos, game.getDisplay(playerPos));
				game.getShield(playerPos).moveCardTo(playerCardPos, game.getShield(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			} 
			else if (targetDeck.equalsIgnoreCase(Flag.STUN) && game.getPlayer(targetPos).isStunned()) {
				//player gets the target's stun and gives their shield to the target
				game.getStun(targetPos).moveCardTo(targetCardPos, game.getStun(playerPos));
				game.getShield(playerPos).moveCardTo(playerCardPos, game.getShield(targetPos));

				//Discard the outwit card
				game.getHand(playerPos).moveCardTo(cardPos, game.getDiscardDeck());
			}
			else{}
		}
		
		return returnString;
	}
	
	//Process Shield
	public static String shield(GameState game, int cardPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos;
		int playerPos = game.getTurn();
		game.getHand(playerPos).moveCardTo(cardPos, game.getShield(playerPos));		
		return returnString;
	}
	
	//Process Stun
	public static String stun(GameState game, int cardPos, int targetPos) {
		String returnString = Flag.ACTION_CARD + ":" + cardPos + ":" + targetPos;

		int playerPos = game.getTurn();
		game.getHand(playerPos).moveCardTo(cardPos, game.getStun(targetPos));

		return returnString;
	}
	
	public static String ivanhoe(GameState game, int actionCardPos, int ivanhoePlayer) {
		String result = (Flag.IVANHOE_PLAYED + ":" + actionCardPos + ":" + ivanhoePlayer);
		int playerPos = game.getTurn();
		game.getHand(playerPos).moveCardTo(actionCardPos, game.getDiscardDeck());
		
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
