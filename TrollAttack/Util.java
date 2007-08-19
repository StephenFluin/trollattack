package TrollAttack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Item;

/*
 * Created on May 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Util {
    static public String wrapChar = "\n\r";

    /**
     * Tests to see if s is in d.
     * 
     * @param s The string we are looking for.
     * @param d The string we are looking in.
     * @return Was s found in string d?
     */
    static public boolean contains(String s, String d) {
        try {
            if (s.length() < d.length()) {
                return false;
            }
        } catch (NullPointerException e) {
            TrollAttack.error("Attempting to see if  '" + s + "' contains '"
                    + d + "'.");
            throw (new NullPointerException());
        }
        if (s.toLowerCase().startsWith(d.toLowerCase())) {
            return true;
        } else {
            return contains(s.substring(1, s.length()), d);
        }
    }

    //How to write a file (easy):
    // FileOutputStream underlyingStream = new FileOutputStream("Players/" +
    // player.getShort() + ".txt");
    // DataOutputStream fileOut = new DataOutputStream(underlyingStream);
    // fileOut.writeBytes( player.getDocument() );
    static public void savePlayer(Player player) {
        try {
            XMLPrint(player.toDocument(), "Players/" + player.getShort()
                    + ".player.xml");
        } catch (Exception e) {
            TrollAttack
                    .error("There was a problem writing the player's file, or create the player's file.");
            e.printStackTrace();
        }
    }

    static public int experienceLevel(int level) {
        //return (int)Math.pow(Math.log(level)/Math.log(2), level - 2) * 2000;
        return (int) (Math.pow(1.6, level) * 4000);
    }

    static public String uppercaseFirst(String string) {
    	int index = 0;
    	if(string.indexOf("&") == 0) {
    		index = 2;
    		//TrollAttack.debug("Found & - " + string);
    	} else {
    		//TrollAttack.debug("Index of & was " + string.indexOf("&"));
    	}
        try {
        	return string.substring(0,index) + 
	        	string.substring(index, index+1).toUpperCase() + 
	        	string.substring(index + 1);
        } catch(Exception e) {
        	TrollAttack.error("Couldn't uppercase first of '" + string + "'.");
        	e.printStackTrace();
        	return string;
        }
    }

    static public Document xmlize(String dataFile) {
        try {
            File xmlFile = new File(dataFile);
            if (xmlFile.exists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();

                factory.setValidating(false);
                factory.setNamespaceAware(false);
                factory.setIgnoringComments(true);

                factory.setIgnoringElementContentWhitespace(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                try {
                    return builder.parse(xmlFile);
                } catch (Exception e) {
                    TrollAttack.error("Badly formed file (" + dataFile
                            + ").");
                }
            }

        } catch (ParserConfigurationException e) {
            TrollAttack.error("The underlying parser does not support "
                    + "the requested features.");
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            System.out.println("Error occurred obtaining Document Builder "
                    + "Factory.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Parse the document

        return null;
    }

    static public void XMLSprint(Document doc) {
        try {
            Source source = new DOMSource(doc);
            Result result = new StreamResult(System.out);

            Transformer trox = TransformerFactory.newInstance()
                    .newTransformer();
            trox.transform(source, result);

        } catch (Exception e) {
            TrollAttack
                    .error("There was a problem in XMLPrint when writing the file.");
            e.printStackTrace();
        }
    }

    static public void XMLPrint(Document doc, String filename)
            throws TransformerFactoryConfigurationError, TransformerException {
        Source source = new DOMSource(doc);
        File file = new File(filename);
        Result result = new StreamResult(file);
        //result = new StreamResult(System.out);

        Transformer trox = TransformerFactory.newInstance().newTransformer();
        trox.transform(source, result);
    }


    static public Node nCreate(Document doc, String name, String value) {
        Node n = doc.createElement(name);
        n.appendChild(doc.createTextNode(value));

        return n;
    }

    static public void saveAllAreas() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Hashtable<String, Document> areasList = new Hashtable<String, Document>();
        Document doc = builder.newDocument();
        // Add all of the rooms to their appropriate file hashes.
        for (Room currentRoom : TrollAttack.gameRooms) {
            //TrollAttack.message("Adding " + currentRoom.vnum + "room to new
            // file...");
            Area.addToList(doc, areasList, currentRoom);

        }

        for (Mobile currentMobile : TrollAttack.gameMobiles) {
            Area.addToList(doc, areasList, currentMobile);
        }

        for(Item currentItem : TrollAttack.gameItems) {
            Area.addToList(doc, areasList, currentItem);
        }

        /* Deleting old files... */
        File dir = new File("Areas");
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".xml");
            }
        };
        File[] children = dir.listFiles(filter);
        if (children == null) {
            TrollAttack.error("Couldn't find area files.");
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                children[i].delete();
            }
        }
        for (Enumeration e = areasList.keys(); e.hasMoreElements();) {
            try {
                String filename = (String) e.nextElement();
                Util.XMLPrint((Document) areasList.get(filename), "Areas/"
                        + filename);
                TrollAttack.message("Writing file Areas/" + filename + ".");
            } catch (Exception ex) {
                TrollAttack.error("There was a problem writing the area file.");
                ex.printStackTrace();
            }
        }
    }

    static public int intize(String s) {
        return intize(null, s);
    }

    static public int intize(Being p, String s) {
    	int result;
    	try {
    		Roll die = new Roll(s);
            result = die.roll();
        } catch (Exception e) {
            if (p != null) {
                p.tell("'" + s + "' isn't a valid number.");
            } else {
            	throw new NumberFormatException("Invalid number not handled from intize.");
            }
            result = 0;
        }
        return result;
    }

    static public String mStat(Being mobile) {
        String result = (mobile.isPlayer() ? ""
                : ("#" + ((Mobile) mobile).vnum + "\t"))
                + mobile.getShort() + "'s Information" + wrapChar;
        result += "Name:\t\t" + mobile.getName() + wrapChar;
        result += "Class:\t\t" + mobile.getClassName() + wrapChar;
        if (!mobile.isPlayer()) {
            result += "Original:\t\t("
                    + (mobile == TrollAttack.getMobile(new Integer(
                            ((Mobile) mobile).vnum)) ? "X" : " ") + ")"
                    + wrapChar;
            result += "Short Desc.:\t" + mobile.getShort() + wrapChar;
            result += "Long Desc.:\t" + mobile.getLong() + wrapChar;
            result += "Room:\t\t" + mobile.currentRoom + wrapChar;

        } else {
            result += "Time Played:\t" + Math.round(mobile.getTimePlayed())
                    + " hours" + wrapChar;
        }
        result += "Deaths:\t" + mobile.deathCount + Util.wrapChar;
        result += "Kills:\t" + mobile.killCount + Util.wrapChar;
        result += "Hit Points:\t" + mobile.hitPoints + "/"
                + mobile.maxHitPoints + wrapChar;
        result += "Mana Points:\t" + mobile.manaPoints + "/"
                + mobile.maxManaPoints + wrapChar;
        result += "Move Points:\t" + mobile.movePoints + "/"
                + mobile.maxMovePoints + wrapChar;
        result += "Level:\t" + mobile.level + "\t" + "Gold:\t" + mobile.gold
                + wrapChar;
        result += "Carrying:\t" + mobile.getCarryingWeight() + " (Max: " + mobile.getCarryingMax() + ")" + Util.wrapChar;
        result += "Strength:\t" + mobile.strength + "\t" + "Constitution:\t" + mobile.constitution
        	+ wrapChar;
        result += "Charisma:\t" + mobile.charisma + "\t" + "Dexterity:\t" + mobile.dexterity
        	+ wrapChar;
        result += "Intelligence:\t" + mobile.intelligence + "\t" + "Wisdom:\t" + mobile.wisdom
        	+ wrapChar;
        result += "Damage Dice:\t" + mobile.hitDamage.toString() + "\t"
                + "Hit Dice:\t" + mobile.hitSkill.toString() + wrapChar;
        result += "Hit Level (Minimum roll to hit):\t" + mobile.hitLevel
                + wrapChar;
        result += "Experience:\t" + mobile.experience + "\t" + "Favor:\t"
                + mobile.favor + wrapChar;
        result += "Can teach? (" + (mobile.canTeach ? "X" : " " ) + ")" + wrapChar;
        
        if (!mobile.isPlayer()) {
            result += "Wanderer? (" + (((Mobile) mobile).isWanderer() ? "X" : " " ) + ")" + wrapChar;
            result += "Clicks:\t" + ((Mobile) mobile).getClicks() + wrapChar;
        } else {
            result += "You are " + mobile.getHungerString() + wrapChar;
            result += "You are " + mobile.getThirstString() + wrapChar;
        }
        return result;
    }
    
    /**
     * Method to take a message and populate %n with the correct name for the correct 
     * recipients.  When the being being populated is the being for whom the message 
     * is generated, the string "you" is used.
     * 
     * @param message
     * @param beings Beings that will be puplated into the message.
     * @param forWhom Being for whom this message is being generated.
     * @return
     */
    static public String replaceBeings(String message, Being[] beings, Being forWhom) {
        for (int j = 0; j < ( beings.length ); j++) {
            try {
            	message = message.replaceAll("%" + (j+1), beings[j].getShort(forWhom));
            	//TrollAttack.debug("Replacing %" + (j+1) + " with " + beings[j].getShort(forWhom));
            } catch (NullPointerException e) {
            	//TrollAttack.debug("Tried to say the message '" + message + "' to " + players[j] + "'(" + j + ").");
            }
        }
        return message;
    }
    static public String replaceBeings(String message, Being[] beings) {
    	return replaceBeings(message, beings, null);
    }
    
    public static String getMOTD() {
        String MOTD =
             
            "&WWelcome to:&Y____   ______  _______ ___      ___   " +       "&B        /           /       " + wrapChar +
            "&Y       /      /  / __  / / __   //  /     /  /   " +         "&B        /' .,,,,  ./        " + wrapChar +
            "&Y      /__  __/  / /_/ / / /_/  //  /     /  /    " +         "&B       /';'     ,/          " + wrapChar +
            "&Y       / /    / __  / /      //  /____ /  /____  " +         "&B      / /   ,,//,`'`        " + wrapChar +
            "&Y      /_/    /_/  \\_\\/______//_______//_______/  " +       "&B     ( ,, '_,  ,,,' ``      " + wrapChar +
            "&Y     _      _______ _______ _     __      _  __  " +         "&B     |    /@  ,,, ;\" `     " + wrapChar +
            "&Y    / \\    /      //      // \\   / _\\    / \\/ /  " +     "&B    /    .   ,''/' `,``     " + wrapChar +
            "&Y   / ^ \\  /__  __//__  __// ^ \\  | |    /    /   " +       "&B   /   .     ./, `,, ` ;    " + wrapChar +
            "&Y  / / \\ \\   / /     / /  / / \\ \\ | |_  /     \\   " +    "&B,./  .   ,-,',` ,,/''\\,'   " + wrapChar +
            "&Y /_/   \\ \\ /_/     /_/  /_/   \\_\\\\__/ /__/\\___\\  " +  "&B|   /; ./,,'`,,'' |   |     " + wrapChar +
            "&Y                                                 " +         "&B|     /   ','    /    |     " + wrapChar +
            "&G     Written by &RStephen Fluin&Y                    " +     "&B\\___/'   '     |     |      " + wrapChar +
            "&Y                                                 " +         "&B  `,,'  |      /     `\\     " + wrapChar +
            "&Y                                                 " +         "&B       /      |        ~\\   " + wrapChar + wrapChar + 
            "&CWelcome players, new and old!  The game has recently been underoing some " + wrapChar +
            "improvments that will help the stability and playability of the game. We have"  + wrapChar +
            "also recently picked up several new players and builders which should make the" + wrapChar +
            "game more interesting." + wrapChar + wrapChar;
        //MOTD = Prompt.color(MOTD);
        //System.out.println(MOTD);
        return MOTD;
    }
    
    // Splits a command into a bunch of strings by " "'s, but allows for single quotes to
    // do stuff like "cast 'create spring'" -> cast, create spring.
    public static String[] split(String command) {
        String[] spaceParts = command.split(" ");
        
        int partsCount = 0;
        String newPart = "";
        boolean waitingCloseQuote = false;
        
        for(int i = 0;i < spaceParts.length;i++) {
            if(waitingCloseQuote) {
                newPart += " ";
                if(spaceParts[i].endsWith("'")) {
                    newPart += spaceParts[i].substring(0,spaceParts[i].length()-1);
                    spaceParts[partsCount - 1] = newPart;
                    waitingCloseQuote = false;
                    newPart = "";
                } else {
                    newPart += spaceParts[i];
                }
            } else if( spaceParts[i].startsWith("'") && !spaceParts[i].endsWith("'")) {
               newPart = spaceParts[i].substring(1);
               partsCount++;
               waitingCloseQuote = true;
            } else {
                if(spaceParts[i].startsWith("'") && spaceParts[i].endsWith("'")) {
                    spaceParts[i] = spaceParts[i].substring(1,spaceParts[i].length()-1);
                }
                spaceParts[partsCount] = spaceParts[i];
                partsCount++;
                
            }
        }
        if(newPart.length() > 0) {
            spaceParts[partsCount] = newPart;
        }
        String[] result = new String[partsCount];
        for(int i = 0;i < partsCount;i++) {
            result[i] = spaceParts[i];
        }
        
        return result;
    }
   
    
    public static String escapeColors(String s) {
        //TrollAttack.debug("Escaped string to: " + s.replaceAll("&", "&&"));
    	return s.replaceAll("&", "&&");
    }

	public static String getRestOfCommand(String[] parts) {
		if(parts.length < 2) {
			return "";
		}
		String command = parts[1];
		for(int i = 2;i<parts.length;i++) {
			command += " " + parts[i];
		}
		return command;
	}

	/**
	 * Take in a string and interpret tabs to create a table.  It is 
	 * currently assumed that the first row is the header row.  Columns
	 * should be separated by tabs, and rows should be separated by the
	 * wrapChar.
	 * 
	 * @param result Tab formatted string
	 * @return Table formatted string.
	 */
	public static String table(String tableString) {
		String[][] table;
		String[] lines = tableString.split(Util.wrapChar);
		table = new String[lines.length][];
		for(int i = 0;i<lines.length;i++) {
			table[i] = lines[i].split("\t");
		}
		//printTable(table);
		char headerChar = '-';
		int[] columnSizes = new int[table[0].length];
		// Calculate max size for each column.
		for(int i = 0;i<table.length;i++) {
			for(int j = 0;j<table[i].length;j++) {
				if(table[i][j] == null) {
					TrollAttack.debug("Table cell was null, What the?!?");
				}
				int colorLessLength = Util.colorLessLength(table[i][j]);
				if(columnSizes[j] < colorLessLength) {
					columnSizes[j] = colorLessLength;
				}
			}
		}
		String result = "";
		for(int i = 0;i<table.length;i++) {
			
			for(int j = 0;j<table[i].length;j++) {

				result += table[i][j];
				for(int n = columnSizes[j] - colorLessLength(table[i][j]);n>0;n--) {
					result += " ";
				}

				result += " ";
				
			}
			if(i == 0) {
				result += wrapChar + Communication.GREEN;
				for(int j = 0;j<table[i].length;j++) {
					//result += columnSizes[j];
					for(int n = 0;n<columnSizes[j];n++) {
						result += headerChar;
					}
					result += " ";
				}
			}
			result += wrapChar;
			
		}
		return result;
	}
	public static void printTable(String[][] table) {
		System.out.print("[");
		System.out.print(table.length + "->");
		for(int i = 0;i< table.length;i++) {
			System.out.print(table[i].length + ":");
			for(int j = 0;j< table[i].length;j++) {
				System.out.print(table[i][j] + ", ");
			}
			System.out.println();
		}
		System.out.print("]");
	}
    public static int colorLessLength(String string) {
        //TrollAttack.debug("Colorless Length: " +  decolor(string).length() + "\nNormal Length: " + string.length());
        return decolor(string).length();
    }
    public static String decolor(String string) {
    	return string.replaceAll("&[^&]", "");
    }

    /**
     * Given a string and a collection, will find the first member of the 
     * collection of the correct class that contains the string.  This method
     * supports item-access by using "3.<string>" to get the 3rd item 
     * matching <string>.
     * @param <T> The type of object the collection contains.
     * @param contents The collection to search in.
     * @param name The string that we are looking for.
     * @param objectClass The class of the object we are looking 
     * 	for, null if not required.
     * @param notThis The item found cannot be equal to this.
     * @return The first object matching the given class and name.
     */
	public static <T> T findMember(Collection<T> contents, String name, Class objectClass, T notThis) {
		int skipItems = 0;
    	if(name.matches("^\\d\\..*$")) {
    		// We want to skip some items until we get to the desired item.
    		skipItems = new Integer(name.substring(0,1)) - 1;
    		name = name.substring(2);
    	}
    	
        T newItem = null;        
        for(T currentItem : contents) {
        	if (Util.contains(getName(currentItem), name) && currentItem != notThis && (--skipItems < 0)) {
                if(objectClass!= null) {
                	if(objectClass.isInstance(currentItem)) {
                		newItem = currentItem;
                	}
                } else {
                	newItem = currentItem;
                	break;
                }
                
            }
        }
        return newItem;
		
	}
	public static <T> T findMember(Collection<T> contents, String name, Class objectClass) {
		return findMember(contents, name, objectClass, null);
	}
	public static <T> T findMember(Collection<T> contents, String name) {
		return findMember(contents, name, null);
	}
	
	/**
	 * Generic method to look at any object and figure out what it's name is.
	 * @param o
	 * @return
	 */
	public static String getName(Object o) {
		if(o instanceof Item) {
			return ((Item)o).getName();
		} else if (o instanceof Being) {
			return ((Being)o).getName();
		} else {
		
			TrollAttack.error("Couldn't figure out the name of an object.");
			return null;
		}
	}
}
