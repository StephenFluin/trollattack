package TrollAttack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Communication extends Thread {
    static int wrapLength = 78;
    static int port = 4000;
    static int ID;
    Player player;
    DataInputStream in;
    DataOutputStream out;
    ServerSocket serverSocket;
    Socket adminSocket;
    
    public Communication() { this(true, null); }
    public Communication( boolean newServerSocket, ServerSocket serverSock ) {
        ID = (int)(Math.random() * 1000);
        if(newServerSocket) {
                port--;
                serverSocket = createNewSocket();
                TrollAttack.message("Server started and listening on port " + port + ".");
        } else {
            serverSocket = serverSock;
        }
    }
    public ServerSocket createNewSocket() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(++port, 1);
        } catch(BindException e) {
            ss = createNewSocket();
        } catch(Exception e) {
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
            
        } catch(Exception e) {
           // e.printStackTrace();
            TrollAttack.message(e.toString() + "- I don't know how to shut down this right.");
            e.printStackTrace();
        }
    }
     public void run() {
       /* String input;
        while(!TrollAttack.gameOver) {
            input = stdin.stringInputLine();
            TrollAttack.ch.handleCommand(input);
        }
        TrollAttack.endGame();
        */
       // adminSocket = null;
       // in = null;
       // out = null;
        String inputLine = "";
        try {
            
                 // Wait for a connection
                //TrollAttack.message("Socket created, waiting for connection.");
                 adminSocket = serverSocket.accept();
                 in = new DataInputStream(adminSocket.getInputStream());
                 out = new DataOutputStream(adminSocket.getOutputStream());
                 Communication newConnection = new Communication(false, serverSocket);
                 newConnection.start();
                //TrollAttack.message("A new player joins the game (" + newConnection.getID() + ").");
                 
                 player = new Player( this );
                
                 player = authenticate(player);
                 
                 player.setLastActive(TrollAttack.getTime());
                 player.setCommunication(this);
                 TrollAttack.broadcast(PURPLE + player.getShort() + " has joined our ranks.");
                 TrollAttack.addPlayer(player);
                 TrollAttack.getRoom(player.getCurrentRoom()).addPlayer(player);
                 TrollAttack.getRoom(player.getCurrentRoom()).say(WHITE + player.getShort() + " enters the room.", player);
                 
                 player.look();
                 player.tell( player.prompt() );
                while (player.authenticated == true) {
                  // TrollAttack.message(ID + ": Accepting command...");
                    try{
                        inputLine = in.readLine();
                        player.handleCommand(inputLine);
                    } catch(Exception e) {
                        //TrollAttack.error("Lost connection to " + ID);
                        break;
                    }
                  
                }
                
            out.close();
            adminSocket.close();
        } catch(Exception e) {
                TrollAttack.error("Exception in Server: "+e.toString());
                e.printStackTrace();
        }
    }
    public Player authenticate(Player player) {
        Player tmpPlayer = null;
        String name = "";
        String pass = "";
        while(tmpPlayer == null) {
            try {
                player.tell(Communication.WHITE + "What is your name (or type " + Communication.CYAN + "new" + Communication.WHITE + " for a new character)?");
                name = in.readLine();
                if(name.compareToIgnoreCase("new") == 0) {
                    tmpPlayer = player;
                    player.tell("Pick a name:");
                    name = in.readLine();
                    player.shortDescription = name;
                    player.tell("Pick a password:");
                    pass = in.readLine();
                    player.setPassword(pass);
                } else {
                
	                player.tell(name + "'s password:");
	                pass = in.readLine();
	                tmpPlayer = DataReader.readPlayerData(name);
	                tmpPlayer.authenticated = true;
	                TrollAttack.message("Created player " + tmpPlayer.getShort());
                }
                
                if(!tmpPlayer.checkPassword(pass)) {
                    tmpPlayer = null;
                    player.tell("Incorrect password.");
                }
            } catch(NullPointerException e) {
                tmpPlayer = null;
                e.printStackTrace();
               
            } catch(IOException e) {
                e.printStackTrace();
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
            if(shouldWrap) {
	            string = wordwrap(string);
            }
            out.writeBytes(color);
            
            out.writeBytes(string);
            out.writeBytes("\033[0m\n\r");
		} catch( IOException e) {
		    player.quit();
		   // TrollAttack.error("Player quit unexpectedly (" + e.getMessage() + ").");
		    //e.printStackTrace();
		    
		} catch( Exception e ) {
		    TrollAttack.error("Output error!");
		    e.printStackTrace();
		}

    }
    public int colorLessLength(String string) {
        String[] colors = string.split("\033");
        return string.length() - ( (colors.length - 1) * 13);
    }
    public String wordwrap(String string) {
        String pattern = "^(.{0," + wrapLength + "}[\\s])(.*)$";
        String[] lines = string.split("\n\r");
        for(int i = 0;i < lines.length; i++ ) {
            if(colorLessLength(lines[i]) > wrapLength) {
                String tmp = lines[i];
                lines[i] = "";
                Pattern p = Pattern.compile(pattern);
                Matcher  m = p.matcher(tmp);
                while(tmp.length() > 0 && m.matches()) {
                    lines[i] += m.group(1) + "\n\r";
                    tmp = m.group(2);
                    m = p.matcher(tmp);
                }
                lines[i] = lines[i].substring(0, lines[i].length() - 2);
                lines[i] += tmp;
            }
        }
        String result = "";
        for(int i = 0;i < lines.length;i++) {
            result += lines[i];
        }
        return result;
        
       
        
    }
   // Server server = new Server();
   // server.start();
   // public
    public static String GREY = 	"\033[1:30:40m";
    public static String RED = 	"\033[1;31;40m";
    public static String GREEN = 	"\033[1;32;40m";
    public static String YELLOW = 	"\033[1;33;40m";
    public static String BLUE = 	"\033[1;34;40m";
    public static String PURPLE = 	"\033[1;35;40m";
    public static String CYAN = 	"\033[1;36;40m";
    public static String WHITE = 	"\033[1;37;40m";
    public static String DARKGREY = 	"\033[0:30:40m";
    public static String DARKRED = 	"\033[0;31;40m";
    public static String DARKGREEN = 	"\033[0;32;40m";
    public static String DARKYELLOW = 	"\033[0;33;40m";
    public static String DARKBLUE = 	"\033[0;34;40m";
    public static String DARKPURPLE = 	"\033[0;35;40m";
    public static String DARKCYAN = 	"\033[0;36;40m";
    public static String DARKWHITE = 	"\033[0;37;40m";
    
   
    
    
}
