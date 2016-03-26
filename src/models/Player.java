package models;
import rulesengine.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
	private String name;
	private Deck hand;
	private Deck display;
	private Deck shieldDeck;
	private Deck stunDeck;
	private boolean inTournament;
	private boolean[] tokens;
	//private boolean[] status;
	private int drawnToken;
	
	public Player(String playerName) {
		name = playerName;
		tokens = new boolean[5];
		for(int i = 0; i < 5; i++) tokens[i] = false;
		hand = new Deck();
		display = new Deck();
		shieldDeck = new Deck();
		stunDeck = new Deck();
	}
	
	public String getName() {
		return name;
	}
	
	//check if the player is stunned
	public boolean isStunned() {
		if(stunDeck.deckSize() == 0)
			return false;
		
		else
			return true;
	}
	
	//getStun function (just in case)
	public Deck getStunDeck()  {
		return stunDeck;
	}
	
	
	//removes the stun from the player but also returns the card in order to throw it in the discard deck
	/*public Card removeStun() {
		Card card = stun;
		stun = null;
		return card;
	}*/
	
	/*public void shieldPlayer() {
		stun = new Card(Type.ACTION, Card.SHIELD);
	}*/
	
	//check if player has a shield
	public boolean hasShield() {
		if(shieldDeck.deckSize() == 0)
			return false;
		else
			return true;
	}
	
	//getShield function (just in case)
	public Deck getShieldDeck() {
		return shieldDeck;
	}
	
	//removes the shield from the player but also returns the card in order to throw it in the discard deck
	/*public Card removeShield() {
		Card card = shield;
		shield = null;
		return card;
	}*/
	
	public boolean checkToken(int i) {
		return tokens[i];
	}
	
	public void setToken(int i, boolean val) {
		tokens[i] = val;
	}
	
	public boolean hasWon() {
		boolean won = false;
		for(int i = 0; i < 5; i++) {
			if(tokens[i] == true) won = true;
			else {
				won = false;
				break;
			}
		}
		return won;
	}
	
	public Deck getDisplay() {
		return display;
	}
	
	public Deck getHand() {
		return hand;
	}
	
	public void enterTournament() {
		inTournament = true;
	}
	
	public void exitTournament() {
		inTournament = false;
	}
	
	public boolean isInTournament() {
		return inTournament;
	}
	
	public int getDisplayValue(int colour) {
		if(colour == Type.GREEN) {
			return display.deckSize();
		}
		else {
			int value = 0;
			for(int i = 0; i < display.deckSize(); i++) {
				value += display.getCard(i).getCardValue();
			}
			return value;
		}
	}
	
	public void setDrawnToken(int token) {
		drawnToken = token;
	}
	
	public boolean displayHasMaiden() {
		for(int i = 0; i < display.deckSize(); i++) {
			if(display.getCard(i).getCardType() == Type.WHITE && display.getCard(i).isMaiden()) {
				return true;
			}
		}
		return false;
	}
	
	public int getDrawnToken() {
		return drawnToken;
	}
	
}
