
package model;
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
	private boolean playedValueCard = false; //toggle to true every time a player plays a value card. useful for STUNNED implementation
	
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
	
	private static int[] UNMODIFIED_ARRAY() {
		int nil = 0;
		int[] arr =    {3, 3, 3, 3, 4, 
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
						nil, nil, nil, nil, nil, 
						nil, nil, nil, nil, nil, 
						nil, nil, nil, nil, nil, 
						nil, nil, nil, nil, nil};
		return arr;
	}
	
	private static final int[] BASIC_CARDS() {
		int[] cards = UNMODIFIED_ARRAY();
		cards[90]  = Card.UNHORSE;
		cards[91]  = Card.CHANGE_WEAPON;
		cards[92]  = Card.DROP_WEAPON;
		cards[93]  = Card.BREAK_LANCE;
		cards[94]  = Card.RIPOSTE;
		cards[95]  = Card.DODGE;
		cards[96]  = Card.RETREAT;
		cards[97]  = Card.KNOCKDOWN;
		cards[98]  = Card.OUTMANEUVER;
		cards[99]  = Card.CHARGE;
		cards[100] = Card.COUNTERCHARGE;
		cards[101] = Card.DISGRACE;
		cards[102] = Card.ADAPT;
		cards[103] = Card.OUTWIT;
		cards[104] = Card.SHIELD;
		cards[105] = Card.STUNNED;
		cards[106] = Card.IVANHOE;
		cards[107] = Card.RIPOSTE;
		cards[108] = Card.RIPOSTE;
		cards[109] = Card.KNOCKDOWN;
		return cards;
	}
	
	private static final int[] ALL_SPECIFIC_ACTION(int actionCard) {
		int[] cards = UNMODIFIED_ARRAY();
		for (int i=90; i<110; i++) {
			cards[i] = actionCard;
		}
		return cards;
	}
	
	private static final int[] cardValueData = ALL_SPECIFIC_ACTION(Card.RIPOSTE);
	
	public static final int TOURNAMENT_NOT_STARTED = -1;
	
	
	public GameState() {
		discardDeck = new Deck();
		tournamentNumber = 0;
		tournamentColour = TOURNAMENT_NOT_STARTED;
		tournamentStarted = false;
		prevTournamentColour = TOURNAMENT_NOT_STARTED;
		//setUpStartingHands();
	}
	
	public String initializeServer(ArrayList<Player> newPlayers) {
		players = newPlayers;
		drawDeck = new Deck();
		String result = "initClient:";
		for(Player p: players) {
			p.enterTournament();
			result += p.getName() + ":";
		}
		result += Flag.CARDS_BEGIN;
		setTurn(0);
		for(int i = 0; i < 110; i++) {
			Card card = new Card(cardTypeData[i], cardValueData[i]);
			drawDeck.add(card);
		}
		drawDeck.shuffle();
		
		for(int i = 0; i < drawDeck.deckSize(); i++) {
			result += ":" + drawDeck.getCard(i).getCardType() + ":";
			result += drawDeck.getCard(i).getCardValue();
		}
		
		setUpStartingHands();
		return result;
	}
	
	//Initialize the game on the client side by receiving the participating players and card data
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
		setUpStartingHands();
	}
	
	//Function to get player by name
	public Player getPlayer(String pName) {
		Player player = null;
		for(int i = 0; i < players.size(); i++) {
			if(pName.equals(String.valueOf(players.get(i).getName()))) {
				player = players.get(i);
			}
		}
		return player;
	}
	
	//Function to get player by index
	public Player getPlayer(int playerPos) {
		return players.get(playerPos);
	}
	
	//Function to get the player's hand
	public Deck getHand(int playerPos) {
		return players.get(playerPos).getHand();
	}
	
	//Function to get player's display
	public Deck getDisplay(int playerPos) {
		return players.get(playerPos).getDisplay();
	}
	
	//Function to get player's stun deck
	public Deck getStun(int playerPos) {
		return players.get(playerPos).getStunDeck();
	}
	
	//Function to get player's shield deck
		public Deck getShield(int playerPos) {
			return players.get(playerPos).getShieldDeck();
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
		if(getPrevTournamentColour() == Type.PURPLE && colour == Type.PURPLE) {
			tournamentColour = TOURNAMENT_NOT_STARTED;
			return;
		}
		
		else if(colour == TOURNAMENT_NOT_STARTED) {
			tournamentColour = colour;
		}
		
		else {
			tournamentColour = colour;
			setPrevTournamentColour(colour);
		}
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
	
	//setter function for testing purposes
	public void setTournamentNumber(int num) {
		tournamentNumber = num;
	}
	
	public void incrementTournamentNumber() {
		tournamentNumber++;
	}
	
	public int getTournamentNumber() {
		return tournamentNumber;
	}
	
	public boolean getPlayedValueCard() {
		return playedValueCard;
	}
	
	public void setplayedValueCard(boolean bool) {
		playedValueCard = bool;
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
	
	public void renewDrawDeck(int[][] cardData) {
		for(int i = 0; i < cardData.length; i++) {
			Card card = new Card(cardData[i][0], cardData[i][1]);
			drawDeck.add(card);
		}
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
	
	public boolean playerCanContinue(int playerPos) {
		int display = players.get(playerPos).getDisplayValue(tournamentColour);
		for (int i=0; i<players.size(); i++) {
			if (i != playerPos && players.get(i).isInTournament()) {
				if (display <= players.get(i).getDisplayValue(tournamentColour)) {
					return false;
				}
			}
		}
		return true;
	}
}
