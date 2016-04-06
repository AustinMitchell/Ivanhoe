package view.panel.overlay;

import java.awt.Color;
import java.awt.Font;

import simple.gui.Canvas;
import simple.gui.CustomDraw;
import simple.gui.Label;
import simple.gui.TextArea.Alignment;
import simple.gui.Widget;
import model.*;
import simple.gui.Button;
import simple.gui.Image.Orientation;
import view.CardWidget;
import view.DescriptionBox;
import view.utilitypanel.CardDisplayPanel;
import view.utilitypanel.TokenBar;

public class IvanhoeOverlay extends OverlayPanel {	
	public static final int TIMEOUT = 15000;
	public static final CustomDraw ARROW = new CustomDraw() {
			public void draw(Widget w) {
				draw.setStroke(Color.white, 6);
				draw.line(w.getX(), w.getY() + w.getHeight()/2, w.getX() + w.getWidth(), w.getY() + w.getHeight()/2);
				draw.line(w.getX() + w.getWidth()*3/4, w.getY(), w.getX() + w.getWidth(), w.getY() + w.getHeight()/2);
				draw.line(w.getX() + w.getWidth()*3/4, w.getY() + w.getHeight(), w.getX() + w.getWidth(), w.getY() + w.getHeight()/2);
			}};
	
	String cardName;
	Button playIvanhoe;
	Button doNothing;
	long startTime;
	
	CardWidget actionCard;
	
