/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Area {
    public int low, high;
    public String filename, name;
    public LinkedList rooms;
    public Area() {
        this(0,0,"uncategorized.xml","Uncategorized");
    }
    public Area(int lowVnum, int highVnum, String areaFilename, String areaName) {
        low = lowVnum;
        high = highVnum;
        filename = areaFilename;
        name = areaName;
        rooms = new LinkedList();
    }
    public void freeze() {
        while(rooms.itemsRemain()) {
            ((Room)rooms.getNext()).freeze();
        }
        rooms.reset();
    }
    public void unfreeze() {
        while(rooms.itemsRemain()) {
            ((Room)rooms.getNext()).unfreeze();
        }
        rooms.reset();
    }
    public Node toNode(Document doc) {
 		   
 		    Node m = doc.createElement("area");
 		    LinkedList attribs = new LinkedList();
 		    attribs.add(Util.nCreate(doc, "low", low + ""));
 		    attribs.add(Util.nCreate(doc, "high", high + ""));
 		    attribs.add(Util.nCreate(doc, "filename", filename));
 		    attribs.add(Util.nCreate(doc, "name", name + ""));
 		    
 		    for(int i = 0; i < attribs.length(); i++) {
 		        
 		        Node newAttrib = (Node)attribs.getNext();
 		        m.appendChild(newAttrib);
 		    }
 		    
 		    return m;
 		}

 		public String[] look() {
 		String[] items = new String[255];
 		return items;
    }
    static public Area test(int vnum) {
        Area currentArea;
        while(TrollAttack.gameAreas.itemsRemain()) {
            currentArea = (Area)TrollAttack.gameAreas.getNext();
            if(vnum >= currentArea.low && vnum <= currentArea.high) {
                TrollAttack.gameAreas.reset();
                return currentArea;
            }
        }
        TrollAttack.gameAreas.reset();
        return new Area();
    }
    static public Area testRoom(Room room) {
        Area currentArea;
        int vnum = room.vnum;
        for(int i = 0;i < TrollAttack.gameAreas.length();i++) {
            currentArea = (Area)TrollAttack.gameAreas.getNext();
            if(vnum >= currentArea.low && vnum <= currentArea.high) {
                TrollAttack.gameAreas.reset();
                return currentArea;
            }
        }
        TrollAttack.gameAreas.reset();
        return new Area();
    }
    static public Area testMobile(Mobile room) {
        Area currentArea;
        int vnum = room.vnum;
        for(int i = 0;i < TrollAttack.gameAreas.length();i++) {
            currentArea = (Area)TrollAttack.gameAreas.getNext();
            if(vnum >= currentArea.low && vnum <= currentArea.high) {
                TrollAttack.gameAreas.reset();
                return currentArea;
            }
        }
        TrollAttack.gameAreas.reset();
        return new Area();
    }
    static public Area testItem(Item room) {
        Area currentArea;
        int vnum = room.vnum;
        for(int i = 0;i < TrollAttack.gameAreas.length();i++) {
            currentArea = (Area)TrollAttack.gameAreas.getNext();
            if(vnum >= currentArea.low && vnum <= currentArea.high) {
                TrollAttack.gameAreas.reset();
                return currentArea;
            }
        }
        TrollAttack.gameAreas.reset();
        return new Area();
    }
    static public void addToList(Document doc, Hashtable hash, Room room) {
        Area roomArea = Area.testRoom(room);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if(rooms == null) {
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
            Document docu = builder.newDocument();
            //TrollAttack.message("Hashtable for " + filename + " didn't contain this file yet.");
            //TrollAttack.message(hash.toString());
    	    n = docu.createElement("TrollAttack");
    	    docu.appendChild(n);
    	    n.appendChild(roomArea.toNode(docu));
    	    hash.put(filename, docu);
    	    //TrollAttack.message("finished adding to the hash");
    	    //TrollAttack.message(hash.toString());
    	    n.appendChild(room.toNode(docu));
        } else {
            n = ((Document)rooms).getFirstChild();
            n.appendChild(room.toNode((Document)rooms));
        }
        
    }
    static public void addToList(Document doc, Hashtable hash, Mobile room) {
        Area roomArea = Area.testMobile(room);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if(rooms == null) {
            
    	    n = doc.createElement("TrollAttack");
    	    doc.appendChild(n);
    	    n.appendChild(roomArea.toNode(doc));
    	    hash.put(filename, doc);
        } else {
            n = ((Document)rooms).getFirstChild();
        }
        n.appendChild(room.toNode(((Document)rooms)));
    }
    static public void addToList(Document doc, Hashtable hash, Item room) {
        Area roomArea = Area.testItem(room);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if(rooms == null) {
            
    	    n = doc.createElement("TrollAttack");
    	    doc.appendChild(n);
    	    n.appendChild(roomArea.toNode(doc));
    	    hash.put(filename, doc);
        } else {
            n = ((Document)rooms).getFirstChild();
        }
        n.appendChild(room.toNode(((Document)rooms)));
    }
    
}
