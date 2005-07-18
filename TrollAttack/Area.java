/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Item;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Area {
    public int low, high;
    public String filename, name;
    public boolean frozen = false;
    public int defaultClicks;
    public LinkedList rooms;
    public Area() {
        this(0,0,"uncategorized.xml","Uncategorized", 15, true);
    }
    public Area(int lowVnum, int highVnum, String areaFilename, String areaName, int clicks, boolean frozen) {
        low = lowVnum;
        high = highVnum;
        filename = areaFilename;
        name = areaName;
        rooms = new LinkedList();
        defaultClicks = clicks;
        this.frozen = frozen;
    }
    public String toString() {
        return filename + ":\t" + low + "\t" + high + "\t" + name;
    }
    public boolean freeze() {
        if(frozen == true) {
            return false;
        } else {
            frozen = true;
	        while(rooms.itemsRemain()) {
	            ((Room)rooms.getNext()).freeze();
	        }
	        rooms.reset();
	        return true;
        }
    }
    public boolean unfreeze() {
        if(frozen == true) {
            while(rooms.itemsRemain()) {
	            ((Room)rooms.getNext()).unfreeze();
	        }
	        rooms.reset();
	        return true;
        } else {
            return false;
        }
    }
    public Node toNode(Document doc) {
 		   
 		    Node m = doc.createElement("area");
 		    LinkedList attribs = new LinkedList();
 		    attribs.add(Util.nCreate(doc, "low", low + ""));
 		    attribs.add(Util.nCreate(doc, "high", high + ""));
 		    attribs.add(Util.nCreate(doc, "filename", filename));
 		    attribs.add(Util.nCreate(doc, "name", name + ""));
 		    attribs.add(Util.nCreate(doc, "clicks",defaultClicks + "" ));
 		    attribs.add(Util.nCreate(doc, "frozen", frozen ? "true" : "false"));
 		    
 		    for(int i = 0; i < attribs.length(); i++) {
 		        
 		        Node newAttrib = (Node)attribs.getNext();
 		        m.appendChild(newAttrib);
 		    }
 		    
 		    return m;
 		}

 
 		
 	public void save() {
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
               return name.compareToIgnoreCase(filename) == 0;
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
        try{
            if(areasList.get(filename) == null) {
                TrollAttack.message("The area doesn't contain info to be saved, or there is no document.");
            } else {
		        Util.XMLPrint((Document)areasList.get(filename), "Areas/" + filename);
		        TrollAttack.message("Writing file Areas/"+ filename + ".");
            }
		} catch(Exception ex) {
		    TrollAttack.message("Problem with area " + filename + ".");
		    Util.XMLSprint((Document)areasList.get(filename));
		    TrollAttack.error("There was a problem writing the area file.");
		    ex.printStackTrace();
		}
 	}
 		
    static public Area findArea(String s) {
        Area currentArea;
        while(TrollAttack.gameAreas.itemsRemain()) {
            currentArea = (Area)TrollAttack.gameAreas.getNext();
            if(s == null) {
                s = "";
            }
            if(currentArea
                    .filename
                    .compareToIgnoreCase(
                            s
                            ) == 0) {
                TrollAttack.gameAreas.reset();
                return currentArea;
            }
        }
        TrollAttack.gameAreas.reset();
        return null;
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
