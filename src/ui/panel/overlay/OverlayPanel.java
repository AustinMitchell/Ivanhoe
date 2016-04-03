package ui.panel.overlay;

import java.awt.Color;
import java.awt.Font;

import models.GameState;
import simple.gui.panel.ScaledPanel;
import simple.gui.*;
import ui.DescriptionBox;

public abstract class OverlayPanel extends ScaledPanel {
	protected GameState game;
	protected int realPlayerIndex;
	
	protected String finalCommandString;
	protected DescriptionBox descriptionBox;
	protected boolean overlayActionComplete;
	
	protected Label title;
	
	public boolean isOverlayActionComplete() { return overlayActionComplete; }
	public String getFinalCommandString() { return finalCommandString; }
	
	public abstract void handleDescriptionBox();
	
	public OverlayPanel(String titleText, DescriptionBox descriptionBox, GameState game, int realPlayerIndex) {
		super(0, 0, 10, 10);
		finalCommandString = "";
		overlayActionComplete = false;
		title = new Label(titleText);
		title.setFont(new Font("Tahoma", Font.BOLD, 25));
		title.setTextColor(Color.WHITE);
		
		this.game = game;
		this.realPlayerIndex = realPlayerIndex;
		this.descriptionBox = descriptionBox;
		
		drawContainingPanel = true;
		borderColor = Color.BLACK;
		fillColor = new Color(0, 0, 0, 210);
		
		addWidget(title, 0, 0, 100, 10);
	}
	
	public int toGameTurn(int turn) {
		return Math.floorMod((turn+realPlayerIndex), game.getAllPlayers().size());
	}
	public int toGUITurn(int turn) {
		return Math.floorMod((turn-realPlayerIndex), game.getAllPlayers().size());
	}
	
	@Override
	public void update() {
		super.update();
		handleDescriptionBox();
	}
}
