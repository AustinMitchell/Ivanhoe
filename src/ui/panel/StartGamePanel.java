package ui.panel;

import java.awt.Color;
import java.awt.Font;

import simple.gui.*;
import simple.gui.panel.ScaledPanel;
import ui.GameController;

public class StartGamePanel extends ScaledPanel {
	GameController controller;
	
	private ScaledPanel playerSelectPanel;
	private Label playerSelectLabel, entryError;
	private TextEntryBox playerSelectTextbox;
	
	public StartGamePanel(GameController controller) {
		super();
		
		this.controller = controller;
		
		playerSelectPanel = new ScaledPanel() {{
			setWidgetColors(new Color(0, 0, 255, 40), Color.BLACK, null, null);
			setDrawContainingPanel(true);
			
			playerSelectLabel = new Label("Enter number of players:");
			playerSelectLabel.setFont(new Font("Arial", Font.BOLD, 40));
			
			playerSelectTextbox = new TextEntryBox();
			playerSelectTextbox.setFont(new Font("Arial", Font.BOLD, 25));
			playerSelectTextbox.setAlignment(TextArea.Alignment.CENTER);
			
			entryError = new Label();
			entryError.setFont(new Font("Arial", Font.PLAIN, 17));
			entryError.setTextColor(new Color(200, 0, 0));
			
			addWidget(playerSelectLabel, 0, 0, 100, 40);
			addWidget(playerSelectTextbox, 35, 35, 30, 25);
			addWidget(entryError, 0, 60, 100, 40);
		}};
		
		addWidget(playerSelectPanel, 35, 15, 30, 20);
	}
	
	@Override
	public void update() {
		super.update();
		if (playerSelectTextbox.hasTextEntered()) {
			String text = playerSelectTextbox.getEnteredText();
			int numPlayers;
			
			try {
				numPlayers = Integer.parseInt(text);
				if (numPlayers >= 2 && numPlayers <= 5) {
					controller.startNewGame(numPlayers);
				} else {
					entryError.setText("Number of players must be between 2 and 5.");
				}
			} catch(NumberFormatException e) {
				entryError.setText("You did not enter a valid integer.");
			} 
		}
	}
}
