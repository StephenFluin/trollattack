/*
 * Created on Jul 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;

import TrollAttack.Items.Item;

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class Reset {
    int limit = 1;
    int clicksMax = 0;
    int clicksRemaining = 0;
    public Reset() {
       
    }
    public void execute() {
        
    }
    public void run() {
        //TrollAttack.message("Running reset (" + clicksRemaining + " clicks left).");
        if(clicksRemaining-- <= 0) {
            execute();
            clicksRemaining = clicksMax;
        }
    }
    public void setClicks(int amount) {
        clicksMax = amount;
        clicksRemaining = 0;
    }
    public static class ItemReset extends Reset {
        Item item;
        Room room;
        public ItemReset(Item nItem, Room nRoom, int clicks) {
           item = nItem;
           room = nRoom;
           setClicks(clicks);
        }
        public void execute() {
            //TrollAttack.message("Adding " + item.getShort() + " to " + room.title);
            if(room.countExactItem(item) < limit) {
                room.addItem(item);
            }
        }
    }
    public static class MobileReset extends Reset {
        Mobile mob;
        Room room;
        public MobileReset(Mobile mobile, Room nRoom, int clicks) {
            mob = mobile;
            room = nRoom;
            setClicks(clicks);
        }
        public void execute() {
            //TrollAttack.message("Adding " + mob.getShort() + " to " + room.title);
            if(room.countExactMobile(mob) < limit) {
                room.addMobile(mob);
                mob.healAll();
            }
            //TrollAttack.message("Done mobile Reset.");
        }
    }
    public static class ExitReset extends Reset {
        Exit exit;
        boolean open;
        boolean locked;
        public ExitReset(Exit ex, boolean shouldBeOpen, boolean shouldBeLocked, int clicks){
            exit = ex;
            open = shouldBeOpen;
            locked = shouldBeLocked;
            setClicks(clicks);
        }
        public void execute() {
            //TrollAttack.message("fixing an exit.");
            if(open) {
                exit.open();
            } else {
                exit.close();
            }
            if(locked) {
                exit.lock();
            } else {
                exit.unlock();
            }
            
        }
    }
}
