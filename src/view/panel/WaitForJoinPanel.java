package view.panel;

import java.util.*;

import controller.GameController;
import controller.network.Client;
import model.Flag;

import java.awt.Color;
import java.awt.Font;

import simple.gui.panel.ScaledPanel;
import simple.gui.*;

public class WaitForJoinPanel extends ScaledPanel {
	GameController controller;
	Client client;
	
	ScaledPanel messagePanel;
	Label message, currentPlayersMessage;
	
	int maxPlayers, currentPlayers;
		
	public WaitForJoinPanel(GameController gc) {
		super();
		
		this.controller = gc;
		client = gc.getClient();
		
		this.currentPlayers = 0;
		
		messagePanel = new ScaledPanel() {{
			setWidgetColors(new Color(0, 0, 0, 200), new Color(0, 0, 0, 150), null, null);
			setDrawContainingPanel(true);
			
			message = new Label();
			message.setFont(new Font("Arial", Font.BOLD, 24));
			message.setTextColor(Color.WHITE);
			
			currentPlayersMessage = new Label(); 
			currentPlayersMessage.setFont(new Font("Arial", Font.PLAIN, 20));
			currentPlayersMessage.setTextColor(Color.WHITE);
			
			addWidget(message,               5,  0, 90, 50);
			addWidget(currentPlayersMessage, 5, 50, 90, 50);
		}};
	
		addWidget(messagePanel, 35, 15, 30, 20);
		
	}
	
	@Override
	public void update() {
		handleClientMessages();
	}
	
	private void updateMaxPlayers() {
		message.setText("Game has been set up for " + maxPlayers + " players");
	}
	
	private void updateCurrentPlayersMessage() {		
		if (maxPlayers - currentPlayers > 1) {
			currentPlayersMessage.setText("Waiting for " + (maxPlayers-currentPlayers) + " more players...");
		} else {
			currentPlayersMessage.setText("Waiting for 1 more player");
		}
	}
	
	private void handleClientMessages() {
		if (client.hasFlags()) {
			String[] command = client.readGuiFlag().split(":");
			switch (command[0]) {
				case Flag.INIT_CLIENT:
					ArrayList<String> playerNames = new ArrayList<String>();
					int i=1;
					while (!command[i].equals(Flag.CARDS_BEGIN)) {
						playerNames.add(command[i]);
						i++;
					}
					controller.setPlayerNames(playerNames);
					controller.beginDrawToken();
					break;
				case Flag.CURRENT_NUM_PLAYERS:
					currentPlayers = Integer.parseInt(command[1]);
					updateCurrentPlayersMessage();
					break;
				case Flag.MAX_PLAYERS:
					maxPlayers = Integer.parseInt(command[1]);
					updateMaxPlayers();
					break;
			}
		}
	}
}
