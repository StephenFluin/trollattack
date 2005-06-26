package TrollAttack;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.*;

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
public class Player extends Being implements Serializable {
	//String prompt = "&G<&R%h&G/&R%H&G-&z%x&G> %T";
	// HARDCODED MAX ITEMS - FIX THIS! @TODO!
	private Item[] playerItems = new Item[50];
	private Item weapon = null;
	private Item helm = null;
	private Item boots = null;
	private Item greaves =  null;
	Communication communication = null;
	private CommandHandler ch = null;

	public void look() {
	    String[] looks = TrollAttack.gameRooms[getCurrentRoom()].look(this);
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
		hitDamage = 3;
		level = 1;
		currentRoom = 1;
		shortDescription = "a player";
		communication = com;
		setCurrentRoom( 1  );
		ch = new CommandHandler(this);
		
		
	}
	public Player(int hp, int mhp, int mp, int mmp, int vp, int mvp, 
	        int hs, int hd, int lev, int room, int exp, String shortd) {
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
	    shortDescription = shortd;
	    ch = new CommandHandler(this);
	    
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
	public String getName() {
	    return shortDescription;
	}
	public String toString() {
	    String r = "";
	    r += "HP: " + hitPoints + "\n" +
	    "MHP: " + maxHitPoints + "\n" +
	    "MP: " + manaPoints + "\n" +
	    "MP: " + maxManaPoints + "\n";
	    return r;
	}
	public Document toDocument() throws ParserConfigurationException {
	    DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

	    // Turn on validation, and turn off namespaces
	    factory.setValidating( false );
	    factory.setNamespaceAware(false);
	    factory.setIgnoringComments( true ) ;
	    
	    // Why doesn't this work?
	    factory.setIgnoringElementContentWhitespace( true );
	    //factory.
	    // Obtain a document builder object
	    DocumentBuilder builder = factory.newDocumentBuilder();
	   // System.out.println();
	    //          System.out.println("DataFile : " + xmlFile);
	   //System.out.println("Parser Implementation  : " + builder.getClass());
	    //System.out.println();
	
	    // Parse the document
	    Document doc = builder.newDocument();
	    // Print the document from the DOM tree and feed it an initial 
	    // indentation of nothing
	    Node n = doc.createElement("TrollAttack");
	    doc.appendChild(n);
	    Node m = doc.createElement("player");
	    n.appendChild(m);
	    LinkedList attribs = new LinkedList();
	    attribs.add(nCreate(doc, "hitPoints", hitPoints + ""));
	    attribs.add(nCreate(doc, "maxHitPoints", maxHitPoints + ""));
	    attribs.add(nCreate(doc, "manaPoints", manaPoints + ""));
	    attribs.add(nCreate(doc, "maxManaPoints", maxManaPoints + ""));
	    attribs.add(nCreate(doc, "movePoints", movePoints + ""));
	    attribs.add(nCreate(doc, "maxMovePoints", maxMovePoints + ""));
	    attribs.add(nCreate(doc, "hitSkill", hitSkill + ""));
	    attribs.add(nCreate(doc, "hitDamage", hitDamage + ""));
	    attribs.add(nCreate(doc, "level", level + ""));
	    attribs.add(nCreate(doc, "room", getCurrentRoom() + ""));
	    attribs.add(nCreate(doc, "experience", experience + ""));
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
	public Node nCreate(Document doc, String name, String value) {
	    Node n = doc.createElement(name);
	    n.appendChild(doc.createTextNode(value));
	    
	    return n;
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
	    int hd = hitDamage;
	    for(int j = 0;j < playerItems.length; j++ ) {
	        if(playerItems[j] != null) {
	            hd += playerItems[j].hitDamage;
	            //TrollAttack.error("Adding to player damage by " + playerItems[j].hitDamage);
	        }
	    }
	    return hd;
	}
	public boolean isReady() {
	    return !(isFighting() || state > 0);
	}
	
	public void addItem(Item i) {
		for(int j = 0;j < playerItems.length; j++ ) {
			if(playerItems[j] == null) {
				playerItems[j] = i;
				return;
			}
		}
		throw( new Error("TOO MANY PLAYER ITEMS!"));
	}
	private String[] inventory() {
		String[] items = new String[255];
		int n = 0;
		
		for(int i = 0;i < playerItems.length; i++ ) {
			if(playerItems[i] != null) {
				items[n] = playerItems[i].getShort();
				n++;
			}
		}
		String[] inventoryList = new String[n];
		for(int i = 0;i < n;i++) {
			inventoryList[i] = items[i];
		}
		return inventoryList;
		
	}
	public void pInventory() {
			String[] inv = inventory();
			tell("Your Inventory:");
			for(int i = 0; i < inv.length; i++ ) {
					tell( inv[i] );
			}
	}
	public void pEquipment() {
		tell("Your Equipment:");
		if(weapon != null) {
			tell("Weapon: " + weapon.getShort());
		}
		if(helm != null) {
		    tell("Helm: " + helm.getShort());
		}
		if(boots != null) {
		    tell("Boots: " + boots.getShort());
		}
		if(greaves != null) {
		    tell("Greaves: " + greaves.getShort());
		}
	}
	
	public Item dropItem(String name) {
			Item newItem;
			for(int i = 0; i < playerItems.length; i++) {
				if(playerItems[i] != null) {
					
					if(playerItems[i].name.compareToIgnoreCase(name) == 0) {
						newItem = playerItems[i];
						playerItems[i] = null;
						return newItem;
						
					} else {
						//TrollAttack.error("looking at object i in room " + .getCurrentRoom());
					}
				}
			}
			return null; 
		
	}
	
	
	
	public String wearItem(String name) {
	    boolean success = false;
		for(int i = 0; i < playerItems.length; i++) {
			if(playerItems[i] != null) {
			    if(playerItems[i].getName().compareToIgnoreCase(name) == 0) {
			        Item newWear = playerItems[i];
			       // TrollAttack.error("newWear would go on your " + newWear.type);
			        switch(newWear.type) {
			            case Item.SWORD:
			                if(weapon == null) {
			                    weapon = newWear;
						        success = true;
			                }
			                break;
			            case Item.HELM:
			                if(helm == null) {
			                    helm = newWear;
			                    success = true;
			                }
			                break;
			            case Item.BOOTS:
			                if(boots == null) {
			                    boots = newWear;
			                    success = true;
			                }
			                break;
			            case Item.GREAVES:
			                if(greaves == null) {
			                    greaves = newWear;
			                    success = true;
			                }
			                break;
			        }
			        if(success) {
			            playerItems[i] = null;
			            return "You wear " + newWear.getShort();
			        } else {
			            return "You are already wearing something where this would go!";
			        }
			        
			    }
			}
		}
		
		return "You don't have that.";

	}
	public String removeItem(String name) {
	    Item inHand = null;
	    if(weapon != null && Util.contains(weapon.getName(), name)) {
	        inHand = weapon;
	        weapon = null;
	    } else if(helm != null && Util.contains(helm.getName(), name)) {
	        inHand = helm;
	        helm = null;
	    } else if(boots != null && Util.contains(boots.getName(), name)) {
	        inHand = boots;
	        boots = null;
	    } else if(greaves != null && Util.contains(greaves.getName(), name)) {
	        inHand = greaves;
	        helm = null;
	    }
	    if(inHand != null) {
	        int i = 0;
            while(playerItems[i] != null) {
                i++;
            }
            playerItems[i] = inHand;
            return "You remove " + inHand.shortDesc;
	    } else {
	        return "You aren't wearing that!";
	    }
	    
	}
}
