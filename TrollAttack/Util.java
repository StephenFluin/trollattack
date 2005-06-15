package TrollAttack;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

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
	    if( s.length() < d.length() ) {
	        return false;
	    }
	    if( s.toLowerCase().startsWith( d.toLowerCase() ) ) {
	        return true;
	    } else {
	        return contains( s.substring( 1 , s.length()), d );
	    }
	}
	static public void save(Player player) {
		
		    try {
		        FileOutputStream underlyingStream = new FileOutputStream("tmp.txt");
		        ObjectOutputStream serializer = new ObjectOutputStream(underlyingStream);

			    serializer.writeObject(player);
			    serializer.flush();
		    } catch(FileNotFoundException e) {
		        TrollAttack.error("Couldn't find the file.");
		    } catch(IOException e) {
		        TrollAttack.error("Couldn't open stream.");
		    }

		}
		static public Player load() {
		  try {    
		        FileInputStream underlyingStream = new FileInputStream("tmp.txt");
		        ObjectInputStream deserializer = new ObjectInputStream(underlyingStream);
		         Object newPlayer = deserializer.readObject();
		         if (newPlayer instanceof Player)
		         {
		         	// Cast object to a Vector
		         	Player pla = (Player) newPlayer;
		         	TrollAttack.error("You found a player!");
		         	TrollAttack.error(pla.toString());
		         	// Do something with vector....
		         } else {
		             TrollAttack.error("this isn't a player!");
		         }
		         return (Player)newPlayer;
		    } catch(FileNotFoundException e) {
		        TrollAttack.error("Couldn't find the file.");
		    } catch(IOException e) {
		        TrollAttack.error("Couldn't open stream.");
		    } catch(ClassNotFoundException e) {
		        TrollAttack.error("Class not found.");
		    }
		    return null;
		}
	
	static public int experienceLevel(int level) {
	    return (int)Math.pow(Math.log(level)/Math.log(2), level - 2) * 2000;
	}
	public static Item[] readItemData() {
	   String dataFile = "items.xml";
		File xmlFile = new File( dataFile );
        try {
        
            // Get Document Builder Factory
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
            Document doc = builder.parse(xmlFile);
            // Print the document from the DOM tree and feed it an initial 
            // indentation of nothing
            
			Node node = doc;
			// @TODO: Stop hardcoding of max items limit.
			final Item[] itemList = new Item[255];
			int vnum = 0, weight = 0, hd, t = 0;
			String shortDesc = "", longDesc = "", itemName = "";
			NodeList kids = node.getChildNodes();
		
	//		 first child of this list is TrollAttack
			Node Kid = kids.item(0);
			
			// Roomlist must go before other rooms, @TODO!:
			kids = Kid.getChildNodes();
			Node kid = kids.item(1);	
			
			kids = kid.getChildNodes();
			//Cycle through this for each room
			for(int j = 1; j < kids.getLength(); j += 2) {
				kid = kids.item(j);
				
				//System.out.println("+");
				//printNode( kid , "" );
				//System.out.println("+" + "has length of " + kid.getChildNodes().getLength());
				weight = hd = t = 0;
				shortDesc = longDesc = itemName = "";
				
				if( kid.getNodeType() != Node.TEXT_NODE ) {
					NodeList children = kid.getChildNodes();
					
					for (int i = 1; i < children.getLength(); i += 2) {
						
						Node child = children.item(i);
						//TrollAttack.error(child.toString() + " and " + child.getChildNodes() + " and " + child.getChildNodes().item(0).getNodeValue());
						if( child.getNodeType() != Node.TEXT_NODE ) {
							//printNode(child, "");
							String name = child.getNodeName();
							String nvalue = child.getChildNodes().item(0).getNodeValue() + "";
							//System.out.println("\"" + name + "->" + nvalue + "\"");
						 	int nodeValue;
						 	if(name.compareTo("short") == 0 || name.compareTo("long") == 0 || name.compareTo("name") == 0 || name.compareTo("type") == 0) {
						 		nodeValue = 0;
						 	} else {
								if(nvalue.compareTo("null") == 0 ) {
							 		nodeValue = 0;
							 	} else {
							 		//System.out.println("Trying to int " + nvalue );
							 		Integer myInteger = new Integer(nvalue);
							 		nodeValue = myInteger.intValue();
							 	}
						 	}
						 	
						 	
						 	if( name.compareTo("vnum")== 0) {
						 		vnum =  nodeValue;
						 	} else if( name.compareTo("short")==0) {
						 		shortDesc = nvalue;
						 	} else if( name.compareTo("long")==0 ) {
						 		longDesc = nvalue;
						 	} else if( name.compareTo("weight")== 0) {
						 		weight = nodeValue;
					 		} else if( name.compareTo("name") == 0) {
					 			itemName = nvalue;
					 		} else if( name.compareTo("hitDamage") == 0) {
					 		    hd = nodeValue;
					 		} else if( name.compareTo("type") == 0 ) {
					 		    if(nvalue.compareToIgnoreCase("sword") == 0) {
					 		        t = Item.SWORD;
					 		    } else if(nvalue.compareToIgnoreCase("helm") == 0) {
					 		        t = Item.HELM;
					 		    } else if(nvalue.compareToIgnoreCase("boots") == 0) {
					 		        t = Item.BOOTS;
					 		    } else if(nvalue.compareToIgnoreCase("greaves") == 0) {
					 		        t = Item.GREAVES;
					 		    } else if(nvalue.compareToIgnoreCase("ring") == 0) {
					 		        t = Item.RING;
					 		    }
					 		}
				        }
					 }
				}
			//TrollAttack.error("vnum:" + vnum + ", south: " + south );
			 itemList[vnum] = new Item(vnum, itemName, weight, shortDesc, longDesc, t, hd);
			 //System.out.println("Created: " + itemList[vnum].toString());
			}
			return itemList;
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
		return null ;
	}

	static public String uppercaseFirst(String string) {
	    return string.substring(0,1).toUpperCase() + string.substring(1);
	}
}
