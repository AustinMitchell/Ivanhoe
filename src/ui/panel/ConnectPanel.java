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
	private Label addressEntryLabel, entryFailed, usernameEntryLabel;
	private TextEntryBox addressEntry, usernameEntry;
		
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
			
			addressEntryLabel = new Label("Enter server IP address:");
			addressEntryLabel.setFont(new Font("Arial", Font.BOLD, 20));
			addressEntryLabel.setTextColor(Color.WHITE);
			
			addressEntry = new TextEntryBox();
			addressEntry.setText("localhost");
			addressEntry.setAlignment(TextArea.Alignment.CENTER);
			addressEntry.setFont(new Font("Tahoma", Font.PLAIN, 20));
			addressEntry.setWidgetColors(null, new Color(200, 200, 200, 100), new Color(120, 120, 120, 200), Color.YELLOW);
			
			usernameEntryLabel = new Label("Enter Username:");
			usernameEntryLabel.setFont(new Font("Arial", Font.BOLD, 20));
			usernameEntryLabel.setTextColor(Color.WHITE);
			
			usernameEntry = new TextEntryBox();
			usernameEntry.setText("Player");
			usernameEntry.setAlignment(TextArea.Alignment.CENTER);
			usernameEntry.setFont(new Font("Tahoma", Font.PLAIN, 20));
			usernameEntry.setWidgetColors(null, new Color(200, 200, 200, 100), new Color(120, 120, 120, 200), Color.YELLOW);
			
			entryFailed = new Label();
			entryFailed.setFont(new Font("Arial", Font.PLAIN, 16));
			entryFailed.setTextColor(Color.RED);
			
			addWidget(addressEntryLabel,   0,  0,  40, 40);
			addWidget(addressEntry,       40,  7,  55, 26);
			addWidget(usernameEntryLabel,  0, 40,  40, 40);
			addWidget(usernameEntry,      40, 47,  55, 26);
			addWidget(entryFailed,  0, 73, 100, 20);
		}};
		addWidget(addressEntryPanel, 35, 55, 30, 35);
	}
	
	public void connectFailed() {
		entryFailed.setText("Connection to server failed (possibly invalid IP)");
	}
	public void invalidIP() {
		entryFailed.setText("Address entered is an invalid IP address");
	}
	public void invalidUsername() {
		entryFailed.setText("Invalid username");
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
		if (addressEntry.hasTextEntered() || usernameEntry.hasTextEntered()) {
			entryFailed.clear();
			controller.connectToServer(this, addressEntry.getText(), usernameEntry.getText());
			addressEntry.clear();
			usernameEntry.clear();
		}
	}
}
