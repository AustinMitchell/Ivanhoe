package model;

public class Type {
	public static final int PURPLE = 0;
	public static final int RED = 1;
	public static final int YELLOW = 2;
	public static final int BLUE = 3;
	public static final int GREEN = 4;
	public static final int WHITE = 5;
	public static final int ACTION = 6;
	
	public static String toString(int type) {
		switch(type) {
			case PURPLE:
				return "PURPLE";
			case RED:
				return "RED";
			case YELLOW:
				return "YELLOW";
			case BLUE:
				return "BLUE";
			case GREEN:
				return "GREEN";
			case WHITE:
				return "WHITE";
			case ACTION:
				return "ACTION";
			default:
				return null;
		}
	}
}
