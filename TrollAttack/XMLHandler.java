package TrollAttack;

import java.util.Hashtable;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLHandler {
    public LinkedList sections = new LinkedList();
    static int loopStart, loopIncrement;
    Node node, kid, Kid;
    NodeList kids;
    public XMLHandler(Document doc) {
        this(doc, true);
    }
    public XMLHandler(Document doc, boolean hackit) {
        
        if(hackit) {
	        loopStart = 1;
	        loopIncrement = 2;
	    } else {
	        loopStart = 0;
	        loopIncrement = 1;
	    }
        node = doc;
	    kids = node.getChildNodes();
		// Kid is root item
	    Kid = kids.item(0);
	    
	    // kid is <object>List
	    kid = Kid.getChildNodes().item(loopStart);
		process(kid, sections);
	}
    public void process(Node n, LinkedList l) {
        NodeList kids = n.getChildNodes();
        Node kid;
		Hashtable newHash = new Hashtable();
		l.add(newHash);
		 for(int j = loopStart; j < kids.getLength(); j += loopIncrement) {
		    kid = kids.item(j);
		    if( ( kid.getNodeValue() == null ) && ( kid.getChildNodes().item(1) != null ) && ( kid.getChildNodes().item(1).getNodeValue() == null )  ) {
		        //TrollAttack.message("Creating kash key(" + j + ") '" + kid.getNodeName() + "' + '" + kid.getNodeValue() + "'.");
		        LinkedList newList = new LinkedList();
		        newHash.put(kid.getNodeName(), newList);
		        process(kid, newList);
		    } else {
		        //TrollAttack.message("Creating hash key(" + j + ") '" + kid.getNodeName() + "' + '" + kid.getNodeValue() + "'.");
		        
		        newHash.put(kid.getNodeName() , kid.getChildNodes().item(0).getNodeValue());
		        //TrollAttack.message("Finished creating hash key.");
		    }
        }
    }
}
