package view.panel.overlay;

import controller.rulesengine.Validator;
import model.GameState;
import view.*;
import view.utilitypanel.TokenBar;

public class WinningTokenOverlay extends OverlayPanel {
	private TokenBar tokenChoice;
	
	public WinningTokenOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex) {
		super("You won the tournament! Take your reward token...", descriptionBox, game, realPlayerIndex);
		
		tokenChoice = new TokenBar();
		boolean[] validChoice = Validator.validateToken(game);
		for (int i=0; i<5; i++) {
			if (validChoice[i]) {
				tokenChoice.enableToken(i);
			}
		}
		
		addWidget(tokenChoice, 30, 40, 40, 20);
	}

	@Override
	public void handleDescriptionBox() {}
	
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
