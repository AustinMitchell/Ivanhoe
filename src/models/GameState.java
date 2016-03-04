package models;
import rulesengine.Type;

import java.util.ArrayList;

public class GameState {
	private int tournamentNumber;
	private int tournamentColour;
	private int turn;
	private ArrayList<Player> players;
	private Deck drawDeck;
	private Deck discardDeck;
	private boolean gameOver;
	private boolean tournamentStarted;
	private int prevTournamentColour;
	
	private static final int[] cardTypeData = {Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, 
												Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, 
												Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.RED, 
												Type.RED, Type.RED, Type.RED, Type.RED, Type.RED, 
												Type.RED, Type.RED, Type.RED, Type.RED, Type.RED, 
												Type.RED, Type.RED, Type.RED, Type.BLUE, Type.BLUE, 
												Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, 
												Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, 
												Type.BLUE, Type.BLUE, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
												Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
												Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
												Type.YELLOW, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
												Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
												Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
												Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
												Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
												Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
												Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
												Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
												Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
												Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
												Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION};
	
	private static final int[] cardValueData = {3, 3, 3, 3, 4, 
												4, 4, 4, 5, 5, 
												5, 5, 7, 7, 3, 
												3, 3, 3, 3, 3, 
												4, 4, 4, 4, 4, 
												4, 5, 5, 2, 2, 
												2, 2, 3, 3, 3, 
												3, 4, 4, 4, 4, 
												5, 5, 2, 2, 2, 
												2, 3, 3, 3, 3, 
												3, 3, 3, 3, 4, 
												4, 1, 1, 1, 1, 
												1, 1, 1, 1, 1, 
												1, 1, 1, 1, 1, 
												2, 2, 2, 2, 2, 
												2, 2, 2, 3, 3, 
												3, 3, 3, 3, 3, 
												3, 6, 6, 6, 6, 
												Card.UNHORSE, Card.CHANGE_WEAPON, Card.DROP_WEAPON, Card.BREAK_LANCE, Card.RIPOSTE, 
												Card.DODGE, Card.RETREAT, Card.KNOCKDOWN, Card.OUTMANEUVER, Card.CHARGE, 
												Card.COUNTERCHARGE, Card.DISGRACE, Card.ADAPT, Card.OUTWIT, Card.SHIELD, 
												Card.STUNNED, Card.IVANHOE, Card.RIPOSTE, Card.RIPOSTE, Card.KNOCKDOWN};
	
	public GameState() {
		discardDeck = new Deck();
		tournamentNumber = 0;
		tournamentColour = -1;
		tournamentStarted = false;
		prevTournamentColour = -1;
		//setUpStartingHands();
	}
	
	public void initializeServer(ArrayList<Player> newPlayers) {
		players = newPlayers;
		drawDeck = new Deck();
		for(int i = 0; i < players.size(); i++) {
			players.get(i).enterTournament();
		}
		setTurn(0);
		for(int i = 0; i < 110; i++) {
			Card card = new Card(cardTypeData[i], cardValueData[i]);
			drawDeck.add(card);
		}
		drawDeck.shuffle();
	}
	
	public void initializeClient(ArrayList<Player> newPlayers, int[][] cardData) {
		players = newPlayers;
		drawDeck = new Deck();
		for(int i = 0; i < players.size(); i++) {
			players.get(i).enterTournament();
		}
		setTurn(0);
		for(int i = 0; i < 110; i++) {
			Card card = new Card(cardData[i][0], cardData[i][1]);
			drawDeck.add(card);
		}
	}
	
	public Player getPlayer(String pName) {
		Player player = null;
		for(int i = 0; i < players.size(); i++) {
			if(pName.equals(String.valueOf(players.get(i).getName()))) {
				player = players.get(i);
			}
		}
		return player;
	}
	
	
	
	public boolean hasTournamentStarted() {
		return tournamentStarted;
	}
	
	public void setTournamentStarted(boolean tournamentStarted) {
		this.tournamentStarted = tournamentStarted;
	}
	
	public ArrayList<Player> getAllPlayers() {
		return players;
	}
	
	public void setTournamentColour(int colour) {
		tournamentColour = colour;
	}
	
	public int getTournamentColour() {
		return tournamentColour;
	}
	
	public void setPrevTournamentColour(int colour) {
		prevTournamentColour = colour;
	}
	
	public int getPrevTournamentColour() {
		return prevTournamentColour;
	}
	
	/*public boolean isGameOver() {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).hasWon()) gameOver = true;
			else gameOver = false;
		}
		return gameOver;
	}*/
	
	public void setGameOver(boolean status) {
		gameOver = status;
	}
	
	public void incrementTournamentNumber() {
		tournamentNumber++;
	}
	
	public int getTournamentNumber() {
		return tournamentNumber;
	}
	
	public Deck getDrawDeck() {
		return drawDeck;
	}
	
	public Deck getDiscardDeck() {
		return discardDeck;
	}
	
	//give each player 8 cards to start with at the beginning of a game
	public void setUpStartingHands() {
		for(int i = 0; i < players.size(); i++) {
			for(int j = 0; j < 8; j++) {
				drawDeck.draw(players.get(i).getHand());
			}
		}
	}
	
	public void renewDrawDeck() {
		drawDeck = discardDeck;
		drawDeck.shuffle();
		discardDeck = new Deck();
	}
	
	public void nextTurn() {
		if(turn==players.size()-1) turn = 0;
		else turn++;
		if(!players.get(turn).isInTournament()) nextTurn();
	}
	
	public void setTurn(int person) {
		if(person>=0 && person<=players.size()) turn = person;
		else {
			throw new RuntimeException("Passed Turn is Out of Bounds: " + person);
		}
	}
	
	public int getTurn() {
		return turn;
	}
	
}
