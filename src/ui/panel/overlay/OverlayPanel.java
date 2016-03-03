package ui.panel.overlay;

import java.awt.Color;
import java.awt.Font;

import simple.gui.panel.ScaledPanel;
import simple.gui.*;
import ui.DescriptionBox;

public abstract class OverlayPanel extends ScaledPanel {
	protected String finalCommandString;
	protected DescriptionBox descriptionBox;
	protected boolean overlayActionComplete;
	
	protected Label title;
	
	public boolean isOverlayActionComplete() { return overlayActionComplete; }
	public String getFinalCommandString() { return finalCommandString; }
	
	public abstract void handleDescriptionBox();
	
	public OverlayPanel(String titleText, DescriptionBox descriptionBox) {
		super(0, 0, 10, 10);
		finalCommandString = "";
		overlayActionComplete = false;
		title = new Label(titleText);
		title.setFont(new Font("Tahoma", Font.BOLD, 25));
		title.setTextColor(Color.WHITE);
		
		this.descriptionBox = descriptionBox;
		
		drawContainingPanel = true;
		borderColor = Color.BLACK;
		fillColor = new Color(0, 0, 0, 210);
		
		addWidget(title, 0, 0, 100, 10);
	}
	
	@Override
	public void update() {
		super.update();
		handleDescriptionBox();
	}
}
