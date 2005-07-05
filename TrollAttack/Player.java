package TrollAttack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.CommandHandler;

/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Player extends Being {
	//String prompt = "&G<&R%h&G/&R%H&G-&z%x&G> %T";
	// HARDCODED MAX ITEMS - FIX THIS! @TODO!
	
	
	private String password = "";
	Communication communication = null;
	CommandHandler ch = null;
	private int lastActive = 0;
	private boolean builder = false;
	public boolean authenticated = false;

	public void look() {
	    String[] looks = {};
	    try {
	        looks = TrollAttack.getRoom(getCurrentRoom()).look(this);
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    for(int i = 0; i < looks.length; i++ ) {
			tell( looks[i] + "" );
	    }
	}
	   
	public Player(Communication com) {
		// Set the default player values here.
	    hitPoints = 50;
		maxHitPoints = 50;
		manaPoints = 40;
		maxManaPoints = 40;
		movePoints = 30;
		maxMovePoints = 30;
		hitSkill = 50;
		hitDamage = new Roll("2d2");
		level = 1;
		currentRoom = 1;
		shortDescription = "a player";
		communication = com;
		setCurrentRoom( 1  );
		ch = new CommandHandler(this);
		isPlayer = true;
		
		
	}
	public Player(int hp, int mhp, int mp, int mmp, int vp, int mvp, 
	        int hs, Roll hd, int lev, int room, int exp, boolean isBuilder, String shortd, String pass) {
	    hitPoints = hp;
	    maxHitPoints = mhp;
	    manaPoints = mp;
	    maxManaPoints = mmp;
	    movePoints = vp;
	    maxMovePoints = mvp;
	    hitSkill = hs;
	    hitDamage = hd;
	    level = lev;
	    setCurrentRoom( room );
	    experience = exp;
	    builder = isBuilder;
	    shortDescription = shortd;
	    ch = new CommandHandler(this);
	    password = pass;
	    isPlayer = true;
	    //TrollAttack.message("New player has password '" + password + "'.");
	    
	}
	public Player(Player p) {
	    hitPoints = p.hitPoints;
	    maxHitPoints = p.maxHitPoints;
	    manaPoints = p.manaPoints;
	    maxManaPoints = p.maxManaPoints;
	    movePoints = p.movePoints;
	    maxMovePoints = p.maxMovePoints;
	    hitSkill = p.hitSkill;
	    hitDamage = p.hitDamage;
	    level = p.level;
	    setCurrentRoom( p.getCurrentRoom() );
	    experience = p.experience;
	    shortDescription = p.shortDescription;
	    ch = p.ch;
	    password = p.password;
	    isPlayer = true;
	}
	public void setCommunication(Communication com) {
	    communication = com;
	}
	public Communication getCommunication() {
	    return communication;
	}
	public void tell(String s) {
	    communication.print(s);
	}
	public void name(String newName) {
	    shortDescription = newName;
	}
	public void save() {
	    Util.savePlayer(this);
	}
	public void quit() {
		getActualRoom().removePlayer(this);
		TrollAttack.gamePlayers.delete(this);
	    closeConnection();
	}
	public String getName() {
	    return shortDescription;
	}
	public void setPassword(String pass) {
	    password = pass;
	}
	public boolean checkPassword(String pass) {
	    //TrollAttack.message("Comparing " + pass + " to " + password);
	    if(password.compareTo(pass) == 0) {
	        return true;
	    } else {
	        return false;
	    }
	}
	public void setLastActive(int time) {
	    lastActive = time;
	}
	public int getIdleTime() {
	    return (TrollAttack.getTime() - lastActive);
	}
	public boolean isBuilder() {
	    return builder;
	}
	public String toString() {
	    String r = "";
	    r += "HP: " + hitPoints + "\n\r" +
	    "MHP: " + maxHitPoints + "\n\r" +
	    "MP: " + manaPoints + "\n\r" +
	    "MP: " + maxManaPoints + "\n\r";
	    return r;
	}
	public Document toDocument() throws ParserConfigurationException {
	    DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

	    // Turn on validation, and turn off namespaces
	    factory.setValidating( false );
	    factory.setNamespaceAware(false);
	    factory.setIgnoringComments( true ) ;
	    
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.newDocument();
	    
	    // Print the document from the DOM tree and feed it an initial 
	    // indentation of nothing
	    Node n = doc.createElement("TrollAttack");
	    doc.appendChild(n);
	    Node m = doc.createElement("player");
	    n.appendChild(m);
	    LinkedList attribs = new LinkedList();
	    attribs.add(Util.nCreate(doc, "hitPoints", hitPoints + ""));
	    attribs.add(Util.nCreate(doc, "maxHitPoints", maxHitPoints + ""));
	    attribs.add(Util.nCreate(doc, "manaPoints", manaPoints + ""));
	    attribs.add(Util.nCreate(doc, "maxManaPoints", maxManaPoints + ""));
	    attribs.add(Util.nCreate(doc, "movePoints", movePoints + ""));
	    attribs.add(Util.nCreate(doc, "maxMovePoints", maxMovePoints + ""));
	    attribs.add(Util.nCreate(doc, "hitSkill", hitSkill + ""));
	    attribs.add(Util.nCreate(doc, "hitDamage", hitDamage + ""));
	    attribs.add(Util.nCreate(doc, "level", level + ""));
	    attribs.add(Util.nCreate(doc, "room", getCurrentRoom() + ""));
	    attribs.add(Util.nCreate(doc, "experience", experience + ""));
	    attribs.add(Util.nCreate(doc, "password", password + ""));
	    attribs.add(Util.nCreate(doc, "builder", this.builder + ""));
	    /*Node itemList = doc.createElement("itemList");
	    
	    for(int i = 0;i < playerItems.getLength();i++) {
	       itemList.appendChild(Util.nCreate(doc, "item", ( (Item)(playerItems.getNext()) ).vnum + ""));
	    }
	    attribs.add(itemList);*/
	    for(int i = 0;i < items.getLength();i++) {
		       attribs.add(Util.nCreate(doc, "item", ( (Item)(items.getNext()) ).vnum + ""));
		}
	    
	    items.reset();
	    boolean stillMore = true;
	    while(stillMore) {
	        Node newAttrib = (Node)attribs.getNext();
	        if(newAttrib == null) {
	            stillMore = false;
	        } else {
	            m.appendChild(newAttrib);
	            
	        }
	    }
	    
	    return doc;
	}
	
	public void roomSay(String string) {
	    getActualRoom().say(Util.uppercaseFirst(string), this);
	}
	
	public void closeConnection() {
	    communication.close();
	}
	public void handleCommand(String command) {
	    ch.handleCommand(command);
	}
	public int getHitDamage() {
	    //TrollAttack.error("Reading player hit damage.");
	    int hd = hitDamage.roll();
	    Item currentItem;
	    for(int j = 0;j < items.length(); j++ ) {
	        currentItem = (Item)items.getNext();
            hd += currentItem.getHitDamage();
	    }
	    items.reset();
	    return hd;
	}
	public boolean isReady() {
	    return !(isFighting() || state > 0);
	}
	

	public void kill(Being killer) {
	    tell( killer.getShort() + " kills you!");
	    dropAll();
	    getActualRoom().removePlayer(this);
	    setCurrentRoom(1);
	    hitPoints = manaPoints = movePoints = 1;
	    save();
	    authenticated = false;
//	    communication.authenticate(this);
	    closeConnection();
	}

	
	
	
	
	
	
}
