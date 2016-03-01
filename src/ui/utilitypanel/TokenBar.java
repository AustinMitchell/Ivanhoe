package ui;

import java.awt.Color;

import simple.gui.*;
import simple.gui.panel.ScaledPanel;

public class TokenBar extends ScaledPanel{
	private static CustomDraw ovalDrawObject(final Color fill, final Color stroke, final Color hoverFill, final Color hoverStroke) {
		return new CustomDraw(){
				public void draw(Widget w) {
					int ovalRadius = 
						(w.getWidth() > w.getHeight()) ? 
							w.getHeight()/2 : 
							w.getWidth()/2;
					
					if (w.isHovering() || w.isClicking()) {
						draw.setColors(hoverFill, hoverStroke);
					} else {
						draw.setColors(fill, stroke);
					}
					draw.ovalCentered(w.getX() + w.getWidth()/2, w.getY() + w.getHeight()/2, ovalRadius-2, ovalRadius-2);
				}
			};
	}
	
	private static final CustomDraw EMPTY_TOKEN = 
			ovalDrawObject(new Color(180, 180, 180), new Color(120, 120, 120), new Color(180, 180, 180), new Color(120, 120, 120));
	private static final CustomDraw[] TOKEN_DRAW = {
			ovalDrawObject(new Color(255,   0, 255), new Color(180,   0, 180), new Color(255, 100, 255), new Color(180,  50, 180)),
			ovalDrawObject(new Color(255,   0,   0), new Color(180,   0,   0), new Color(255, 100, 100), new Color(180,  50,  50)),
			ovalDrawObject(new Color(255, 255,   0), new Color(180, 180,   0), new Color(255, 255, 180), new Color(180, 180, 100)),
			ovalDrawObject(new Color(0,     0, 255), new Color(0,     0, 180), new Color(100, 100, 255), new Color(50,   50, 180)),
			ovalDrawObject(new Color(0,   255,   0), new Color(0,   180,   0), new Color(100, 255, 100), new Color(50,  180,  50))
		};
	
	private boolean[] tokenActive;
	private Canvas[] tokenUI;
	
	public boolean isTokenEnabled(int type) {
		return tokenActive[type];
	}
	
	public void enableToken(int type) {
		tokenActive[type] = true;
		tokenUI[type].setCustomDraw(TOKEN_DRAW[type]);
	}
	public void disableToken(int type) {
		tokenActive[type] = false;
		tokenUI[type].setCustomDraw(EMPTY_TOKEN);
	}
	
	public TokenBar() {
		super(5, 1);
		tokenActive = new boolean[5];
		tokenUI = new Canvas[5];
		for (int i=0; i<5; i++) {
			tokenActive[i] = false;
			tokenUI[i] = new Canvas();
			tokenUI[i].setCustomDraw(EMPTY_TOKEN);
			addWidget(tokenUI[i], i, 0, 1, 1);
		}
		
		setDrawContainingPanel(true);
	}
}
