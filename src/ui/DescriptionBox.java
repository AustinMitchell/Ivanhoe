package ui;

import java.awt.Color;
import java.awt.Font;

import simple.gui.*;

public class DescriptionBox extends Widget {
	public static final int NAME_LABEL_HEIGHT = 40;
	
	private Label name, description;
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		setLabelPositions();
	}
	@Override
	public void setX(int x) { setLocation(x, this.y); }
	@Override
	public void setY(int y) { setLocation(this.x, y); }
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		setLabelSizes();
	}
	@Override
	public void setWidth(int w) { setSize(w, this.h); }
	@Override
	public void setHeight(int h) { setSize(this.w, h); }
	
	// Sets positions for label widgets
	private void setLabelPositions() {
		name.setLocation(x, y);
		description.setLocation(x, y+NAME_LABEL_HEIGHT);
	}
	// Sets size for label widgets
	private void setLabelSizes() {
		name.setSize(w, NAME_LABEL_HEIGHT);
		description.setSize(w, h-NAME_LABEL_HEIGHT);
	}
	
	// Set the display to the desired card
	public void setDisplay(int type, int value) {
		name.setText(CardData.getCardName(type, value));
		description.setText(CardData.getCardDescription(type, value));
	}
	// Empty out display
	public void clearDisplay() {
		name.setText("");
		description.setText("");
	}
	
	public DescriptionBox() {
		this(0, 0, 10, 10);
	}
	public DescriptionBox(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		name = new Label();
		name.setFont(new Font("Arial", Font.BOLD, 20));
		name.setTextColor(Color.WHITE);
		
		description = new Label();
		description.setFont(new Font("Tahoma", Font.PLAIN, 16));
		description.setAlignment(TextArea.Alignment.NORTHWEST);
		description.setBoxIsVisible(true);
		
		setLabelPositions();
		setLabelSizes();
	}	
	
	public void update() {
		if (!enabled || !visible) return;
		updateClickingState();
	}
	public void draw() {
		if (!visible) return;
		
		draw.setColors(Color.BLACK, Color.BLACK);
		draw.rect(x, y, w, h);
		name.draw();
		description.draw();
	}
}
