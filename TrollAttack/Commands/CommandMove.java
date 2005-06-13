package TrollAttack.Commands;
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
	public CommandMove() {}
	public CommandMove(String n, int d) {
		Direction = d;
		name = n;
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
		results = TrollAttack.gameRooms[TrollAttack.player.getCurrentRoom()].followLink(Direction);
		if(results != 0) {
			TrollAttack.player.setCurrentRoom(results);
			TrollAttack.look();
		} else {
			TrollAttack.print("Alas, you cannot go that way.");
		}
 
	}
}
