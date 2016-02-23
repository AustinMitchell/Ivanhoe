package ui;

import java.awt.Color;

import simple.run.*;
import simple.gui.*;
import simple.gui.panel.*;

public class IvanhoeApp extends SimpleGUIApp {
	public static void main(String[] args) { start(new IvanhoeApp(), "Ivanhoe"); }
	public IvanhoeApp() { super(MAXWIDTH, MAXHEIGHT-70, 60); }
	//public IvanhoeApp() { super(1000, 700, 60); }
	
	ScaledPanel screen;
	GamePanel game;
	
	public void setup() {		
		setBackgroundColor(new Color(195, 195, 180));
		
		screen = new ScaledPanel(0, 0, getWidth(), getHeight());
		
		game = new GamePanel(4);
		screen.addWidget(game, 0, 0, 100, 100);
	}
	
	public void loop() {
		screen.update();
		
		screen.draw();
		updateView();
	}
}
