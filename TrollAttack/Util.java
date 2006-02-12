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
        Roll die = new Roll(s);
        int result;
        try {
            result = die.roll();
        } catch (Exception e) {
            if (p != null) {
                p.tell("'" + s + "' isn't a valid number.");
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
        result += "Hit Points:\t" + mobile.hitPoints + "/"
                + mobile.maxHitPoints + wrapChar;
        result += "Mana Points:\t" + mobile.manaPoints + "/"
                + mobile.maxManaPoints + wrapChar;
        result += "Move Points:\t" + mobile.movePoints + "/"
                + mobile.maxMovePoints + wrapChar;
        result += "Level:\t" + mobile.level + "\t" + "Gold:\t" + mobile.gold
                + wrapChar;
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
            "&Y                                                 " +         "&B       /      |        ~\\   " + wrapChar;
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

    public static <T> T findIn (String name, Collection<T> list) {
        for(T element : list) {
            if(element.toString().toLowerCase().startsWith(name.toLowerCase())) {
                return element;
            }
        }
        return null;
    }
    public static <T> T findIn (String name, T[] list) {
        for(T element : list) {
            if(element.toString().toLowerCase().startsWith(name.toLowerCase())) {
                return element;
            }
        }
        return null;
        
    }

    public static String decolor(String s) {
        //TrollAttack.debug("Escaped string to: " + s.replaceAll("&", "&&"));
    	return s.replaceAll("&", "&&");
    }
}
