package TrollAttack;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Communication extends Thread {
    static int wrapLength = 76;

    static int port = 4000;

    static int ID;

    Player player;

    //DataInputStream in;
    BufferedReader in;

    DataOutputStream out;

    ServerSocket serverSocket;

    Socket adminSocket;

    public Communication() {
        this(true, null);
    }

    public Communication(boolean newServerSocket, ServerSocket serverSock) {
        ID = (int) (Math.random() * 1000);
        if (newServerSocket) {
            port--;
            serverSocket = createNewSocket();
            TrollAttack.message("Server started and listening on port " + port
                    + ".");
        } else {
            serverSocket = serverSock;
        }
    }

    public ServerSocket createNewSocket() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(++port, 1);
        } catch (BindException e) {
            ss = createNewSocket();
        } catch (Exception e) {
            TrollAttack.error("Exception: " + e.toString());
        }
        return ss;
    }

    public int getID() {
        return ID;
    }

    public void close() {
        this.killConnections();
    }

    public void killConnections() {
        try {
            //TrollAttack.message("Player losing connection.");
            out.close();
            in.close();
            //adminSocket.shutdownInput();
            //adminSocket.shutdownOutput();
            adminSocket.close();

        } catch (Exception e) {
            // e.printStackTrace();
            TrollAttack.message(e.toString()
                    + "- I don't know how to shut down this right.");
            e.printStackTrace();
        }
    }

    public void run() {
        String inputLine = "";
        try {

            // Wait for a connection
            //TrollAttack.message("Socket created, waiting for connection.");
            adminSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            
            out = new DataOutputStream(adminSocket.getOutputStream());
            Communication newConnection = new Communication(false, serverSocket);
            newConnection.start();
            //TrollAttack.message("A new player joins the game (" +
            // newConnection.getID() + ").");

            try {
                player = new Player(this);
            } catch(Exception e) {
                e.printStackTrace();
            }

            player = authenticate(player);
            if (player == null) {
                out.close();
                adminSocket.close();
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
            player.tell(player.prompt());
            
            // Main loop of entire game for each player.
            while (player.authenticated == true) {
                // TrollAttack.message(ID + ": Accepting command...");
                try {
                    inputLine = in.readLine();
                    player.handleCommand(inputLine);
                } catch (Exception e) {
                    TrollAttack.message("Lost connection to " + ID);
                    break;
                }

            }

            out.close();
            adminSocket.close();
        } catch (Exception e) {
            TrollAttack.error("Exception in Server: " + e.toString());
            e.printStackTrace();
        }
    }

    public Player authenticate(Player player) {
        Player tmpPlayer = null;
        String name = "";
        String pass = "";
        while (tmpPlayer == null) {
            try {
                player.tell(Communication.WHITE + "What is your name (or type "
                        + Communication.CYAN + "new" + Communication.WHITE
                        + " for a new character)?");
                name = in.readLine();
                //TrollAttack.message("Read name " + name + ".");
                if (name == null) {
                    continue;
                }
                if (name.compareToIgnoreCase("new") == 0) {
                    tmpPlayer = player;
                    while (name.compareToIgnoreCase("new") == 0) {
                        player.tell("Pick a name:");
                        name = in.readLine();
                        if (DataReader.readPlayerData(name) != null) {
                            name = "new";
                            player.tell("That name is taken!");
                        }
                    }

                    player.shortDescription = name;
                    player.tell("Pick a password:");
                    pass = in.readLine();
                    player.setPassword(pass);
                    player.authenticated = true;
                } else {

                    player.tell(name + "'s password:");
                    pass = in.readLine();
                    tmpPlayer = DataReader.readPlayerData(name);
                    if (tmpPlayer != null) {
                        tmpPlayer.authenticated = true;
                        TrollAttack.message("Created player "
                                + tmpPlayer.getShort());
                    } else {
                        TrollAttack.message("Could not create player (" + name
                                + ":" + pass + ").");
                    }

                }

                if (tmpPlayer == null || !tmpPlayer.checkPassword(pass)) {
                    tmpPlayer = null;
                    player.tell("Incorrect password or player not found.");
                }
            } catch (NullPointerException e) {
                tmpPlayer = null;
                e.printStackTrace();

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

    public void print(String string, String color) {
        this.print(string, true, color);
    }

    public void print(String string) {
        this.print(string, "");
    }

    public void print(String string, boolean shouldWrap, String color) {
        int wrap = wrapLength;

        try {
            if (shouldWrap) {
                string = wordwrap(string);
            }
            out.writeBytes(color);

            out.writeBytes(string);
            out.writeBytes("\033[0m\n\r");
        } catch (IOException e) {
            player.quit();
            // TrollAttack.error("Player quit unexpectedly (" + e.getMessage() +
            // ").");
            //e.printStackTrace();

        } catch (Exception e) {
            TrollAttack.error("Output error!");
            e.printStackTrace();
        }

    }

    public int colorLessLength(String string) {
        // Each occurence of \033 means that a color is in the string, and
        // 13 non-content characters are added to the length, so remove them.
        String[] colors = string.split("\033");
        return string.length() - ((colors.length - 1) * 13);
    }

    public String wordwrap(String string) {
        // Pattern that only allows strings of up to the wrapLength
        // into the first capture group.
        String pattern = "^(.{0," + wrapLength + "}[\\s])(.*)$";

        // Find all of the intended lines in the string and put them in
        // the lines variable.
        String[] lines = string.split(Util.wrapChar);

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
                    lines[i] = lines[i].substring(0, lines[i].length() - 2);
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
        result += lines[lines.length - 1];
        return result;

    }

    // Server server = new Server();
    // server.start();
    // public
    public static String GREY = "\033[1:30:40m";

    public static String RED = "\033[1;31;40m";

    public static String GREEN = "\033[1;32;40m";

    public static String YELLOW = "\033[1;33;40m";

    public static String BLUE = "\033[1;34;40m";

    public static String PURPLE = "\033[1;35;40m";

    public static String CYAN = "\033[1;36;40m";

    public static String WHITE = "\033[1;37;40m";

    public static String DARKGREY = "\033[0:30:40m";

    public static String DARKRED = "\033[0;31;40m";

    public static String DARKGREEN = "\033[0;32;40m";

    public static String DARKYELLOW = "\033[0;33;40m";

    public static String DARKBLUE = "\033[0;34;40m";

    public static String DARKPURPLE = "\033[0;35;40m";

    public static String DARKCYAN = "\033[0;36;40m";

    public static String DARKWHITE = "\033[0;37;40m";

}
