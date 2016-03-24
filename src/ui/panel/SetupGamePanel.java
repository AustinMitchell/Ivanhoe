package ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import network.Client;
import network.Flag;
import simple.gui.*;
import simple.gui.panel.ScaledPanel;
import ui.GameController;

public class SetupGamePanel extends ScaledPanel {
	GameController controller;
	Client client;
	
	private ScaledPanel playerSelectPanel;
	private Label playerSelectLabel, entryError;
	private TextEntryBox playerSelectTextbox;
	
	public SetupGamePanel(GameController controller) {
		super();
		
		this.controller = controller;
		client = controller.getClient();
		
		playerSelectPanel = new ScaledPanel() {{
			setWidgetColors(new Color(0, 0, 0, 200), new Color(0, 0, 0, 150), null, null);
			setDrawContainingPanel(true);
			
			playerSelectLabel = new Label("Enter number of players (2 - 5):");
			playerSelectLabel.setFont(new Font("Arial", Font.BOLD, 25));
			playerSelectLabel.setTextColor(Color.WHITE);
			
			playerSelectTextbox = new TextEntryBox();
			playerSelectTextbox.setAlignment(TextArea.Alignment.CENTER);
			playerSelectTextbox.setFont(new Font("Tahoma", Font.PLAIN, 20));
			playerSelectTextbox.setWidgetColors(null, new Color(200, 200, 200, 100), new Color(120, 120, 120, 200), Color.YELLOW);
			
			entryError = new Label();
			entryError.setFont(new Font("Arial", Font.PLAIN, 16));
			entryError.setTextColor(Color.RED);
			
			addWidget(playerSelectLabel,    0,  0, 100, 40);
			addWidget(playerSelectTextbox, 35, 35,  30, 25);
			addWidget(entryError, 			0, 60, 100, 40);
		}};
		
		addWidget(playerSelectPanel, 35, 15, 30, 20);
	}
	
	@Override
	public void update() {
		super.update();
		
		handlePlayerEntry();
		handleClientMessages();
	}
	
	private void handlePlayerEntry() {
		if (playerSelectTextbox.hasTextEntered()) {
			entryError.clear();
			String text = playerSelectTextbox.getEnteredText();
			int numPlayers;
			
			try {
				numPlayers = Integer.parseInt(text);
				if (numPlayers >= 2 && numPlayers <= 5) {
					client.sendMessage(Flag.MAX_PLAYERS_SET + ":" + numPlayers);
				} else {
					entryError.setText("Number of players must be between 2 and 5.");
				}
			} catch(NumberFormatException e) {
				entryError.setText("You did not enter a valid integer.");
			} 
		}
	}
	
	private void handleClientMessages() {
		if (client.hasFlags()) {
			String msg = client.readGuiFlag();
			String[] command = msg.split(":");
			
			System.out.println(msg);
			switch (command[0]) {
				case Flag.MAX_PLAYERS_SET:
					controller.waitForGame();
					break;
			}
		}
	}
}
