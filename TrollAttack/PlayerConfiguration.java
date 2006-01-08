/*
 * Created on Nov 16, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack;

import java.util.LinkedList;
import TrollAttack.Communication.*;

public class PlayerConfiguration {
    private LinkedList<Communication.Channel> channels;
    
    public PlayerConfiguration() {
        channels = new LinkedList<Communication.Channel>();
    }
    public void addChannel(String channelName) {
        LinkedList<Channel> chans = Communication.getChannels();
        Util.findIn(channelName, chans);
        
    }
    public void addChannel(Communication.Channel channel) {
        channels.add(channel);
    }
    public void removeChannel(Communication.Channel channel) {
        channels.remove(channel);
    }
    
    
}
