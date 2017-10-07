package view.panel.overlay;

import java.awt.Color;
import java.util.*;

import controller.rulesengine.Validator;
import model.Flag;
import model.GameState;
import simple.gui.CustomDraw;
import simple.gui.Draw;
import simple.gui.Image;
import simple.gui.ImageBox;
import simple.gui.Widget;
import simple.gui.Image.Orientation;
import simple.gui.panel.ScaledPanel;
import view.CardWidget;
import view.DescriptionBox;
import view.ResLoader;
import view.utilitypanel.CardDisplayPanel;
import view.utilitypanel.StatusBar;

public class OutwitOverlay extends OverlayPanel {
	private static class ConstantRatioImageBox extends ImageBox {
		private int imagex, imagey;
		
		@Override
		public void setImage(Image image) { 
			if (image == null) {
				baseImage = null;
				this.image = null;
			} else {
				baseImage = image.resize(image.getWidth(), image.getHeight());
				if (w < h) {
					this.image = baseImage.resizeScaledWidth(w);
				} else if (w > h) {
					this.image = baseImage.resizeScaledHeight(h);
				} 
			}
		}
		
		@Override
		public void setSize(int w, int h) {
			this.w = w;
			this.h = h;
			
			if (baseImage != null) {
				if (w <= h) {
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
			    Draw.image(this.image, imagex, imagey);
			}
		}
	}
	
	private static final Image greyImage(Image img) {
		Image returnImage = new Image(img);
		Draw.setColors(new Color(180, 180, 180, 200), null);
		Draw.rect(returnImage, 0, 0, returnImage.getWidth(), returnImage.getHeight());
		return returnImage;
	}
	
	private static final CustomDraw DRAW_ON_SELECTION = new CustomDraw() {
		@Override
		public void draw(Widget w) {
		    Draw.setFill(null);
		    Draw.setStroke(Color.CYAN, 3);
		    Draw.rect(w.getX(), w.getY(), w.getWidth(), w.getHeight());
		}
	};
	
	private static final Image SHIELD_IMAGE = new Image(ResLoader.load("status/shield.png"));
	private static final Image SHIELD_EMPTY = greyImage(SHIELD_IMAGE);
	private static final Image STUN_IMAGE = new Image(ResLoader.load("status/stun.png"));
	private static final Image STUN_EMPTY = greyImage(STUN_IMAGE);
	
	private CardDisplayPanel[] display;
	private ConstantRatioImageBox[][] status;
	
	private Widget playerSelection, opponentSelection;
	private String playerSelectionPos, opponentSelectionPos;
	private int opponentPosition;
	
