package TrollAttack.Commands;
import TrollAttack.*;
import TrollAttack.Player;
import TrollAttack.Room;
import TrollAttack.TrollAttack;
import TrollAttack.Util;
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
	


	
	public void execute(String s) {
		this.execute();
	}
	public void execute() {
	    //TrollAttack.error("Attempting to go!");
		Exit results = null;
		Room previousRoom = player.getActualRoom();
		
		//player.tell(player.getCurrentRoom() + "");
		results = previousRoom.followLink(direction);
		if(results != null) {
		    if(!results.isOpen()) {
		        player.tell("The door is closed.");
		        return;
		    }
		    Room nextRoom = TrollAttack.getRoom(results.getDestination());
		    try{
		        nextRoom.say(Util.uppercaseFirst(player.getShort()) + " arrives from the " + Exit.directionName(Exit.directionOpposite(direction)) + ".");
		    } catch(Exception e) {
		        TrollAttack.error("Error in CommandMove when attempting to say something as player moves to next room.");
		        if(nextRoom == null) {
		            TrollAttack.error("The next room is null, uh oh! (" + results + ").");
		        }
		        e.printStackTrace();
		    }
		    player.setCurrentRoom(results.getDestination());
			previousRoom.removePlayer(player);
			previousRoom.say(Util.uppercaseFirst(player.getShort()) + " leaves to the " + name);
			nextRoom.addPlayer(player);
			player.look();
		} else {
			player.tell("Alas, you cannot go that way.");
			/*player.tell("Alas, you cannot go that way (" + name + ", " + direction + "), only exits are:");
			while(player.getActualRoom().roomExits.itemsRemain()) {
			    Exit tmp = (Exit) player.getActualRoom().roomExits.getNext();
			    player.tell(tmp.getDirectionName() + ", " + tmp.getDirection());
			}
			player.getActualRoom().roomExits.reset();*/
		}
		//player.tell(player.getCurrentRoom() + "");
 
	}
}
