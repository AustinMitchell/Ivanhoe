package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import models.Card;
import models.GameState;
import models.Player;
import network.Client;
import rulesengine.RulesEngine;
import rulesengine.Type;
import rulesengine.UpdateEngine;
import rulesengine.Validator;
import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.scrollbox.ScrollDialogBox;
import ui.*;
import ui.panel.overlay.*;
import ui.utilitypanel.*;

public class GamePanel extends ScaledPanel {
	public enum OverlayCommand {
		START_TOURNAMENT, UNHORSE, CHANGE_WEAPON
	}
	
	public static final int THIS_PLAYER = 0;
	
	GameController controller;
	Client client;
	GameState game;
	
	CardDisplayPanel[] hand, display;
	StatusBar[] playerStatus;
	Label[] displayValue;
	ScrollDialogBox messageScrollBox;
	DescriptionBox descriptionBox;
	ScaledPanel mainGamePanel, sidePanel;
	CardWidget deck, discard;
	Image emptyDiscard;
	Button endTurn;
	Label tournamentColourLabel;
	TokenBar tournamentColourBar;
	
	OverlayPanel currentOverlay;
	OverlayCommand overlayCommand;
	boolean startNewOverlay;
	int overlayCardReferenceIndex;
	
	boolean firstTournamentPlay;
	
	ArrayList<String> playerNames;
	
	int numPlayers;
	int realPlayerIndex;
	
	int tournamentColour;
	
	CardWidget interactionCard;
	int interactionPlayer;
	int interactionIndex;
	
	int currentTurn;
	boolean turnStart;

	public GamePanel(GameController controller, final ArrayList<String> playerNames) {
		super();
		
		this.controller = controller;
		this.playerNames = playerNames;
		client = controller.getClient();
		game = client.getGame();
		realPlayerIndex = client.getID();
		
		numPlayers = playerNames.size();
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
					throw new RuntimeException("Initialized game with a bad number of players (not within 2 and 5)");
			}
			
			for (int i=0; i<hand.length; i++) {
				for (Card c: game.getAllPlayers().get(toGameTurn(i)).getHand().getAllCards()) {
					CardWidget cw = new CardWidget(c.getCardType(), c.getCardValue());
					hand[i].addCard(cw);
				}
				if (i != THIS_PLAYER) {
					// Opponent hands won't pop up on hover
					hand[i].setCardMoveOnHover(false);
				} else {
					// Disables your hand by default
					for (Widget w: hand[i].getWidgetList()) {
						w.setEnabled(false);
					}
				}
			}
			
			
			// Panel to get the deck and discard in the right spot. Panel background is invisible
			CardDisplayPanel deckPlaceHolder = new CardDisplayPanel(Image.Orientation.UP, false);
			
			// Card widget for the deck
			deck = new CardWidget(0, 0, false);
			if (realPlayerIndex != THIS_PLAYER) {
				deck.setEnabled(false);
			}
			
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
			messageScrollBox.setFont(new Font("Courier", Font.PLAIN, 14));
			
			tournamentColourLabel = new Label("Tournament Colour");
			tournamentColourLabel.setFont(new Font("Arial", Font.PLAIN, 13));
			tournamentColourBar = new TokenBar();
			
			descriptionBox = new DescriptionBox();
			
			ScaledPanel playerStatusPanel = new ScaledPanel(1, numPlayers) {{
				playerStatus = new StatusBar[numPlayers];
				for (int i=0; i<numPlayers; i++) {
					if (i == realPlayerIndex) {
						playerStatus[i] = new StatusBar(playerNames.get(i) + " (you)");
					} else {
						playerStatus[i] = new StatusBar(playerNames.get(i));
					}
					addWidget(playerStatus[i], 0, i, 1, 1);
				}
			}};
			
			endTurn = new Button("End Turn");
			endTurn.setEnabled(false);
			
