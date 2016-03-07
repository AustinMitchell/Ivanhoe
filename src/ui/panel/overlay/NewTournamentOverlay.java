package ui.panel.overlay;

import models.GameState;
import rulesengine.Type;
import simple.gui.*;
import ui.DescriptionBox;
import ui.utilitypanel.CardDisplayPanel;
import ui.utilitypanel.TokenBar;
import ui.*;

public class NewTournamentOverlay extends OverlayPanel {
	private CardDisplayPanel playerHand;
	private TokenBar tokenChoice;
	
	public NewTournamentOverlay(DescriptionBox descriptionBox, CardDisplayPanel playerHand, GameState game) {
		super("Select New Tournament Colour...", descriptionBox);
		
		tokenChoice = new TokenBar();
		
		this.playerHand = new CardDisplayPanel(Image.Orientation.UP, true);
		for (Widget w: playerHand.getWidgetList()) {
			CardWidget c = (CardWidget)w;
			int t = c.getType();
			this.playerHand.addCard(new CardWidget(t, c.getValue()));
			if (t < 5) {
				tokenChoice.enableToken(t);
			} else if (t == 5) {
				tokenChoice.enableAllTokens();
			}
		}
		
		if (game.getPrevTournamentColour() == Type.PURPLE) {
			tokenChoice.disableToken(Type.PURPLE);
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
