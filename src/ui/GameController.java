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
		mainApp.setBackgroundColor(new Color(195, 195, 180));
		screen = new PanelCollection(0, 0, mainApp.getWidth(), mainApp.getHeight());
		
		mainScreen();
	}
	
	public void loop() {
		screen.update();
		screen.draw();
	}
	
	public void mainScreen() {
		screen.clear();
		screen.addPanel(new ConnectPanel(this));
		screen.setCurrentPanel(0);
	}
	
	public void connectToServer(ConnectPanel connectPanel, String address) {
		if (isValidIPAddress(address)) {
			setupGame();
		} else {
			connectPanel.connectFailed();
		}
	}
	private boolean isValidIPAddress (String ip) {
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
	
	public void setupGame() {
		screen.clear();
		screen.addPanel(new StartGamePanel(this));
		screen.setCurrentPanel(0);
	}
	
	public void startNewGame(int numPlayers) {
		screen.clear();
		screen.addPanel(new GamePanel(numPlayers, this));
		screen.setCurrentPanel(0);
	}
}
