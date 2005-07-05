package TrollAttack;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLHandler {
    public LinkedList sections;
    static int loopStart, loopIncrement;
    Node node, kid, Kid;
    NodeList kids;
    public XMLHandler(Document doc) {
        sections = new LinkedList();

        node = doc;
	    kids = node.getChildNodes();
		// Kid is root item
	    Kid = kids.item(0);
	    NamedNodeMap attribs = Kid.getAttributes();
        if(attribs.getNamedItem("hacked") != null) {
	        loopStart = 1;
	        loopIncrement = 2;
	    } else {
	        loopStart = 0;
	        loopIncrement = 1;
	    }
        
	    listProcess(Kid, sections);
		
		/*
		 * I would like section to look like this:
		 * List[x] { [1]: {vnum=1, name=sword}
		 * 		[2]: {vnum=2, name=sword2, ... , item=List[2] {...}}
		 */
	}
    // Take in a node and a linked list, and attach a hash for each child of the node.
    public void listProcess(Node n, LinkedList l) {
        NodeList kids = n.getChildNodes();
        Node kid;
		
        // Loop through each of the <object> elements
		 for(int j = loopStart; j < kids.getLength(); j += loopIncrement) {
		     kid = kids.item(j);
		    // TrollAttack.message("Looping through childnode " + kid.getNodeName());
		     Hashtable newHash = new Hashtable();
		     l.add(newHash);
		     
		     if(kid.getChildNodes().getLength() > loopIncrement+loopStart) {
		         hashProcess(kid, newHash);
		     }
		     /*
		     if( ( kid.getNodeValue() == null ) && ( kid.getChildNodes().item(1) != null ) && ( kid.getChildNodes().item(1).getNodeValue() == null )  ) {
		        //TrollAttack.message("Creating kash key(" + j + ") '" + kid.getNodeName() + "' + '" + kid.getNodeValue() + "'.");
		        LinkedList newList = new LinkedList();
		        newHash.put(kid.getNodeName(), newList);
		        process(kid, newList);
		    } else {
		        //TrollAttack.message("Creating hash key(" + j + ") '" + kid.getNodeName() + "' + '" + kid.getNodeValue() + "'.");
		        
		        
		        //TrollAttack.message("Finished creating hash key.");
		    }
		    */
        }
    }
    // Take a node and a hahstable and populate the hashtable with all of the children of the node.
    public void hashProcess(Node n, Hashtable hash) {
        NodeList kids = n.getChildNodes();
        Node kid;
        
        //Look through all of the childnotes  EG: all of the attribs of an item
        for(int j = loopStart; j < kids.getLength(); j += loopIncrement) {
            kid = kids.item(j);
            //TrollAttack.message("Looping through childnode " + kid.getNodeName());
            Object currentValue  = hash.get(kid.getNodeName());
            if( currentValue != null ) {
                if( currentValue.getClass() == LinkedList.class ) {
                    LinkedList list = (LinkedList)currentValue;
                    list.add(kid.getChildNodes().item(0).getNodeValue());
                } else {
                    Object tmp = currentValue;
                    LinkedList ll = new LinkedList();
                    hash.put(kid.getNodeName(), ll);
                    ll.add(tmp);
                    ll.add(kid.getChildNodes().item(0).getNodeValue());
                }
            } else {
                hash.put( kid.getNodeName() , kid.getChildNodes().item(0).getNodeValue());
            }
        }
    }
}
