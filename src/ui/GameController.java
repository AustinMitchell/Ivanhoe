package ui;

import java.awt.Color;
import java.util.ArrayList;

import network.Client;
import network.Server;
import simple.gui.panel.*;
import simple.run.SimpleGUIApp;
import ui.panel.*;;

public class GameController {
	private SimpleGUIApp mainApp;
	private PanelCollection screen;
	private Client client;
	
	public GameController(SimpleGUIApp mainApp) {
		this.mainApp = mainApp;
		client = new Client();
		mainApp.setBackgroundColor(new Color(175,117,68));
		screen = new PanelCollection(0, 0, mainApp.getWidth(), mainApp.getHeight());
		
		mainScreen();
	}
	
	public Client getClient() { return client; }
	
	// Called from IvanhoeApp to update the current UI and draw it to the screen
	public void loop() {
		screen.update();
		screen.draw();
	}
	
	// Checks if address is a valid IP string
	private boolean isValidIPAddress (String ip) {
		if (ip.equals("localhost")) {
			return true;
		}
		
	    try {
	        if ( ip == null || ip.isEmpty() ) {
	            return false;
	        }

	        String[] parts = ip.split( "\\." );
	        if ( parts.length != 4 ) {
	            return false;
	        }

	        for ( String s : parts ) {
	            int i = Integer.parseInt( s );
	            if ( (i < 0) || (i > 255) ) {
	                return false;
	            }
	        }
	        if ( ip.endsWith(".") ) {
	            return false;
	        }

	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
	// Sets up a new UI with the opening screen
	public void mainScreen() {
		screen.clear();
		screen.addPanel(new ConnectPanel(this));
		screen.setCurrentPanel(0);
	}
	// Either sets up a new UI, or leaves the current panel and tells it the connection failed
	public void connectToServer(ConnectPanel connectPanel, String address, String username) {
		if (!username.trim().equals("")) {
			if (isValidIPAddress(address)) {
				client.connect(address, username);
				while (client.waitingForConnection()) {}
				if (client.connectPassed()) {
					// TODO: Implement game being set up
					waitForGame(Server.NUM_PLAYERS);
				} else {
					connectPanel.connectFailed();
				}
			} else {
				connectPanel.invalidIP();
			} 
		} else {
			connectPanel.invalidUsername();
		}
	}
	
	// Creates a new UI panel for setting up a game
	public void setupGame() {
		screen.clear();
		screen.addPanel(new SetupGamePanel(this));
		screen.setCurrentPanel(0);
	}
	
	// Creates a new UI panel for waiting for a game to start
	public void waitForGame(int numPlayers) {
		screen.clear();
		screen.addPanel(new WaitForJoinPanel(this, numPlayers, null));
		screen.setCurrentPanel(0);
	}
	
	// Creates a new game with the given number of players (2 to 5)
	public void startNewGame(ArrayList<String> playerNames) {
		screen.clear();
		screen.addPanel(new GamePanel(this, playerNames));
		screen.setCurrentPanel(0);
	}
}
