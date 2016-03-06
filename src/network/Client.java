package network;

import models.*;
import rulesengine.*;

import java.net.*;
import java.util.LinkedList;
import java.util.Queue;
import java.io.*;

public class Client implements Runnable{
	private GameState game;
	private BufferedReader in;
	private PrintWriter out;
	private int id;
	private String updateIn;
	private String updateOut;
	private Queue<String> guiFlags;
	
	
	//constructor
		public Client () {
			guiFlags = new LinkedList<String>();
			game = new GameState();
			updateIn = "";
			updateOut = "";
		}
	
	   public static void main(String [] args)
	   {
	      new Thread(new Client()).start();
	   }
	   
	   
	   public GameState getGame() {
		   synchronized(game) {
			   return game;
		   }
	   }
	   
	   public void sendMessage(String message) {
		   synchronized(updateOut) {
			   updateOut = message;
		   }
	   }
	

		@Override
		public void run() {
			String serverName = "localhost";
		      int port = Server.PORT;
		      try
		      {
		         System.out.println("Connecting to " + serverName + " on port " + port);
		         Socket socket = new Socket(serverName, port);
		         System.out.println("Just connected to " + socket.getRemoteSocketAddress());
		         
		         in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		         out = new PrintWriter(socket.getOutputStream(), true);
		         
		         id = Integer.parseInt(in.readLine());
		         out.println("Ahmed");
		         
		         for(;;) {
			         updateIn = in.readLine();
			         synchronized(guiFlags) {
			        	 Parser.guiSplitter(guiFlags, updateIn);
			         }
			         Parser.networkSplitter(updateIn, game);
			         if(game.getTurn() == id) {
			        	 while(true) {
			        		 synchronized(updateOut) {
				        		 if (!updateOut.equals("")) {
				        			 out.println(updateOut);
				        			 updateOut = "";
				        			 break;
				        		 }
			        		 }
			        	 }
			         }
		         }
		         
		         //socket.close();
		      }catch(IOException e)
		      {
		         e.printStackTrace();
		      }
			
		}
	
	
	
	
	
	/*
	 * =====================================================================================
	 * Creating guiFlags using a Queue where the flags will be stored
	 */
	
	
	//check if the flag queue is empty
	//this is used by the GUI to figure out whether there is a need
	//to update the UI or not
	public boolean isFlagQueueEmpty() {
		synchronized(guiFlags) {
			return guiFlags.isEmpty();
		}
	}
	
	
	public Queue<String> getGuiFlags() {
		synchronized(guiFlags) {
			return guiFlags;
		}
	}
	
	
	//this function will remove the first flag as soon as it is read
	public String readGuiFlag() {
		synchronized(guiFlags) {
			String flag = guiFlags.peek();
			guiFlags.remove();
			return flag;
		}
	}
	
	
	/*
	 * End of Flags preparation
	 * ======================================================================================
	 */
	
	
	
}
