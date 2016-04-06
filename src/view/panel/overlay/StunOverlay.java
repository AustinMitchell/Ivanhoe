package view.panel.overlay;

import model.GameState;
import simple.gui.Widget;
import view.CardWidget;
import view.DescriptionBox;
import view.utilitypanel.CardDisplayPanel;

public class StunOverlay extends OverlayPanel {
	CardDisplayPanel[] hand, display;
	
	public StunOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel[] gameHand, CardDisplayPanel[] gameDisplay) {
		super("Stun - Select an opponent to stun...", descriptionBox, game, realPlayerIndex);
		
		removeWidget(title);
		addWidget(title, 0, 0, 100, 90);
		
		hand = new CardDisplayPanel[gameHand.length];
		display = new CardDisplayPanel[gameDisplay.length];
		
		for (int i=0; i<gameHand.length; i++) {
			hand[i] = new CardDisplayPanel(gameHand[i].getOrientation(), gameHand[i].isFaceUp());
			display[i] = new CardDisplayPanel(gameDisplay[i].getOrientation(), true);
			
			for (Widget w: gameHand[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				hand[i].addCard(new CardWidget(c.getType(), c.getValue()));
			}
			for (Widget w: gameDisplay[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				display[i].addCard(new CardWidget(c.getType(), c.getValue()));
			}
			
		}
		hand[0].setEnabled(false);
		display[0].setEnabled(false);
		
		switch(hand.length) {
			case 2:
				addWidget(hand[0],              17,  75, 66, 23);
				addWidget(hand[1],              17, -19, 66, 23);
				
				addWidget(display[0],           17,  50, 66, 23);
				addWidget(display[1],           17,   6, 66, 23);
				break;
			case 3:
				addWidget(hand[0],              25,  75, 50, 23);
				addWidget(hand[1],             -10,  15, 13, 70);
				addWidget(hand[2],              97,  15, 13, 70);
				
				addWidget(display[0],           25,  50, 50, 23);
				addWidget(display[1],            5,  15, 13, 70);
				addWidget(display[2],           82,  15, 13, 70);
				break;
			case 4:
				addWidget(hand[0],              25,  75, 50, 23);
				addWidget(hand[1],             -10,  15, 13, 70);
				addWidget(hand[2],              25, -19, 50, 23);
				addWidget(hand[3],              97,  15, 13, 70);
				
				addWidget(display[0],           25,  50, 50, 23);
				addWidget(display[1],            5,  15, 13, 70);
				addWidget(display[2],           25,   6, 50, 23);
				addWidget(display[3],           82,  15, 13, 70);
				break;
			case 5:
				addWidget(hand[0],              25,  75, 50, 23);
				addWidget(hand[1],             -10,  30, 13, 65);
				addWidget(hand[2],               2, -16, 45, 20);
				addWidget(hand[3],              53, -16, 45, 20);
				addWidget(hand[4],              97,  30, 13, 65);
				
				addWidget(display[0],           25,  50, 50, 23);
				addWidget(display[1],            5,  30, 13, 65);
				addWidget(display[2],            2,   6, 45, 20);
				addWidget(display[3],           53,   6, 45, 20);
				addWidget(display[4],           82,  30, 13, 65);
				break;
		}
	}

	@Override
	public void handleDescriptionBox() {
		for (int i=0; i<hand.length; i++) {
			for (Widget w: hand[i].getWidgetList()) {
				if (w.isHovering()) {
					CardWidget c = (CardWidget)w;
					descriptionBox.setDisplay(c.getType(), c.getValue());
				}
			}
			for (Widget w: display[i].getWidgetList()) {
				if (w.isHovering()) {
					CardWidget c = (CardWidget)w;
					descriptionBox.setDisplay(c.getType(), c.getValue());
				}
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		for (int i=0; i<hand.length; i++) {
			if (hand[i].isClicked() || display[i].isClicked()) {
				overlayActionComplete = true;
				finalCommandString = ""+toGameTurn(i);
			}
		}
	}

}
