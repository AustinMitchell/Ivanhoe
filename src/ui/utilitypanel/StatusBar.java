package ui.utilitypanel;

import java.awt.Color;
import java.awt.Font;

import simple.gui.*;
import simple.gui.panel.ScaledPanel;

public class StatusBar extends ScaledPanel {
	private static final Image greyImage(Image img) {
		Image returnImage = new Image(img);
		draw.setColors(new Color(180, 180, 180, 200), null);
		draw.rect(returnImage, 0, 0, returnImage.getWidth(), returnImage.getHeight());
		return returnImage;
	}
	
	private static class ConstantRatioImageBox extends ImageBox {
		private int imagex, imagey;
		
		@Override
		public void setSize(int w, int h) {
			this.w = w;
			this.h = h;
			if (baseImage != null) {
				image = baseImage.resize(this.w, this.h);
			}
			if (baseImage != null) {
				if (w < h) {
					imagex = x;
					imagey = y+h/2 - w/2;
					image = baseImage.resizeScaledWidth(w);
				} else if (w > h) {
					imagex = x+w/2 - h/2;
					imagey = y;
					image = baseImage.resizeScaledHeight(h);
				} 
			}
		}
		
		public ConstantRatioImageBox(Image image) {
			super(image);
		}
		
		@Override
		public void draw() {
			if (!visible) {
				return;
			}
			if (image != null) {
				draw.image(this.image, imagex, imagey);
			}
		}
	}
	
	private static final Image SHIELD_IMAGE = new Image("res/status/shield.png");
	private static final Image SHIELD_EMPTY = greyImage(SHIELD_IMAGE);
	private static final Image STUN_IMAGE = new Image("res/status/stun.png");
	private static final Image STUN_EMPTY = greyImage(STUN_IMAGE);
	
	private TokenBar tokens;
	private ConstantRatioImageBox shield, stun;
	private Label name;
	private Label displayValue;
	
	public StatusBar(String name) {
		super(); 
		
		this.name = new Label(name);
		this.name.setFont(new Font("Arial", Font.PLAIN, 12));
		this.name.setAlignment(TextArea.Alignment.WEST);
		
		this.displayValue = new Label("-");
		this.displayValue.setFont(new Font("Impact", Font.BOLD, 20));
		
		tokens = new TokenBar();
		shield = new ConstantRatioImageBox(SHIELD_EMPTY);
		stun = new ConstantRatioImageBox(STUN_EMPTY);
				
		addWidget(displayValue, 0, 30, 20, 70);
		addWidget(this.name,    0,  0, 40, 30);
		addWidget(tokens,      20, 30, 50, 70);
		addWidget(shield,      71, 31, 13, 68);
		addWidget(stun,        86, 31, 13, 68);
	}
	
	public void setDisplayValue(int value) {
		if (value == 0) {
			displayValue.setText("-");
		} else {
			displayValue.setText("" + value);
		}
	}
	
	public void collectToken(int type) {
		tokens.enableToken(type);
	}
	
	public void enableShield() {
		shield.setImage(SHIELD_IMAGE);
	}
	public void disableShield() {
		shield.setImage(SHIELD_EMPTY);
	}
	
	public void enableStun() {
		stun.setImage(STUN_IMAGE);
	}
	public void disableStun() {
		stun.setImage(STUN_EMPTY);
	}
	
	public void clearStatus() {
		disableShield();
		disableStun();
	}
}
