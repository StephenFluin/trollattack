import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

//import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Item {
	int vnum, weight;
	String  shortDesc = "", longDesc = "", name = "";
	static String dataFile = "items.xml";
	
	public Item(int v, String n, int w, String s, String l) {
	 vnum = v;
	 name = n;
	 weight = w;
	 shortDesc = s;
	 longDesc = l;
	 //TrollAttack.print("Creating item");
	}
	
	public String toString() {
		return vnum + ":" +
		name + "," +
		weight + "," +
		shortDesc + "," +
		longDesc;
					
	}
	

	public static Item[] readData() {
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
			int vnum = 0, weight = 0;
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
				weight = 0;
				shortDesc = longDesc = itemName = "";
				
				if( kid.getNodeType() != Node.TEXT_NODE ) {
					NodeList children = kid.getChildNodes();
					
					for (int i = 1; i < children.getLength(); i += 2) {
						
						Node child = children.item(i);
						if( child.getNodeType() != Node.TEXT_NODE ) {
							//printNode(child, "");
							String name = child.getNodeName();
							String nvalue = child.getChildNodes().item(0) + "";
							//System.out.println("\"" + name + "->" + nvalue + "\"");
						 	int nodeValue;
						 	if(name.compareTo("short") == 0 || name.compareTo("long") == 0 || name.compareTo("name") == 0) {
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
					 		}
				        }
					 }
				}
			//TrollAttack.print("vnum:" + vnum + ", south: " + south );
			 itemList[vnum] = new Item(vnum, itemName, weight, shortDesc, longDesc);
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
	public String[] look() {
		String[] items = new String[255];
		return items;
	}
	public String getLong() {
		return longDesc;
	}
	public String getShort() {
		return shortDesc;
	}
	public String getName() {
		return name;
	}

}
