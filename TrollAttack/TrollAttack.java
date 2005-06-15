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
package TrollAttack;

 
import org.w3c.dom.Document;
import java.util.Calendar;
import java.util.GregorianCalendar;

import TrollAttack.Commands.CommandHandler;

public class TrollAttack {
	static Document document;
	static boolean gameOver = false;
	static Background backGround;
	static Communication io;
	static Calendar cal = new GregorianCalendar();
	/**
	 *  You need to load the game mobiles before you can load
	 *  the game rooms because the rooms contain Mobile objects and item objects.
	 */
	public static Mobile[] gameMobiles;
	public static LinkedList gamePlayers;
	public static Item[] gameItems;
	public static Room[] gameRooms;
	public static boolean isGameOver() {
	    return gameOver;
	}
	static DeadMobiles deadies = new DeadMobiles();
	
	
	public static CommandHandler ch;
	public static void main(String[] args) {
		//print("starting program...");
		int a = 0;
		int direction;
		
		String command;
		io = new Communication();
		
		gameItems = Util.readItemData();
        gameMobiles = Mobile.readData();
		gameRooms =  Room.readData();
		gamePlayers = new LinkedList();
        
        // Done reading data files.
        backGround = new Background();
		backGround.start();
    
    	io.run();

	}
	
	/**
	 * print function takes in a string and uses the system
	 * out as well as a wrapLength in order to make sure that 
	 * the string is not too long to fit on the chosen screen.
	 * @param string
	 */
	static public void print(String string) {
	    print(string, true);
	}
	public static void addPlayer(Player player) {
	    	gamePlayers.add(player);
	}
	public static void healAllPlayers(int health) {
	    Player player;
	    for(int i = 1;i <= gamePlayers.length(); i++) {
	        if(gamePlayers.find(i) != null ) {
	            player = (Player)gamePlayers.find(i);
	            player.increaseHitPoints(health + player.getState());
	            player.increaseManaPoints(health + player.getState());
	            player.increaseMovePoints(health + player.getState());
	        }
	    }
	}
	public static void broadcast(String message) {
	    for(int i = 1;i <= gamePlayers.length(); i++) {
	        ((Player)gamePlayers.find(i)).tell(message);
	    }
	}
	static public void print(String string, boolean shouldWrap) {
	    //io.print(string, shouldWrap);
	}
	static public void error(String string) {
	    System.out.println(string);
	}
	static public void message(String string) {
	   // System.out.println(cal.MONTH + "/" + cal.DAY_OF_MONTH + "/" + cal.YEAR + " " + cal.HOUR + ":" + cal.MINUTE + ((cal.AM == 0) ? "PM" : "AM") + " " + string);
	    error(string);
	}
	static public void healMobiles() {
	    for(int i = 0; i < gameRooms.length;i++) {
	        if(gameRooms[i] != null) {
	            gameRooms[i].healMobiles();
	        }
	    }
	}
	static public void endGame() {
	    gameOver = true;
        backGround.stop();
        io.killConnections();
        io.stop();
	}
}
