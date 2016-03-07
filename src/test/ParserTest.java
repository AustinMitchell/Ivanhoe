package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import models.GameState;
import models.Player;
import network.*;
import rulesengine.RulesEngine;
import rulesengine.Type;
import rulesengine.Validator;

public class ParserTest {
	Queue<String> queue;
	RulesEngine engine;
	GameState game;
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	Player player5;
	ArrayList<Player> players;
	
	@Before
	public void setupClient() {
		Client client = new Client();
		queue = client.getGuiFlags();
		players = new ArrayList<Player>();
		player1 = new Player("Ahmed");
		player2 = new Player("Nick");
		player3 = new Player("Austin");
		player4 = new Player("Mike");
		player5 = new Player("Malik");
		game = new GameState();
		engine = new RulesEngine();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		players.add(player5);
		game = new GameState();
		game.initializeServer(players);
	}

	@Test
	public void guiSplitterTest() {
		Parser.guiSplitter(queue, ("command 1" + RulesEngine.NEW_COM + "command 2"));
		assertEquals(queue.peek(), "command 1");
	}
	
	@Test
	public void parseInitClientTest() {
		
		GameState serverGame = new GameState();
		GameState clientGame = new GameState();
		//serverGame.initializeServer(players);
		
		Parser.networkSplitter(serverGame.initializeServer(players), clientGame);
		
		assertEquals(clientGame.getAllPlayers().size(), 5);
	}
	
	@Test
	public void renewDrawDeckTest() {
		GameState clientGame = new GameState();
		Parser.networkSplitter(clientGame.initializeServer(players), clientGame);
		assertEquals(clientGame.getDrawDeck().deckSize(), 70);
		clientGame.getDrawDeck().emptyDeck(clientGame.getDiscardDeck());
		assertEquals(clientGame.getDiscardDeck().deckSize(), 70);
		assertEquals(clientGame.getDrawDeck().deckSize(), 0);
		Parser.networkSplitter("renewDeck:2:3:1:4:6:2", clientGame);
		assertEquals(clientGame.getDrawDeck().deckSize(), 3);
	}
	
	@Test
	public void startGameTest() {
		GameState serverGame = new GameState();
		serverGame.initializeServer(players);
		String[] command = {"startGame"};
		
		assertEquals(Parser.parse(command, serverGame), "startGame");
	}
	
	@Test
	public void drawCardTest() {
		GameState serverGame = new GameState();
		serverGame.initializeServer(players);
		serverGame.setTournamentColour(Type.BLUE);
		serverGame.getAllPlayers().get(serverGame.getTurn()).getHand().emptyDeck(serverGame.getDiscardDeck());
		String[] command = {"drawCard"};
		
		String output = Parser.parse(command, serverGame);
		if(Validator.canStartTournament(serverGame)) {
			assertEquals(output, "drawCard:" + serverGame.getTurn() + RulesEngine.NEW_COM + "startTournament" + RulesEngine.NEW_COM + "canStartTournament:true");
		}
		else {
			assertEquals(output, "drawCard:" + serverGame.getTurn() + RulesEngine.NEW_COM + "startTournament" + RulesEngine.NEW_COM + "canStartTournament:false");
		}
	}
	
	@Test
	public void endTurnTest() {
		GameState serverGame = new GameState();
		serverGame.initializeServer(players);
		serverGame.setTournamentColour(Type.BLUE);
		serverGame.getAllPlayers().get(serverGame.getTurn()).getHand().emptyDeck(serverGame.getDiscardDeck());
		String[] command = {"endTurn", "false"};
		
		assertEquals(Parser.parse(command, serverGame), "endTurn:false");
	}
	
	@Test
	public void startTournamentTest() {
		GameState serverGame = new GameState();
		serverGame.initializeServer(players);
		serverGame.setTournamentColour(Type.BLUE);
		serverGame.getAllPlayers().get(serverGame.getTurn()).getHand().emptyDeck(serverGame.getDiscardDeck());
		String[] command = {"startTournament"};
		assertEquals(Parser.parse(command, serverGame), "startTournament:0");
	}
	
	@Test
	public void setColourTest() {
		GameState serverGame = new GameState();
		serverGame.initializeServer(players);
		serverGame.setTournamentColour(Type.BLUE);
		serverGame.getAllPlayers().get(serverGame.getTurn()).getHand().emptyDeck(serverGame.getDiscardDeck());
		String[] command = {"setColour", "2"};
		assertEquals(Parser.parse(command, serverGame), "setColour:2");
	}

}
