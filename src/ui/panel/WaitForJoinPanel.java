package ui.panel;

import java.util.*;

import java.awt.Color;
import java.awt.Font;

import simple.gui.panel.ScaledPanel;
import simple.gui.*;
import ui.GameController;

public class WaitForJoinPanel extends ScaledPanel {
	GameController controller;
	
	ScaledPanel messagePanel;
	Label message, currentPlayersMessage;
	
	int numPlayers, currentPlayers;
	
	int joinTimer;
	
	public WaitForJoinPanel(GameController gc, final int numPlayers, ArrayList<String> currentPlayerList) {
		super();
		
		this.controller = gc;
		
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
	
	private void updateCurrentPlayersMessage() {
		
		if (numPlayers - currentPlayers > 1) {
			currentPlayersMessage.setText("Waiting for " + (numPlayers-currentPlayers) + " more players...");
		} else {
			currentPlayersMessage.setText("Waiting for 1 more player");
		}
	}
	
	private void handleJoinTimer() {
		joinTimer -= 1;
		if (joinTimer <= 0) {
			joinTimer = 120;
			playerJoined();
		}
	}
	
	private void playerJoined() {
		currentPlayers += 1;
		if (numPlayers - currentPlayers <= 0) {
			controller.startNewGame(numPlayers);
		} else {
			updateCurrentPlayersMessage();
		}
	}
	
	@Override
	public void update() {
		handleJoinTimer();
	}
}
