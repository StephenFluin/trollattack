/*
 * Created on Jul 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;


import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;
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

    public Vector<Room> areaRooms;
    public Area() {
        this(0, 0, "uncategorized.xml", "Uncategorized", 15, true);
    }

    public Area(int lowVnum, int highVnum, String areaFilename,
            String areaName, int clicks, boolean frozen) {
        low = lowVnum;
        high = highVnum;
        filename = areaFilename;
        name = areaName;
        areaRooms = new Vector<Room>();
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
        Vector<Node> attribs = new Vector<Node>();
        attribs.add(Util.nCreate(doc, "low", low + ""));
        attribs.add(Util.nCreate(doc, "high", high + ""));
        attribs.add(Util.nCreate(doc, "filename", filename));
        attribs.add(Util.nCreate(doc, "name", name + ""));
        attribs.add(Util.nCreate(doc, "clicks", defaultClicks + ""));
        attribs.add(Util.nCreate(doc, "frozen", frozen ? "true" : "false"));

        for(Node newAttrib : attribs) {
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
     * Saving works by loading everything known to be in the game from the 
     * given list of rooms, mobiles, and items.  It then dumps this information 
     * into hashtables and linkedlists, and then saving only the files that we 
     * wanted to change/update.
     * 
     * We may want to change this from taking in 3 lists to just accessing game 
     * data directly.  Also, eventually we probably want each area to be aware 
     * of its own information instead of storing all of this data in the game.
     * 
     *  @param gameRooms A list of rooms that we know about.
     *  @param gameMobiles A list of mobiles that we know about.
     *  @param gameItems A list of items that we know about.
     */
    public void save(Vector<Room> gameRooms, Vector<Mobile> gameMobiles,
            Vector<Item> gameItems) {
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

        // Add all of the rooms to their appropriate file hashes.
        for (Room currentRoom : gameRooms) {
            Area.addToList(doc, areasList, currentRoom);

        }
        for (Mobile currentMobile : gameMobiles) {
            Area.addToList(doc, areasList, currentMobile);
        }

        for (Item currentItem : gameItems) {
            Area.addToList(doc, areasList, currentItem);
        }

        /* Delete only the file we are saving... */
        File dir = new File("Areas");
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.compareToIgnoreCase(filename) == 0;
            }
        };
        File[] children = dir.listFiles(filter);
        if (children == null) {
        	TrollAttack.debug("Couldn't find the area file we are trying to delete before we rewrite it.");
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
                        .message("The game isn't aware of any items, rooms, or mobiles belonging to this area, so it can't be found.");
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

    static public Area findArea(String filename, Vector<Area> gameAreas) {
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

    static public Area test(int vnum, Vector<Area> gameAreas) {
        for(Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testRoom(Room room, Vector<Area> gameAreas) {
        int vnum = room.vnum;
        for (Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testMobile(Mobile room, Vector<Area> gameAreas) {
        int vnum = room.vnum;
        for(Area currentArea : gameAreas) {
            if (vnum >= currentArea.low && vnum <= currentArea.high) {
                return currentArea;
            }
        }
        return new Area();
    }

    static public Area testItem(Item room, Vector<Area> gameAreas) {
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
        n.appendChild(room.toItemNode(((Document) rooms)));
        //n.appendChild(room.toNode(doc));
    }
    
    public boolean equals(Area a) {
    	return (a.filename.equals(filename) && a.frozen == frozen && a.high == high && a.low == low && a.name.equals(name));
    }

    /**
     * Deletes an area from the entire game.  Removes the area from the game 
     * listing, deletes all associated resets, relocates all players within it, 
     * and deletes the file storing the area.
     *
     */
	public void delete() {
		TrollAttack.gameAreas.remove(this);
		
		
		Vector<Reset> deletionList = new Vector<Reset>();
		for(Reset r : TrollAttack.gameResets) {
			if(r.area == this) {
				deletionList.add(r);
			}
		}
		for(Reset r : deletionList) {
			TrollAttack.gameResets.remove(r);
		}
		
		for(Player p : TrollAttack.gamePlayers) {
			Area pArea, eArea;
			pArea = p.getActualArea();
			eArea = new Area();
			if(pArea == this || pArea.equals(eArea)) {
				
				p.transport(1);
				p.interrupt(Communication.WHITE + "The place where you were no longer is.");
			} else {
				//p.interrupt("Sorry to bother you, but you weren't in the area that just got deleted (You are in " + p.getActualArea().toString() + " and we are deleting " + toString() + "!");
			}
			if(p.getArea() == this) {
				p.setArea(null);
			}
		}
		
		File areaFile = new File("Areas/" + filename);
		areaFile.delete();
		
		
	}

}
