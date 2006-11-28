/*
 * Created on Jan 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

//import org.jivesoftware.smack.PacketCollector;
import org.w3c.dom.Document;

import TrollAttack.Commands.AbilityHandler;
import TrollAttack.Commands.CommandHandler;
import TrollAttack.Items.Disposable;
import TrollAttack.Items.Item;
import TrollAttack.Classes.Class;


public class TrollAttack {
	public static String version = "0.89";
	
	static Document document;

    static public boolean gameOver = false;
    
    

    static Background backGround;

    
    static Calendar cal = new GregorianCalendar();

    /**
     * You need to load the items before the mobiles, and the game mobiles
     * before you can load the game rooms because the rooms contain Mobile
     * objects and item objects, and Mobiles can contain items.
     */
    public static DataReader myData;

    public static Vector<Area> gameAreas;

    public static Vector<Item> gameItems;

    public static Vector<Mobile> gameMobiles;

    public static Vector<Player> gamePlayers;
    public static Vector<Class> gameClasses;
    public static Vector<Reset> gameResets;
    
    public static Vector<Room> gameRooms;
    public static Vector<Communication> gameCommunications = new Vector<Communication>();
    
    public static AbilityHandler abilityHandler = new AbilityHandler();



    public static boolean isGameOver() {
        return gameOver;
    }

    public static CommandHandler ch;

    public static int maxIdleTime= 60 * 30;

    

    public static void main(String[] args) {
        message("Starting TrollAttack, version: " + version);

        gamePlayers = new Vector<Player>();

        reloadWorld();
        
        Communication io = new TelnetServer();
        //Communication io2 = new ImClient();
        
        // Done reading data files.
        backGround = new Background();
        backGround.start();
        //debug("main done.");

    }

    public static void addPlayer(Player player) {
        gamePlayers.add(player);
    }

    /**
     * Sends the given message to all players of the game.
     * @param message The message sent to each player.
     */
    public static void broadcast(String message) {
        for (Player currentPlayer : gamePlayers) {
            currentPlayer.interrupt(message);
        }
    }


    static public void error(String string) {
        System.out.println("ERROR:" + string);
    }

    static public void debug(String string) {
        System.out.println("DEBUG:" + string);
    }

    static public void message(String string) {
    	cal = new GregorianCalendar();
        System.out.println("(" + TrollAttack.cal.getTime().toString() + ") "  + string);
    }

    static public Player getPlayer(String s) {
        Player returnPlayer = null;
        for(Player p : gamePlayers) {
            if (p.getName().toLowerCase().startsWith(s.toLowerCase())) {
                returnPlayer = p;
            } else {
                //TrollAttack.message(s + " != " + p.getName());
            }
        }
        return returnPlayer;
    }

    static public Item getItem(Integer vnum) {
        Item returnItem = null;
        for(Item item : gameItems) {
            if (item.vnum == vnum.intValue()) {
                returnItem = item;
            }
        }
        return returnItem;
    }

    static public Mobile getMobile(Integer vnum) {
        //TrollAttack.message("Getting mobile #" + vnum.toString());
        Mobile returnMobile = null;
        for(Mobile mobile : gameMobiles) {
            if (mobile.vnum == vnum.intValue()) {
                //TrollAttack.message("We found the mobile...");
                returnMobile = mobile;
                break;
            } else {
                //TrollAttack.message("This isn't the mobile, it has vnum " +
                // mobile.vnum);
            }
        }
        if (returnMobile == null) {
            //TrollAttack.error("Couldn't find mobile #" + vnum.toString() + "
            // that we were looking for...");
        }
        return returnMobile;
    }

    static public Room getRoom(int vnum) {
        for (Room room : gameRooms) {
            if (room.vnum == vnum) {
                return room;
            } else {
            }
        }
        return null;
    }

    static public void healBeings() {
        for (Room room : gameRooms) {
            room.healBeings();
        }
    }

    static public void agePlayers(double amount) {
        for (Player player : gamePlayers) {
            player.timePlayed += amount;
        }
    }

    static public void wanderAndDecay() {
        Roll chance = new Roll("1d10");
        Vector<Mobile> wanderers = new Vector<Mobile>();
        for(Area area : gameAreas) {
            if(!area.frozen) {
                for(Room room : area.areaRooms) {
                    for(Being being : room.roomBeings) {
                        if(!being.isPlayer && ((Mobile)being).isWanderer() && chance.roll() > 7) {
                            wanderers.add( (Mobile)being );
                        }
                    }
                    for(Item i : room.roomItems) {
                    	if(i instanceof Disposable) {
                    		((Disposable)i).decay();
                    		TrollAttack.debug("Decaying " + i.getShort());
                    		if(((Disposable)i).isDone()) {
                    			room.roomItems.remove(i);
                    			TrollAttack.debug(i.getShort() + " has decayed beyond repair.");
                    			room.say(Util.uppercaseFirst(i.getShort()) + " decays away.");
                    			break;
                    		}
                    	}
                    }
                }
            }
        }
        for(Mobile mob : wanderers) {
            mob.wander();
        }
    }
    static public void hungerStrike(int strength) {
        for(Player p : gamePlayers) {
            if (++p.hunger >= 9) {
                p.hunger = 9;
                p.hitPoints -= strength;
                p.interrupt("You are " + p.getHungerString()
                        + ", and your rampant hunger causes you " + strength
                        + " damage.");
                p.prompt();

            } else {
                if (p.hunger % 2 == 0) {
                    p
                            .interrupt("Your stomach feels a little emptier and you are now "
                                    + p.getHungerString());
                }
            }
        }
        for(Player p : gamePlayers) {
            if (++p.thirst >= 9) {
                p.thirst = 9;
                p.hitPoints -= strength;
                p.interrupt("You are " + p.getThirstString()
                        + ", and your rampant thirst causes you " + strength
                        + " damage.");
            } else {
                if (p.thirst % 2 == 0) {
                    p.interrupt("Your mouth feels drier and you are now "
                            + p.getThirstString());
                }
            }
        }
    }

    /*static public void endGame() {
        gameOver = true;
        backGround.stop();
        io.killConnections();
        io.stop();
    }*/

    static public int getTime() {
        ;
        return backGround.getTime();
    }

    static public void puntIdlePlayers(int time) {
        Vector<Player> puntPlayers = new Vector<Player>();
        for(Player currentPlayer : gamePlayers) {
            if (currentPlayer.getIdleTime() > time) {
                puntPlayers.add(currentPlayer);

            }
        }
        for(Player currentPlayer : puntPlayers) {
            message("Punting player " + currentPlayer.getShort()
                    + " for idleness.");
            currentPlayer.interrupt("You have been idle for more than "
                    + (time / 60) + " minutes, kicking...");
            currentPlayer.save();
            currentPlayer.quit();
        }
    }

    public static void replaceItem(Item find, Item replace) {
        gameItems.remove(find);
        gameItems.add(replace);
        for(Room currentRoom : gameRooms) {
            currentRoom.replaceItem(find, replace);
        }
    }

    /**
     * This is a very important function that loads areas, classes, 
     * items, mobiles, and rooms from the xml files that contain them. 
     * This can be called when the game is just starting up, or to 
     * clean up the game in-game.
     *
     */
    public static void reloadWorld() {
        
    	/* Surprisingly, the order here is important. You can't create the rooms
         * if you don't already have the info about the mobs that will be in them.
         * You can't create the mobs, unless you have the info about the items that
         * they will carry.  And you can't create the Mobs unless you have the info
         * about their classes.
         */
        TrollAttack.gameResets = new Vector<Reset>();
        TrollAttack.myData = new DataReader();
        TrollAttack.gameAreas = TrollAttack.myData.getAreas();
        TrollAttack.gameClasses = TrollAttack.myData.getClasses();
        TrollAttack.gameItems = TrollAttack.myData.getItems();
        TrollAttack.gameMobiles = TrollAttack.myData.getMobiles();
        TrollAttack.gameRooms = TrollAttack.myData.getRooms();
        
        /**
         * Runs all of the resets at least once, this ensures game is set up properly 
         * without waiting for the first tick.
         */ 
        for(Reset reset : gameResets) {
            reset.run();
        }
        for(Player p : gamePlayers) {
            TrollAttack.getRoom(p.getCurrentRoom()).addBeing(p);
            p.setCurrentRoom(p.getCurrentRoom());
        }

    }

    public static void shutdown() {
        broadcast(Communication.RED + "MUD Shutting down NOW!");
        while(gamePlayers.size() > 0){
            Player p = gamePlayers.remove(0);
            p.interrupt(Communication.WHITE + "The game forces you to save and quit.");
            p.handleCommand("save");
            p.handleCommand("quit");
        }
        gameOver = true;
        try {
            Thread.sleep(200);
        } catch(Exception e) {
            
        }
        
        for(Communication c : gameCommunications) {
        	c.shutdown();
        }
        message("Game shutting down.");
        System.exit(0);
        //message(gameCommunications.toString());
       
    }

	public static void replaceRoom(Room r, Room newRoom) {
		if(!gameRooms.contains(r)) {
			TrollAttack.error("Room list doesn't contain the room we are trying to replace.");
		}
		if(gameRooms.contains(newRoom)) {
			TrollAttack.error("Room list already contains the room we are trying to replace with.");
		}
		gameRooms.remove(r);
		gameRooms.add(newRoom);
	}
}
