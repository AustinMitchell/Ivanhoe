package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.scrollbox.ScrollDialogBox;
import ui.*;
import ui.panel.overlay.NewTournamentOverlay;
import ui.panel.overlay.OverlayPanel;
import ui.utilitypanel.CardDisplayPanel;
import ui.utilitypanel.StatusBar;

public class GamePanel extends ScaledPanel {
	public enum OverlayCommand {
		START_TOURNAMENT, END_TURN
	}
	
	public static final int THIS_PLAYER = 0;
	
	GameController controller;
	
	CardDisplayPanel[] hand, display;
	StatusBar[] playerStatus;
	ScrollDialogBox messageScrollBox;
	DescriptionBox descriptionBox;
	ScaledPanel mainGamePanel, sidePanel;
	CardWidget deck, discard;
	Image emptyDiscard;
	Button endTurn, exitGame;
	
	OverlayPanel currentOverlay;
	OverlayCommand overlayCommand;
	boolean startNewOverlay;
	
	int numPlayers;
	
	CardWidget interactionCard;
	int interactionPlayer;
	int interactionIndex;
	
	int currentTurn;
	boolean turnStart;
	
	int aiTimer;
	int aiActions;
	

	public GamePanel(final int numPlayers, GameController controller) {
		super();
		
		aiTimer = 0;
		aiActions = 0;
		
		this.controller = controller;
		
		this.numPlayers = numPlayers;
		currentTurn = 0;
		turnStart = true;
		
		// Main panel with hands, displays and draw. Any overlays that come up also get placed here.
		mainGamePanel = new ScaledPanel(){{			
			// Set orientations for each hand and display, and whether cards will be faceup or down
			hand =    new CardDisplayPanel[numPlayers];
			display = new CardDisplayPanel[numPlayers];
			hand   [THIS_PLAYER] = new CardDisplayPanel(Image.Orientation.UP, true);
			display[THIS_PLAYER] = new CardDisplayPanel(Image.Orientation.UP, true);
			switch(numPlayers) {
				case 2:
					hand   [1] = new CardDisplayPanel(Image.Orientation.DOWN, false);
					display[1] = new CardDisplayPanel(Image.Orientation.UP, true);
					break;
				case 3:
					hand   [1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					hand   [2] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, true);
					display[2] = new CardDisplayPanel(Image.Orientation.LEFT, true);
					break;
				case 4:
					hand   [1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					hand   [2] = new CardDisplayPanel(Image.Orientation.DOWN, false);
					hand   [3] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, true);
					display[2] = new CardDisplayPanel(Image.Orientation.UP, true);
					display[3] = new CardDisplayPanel(Image.Orientation.LEFT, true);
					break;
				case 5:
					hand   [1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					hand   [2] = new CardDisplayPanel(Image.Orientation.DOWN, false);
					hand   [3] = new CardDisplayPanel(Image.Orientation.DOWN, false);
					hand   [4] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, true);
					display[2] = new CardDisplayPanel(Image.Orientation.UP, true);
					display[3] = new CardDisplayPanel(Image.Orientation.UP, true);
					display[4] = new CardDisplayPanel(Image.Orientation.LEFT, true);
					break;
				default:
					throw new RuntimeException("Initialized game with a bad number of players (not within 2 an 5)");
			}
			
			// TODO: Fix, adds random cards for testing
			for (int d=0; d<hand.length; d++) {
				for (int i=0; i<8; i++) {
					int[] v = CardData.ALL_CARD_VALUES[(int)(Math.random()*CardData.ALL_CARD_VALUES.length)];
					CardWidget c = new CardWidget(v[0], v[1]);
					hand[d].addCard(c);
				}
				if (d != THIS_PLAYER) {
					hand[d].setCardMoveOnHover(false);
				} else {
					hand[d].setEnabled(false);
				}
				display[d].setDrawContainingPanel(true);
				display[d].setFillColor(null);
			}
			
			// Panel to get the deck and discard in the right spot. Panel background is invisible
			CardDisplayPanel deckPlaceHolder = new CardDisplayPanel(Image.Orientation.UP, false);
			
			// Card widget for the deck
			deck = new CardWidget(0, 0, false);
			
			// Same for discard pile
			discard = new CardWidget(0, 0, false);
			
			deckPlaceHolder.addCard(deck);
			deckPlaceHolder.addCard(discard);
			
			// Image to render when discard is empty
			emptyDiscard = new Image(144, 200);
			draw.setColors(new Color(220, 220, 220), null);
			draw.rect(emptyDiscard, 0, 0, 144, 200);
			discard.setImage(emptyDiscard);
			discard.setMoveOnHover(false);
			
			// Layouts for player displays and hands according to number of players
			// Represents relative x and y positions on panel, and width and height variables
			switch(numPlayers) {
				case 2:
					addWidget(hand[THIS_PLAYER],    17,  75, 66, 23);
					addWidget(hand[1],              17, -19, 66, 23);
					
					addWidget(display[THIS_PLAYER], 17,  50, 66, 23);
					addWidget(display[1],           17,   6, 66, 23);
					break;
				case 3:
					addWidget(hand[THIS_PLAYER],    25,  75, 50, 23);
					addWidget(hand[1],             -10,  15, 13, 70);
					addWidget(hand[2],              97,  15, 13, 70);
					
					addWidget(display[THIS_PLAYER], 25,  50, 50, 23);
					addWidget(display[1],            5,  15, 13, 70);
					addWidget(display[2],           82,  15, 13, 70);
					break;
				case 4:
					addWidget(hand[THIS_PLAYER],    25,  75, 50, 23);
					addWidget(hand[1],             -10,  15, 13, 70);
					addWidget(hand[2],              25, -19, 50, 23);
					addWidget(hand[3],              97,  15, 13, 70);
					
					addWidget(display[THIS_PLAYER], 25,  50, 50, 23);
					addWidget(display[1],            5,  15, 13, 70);
					addWidget(display[2],           25,   6, 50, 23);
					addWidget(display[3],           82,  15, 13, 70);
					break;
				case 5:
					addWidget(hand[THIS_PLAYER],    25,  75, 50, 23);
					addWidget(hand[1],             -10,  30, 13, 65);
					addWidget(hand[2],               2, -16, 45, 20);
					addWidget(hand[3],              53, -16, 45, 20);
					addWidget(hand[4],              97,  30, 13, 65);
					
					addWidget(display[THIS_PLAYER], 25,  50, 50, 23);
					addWidget(display[1],            5,  30, 13, 65);
					addWidget(display[2],            2,   6, 45, 20);
					addWidget(display[3],           53,   6, 45, 20);
					addWidget(display[4],           82,  30, 13, 65);
					break;
			}
			
			addWidget(deckPlaceHolder, 0, 32, 100, 14);
		}};
		
		// Panel on the side for other useful widgets like a descriptionbox for cards, end turn button,
		// tokens collected for each player, statuses, etc.
		sidePanel = new ScaledPanel() {{
			setCustomDraw(new CustomDraw() { 
				public void draw(Widget w) {
					draw.setColors(new Color(0xf5f5dc), Color.BLACK);
					draw.rect(w.getX(), w.getY(), w.getWidth(), w.getHeight());
				}});
			
			messageScrollBox = new ScrollDialogBox();
			
			descriptionBox = new DescriptionBox();
			
			ScaledPanel playerStatusPanel = new ScaledPanel(1, numPlayers) {{
				playerStatus = new StatusBar[numPlayers];
				for (int i=0; i<numPlayers; i++) {
					playerStatus[i] = new StatusBar("Player " + (i+1));
					addWidget(playerStatus[i], 0, i, 1, 1);
				}
			}};
			
			endTurn = new Button("End Turn");
			endTurn.setEnabled(false);
			
			// TODO: remove or modify, here for testing purposes
			exitGame = new Button("Exit Game");
			exitGame.setFillColor(Color.RED);
			exitGame.setBorderColor(Color.WHITE);
			exitGame.setTextColor(Color.WHITE);
			exitGame.setFont(new Font("Arial", Font.PLAIN, 10));
			
			addWidget(messageScrollBox, 3,  3, 94, 35);
			addWidget(descriptionBox, 	3, 40, 94, 20);
			addWidget(playerStatusPanel, 3, 62, 94, 23);
			addWidget(endTurn, 		   20, 87, 60,  8);			
			addWidget(exitGame, 	   77, 97, 18,  2);
			
		}};		
		
		addWidget(mainGamePanel, 0, 0, 80, 100, 1);
		addWidget(sidePanel, 	80, 0, 20, 100, 0);
		
		// TODO: Remove, here for testing
		prepareOverlay(OverlayCommand.START_TOURNAMENT);
		
	}
	
