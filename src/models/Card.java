package models;

import rulesengine.Type;

public class Card {
	
    private int value;
    private int type;
    public static final int UNHORSE = 0;
    public static final int CHANGE_WEAPON = 1;
    public static final int DROP_WEAPON = 2;
    public static final int BREAK_LANCE = 3;
    public static final int RIPOSTE = 4;
    public static final int DODGE = 5;
    public static final int RETREAT = 6;
    public static final int KNOCKDOWN = 7;
    public static final int OUTMANEUVER = 8;
    public static final int CHARGE = 9;
    public static final int COUNTERCHARGE = 10;
    public static final int DISGRACE = 11;
    public static final int ADAPT = 12;
    public static final int OUTWIT = 13;
    public static final int SHIELD = 14;
    public static final int STUNNED = 15;
    public static final int IVANHOE = 16;
 
    //constructor for regular typed cards
    public Card(int type, int value) {
		this.value = value;
		this.type = type;
    }
	
	public int getCardValue() {
        return value;
    }
	
	public int getCardType() {
		return type;
	}
	
	public boolean isActionCard() {
		return (type == Type.ACTION);
	}
	
	public boolean isIvanhoe() {
		return (type == Type.ACTION && value == IVANHOE);
	}
	
	public boolean isMaiden() {
		return (type == Type.WHITE && value == 6);
	}
}
