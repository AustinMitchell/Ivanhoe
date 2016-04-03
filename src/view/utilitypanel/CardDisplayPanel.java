package view.utilitypanel;

import java.util.*;

import simple.gui.Image;
import simple.gui.Widget;
import simple.gui.panel.*;
import view.CardWidget;

public class CardDisplayPanel extends Panel {
	private int drawnOnTop;
	Image.Orientation orientation;
	boolean faceUp, cardMoveOnHover;
	
	public Image.Orientation getOrientation() { return orientation; }
	public boolean isFaceUp() { return faceUp; }
	
	@Override
	public void setSize(int w, int h) {
		super.setSize(w, h);
		for (Widget card: widgetList) {
			setCardSize((CardWidget)card);
		}
		setCardPositions();
	}
	@Override
	public void setWidth(int w) { setSize(w, this.h); }
	@Override
	public void setHeight(int h) { setSize(this.w, h); }
	
	@Override
	public void blockWidget() {
		super.blockWidget();
		for (Widget w: widgetList) {
			w.blockWidget();
		}
	}
	
	public void setCardMoveOnHover(boolean cardMoveOnHover) {
		this.cardMoveOnHover = cardMoveOnHover;
		for (Widget w: widgetList) {
			((CardWidget)w).setMoveOnHover(cardMoveOnHover);
		}
	}
	
	public CardDisplayPanel(Image.Orientation orientation, boolean faceUp) {
		this(0, 0, 10, 10, orientation, faceUp);
	}
	public CardDisplayPanel(int x, int y, int w, int h, Image.Orientation orientation, boolean faceUp) {
		super(x, y, w, h);
		drawnOnTop = -1;
		this.orientation = orientation;
		priorityMap = new TreeMap<Integer, List<Widget>>(new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return i2-i1;
				}
			});
		
		this.faceUp = faceUp;
		cardMoveOnHover = true;
	}
	
	private void setCardPositions() {
		if (widgetList.size() == 0) {
			return;
		}
		
		if (orientation == Image.Orientation.UP || orientation == Image.Orientation.DOWN) {
			int cardWidth = widgetList.get(0).getWidth();
			float cardDistance;
			int cardStart;
			if (w > cardWidth*widgetList.size() || widgetList.size() <= 1) {
				cardStart = x + w/2 - cardWidth*widgetList.size()/2;
				cardDistance = cardWidth;
			} else {
				cardStart = x;
				cardDistance = (w - cardWidth) / (float)(widgetList.size()-1);
			}
			for (int i=0; i<widgetList.size(); i++) {
				widgetList.get(i).setLocation(cardStart +(int)(cardDistance*i), y);
			}
				
		} else {
			int cardHeight = widgetList.get(0).getHeight();
			float cardDistance;
			int cardStart;
			if (h > cardHeight*widgetList.size() || widgetList.size() <= 1) {
				cardStart = y + h/2 - cardHeight*widgetList.size()/2;
				cardDistance = cardHeight;
			} else {
				cardStart = y;
				cardDistance = (h - cardHeight) / (float)(widgetList.size()-1);
			}
			for (int i=0; i<widgetList.size(); i++) {
				widgetList.get(i).setLocation(x, cardStart + (int)(cardDistance*i));
			}
		}
	}
	private void setCardSize(CardWidget card) {
		if (orientation == Image.Orientation.UP || orientation == Image.Orientation.DOWN) {
			card.setScaledHeight(h);
		} else {
			card.setScaledWidth(w);
		}
	}
	
	@Override
	public void setWidgetDimensions(Widget widget, Dimensions d) {}

	@Override
	public void addWidget(Widget newWidget, int priority) {}
	@Override
	public void addWidget(Widget newWidget, Dimensions d, Constraints c, int priority) {}
	@Override
	public void addWidget(Widget newWidget) {
		
	}
	public void addCard(CardWidget card) {
		addWidgetToCollection(card, widgetList.size());	
		card.setOrientation(orientation);
		card.setFaceUp(faceUp);
		card.setMoveOnHover(cardMoveOnHover);
		setCardSize(card);
		setCardPositions();
	}
	@Override
	public CardWidget removeIndex(int widgetID) {
		Widget removedWidget = super.removeIndex(widgetID);
		for (int i=widgetID; i<widgetList.size(); i++) {
			setWidgetPriority(widgetList.get(i), i);
		}
		setCardPositions();
		
		if (drawnOnTop == widgetID) {
			drawnOnTop = -1;
		}
		
		return (CardWidget)removedWidget;
	}
	public CardWidget removeCard(int type, int value) {
		int removeIndex = -1;
		for (int i=0; i<widgetList.size(); i++) {
			CardWidget c = (CardWidget)widgetList.get(i);
			if (c.getType() == type && c.getValue() == value) {
				removeIndex = i;
				break;
			}
		}
		
		return (removeIndex == -1 ? null : removeIndex(removeIndex));
	}

	@Override
	public void update() {
		if (!enabled || !visible) 
			return;
		
		updateClickingState();
		
		boolean setFlag = false;
		boolean mouseInPriorityWidget = false; 
		drawnOnTop = -1;
		
		for (int priority: priorityMap.keySet()) {
			for (Widget w: priorityMap.get(priority)) {
				if (mouseInPriorityWidget) {
					// Blocking a widget makes the widget think the mouse isn't in it.
					// This means if the mouse is hovering over two widgets, only the highest priority one will get mouse interaction.
					w.blockWidget();
				}
				w.update();
				if (w.isHovering()) {
					setFlag = true;
				}
			}
			if (setFlag && !mouseInPriorityWidget) {
				mouseInPriorityWidget = true;
				drawnOnTop = priority;
			}
		}
		
	}
	
	@Override
	public void draw() {
		super.draw();
		if (drawnOnTop != -1 && drawnOnTop < widgetList.size()) {
			widgetList.get(drawnOnTop).draw();
		}
	}
}
