package view.utilitypanel;

import java.awt.Color;

import simple.gui.*;
import simple.gui.panel.ScaledPanel;

// Represents a collection of tokens. Can be used for showing players tokens or selecting colours
public class TokenBar extends ScaledPanel{
	// Returns a drawing function given a specific set of colours
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
	// Drawing function for disabled tokens
	public static final CustomDraw EMPTY_TOKEN = 
			ovalDrawObject(new Color(180, 180, 180), new Color(120, 120, 120), new Color(180, 180, 180), new Color(120, 120, 120));
	// Drawing functions for each type of token
	public static final CustomDraw[] TOKEN_DRAW = {
			// Purple
			ovalDrawObject(new Color(156, 114, 178), new Color(100,  70, 120), new Color(120,  90, 150), new Color(100,  70, 120)),
			// Red
			ovalDrawObject(new Color(250,  94, 108), new Color(200,  70,  80), new Color(200,  70,  80), new Color(180,  50,  60)),
			// Yellow
			ovalDrawObject(new Color(249, 235, 172), new Color(210, 200, 150), new Color(210, 200, 150), new Color(180, 170, 120)),
			// Blue
			ovalDrawObject(new Color(147, 163, 214), new Color(110, 120, 180), new Color(110, 120, 180), new Color( 90, 100, 160)),
			// Green
			ovalDrawObject(new Color(142, 190, 148), new Color(125, 170, 130), new Color(125, 170, 130), new Color(110, 155, 120))
		};
	
	private boolean[] tokenActive;
	private Canvas[] tokenUI;
	
	// Returns whether a specific token is enabled
	public boolean isTokenEnabled(int type) {
		return tokenActive[type];
	}
	
	// Enables a specific token
	public void enableToken(int type) {
		tokenActive[type] = true;
		tokenUI[type].setCustomDraw(TOKEN_DRAW[type]);
	}
	// Enables all tokens
	public void enableAllTokens() {
		for (int i=0; i<5; i++) {
			enableToken(i);
		}
	}
	// Disables token of specific type(colour)
	public void disableToken(int type) {
		tokenActive[type] = false;
		tokenUI[type].setCustomDraw(EMPTY_TOKEN);
	}
	// Disables all tokens
	public void disableAllTokens() {
		for (int i=0; i<5; i++) {
			disableToken(i);
		}
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
		setWidgetColors(new Color(120, 120, 120, 200), new Color(200, 200, 200, 100), null, null);
		setDrawContainingPanel(true);
	}
	
	public boolean isClicked(int type) {
		return tokenActive[type] && tokenUI[type].isClicked();
	}
}
