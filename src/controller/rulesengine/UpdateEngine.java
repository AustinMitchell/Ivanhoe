package controller.rulesengine;

import model.Flag;
import model.GameState;

public class UpdateEngine {	
	
	public static String playValueCard(GameState game, int cardPos) {
		String returnString;
		returnString = (Flag.CARD + ":" + cardPos);
		
		return returnString;
	}
	
	public static String unhorse(GameState game, int cardPos, int colour) {
		String returnString;
		returnString = (Flag.CARD + ":" + cardPos + ":" + colour);
		
		return returnString;
	}
	
	public static String changeWeapon(GameState game, int cardPos, int colour) {
		String returnString;
		returnString = (Flag.CARD + ":"  + cardPos + ":" + colour);
		return returnString;
	}
	
	public static String dropWeapon(GameState game, int cardPos) {
		return (Flag.CARD + ":" + cardPos);
	}
	
	public static String breakLance(GameState game, int cardPos, int targetPos) {
		String returnString = (Flag.CARD + ":" + cardPos + ":" + targetPos);
		return returnString;
	}
	
	public static String riposte(GameState game, int cardPos, int targetPos) {
		String returnString = ( Flag.CARD + ":" + cardPos + ":" + targetPos);
		return returnString;
	}
	
	public static String unimplementedActionCard(GameState game, int cardPos) {
		String result = (Flag.CARD + ":" + cardPos);
		return result;
	}
	
	public static String dodge(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = Flag.CARD + ":" + cardPos + ":" + targetPos + ":" + targetCardPos;
		return returnString;
	}
	
	public static String retreat(GameState game, int cardPos, int targetCardPos) {
		String returnString = Flag.CARD + ":" + cardPos + ":" + targetCardPos;
		return returnString;
	}
	
	public static String knockdown(GameState game, int cardPos, int targetPos, int targetCardPos) {
		String returnString = Flag.CARD + ":" + cardPos + ":" + targetPos + ":" + targetCardPos;
		return returnString;
	}
	
	public static String outmaneuver(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;
		return returnString;
	}

	//Process Charge
	public static String charge(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;
		return returnString;
	}
	
	//Process Counterharge
	public static String countercharge(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;
		return returnString;
	}
	
	//Process Disgrace
	public static String disgrace(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;
		return returnString;
	}
	
	//Process Adapt (absolve)
	public static String adapt(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;
		return returnString;
	}
	
	//Process Outwit
	public static String outwit(GameState game, int cardPos, String playerDeck, int playerCardPos, int targetPos, String targetDeck, int targetCardPos) {
		String returnString = Flag.CARD + ":" + cardPos + ":" + playerDeck + ":" + playerCardPos + ":" + targetPos + ":" + targetDeck + ":" + targetCardPos;
		return returnString;
	}
	
	//Process Shield
	public static String shield(GameState game, int cardPos) {
		String returnString = Flag.CARD + ":" + cardPos;	
		return returnString;
	}
	
	//Process Stun
	public static String stun(GameState game, int cardPos, int targetPos) {
		String returnString = Flag.CARD + ":" + cardPos + ":" + targetPos;
		return returnString;
	}
	
	public static String ivanhoe(GameState game, boolean played) {
		String returnString = Flag.IVANHOE_RESPONSE + ":" + (played?"true":"false");
		return returnString;
	}
	
	public static String pickToken() {
		String returnString =  Flag.PICK_TOKEN;
		return returnString;
	}
	
	public static String awardToken(GameState game, String colour) {
		String returnString =  Flag.AWARD_TOKEN + Flag.COM_SPLIT + colour;
		return returnString;
	}
	
	public static String endGame(GameState game) {
		String returnString =  Flag.END_GAME;
		return returnString;
	}
	

	public static Object endTurn(GameState game) {
		return Flag.END_TURN;
	}

}
