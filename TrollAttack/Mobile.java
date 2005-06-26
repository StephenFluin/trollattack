package TrollAttack;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Created on May 5, 2005
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
public class Mobile extends Being {
	int vnum, reSpawn;
	String  longDesc = "";
	static String dataFile = "mobiles.xml";
	
	public Mobile(int v, int leve, String n, int h, int mh, int hSkill, int hDamage, int rSpawn, String s, String l) {
	 vnum = v;
	 level = leve;
	 isPlayer = false;
	 name = n;
	 hitPoints = h;
	 maxHitPoints = mh;
	 hitSkill = hSkill;
	 hitDamage = hDamage;
	 shortDescription = s;
	 longDesc = l;
	 reSpawn = rSpawn;
	 //TrollAttack.error("Creating mobile #" + v);
	 this.setPrompt("<%h>");
	}
	
	// Is there a better way to duplicate a mobile?
	public Mobile( Mobile m ) {
	    vnum = m.vnum;
	    name = m.name;
	    hitPoints = m.hitPoints;
	    maxHitPoints = m.maxHitPoints;
	    hitSkill = m.hitSkill;
	    hitDamage = m.hitDamage;
	    shortDescription = m.shortDescription;
	    longDesc = m.longDesc;
	    reSpawn = m.reSpawn;
	    setPrompt("<%h>");
	    isPlayer = m.isPlayer;
	    level = m.level;
	}
	
	public String toString() {
		return vnum + ":" +
		name + "," +
		hitPoints + "/" + maxHitPoints + "," +
		super.getShort() + "," +
		longDesc;
					
	}
	public String getLong() {
		return longDesc;
	}
	public String getname() {
		return name;
	}
	public int getRespawnTime() {
		return reSpawn;
	}
	

	public static Mobile[] readData() {

    
        
        Document doc = Util.xmlize( dataFile );
        // Print the document from the DOM tree and feed it an initial 
        // indentation of nothing

		Node node = doc;
		// @TODO: Stop hardcoding of max items limit.
		final Mobile[] mobileList = new Mobile[255];
		int vnum = 0, hp = 0, maxhp = 0, hitskill = 0, hitdamage = 0, rSpawn = 0, leve = 0;
		String shortDesc = "", longDesc = "", mobileName = "";
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
			hp = maxhp = hitskill = hitdamage = rSpawn = leve = 0;
			shortDesc = longDesc = mobileName = "";
			
			if( kid.getNodeType() != Node.TEXT_NODE ) {
				NodeList children = kid.getChildNodes();
				
				for (int i = 1; i < children.getLength(); i += 2) {
					
					Node child = children.item(i);
					if( child.getNodeType() != Node.TEXT_NODE ) {
						//printNode(child, "");
						String name = child.getNodeName();
						String nvalue = child.getChildNodes().item(0).getNodeValue() + "";
						//System.out.println("\"" + name + "->" + nvalue + "\"");
					 	int nodeValue;
					 	if(name.compareTo("short") == 0 || name.compareTo("long") == 0 || name.compareTo("name") == 0) {
					 		nodeValue = 0;
					 	} else {
							if(nvalue.compareTo("null") == 0 ) {
						 		nodeValue = 0;
						 	} else {
						 		Integer myInteger = new Integer(nvalue);
						 		nodeValue = myInteger.intValue();
						 	}
					 	}
					 	
					 	
					 	if( name.compareTo("vnum")== 0) {
					 		vnum =  nodeValue;
					 	} else if( name.compareTo("level") == 0 ) {
					 	    leve = nodeValue;
					 	} else if( name.compareTo("short")==0) {
					 		shortDesc = nvalue;
					 	} else if( name.compareTo("long")==0 ) {
					 		longDesc = nvalue;
					 	} else if( name.compareTo("hp")== 0) {
					 		hp = nodeValue;
					 	} else if( name.compareTo("maxhp") == 0 ) {
					 		maxhp = nodeValue;
				 		} else if( name.compareTo("hitskill") == 0 ) {
				 			hitskill = nodeValue;
				 		} else if( name.compareTo("hitdamage") == 0 ) {
				 			hitdamage = nodeValue;
				 		} else if( name.compareTo("name") == 0) {
				 			mobileName = nvalue;
				 		} else if( name.compareTo("respawn") == 0 ) {
				 			rSpawn = nodeValue;
				 		}
			        }
				 }
			}
		 mobileList[vnum] = new Mobile(vnum, leve, mobileName, hp, maxhp, hitskill, hitdamage, rSpawn, shortDesc, longDesc);
		 //System.out.println("Created: " + itemList[vnum].toString());
		}
		return mobileList;

	}
	
	
}
