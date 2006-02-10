/*
 * Created on Dec 11, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack;

import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer extends Communication {
    BufferedReader in;
    DataOutputStream out;
    ServerSocket serverSocket;
    Socket adminSocket;
    static int port = 4000;
    static final char ESCAPE = '\033';
    static final char X = '\377';
    static final String TELOPT_ECHO =   X + "\001"; //01
    static final String GA =            X + "\371"; //F9
    static final String SB =            X + "\372"; //FA
    static final String WILL =          X + "\373"; //FB
    static final String WONT =          X + "\374"; //FC
    static final String DO   =          X + "\375"; //FD
    static final String DONT =          X + "\376"; //FE
    static final String IAC =           X + "\377"; //FF
    
    public TelnetServer() {
        this(true, null);
        this.start();
    }

    public TelnetServer(boolean newServerSocket, ServerSocket serverSock) {
        this.ID = (int) (Math.random() * 1000);
        if (newServerSocket) {
            port--;
            serverSocket = createNewSocket();
            TrollAttack.message("Server started and listening on port " + port
                    + ".");
        } else {
            serverSocket = serverSock;
        }
    }
    public void waitForUser() {
        try {
            adminSocket = serverSocket.accept();
            TrollAttack.message("New connection attempt.");
            in = new BufferedReader(new InputStreamReader(adminSocket.getInputStream()));
            
            out = new DataOutputStream(adminSocket.getOutputStream());
            
            Communication newConnection = new TelnetServer(false, serverSocket);
            newConnection.start();
            TrollAttack.debug("New telnet listener started.");
            TrollAttack.unusedCommunication = newConnection;
        } catch(Exception e) {
            e.printStackTrace();
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
    public String getLine() throws NullPointerException {
        String line;
    	try {
             line = in.readLine();
        } catch(Exception e) {
            throw new NullPointerException("Problem with getLine.");
        }
//      remove backspaces from inputLine
        if(line.contains("\010")) {
            //TrollAttack.debug("Problem.");
        }
        while(line.contains("\010")) {
            //TrollAttack.debug(line);
            int location = line.indexOf("\010");
            if(location == 0) {
                line = line.substring(1);
            } else {
                line = line.substring(0,location-1) + line.substring(location+1);
            }
        }
        return line;
    }
    public void send(String string) {
        try {
            out.writeBytes(string);
        } catch (IOException e) {
            player.quit(true);
            // TrollAttack.error("Player quit unexpectedly (" + e.getMessage() +
            // ").");
            //e.printStackTrace();

        } catch (Exception e) {
            TrollAttack.error("Output error!");
            e.printStackTrace();
        }

    }
    public void close() {
        try {
            //TrollAttack.message("Player losing connection.");
            if(in != null) {
                in.close();
            }
            if(out != null) {
                out.close();
            }
            if(adminSocket != null) {
                adminSocket.close();
            }
        } catch(Exception e) {
            
        }
    }
    
    public static String GREY = ESCAPE + "[1;30m";
    public static String RED = ESCAPE + "[1;31m";
    public static String GREEN = ESCAPE + "[1;32m";
    public static String YELLOW = ESCAPE + "[1;33m";
    public static String BLUE = ESCAPE + "[1;34m";
    public static String PURPLE = ESCAPE + "[1;35m";
    public static String CYAN = ESCAPE + "[1;36m";
    public static String WHITE = ESCAPE + "[1;37m";
    public static String DARKGREY = ESCAPE + "[0:30m";
    public static String DARKRED = ESCAPE + "[0;31m";
    public static String DARKGREEN = ESCAPE + "[0;32m";
    public static String DARKYELLOW = ESCAPE + "[0;33m";
    public static String DARKBLUE = ESCAPE + "[0;34m";
    public static String DARKPURPLE = ESCAPE + "[0;35m";
    public static String DARKCYAN = ESCAPE + "[0;36m";
    public static String DARKWHITE = ESCAPE + "[0;37m";
    public String color(String s) {
    	//TrollAttack.debug(s);
        // Lights
        s = s.replaceAll("([^&]|^)&C", "$1" + CYAN);
        s = s.replaceAll("([^&]|^)&P", "$1" + PURPLE);
        s = s.replaceAll("([^&]|^)&W", "$1" + WHITE);
        s = s.replaceAll("([^&]|^)&Y", "$1" + YELLOW);
        s = s.replaceAll("([^&]|^)&G", "$1" + GREEN);
        s = s.replaceAll("([^&]|^)&R", "$1" + RED);
        s = s.replaceAll("([^&]|^)&B", "$1" + BLUE);
        s = s.replaceAll("([^&]|^)&A", "$1" + GREY);

        // Darks
        s = s.replaceAll("([^&]|^)&DC", "$1" + DARKCYAN);
        s = s.replaceAll("([^&]|^)&DP", "$1" + DARKPURPLE);
        s = s.replaceAll("([^&]|^)&DW", "$1" + DARKWHITE);
        s = s.replaceAll("([^&]|^)&DY", "$1" + DARKYELLOW);
        s = s.replaceAll("([^&]|^)&DG", "$1" + DARKGREEN);
        s = s.replaceAll("([^&]|^)&DR", "$1" + DARKRED);
        s = s.replaceAll("([^&]|^)&DB", "$1" + DARKBLUE);
        s = s.replaceAll("([^&]|^)&DA", "$1" + DARKGREY);
        
        s = s.replaceAll("&&(..)", "&$1");
        return s;
    }
}
