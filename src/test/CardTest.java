package test;
import models.Card;
import rulesengine.Type;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {
	Card purple;
	Card ivanhoe;
	Card squire;
	Card maiden;
	Card charge;

	
	@Before
	public void createCards() {
		purple = new Card(Type.PURPLE, 4);
		ivanhoe = new Card(Type.ACTION, Card.IVANHOE);
		squire = new Card(Type.WHITE, 3);
		maiden = new Card(Type.WHITE, 6);
		charge = new Card(Type.ACTION, Card.CHARGE);
	}
	
	
	@Test
	public void actionCardTest() {
		assertTrue(charge.isActionCard());
		assertFalse(purple.isActionCard());
	}
	
	@Test
	public void ivanhoeCardTest() {
		assertTrue(ivanhoe.isIvanhoe());
	}
	
	@Test
	public void maidenCardTest() {
		assertTrue(maiden.isMaiden());
		assertFalse(squire.isMaiden());
	}

}
