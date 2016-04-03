package model;

import java.util.*;
import java.io.*;

import simple.gui.Image;
import view.ResLoader;

// Static utility class to store data for cards (i.e. name, description and image)
public class CardData {
	
	private static final int[] INITIAL_DECK_TYPE = {Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, 
		Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, 
		Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.PURPLE, Type.RED, 
		Type.RED, Type.RED, Type.RED, Type.RED, Type.RED, 
		Type.RED, Type.RED, Type.RED, Type.RED, Type.RED, 
		Type.RED, Type.RED, Type.RED, Type.BLUE, Type.BLUE, 
		Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, 
		Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, Type.BLUE, 
		Type.BLUE, Type.BLUE, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
		Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
		Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, Type.YELLOW, 
		Type.YELLOW, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
		Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
		Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, Type.GREEN, 
		Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
		Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
		Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
		Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, Type.WHITE, 
		Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
		Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
		Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, 
		Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION, Type.ACTION};
	
	private static Card[] UNMODIFIED_ARRAY() {
		int[] arr =    {3, 3, 3, 3, 4, 
						4, 4, 4, 5, 5, 
						5, 5, 7, 7, 3, 
						3, 3, 3, 3, 3, 
						4, 4, 4, 4, 4, 
						4, 5, 5, 2, 2, 
						2, 2, 3, 3, 3, 
						3, 4, 4, 4, 4, 
						5, 5, 2, 2, 2, 
						2, 3, 3, 3, 3, 
						3, 3, 3, 3, 4, 
						4, 1, 1, 1, 1, 
						1, 1, 1, 1, 1, 
						1, 1, 1, 1, 1, 
						2, 2, 2, 2, 2, 
						2, 2, 2, 3, 3, 
						3, 3, 3, 3, 3, 
						3, 6, 6, 6, 6};
		Card[] cards = new Card[110];
		for (int i=0; i<90; i++) {
			cards[i] = new Card(INITIAL_DECK_TYPE[i], arr[i]);
		}
		return cards;
	}
	
	public static final Card[] basicDeck() {
		Card[] cards = UNMODIFIED_ARRAY();
		
		cards[90]  = new Card(Type.ACTION, Card.UNHORSE);
		cards[91]  = new Card(Type.ACTION, Card.CHANGE_WEAPON);
		cards[92]  = new Card(Type.ACTION, Card.DROP_WEAPON);
		cards[93]  = new Card(Type.ACTION, Card.BREAK_LANCE);
		cards[94]  = new Card(Type.ACTION, Card.RIPOSTE);
		cards[95]  = new Card(Type.ACTION, Card.DODGE);
		cards[96]  = new Card(Type.ACTION, Card.RETREAT);
		cards[97]  = new Card(Type.ACTION, Card.KNOCKDOWN);
		cards[98]  = new Card(Type.ACTION, Card.OUTMANEUVER);
		cards[99]  = new Card(Type.ACTION, Card.CHARGE);
		cards[100] = new Card(Type.ACTION, Card.COUNTERCHARGE);
		cards[101] = new Card(Type.ACTION, Card.DISGRACE);
		cards[102] = new Card(Type.ACTION, Card.ADAPT);
		cards[103] = new Card(Type.ACTION, Card.OUTWIT);
		cards[104] = new Card(Type.ACTION, Card.SHIELD);
		cards[105] = new Card(Type.ACTION, Card.STUNNED);
		cards[106] = new Card(Type.ACTION, Card.IVANHOE);
		cards[107] = new Card(Type.ACTION, Card.RIPOSTE);
		cards[108] = new Card(Type.ACTION, Card.RIPOSTE);
		cards[109] = new Card(Type.ACTION, Card.KNOCKDOWN);
		return cards;
	}
	
	public static final Card[] specificActionDeck(int actionCardValue) {
		Card[] cards = UNMODIFIED_ARRAY();
		for (int i=90; i<110; i++) {
			cards[i] = new Card(Type.ACTION, actionCardValue);
		}
		return cards;
	}
	
	private static Image BACK_IMAGE;
	
