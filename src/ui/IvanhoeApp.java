package ui;

import java.awt.Color;

import simple.run.*;
import simple.gui.*;
import simple.gui.panel.*;

public class IvanhoeApp extends SimpleGUIApp {
	public static void main(String[] args) { start(new IvanhoeApp(), "Ivanhoe"); }
	public IvanhoeApp() { super(MAXWIDTH, MAXHEIGHT-70, 60); }
	
	GameController controller;
	
	public void setup() {		
		controller = new GameController(this);
	}
	
	public void loop() {
		controller.loop();
		updateView();
	}
}
