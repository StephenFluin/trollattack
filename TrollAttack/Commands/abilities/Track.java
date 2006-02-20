/*
 * Created on Nov 2, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Commands.abilities;
import TrollAttack.*;
import TrollAttack.Commands.*;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Track extends Ability {
    public Track() {
        super("track");
    }
    public boolean execute(Being player) {
        player.tell("Usage: track <being>");
        return false;
    }
    public LinkedList<Path> trackTasks;
    public boolean run(Being player, String command) {
    	trackTasks = new LinkedList<Path>();
        double tmpProficiency = player.getProficiency(this);
        LinkedList<Path> now;
        int distance = 0;
        
        Path n = new Path( player.getActualRoom(), command );
        n.track();
        now = trackTasks;
        
        while(tmpProficiency > 0) {
        	trackTasks = new LinkedList<Path>();
        	for(Path p : now) {
        		if(p.track() != null) {
        			player.tell("You sense something to the " + p.path.getFirst().getDirectionName() + ".");
        			return true;
        		}
        	}
        	//TrollAttack.debug("Completed round of tracking.");
        	now = trackTasks;
        	
        	distance++;
        	tmpProficiency -= 20;
        }
        player.tell("You track within " + distance + " rooms, and you can't find '" + command + "'.");
        	
            

        return false;
    }
    class Path {
    	public LinkedList<Exit> path;
    	public Room room;
    	public String name;
    	public Path(Room r, String name) {
    		path = new LinkedList<Exit>();
    		room = r;
    		this.name = name;
    	}
    	public Path(Path e) {
    		super();
    		room = e.room;
    		name = e.name;
    		path = new LinkedList<Exit>();
    		for(Exit exit : e.path) {
    			addExit(exit);
    		}
    	}
    	public void addExit(Exit e) {
    		path.add(e);
    	}
    	public Path track() {
    		return track(true);
    	}
    	public Path track(boolean searchHere) {
    		if(searchHere) {
    			for(Being b : room.roomBeings) {
    				if(Util.contains(b.name, name)) {
    					return this;
    				}
    			}
    		}
    		//TrollAttack.debug("Tracking all rooms from " + room.title + " (" + room.vnum + ").");
    		for(Exit exit : room.roomExits) {
	    		if(!path.contains(exit)) {
    				Path p = new Path(this);
	    			p.addExit(exit);
	    			p.room = exit.getDestinationRoom();
	    			trackTasks.add(p);
	    		}
    		}
    		return null;
    	}
    	
    }

}
