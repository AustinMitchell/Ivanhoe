package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

public class Deck {
	private int cardNum;
	private ArrayList<Card> deck;
	
	public Deck() {
		deck = new ArrayList<Card>();
	}
	
	public void shuffle() {
		long seed = System.nanoTime();
		Collections.shuffle(deck, new Random(seed));
	}
	
	public void add(Card card) {
		deck.add(card);
	}
	
	public void moveCardTo(int pos, Deck newDeck) {
		Card card = deck.get(pos);
		deck.remove(pos);
		newDeck.add(card);
	}
	
	public void emptyDeck(Deck discardDeck) {
		int size = deck.size();
		for(int i = 0; i < size; i++) {
			Card card = deck.get(i);
			discardDeck.add(card);
		}
		deck = new ArrayList<Card>();
	}
	
	public void draw(Deck newDeck) {
		moveCardTo(deck.size()-1, newDeck);
	}
	
	public ArrayList<Card> getAllCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		for(int i = 0; i < deck.size(); i++) {
			cards.add(deck.get(i));
		}
		return cards;
	}
	
	public int deckSize() {
		cardNum = deck.size();
		return cardNum;
	}
	
	public Card getCard(int i) {
		return deck.get(i);
	}
	
	
	//A function to return a deck sorted by card value as a TreeMap
	public TreeMap<Card, Integer> sortedDeck() {
		TreeMap<Card, Integer> sortedDeck = new TreeMap<Card, Integer>(new Comparator<Card>() {
			public int compare(Card card1, Card card2) {
				return card1.getCardValue() - card2.getCardValue();
			}
		});
		for(int i = 0; i < deck.size(); i ++) {
			sortedDeck.put(deck.get(i), i);
		}
		return sortedDeck;
	}
		
	
	/*
	public ArrayList<Card> getDeck() {
		return deck;
	}
	*/
	
}
