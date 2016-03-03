package ui.panel;

import java.awt.Color;
import java.awt.Font;

import simple.gui.*;
import simple.gui.panel.ScaledPanel;
import ui.GameController;

public class ConnectPanel extends ScaledPanel {	
	private ImageBox ivanhoeImage;
	private Color bgColor;
	
	private GameController controller;
	
	private ScaledPanel addressEntryPanel;
	private Label addressEntryLabel, addressEntryFailed;
	private TextEntryBox addressEntry;
		
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		ivanhoeImage.setScaledHeight(h);
		ivanhoeImage.setX(draw.windowWidth()/2 - ivanhoeImage.getWidth()/2);
	}
	
	public ConnectPanel(GameController controller) {
		super();
				
		ivanhoeImage = new ImageBox(new Image("res/ivanhoe_cover.jpg"));
		bgColor = new Color(40, 40, 30);
				
		this.controller = controller;
		
		addressEntryPanel = new ScaledPanel() {{
			setWidgetColors(new Color(0, 0, 0, 200), new Color(0, 0, 0, 150), null, null);
			setDrawContainingPanel(true);
			
			addressEntryLabel = new Label("Enter IP address of Ivanhoe server:");
			addressEntryLabel.setFont(new Font("Arial", Font.BOLD, 25));
			addressEntryLabel.setTextColor(Color.WHITE);
			
			addressEntry = new TextEntryBox();
			addressEntry.setAlignment(TextArea.Alignment.CENTER);
			addressEntry.setFont(new Font("Tahoma", Font.PLAIN, 20));
			addressEntry.setWidgetColors(null, new Color(200, 200, 200, 100), new Color(120, 120, 120, 200), Color.YELLOW);
			
			addressEntryFailed = new Label();
			addressEntryFailed.setFont(new Font("Arial", Font.PLAIN, 16));
			addressEntryFailed.setTextColor(Color.RED);
			
			addWidget(addressEntryLabel, 0, 0, 100, 40);
			addWidget(addressEntry, 5, 35, 90, 25);
			addWidget(addressEntryFailed, 0, 60, 100, 40);
		}};
		addWidget(addressEntryPanel, 35, 60, 30, 25);
	}
	
	public void connectFailed() {
		addressEntryFailed.setText("Address entered is an invalid IP address");
	}
	
	@Override
	public void draw() {
		draw.setColors(bgColor, Color.BLACK);
		draw.rect(0, 0, draw.windowWidth(), draw.windowHeight());
		ivanhoeImage.draw();
		
		super.draw();		
	}
	
	@Override
	public void update() {
		super.update();
		
		handleIPEntry();
	}
	
	private void handleIPEntry() {
		if (addressEntry.hasTextEntered()) {
			addressEntryFailed.clear();
			controller.connectToServer(this, addressEntry.getEnteredText());
		}
	}
}
