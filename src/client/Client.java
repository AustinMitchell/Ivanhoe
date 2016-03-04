package client;

import models.*;
import rulesengine.*;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;


public class Client {
	private GameState game;
	
	/*
	 * =====================================================================================
	 * Creating guiFlags using a Queue where the flags will be stored
	 */
	
	private Queue<String> guiFlags;
	
	//constructor
	public Client () {
		guiFlags = new LinkedList<String>();
	}
	

	//check if the flag queue is empty
	//this is used by the GUI to figure out whether there is a need
	//to update the UI or not
	public boolean isFlagQueueEmpty() {
		return guiFlags.isEmpty();
	}
	
	
	/*
	 * add an incoming flag to guiFlags queue
	 * this method is capable of parsing multiple commands passed as a single string then
	 * add each command to the queue as a single string
	 * 
	 * NOTE: commands must be separated by the single line character "\n"
	 * 		 DO NOT insert spaces before or after that new line character 
	 */
	public void addGuiFlag(String flag) {
		
		if(flag.contains("$")) //check if there are multiple commands 
		{
			String commands[] = flag.split("$");
			for(int i = 0; i < commands.length; i++) {
				guiFlags.add(commands[i]);
				Parser.parse(commands[i].split(":"), game);
			}
		}
		else //the case of one single command
			guiFlags.add(flag);
	}
	
	
	//this function will remove the first flag as soon as it is read
	public String readGuiFlag() {
		String flag = guiFlags.peek();
		guiFlags.remove();
		return flag;
	}
	
	
	
	/*
	 * End of Flags preparation
	 * ======================================================================================
	 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public synchronized void handle(int ID, String input) {
		if (input.equals("quit!")) 
		{
			broadcast(ID + input);
			if (clients.containsKey(ID)) {
				clients.get(ID).send("quit!" + "\n");
				remove(ID);
			}}
		else if (input.equals("shutdown!")) {
			broadcast(ID + input);
			shutdown(); 
		}
		
		else if(input.contains("join")) {
			broadcast(ID + input);
			
			if(players.size() < numPlayers) {
				String clientName[] = input.split(" ");
				Player player = new Player(clientName[1]);
				players.add(player);
				if(players.size() == numPlayers) {
					broadcast("\nThe available players are:");
					for(int i = 0; i < players.size(); i++) {
						broadcast(String.valueOf(players.get(i).getName()));
					}
					broadcast("READY");
				}
			}
			else {
				broadcast(ID + input);
				String clientName[] = input.split(" ");
				Player kickedOut = new Player(clientName[1]);
				players.add(kickedOut);
				sendToOne(ID, "SORRY YOU ARE OVER CAPACITY");
				remove(ID);
				players.remove(players.size()-1);
				System.out.println("REMAINING PLAYERS ARE: ");
				for(int i = 0; i < players.size(); i++) {
					System.out.println(String.valueOf(players.get(i).getName()));
				}
			}
			
		}

		else if(input.startsWith("select"))
		{
			broadcast(ID + input);
			ServerThread from = clients.get(ID);
				String playerInfoArray[] = input.split(" ");
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getName().contains(String.valueOf(playerInfoArray[1]))) {
						int attackSpeed = Integer.valueOf((playerInfoArray[4]));
						int defenseSpeed = Integer.valueOf((playerInfoArray[6]));
						if((attackSpeed + defenseSpeed) < 4) {
							sendToOne(ID, "\nYour attack speed and defense speed must add up to at least 4, please try again");
						}
						else {
							players.get(i).setTarget(playerInfoArray[2]);
							players.get(i).setAttack(playerInfoArray[3]);
							players.get(i).setAttackSpd(Integer.valueOf(playerInfoArray[4]));
							players.get(i).setDefense(playerInfoArray[5]);
							players.get(i).setDefenseSpd(Integer.valueOf(playerInfoArray[6]));
							selectDone++;
							if(selectDone < players.size()) {
								sendToOne(ID, "Please wait until everyone is finished");
							}
						}
					}
				}
			if(selectDone == players.size()) {
				broadcast(ID + input);
				broadcast("\nPlease Roll");
			}
		}//}
		
		else if(input.contains("roll")) {
			broadcast(ID + input);
			String roll[] = input.split(" ");
			int rolled = Integer.valueOf(roll[2]);
			if (Integer.valueOf(roll[2]) < 7 && rolled > 0) {
				rollDone++;
				for(int i = 0; i < players.size(); i++) {
					if(players.get(i).getName().equals(String.valueOf(roll[1]))) {
						players.get(i).setRoll(rolled);
						players.get(i).setAttack(engine.processRoll(players.get(i).getAttack(), rolled));
					}
				}
				if(rollDone < players.size()) {
					sendToOne(ID, "Please wait until other players have rolled");
				}
			}
			else {
				sendToOne(ID, "\nInvalid roll! Please enter an integer between 1 and 6");
			}
			
			if(rollDone==players.size()) {
				for(int i = 0; i < players.size(); i++) {
					broadcast("\n" + players.get(i).getName() + " roll " + players.get(i).getRoll());
					Boolean foundTarget = false;
					Boolean hit = false;
					String target = players.get(i).getTarget();
					String targetDefense = "UNKNOWN";
					for(int j = 0; j < players.size(); j++) {
						if(players.get(i).getTarget().equalsIgnoreCase(players.get(j).getName())) {
							targetDefense = players.get(j).getDefense();
							hit = engine.processGameLogic(players.get(i).getAttack(), players.get(i).getAttackSpd(), 
									players.get(j).getDefense(), players.get(j).getDefenseSpd());
							if (hit) {
								players.get(j).setWound(players.get(j).getWound() + 1);
							}
						}
					}
					if(hit) {
						broadcast(target + " was successfully hit by " + players.get(i).getName() +
								" using " + players.get(i).getAttack() + " against " + targetDefense + "\n" + 
								target + " suffered 1 wound. \n");
					}
					else {
						broadcast(target + " was NOT hit by " + players.get(i).getName() +
								" using " + players.get(i).getAttack() + " against " + targetDefense + "\n" + 
								target + " did not suffer any wounds \n");
					}
				}
				roundsDone++;
				if (roundsDone < rounds) {
					int nextRound = roundsDone + 1;
					broadcast("End of round " + roundsDone + ". Please enter your select statements for round " +
							nextRound);
					selectDone = 0;
					rollDone = 0;
				}
				else {
					String results = "";
					for(int i = 0; i < players.size(); i++) {
						results = results.concat("\n"+players.get(i).getName() + ": " + players.get(i).getWound() + " wounds");
						
					}
					broadcast("\nGame Over! Final results are: " + results);
					broadcast("Thank you for playing :D");
					broadcast("quit!");
				}
			}
		}
		
	}*/
	
	
	
	
}
