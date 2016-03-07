package network;

import java.util.ArrayList;
import java.util.Queue;

import models.Card;
import models.GameState;
import models.Player;
import rulesengine.RulesEngine;
import rulesengine.Type;
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
	public static void guiSplitter(Queue<String> guiFlags, String flag) {
		
		if(flag.contains(RulesEngine.NEW_COM)) //check if there are multiple commands
		{
			String commands[] = flag.split(RulesEngine.NEW_COM);
			for(int i = 0; i < commands.length; i++) {
				guiFlags.add(commands[i]);
			}
		}
		else { //the case of one single command
			guiFlags.add(flag);
		}
	}
	
	

	public static String networkSplitter(String flag, GameState game) {
		String[] commands;
		if(flag.contains(RulesEngine.NEW_COM)) //check if there are multiple commands
		{
			commands = flag.split(RulesEngine.NEW_COM);
			for(int i = 0; i < commands.length; i++) {
				commands[i] = parse(commands[i].split(":"), game);
			}
			return String.join(RulesEngine.NEW_COM, commands);
		}
		else //the case of one single command
			return parse(flag.split(":"), game);
	}
	
	
	
	public static String parse (String[] command, GameState game) {
		
		String result = "";
		synchronized(game) {
			switch (command[0]) {
			
				case "initClient": { //initializes draw deck on the client side
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
					result = "initClient";
					break;
				}
					
				case "renewDeck":
					
					int[][] cardData = new int[(command.length-1) / 2][2];
					for(int j = 0; j < cardData.length; j++) {
						cardData[j][0] =
								Integer.parseInt(
								command[j*2 + 1]);
						cardData[j][1] = Integer.parseInt(command[j*2 + 2]);
					}
					
					game.renewDrawDeck(cardData);
					result = "renewDrawDeck";
					
					break;
					
			
				case  "startGame":
					result = RulesEngine.startGame(game);	
					break;
				
				case "drawCard":
					result = RulesEngine.drawCard(game);
					if(!game.hasTournamentStarted()) {
						result += RulesEngine.NEW_COM + "startTournament";
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
					int currPlayer = game.getTurn();
					int type = game.getAllPlayers().get(currPlayer).getHand().getCard(Integer.parseInt(command[1])).getCardType();
					int value = game.getAllPlayers().get(currPlayer).getHand().getCard(Integer.parseInt(command[1])).getCardValue();
					
					switch (type) {
						case Type.ACTION: 
							switch (value) {
								case Card.UNHORSE:
									result = RulesEngine.unhorse(game, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
									break;
									
								case Card.CHANGE_WEAPON:
									result = RulesEngine.changeWeapon(game, Integer.parseInt(command[1]), Integer.parseInt(command[2]));
									break;
									
								case Card.DROP_WEAPON:
									result = RulesEngine.dropWeapon(game, Integer.parseInt(command[1]));
									break;
									
								default:
									result = RulesEngine.unimplementedActionCard(game, Integer.parseInt(command[1]));
								
							}
							break;
						default: 
							result = RulesEngine.playValueCard(game, Integer.parseInt(command[1]));
							break;
					}	
				break;
			}
			
		}
		
		return result;
	}

}