	public OutwitOverlay(DescriptionBox descriptionBox, GameState game, int realPlayerIndex, CardDisplayPanel[] gameDisplay) {
		super("Outwit - choose a display item in your area and an opponents...", descriptionBox, game, realPlayerIndex);
		
		removeWidget(title);
		addWidget(title, 30, 0, 40, 90);
		
		ArrayList<Set<String>> validPlayers = Validator.validateOutwit(game);
		
		display = new CardDisplayPanel[gameDisplay.length];
		for (int i=0; i<gameDisplay.length; i++) {
			display[i] = new CardDisplayPanel(gameDisplay[i].getOrientation(), true);
			for (Widget w: gameDisplay[i].getWidgetList()) {
				CardWidget c = (CardWidget)w;
				display[i].addCard(new CardWidget(c.getType(), c.getValue()));
			}
			display[i].setEnabled(validPlayers.get(toGameTurn(i)) != null && validPlayers.get(toGameTurn(i)).contains(Flag.DISPLAY));
			display[i].setDrawContainingPanel(true);
			display[i].setBorderColor(null);
			display[i].setFillColor(new Color(255, 255, 255, 40));
		}
		
		status = new ConstantRatioImageBox[display.length][2];
		for (int i=0; i<status.length; i++) {
			boolean isValid = validPlayers.get(toGameTurn(i)) != null;
			
			boolean hasShield = isValid && validPlayers.get(toGameTurn(i)).contains(Flag.SHIELD);
			status[i][0] = new ConstantRatioImageBox(hasShield ? SHIELD_IMAGE : SHIELD_EMPTY);
			status[i][0].setEnabled(hasShield);
			
			boolean hasStun = isValid && validPlayers.get(toGameTurn(i)).contains(Flag.STUN);
			status[i][1] = new ConstantRatioImageBox(hasStun ? STUN_IMAGE : STUN_EMPTY);
			status[i][1].setEnabled(hasStun);

		}
		
		switch(display.length) {
			case 2:
				addWidget(display[0], 17, 65, 66, 23);
				addWidget(display[1], 17,  6, 66, 23);
				
				addWidget(status[0][0], 41, 59, 5, 5); addWidget(status[0][1], 54, 59, 5, 5);
				addWidget(status[1][0], 41, 31, 5, 5); addWidget(status[1][1], 54, 31, 5, 5);
				break;
			case 3:
				addWidget(display[0], 25, 65, 50, 23);
				addWidget(display[1],  5, 15, 13, 70);
				addWidget(display[2], 82, 15, 13, 70);
				
				addWidget(status[0][0], 41, 59, 5, 5); addWidget(status[0][1], 54, 59, 5, 5);
				addWidget(status[1][0], 18, 32, 5, 5); addWidget(status[1][1], 18, 49, 5, 5);
				addWidget(status[2][0], 77, 32, 5, 5); addWidget(status[2][1], 77, 49, 5, 5);
				break;
			case 4:
				addWidget(display[0], 25, 65, 50, 23);
				addWidget(display[1],  5, 15, 13, 70);
				addWidget(display[2], 25,  6, 50, 23);
				addWidget(display[3], 82, 15, 13, 70);
				
				addWidget(status[0][0], 41, 59, 5, 5); addWidget(status[0][1], 54, 59, 5, 5);
				addWidget(status[1][0], 18, 32, 5, 5); addWidget(status[1][1], 18, 49, 5, 5);
				addWidget(status[2][0], 41, 31, 5, 5); addWidget(status[2][1], 54, 31, 5, 5);
				addWidget(status[3][0], 77, 32, 5, 5); addWidget(status[3][1], 77, 49, 5, 5);
				break;
			case 5:
				addWidget(display[0], 25, 65, 50, 23);
				addWidget(display[1],  5, 30, 13, 65);
				addWidget(display[2],  2,  6, 45, 20);
				addWidget(display[3], 53,  6, 45, 20);
				addWidget(display[4], 82, 30, 13, 65);
				
				addWidget(status[0][0], 41, 59, 5, 5); addWidget(status[0][1], 54, 59, 5, 5);
				addWidget(status[1][0], 18, 40, 5, 5); addWidget(status[1][1], 18, 50, 5, 5);
				addWidget(status[2][0], 30, 28, 5, 5); addWidget(status[2][1], 57, 28, 5, 5);
				addWidget(status[3][0], 38, 28, 5, 5); addWidget(status[3][1], 65, 28, 5, 5);
				addWidget(status[4][0], 77, 40, 5, 5); addWidget(status[4][1], 77, 50, 5, 5);
				break;
		}
	}

	@Override
	public void handleDescriptionBox() {
		first: for (CardDisplayPanel d: display) {
			for (Widget w: d.getWidgetList()) {
				CardWidget c = (CardWidget)w;
				if (c.isHovering()) {
					descriptionBox.setDisplay(c.getType(), c.getValue());
					break first;
				}
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		handleDescriptionBox();
		
		first: for (int i=0; i<display.length; i++) {
			for (int j=0; j<display[i].getWidgetList().size(); j++) {
				Widget w = display[i].getIndex(j);
				if (w.isClicked()) {
					if (i==0) {
						playerSelection = w;
						playerSelectionPos = ""+j;
					} else {
						opponentSelection = w;
						opponentSelectionPos = ""+j;
						opponentPosition = toGameTurn(i);
					}
					break first;
				}
			}
			if (status[i][0].isClicked()) {
				if (i==0) {
					playerSelection = status[i][0];
					playerSelectionPos = Flag.SHIELD;
				} else {
					opponentSelection = status[i][0];
					opponentSelectionPos = Flag.SHIELD;
					opponentPosition = i;
				}
				break;
			} else if (status[i][1].isClicked()) {
				if (i==0) {
					playerSelection = status[i][1];
					playerSelectionPos = Flag.STUN;
				} else {
					opponentSelection = status[i][1];
					opponentSelectionPos = Flag.STUN;
					opponentPosition = toGameTurn(i);
				}
				break;
			}
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		
		if (playerSelection != null) {
			DRAW_ON_SELECTION.draw(playerSelection);
		}
		if (opponentSelection != null) {
			DRAW_ON_SELECTION.draw(opponentSelection);
		}
		
		if (playerSelection != null && opponentSelection != null) {
			String playerDeck, opponentDeck;
			if (playerSelectionPos.equals(Flag.SHIELD) || playerSelectionPos.equals(Flag.STUN)) {
				playerDeck = playerSelectionPos;
				playerSelectionPos = "0";
			} else { 
				playerDeck = Flag.DISPLAY;
			}
			if (opponentSelectionPos.equals(Flag.SHIELD) || opponentSelectionPos.equals(Flag.STUN)) {
				opponentDeck = opponentSelectionPos;
				opponentSelectionPos = "0";
			} else { 
				opponentDeck = Flag.DISPLAY;
			}
			
			overlayActionComplete = true;
			finalCommandString = playerDeck + ":" + playerSelectionPos + ":" + opponentPosition + ":" + opponentDeck + ":" + opponentSelectionPos;
		}
	}

}
