package view.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.*;

import controller.GameController;
import controller.network.Client;
import controller.rulesengine.UpdateEngine;
import controller.rulesengine.Validator;
import model.Card;
import model.CardData;
import model.Flag;
import model.GameState;
import model.Player;
import model.Type;
import simple.gui.*;
import simple.gui.panel.*;
import simple.gui.scrollbox.ScrollDialogBox;
import view.*;
import view.panel.overlay.*;
import view.utilitypanel.*;

public class GamePanel extends ScaledPanel {
	public enum OverlayCommand {
		START_TOURNAMENT, WIN_TOKEN, UNHORSE, CHANGE_WEAPON, IVANHOE, BREAK_LANCE, RIPOSTE, DODGE, RETREAT, KNOCKDOWN
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
	String[] ivanhoeOverlayCommand;
	
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
			if (realPlayerIndex != game.getTurn()) {
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
		if (realPlayerIndex == game.getTurn()) {
			messageScrollBox.addLine("The first player is you.");
		} else {
			messageScrollBox.addLine("The first player is " + playerNames.get(game.getTurn()));
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
		
		for (int i=0; i<display.length; i++) {
			if (game.getAllPlayers().get(toGameTurn(i)).isInTournament()) {
				CardDisplayPanel p = display[i];
				draw.setColors(new Color(255, 255, 255, 30), null);
				draw.rect(p.getX()-3, p.getY()-3, p.getWidth()+3, p.getHeight()+3);
			}
		}
		
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
				// After drawing, if a player can start a tournament
				case Flag.CAN_START_TOURNAMENT : {
					switch(command[1]) {
						case "true":
							if (guiTurn == THIS_PLAYER) {
								// Allows player to start a new tournament
								messageScrollBox.addLine(" > You can start a new tournament");
								prepareOverlay(OverlayCommand.START_TOURNAMENT, -1);
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " can start a tournament");
							}
							break;
						case "false":
							if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You cannot start a new tournament");
								client.sendMessage(Flag.END_TURN);
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " cannot start a new tournament");
							}
							break;
					
					}
					break;
				}
				// Drawing a card from the deck
				case Flag.DRAW_CARD: {
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
				case Flag.END_TURN: {
					messageScrollBox.addRepeatedTextLine("-");
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > It is now your turn");
						deck.setEnabled(true);
						endTurn.setText("(draw card first)");
					} else {
						messageScrollBox.addLine(" > It is now " + playerNames.get(gameTurn) + "'s turn");
						for (Widget w: hand[THIS_PLAYER].getWidgetList()) {
							w.setEnabled(false);
						}
					}
					break;
				}
				case Flag.WITHDRAW: {
					int lastTurn = Integer.parseInt(command[1]);
					if (lastTurn == realPlayerIndex) {
						messageScrollBox.addLine(" > You withdrew from the tournament");
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(lastTurn) + " withdrew from the tournament");
					}
					display[toGUITurn(lastTurn)].clear();
					playerStatus[lastTurn].clearStatus();
					break;
				}
				case Flag.START_TOURNAMENT: {
					//messageScrollBox.addRepeatedTextLine("* ");
					messageScrollBox.addLine("-- New Tournament --");
					//messageScrollBox.addRepeatedTextLine("* ");
					if (guiTurn == THIS_PLAYER) {
						boolean[] canPlay = Validator.cardsAbleToStart(game);
						for (int i=0; i<canPlay.length; i++) {
							hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
						}
					}
					break;
				}
				case Flag.END_TOURNAMENT: {
					int type = Integer.parseInt(command[1]);
					String colour = Type.toString(type);
					
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > You won the tournament!");
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " won the tournament!");
					}
					tournamentColourBar.disableAllTokens();
					for (CardDisplayPanel cp: display) {
						cp.clear();
					}
					for (int i=0; i<playerStatus.length; i++) {
						playerStatus[i].setDisplayValue(0);
						playerStatus[i].clearStatus();
						for (int j=0; j<5; j++) {
							if (!game.getPlayer(i).checkToken(j)) {
								playerStatus[i].removeToken(j);
							}
						}
					}
					break;
				}
				case Flag.PICK_TOKEN: {
					if (guiTurn == THIS_PLAYER) {
						prepareOverlay(OverlayCommand.WIN_TOKEN, -1);
					}
					break;
				}
				case Flag.AWARD_TOKEN: {
					if (guiTurn == THIS_PLAYER) {
						deck.setEnabled(true);
						endTurn.setText("(draw card first)");
					}
					int colour = Integer.parseInt(command[1]);
					String colourName = Type.toString(colour);
					
					playerStatus[gameTurn].collectToken(colour);
					tournamentColourBar.disableAllTokens();
					
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > You collected a " + colourName + " token");
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " collected a " + colourName + " token");
					}
					break;
				}
				case Flag.SET_COLOUR: {
					int type = Integer.parseInt(command[1]);
					String colour = Type.toString(type);
					
					messageScrollBox.addLine(" > Tournament colour was set to " + colour);
					tournamentColourBar.disableAllTokens();
					tournamentColourBar.enableToken(type);
					tournamentColour = type;
					break;
				}
				case Flag.ACTION_CARD: {
					int cardPos = Integer.parseInt(command[1]);
					int type = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getType();
					int value = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getValue();
					
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > You played: " + CardData.getCardName(type, value));
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " played: " + CardData.getCardName(type, value));
					}
					
					discard.setCard(type, value);
					discard.setFaceUp(true);
					
					hand[guiTurn].removeIndex(cardPos);
					
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
						case Card.BREAK_LANCE: {
							int targetIndex = toGUITurn(Integer.parseInt(command[2]));
							
							if (targetIndex == THIS_PLAYER) {
								messageScrollBox.addLine(" > Your PURPLE cards were removed");
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(targetIndex) + "'s PURPLE cards were removed");
							}
							
							display[targetIndex].clear();
							for (Card c: game.getAllPlayers().get(toGameTurn(targetIndex)).getDisplay().getAllCards()) {
								display[targetIndex].addCard(new CardWidget(c.getCardType(), c.getCardValue()));
							}
							
							for (int i=0; i<numPlayers; i++) {
								playerStatus[i].setDisplayValue(game.getAllPlayers().get(i).getDisplayValue(tournamentColour));
							}
							break;
						}
						case Card.RIPOSTE: {
							int targetIndex = toGUITurn(Integer.parseInt(command[2]));
							CardWidget stolenCard = (CardWidget)display[targetIndex].removeIndex(display[targetIndex].numWidgets()-1);
							
							if (targetIndex == THIS_PLAYER) {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " stole your " + CardData.getCardName(stolenCard.getType(),
																																	  stolenCard.getValue()));
							} else if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You stole " + playerNames.get(toGameTurn(targetIndex)) + "'s " + CardData.getCardName(stolenCard.getType(),
										  																							   stolenCard.getValue()));
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(guiTurn) + " stole " + playerNames.get(toGameTurn(targetIndex)) + "'s " 
											+ CardData.getCardName(stolenCard.getType(),
										   			               stolenCard.getValue()));
							}
							
							display[guiTurn].addCard(stolenCard);
							
							for (int i=0; i<numPlayers; i++) {
								playerStatus[i].setDisplayValue(game.getAllPlayers().get(i).getDisplayValue(tournamentColour));
							}
							break;
						}
						case Card.DODGE: {
							int targetIndex = toGUITurn(Integer.parseInt(command[2]));
							int targetCardIndex = Integer.parseInt(command[3]);
							CardWidget discardCard = (CardWidget)display[targetIndex].removeIndex(targetCardIndex);
						
							if (targetIndex == THIS_PLAYER) {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " discarded your " + CardData.getCardName(discardCard.getType(),
																																	   discardCard.getValue()));
							} else if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You discarded " + playerNames.get(toGameTurn(targetIndex)) + "'s " + CardData.getCardName(discardCard.getType(),
										                         																			           discardCard.getValue()));
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(guiTurn) + " discarded " + playerNames.get(toGameTurn(targetIndex)) + "'s " 
											+ CardData.getCardName(discardCard.getType(),
																   discardCard.getValue()));
							}
														
							playerStatus[toGameTurn(targetCardIndex)].setDisplayValue(game.getPlayer(toGameTurn(targetCardIndex)).getDisplayValue(tournamentColour));
							break;
						}
						case Card.RETREAT: {
							int targetCardIndex = Integer.parseInt(command[2]);
							CardWidget discardCard = (CardWidget)display[guiTurn].removeIndex(targetCardIndex);
							hand[guiTurn].addCard(discardCard);
							
							if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You returned your " + CardData.getCardName(discardCard.getType(), discardCard.getValue()) + " to your hand");
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " returned their " + CardData.getCardName(discardCard.getType(), discardCard.getValue()) + " to their hand");
							}
							
							playerStatus[gameTurn].setDisplayValue(game.getPlayer(gameTurn).getDisplayValue(tournamentColour));
							break;
						}
						case Card.KNOCKDOWN: {
							int targetIndex = Integer.parseInt(command[2]);
							int targetCardIndex = Integer.parseInt(command[3]);
							
							CardWidget stolenCard = (CardWidget)hand[toGUITurn(targetIndex)].removeIndex(targetCardIndex);
							
							if (toGUITurn(targetIndex) == THIS_PLAYER) {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " took " + CardData.getCardName(stolenCard.getType(), stolenCard.getValue())
										+ " from your hand");
							} else if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You took " +  CardData.getCardName(stolenCard.getType(), stolenCard.getValue()) 
										+ " from " + playerNames.get(targetIndex) + "'s hand");
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " took a card from " + playerNames.get(targetIndex) + "'s hand");
							}
							
							hand[guiTurn].addCard(stolenCard);
							
							break;
						}
						default: {
							break;
						}
					}
					if (guiTurn == THIS_PLAYER) {
						hand[THIS_PLAYER].setEnabled(true);
						
						endTurn.setEnabled(true);
						if (game.playerCanContinue(toGameTurn(0))) {
							endTurn.setText("End Turn");
						}
						boolean[] canPlay = Validator.cardsAbleToPlay(game);
						for (int i=0; i<canPlay.length; i++) {
							hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
						}
					}
					break;
				}
				case Flag.IVANHOE_PLAYED: {
					CardWidget blockedCard = hand[guiTurn].removeIndex(Integer.parseInt(command[1]));
					
					int ivanhoeIndex = -1;
					int ivanhoePlayer = Integer.parseInt(command[2]);
					for (Widget w: hand[toGUITurn(ivanhoePlayer)].getWidgetList()) {
						ivanhoeIndex++;
						
						CardWidget c = (CardWidget)w;
						if (c.getType() == Type.ACTION && c.getValue() == Card.IVANHOE) {
							break;
						}
					}
					hand[toGUITurn(ivanhoePlayer)].removeIndex(ivanhoeIndex);
					
					if (guiTurn == THIS_PLAYER) {
						hand[THIS_PLAYER].setEnabled(true);
						
						endTurn.setEnabled(true);
						if (game.playerCanContinue(toGameTurn(0))) {
							endTurn.setText("End Turn");
						}
						boolean[] canPlay = Validator.cardsAbleToPlay(game);
						for (int i=0; i<canPlay.length; i++) {
							hand[guiTurn].getIndex(i).setEnabled(canPlay[i]);
						}
					}
					
					if (guiTurn == THIS_PLAYER) {
						messageScrollBox.addLine(" > " + playerNames.get(ivanhoePlayer) + " blocked your " 
								+ CardData.getCardName(blockedCard.getType(), blockedCard.getValue()) + " with IVANHOE");
					} else if (toGUITurn(ivanhoePlayer) == THIS_PLAYER) {
						messageScrollBox.addLine(" > You blocked " + playerNames.get(gameTurn) + "'s " 
								+ CardData.getCardName(blockedCard.getType(), blockedCard.getValue()) + " with IVANHOE");
					} else {
						messageScrollBox.addLine(" > " + playerNames.get(ivanhoePlayer) + " blocked " + playerNames.get(gameTurn) + "'s "
								+ CardData.getCardName(blockedCard.getType(), blockedCard.getValue()) + " with IVANHOE");
					}
					break;
				}
	
				case Flag.CARD: {
					int cardPos = Integer.parseInt(command[1]);
					int type = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getType();
					int value = ((CardWidget)hand[guiTurn].getIndex(cardPos)).getValue();
					
					switch(type) {
						case Type.ACTION: {		
							if (guiTurn != THIS_PLAYER) {
								prepareOverlay(OverlayCommand.IVANHOE, -1);
								ivanhoeOverlayCommand = command;
							}
							break;
						}
						default: {
							hand[guiTurn].removeIndex(cardPos);
							
							if (guiTurn == THIS_PLAYER) {
								messageScrollBox.addLine(" > You played: " + CardData.getCardName(type, value));
							} else {
								messageScrollBox.addLine(" > " + playerNames.get(gameTurn) + " played: " + CardData.getCardName(type, value));
							}
							
							display[guiTurn].addCard(new CardWidget(type, value));
							playerStatus[gameTurn].setDisplayValue(game.getAllPlayers().get(gameTurn).getDisplayValue(tournamentColour));
							if (guiTurn == THIS_PLAYER) {
								hand[THIS_PLAYER].setEnabled(true);
								
								endTurn.setEnabled(true);
								if (game.playerCanContinue(toGameTurn(0))) {
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
			client.sendMessage(Flag.END_TURN);
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
			client.sendMessage(Flag.DRAW_CARD);
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
								prepareOverlay(OverlayCommand.UNHORSE, i);
								break;
							}
							case Card.CHANGE_WEAPON: {
								prepareOverlay(OverlayCommand.CHANGE_WEAPON, i);
								break;
							}
							case Card.DROP_WEAPON: {
								client.sendMessage(UpdateEngine.dropWeapon(game, i));
								messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
								break;
							}
							case Card.BREAK_LANCE: {
								prepareOverlay(OverlayCommand.BREAK_LANCE, i);
								break;
							}
							case Card.RIPOSTE: {
								prepareOverlay(OverlayCommand.RIPOSTE, i);
								break;
							}
							case Card.DODGE: {
								prepareOverlay(OverlayCommand.DODGE, i);
								break;
							}
							case Card.RETREAT: {
								prepareOverlay(OverlayCommand.RETREAT, i);
								break;
							}
							case Card.KNOCKDOWN: {
								prepareOverlay(OverlayCommand.KNOCKDOWN, i);
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
				endTurn.setEnabled(false);
				hand[THIS_PLAYER].setEnabled(false);
				break;
			}
		}
	}

	private void prepareOverlay(OverlayCommand oc, int cardReferenceIndex) {
		startNewOverlay = true;
		overlayCommand = oc;
		overlayCardReferenceIndex = cardReferenceIndex;
	}
	private void handleOverlay() {
		// Checks if a new overlay needs to start
		if (startNewOverlay) {
			switch(overlayCommand) {
				// A new tournament is starting, and you are starting the tournament
				case START_TOURNAMENT:
					currentOverlay = new NewTournamentOverlay(descriptionBox, game, realPlayerIndex, hand[THIS_PLAYER]);
					break;
				case WIN_TOKEN:
					currentOverlay = new WinningTokenOverlay(descriptionBox, game, realPlayerIndex);
					break;
				case UNHORSE:
					currentOverlay = new UnhorseOverlay(descriptionBox, game, realPlayerIndex, hand[THIS_PLAYER]);
					break;
				case CHANGE_WEAPON:
					currentOverlay = new ChangeWeaponOverlay(descriptionBox, game, realPlayerIndex, hand[THIS_PLAYER]);
					break;
				case IVANHOE:
					currentOverlay = new IvanhoeOverlay(descriptionBox, ivanhoeOverlayCommand, game, toGameTurn(THIS_PLAYER));
					break;
				case BREAK_LANCE:
					currentOverlay = new BreakLanceOverlay(descriptionBox, game, realPlayerIndex, display);
					break;
				case RIPOSTE: 
					currentOverlay = new RiposteOverlay(descriptionBox, game, realPlayerIndex, display);
					break;
				case DODGE:
					currentOverlay = new DodgeOverlay(descriptionBox, game, realPlayerIndex, display);
					break;
				case RETREAT:
					currentOverlay = new RetreatOverlay(descriptionBox, game, realPlayerIndex, display[THIS_PLAYER]);
					break;
				case KNOCKDOWN:
					currentOverlay = new KnockdownOverlay(descriptionBox, game, realPlayerIndex, hand);
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
				System.out.println(Arrays.toString(result));
				switch(overlayCommand) {
					case START_TOURNAMENT:
						client.sendMessage(Flag.SET_COLOUR + ":" + result[0] + Flag.NEW_COM + Flag.START_TOURNAMENT);
						break;
					case WIN_TOKEN:
						client.sendMessage(UpdateEngine.awardToken(game, result[0]));
						break;
					case UNHORSE:
						client.sendMessage(UpdateEngine.unhorse(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case CHANGE_WEAPON:
						client.sendMessage(UpdateEngine.changeWeapon(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case BREAK_LANCE:
						client.sendMessage(UpdateEngine.breakLance(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case RIPOSTE:
						client.sendMessage(UpdateEngine.riposte(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case DODGE:
						client.sendMessage(UpdateEngine.dodge(game, overlayCardReferenceIndex, Integer.parseInt(result[0]), Integer.parseInt(result[1])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case RETREAT:
						client.sendMessage(UpdateEngine.retreat(game, overlayCardReferenceIndex, Integer.parseInt(result[0])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case KNOCKDOWN:
						client.sendMessage(UpdateEngine.knockdown(game, overlayCardReferenceIndex, Integer.parseInt(result[0]), Integer.parseInt(result[1])));
						messageScrollBox.addRepeatedTextLine("** Wait for opponent response **");
						break;
					case IVANHOE:
						client.sendMessage(UpdateEngine.ivanhoe(game, result[0].equals("true")));
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
