package TrollAttack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Created on Jun 12, 2005
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
public class Server extends Thread {
    int port = 4000;
    ServerSocket serverSocket = null;
    Socket adminSocket = null;

    DataInputStream in = null;
    DataOutputStream out = null;

    String inputLine = "";
    Server() {}

    public void run() {
        try {
            serverSocket = new ServerSocket(port, 1);
            while (true) {
                 // Wait for a connection
                 adminSocket = serverSocket.accept();

                 in = new DataInputStream(adminSocket.getInputStream());
                 out = new DataOutputStream(adminSocket.getOutputStream());
                 System.out.println("Connection started.");	
                while (true) {
                    inputLine = in.readLine();
                    if(inputLine != null) {
                       // out.writeBytes("I hear you saying '" + inputLine + "'");
                           // out.writeBytes("PW springMUD " + pwd +
                    }
                }

             }
        } catch(Exception e) {
                System.out.println("Exception in Server: "+e.toString());
        }
    }
}
