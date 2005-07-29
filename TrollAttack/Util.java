package TrollAttack;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
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
	        TrollAttack.error("Attempting to see if  '" + s + "' contains '" + d + "'.");
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
		    e.printStackTrace();
		}
	}
	static public int experienceLevel(int level) {
	    //return (int)Math.pow(Math.log(level)/Math.log(2), level - 2) * 2000;
	    return (int)(Math.pow(1.6, level)*4000);
	}
	

	static public String uppercaseFirst(String string) {
	    return string.substring(0,1).toUpperCase() + string.substring(1);
	}
	static public Document xmlize(String dataFile) {
	    try {
		    File xmlFile = new File( dataFile );
		    if(xmlFile.exists()) {
		        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		        factory.setValidating( false );
		        factory.setNamespaceAware(false);
		        factory.setIgnoringComments( true ) ;
		        
		        factory.setIgnoringElementContentWhitespace( true );
		        DocumentBuilder builder = factory.newDocumentBuilder();
		        try {
		            return builder.parse(xmlFile);
		        } catch(Exception e) {
		            TrollAttack.error("Badly formed area file (" + dataFile + ").");
		        }
		    }
	        
	        
		} catch (ParserConfigurationException e) {
	        TrollAttack.error("The underlying parser does not support " +
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
	static public void XMLSprint(Document doc) {
	    try {  
            Source source = new DOMSource(doc);
            Result result = new StreamResult(System.out);
            
            Transformer trox = TransformerFactory.newInstance().newTransformer();
            trox.transform(source,result);
            
            
            
         } catch(Exception e) {
             TrollAttack.error("There was a problem in XMLPrint when writing the file.");
             e.printStackTrace();
         }
	}
	static public void XMLPrint(Document doc, String filename) throws TransformerFactoryConfigurationError, TransformerException {
	            Source source = new DOMSource(doc);
	            File file = new File(filename);
	            Result result = new StreamResult(file);
	            //result = new StreamResult(System.out);
	            
	            Transformer trox = TransformerFactory.newInstance().newTransformer();
	            trox.transform(source,result);
	}
	static public void fillDefaults(Hashtable hash, String[] fillNames, String[] fillValues) {
	    Object tmp;
	    for(int i = 0;i < fillNames.length;i++) {
	        tmp = hash.get(fillNames[i]);
	        if(tmp == null) {
	            hash.put(fillNames[i], fillValues[i]);
	        }
	    }
	}
	static public Node nCreate(Document doc, String name, String value) {
	    Node n = doc.createElement(name);
	    n.appendChild(doc.createTextNode(value));
	    
	    return n;
	}
	static public void saveAllAreas() {
	    DocumentBuilderFactory factory = 
            DocumentBuilderFactory.newInstance();

	    DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Hashtable areasList = new Hashtable();
        Document doc = builder.newDocument();
	    Room currentRoom = null;
	    // Add all of the rooms to their appropriate file hashes.
	    for(int i = 0; i < TrollAttack.gameRooms.length(); i++ ) {
	        currentRoom = (Room)TrollAttack.gameRooms.getNext();
	        //TrollAttack.message("Adding " + currentRoom.vnum + "room to new file...");
	        Area.addToList(doc, areasList, currentRoom);
	        
	    }
	    TrollAttack.gameRooms.reset();
	    
	    Mobile currentMobile = null;
	    for(int i = 0; i < TrollAttack.gameMobiles.length();i++) {
	        currentMobile = (Mobile)TrollAttack.gameMobiles.getNext();
	        Area.addToList(doc, areasList, currentMobile);
	    }
	    TrollAttack.gameMobiles.reset();
	    
	    Item currentItem = null;
	    for(int i = 0; i < TrollAttack.gameItems.length();i++) {
	        currentItem = (Item)TrollAttack.gameItems.getNext();
	        Area.addToList(doc, areasList, currentItem);
	    }
	    TrollAttack.gameItems.reset();
	    
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
            for (int i=0; i<children.length; i++) {
                // Get filename of file or directory
                children[i].delete();
            }
        }
	    for(Enumeration e = areasList.keys() ; e.hasMoreElements() ;) {
	        try{
		        String filename = (String)e.nextElement();
		        Util.XMLPrint((Document)areasList.get(filename), "Areas/" + filename);
		        TrollAttack.message("Writing file Areas/"+ filename + ".");
			} catch(Exception ex) {
			    TrollAttack.error("There was a problem writing the area file.");
			    ex.printStackTrace();
			}
	    }
	}

	static public int intize(Player p, String s) {
	    Roll die = new Roll(s);
	    int result;
	    try {
	        result = die.roll();
	    } catch(Exception e) {
	        if(p != null) {
	            p.tell("'" + s + "' isn't a valid number.");
	        }
	        result = 0;
	    }
	    return result;
	}
}
