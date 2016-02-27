package ui;

import java.awt.Color;

import simple.gui.panel.PanelCollection;
import simple.gui.panel.ScaledPanel;
import simple.run.SimpleGUIApp;
import ui.panel.GamePanel;

public class GameController {
	private SimpleGUIApp mainApp;
	private PanelCollection screen;
	
	private ScaledPanel startScreen;
	private GamePanel game;
	
	public GameController(SimpleGUIApp mainApp) {
		this.mainApp = mainApp;
		mainApp.setBackgroundColor(new Color(195, 195, 180));
		
		screen = new PanelCollection(0, 0, mainApp.getWidth(), mainApp.getHeight());
		
		game = new GamePanel(5);
		screen.addPanel(game);
		screen.setCurrentPanel(game);
	}
	
	public void loop() {
		screen.update();
		screen.draw();
	}
	
	public void goToMainScreen() {
		
	}
	
	public void goToGame() {
		
	}
}
