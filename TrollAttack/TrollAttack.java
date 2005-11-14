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

import org.w3c.dom.Document;

import TrollAttack.Commands.AbilityHandler;
import TrollAttack.Commands.CommandHandler;
import TrollAttack.Items.Item;
import TrollAttack.Classes.Class;


public class TrollAttack {
    static Document document;

    static public boolean gameOver = false;
    
    

    static Background backGround;

    public static Communication unusedCommunication;
    
    static Communication io;

    static Calendar cal = new GregorianCalendar();

    /**
     * You need to load the items before the mobiles, and the game mobiles
     * before you can load the game rooms because the rooms contain Mobile
     * objects and item objects, and Mobiles can contain items.
     */
    public static DataReader myData;

    public static java.util.LinkedList<Area> gameAreas;

    public static LinkedList gameItems;

    public static LinkedList gameMobiles;

    public static java.util.LinkedList<Player> gamePlayers;
    public static java.util.LinkedList<Class> gameClasses;
    public static java.util.LinkedList<Reset> gameResets;
    
    public static LinkedList gameRooms;
    
    public static AbilityHandler abilityHandler = new AbilityHandler();



    public static boolean isGameOver() {
        return gameOver;
    }

    public static CommandHandler ch;

    public static void main(String[] args) {
        //print("starting program...");

        gamePlayers = new java.util.LinkedList<Player>();

        reloadWorld();
        
        io = new Communication();
        unusedCommunication = io;
        // Done reading data files.
        backGround = new Background();
        backGround.start();

        io.start();
    }

    /**
     * print function takes in a string and uses the system out as well as a
     * wrapLength in order to make sure that the string is not too long to fit
     * on the chosen screen.
     * 
     * @param string
     */
    static public void print(String string) {
        print(string, true);
    }

    public static void addPlayer(Player player) {
        gamePlayers.add(player);
    }

    public static void broadcast(String message) {
        for (Player currentPlayer : gamePlayers) {
            currentPlayer.interrupt(message);
        }
    }
    

    static public void print(String string, boolean shouldWrap) {
        //io.print(string, shouldWrap);
    }

    static public void error(String string) {
        System.out.println("ERROR:" + string);
    }

    static public void debug(String string) {
        System.out.println("DEBUG:" + string);
    }

    static public void message(String string) {
        System.out.println("SYSTEM:" + string);
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
        Item item;
        Item returnItem = null;
        for (int i = 0; i < gameItems.length(); i++) {
            item = (Item) gameItems.getNext();
            if (item.vnum == vnum.intValue()) {
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
        for (int i = 0; i < gameMobiles.length(); i++) {
            mobile = (Mobile) gameMobiles.getNext();
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
        gameMobiles.reset();
        return returnMobile;
    }

    static public Room getRoom(int vnum) {
        Room room;
        for (int i = 0; i < gameRooms.length(); i++) {
            room = (Room) gameRooms.getNext();
            if (room.vnum == vnum) {
                gameRooms.reset();
                return room;
            } else {
            }
        }
        gameRooms.reset();
        return null;
    }

    static public void healBeings() {
        for (int i = 0; i < gameRooms.length(); i++) {
            ((Room) (gameRooms.getNext())).healBeings();
        }
        gameRooms.reset();
    }

    static public void agePlayers(double amount) {
        for (Player player : gamePlayers) {
            player.timePlayed += amount;
        }
    }

    static public void wanderLust() {
        Roll chance = new Roll("1d10");
        java.util.LinkedList<Mobile> wanderers = new java.util.LinkedList<Mobile>();
        for(Area area : gameAreas) {
            if(!area.frozen) {
                for(Room room : area.areaRooms) {
                    for(Being being : room.roomBeings) {
                        if(!being.isPlayer && ((Mobile)being).isWanderer() && chance.roll() > 3) {
                            wanderers.add( (Mobile)being );
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
        for(Player currentPlayer : gamePlayers) {
            if (currentPlayer.getIdleTime() > time) {
                message("Punting player " + currentPlayer.getShort()
                        + " for idleness.");
                currentPlayer.interrupt("You have been idle for more than "
                        + (time / 60) + " minutes, kicking...");
                currentPlayer.save();
                currentPlayer.quit();

            } else {
                //currentPlayer.tell("You have been idle for " +
                // currentPlayer.getIdleTime() + " seconds.");
            }
        }
    }

    public static void replaceItem(Item find, Item replace) {
        gameItems.delete(find);
        gameItems.add(replace);
        while (gameRooms.itemsRemain()) {
            Room currentRoom = (Room) gameRooms.getNext();
            currentRoom.replaceItem(find, replace);
        }
        gameRooms.reset();
    }

    public static void reloadWorld() {
        /* Surprisingly, the order here is important. You can't create the rooms
         * if you don't already have the info about the mobs that will be in them.
         * You can't create the mobs, unless you have the info about the items that
         * they will carry.  And you can't create the Mobs unless you have the info
         * about their classes.
         */
        
        TrollAttack.gameResets = new java.util.LinkedList<Reset>();
        TrollAttack.myData = new DataReader();
        TrollAttack.gameAreas = TrollAttack.myData.getAreas();
        TrollAttack.gameClasses = TrollAttack.myData.getClasses();
        TrollAttack.gameItems = TrollAttack.myData.getItems();
        TrollAttack.gameMobiles = TrollAttack.myData.getMobiles();
        TrollAttack.gameRooms = TrollAttack.myData.getRooms();
        
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
            Player p = gamePlayers.getFirst();
            p.interrupt(Communication.WHITE + "The game forces you to save and quit.");
            p.handleCommand("save");
            p.handleCommand("quit");
        }
        try {
            Thread.sleep(200);
        } catch(Exception e) {
            
        }
        gameOver = true;
        unusedCommunication.killConnections();
        message("Game shutting down.");
       
    }
}
