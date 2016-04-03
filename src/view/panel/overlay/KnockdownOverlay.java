package view.panel.overlay;

import java.util.ArrayList;

import model.GameState;
import controller.rulesengine.Validator;
import simple.gui.Widget;
import view.CardWidget;
import view.DescriptionBox;
import view.utilitypanel.CardDisplayPanel;

public class KnockdownOverlay extends OverlayPanel {
	private CardDisplayPanel[] hand;
	
	public KnockdownOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel[] gameHand) {
		super("Knockdown - Select opponent hand...", descriptionBox, game, realPlayerIndex);
		
		removeWidget(title);
		addWidget(title, 0, 0, 100, 90);
		
		ArrayList<Boolean> validPlayers = Validator.validateKnockdown(game);
		
		hand = new CardDisplayPanel[gameHand.length];
		for (int i=0; i<hand.length; i++) {
			hand[i] = new CardDisplayPanel(gameHand[i].getOrientation(), gameHand[i].isFaceUp());
			for (Widget w: gameHand[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				hand[i].addCard(new CardWidget(c.getType(), c.getValue()));
			}
			hand[i].setEnabled(validPlayers.get(toGameTurn(i)));
		}
		
		switch(hand.length) {
			case 2:
				addWidget(hand[0], 17, 72, 66, 23);
				addWidget(hand[1], 17,  3, 66, 23);
				break;
			case 3:
				addWidget(hand[0], 25, 72, 50, 23);
				addWidget(hand[1],  2, 15, 13, 70);
				addWidget(hand[2], 85, 15, 13, 70);
				break;
			case 4:
				addWidget(hand[0], 25, 72, 50, 23);
				addWidget(hand[1],  2, 15, 13, 70);
				addWidget(hand[2], 25,  3, 50, 23);
				addWidget(hand[3], 85, 15, 13, 70);
				break;
			case 5:
				addWidget(hand[0], 25, 72, 50, 23);
				addWidget(hand[1],  2, 30, 13, 65);
				addWidget(hand[2],  2,  3, 45, 20);
				addWidget(hand[3], 53,  3, 45, 20);
				addWidget(hand[4], 85, 30, 13, 65);
				break;
		}
	}

	@Override
	public void handleDescriptionBox() {
		for (CardDisplayPanel h: hand) {
			for (Widget w: h.getWidgetList()) {
				CardWidget c = (CardWidget)w;
				if (c.isHovering() && c.isFaceUp()) {
					descriptionBox.setDisplay(c.getType(), c.getValue());
				}
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		for (int i=0; i<hand.length; i++) {
			for (int j=0; j<hand[i].getWidgetList().size(); j++) {
				if (hand[i].getIndex(j).isClicked()) {
					overlayActionComplete = true;
					finalCommandString = "" + toGameTurn(i) + ":" + j;
				}
			}
		}
	}
}
