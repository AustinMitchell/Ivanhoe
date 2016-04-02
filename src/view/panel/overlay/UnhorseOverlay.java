package view.panel.overlay;

import model.GameState;
import model.Type;
import simple.gui.*;
import view.*;
import view.utilitypanel.CardDisplayPanel;
import view.utilitypanel.TokenBar;

public class UnhorseOverlay extends OverlayPanel {
	private CardDisplayPanel playerHand;
	private TokenBar tokenChoice;
	
	public UnhorseOverlay(DescriptionBox descriptionBox, CardDisplayPanel playerHand) {
		super("Unhorse - Select Tournament Colour...", descriptionBox);
		
		tokenChoice = new TokenBar();
		tokenChoice.enableToken(Type.RED);
		tokenChoice.enableToken(Type.YELLOW);
		tokenChoice.enableToken(Type.BLUE);
		
		this.playerHand = new CardDisplayPanel(Image.Orientation.UP, true);
		for (Widget w: playerHand.getWidgetList()) {
			CardWidget c = (CardWidget)w;
			this.playerHand.addCard(new CardWidget(c.getType(), c.getValue()));
		}
		
		addWidget(this.playerHand, 17, 75, 66, 23);
		addWidget(tokenChoice, 30, 40, 40, 15);
	}

	@Override
	public void handleDescriptionBox() {
		for (Widget w: playerHand.getWidgetList()) {
			if (w.isHovering()) {
				CardWidget c = (CardWidget)w;
				descriptionBox.setDisplay(c.getType(), c.getValue());
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		for (int i=0; i<5; i++) {
			if (tokenChoice.isClicked(i)) {
				finalCommandString = "" + i;
				overlayActionComplete = true;
			}
		}
	}

}
