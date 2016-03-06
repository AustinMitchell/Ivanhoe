package ui.panel;

import java.util.*;
import java.awt.Color;
import java.awt.Font;

import network.Client;
import simple.gui.panel.ScaledPanel;
import simple.gui.*;
import ui.GameController;

public class WaitForJoinPanel extends ScaledPanel {
	GameController controller;
	Client client;
	
	ScaledPanel messagePanel;
	Label message, currentPlayersMessage;
	
	int numPlayers, currentPlayers;
	
	int joinTimer;
	
	public WaitForJoinPanel(GameController gc, final int numPlayers, ArrayList<String> currentPlayerList) {
		super();
		
		this.controller = gc;
		client = gc.getClient();
		
		this.numPlayers = numPlayers;
		currentPlayers = 1;
		
		messagePanel = new ScaledPanel() {{
			setWidgetColors(new Color(0, 0, 0, 200), new Color(0, 0, 0, 150), null, null);
			setDrawContainingPanel(true);
			
			message = new Label("Game has been set up for " + numPlayers + " players");
			message.setFont(new Font("Arial", Font.BOLD, 24));
			message.setTextColor(Color.WHITE);
			
			currentPlayersMessage = new Label(); 
			currentPlayersMessage.setFont(new Font("Arial", Font.PLAIN, 20));
			currentPlayersMessage.setTextColor(Color.WHITE);
			updateCurrentPlayersMessage();
			
			addWidget(message,               5,  0, 90, 50);
			addWidget(currentPlayersMessage, 5, 50, 90, 50);
		}};
	
		addWidget(messagePanel, 35, 15, 30, 20);
		
		joinTimer = 120;
	}
	
	@Override
	public void update() {
		handleClientMessages();
	}
	
	private void updateCurrentPlayersMessage() {
		currentPlayersMessage.setText("Waiting for players to join...");
		/*
		if (numPlayers - currentPlayers > 1) {
			currentPlayersMessage.setText("Waiting for " + (numPlayers-currentPlayers) + " more players...");
		} else {
			currentPlayersMessage.setText("Waiting for 1 more player");
		}*/
	}
	
	private void handleClientMessages() {
		if (client.hasFlags()) {
			String[] command = client.readGuiFlag().split(":");
			switch (command[0]) {
				case "initClient":
					ArrayList<String> playerNames = new ArrayList<String>();
					int i=1;
					while (!command[i].equals("cards")) {
						playerNames.add(command[i]);
						i++;
					}
					controller.startNewGame(playerNames);
					break;
			}
		}
	}
}