			addWidget(messageScrollBox,      3,  1, 94, 32);
			addWidget(tournamentColourLabel, 0, 34, 30,  5);
			addWidget(tournamentColourBar,  30, 34, 67,  5);
			addWidget(descriptionBox, 	     3, 40, 94, 20);
			addWidget(playerStatusPanel,     3, 62, 94, 23);
			addWidget(endTurn, 		        20, 87, 60,  8);			
			
		}};		
		
		
		addWidget(mainGamePanel, 0, 0, 80, 100, 1);
		addWidget(sidePanel, 	80, 0, 20, 100, 0);
		
		messageScrollBox.addLine("Welcome to IVANHOE");
		if (realPlayerIndex == THIS_PLAYER) {
			messageScrollBox.addLine("The first player is you.");
		} else {
			messageScrollBox.addLine("The first player is " + playerNames.get(0));
		}
	}
	
	@Override
	public void update() {
		super.update();
				
		handleClientMessages();
		
		handleDescriptionBox();
		
		// Check this player interaction
		findCardInteraction();
		handleDeckObject();
		handleHand();
		handleEndTurn();
		
		// Handles any overlay stuff
		handleOverlay();
	}
	@Override
	public void draw() {
		// Display panel for the current player's turn
		CardDisplayPanel p = display[toGUITurn(game.getTurn())];
		draw.setColors(null, new Color(0, 200, 200), 4);
		draw.rect(p.getX()-3, p.getY()-3, p.getWidth()+3, p.getHeight()+3);
		
		super.draw();
		
		StatusBar s = playerStatus[game.getTurn()];
		draw.setColors(null, new Color(0, 200, 200), 2);
		draw.rect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
	}
	
	private int toGameTurn(int turn) {
		return Math.floorMod((turn+realPlayerIndex), numPlayers);
	}
	private int toGUITurn(int turn) {
		return Math.floorMod((turn-realPlayerIndex), numPlayers);
	}
	
	// Bulk of the game logic. This reads the messages that will affect the GameState object, and adjusts the UI models accordingly
	private void handleClientMessages() {
		if (client.hasFlags()) {
			String commandString = client.readGuiFlag();
			String[] command = commandString.split(":");
			int gameTurn = game.getTurn();
			int guiTurn = toGUITurn(gameTurn);
			Player player = client.getGame().getAllPlayers().get(gameTurn);
			switch(command[0]) {
				// Not yet implemented
				case  "startGame": {
					break;
				}
				// After drawing, if a player can start a tournament
				case "canStartTournament" : {
					switch(command[1]) {
						case "true":
							if (guiTurn == THIS_PLAYER) {
								// Allows player to start a new tournament
								messageScrollBox.addLine(" > You can start a new tournament");
								prepareOverlay(OverlayCommand.START_TOURNAMENT);
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " can start a tournament");
							}
							break;
						case "false":
							if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You cannot start a new tournament");
								client.sendMessage("endTurn:false");
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " cannot start a new tournament");
							}
							break;
					
					}
					break;
				}
				// Drawing a card from the deck
				case "drawCard": {
					Card newCard = player.getHand().getCard(player.getHand().deckSize()-1);
					CardWidget newCardWidget = new CardWidget(newCard.getCardType(), newCard.getCardValue());
					hand[guiTurn].addCard(newCardWidget);
					if (guiTurn == THIS_PLAYER) {
						newCardWidget.setEnabled(false);
						if (game.hasTournamentStarted()) {
							hand[THIS_PLAYER].setEnabled(true);
							endTurn.setEnabled(true);
							endTurn.setText("Withdraw");
						} 
					}
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > You drew: " + CardData.getCardName(newCard.getCardType(), newCard.getCardValue()));
						if (game.hasTournamentStarted()) {
							boolean[] canPlay = Validator.cardsAbleToPlay(game);
							for (int i=0; i<canPlay.length; i++) {
								hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
							}
						}
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " drew a card");
					}
					break;
				}
				case "endTurn": {
					if (command[1].equals("true")) {
						int lastTurn = Math.floorMod(gameTurn-1, numPlayers);
						if (lastTurn == realPlayerIndex) {
							messageScrollBox.addLine("You withdrew from the tournament");
						} else {
							messageScrollBox.addLine(playerNames.get(lastTurn) + " withdrew from the tournament");
						}
						display[toGUITurn(lastTurn)].clear();
						playerStatus[lastTurn].clearStatus();
					}
					messageScrollBox.addRepeatedTextLine("-");
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine("It is now your turn");
						deck.setEnabled(true);
						endTurn.setText("(draw card first)");
					} else {
						messageScrollBox.addLine("It is now " + playerNames.get(gameTurn) + "'s turn");
						for (Widget w: hand[THIS_PLAYER].getWidgetList()) {
							w.setEnabled(false);
						}
					}
					break;
				}
				case "startTournament": {
					messageScrollBox.addRepeatedTextLine("* ");
					messageScrollBox.addLine("-- New Tournament --");
					messageScrollBox.addRepeatedTextLine("* ");
					if (guiTurn == THIS_PLAYER) {
						boolean[] canPlay = Validator.cardsAbleToStart(game);
						for (int i=0; i<canPlay.length; i++) {
							hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
						}
					}
					break;
				}
				case "endTournament": {
					int type = Integer.parseInt(command[1]);
					String colour = Type.toString(type);
					
					messageScrollBox.addRepeatedTextLine("* ");
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine("You won the tournament!");
						messageScrollBox.addLine("You were awarded a " + colour + " token");
					} else {
						messageScrollBox.addLine(playerNames.get(gameTurn) + " won the tournament!");
						messageScrollBox.addLine(playerNames.get(gameTurn) + " was awarded a " + colour + " token");
					}
					messageScrollBox.addRepeatedTextLine("* ");
					playerStatus[gameTurn].collectToken(type);
					tournamentColourBar.disableAllTokens();
					for (CardDisplayPanel cp: display) {
						cp.clear();
					}
					for (StatusBar sb: playerStatus) {
						sb.setDisplayValue(0);
						sb.clearStatus();
					}
					break;
				}
				case "setColour": {
					int type = Integer.parseInt(command[1]);
					String colour = Type.toString(type);
					
					messageScrollBox.addLine(" > Tournament colour was set to " + colour);
					tournamentColourBar.disableAllTokens();
					tournamentColourBar.enableToken(type);
					tournamentColour = type;
					break;
				}
				case "card": {
					int cardPos = Integer.parseInt(command[1]);
					int type = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getType();
					int value = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getValue();
					
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > You played: " + CardData.getCardName(type, value));
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " played: " + CardData.getCardName(type, value));
					}
					
					hand[guiTurn].removeIndex(cardPos);
					switch(type) {
						case Type.ACTION: {							
							discard.setCard(type, value);
							discard.setFaceUp(true);
							switch (value) {
								case Card.UNHORSE: {
									messageScrollBox.addLine(" > Tournament colour was set to " + Type.toString(Integer.parseInt(command[2])));
									tournamentColourBar.disableAllTokens();
									tournamentColourBar.enableToken(Integer.parseInt(command[2]));
									tournamentColour = Integer.parseInt(command[2]);
									break;
								}
								case Card.CHANGE_WEAPON: {
									messageScrollBox.addLine(" > Tournament colour was set to " + Type.toString(Integer.parseInt(command[2])));
									tournamentColourBar.disableAllTokens();
									tournamentColourBar.enableToken(Integer.parseInt(command[2]));
									tournamentColour = Integer.parseInt(command[2]);
									break;
								}
								case Card.DROP_WEAPON: {
									messageScrollBox.addLine(" > Tournament colour was set to GREEN");
									tournamentColourBar.disableAllTokens();
									tournamentColourBar.enableToken(Type.GREEN);
									tournamentColour = Type.GREEN;
									for (int i=0; i<numPlayers; i++) {
										playerStatus[i].setDisplayValue(game.getAllPlayers().get(i).getDisplayValue(tournamentColour));
									}
									break;
								}
								default: {
									break;
								}
							}
							break;
						}
						default: {
							display[guiTurn].addCard(new CardWidget(type, value));
							playerStatus[gameTurn].setDisplayValue(game.getAllPlayers().get(gameTurn).getDisplayValue(tournamentColour));
							break;
						}
					}
					
					if (guiTurn == THIS_PLAYER) {
						endTurn.setEnabled(true);
						int currentValue = playerStatus[realPlayerIndex].getDisplayValue();
						boolean greatest = true;
						for (int i=0; i<playerStatus.length; i++) {
							if (i != realPlayerIndex && playerStatus[i].getDisplayValue() >= currentValue) {
								greatest = false;
								break;
							}
						}
						if (greatest) {
							endTurn.setText("End Turn");
						}
						boolean[] canPlay = Validator.cardsAbleToPlay(game);
						for (int i=0; i<canPlay.length; i++) {
							hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
						}
					}
					break;
				}
			}
		}
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
					widgetList = display[i].getWidgetList();
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
	
	private void handleEndTurn() {
		if (endTurn.isClicked()) {
			endTurn.setEnabled(false);
			
			int currentValue = playerStatus[realPlayerIndex].getDisplayValue();
			boolean greatest = true;
			for (int i=0; i<playerStatus.length; i++) {
				if (i != realPlayerIndex && playerStatus[i].getDisplayValue() >= currentValue) {
					greatest = false;
					break;
				}
			}
			if (greatest) {
				client.sendMessage("endTurn:false");
			} else {
				client.sendMessage("endTurn:true");
			}
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
	
	private void handleDeckObject() {
		if (deck.isClicked()) {
			deck.setEnabled(false);
			client.sendMessage("drawCard");
		}
	}
	
	private void handleHand() {
		for (int i=0; i<hand[THIS_PLAYER].getWidgetList().size(); i++) {
			CardWidget c = (CardWidget)hand[THIS_PLAYER].getIndex(i);
			if (c.isClicked()) {
				switch (c.getType()) {
					case Type.ACTION: {
						switch (c.getValue()) {
							case Card.UNHORSE: {
								overlayCardReferenceIndex = i;
								prepareOverlay(OverlayCommand.UNHORSE);
								break;
							}
							case Card.CHANGE_WEAPON: {
								overlayCardReferenceIndex = i;
								prepareOverlay(OverlayCommand.CHANGE_WEAPON);
								break;
							}
							case Card.DROP_WEAPON: {
								client.sendMessage(UpdateEngine.dropWeapon(game, i));
								break;
							}
							default: {
								client.sendMessage(UpdateEngine.unimplementedActionCard(game, i));
							}
						}
						break;
					}
					default: {
						client.sendMessage(UpdateEngine.playValueCard(game, i));
						break;
					}
				}
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
					currentOverlay = new NewTournamentOverlay(descriptionBox, hand[THIS_PLAYER], game);
					break;
				case UNHORSE:
					currentOverlay = new UnhorseOverlay(descriptionBox, hand[THIS_PLAYER]);
					break;
				case CHANGE_WEAPON:
					currentOverlay = new ChangeWeaponOverlay(descriptionBox, hand[THIS_PLAYER], game);
					break;
				default:
					break;
			}
			startNewOverlay = false;
			mainGamePanel.addWidget(currentOverlay, 2, 2, 96, 96, 0);
		// Otherwise, check on the status of the current overlay if one exists
		} else if (currentOverlay != null) {
			if (currentOverlay.isOverlayActionComplete()) {
				String[] result = currentOverlay.getFinalCommandString().split(":");
				switch(overlayCommand) {
					case START_TOURNAMENT:
						client.sendMessage("setColour:" + result[0] + RulesEngine.NEW_COM + "startTournament");
						break;
					case UNHORSE:
						client.sendMessage(UpdateEngine.unhorse(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						break;
					case CHANGE_WEAPON:
						client.sendMessage(UpdateEngine.unhorse(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						break;
					default:
						break;
				}
				
				mainGamePanel.removeWidget(currentOverlay);
				currentOverlay = null;
			}
		}
	}
}
