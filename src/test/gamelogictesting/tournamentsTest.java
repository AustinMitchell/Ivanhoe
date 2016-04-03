package test.gamelogictesting;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import controller.rulesengine.RulesEngine;
import model.Card;
import model.GameState;
import model.Player;
import model.Type;

public class tournamentsTest {
	Player player1;
	Player player2;
	Player player3;
	Player player4;
	ArrayList<Player> players;
	GameState game;
	
	//setup a game with 4 players and customized hands for testing purposes
	@Before
	public void setupGameState() {
		players = new ArrayList<Player>();
		player1 = new Player("Nick");
		player1.enterTournament();
		player2 = new Player("Austin");
		player2.enterTournament();;
		player3 = new Player("Ahmed");
		player3.enterTournament();
		player4 = new Player("Mike");
		player4.enterTournament();
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		game = new GameState();
		game.initializeServer(players);

		//Setting tournament colour through the rules engine method in order to set tournamentStarted in game state to true 
		RulesEngine.setColour(game, String.valueOf(Type.BLUE));
		
		

		//create cards to be added to player's hand and target's display
		Card purpleFive = new Card(Type.PURPLE, 5);
		Card purpleSeven = new Card(Type.PURPLE, 7);
		Card redFour = new Card(Type.RED, 4);
		
		Card squireTwo = new Card(Type.WHITE, 2);
		Card squireThree = new Card(Type.WHITE, 3);
		Card maiden = new Card (Type.WHITE, 6);
		Card blueTwo = new Card(Type.BLUE, 2);
		Card blueThree = new Card(Type.BLUE, 3);
		Card blueFour = new Card(Type.BLUE, 4);
		Card blueFive = new Card(Type.BLUE, 5);
		
		game.getHand(0).emptyDeck(game.getDiscardDeck());
		game.getHand(1).emptyDeck(game.getDiscardDeck());
		game.getHand(2).emptyDeck(game.getDiscardDeck());
		game.getHand(3).emptyDeck(game.getDiscardDeck());
		

		//Give target players custom hands
		game.getHand(0).add(squireTwo);
		game.getHand(0).add(squireThree);
		game.getHand(0).add(blueTwo);
		game.getHand(0).add(squireThree);

		game.getHand(1).add(squireThree);
		game.getHand(1).add(blueFour);
		game.getHand(1).add(squireThree);
		game.getHand(1).add(blueThree);
		game.getHand(1).add(squireTwo);
		
		game.getHand(2).add(maiden);
		game.getHand(2).add(squireTwo);
		game.getHand(2).add(squireThree);
		game.getHand(2).add(blueFour);
		game.getHand(2).add(blueTwo);

		game.getHand(3).add(maiden);
		game.getHand(3).add(squireThree);
		game.getHand(3).add(squireTwo);
		game.getHand(3).add(maiden);
		game.getHand(3).add(blueFive);
	}
	
	
	/*
	 * one player draws/starts, others draw but do not participate (ie withdraws)
	 */
	@Test
	public void playerStartsAndWinsTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//Make sure the first player has won a blue token
		assertTrue(game.getPlayer(0).playerHasToken());
		assertTrue(game.getPlayer(0).checkToken(Type.BLUE));
		
