package TrollAttack;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/*
 * Created on May 29, 2005
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
public class Util {
	static public boolean contains(String s, String d) {
	    try {
		    if( s.length() < d.length() ) {
		        return false;
		    }
	    } catch( NullPointerException e ) {
	        TrollAttack.print("Attempting to see if  '" + s + "' contains '" + d + "'.");
	        throw(new NullPointerException());
	    }
	    if( s.toLowerCase().startsWith( d.toLowerCase() ) ) {
	        return true;
	    } else {
	        return contains( s.substring( 1 , s.length()), d );
	    }
	}
    //How to write a file (easy):
    // FileOutputStream underlyingStream = new FileOutputStream("Players/" + player.getShort() + ".txt");
    // DataOutputStream fileOut = new DataOutputStream(underlyingStream);
	// fileOut.writeBytes( player.getDocument() );
	static public void savePlayer(Player player) {
		try{
		    XMLPrint(player.toDocument(), "Players/" + player.getShort() + ".txt");
		} catch(Exception e) {
		    TrollAttack.error("There was a problem writing the player's file, or create the player's file.");
		}
	}
	static public int experienceLevel(int level) {
	    return (int)Math.pow(Math.log(level)/Math.log(2), level - 2) * 2000;
	}
	public static Player readPlayerData(String playerName) {
	    Document doc = xmlize("Players/" + playerName + ".txt");
	    XMLHandler handler = new XMLHandler(doc, false);
	    Hashtable hash = (Hashtable)handler.sections.getNext();
	    Player p = new Player(
	            new Integer((String)hash.get("hitPoints")).intValue(),
	            new Integer((String)hash.get("maxHitPoints")).intValue(),
	            new Integer((String)hash.get("manaPoints")).intValue(),
	            new Integer((String)hash.get("maxManaPoints")).intValue(),
	            new Integer((String)hash.get("movePoints")).intValue(),
	            new Integer((String)hash.get("maxMovePoints")).intValue(),
	            new Integer((String)hash.get("hitSkill")).intValue(),
	            new Integer((String)hash.get("hitDamage")).intValue(),
	            new Integer((String)hash.get("level")).intValue(),
	            new Integer((String)hash.get("room")).intValue(),
	            new Integer((String)hash.get("experience")).intValue(),
	            playerName
	            );
	    return p;
	    
	}
	public static LinkedList readItemData() {
	    int vnum = 0, weight = 0, hd, t = 0;
		String shortDesc = "", longDesc = "", itemName = "", type = "";
        Document doc = xmlize("items.xml");
        XMLHandler handler = new XMLHandler(doc);
        LinkedList items = new LinkedList();
        
        for(int i = 0;i < handler.sections.length(); i++) {
            Hashtable item = (Hashtable)handler.sections.getNext();
            Hashtable hash = (Hashtable)((LinkedList)item.get("item")).getNext();	
            vnum = new Integer((String)hash.get("vnum")).intValue();
            shortDesc = (String)hash.get("short");
            longDesc = (String)hash.get("long");
            weight = new Integer((String)hash.get("weight")).intValue();
            itemName = (String)hash.get("name");
            hd = new Integer((String)hash.get("hitDamage")).intValue();
            type = (String)hash.get("type");
 		    if(type.compareToIgnoreCase("sword") == 0) {
 		        t = Item.SWORD;
 		    } else if(type.compareToIgnoreCase("helm") == 0) {
 		        t = Item.HELM;
 		    } else if(type.compareToIgnoreCase("boots") == 0) {
 		        t = Item.BOOTS;
 		    } else if(type.compareToIgnoreCase("greaves") == 0) {
 		        t = Item.GREAVES;
 		    } else if(type.compareToIgnoreCase("ring") == 0) {
 		        t = Item.RING;
 		    }
			
		//TrollAttack.error("vnum:" + vnum + ", south: " + south );
		 items.add( new Item(vnum, itemName, weight, shortDesc, longDesc, t, hd) );
		 //System.out.println("Created: " + itemList[vnum].toString());
		}
		return items;
	}
	

	static public String uppercaseFirst(String string) {
	    return string.substring(0,1).toUpperCase() + string.substring(1);
	}
	static public Document xmlize(String dataFile) {
	    try {
		    File xmlFile = new File( dataFile );
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	        factory.setValidating( false );
	        factory.setNamespaceAware(false);
	        factory.setIgnoringComments( true ) ;
	        
	        factory.setIgnoringElementContentWhitespace( true );
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(xmlFile);
	        
	        
		} catch(FileNotFoundException e) {
		    
		} catch (ParserConfigurationException e) {
	        System.out.println("The underlying parser does not support " +
	                           "the requested features.");
	        e.printStackTrace();
		} catch (FactoryConfigurationError e) {
		    System.out.println("Error occurred obtaining Document Builder " +
		                       "Factory.");
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
        // Parse the document
       
       return null;
	}
	static public void XMLPrint(Document doc, String filename) {
	    try {  
	            Source source = new DOMSource(doc);
	            File file = new File(filename);
	            Result result = new StreamResult(file);
	            
	            Transformer trox = TransformerFactory.newInstance().newTransformer();
	            trox.transform(source,result);
	            
	            
	            
	         } catch(Exception e) {
	             
	         }
	}
}
