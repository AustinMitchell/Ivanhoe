package ui;

import simple.run.*;

// All boilerplate code to run the UI
public class IvanhoeApp extends SimpleGUIApp {
	public static void main(String[] args) { start(new IvanhoeApp(), "Ivanhoe"); }
	public IvanhoeApp() { super(MAXWIDTH, MAXHEIGHT-70, 25); }
	
	GameController controller;
	
	public void setup() {		
		CardData.initialize();
		controller = new GameController(this);
	}
	
	public void loop() {
		controller.loop();
		updateView();
	}
}
