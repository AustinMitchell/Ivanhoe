package view;

import java.awt.Color;

import model.CardData;
import simple.gui.*;

// Represents a card. Stores type and value, and specific CardWidgets can change what card they are and whether they display as facedown or not
public class CardWidget extends ImageBox {
	public static final int GREY_ALPHA = 180;
	public static final double WIDTH_HEIGHT_RATIO = 144.0/200.0;
	public static final int HOVER_SHIFT_VALUE = 20;
	
	private int type, value;
	private int xdir, ydir;
	private boolean faceUp;
	
	private boolean moveOnHover;
	
	public int getType() { return type; }
	public int getValue() { return value; }
	
	public boolean isFaceUp() { return faceUp; }
	
	@Override
	public void setImage(Image image) {
		super.setImage(image);
		if (this.image != null && !enabled) {
			Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
			Draw.rect(this.image, 0, 0, w, h);
		}
	}
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		if (image != null && !enabled) {
		    Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
		    Draw.rect(image, 0, 0, w, h);
		}
	}
	
	@Override
	public void setScaledWidth(int w) {
		super.setScaledWidth(w);
		if (image != null && !enabled) {
		    Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
		    Draw.rect(image, 0, 0, w, h);
		}
	}
	
	@Override
	public void setScaledHeight(int h) {
		super.setScaledHeight(h);
		if (image != null && !enabled) {
		    Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
		    Draw.rect(image, 0, 0, w, h);
		}
	}
	
	@Override
	public void setOrientation(Image.Orientation orientation) {
		super.setOrientation(orientation);
		if (image != null && !enabled) {
		    Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
		    Draw.rect(image, 0, 0, w, h);
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled && this.enabled) {
		    Draw.setColors(new Color(100, 100, 100, GREY_ALPHA), null);
		    Draw.rect(image, 0, 0, w, h);
		} else if (enabled && !this.enabled) {
			image = baseImage.resize(w, h);
		}
		super.setEnabled(enabled);
	}
	
	// Set the card this widget represents. If faceup, the image will change.
	public void setCard(int type, int value) {
		if ((this.type != type || this.value != value) && faceUp) {
			setImage(CardData.getCardImage(type, value));
		}
		this.type = type;
		this.value = value;
	}
	// Sets the card to faceup or facedown. Will change image accordingly.
	public void setFaceUp(boolean faceUp) { 
		if (image != null) {
			if (faceUp){
				setImage(CardData.getCardImage(type, value));
			} else {
				setImage(CardData.getBackImage());
			}
		}
		this.faceUp = faceUp;
	}
	
	// Sets whether the card will move when hovered over or not. Moves according to orientation.
	public void setMoveOnHover(boolean moveOnHover) { this.moveOnHover = moveOnHover; }
	
	public CardWidget(int type, int value) {
		this(0, 0, 144, 200, type, value, true);
	}
	public CardWidget(int type, int value, boolean faceUp) {
		this(0, 0, 144, 200, type, value, faceUp);
	}
	public CardWidget(int x, int y, int w, int h, int type, int value) {
		this(x, y, w, h, type, value, true);
	}
	public CardWidget(int x, int y, int w, int h, int type, int value, boolean faceUp) {
		super(x, y, w, h, null); 
		
		this.faceUp = faceUp;
		if (faceUp) {
			setImage(CardData.getCardImage(type, value));
		} else {
			setImage(CardData.getBackImage());
		}
		
		this.type = type;
		this.value = value;
		
		this.xdir = 0;
		this.ydir = 0;
		
		moveOnHover = true;
	}
	
	public void draw() {
		if (!visible) {
			return;
		}

		if (enabled && moveOnHover && isHovering()) {
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
		} else {
			xdir = 0;
			ydir = 0;
		}
		
		if (image != null) {
		    Draw.image(this.image, x + xdir*HOVER_SHIFT_VALUE, y + ydir*HOVER_SHIFT_VALUE);
		}
	}
	
}
