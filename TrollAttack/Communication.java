package TrollAttack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
    static int wrapLength = 400;
    static int port = 4000;
    static int ID;
    DataInputStream in;
    DataOutputStream out;
    ServerSocket serverSocket;
    Socket adminSocket;
    EasyReader stdin = new EasyReader(System.in);
    
    public Communication() { this(true, null); }
    public Communication( boolean newServerSocket, ServerSocket serverSock ) {
        ID = (int)(Math.random() * 1000);
        if(newServerSocket) {
            try {
                serverSocket = new ServerSocket(port, 1);
                TrollAttack.message("Server started and listening on port " + port + ".");
            } catch(Exception e) {
                TrollAttack.error("Exception: " + e.toString());
            }
        } else {
            serverSocket = serverSock;
        }
    }
    public int getID() {
        return ID;
    }
    public void close() {
        this.killConnections();
    }
    public void killConnections() {
        try { 
            TrollAttack.message("Player losing connection.");
            out.close();
            in.close();
            adminSocket.shutdownInput();
            adminSocket.shutdownOutput();
            adminSocket.close();
            
        } catch(Exception e) {
           // e.printStackTrace();
            TrollAttack.message(e.toString() + "- I don't know how to shut down this right.");
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
                 TrollAttack.message("A new player joins the game (" + newConnection.getID() + ").");
                 
                 Player player = new Player( this );
                 TrollAttack.broadcast(PURPLE + "A new player has joined our ranks.");
                 TrollAttack.addPlayer(player);
                 TrollAttack.gameRooms[player.getCurrentRoom()].addPlayer(player);
                 TrollAttack.gameRooms[player.getCurrentRoom()].say(WHITE + "A new player enters the room.", player);
                 
                 player.look();
                 player.tell( player.prompt() );
                while (!TrollAttack.gameOver) {
                   // TrollAttack.error(ID + ": Accepting command...");
                    try{
                        inputLine = in.readLine();
                        player.handleCommand(inputLine);
                    } catch(Exception e) {
                        TrollAttack.print("Lost connection to " + ID);
                        break;
                    }
                  
                }
                
            out.close();
            adminSocket.close();
        } catch(Exception e) {
                System.out.println("Exception in Server: "+e.toString());
                e.printStackTrace();
        }
    }
    public void print(String string, String color) {
        this.print(string, false, color);
    }
    public void print(String string) {
        this.print(string, "");
    }
    public void print(String string, boolean shouldWrap, String color) {
        int wrap = wrapLength;	
		if(Util.contains(string, "Exits") && false) {
		   // print("Contents of current room", true);
		    //print(TrollAttack.gameRooms[TrollAttack.player.currentRoom].toString(), true);;
		}
        while(string.length() > 0 ) {
			if(wrapLength > string.length() || !shouldWrap ) {
					wrap = string.length();
			}
			try {
			    //
			    out.writeBytes(color + string.substring(0,wrap) + "\033[0m\n\r");
			} catch( IOException e) {
			    //TrollAttack.error("IO exception" + e.getMessage());
			} catch( Exception e ) {
			    TrollAttack.error("Output error!");
			}
			string = string.substring(wrap);
		}

    }
   // Server server = new Server();
   // server.start();
   // public
    static String GREY = 	"\033[1:30:40m";
    static String RED = 	"\033[1;31;40m";
    static String GREEN = 	"\033[1;32;40m";
    static String YELLOW = 	"\033[1;33;40m";
    static String BLUE = 	"\033[1;34;40m";
    static String PURPLE = 	"\033[1;35;40m";
    static String CYAN = 	"\033[1;36;40m";
    static String WHITE = 	"\033[1;37;40m";
    static String DARKGREY = 	"\033[0:30:40m";
    static String DARKRED = 	"\033[0;31;40m";
    static String DARKGREEN = 	"\033[0;32;40m";
    static String DARKYELLOW = 	"\033[0;33;40m";
    static String DARKBLUE = 	"\033[0;34;40m";
    static String DARKPURPLE = 	"\033[0;35;40m";
    static String DARKCYAN = 	"\033[0;36;40m";
    static String DARKWHITE = 	"\033[0;37;40m";
    
   
    
    
}
