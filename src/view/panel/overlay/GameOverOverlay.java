package view.panel.overlay;

import java.awt.Color;
import java.awt.Font;

import model.GameState;
import simple.gui.Button;
import simple.gui.Label;
import view.DescriptionBox;

public class GameOverOverlay extends OverlayPanel {
	Button returnButton;
	
	public GameOverOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex) {
		super("Game Over", descriptionBox, game, realPlayerIndex);
		
		Label winLabel = new Label();
		winLabel.setTextColor(new Color(255, 180, 100));
		winLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		
		int winner = game.getTurn();
		if (winner == realPlayerIndex) {
			winLabel.setText("You Win!");
		} else {
			winLabel.setText(game.getPlayer(game.getTurn()).getName() + " Wins!");
		}
		
		returnButton = new Button("Return to Main Screen");
		
		addWidget(winLabel, 20, 20, 60, 40);
		addWidget(returnButton, 43, 70, 14, 10);
	}

	@Override
	public void handleDescriptionBox() {
		
	}
	
	@Override
	public void update() {
		super.update();
		if (returnButton.isClicked()) {
			overlayActionComplete = true;
			finalCommandString = "";
		}
	}

}
