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
	int Direction;
	Player player;
	public CommandMove() {}
	public CommandMove(Player play, String n, int d) {
		Direction = d;
		name = n;
		player = play;
	}
	public static int 	EAST = 		1,
	NORTHEAST = 2,
	NORTH = 	3,
	NORTHWEST = 4,
	WEST = 		5,
	SOUTHWEST = 6,
	SOUTH = 	7,
	SOUTHEAST = 8,
	UP = 		9,
	DOWN = 		10;
	
	public void execute(String s) {
		this.execute();
	}
	public void execute() {
		int results = 0;
		results = TrollAttack.gameRooms[player.getCurrentRoom()].followLink(Direction);
		if(results != 0) {
			player.setCurrentRoom(results);
			player.look();
		} else {
			TrollAttack.print("Alas, you cannot go that way.");
		}
 
	}
}
