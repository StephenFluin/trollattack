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

	/**
	 *  You need to load the game mobiles before you can load
	 *  the game rooms because the rooms contain Mobile objects and item objects.
	 */
	static Mobile[] gameMobiles;
	static Item[] gameItems;
	static Room[] gameRooms;
	
	static DeadMobiles deadies = new DeadMobiles();
	
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
		gameItems = Item.readData();
        gameMobiles = Mobile.readData();
		gameRooms =  Room.readData();
        
        // Done reading data files.
        Background backGround = new Background();
		backGround.start();
        look();
        while(gameOver == false) {
        	print( player.prompt() );
        	command = stdin.stringInputLine();
        	// Gets a command (south, east, west, north)
        	ch.handleCommand( command );
        	
        	if(player.getCurrentRoom() == 40) {
        		gameOver = true;
        	}
        }
        backGround.stop();


	}
	
	/**
	 * print function takes in a string and uses the system
	 * out as well as a wrapLength in order to make sure that 
	 * the string is not too long to fit on the chosen screen.
	 * @param string
	 */
	static public void print(String string) {
		int wrap = wrapLength;	
		if(string == null) {
		    print("Contents of current room");
		    for( int i = 0; i < gameRooms[player.currentRoom].roomMobiles.length; i++ ) {
		        if(gameRooms[player.currentRoom].roomMobiles[i] != null ) {
		            print(gameRooms[player.currentRoom].roomMobiles[i].getLong());
		            throw( new Error("bad"));
		        }
		    }
		} else {
			while(string.length() > 0 ) {
				if(wrapLength > string.length()) {
						wrap = string.length();
				}
				System.out.println(string.substring(0,wrap));
				string = string.substring(wrap);
			}
		}
	}
	static public void look() {
		gameRooms[player.getCurrentRoom()].pLook();
	}
	static public void healMobiles() {
	    for(int i = 0; i < gameRooms.length;i++) {
	        if(gameRooms[i] != null) {
	            gameRooms[i].healMobiles();
	        }
	    }
	}
}
