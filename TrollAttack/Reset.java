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
    public Area area;
    

    public Reset() {
       
    }
    public void execute() {
        
    }
    public void run() {
        //TrollAttack.message("Running reset " + this.toString() + " (" + clicksRemaining + " clicks left).");
        if(!isFrozen()) {
	        if(clicksRemaining-- <= 0 ) {
	            execute();
	            clicksRemaining = clicksMax;
	        } else {
	            //TrollAttack.message(this.toString() + " has " + clicksRemaining + "left.");
	        }
        } else {
            //TrollAttack.message(this.toString() + " is frozen...");
        }
    }
    public boolean isFrozen() {
        return area.frozen;
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
           area = Area.testRoom(room);
        }
        public void execute() {
            //TrollAttack.message("executing an item reset.");
            if(room.countExactItem(item) < limit) {
                //TrollAttack.message("Adding " + item.getShort() + " to " + room.title);
                room.addItem(item);
            } else {
                //TrollAttack.message("Item can't be added because there are (" + room.countExactItem(item) + "/" + limit + ").");
            }
        }
        public String toString() {
            return "Put (Item x " + limit + ") '" + item.getShort() + "' in (" + room.vnum + ") '" + room.title + "'.";
        }
    }
    public static class MobileReset extends Reset {
        Mobile mob;
        Room room;
        public MobileReset(Mobile mobile, Room nRoom, int clicks) {
            mob = mobile;
            room = nRoom;
            setClicks(clicks);
            area = Area.testMobile(mobile);
            //TrollAttack.message("new Mobile reset is " + area.name + " and is " + area.frozen);
        }
        public void execute() {
            if(room.countExactMobile(mob) < limit) {
                //TrollAttack.message("Adding " + mob.getShort() + " to " + room.title);
                room.addMobile(mob);
                mob.healAll();
            }
            //TrollAttack.message("Done mobile Reset.");
        }
        public String toString() {
            return "Put (Mobile x " + limit + ") '" + mob.getShort() + "' in (" + room.vnum + ") '" + room.title + "'.";
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
            area = Area.testRoom(ex.getRoom());
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
        public String toString() {
            return "Make door " + (open ? "open" : "closed") + ".";
        }
    }
}
