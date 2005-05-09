 /*
 * Created on Jan 31, 2005
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
import org.w3c.dom.Document;


public class TrollAttack {
	static Document document;
	static boolean gameOver = false;
	static Room[] gameRooms;
	static Item[] gameItems;
	static Mobile[] gameMobiles;
	static int wrapLength = 79;
	static CommandHandler ch;
	static Player player = new Player();
	public static void main(String[] args) {
		//print("starting program...");
		int a = 0;
		int direction;
		ch = new CommandHandler();
		String command;
		EasyReader stdin = new EasyReader(System.in);
        gameRooms =  Room.readData();
        gameItems = Item.readData();
        gameMobiles = Mobile.readData();
        // Done reading rooms data.
        
        look();
        while(gameOver == false) {
        	command = stdin.stringInputLine();
        	// Gets a command (south, east, west, north)
        	ch.handleCommand( command );
        	
        	if(player.getCurrentRoom() == 5) {
        		gameOver = true;
        	}
        }
        System.out.println("\n");


	}
	
	/**
	 * print function takes in a string and uses the system
	 * out as well as a wrapLength in order to make sure that 
	 * the string is not too long to fit on the chosen screen.
	 * @param string
	 */
	static public void print(String string) {
		int wrap = wrapLength;	
		while(string.length() > 0 ) {
			if(wrapLength > string.length()) {
					wrap = string.length();
			}
					System.out.println(string.substring(0,wrap));
					string = string.substring(wrap);
			}
	}
	static public void look() {
		gameRooms[player.getCurrentRoom()].pLook();
	}
	
}
