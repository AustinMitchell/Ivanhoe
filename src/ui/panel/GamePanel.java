package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.scrollbox.ScrollDialogBox;
import ui.CardData;
import ui.CardDisplayPanel;
import ui.CardWidget;
import ui.DescriptionBox;
import ui.GameController;
import ui.panel.overlay.OverlayPanel;

public class GamePanel extends ScaledPanel {
	GameController controller;
	
	CardDisplayPanel[] display;
	ScrollDialogBox messageScrollBox;
	DescriptionBox descriptionBox;
	ScaledPanel mainGamePanel, sidePanel;
	CardWidget deck, discard;
	Button endTurn, exitGame;
	OverlayPanel currentOverlay;
	
	int numPlayers;
	
	CardWidget interactionCard;
	int interactionPlayer;
	int interactionIndex;
	
	int currentTurn;
	boolean turnStart;
	
	public GamePanel(final int numPlayers, GameController controller) {
		super();
		
		this.controller = controller;
		
		this.numPlayers = numPlayers;
		currentTurn = 0;
		turnStart = true;
		
		mainGamePanel = new ScaledPanel(){{			
			display = new CardDisplayPanel[numPlayers];
			
			display[0] = new CardDisplayPanel(Image.Orientation.UP);
			switch(numPlayers) {
				case 2:
					display[1] = new CardDisplayPanel(Image.Orientation.UP);
					break;
				case 3:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT);
					display[2] = new CardDisplayPanel(Image.Orientation.LEFT);
					break;
				case 4:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT);
					display[2] = new CardDisplayPanel(Image.Orientation.UP);
					display[3] = new CardDisplayPanel(Image.Orientation.LEFT);
					break;
				case 5:
					display[1] = new CardDisplayPanel(Image.Orientation.RIGHT);
					display[2] = new CardDisplayPanel(Image.Orientation.UP);
					display[3] = new CardDisplayPanel(Image.Orientation.UP);
					display[4] = new CardDisplayPanel(Image.Orientation.LEFT);
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
				display[d].setEnabled(false);
				display[d].setDrawContainingPanel(true);
				display[d].setFillColor(null);
			}
			
			CardDisplayPanel deckPlaceHolder = new CardDisplayPanel(Image.Orientation.UP);
			
			deck = new CardWidget(0, 0, false);
			discard = new CardWidget(0, 0, false);
			discard.setMoveOnHover(false);
			
			deckPlaceHolder.addCard(deck);
			deckPlaceHolder.addCard(discard);
			
			switch(numPlayers) {
				case 2:
					addWidget(display[0], 17, 75, 66, 23);
					addWidget(display[1], 17, 2, 66, 23);
					break;
				case 3:
					addWidget(display[0], 25, 75, 50, 23);
					addWidget(display[1], 2, 15, 13, 70);
					addWidget(display[2], 85, 15, 13, 70);
					break;
				case 4:
					addWidget(display[0], 25, 75, 50, 23);
					addWidget(display[1], 2, 15, 13, 70);
					addWidget(display[2], 25, 2, 50, 23);
					addWidget(display[3], 85, 15, 13, 70);
					break;
				case 5:
					addWidget(display[0], 25, 75, 50, 23);
					addWidget(display[1], 2, 30, 13, 65);
					addWidget(display[2], 2, 2, 45, 20);
					addWidget(display[3], 53, 2, 45, 20);
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
			
			endTurn = new Button("End Turn");
			endTurn.setEnabled(false);
			
			exitGame = new Button("Exit Game");
			exitGame.setFillColor(Color.RED);
			exitGame.setBorderColor(Color.WHITE);
			exitGame.setTextColor(Color.WHITE);
			exitGame.setFont(new Font("Arial", Font.PLAIN, 10));
			
			addWidget(messageScrollBox, 3, 3, 94, 45);
			addWidget(descriptionBox, 3, 50, 94, 20);	
			addWidget(endTurn, 20, 85, 60, 8);
			addWidget(exitGame, 77, 97, 18, 2);
		}};
		
		
		addWidget(mainGamePanel, 0, 0, 80, 100, 1);
		addWidget(sidePanel, 80, 0, 20, 100, 0);
		
	}
	
	public void update() {
		super.update();
				
		handleDescriptionBox();
		
		findCardInteraction();
		handleDeck();
		handleHand();
		
		checkTurnChange();
		
		handleExitGame();
	}
	
	private void findCardInteraction() {
		interactionCard = null;
		interactionIndex = -1;
		interactionPlayer = -1;
		
		outer: for (int i=0; i<numPlayers; i++) {
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
	
	private void checkTurnChange() {
		if (endTurn.isClicked()) {
			changeTurn();
		}
	}
	private void changeTurn() {
		display[currentTurn].setEnabled(false);
		endTurn.setEnabled(false);
		deck.setEnabled(true);
		turnStart = true;
		
		currentTurn += 1;
		if (currentTurn == numPlayers) {
			currentTurn = 0;
		}
		messageScrollBox.addRepeatedTextLine("-");
		messageScrollBox.addLine("It is now Player " + (currentTurn+1) + "'s turn.");
	}
	
	private void handleExitGame() {
		if (exitGame.isClicked()) {
			controller.mainScreen();
		}
	}
	
	private void handleDescriptionBox() {
		if (currentOverlay == null) {
			if (interactionCard != null) {
				descriptionBox.setDisplay(interactionCard.getType(), interactionCard.getValue());
			} else {
				descriptionBox.clearDisplay();
			}
		}
	}
	
	private void handleDeck() {
		if (deck.isClicked()) {
			display[currentTurn].setEnabled(true);
			deck.setEnabled(false);
			endTurn.setEnabled(true);
			turnStart = false;
			int[] v = CardData.ALL_CARD_VALUES[(int)(Math.random()*CardData.ALL_CARD_VALUES.length)];
			display[currentTurn].addCard(new CardWidget(v[0], v[1]));
		}
	}
	
	private void handleHand() {
		if (interactionPlayer == currentTurn && interactionCard.isClicked()) {
			int type = interactionCard.getType();
			int value = interactionCard.getValue();
			
			messageScrollBox.addLine("Player " + (currentTurn+1) + " played " + CardData.getCardName(type, value));
			discard.setCard(type, value);
			discard.setFaceUp(true);
			display[interactionPlayer].removeIndex(interactionIndex);
		}
	}
}
