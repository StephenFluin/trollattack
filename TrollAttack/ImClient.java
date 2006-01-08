/*
 * Created on Dec 11, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack;

import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.jar.Attributes;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.packet.RosterPacket.Item;

public class ImClient extends Communication {
    XMPPConnection con;
    PacketCollector myCollector;
    String clientName;
    Chat connectionChat;
    public static Hashtable<String, Chat> clientList = new Hashtable<String, Chat>();
    LinkedBlockingQueue<String> mq;
    public ImClient() {
        try{
            con = new XMPPConnection("im.nullirc.net");
            con.login("trollattack", "taserver");
           // myCollector = con.createPacketCollector(new PacketTypeFilter(Message.class));
            //con.
            //$JABBER->TransportRegistration( "aim-trans.im.nullirc.net", array( "username" => "loopyai", "password" => "xxxxxxxxxxxx" ) );
            con.addPacketListener(new PListener(), 
                    new OrFilter(
                            new PacketTypeFilter(Message.class),
                            new PacketTypeFilter(Packet.class)
                            ));
            sendAIMGatewayPackets(con);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void sendAIMGatewayPackets(XMPPConnection conName) {
        Registration gateway = new Registration();
        gateway.setType(IQ.Type.SET);
        gateway.setTo("aim-trans.im.nullirc.net");
        gateway.setFrom(con.getUser());
        
        Hashtable<String, String> attributes = new Hashtable<String,String>();
        attributes.put("username", "trollattackserve");
        attributes.put("password", "harlan1");
        gateway.setAttributes(attributes);
        conName.sendPacket(gateway);
        TrollAttack.debug(gateway.toXML());
        /*<iq type='set'
            from='romeo@montague.net/orchard'
            to='aim.shakespeare.lit'
            id='reg2'>
          <query xmlns='jabber:iq:register'>
            <username>RomeoMyRomeo</username>
            <password>ILoveJuliet</password>
          </query>
        </iq>*/
        
        RosterPacket roster = new RosterPacket();
        roster.addRosterItem(new Item("aim-trans.im.nullirc.net", "AIM Gateway"));
        roster.setFrom(conName.getUser());
        roster.setType(IQ.Type.SET);
        TrollAttack.debug(roster.toXML());
        conName.sendPacket(roster);
        
        //Presence p = new Prese
        
    }
    public ImClient(XMPPConnection con, Chat chat) {
        TrollAttack.message("Communicating with potential player.");
        this.con = con;
        connectionChat = chat;
        mq = new LinkedBlockingQueue<String>();
        clientName = chat.getParticipant();
        clientList.put(clientName, chat);
        
        chat.addMessageListener(new PacketListener() {
            public void processPacket(Packet packet) {
                Message msg = (Message)packet;
                mq.add(msg.getBody());
            }
        });
    }
    public String getLine()  throws NullPointerException  {

        String message = null;
        try {
           message = mq.poll(TrollAttack.maxIdleTime + 120, TimeUnit.SECONDS);
        } catch(InterruptedException e) {
            send("You have been idle too long.");
            close();
            TrollAttack.message("Kicking idle connection to " + clientName);
        }
        
       
        return message;
    }
    public void send(String string) {
        Message newMessage = new Message();
        newMessage.setBody(string);
        try {
            connectionChat.sendMessage(newMessage);
        } catch(Exception e) {
            try{Thread.sleep(3000);}catch(Exception eg){eg.printStackTrace();}
            e.printStackTrace();
        }
    }
    public void close() {
        
        send("Connection closed.");
        clientList.remove(clientName);
    }
    public void waitForUser() {
        
    }
    public String color(String s) {
        s = s.replaceAll(Util.wrapChar, "\n");
        // Lights
        s = s.replaceAll("([^\\]|^)&C", "$1");
        s = s.replaceAll("([^\\]|^)&P", "$1");
        s = s.replaceAll("([^\\]|^)&W", "$1");
        s = s.replaceAll("([^\\]|^)&Y", "$1");
        s = s.replaceAll("([^\\]|^)&G", "$1");
        s = s.replaceAll("([^\\]|^)&R", "$1");
        s = s.replaceAll("([^\\]|^)&B", "$1");
        s = s.replaceAll("([^\\]|^)&A", "$1");

        // Darks
        s = s.replaceAll("([^\\]|^)&DC", "$1");
        s = s.replaceAll("([^\\]|^)&DP", "$1");
        s = s.replaceAll("([^\\]|^)&DW", "$1");
        s = s.replaceAll("([^\\]|^)&DY", "$1");
        s = s.replaceAll("([^\\]|^)&DG", "$1");
        s = s.replaceAll("([^\\]|^)&DR", "$1");
        s = s.replaceAll("([^\\]|^)&DB", "$1");
        s = s.replaceAll("([^\\]|^)&DA", "$1");
        s = s.replaceAll("\\&", "&");
        s = "<span style=\"font-family: courier-new;\">" + s + "</span>";
        return s;
    }
    public class PListener implements PacketListener {
        public void processPacket(Packet packet) {
            if(!( packet instanceof Message ) ) {
                TrollAttack.debug(packet.toXML());
                return;
            }
            Message msg = (Message)packet; 
            /*System.out.print("New Message From: "+
                    msg.getFrom()+": "+
                    msg.getBody()+"\n");*/
            boolean contains = false;
            for(String s : clientList.keySet()) {
                if(s.compareToIgnoreCase(msg.getFrom()) == 0) {
                    contains = true;
                    break;
                }
            }
            if(contains) {
                // Do nothing because this wil be handled by by a chat listener.
            } else {
                TrollAttack.debug("Creating new chat because name wasn't found in clientList.");
                TrollAttack.debug(clientList.toString());
                Chat newChat = con.createChat(msg.getFrom());
                
                ImClient newConnection = new ImClient(con, newChat);
                newConnection.start();
            }
        }
    }
}
