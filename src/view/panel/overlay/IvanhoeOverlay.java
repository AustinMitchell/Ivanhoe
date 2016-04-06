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
				break;
			}
			case Card.RETREAT: {
				break;
			}
			case Card.OUTWIT: {
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
			if (System.currentTimeMillis() - startTime > 8000) {
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
