/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import TrollAttack.LinkedList;

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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Area {
    public int low, high;

    public String filename, name;

    public boolean frozen = false;

    public int defaultClicks;

    public java.util.LinkedList<Room> areaRooms;

    public Area() {
        this(0, 0, "uncategorized.xml", "Uncategorized", 15, true);
    }

    public Area(int lowVnum, int highVnum, String areaFilename,
            String areaName, int clicks, boolean frozen) {
        low = lowVnum;
        high = highVnum;
        filename = areaFilename;
        name = areaName;
        areaRooms = new java.util.LinkedList<Room>();
        defaultClicks = clicks;
        this.frozen = frozen;
    }

    public String toString() {
        return filename + ":\t" + low + "\t" + high + "\t" + name;
    }

    public boolean freeze() {
        if (frozen == true) {
            return false;
        } else {
            frozen = true;
            for(Room room : areaRooms) {
                room.freeze();
            }
            return true;
        }
    }

    public boolean unfreeze() {
        if (frozen == true) {
            for(Room room : areaRooms) {
                room.unfreeze();
            }
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
        attribs.add(Util.nCreate(doc, "clicks", defaultClicks + ""));
        attribs.add(Util.nCreate(doc, "frozen", frozen ? "true" : "false"));

        for (int i = 0; i < attribs.length(); i++) {

            Node newAttrib = (Node) attribs.getNext();
            m.appendChild(newAttrib);
        }

        return m;
    }

    public int countBeing(Being being) {
        int number = 0;
        for(Room room : areaRooms) {
            for(Being thing : room.roomBeings) {
                if(thing == being) {
                    number++;
                }
            }
        }
        return number;
    }
    
    /**
     * Saving works by loading everything in the game into hashtables and
     * linkedlists, and then saving only the files that we wanted to
     * change/update.
     *  
     */
    public void save(LinkedList gameRooms, LinkedList gameMobiles,
            LinkedList gameItems) {
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
        Room currentRoom = null;

        // Add all of the rooms to their appropriate file hashes.
        for (int i = 0; i < gameRooms.length(); i++) {
            currentRoom = (Room) gameRooms.getNext();
            //TrollAttack.message("Adding " + currentRoom.vnum + "room to new
            // file...");
            Area.addToList(doc, areasList, currentRoom);

        }
        gameRooms.reset();

        Mobile currentMobile = null;
        for (int i = 0; i < gameMobiles.length(); i++) {
            currentMobile = (Mobile) gameMobiles.getNext();
            Area.addToList(doc, areasList, currentMobile);
        }
        gameMobiles.reset();

        Item currentItem = null;
        for (int i = 0; i < gameItems.length(); i++) {
            currentItem = (Item) gameItems.getNext();
            Area.addToList(doc, areasList, currentItem);
        }
        gameItems.reset();

        /* Deleting old files... */
        File dir = new File("Areas");
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.compareToIgnoreCase(filename) == 0;
            }
        };
        File[] children = dir.listFiles(filter);
        if (children == null) {
            throw (new Error("Couldn't find area files."));
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                children[i].delete();
            }
        }
        try {
            if (areasList.get(filename) == null) {
                TrollAttack
                        .message("The area doesn't contain info to be saved, or there is no document.");
            } else {
                Util.XMLPrint((Document) areasList.get(filename), "Areas/"
                        + filename);
                TrollAttack.message("Writing file Areas/" + filename + ".");
            }
        } catch (Exception ex) {
            TrollAttack.message("Problem with area " + filename + ".");
            Util.XMLSprint((Document) areasList.get(filename));
            ex.printStackTrace();
            throw (new Error("There was a problem writing the area file."));

        }
    }

    static public Area findArea(String filename, java.util.LinkedList<Area> gameAreas) {
        for (Area currentArea : gameAreas) {
            if (filename == null) {
                filename = "";
            }
            if (currentArea.filename.compareToIgnoreCase(filename) == 0) {
                return currentArea;
            }
        }
        return null;
    }

    static public Area test(int vnum, java.util.LinkedList<Area> gameAreas) {
        for(Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testRoom(Room room, java.util.LinkedList<Area> gameAreas) {
        int vnum = room.vnum;
        for (Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testMobile(Mobile room, java.util.LinkedList<Area> gameAreas) {
        int vnum = room.vnum;
        for(Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testItem(Item room, java.util.LinkedList<Area> gameAreas) {
        int vnum = room.vnum;
        for(Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public void addToList(Document doc, Hashtable<String, Document> hash, Room room) {
        Area roomArea = Area.testRoom(room, TrollAttack.gameAreas);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if (rooms == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Hashtable<String, Document> areasList = new Hashtable<String, Document>();
            Document docu = builder.newDocument();
            n = docu.createElement("TrollAttack");
            docu.appendChild(n);
            n.appendChild(roomArea.toNode(docu));
            hash.put(filename, docu);
            //TrollAttack.message("finished adding to the hash");
            //TrollAttack.message(hash.toString());
            n.appendChild(room.toNode(docu));
        } else {
            n = ((Document) rooms).getFirstChild();
            n.appendChild(room.toNode((Document) rooms));
        }

    }

    static public void addToList(Document doc, Hashtable<String, Document> hash, Mobile room) {
        Area roomArea = Area.testMobile(room, TrollAttack.gameAreas);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if (rooms == null) {

            n = doc.createElement("TrollAttack");
            doc.appendChild(n);
            n.appendChild(roomArea.toNode(doc));
            hash.put(filename, doc);
        } else {
            n = ((Document) rooms).getFirstChild();
        }
        n.appendChild(room.toNode(((Document) rooms)));
    }

    static public void addToList(Document doc, Hashtable<String, Document> hash, Item room) {
        Area roomArea = Area.testItem(room, TrollAttack.gameAreas);
        String filename = roomArea.filename;
        Object rooms = hash.get(filename);
        Node n;
        if (rooms == null) {

            n = doc.createElement("TrollAttack");
            doc.appendChild(n);
            n.appendChild(roomArea.toNode(doc));
            hash.put(filename, doc);

            // This fixes a null pointer exception.
            rooms = doc;
        } else {
            n = ((Document) rooms).getFirstChild();
        }
        // How does this command work???? causes a null pointer
        // because (Document)rooms is null,, 7/9/05 (USF)
        if ((Document) rooms == null) {
            throw (new Error("The 'rooms' variable is null when saving area "
                    + filename + "!"));
        } else {
        }
        n.appendChild(room.toNode(((Document) rooms)));
        //n.appendChild(room.toNode(doc));
    }

}
