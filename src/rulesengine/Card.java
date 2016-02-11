package rulesengine;


public class Card {
	
	private static String COLOUR;
	private static int SQUIRE_VALUE = 3;
	private static int MAIDEN_VALUE;
	private static boolean MAIDEN = true;
	private static boolean NON_MAIDEN = true;
	
    private String primaryKey;
    private int value;
    private String colour;
    private String action;
    private boolean ivanhoe;
    private boolean actionCard;
    private boolean maiden;
    
    //empty constructor
    public Card() {
		primaryKey = "";
		value = 0;
		colour = "";
		action = "";
		ivanhoe = false;
		actionCard = false;
		maiden = false;
		
    }
 
    //constructor for regular coloured cards
    public Card(String primaryKey, int value, String colour) {
		this.primaryKey = primaryKey;
		this.value = value;
		this.colour = colour;
    }
    
    //constructor for supporter cards
    public Card(String primaryKey, boolean maiden, int value) {
    	this.primaryKey = primaryKey;
    	this.maiden = maiden;
    	this.value = value;
    }
    
    //constructor for action/special action cards
    public Card(String primaryKey, boolean actionCard,  boolean ivanhoe, String action) {
    	this.primaryKey = primaryKey;
    	this.actionCard = actionCard;
    	this.ivanhoe = ivanhoe;
    	this.action = action;
    }

    
    //the following are getters and setters
	public String getCardIdPrimaryKey() {
		return primaryKey;
	}
	
	public void setCardIdPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	public int getCardValue() {
        return value;
    }
	
	public void setCardValue(int value) {
		this.value = value;
	}
	
	public String getCardColour() {
		return colour;
	}
	
	public void setCardColour(String colour) {
		this.colour = colour;
	}
	
	public String getCardAction() {
		return action;
	}
	
	public void setCardAction(String action) {
		this.action = action;
	}
	
	public boolean isActionCard() {
		return actionCard;
	}
	
	public void setActionCard(boolean bool) {
		this.actionCard = bool;
	}
	
	public boolean isIvanhoe() {
		return ivanhoe;
	}
	
	public void setIvanhoe(boolean bool) {
		this.ivanhoe = bool;
	}
	
	public boolean isMaiden() {
		return maiden;
	}
	
	public void setMaiden(boolean bool) {
		this.maiden = maiden;
	}
}
