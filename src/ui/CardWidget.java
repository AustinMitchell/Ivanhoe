package ui;

import java.util.*;

import simple.gui.*;

public class CardWidget extends ImageBox {
	public static final double WIDTH_HEIGHT_RATIO = 144.0/200.0;
	public static final int HOVER_SHIFT_VALUE = 20;
	
	int type, value;
	
	public int getType() { return type; }
	public int getValue() { return value; }
		
	public CardWidget(int type, int value) {
		this(0, 0, 144, 200, type, value);
	}
	public CardWidget(int x, int y, int w, int h, int type, int value) {
		super(x, y, w, h, CardData.getCardImage(type, value)); 
		this.type = type;
		this.value = value;
	}
	
	public void draw() {
		if (!visible) {
			return;
		}

		int xdir=0, ydir=0;
		if (isHovering()) {
			switch(getOrientation()) {
				case UP:
					ydir = -1;
					break;
				case RIGHT:
					xdir = 1;
					break;
				case DOWN:
					ydir = 1;
					break;
				case LEFT:
					xdir = -1;
					break;
			}
		}
		
		draw.image(this.image, x + xdir*HOVER_SHIFT_VALUE, y + ydir*HOVER_SHIFT_VALUE);
	}
	
}