	@Override
	public void update() {
		super.update();
				
		handleDescriptionBox();
		
		// Check this player interaction
		findCardInteraction();
		handleDeck();
		handleHand();
		checkTurnChange();
		
		// Handles any overlay stuff
		handleOverlay();
		
		// Handle turns if it's AI's turn
		handleAITurn();
		
		handleExitGame();
	}
	@Override
	public void draw() {
		// Display panel for the current player's turn
		CardDisplayPanel p = hand[currentTurn];
		draw.setColors(null, new Color(0, 200, 0), 4);
		draw.rect(p.getX(), p.getY(), p.getWidth(), p.getHeight());
		super.draw();
	}
	
	private void findCardInteraction() {
		interactionCard = null;
		interactionIndex = -1;
		interactionPlayer = -1;
		
		outer: {
			if (currentOverlay == null) {
				for (int i=0; i<numPlayers; i++) {
					List<Widget> widgetList = hand[i].getWidgetList();
					for (int j=widgetList.size()-1; j>=0; j--) {
						CardWidget c = (CardWidget)widgetList.get(j);
						if (c.containsMouse()) {
							interactionCard = c;
							interactionPlayer = i;
							interactionIndex = j;
							break outer;
						}
					}
				}
			}
			if (discard.containsMouse()) {
				interactionCard = discard;
				interactionPlayer = -1;
				interactionIndex = -1;
			}
		}
	}
	
