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

 
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.w3c.dom.Document;

import TrollAttack.Commands.CommandHandler;

public class TrollAttack {
	static Document document;
	static boolean gameOver = false;
	static Background backGround;
	static Communication io;
	static Calendar cal = new GregorianCalendar();
	/**
	 *  You need to load the items before the mobiles, and the game mobiles 
	 *  before you can load the game rooms because the rooms contain Mobile 
	 *  objects and item objects, and Mobiles can contain items.
	 */
	public static LinkedList gameItems;
	public static LinkedList gameMobiles;
	public static LinkedList gamePlayers;
	public static LinkedList gameRooms;
	

	public static boolean isGameOver() {
	    return gameOver;
	}
	static DeadMobiles deadies = new DeadMobiles();
	
	
	public static CommandHandler ch;
	public static void main(String[] args) {
		//print("starting program...");
		
		String command;
		
		gameItems = Util.readItemData();
        gameMobiles = Util.readMobileData();
		gameRooms =  Util.readRoomData();
		gamePlayers = new LinkedList();
		io = new Communication();
		
		
        
        // Done reading data files.
        backGround = new Background();
		backGround.start();
    
    	io.start();

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
	public static void broadcast(String message) {
	    //TrollAttack.message("Broadcasting: " + message);
	    Player currentPlayer;
	    for(int i = 1;i <= gamePlayers.length(); i++) {
	        currentPlayer = (Player)gamePlayers.getNext();
	        currentPlayer.tell(message);
	    }
	    gamePlayers.reset();
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
	
	static public Item getItem(Integer vnum) {
	    Item item;
	    Item returnItem = null;
	    for(int i = 0;i < gameItems.length();i++) {
	        item = (Item)gameItems.getNext();
	        if(item.vnum == vnum.intValue()) {
	            returnItem = item;
	        }
	    }
	    gameItems.reset();
	    return returnItem;
	}
	static public Mobile getMobile(Integer vnum) {
	    //TrollAttack.message("Getting mobile #" + vnum.toString());
	    Mobile mobile;
	    Mobile returnMobile = null;
	    for(int i = 0;i < gameMobiles.length();i++) {
	        mobile = (Mobile)gameMobiles.getNext();
	        if(mobile.vnum == vnum.intValue()) {
	            //TrollAttack.message("We found the mobile...");
	            returnMobile = mobile;
	            break;
	        } else {
	            //TrollAttack.message("This isn't the mobile, it has vnum " + mobile.vnum);
	        }
	    }
	    if(returnMobile == null) {
	        //TrollAttack.error("Couldn't find mobile #" + vnum.toString() + " that we were looking for...");
	    }
	    gameMobiles.reset();
	    return returnMobile;
	}
	static public Room getRoom(int vnum) {
	    Room room;
	    Room returnRoom = null;
	    for(int i = 0;i < gameRooms.length();i++) {
	        room = (Room)gameRooms.getNext();
	        if(room.vnum == vnum) {
	            gameRooms.reset();
	            return room;
	        } else {
	        }
	    }
	    TrollAttack.error("Room " + vnum + " not found!");
	    gameRooms.reset();
	    return null;
	}
	static public void healBeings() {
	    for(int i = 0; i < gameRooms.length();i++) {
	            ((Room)(gameRooms.getNext())).healBeings();
	    }
	    gameRooms.reset();
	}
	
	static public void endGame() {
	    gameOver = true;
        backGround.stop();
        io.killConnections();
        io.stop();
	}
	static public int getTime() {;
		return backGround.getTime();
	}
	static public void puntIdlePlayers(int time) {
	    Player currentPlayer;

	    for(int i = 1;i <= gamePlayers.length(); i++) {
	        
	        currentPlayer = (Player)gamePlayers.getNext();
	        //message(currentPlayer.getShort() + ": idle " + currentPlayer.getIdleTime() + " seconds.");
	        if(currentPlayer.getIdleTime() > time) {
	            message("Punting player " + currentPlayer.getShort() + " for idleness.");
	            currentPlayer.tell("You have been idle for more than " + (time/60) + " minutes, kicking...");
	            currentPlayer.save();
	            currentPlayer.quit();
	            
	        } else {
	            //currentPlayer.tell("You have been idle for " + currentPlayer.getIdleTime() + " seconds.");
	        }
	    }
	    gamePlayers.reset();
	}
}
