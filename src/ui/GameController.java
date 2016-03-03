package ui;

import java.awt.Color;

import simple.gui.panel.*;
import simple.run.SimpleGUIApp;
import ui.panel.*;;

public class GameController {
	private SimpleGUIApp mainApp;
	private PanelCollection screen;
	
	public GameController(SimpleGUIApp mainApp) {
		this.mainApp = mainApp;
		mainApp.setBackgroundColor(new Color(175,117,68));
		screen = new PanelCollection(0, 0, mainApp.getWidth(), mainApp.getHeight());
		
		mainScreen();
	}
	
	// Called from IvanhoeApp to update the current UI and draw it to the screen
	public void loop() {
		screen.update();
		screen.draw();
	}
	
	// Sets up a new UI with the opening screen
	public void mainScreen() {
		screen.clear();
		screen.addPanel(new ConnectPanel(this));
		screen.setCurrentPanel(0);
	}
	
	// Either sets up a new UI, or leaves the current panel and tells it the connection failed
	public void connectToServer(ConnectPanel connectPanel, String address) {
		if (isValidIPAddress(address)) {
			setupGame();
		} else {
			connectPanel.connectFailed();
		}
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
	
	// Creates a new UI panel for setting up a game
	public void setupGame() {
		screen.clear();
		screen.addPanel(new StartGamePanel(this));
		screen.setCurrentPanel(0);
	}
	
	// Creates a new game with the given number of players (2 to 5)
	public void startNewGame(int numPlayers) {
		screen.clear();
		screen.addPanel(new GamePanel(numPlayers, this));
		screen.setCurrentPanel(0);
	}
}
