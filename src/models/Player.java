package models;
import rulesengine.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {
	private String name;
	private Deck hand;
	private Deck display;
	private boolean inTournament;
	private boolean[] tokens;
	private boolean[] status;
	private int drawnToken;
	
	public Player(String playerName) {
		name = playerName;
		tokens = new boolean[5];
		for(int i = 0; i < 5; i++) tokens[i] = false;
		status = new boolean[2];
		for(int i = 0; i < 2; i++) status[i] = false;
		hand = new Deck();
		display = new Deck();
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isStunned() {
		return status[0];
	}
	
	public void stunPlayer() {
		status[0] = true;
	}
	
	public void unstunPlayer() {
		status[0] = false;
	}
	
	public boolean hasShield() {
		return status[1];
	}

	public void shieldPlayer() {
		status[1] = true;
	}
	
	public void unshieldPlayer() {
		status[1] = false;
	}
	
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
	
	public int getDrawnToken() {
		return drawnToken;
	}
	
}
