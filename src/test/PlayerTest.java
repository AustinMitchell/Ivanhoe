package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Card;
import model.Player;
import model.Type;
public class PlayerTest {

	Player player1;
	Card squireTwo;
	Card maiden;
	Card purpleSeven;
	Card redThree;
	Card yellowTwo;
	
	@Before
	public void setupPlayer() {
		player1 = new Player("Austin");
		squireTwo = new Card(Type.WHITE, 2);
		player1.getDisplay().add(squireTwo);
		maiden = new Card(Type.WHITE, 6);
		player1.getDisplay().add(maiden);
		purpleSeven = new Card(Type.PURPLE, 7);
		player1.getDisplay().add(purpleSeven);
		redThree = new Card(Type.RED, 3);
		player1.getDisplay().add(redThree);
		yellowTwo = new Card(Type.YELLOW, 2);
		player1.getDisplay().add(yellowTwo);
	}
	
	@Test
	public void test() {
		assertFalse(player1.hasWon());
		for(int i = 0; i < 5; i++) {
			player1.setToken(i, true);
		}
		assertTrue(player1.hasWon());
	}
	
	@Test
	public void displayValueTest() {
		assertEquals(5, player1.getDisplayValue(Type.GREEN));
		assertEquals(20, player1.getDisplayValue(Type.RED));
	}
	
	

}