		//test to make sure only player one is still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertFalse(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
	}

	
	/*
	 * one player draws/starts, others draw but only one participates by playing a card
	 */
	@Test
	public void playerStartsAndOponentPlaysOneCardTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);

		assertTrue(game.getPlayer(1).isInTournament());
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//test to make sure only players 1 and 2 are still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertTrue(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
	}


	/*
	 * one player draws/starts, others draw but only one participates by playing several cards
	 */
	@Test
	public void playerStartsAndOponentPlaysSeveralCardsTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 4);
		RulesEngine.playValueCard(game, 0);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 2);

		assertTrue(game.getPlayer(1).isInTournament());
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//test to make sure only players 1 and 2 are still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertTrue(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
	}

	
	/*
	 * one player draws/starts, others draw and all participate by playing a card
	 */
	@Test
	public void playerStartsAndAllPlayOneCardTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);

		assertTrue(game.getPlayer(1).isInTournament());
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//third player plays blue four
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the third player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//fourth player plays blue five
		RulesEngine.playValueCard(game, 4);
		
		//test to make sure the fourth player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//test to make sure all players are still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertTrue(game.getPlayer(1).isInTournament());
		assertTrue(game.getPlayer(2).isInTournament());
		assertTrue(game.getPlayer(3).isInTournament());
	}

	
	/*
	 * one player draws/starts, others draw and all participate by playing several cards
	 */
	@Test
	public void playerStartsAndAllPlaySeveralCardsTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//third player plays blue four
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the third player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the third player's turn
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//fourth player plays blue five
		RulesEngine.playValueCard(game, 4);
		
		//test to make sure the fourth player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the fourth player's turn
		RulesEngine.endTurn(game);
		
		//test to make sure all players are still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertTrue(game.getPlayer(1).isInTournament());
		assertTrue(game.getPlayer(2).isInTournament());
		assertTrue(game.getPlayer(3).isInTournament());
	}

	
	/*
	 * starting with a supporter
	 */
	@Test
	public void startWithOneSupporterTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays squire two
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
	}

	
	/*
	 * starting with several supporters
	 */
	@Test
	public void startWithSeveralSupportersTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays squire two and squire three
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 3);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 2);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
	}

	
	/*
	 * a multiplayer tournament has several rounds where each player plays one 
	 * and then several supporters in different rounds 
	 */
	@Test
	public void playSupportersSeveralRoundTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays squire three
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three and squire two
		RulesEngine.playValueCard(game, 4);
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 2 card and his display increased by 2
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 2);
		
		//end the second player's turn
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//third player plays blue four and squire three and blue two
		RulesEngine.playValueCard(game, 2);
		RulesEngine.playValueCard(game, 2);
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the third player's hand decreased by 3 card and his display increased by 3
		assertEquals(game.getHand(currentPlayer).deckSize(), 3);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 3);
		
		//end the third player's turn
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//fourth player plays blue five and a maiden
		RulesEngine.playValueCard(game, 4);
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the fourth player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 2);
		
		//end the fourth player's turn
		RulesEngine.endTurn(game);
		
		//test to make sure only all players are still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertTrue(game.getPlayer(1).isInTournament());
		assertTrue(game.getPlayer(2).isInTournament());
		assertTrue(game.getPlayer(3).isInTournament());

		currentPlayer = game.getTurn(); //update current player position
		
		//first player plays several supporter cards (squire 2 and squire 3)
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 2 card and his display increased by 2
		assertEquals(game.getHand(currentPlayer).deckSize(), 2);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 3);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//second player plays several supporter cards (two squire three)
		RulesEngine.playValueCard(game, 2);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the second player's hand decreased by 2 card and his display increased by 2
		assertEquals(game.getHand(currentPlayer).deckSize(), 2);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 4);
		
		//end second player's turn
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//third player plays several supporter cards (squire two and maiden)
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the third player's hand decreased by 2 card and his display increased by 2
		assertEquals(game.getHand(currentPlayer).deckSize(), 1);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 5);
		
		//end third player's turn
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//fourth player plays several supporter cards (squire two and squire three)
		RulesEngine.playValueCard(game, 1);
		RulesEngine.playValueCard(game, 1);
		
		//test to make sure the fourth player's hand decreased by 2 card and his display increased by 2
		assertEquals(game.getHand(currentPlayer).deckSize(), 2);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 4);
		
		//end fourth player's turn
		RulesEngine.endTurn(game);
		
		//test to make sure only the third player remains in the tournament
		assertFalse(game.getPlayer(0).isInTournament());
		assertFalse(game.getPlayer(1).isInTournament());
		assertTrue(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
		
		
	}

	
	/*
	 *  trying to play cards that do not get the current player to beat the tournament 
	 *  originator (ie not enough to be the leader)
	 */
	@Test
	public void opponentsCantBeatPlayerTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two, squire two and squire three
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 2);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 3);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//third player plays blue four
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the third player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//fourth player plays blue five
		RulesEngine.playValueCard(game, 4);
		
		//test to make sure the fourth player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//test to make sure everyone failed to beat player 1
		assertTrue(game.getPlayer(0).isInTournament());
		assertFalse(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
	}

	/*
	 * trying to play invalid cards (wrong color)
	 */
	@Test
	public void playingWrongColourTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);

		//create a different coloured card to be given to the player
		Card redFour = new Card(Type.RED, 4);
		game.getHand(currentPlayer).add(redFour);
		
		//Test to make sure the first player's hand increased after drawing a card and adding the red card
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//first player plays red four (wrong colour)
		RulesEngine.playValueCard(game, 5);
		
		//test to make sure the player's hand and display remain unchanged
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 0);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
	}
	
	/*
	 * restriction to 1 maiden per player per tournament
	 */
	@Test
	public void maidenRestrictionTest() {
		game.setTurn(3); //set turn to the player with multiple maidens for testing purposes
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//first player plays two maidens
		RulesEngine.playValueCard(game, 3);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1. only 1 maiden played
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
	}

	
	/*
	 * winning and getting token
	 */
	@Test
	public void winAndGetTokenTest() {
		int currentPlayer = game.getTurn();
		
		//Make sure the first player does not have any tokens
		assertFalse(game.getPlayer(0).playerHasToken());
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two
		RulesEngine.playValueCard(game, 2);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//Make sure the first player has won a blue token
		assertTrue(game.getPlayer(0).playerHasToken());
		assertTrue(game.getPlayer(0).checkToken(Type.BLUE));
		
		//test to make sure only player one is still in the tournament
		assertTrue(game.getPlayer(0).isInTournament());
		assertFalse(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
	}

	
	/*
	 *  winning and choosing token when purple tournament
	 */
	
	
	/*
	 * losing with a maiden and losing a token
	 */
	@Test
	public void losingWithMaidenTest() {
		int currentPlayer = game.getTurn();
		
		//Test to make sure the player's hand is of size 4
		assertEquals(game.getHand(currentPlayer).deckSize(), 4);
		
		//first player draws card
		RulesEngine.drawCard(game);
		
		//Test to make sure the first player's hand increased after drawing a card
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//first player plays blue two, squire two and squire three
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 2);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 3);
		
		//end turn and move on to next player
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure second player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//second player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure second player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//second player plays blue three
		RulesEngine.playValueCard(game, 3);

		//test to make sure the second player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the second player's turn 
		RulesEngine.endTurn(game);
		
		currentPlayer = game.getTurn(); //update current player position
		
		//test to make sure third player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//third player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure third player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//third player plays blue four
		RulesEngine.playValueCard(game, 3);
		
		//test to make sure the third player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the third player's turn 
		RulesEngine.endTurn(game);

		currentPlayer = game.getTurn(); //update current player position
		
		//give player four a yellow token which he will lose after withdrawing 
		game.getPlayer(currentPlayer).setToken(Type.YELLOW, true);
		
		//test to make sure the player four indeed has a yellow token
		assertTrue(game.getPlayer(currentPlayer).checkToken(Type.YELLOW));
		
		//test to make sure fourth player's hand is of size 5
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		
		//fourth player draws a card 
		RulesEngine.drawCard(game);
		
		//test to make sure fourth player's hand increased
		assertEquals(game.getHand(currentPlayer).deckSize(), 6);
		
		//fourth player plays maiden
		RulesEngine.playValueCard(game, 0);
		
		//test to make sure the fourth player's hand decreased by 1 card and his display increased by 1
		assertEquals(game.getHand(currentPlayer).deckSize(), 5);
		assertEquals(game.getDisplay(currentPlayer).deckSize(), 1);
		
		//end the fourth player's turn 
		RulesEngine.endTurn(game);
		
		//test to make sure everyone failed to beat player 1
		assertTrue(game.getPlayer(0).isInTournament());
		assertFalse(game.getPlayer(1).isInTournament());
		assertFalse(game.getPlayer(2).isInTournament());
		assertFalse(game.getPlayer(3).isInTournament());
		
		//test to make sure the player four indeed has a yellow token
		assertFalse(game.getPlayer(currentPlayer).checkToken(Type.YELLOW));
	}

	/*
	 * winning the game
	 */
	public void winningGameTest() {
		int playerPos = 0;
		game.getPlayer(playerPos).setToken(Type.BLUE, true);
		game.getPlayer(playerPos).setToken(Type.GREEN, true);
		game.getPlayer(playerPos).setToken(Type.PURPLE, true);
		game.getPlayer(playerPos).setToken(Type.RED, true);
		game.getPlayer(playerPos).setToken(Type.YELLOW, true);
		
		assertTrue(game.getPlayer(playerPos).hasWonAll());
		
	}
}
