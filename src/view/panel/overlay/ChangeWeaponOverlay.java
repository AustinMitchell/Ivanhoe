package view.panel.overlay;

import controller.rulesengine.Validator;
import model.GameState;
import simple.gui.*;
import view.*;
import view.utilitypanel.CardDisplayPanel;
import view.utilitypanel.TokenBar;

public class ChangeWeaponOverlay extends OverlayPanel {
	private CardDisplayPanel playerHand;
	private TokenBar tokenChoice;
	
	public ChangeWeaponOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel playerHand) {
		super("Change Weapon - Select Tournament Colour...", descriptionBox, game, realPlayerIndex);
		
		
		tokenChoice = new TokenBar();
		boolean[] validColours = Validator.validateChangeWeapon(game);
		for(int i = 0; i < validColours.length; i++) {
			if(validColours[i]) {
				tokenChoice.enableToken(i);
			}
		}
		
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
