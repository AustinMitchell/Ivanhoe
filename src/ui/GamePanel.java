package ui;

import java.awt.Color;

import simple.gui.*;
import simple.gui.panel.*;

public class GamePanel extends ScaledPanel {
	CardDisplayPanel[] display;
	DescriptionBox desc;
	ScaledPanel mainGamePanel, sidePanel;
	
	int numPlayers;
	
	public GamePanel(int numPlayers) {
		super();
		
		this.numPlayers = numPlayers;
		
		mainGamePanel = new ScaledPanel();
		sidePanel = new ScaledPanel();
		
		desc = new DescriptionBox();
		display = new CardDisplayPanel[4];
		
		display[0] = new CardDisplayPanel(Image.Orientation.UP);
		display[1] = new CardDisplayPanel(Image.Orientation.RIGHT);
		display[2] = new CardDisplayPanel(Image.Orientation.UP);
		display[3] = new CardDisplayPanel(Image.Orientation.LEFT);
		
		for (int d=0; d<display.length; d++) {
			for (int i=0; i<8; i++) {
				int[] v = CardData.ALL_CARD_VALUES[(int)(Math.random()*CardData.ALL_CARD_VALUES.length)];
				display[d].addCard(new CardWidget(v[0], v[1]));
			}
		}
		
		mainGamePanel.addWidget(display[0], 25, 75, 50, 23);
		mainGamePanel.addWidget(display[1], 2, 15, 13, 70);
		mainGamePanel.addWidget(display[2], 25, 2, 50, 23);
		mainGamePanel.addWidget(display[3], 85, 15, 13, 70);
		
		sidePanel.setCustomDraw(new CustomDraw() { 
			public void draw(Widget w) {
				draw.setColors(new Color(0xf5f5dc), Color.BLACK);
				draw.rect(w.getX(), w.getY(), w.getWidth(), w.getHeight());
			}});
				
		mainGamePanel.addWidget(desc, 40, 40, 20, 20);
		
		addWidget(mainGamePanel, 0, 0, 80, 100);
		addWidget(sidePanel, 80, 0, 20, 100);
	}
	
	public void update() {
		super.update();
		
		boolean cardFound = false;
		outer: for (int i=0; i<4; i++) {
			for (Widget w: display[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				if (c.isHovering()) {
					cardFound = true;
					desc.setDisplay(c.getType(), c.getValue());
					break outer;
				}
			}
		}
		if (!cardFound) {
			desc.clearDisplay();
		}
	}
}