	private static final String DESCRIPTION_PATH = "descriptions.txt";
	private static final String VALUE_PATH = "cards/value/";
	private static final String ACTION_PATH = "cards/action/";
	// Stores image object, name and description for a card
	private static class Data {
		Image image;
		String name, description;
		Data setImage(Image image) {
			this.image = image;
			return this;
		}
	}
	
	// Creates a data structure that maps two integers to an image, name and description

	private static ArrayList<HashMap<Integer, Data>> CARD_IMAGE_MAP;
		
	public static void initialize() {
		BACK_IMAGE = new Image(ResLoader.load("cards/back.png"));
		
		CARD_IMAGE_MAP = new ArrayList<HashMap<Integer, Data>>() {{
			final Queue<Data> descriptions = new LinkedList<Data>() {{
					try {
			            // FileReader reads text files in the default encoding.
			            // Always wrap FileReader in BufferedReader.
			            BufferedReader bufferedReader = 
			                new BufferedReader(new InputStreamReader(ResLoader.load(DESCRIPTION_PATH)));
	
			            String line = null;
			            List<String> lines = new ArrayList<String>();
			            while((line = bufferedReader.readLine()) != null) {
			            	if (line.equals("~~~")) {
			            		lines.add("");
			            		
			            		Data d = new Data();
			            		d.name = lines.get(0);
			            		d.description = lines.get(1);
			            		
			            		add(d);
			            		
			            		lines = new ArrayList<String>();
			            	} else {
			            		lines.add(line);
			            	}
			            }   
	
			            // Always close files.
			            bufferedReader.close();         
			        }
			        catch(FileNotFoundException ex) {
			            System.out.println("Unable to open file '" + DESCRIPTION_PATH + "'");                
			        }
			        catch(IOException ex) {
			            System.out.println("Error reading file '" + DESCRIPTION_PATH + "'");                  
			        }
				}};
		
			HashMap<Integer, Data> purple = new HashMap<Integer, Data>(){{
					put(3, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"0-0.jpeg"))));
					put(4, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"0-1.jpeg"))));
					put(5, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"0-2.jpeg"))));
					put(7, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"0-3.jpeg"))));
				}};
			HashMap<Integer, Data> red = new HashMap<Integer, Data>(){{
					put(3, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"1-0.jpeg"))));
					put(4, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"1-1.jpeg"))));
					put(5, descriptions.remove().setImage( new Image(ResLoader.load(VALUE_PATH+"1-2.jpeg"))));
				}};
			HashMap<Integer, Data> yellow = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"2-0.jpeg"))));
					put(3, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"2-1.jpeg"))));
					put(4, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"2-2.jpeg"))));
				}};
			HashMap<Integer, Data> blue = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"3-0.jpeg"))));
					put(3, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"3-1.jpeg"))));
					put(4, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"3-2.jpeg"))));
					put(5, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"3-3.jpeg"))));
				}};
			HashMap<Integer, Data> green = new HashMap<Integer, Data>(){{
					put(1, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"4-0.jpeg"))));
				}};
			HashMap<Integer, Data> white = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"5-0.jpeg"))));
					put(3, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"5-1.jpeg"))));
					put(6, descriptions.remove().setImage(new Image(ResLoader.load(VALUE_PATH+"5-2.jpeg"))));
				}};
			
			HashMap<Integer, Data> action = new HashMap<Integer, Data>(){{
					for (int i=0; i<17; i++) {

						put(i, descriptions.remove().setImage(new Image(ResLoader.load(ACTION_PATH + i + ".jpeg"))));

					}
				}};
				
			add(purple);
			add(red);
			add(yellow);
			add(blue);
			add(green);
			add(white);
			add(action);
		}};
	}

	// Rrturns image object for the back of cards
	public static Image getBackImage() { 
		return BACK_IMAGE;
	}
	// Returns image object for given card
	public static Image getCardImage(int type, int value) {
		return CARD_IMAGE_MAP.get(type).get(value).image;
	}
	// Returns name of given card
	public static String getCardName(int type, int value) {
		String name = CARD_IMAGE_MAP.get(type).get(value).name;
		return (name==null ? "" : name);
	}
	// Return description of given card
	public static String getCardDescription(int type, int value) {
		String description = CARD_IMAGE_MAP.get(type).get(value).description;
		return (description==null ? "" : description);
	}
}
