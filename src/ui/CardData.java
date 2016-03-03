package ui;

import java.util.*;
import java.io.*;

import simple.gui.Image;

public class CardData {
	public static int[][] ALL_CARD_VALUES = {
		{0, 3}, {0, 4},{0, 5},{0, 7},
		{1, 3}, {1, 4},{1, 5},
		{2, 2}, {2, 3},{2, 4},
		{3, 2}, {3, 3},{3, 4},{3, 5},
		{4, 1},
		{5, 2}, {5, 3},{5, 6},
		
		{6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8},
		{6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14}, {6, 15}, {6, 16}};
	
	private static final Image BACK_IMAGE = new Image("res/cards/back.png");
	
	private static final String DESCRIPTION_PATH = "res/descriptions.txt";
	private static final String VALUE_PATH = "res/cards/value/";
	private static final String ACTION_PATH = "res/cards/action/";
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
	private static final ArrayList<HashMap<Integer, Data>> CARD_IMAGE_MAP = new ArrayList<HashMap<Integer, Data>>() {{
			final Queue<Data> descriptions = new LinkedList<Data>() {{
					try {
			            // FileReader reads text files in the default encoding.
			            FileReader fileReader = 
			                new FileReader(DESCRIPTION_PATH);
	
			            // Always wrap FileReader in BufferedReader.
			            BufferedReader bufferedReader = 
			                new BufferedReader(fileReader);
	
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
					put(3, descriptions.remove().setImage(new Image(VALUE_PATH+"0-0.jpeg")));
					put(4, descriptions.remove().setImage(new Image(VALUE_PATH+"0-1.jpeg")));
					put(5, descriptions.remove().setImage(new Image(VALUE_PATH+"0-2.jpeg")));
					put(7, descriptions.remove().setImage(new Image(VALUE_PATH+"0-3.jpeg")));
				}};
			HashMap<Integer, Data> red = new HashMap<Integer, Data>(){{
					put(3, descriptions.remove().setImage(new Image(VALUE_PATH+"1-0.jpeg")));
					put(4, descriptions.remove().setImage(new Image(VALUE_PATH+"1-1.jpeg")));
					put(5, descriptions.remove().setImage( new Image(VALUE_PATH+"1-2.jpeg")));
				}};
			HashMap<Integer, Data> yellow = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(VALUE_PATH+"2-0.jpeg")));
					put(3, descriptions.remove().setImage(new Image(VALUE_PATH+"2-1.jpeg")));
					put(4, descriptions.remove().setImage(new Image(VALUE_PATH+"2-2.jpeg")));
				}};
			HashMap<Integer, Data> blue = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(VALUE_PATH+"3-0.jpeg")));
					put(3, descriptions.remove().setImage(new Image(VALUE_PATH+"3-1.jpeg")));
					put(4, descriptions.remove().setImage(new Image(VALUE_PATH+"3-2.jpeg")));
					put(5, descriptions.remove().setImage(new Image(VALUE_PATH+"3-3.jpeg")));
				}};
			HashMap<Integer, Data> green = new HashMap<Integer, Data>(){{
					put(1, descriptions.remove().setImage(new Image(VALUE_PATH+"4-0.jpeg")));
				}};
			HashMap<Integer, Data> white = new HashMap<Integer, Data>(){{
					put(2, descriptions.remove().setImage(new Image(VALUE_PATH+"5-0.jpeg")));
					put(3, descriptions.remove().setImage(new Image(VALUE_PATH+"5-1.jpeg")));
					put(6, descriptions.remove().setImage(new Image(VALUE_PATH+"5-2.jpeg")));
				}};
			
			HashMap<Integer, Data> action = new HashMap<Integer, Data>(){{
					for (int i=0; i<17; i++) {
						put(i, descriptions.remove().setImage(new Image(ACTION_PATH + i + ".jpeg")));
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
		
	// Rrturns image object for the back of cards
	public static Image getBackImage() { 
		return BACK_IMAGE;
	}
	public static Image getCardImage(int type, int value) {
		return CARD_IMAGE_MAP.get(type).get(value).image;
	}
	public static String getCardName(int type, int value) {
		String name = CARD_IMAGE_MAP.get(type).get(value).name;
		return (name==null ? "" : name);
	}
	public static String getCardDescription(int type, int value) {
		String description = CARD_IMAGE_MAP.get(type).get(value).description;
		return (description==null ? "" : description);
	}
}
