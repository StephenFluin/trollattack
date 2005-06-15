package TrollAttack.Commands;
import TrollAttack.*;
/*
 * Created on May 7, 2005
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
public class CommandMove extends Command {
	int direction;
	Player player;
	public CommandMove() {}
	public CommandMove(Player play, String n, int d) {
		direction = d;
		name = n;
		player = play;
	}
	public static int 	EAST = 		1,
	NORTHEAST = 7,
	NORTH = 	3,
	NORTHWEST = 9,
	WEST = 		2,
	SOUTHWEST = 8,
	SOUTH = 	4,
	SOUTHEAST = 10,
	UP = 		5,
	DOWN = 		6;
	public String directionName(int direction) {
	    String[] directionList = {"east", "west", "north", "south", "up", "down", "northeast", "southwest", "northwest", "southeast"};
	    return directionList[direction - 1];
	}
	public int directionOpposite(int direction) {
	    if(direction % 2 == 0) {
	        return direction - 1;
	    } else {
	        return direction + 1;
	    }
	}
	
	public void execute(String s) {
		this.execute();
	}
	public void execute() {
	    //TrollAttack.error("Attempting to go!");
		int results = 0;
		Room previousRoom = TrollAttack.gameRooms[player.getCurrentRoom()];
		
		//player.tell(player.getCurrentRoom() + "");
		results = previousRoom.followLink(direction);
		if(results != 0) {
		    Room nextRoom = TrollAttack.gameRooms[results];
		    try{
		        nextRoom.say(Util.uppercaseFirst(player.getShort()) + " arrives from the " + directionName(directionOpposite(direction)) + ".");
		    } catch(Exception e) {
		        e.printStackTrace();
		    }
		    player.setCurrentRoom(results);
			previousRoom.removePlayer(player);
			nextRoom.addPlayer(player);
			previousRoom.say(Util.uppercaseFirst(player.getShort()) + " leaves to the " + name);
		    player.look();
		} else {
			player.tell("Alas, you cannot go that way.");
		}
		//player.tell(player.getCurrentRoom() + "");
 
	}
}
