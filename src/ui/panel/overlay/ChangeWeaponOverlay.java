package ui.panel.overlay;

import models.GameState;
import rulesengine.Validator;
import rulesengine.Type;
import simple.gui.*;
import ui.DescriptionBox;
import ui.utilitypanel.CardDisplayPanel;
import ui.utilitypanel.TokenBar;
import ui.*;

public class ChangeWeaponOverlay extends OverlayPanel {
	private CardDisplayPanel playerHand;
	private TokenBar tokenChoice;
	
	public ChangeWeaponOverlay(DescriptionBox descriptionBox, CardDisplayPanel playerHand, GameState game) {
		super("Change Weapon - Select Tournament Colour...", descriptionBox);
		
		
		tokenChoice = new TokenBar();
		boolean[] validColours = Validator.validateChangeWeapon(game);
		for(int i = 0; i < validColours.length; i++) {
			if(validColours[i]) {
				tokenChoice.enableToken(i);
			}
		}
		
		/*tokenChoice.enableToken(Type.RED);
		tokenChoice.enableToken(Type.YELLOW);
		tokenChoice.enableToken(Type.BLUE);*/
		//tokenChoice.disableToken(game.getTournamentColour());
		
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
