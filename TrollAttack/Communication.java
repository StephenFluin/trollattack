package TrollAttack;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;
/*
 * Created on Jun 8, 2005
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
public abstract class Communication extends Thread {
    static int wrapLength = 76;

    int ID;
   

    static enum Channel {BUILD, CHAT, MUD, STANDARD};
    

    Player player;

    public int getID() {
        return ID;
    }
    public abstract void close();
    public abstract void waitForUser();
    public void run() {
    	TrollAttack.gameCommunications.add(this);
        waitForUser();
        //TrollAttack.debug("Send and Receive methods have been registered, prompting for login.");
        String inputLine = "";
        try {
            try {
                player = new Player(this);
            } catch(Exception e) {
                e.printStackTrace();
            }
            player = authenticate(player);
            if (player == null) {
                close();
                return;
            }
            
            player.setLastActive(TrollAttack.getTime());
            player.setCommunication(this);
            TrollAttack.broadcast(PURPLE + player.getShort()
                    + " has joined our ranks.");
            TrollAttack.addPlayer(player);
            player.getActualRoom().addBeing(player);
            Player[] pBroadcast = { player };
            player.getActualRoom()
                    .say(WHITE + player.getShort() + " enters the room.",
                            pBroadcast);

            player.look();
            player.tell();
            player.prompt();
            // Main loop of entire game for each player.
            while (player.authenticated == true && !TrollAttack.gameOver) {
                // TrollAttack.message(ID + ": Accepting command...");
                try {
                    inputLine = getLine();
                    player.handleCommand(inputLine);
                } catch (Exception e) {
                    TrollAttack.message(player.getShort() + " logged out.");
                    break;
                }
            }

            TrollAttack.gameCommunications.remove(this);
        } catch (Exception e) {
            TrollAttack.error("Exception in Server: " + e.toString());
            e.printStackTrace();
        }
    }
    public abstract String color(String s);
    public Player authenticate(Player player) {
        Player tmpPlayer = null;
        String name = "";
        String pass = "";
        int attempts = 0;
        while (tmpPlayer == null) {
            try {
                print(Util.getMOTD(), false);
                player.tell(WHITE +"What is your name (or type "
                        + Communication.CYAN + "new" + Communication.WHITE
                        + " for a new character)?");
                name = getLine();
                //TrollAttack.message("Read name " + name + ".");
                if (name == null) {
                    continue;
                }
                if (name.compareToIgnoreCase("new") == 0) {
                    pass = player.interactiveNewPlayer();
                    tmpPlayer = player;
                   
                } else {

                    player.tell(name + "'s password:");
                    //TrollAttack.debug("Login Problems - Start");
                    pass = getLine();
                    tmpPlayer = DataReader.readPlayerFile(name);
                    //TrollAttack.debug("Login Problems - Read player file.");
                    if (tmpPlayer != null) {
                        Player offendingPlayer = null;
                        for(Player p : TrollAttack.gamePlayers) {
                            if(p.equals(tmpPlayer)) {
                                offendingPlayer = p;
                                break;
                            } else {
                               //TrollAttack.debug("Found a player " + p.getShort() + ", but he is not " + tmpPlayer.getShort());
                            }
                        }
                        if(offendingPlayer != null) {
                        	//TrollAttack.debug("Login Problems - Double login.");
                            offendingPlayer.tell("You are attempting to log in, that means you are OUT OF HERE!");
                            
                            offendingPlayer.save();
                            //TrollAttack.debug("Login Problems - saved.");
                            offendingPlayer.quit();
                            //TrollAttack.debug("Login Problems - fquited.");
                        }
                        tmpPlayer.authenticated = true;
                        TrollAttack.message("Player "
                                + tmpPlayer.getShort() + " joined the game.");
                    } else {
                        TrollAttack.message("Could not create player (" + name
                                + ":" + "***" + ").");
                    }

                }

                if (tmpPlayer == null || !tmpPlayer.checkPassword(pass)) {
                    tmpPlayer = null;
                    player.tell("Incorrect password or player not found.");
                }
            } catch (NullPointerException e) {
                if(TrollAttack.gameOver) {
	            	return null;
	            	
                } else {
                	e.printStackTrace();
                	TrollAttack.message(getID() + "Authentication failure (" + attempts++ + ").");
                	tmpPlayer = null;
                	return null;
                }
	                //e.printStackTrace();
                		
            } catch (SocketException e) {
                return null;
            } catch (IOException e) {
                //e.printStackTrace();
                TrollAttack.debug("Player quit while logging in.");
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        player = tmpPlayer;
        return player;
    }
    public void print(String string) {

        print(string, true);
    }
    public void print(String string, boolean shouldWrap) {
        if (shouldWrap) {
            string = wordwrap(string);
        }
        if(player.shouldColor) {
        	string = color(string);
        } else {
        	string = decolor(string);
        }
        try{
        	send(string);
        } catch(NullPointerException e) {
        	//TrollAttack.message("Can't print, socket no longer exists.");
        }
    }
    public abstract void send(String string);

    public void start() {
        super.start();
    }
    public int colorLessLength(String string) {
        // Each occurence of \033 means that a color is in the string, and
        // 7 non-content characters are added to the length, so remove them.
        String[] colors = string.split("[^&]&");
        return string.length() - ((colors.length - 1) * 3);
    }

    public String wordwrap(String string) {
        // Pattern that only allows strings of up to the wrapLength
        // into the first capture group.
        String pattern = "^(.{0," + wrapLength + "}[\\s])(.*)$";

        // Find all of the intended lines in the string and put them in
        // the lines variable.
        String[] lines = string.split(Util.wrapChar, -1);

        // For each line that we find, wrap it if it is too long.
        for (int i = 0; i < lines.length; i++) {
            if (colorLessLength(lines[i]) > wrapLength) {
                String tmp = lines[i];
                lines[i] = "";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(tmp);
                while (tmp.length() > 0 && m.matches()) {
                    lines[i] += m.group(1) + Util.wrapChar;
                    //TrollAttack.debug(tmp);
                    tmp = m.group(2);

                    m = p.matcher(tmp);

                }
                // Handles the last word of lines.
                if (!m.matches() && tmp.length() > 0) {
                    lines[i] = lines[i].substring(0, lines[i].length() - (Util.wrapChar.length() + 1));
                    lines[i] += " " + tmp;
                }
                if (lines[i].length() < 2) {
                    TrollAttack
                            .error("Attempting to parse a line that has fewer than two characters, this is bad.");
                    TrollAttack.error("The line is: '" + lines[i] + "'");
                }

            }
        }
        String result = "";
        for (int i = 0; i < lines.length - 1; i++) {
            result += lines[i] + Util.wrapChar;
        }
        if(lines.length > 0) {
            result += lines[lines.length - 1];
        }
        return result;

    }

    public abstract String getLine() throws NullPointerException;

    public static Vector<Channel> getChannels() {
        Vector<Channel> list = new Vector<Channel>();
        for(Channel c : Channel.values()) {
            list.add(c);
        }
        return list;
    }
    
    public void shutdown() {
    	
    }

    public static final String GREY = "&A";
    public static final String RED = "&R";
    public static final String GREEN = "&G";
    public static final String YELLOW = "&Y";
    public static final String BLUE = "&B";
    public static final String PURPLE = "&P";
    public static final String CYAN = "&C";
    public static final String WHITE = "&W";
    public static final String DARKGREY = "&DA";
    public static final String DARKRED = "&DR";
    public static final String DARKGREEN = "&DG";
    public static final String DARKYELLOW = "&DY";
    public static final String DARKBLUE = "&DB";
    public static final String DARKPURPLE = "&DP";
    public static final String DARKCYAN = "&DC";
    public static final String DARKWHITE = "&DW";

	public String decolor(String s) {
	    s = s.replaceAll("([^&]|^)&C", "$1" + "");
	    s = s.replaceAll("([^&]|^)&P", "$1" + "");
	    s = s.replaceAll("([^&]|^)&W", "$1" + "");
	    s = s.replaceAll("([^&]|^)&Y", "$1" + "");
	    s = s.replaceAll("([^&]|^)&G", "$1" + "");
	    s = s.replaceAll("([^&]|^)&R", "$1" + "");
	    s = s.replaceAll("([^&]|^)&B", "$1" + "");
	    s = s.replaceAll("([^&]|^)&A", "$1" + "");
	
	    // Darks
	    s = s.replaceAll("([^&]|^)&DC", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DP", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DW", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DY", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DG", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DR", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DB", "$1" + "");
	    s = s.replaceAll("([^&]|^)&DA", "$1" + "");
	    
        s = s.replaceAll("&&", "&");
        return s;
	}
}
