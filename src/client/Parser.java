package client;

import java.util.Queue;

import models.GameState;

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
		
		
		
		return null;
	}

}
