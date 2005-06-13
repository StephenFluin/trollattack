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
    DataInputStream in;
    DataOutputStream out;
    
     EasyReader stdin = new EasyReader(System.in);
    public void killConnections() {
        try { 
            out.close();
        
        } catch(Exception e) {
            
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
        int port = 4000;
        ServerSocket serverSocket = null;
        Socket adminSocket = null;
        in = null;
        out = null;
        String inputLine = "";
        try {
            serverSocket = new ServerSocket(port, 1);
            while (!TrollAttack.gameOver) {
                 // Wait for a connection
                System.out.println("Socket created, waiting for connection.");
                 adminSocket = serverSocket.accept();

                 in = new DataInputStream(adminSocket.getInputStream());
                 out = new DataOutputStream(adminSocket.getOutputStream());
                 System.out.println("Connection started.");
                 Player player = new Player(this);
                 player.look();
                 TrollAttack.print( player.prompt() );
                while (!TrollAttack.gameOver) {
                    inputLine = in.readLine();
                    player.handleCommand(inputLine);
                }
                
             }
            out.close();
            adminSocket.close();
        } catch(Exception e) {
                System.out.println("Exception in Server: "+e.toString());
        }
    }
    public void print(String string) {
        this.print(string, true);
    }
    public void print(String string, boolean shouldWrap) {
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
			    out.writeBytes(string.substring(0,wrap) + "\n");
			} catch( IOException e) {
			    TrollAttack.error("IO exception...");
			} catch( Exception e ) {
			    TrollAttack.error("Output error!");
			}
			string = string.substring(wrap);
		}

    }
   // Server server = new Server();
   // server.start();
   // public
}
