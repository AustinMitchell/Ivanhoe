package view.panel.overlay;

import model.*;
import simple.gui.Button;
import view.DescriptionBox;

public class IvanhoeOverlay extends OverlayPanel {	
	String cardName;
	Button playIvanhoe;
	Button doNothing;
	long startTime;
	
	public IvanhoeOverlay(DescriptionBox descriptionBox, String[] command, GameState game, int thisPlayer) {
		super("", descriptionBox);
		Player instigator = game.getAllPlayers().get(game.getTurn());
		int cardType = instigator.getHand().getCard(Integer.parseInt(command[1])).getCardType();
		int cardValue = instigator.getHand().getCard(Integer.parseInt(command[1])).getCardValue();
		
		title.setText(instigator.getName() + " played " + CardData.getCardName(cardType, cardValue));
		
		playIvanhoe = new Button("Play Ivanhoe");
		doNothing = new Button("Continue");
		
		boolean hasIvanhoe = false;
		if (game.getAllPlayers().get(thisPlayer).isInTournament()) {
			for (int i=0; i<game.getAllPlayers().get(thisPlayer).getHand().deckSize(); i++) {
				Card c = game.getAllPlayers().get(thisPlayer).getHand().getCard(i);
				if (c.getCardType() == Type.ACTION && c.getCardValue() == Card.IVANHOE) {
					hasIvanhoe = true;
					break;
				}
			}
		}
		
		
		if (hasIvanhoe) {
			addWidget(doNothing, 39, 90, 8, 8);
			addWidget(playIvanhoe, 61, 90, 8, 8);
		} else {
			addWidget(doNothing, 45, 90, 10, 8);
		}
		
		startTime = System.currentTimeMillis();
	}

	@Override
	public void handleDescriptionBox() {
		
	}
	
	@Override
	public void update() {
		super.update();
		if (!overlayActionComplete) {
			if (System.currentTimeMillis() - startTime > 8000) {
				finalCommandString = "false";
				overlayActionComplete = true;
			}
			
			if (doNothing.isClicked()) {	
				finalCommandString = "false";
				overlayActionComplete = true;
			} else if (playIvanhoe.isClicked()) {
				finalCommandString = "true";
				overlayActionComplete = true;
			}
		}
	}
}