	public IvanhoeOverlay(DescriptionBox descriptionBox, String[] command, GameState game, int realPlayerIndex) {
		super("", descriptionBox, game, realPlayerIndex);
		Player instigator = game.getAllPlayers().get(game.getTurn());
		int cardType = instigator.getHand().getCard(Integer.parseInt(command[1])).getCardType();
		int cardValue = instigator.getHand().getCard(Integer.parseInt(command[1])).getCardValue();
		
		title.setText(instigator.getName() + " is playing " + CardData.getCardName(cardType, cardValue));
		
		playIvanhoe = new Button("Play Ivanhoe");
		doNothing = new Button("Continue");
		
		boolean hasIvanhoe = false;
		if (game.getAllPlayers().get(realPlayerIndex).isInTournament()) {
			for (int i=0; i<game.getAllPlayers().get(realPlayerIndex).getHand().deckSize(); i++) {
				Card c = game.getAllPlayers().get(realPlayerIndex).getHand().getCard(i);
				if (c.getCardType() == Type.ACTION && c.getCardValue() == Card.IVANHOE) {
					hasIvanhoe = true;
					break;
				}
			}
		}
		
		if (hasIvanhoe) {
			addWidget(doNothing, 39, 90, 8, 8);
			addWidget(playIvanhoe, 61, 90, 8, 8);
		} else {
			addWidget(doNothing, 45, 90, 10, 8);
		}
		
		startTime = System.currentTimeMillis();
		
		Card ac = game.getHand(game.getTurn()).getCard(Integer.parseInt(command[1]));
		actionCard = new CardWidget(ac.getCardType(), ac.getCardValue());
		CardDisplayPanel actionDisplay = new CardDisplayPanel(Orientation.UP, true);
		actionDisplay.addCard(actionCard);
		
		switch(actionCard.getValue()) {
			case Card.UNHORSE:
			case Card.CHANGE_WEAPON: {
				Canvas token = new Canvas();
				token.setCustomDraw(TokenBar.TOKEN_DRAW[Integer.parseInt(command[2])]);
				
				Canvas arrow = new Canvas();
				arrow.setCustomDraw(ARROW);
				
				addWidget(actionDisplay, 32, 35, 10, 30);
				addWidget(arrow, 45, 46, 10, 8);
				addWidget(token, 58, 42, 10, 16);
				break;
			}
			case Card.DROP_WEAPON:
			case Card.OUTMANEUVER:
			case Card.CHARGE:
			case Card.COUNTERCHARGE:
			case Card.DISGRACE:
			case Card.ADAPT:
			case Card.SHIELD: {
				addWidget(actionDisplay, 45, 35, 10, 30);
				break;
			}
			case Card.BREAK_LANCE:
			case Card.RIPOSTE:
			case Card.KNOCKDOWN:
			case Card.STUNNED: {
				Canvas arrow = new Canvas();
				arrow.setCustomDraw(ARROW);
				
				String position = "";
				int targetGUI = toGUITurn(Integer.parseInt(command[2]));
				switch (game.getAllPlayers().size()) {
					case 2:
						switch (targetGUI) {
							case 1:
								position = "";
								break;
						}
						break;
					case 3:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Right)";
								break;
						}
						break;
					case 4:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top)";
								break;
							case 3: 
								position = "(Right)";
								break;
						}
						break;
					case 5:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top-Left)";
								break;
							case 3: 
								position = "(Top-Right)";
								break;
							case 4: 
								position = "(Right)";
								break;
						}
						break;
				}
				if (toGameTurn(targetGUI) == realPlayerIndex) {
					position = "You";
				}
				Label target = new Label(game.getPlayer(Integer.parseInt(command[2])).getName() + " " + position);
				target.setTextColor(Color.white);
				target.setFont(new Font("Arial", Font.PLAIN, 18));
				target.setAlignment(Alignment.WEST);
				
				addWidget(actionDisplay, 32, 35, 10, 30);
				addWidget(arrow, 45, 46, 10, 8);
				addWidget(target, 58, 40, 30, 20);
				break;
			}
			case Card.DODGE: {
				Canvas arrow1 = new Canvas();
				arrow1.setCustomDraw(ARROW);
				
				Canvas arrow2 = new Canvas();
				arrow2.setCustomDraw(ARROW);
				
				String position = "";
				int targetGUI = toGUITurn(Integer.parseInt(command[2]));
				switch (game.getAllPlayers().size()) {
					case 2:
						switch (targetGUI) {
							case 1:
								position = "";
								break;
						}
						break;
					case 3:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Right)";
								break;
						}
						break;
					case 4:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top)";
								break;
							case 3: 
								position = "(Right)";
								break;
						}
						break;
					case 5:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top-Left)";
								break;
							case 3: 
								position = "(Top-Right)";
								break;
							case 4: 
								position = "(Right)";
								break;
						}
						break;
				}
				if (toGameTurn(targetGUI) == realPlayerIndex) {
					position = "You";
				}
				Label target = new Label(game.getPlayer(Integer.parseInt(command[2])).getName() + " " + position);
				target.setTextColor(Color.white);
				target.setFont(new Font("Arial", Font.PLAIN, 18));
				target.setAlignment(Alignment.WEST);
				
				Card c = game.getDisplay(toGameTurn(targetGUI)).getCard(Integer.parseInt(command[3]));
				CardWidget targetCard = new CardWidget(c.getCardType(), c.getCardValue());
				CardDisplayPanel targetDisplay = new CardDisplayPanel(Orientation.UP, true);
				targetDisplay.addCard(targetCard);
				
				addWidget(actionDisplay, 20, 35, 10, 30);
				addWidget(arrow1, 33, 46, 10, 8);
				addWidget(target, 45, 40, 12, 20);
				addWidget(arrow2, 59, 46, 10, 8);
				addWidget(targetDisplay, 71, 35, 10, 30);
				break;
			}
			case Card.RETREAT: {
				Canvas arrow = new Canvas();
				arrow.setCustomDraw(ARROW);
				
				Card c = game.getDisplay(game.getTurn()).getCard(Integer.parseInt(command[2]));
				CardWidget targetCard = new CardWidget(c.getCardType(), c.getCardValue());
				CardDisplayPanel targetDisplay = new CardDisplayPanel(Orientation.UP, true);
				targetDisplay.addCard(targetCard);
				
				addWidget(actionDisplay, 32, 35, 10, 30);
				addWidget(arrow, 45, 46, 10, 8);
				addWidget(targetDisplay, 58, 40, 10, 20);
				break;
			}
			case Card.OUTWIT: {
				Canvas arrow = new Canvas();
				arrow.setCustomDraw(ARROW);
				
				String position = "";
				int targetGUI = toGUITurn(Integer.parseInt(command[4]));
				switch (game.getAllPlayers().size()) {
					case 2:
						switch (targetGUI) {
							case 1:
								position = "";
								break;
						}
						break;
					case 3:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Right)";
								break;
						}
						break;
					case 4:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top)";
								break;
							case 3: 
								position = "(Right)";
								break;
						}
						break;
					case 5:
						switch (targetGUI) {
							case 1:
								position = "(Left)";
								break;
							case 2:
								position = "(Top-Left)";
								break;
							case 3: 
								position = "(Top-Right)";
								break;
							case 4: 
								position = "(Right)";
								break;
						}
						break;
				}
				if (toGameTurn(targetGUI) == realPlayerIndex) {
					position = "You";
				}
				Label target = new Label(game.getPlayer(Integer.parseInt(command[4])).getName() + " " + position);
				target.setTextColor(Color.white);
				target.setFont(new Font("Arial", Font.PLAIN, 18));
				target.setAlignment(Alignment.WEST);
				
				addWidget(actionDisplay, 32, 15, 10, 30);
				addWidget(arrow, 45, 26, 10, 8);
				addWidget(target, 58, 20, 30, 20);
				
				String playerDeck = command[2];
				String targetDeck = command[5];
				CardWidget playerCard, targetCard;
				
				if (playerDeck.equals(Flag.DISPLAY)) {
					Card c = game.getDisplay(game.getTurn()).getCard(Integer.parseInt(command[3]));
					playerCard = new CardWidget(c.getCardType(), c.getCardValue());
				} else if(playerDeck.equals(Flag.SHIELD)) {
					playerCard = new CardWidget(Type.ACTION, Card.SHIELD);
				} else {
					playerCard = new CardWidget(Type.ACTION, Card.STUNNED);
				}
				if (targetDeck.equals(Flag.DISPLAY)) {
					Card c = game.getDisplay(Integer.parseInt(command[4])).getCard(Integer.parseInt(command[6]));
					targetCard = new CardWidget(c.getCardType(), c.getCardValue());
				} else if(playerDeck.equals(Flag.SHIELD)) {
					targetCard = new CardWidget(Type.ACTION, Card.SHIELD);
				} else {
					targetCard = new CardWidget(Type.ACTION, Card.STUNNED);
				}
				
				Label swap = new Label("Swapping for:");
				swap.setTextColor(Color.white);
				swap.setFont(new Font("Arial", Font.PLAIN, 18));
				
				CardDisplayPanel playerDisplay = new CardDisplayPanel(Orientation.UP, true);
				CardDisplayPanel targetDisplay = new CardDisplayPanel(Orientation.UP, true);
				playerDisplay.addCard(playerCard);
				targetDisplay.addCard(targetCard);
				
				addWidget(playerDisplay, 32, 55, 10, 30);
				addWidget(swap, 45, 66, 10, 8);
				addWidget(targetDisplay, 58, 55, 10, 30);
				
				break;
			}
		}
	}

	@Override
	public void handleDescriptionBox() {
		if (actionCard.isHovering()) {
			descriptionBox.setDisplay(actionCard.getType(), actionCard.getValue());
		}
	}
	
	@Override
	public void update() {
		super.update();
		if (!overlayActionComplete) {
			if (System.currentTimeMillis() - startTime > TIMEOUT) {
				finalCommandString = "false";
				overlayActionComplete = true;
			}
			
			if (doNothing.isClicked()) {	
				finalCommandString = "false";
				overlayActionComplete = true;
			} else if (playIvanhoe.isClicked()) {
				finalCommandString = "true";
				overlayActionComplete = true;
			}
		}
	}
}
