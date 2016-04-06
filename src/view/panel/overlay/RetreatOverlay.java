package view.panel.overlay;

import model.GameState;
import simple.gui.*;
import view.*;
import view.utilitypanel.CardDisplayPanel;

public class RetreatOverlay extends OverlayPanel {
	private CardDisplayPanel playerDisplay;
	
	public RetreatOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel playerDisplay) {
		super("Retreat - Select a card from your display...", descriptionBox, game, realPlayerIndex);
		
		this.playerDisplay = new CardDisplayPanel(Image.Orientation.UP, true);
		for (Widget w: playerDisplay.getWidgetList()) {
			CardWidget c = (CardWidget)w;
			this.playerDisplay.addCard(new CardWidget(c.getType(), c.getValue()));
		}
		
		addWidget(this.playerDisplay, 17, 75, 66, 23);
	}

	@Override
	public void handleDescriptionBox() {
		for (Widget w: playerDisplay.getWidgetList()) {
			if (w.isHovering()) {
				CardWidget c = (CardWidget)w;
				descriptionBox.setDisplay(c.getType(), c.getValue());
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		for (int i=0; i<playerDisplay.size(); i++) {
			if (playerDisplay.getIndex(i).isClicked()) {
				overlayActionComplete = true;
				finalCommandString = ""+i;
			}
		}
	}

}
