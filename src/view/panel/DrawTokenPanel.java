package view.panel;

import java.awt.Color;
import java.awt.Font;

import controller.GameController;
import controller.network.Client;
import model.Flag;
import simple.gui.*;
import simple.gui.panel.*;
import view.utilitypanel.TokenBar;

public class DrawTokenPanel extends ScaledPanel {
	GameController controller;
	Client client;
	
	Button drawTokenButton, continueButton;
	ScaledPanel tokenRecievedPanel;
	Canvas drawnToken;
	
	
	public DrawTokenPanel(GameController gc) {
		controller = gc;
		client = gc.getClient();
		
		drawTokenButton = new Button("Click to recieve a token");
		continueButton = new Button("Continue...");
		continueButton.setVisible(false);
		
		tokenRecievedPanel = new ScaledPanel() {{
			setVisible(false);
			
			setWidgetColors(new Color(0, 0, 0, 200), new Color(0, 0, 0, 150), null, null);
			setDrawContainingPanel(true);
			
			drawnToken = new Canvas();
			
			Label lb = new Label("Drawn token:");
			lb.setFont(new Font("Arial", Font.PLAIN, 20));
			lb.setTextColor(Color.WHITE);
			
			addWidget(lb, 0, 0, 60, 100);
			addWidget(drawnToken, 60, 0, 40, 100);
		}};
		
		addWidget(tokenRecievedPanel, 38, 20, 24, 20);
		addWidget(drawTokenButton, 42, 23, 16, 14);
		addWidget(continueButton, 42, 53, 16, 14);
	}
	
	@Override
	public void update() {
		super.update();
		
		handleClientMessages();
		handleContinue();
		handleDrawToken();
	}
	
	
	
	private void handleDrawToken() {
		if (drawTokenButton.isClicked()) {
			client.sendMessage(Flag.DRAW_TOKEN);
			drawTokenButton.setEnabled(false);
		}
	}

	private void handleContinue() {
		if (continueButton.isClicked()) {
			client.sendMessage(Flag.BEGIN_TOKEN_DRAW_CONTINUE);
			continueButton.setEnabled(false);
			continueButton.setText("Waiting for other players...");
		}
	}

	private void handleClientMessages() {
		if (client.hasFlags()) {
			String[] command = client.readGuiFlag().split(":");
			switch (command[0]) {
				case Flag.GET_TOKEN: {
					if (client.getID() == Integer.parseInt(command[1])) {
						drawTokenButton.setVisible(false);
						tokenRecievedPanel.setVisible(true);
						continueButton.setVisible(true);
						drawnToken.setCustomDraw(TokenBar.TOKEN_DRAW[Integer.parseInt(command[2])]);
					}
					break;
				}
				case Flag.START_GAME: {
					controller.startNewGame();
					break;
				}
			}
		}
	}
}
