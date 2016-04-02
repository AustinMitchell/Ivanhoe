package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Card;
import model.Deck;

public class DeckTest {
	Card card1;
	Card card2;
	Deck deck1;
	Deck deck2;
	
	

	@Before
	public void deckSetup() {
		card1 = new Card(1,3);
		card2 = new Card(2,4);
		deck1 = new Deck();
		deck2 = new Deck();
	}
	
	@Test
	public void addTest() {
		assertEquals(0, deck1.deckSize());
		deck1.add(card1);
		assertEquals(1, deck1.deckSize());
		assertEquals(1, deck1.getCard(0).getCardType());
		assertEquals(3, deck1.getCard(0).getCardValue());
		deck1.add(card2);
		assertEquals(2, deck1.deckSize());
		assertEquals(2, deck1.getCard(1).getCardType());
		assertEquals(4, deck1.getCard(1).getCardValue());
	}
	
	
	@Test
	public void moveToTest() {
		deck1.add(card1);
		deck1.add(card2);
		deck1.moveCardTo(0, deck2);
		assertEquals(1, deck1.deckSize());
		assertEquals(1, deck2.deckSize());
		assertEquals(1, deck2.getCard(0).getCardType());
		assertEquals(3, deck2.getCard(0).getCardValue());
		deck1.moveCardTo(0, deck2);
		assertEquals(0, deck1.deckSize());
		assertEquals(2, deck2.deckSize());
		assertEquals(2, deck2.getCard(1).getCardType());
		assertEquals(4, deck2.getCard(1).getCardValue());
	}
	
	@Test
	public void emptyDeckTest() {
		deck1.add(card1);
		deck1.add(card2);
		deck1.emptyDeck(deck2);
		assertEquals(deck2.deckSize(), 2);
		assertEquals(deck1.deckSize(), 0);
	}

}
