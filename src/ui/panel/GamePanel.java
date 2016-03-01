package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.scrollbox.ScrollDialogBox;
import ui.*;
import ui.panel.overlay.OverlayPanel;

public class GamePanel extends ScaledPanel {
	public static final int THIS_PLAYER = 0;
	
	GameController controller;
	
	CardDisplayPanel[] display;
	StatusBar[] playerStatus;
	ScrollDialogBox messageScrollBox;
	DescriptionBox descriptionBox;
	ScaledPanel mainGamePanel, sidePanel;
	CardWidget deck, discard;
	Image emptyDiscard;
	Button endTurn, exitGame;
	OverlayPanel currentOverlay;
	
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
		
		mainGamePanel = new ScaledPanel(){{			
			display = new CardDisplayPanel[numPlayers];
			display[THIS_PLAYER] = new CardDisplayPanel(Image.Orientation.UP, true);
			switch(numPlayers) {
				case 2:
					display[1] = new CardDisplayPanel(Image.Orientation.UP, false);
					break;
				case 3:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					display[2] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					break;
				case 4:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					display[2] = new CardDisplayPanel(Image.Orientation.UP, false);
					display[3] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					break;
				case 5:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT, false);
					display[2] = new CardDisplayPanel(Image.Orientation.UP, false);
					display[3] = new CardDisplayPanel(Image.Orientation.UP, false);
					display[4] = new CardDisplayPanel(Image.Orientation.LEFT, false);
					break;
				default:
					throw new RuntimeException("Initialized game with a bad number of players (not within 2 an 5)");
			}
			
			for (int d=0; d<display.length; d++) {
				for (int i=0; i<8; i++) {
					int[] v = CardData.ALL_CARD_VALUES[(int)(Math.random()*CardData.ALL_CARD_VALUES.length)];
					CardWidget c = new CardWidget(v[0], v[1]);
					display[d].addCard(c);
				}
				if (d != THIS_PLAYER) {
					display[d].setCardMoveOnHover(false);
				} else {
					display[d].setEnabled(false);
				}
				display[d].setDrawContainingPanel(false);
				display[d].setFillColor(null);
			}
			
			CardDisplayPanel deckPlaceHolder = new CardDisplayPanel(Image.Orientation.UP, false);
			
			deck = new CardWidget(0, 0, false);
			
			discard = new CardWidget(0, 0, false);
			
			deckPlaceHolder.addCard(deck);
			deckPlaceHolder.addCard(discard);
			
			emptyDiscard = new Image(144, 200);
			draw.setColors(new Color(220, 220, 220), null);
			draw.rect(emptyDiscard, 0, 0, 144, 200);
			discard.setImage(emptyDiscard);
			discard.setMoveOnHover(false);
			
			switch(numPlayers) {
				case 2:
					addWidget(display[THIS_PLAYER], 17, 75, 66, 23);
					addWidget(display[1], 17,  2, 66, 23);
					break;
				case 3:
					addWidget(display[THIS_PLAYER], 25, 75, 50, 23);
					addWidget(display[1],  2, 15, 13, 70);
					addWidget(display[2], 85, 15, 13, 70);
					break;
				case 4:
					addWidget(display[THIS_PLAYER], 25, 75, 50, 23);
					addWidget(display[1],  2, 15, 13, 70);
					addWidget(display[2], 25,  2, 50, 23);
					addWidget(display[3], 85, 15, 13, 70);
					break;
				case 5:
					addWidget(display[THIS_PLAYER], 25, 75, 50, 23);
					addWidget(display[1],  2, 30, 13, 65);
					addWidget(display[2],  2,  2, 45, 20);
					addWidget(display[3], 53,  2, 45, 20);
					addWidget(display[4], 85, 30, 13, 65);
					break;
			}
			
			addWidget(deckPlaceHolder, 0, 40, 100, 20);
		}};
		
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
					playerStatus[i].collectToken(i);
					addWidget(playerStatus[i], 0, i, 1, 1);
				}
			}};
			
			endTurn = new Button("End Turn");
			endTurn.setEnabled(false);
			
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
		
		// Handle turns if it's AI's turn
		handleAITurn();
		
		handleExitGame();
	}
	@Override
	public void draw() {
		// Display panel for the current player's turn
		CardDisplayPanel p = display[currentTurn];
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
					List<Widget> widgetList = display[i].getWidgetList();
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
			display[THIS_PLAYER].setEnabled(false);
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
		display[player].addCard(new CardWidget(v[0], v[1]));
		if (currentTurn == THIS_PLAYER) {
			messageScrollBox.addLine(" > You drew a card.");
		} else {
			messageScrollBox.addLine(" > Player " + (currentTurn+1) + " drew a card.");
		}
	}
	
	private void handleDeck() {
		if (deck.isClicked()) {
			display[0].setEnabled(true);
			deck.setEnabled(false);
			endTurn.setEnabled(true);
			drawFromDeck(0);
		}
	}
	
	/** HAND OPERATIONS **/
	private void playCard(int cardIndex) {
		CardWidget playedCard = (CardWidget)display[currentTurn].getWidgetList().get(cardIndex);
		int type = playedCard.getType();
		int value = playedCard.getValue();
		
		discard.setCard(type, value);
		discard.setFaceUp(true);
		display[currentTurn].removeIndex(cardIndex);
		
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
				aiActions = (int)Math.min((Math.random()*2)+1, display[currentTurn].getWidgetList().size());
				drawFromDeck(currentTurn);
			} else if (aiActions > 0) {
				aiActions--;
				playCard((int)(Math.random()*display[currentTurn].numWidgets()));
			} else {
				changeTurn();
			}
		}
	}
}
