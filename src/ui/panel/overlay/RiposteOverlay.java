package ui.panel.overlay;

import java.util.ArrayList;

import models.GameState;
import rulesengine.Validator;
import simple.gui.Widget;
import ui.CardWidget;
import ui.DescriptionBox;
import ui.utilitypanel.CardDisplayPanel;

public class RiposteOverlay extends OverlayPanel {
	private CardDisplayPanel[] display;
	
	public RiposteOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel[] gameDisplay) {
		super("Riposte - Select Opponent...", descriptionBox, game, realPlayerIndex);
		
		removeWidget(title);
		addWidget(title, 0, 0, 100, 100);
		
		ArrayList<Boolean> validPlayers = Validator.validateRiposte(game);
		
		display = new CardDisplayPanel[gameDisplay.length];
		for (int i=0; i<gameDisplay.length; i++) {
			display[i] = new CardDisplayPanel(gameDisplay[i].getOrientation(), true);
			for (Widget w: gameDisplay[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				display[i].addCard(new CardWidget(c.getType(), c.getValue()));
			}
			display[i].setEnabled(validPlayers.get(toGameTurn(i)));
		}
		
		switch(display.length) {
			case 2:
				addWidget(display[0], 17, 50, 66, 23);
				addWidget(display[1], 17,  6, 66, 23);
				break;
			case 3:
				addWidget(display[0], 25, 50, 50, 23);
				addWidget(display[1],  5, 15, 13, 70);
				addWidget(display[2], 82, 15, 13, 70);
				break;
			case 4:
				addWidget(display[0], 25, 50, 50, 23);
				addWidget(display[1],  5, 15, 13, 70);
				addWidget(display[2], 25,  6, 50, 23);
				addWidget(display[3], 82, 15, 13, 70);
				break;
			case 5:
				addWidget(display[0], 25, 50, 50, 23);
				addWidget(display[1],  5, 30, 13, 65);
				addWidget(display[2],  2,  6, 45, 20);
				addWidget(display[3], 53,  6, 45, 20);
				addWidget(display[4], 82, 30, 13, 65);
				break;
		}
	}

	@Override
	public void handleDescriptionBox() {
		for (CardDisplayPanel d: display) {
			for (Widget w: d.getWidgetList()) {
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
		for (int i=0; i<display.length; i++) {
			if (display[i].isClicked()) {
				overlayActionComplete = true;
				finalCommandString = ""+toGameTurn(i);
			}
		}
	}
}