	private void checkTurnChange() {
		if (endTurn.isClicked()) {
			hand[THIS_PLAYER].setEnabled(false);
			endTurn.setEnabled(false);
			changeTurn();
		}
	}
	private void changeTurn() {
		turnStart = true;
		
		currentTurn += 1;
		if (currentTurn == numPlayers) {
			currentTurn = THIS_PLAYER;
		}
		
		messageScrollBox.addRepeatedTextLine("-");
		if (currentTurn == THIS_PLAYER) {
			prepareOverlay(OverlayCommand.START_TOURNAMENT);
			deck.setEnabled(true);
			messageScrollBox.addLine("It is now your turn.");
		} else {
			messageScrollBox.addLine("It is now Player " + (currentTurn+1) + "'s turn.");
		}
	}
	
	private void handleExitGame() {
		if (exitGame.isClicked()) {
			controller.mainScreen();
		}
	}
	
	private void handleDescriptionBox() {
		if (currentOverlay == null) {
			if (interactionCard != null && interactionCard.isFaceUp()) {
				descriptionBox.setDisplay(interactionCard.getType(), interactionCard.getValue());
			} else {
				descriptionBox.clearDisplay();
			}
		}
	}
	
	/** DECK OPERATIONS **/
	private void drawFromDeck(int player) {
		turnStart = false;
		int[] v = CardData.ALL_CARD_VALUES[(int)(Math.random()*CardData.ALL_CARD_VALUES.length)];
		hand[player].addCard(new CardWidget(v[0], v[1]));
		if (currentTurn == THIS_PLAYER) {
			messageScrollBox.addLine(" > You drew a card.");
		} else {
			messageScrollBox.addLine(" > Player " + (currentTurn+1) + " drew a card.");
		}
	}
	
	private void handleDeck() {
		if (deck.isClicked()) {
			hand[0].setEnabled(true);
			deck.setEnabled(false);
			endTurn.setEnabled(true);
			drawFromDeck(0);
		}
	}
	
	/** HAND OPERATIONS **/
	private void playCard(int cardIndex) {
		CardWidget playedCard = (CardWidget)(hand[currentTurn].getWidgetList().get(cardIndex));
		int type = playedCard.getType();
		int value = playedCard.getValue();
		
		if (type <= 5) {
			display[currentTurn].addCard(new CardWidget(type, value));
		} else {
			discard.setCard(type, value);
			discard.setFaceUp(true);
		}
		
		hand[currentTurn].removeIndex(cardIndex);
		
		if (currentTurn == THIS_PLAYER) {
			messageScrollBox.addLine(" > You played " + CardData.getCardName(type, value));
		} else {
			messageScrollBox.addLine(" > Player " + (currentTurn+1) + " played " + CardData.getCardName(type, value));
		}
	}
	private void handleHand() {
		if (currentTurn == THIS_PLAYER && interactionCard != null && interactionPlayer == THIS_PLAYER) {	
			if (interactionCard.isClicked()) {
				playCard(interactionIndex);
			}
		}
	}
	
	/** AI CONTROLLER **/
	private void handleAITurn() {
		if (aiTimer > 0) {
			aiTimer--;
		}
		if (currentTurn != 0 && aiTimer <= 0) {
			aiTimer = 45;
			if (turnStart) {
				aiActions = (int)Math.min((Math.random()*2)+1, hand[currentTurn].getWidgetList().size());
				drawFromDeck(currentTurn);
			} else if (aiActions > 0) {
				aiActions--;
				playCard((int)(Math.random()*hand[currentTurn].numWidgets()));
			} else {
				changeTurn();
			}
		}
	}
	
	private void prepareOverlay(OverlayCommand oc) {
		startNewOverlay = true;
		overlayCommand = oc;
	}
	private void handleOverlay() {
		// Checks if a new overlay needs to start
		if (startNewOverlay) {
			switch(overlayCommand) {
				// A new tournament is starting, and you are starting the tournament
				case START_TOURNAMENT:
					currentOverlay = new NewTournamentOverlay(descriptionBox, hand[THIS_PLAYER]);
					break;
				case END_TURN:
					break;
			}
			startNewOverlay = false;
			mainGamePanel.addWidget(currentOverlay, 2, 2, 96, 96, 0);
		// Otherwise, check on the status of the current overlay if one exists
		} else if (currentOverlay != null) {
			if (currentOverlay.isOverlayActionComplete()) {
				mainGamePanel.removeWidget(currentOverlay);
				currentOverlay = null;
			}
		}
	}
}
