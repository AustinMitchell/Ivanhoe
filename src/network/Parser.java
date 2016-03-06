package network;

import java.util.ArrayList;
import java.util.Queue;

import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Validator;

public class Parser {
	
	/*
	 * add an incoming flag to guiFlags queue
	 * this method is capable of parsing multiple commands passed as a single string then
	 * add each command to the queue as a single string
	 * 
	 * NOTE: commands must be separated by the single line character "\n"
	 * 		 DO NOT insert spaces before or after that new line character 
	 */
	public static void guiSplitter(Queue guiFlags, String flag) {
		
		if(flag.contains("$")) //check if there are multiple commands
		{
			String commands[] = flag.split("$");
			for(int i = 0; i < commands.length; i++) {
				guiFlags.add(commands[i]);
			}
		}
		else //the case of one single command
			guiFlags.add(flag);
	}
	
	

	public static void networkSplitter(String flag, GameState game) {
		String[] commands;
		if(flag.contains("$")) //check if there are multiple commands
		{
			commands = flag.split("$");
			for(int i = 0; i < commands.length; i++) {
				parse(commands[i].split(":"), game);
			}
		}
		else //the case of one single command
			parse(flag.split(":"), game);
	}
	
	
	
	public static String parse (String[] command, GameState game) {
		
		String result = "";
		synchronized(game) {
			switch (command[0]) {
			
				case "initClient": //initializes draw deck on the client side
					int i = 1;
					ArrayList<Player> players = new ArrayList();
					while(!command[i].equals("cards")) {
						players.add(new Player(command[i]));
						i++;
					}
					i++;
					int[][] cardData = new int[(command.length-i) / 2][2];
					for(int j = 0; j < cardData.length; j++) {
						cardData[j][0] =
								Integer.parseInt(
								command[j*2 + i]);
						cardData[j][1] = Integer.parseInt(command[j*2 + i + 1]);
					}
					game.initializeClient(players, cardData);
					break;
					
			
				case  "startGame":
					result = RulesEngine.startGame(game);	
					break;
				
				case "drawCard":
					result = RulesEngine.drawCard(game);
					if(!game.hasTournamentStarted()) {
						if(Validator.canStartTournament(game)) {
							result += RulesEngine.NEW_COM + "canStartTournament:true";
						}
						else {
							result += RulesEngine.NEW_COM + "canStartTournament:false";
						}
					}
					break;
					
				case "endTurn":
					result = RulesEngine.endTurn(game, command[1]);
					break;
					
				case "startTournament":
					result = RulesEngine.startTournament(game);
					break;
					
				case "setColour":
					result = RulesEngine.setColour(game, command[1]);
					break;
					
				case "card":
					result = RulesEngine.playValueCard(game, Integer.getInteger(command[1]));
					break;
					
				case "unhorseCard":
					result = RulesEngine.unhorse(game, Integer.getInteger(command[1]), Integer.getInteger(command[2]));
					break;
					
				case "changeWeaponCard":
					result = RulesEngine.changeWeapon(game, Integer.getInteger(command[1]), Integer.getInteger(command[2]));
					break;
					
				case "dropWeaponCard":
					result = RulesEngine.dropWeapon(game, Integer.getInteger(command[1]));
					break;
					
				case "dummyCard":
					result = RulesEngine.unimplementedActionCard(game, Integer.getInteger(command[1]));
					break;
					
			}
		}
		
		return result;
	}

}
