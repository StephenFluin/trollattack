package TrollAttack;
/*
 * Created on Jul 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import TrollAttack.Items.Container;
import TrollAttack.Items.Equipment;
import TrollAttack.Items.Item;

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
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
        //TrollAttack.message("Running reset " + this.toString() + " (" +
        // clicksRemaining + " clicks left).");
        if (!isFrozen()) {
            if (clicksRemaining-- <= 0) {
                execute();
                clicksRemaining = clicksMax;
            } else {
                //TrollAttack.debug(this.toString() + " has " +
                // clicksRemaining + " left.");
            }
        } else {
            //TrollAttack.debug(this.toString() + " is frozen...");
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
            area = Area.testRoom(room, TrollAttack.gameAreas);
        }

        public void execute() {
            //TrollAttack.debug("executing an item reset.");
            if (room.countExactItem(item) < limit) {
                //TrollAttack.debug("Adding " + item.getShort() + " to " +
                // room.title);
                room.addItem(item);
            } else {
                //TrollAttack.debug(toString() + " can't be added because there are ("
                // + room.countExactItem(item) + "/" + limit + ").");
            }
        }

        public String toString() {
            return "Put (Item x " + limit + ") '" + item.getShort() + "' in ("
                    + room.vnum + ") '" + room.title + "'.";
        }
    }

    public static class MobileReset extends Reset {
        Mobile mob;

        Room room;

        public MobileReset(Mobile mobile, Room nRoom, int clicks) {
            mob = mobile;
            room = nRoom;
            setClicks(clicks);
            area = Area.testMobile(mobile, TrollAttack.gameAreas);
            //TrollAttack.message("new Mobile reset is " + area.name + " and is
            // " + area.frozen);
        }

        public void execute() {
            if (area.countBeing(mob) < limit) {
                room.addBeing(mob);
                mob.setCurrentRoom(room.vnum);
                mob.healAll();
            }
            //TrollAttack.message("Done mobile Reset.");
        }

        public String toString() {
            return "Put (Mobile x " + limit + ") '" + mob.getShort() + "' in ("
                    + room.vnum + ") '" + room.title + "'.";
        }
    }

    public static class GiveReset extends Reset {
        Item item;

        Mobile mobile;

        public GiveReset(Item ite, Mobile mob, int clicks) {
            item = ite;
            mobile = mob;
            setClicks(clicks);
            area = Area.testMobile(mobile, TrollAttack.gameAreas);
        }

        public void execute() {
            if (mobile.countExactItem(item) < limit) {
                mobile.addItem(item);
            }
        }

        public String toString() {
            return "Give (Item x " + limit + ") '" + item.getShort() + "' to ("
                    + mobile.vnum + ") '" + mobile.getShort() + "'.";
        }
    }
    
    public static class ContainsReset extends Reset {
    	Item item;
    	Container con;
    	public ContainsReset(Item ite, Container co, int clicks) {
    		item = ite;
    		con = co;
    		setClicks(clicks);
    		area = Area.testItem(con, TrollAttack.gameAreas);
    	}
    	public void execute() {
    		if(con.countExactItem(item) < limit) {
    			con.add(item);
    		}
    	}
    	public String toString() {
    		return "Contained (Item x " + limit + ") '" + item.getShort() + "' in (" +
    			con.vnum + ") '" + con.getShort() + "'.";
    	}
    }

    public static class WearReset extends Reset {
        Equipment item;

        Mobile mobile;

        public WearReset(Equipment ite, Mobile mob, int clicks) {
            item = ite;
            mobile = mob;
            setClicks(clicks);
            area = Area.testMobile(mobile, TrollAttack.gameAreas);
        }

        public void execute() {
            if (mobile.countExactEquipment(item) < limit) {
                mobile.wearItem(item, false);
            } else {
            }
        }

        public String toString() {
            return "Wear (Item x " + limit + ") '" + item.getShort() + "' on ("
                    + mobile.vnum + ") '" + mobile.getShort() + "'.";
        }
    }

    public static class ExitReset extends Reset {
        Exit exit;

        boolean open;

        boolean locked;

        public ExitReset(Exit ex, boolean shouldBeOpen, boolean shouldBeLocked,
                int clicks) {
            exit = ex;
            open = shouldBeOpen;
            locked = shouldBeLocked;
            setClicks(clicks);
            area = Area.testRoom(ex.getRoom(), TrollAttack.gameAreas);
        }

        public void execute() {
            //TrollAttack.message("fixing an exit.");
            if (open) {
                exit.open();
            } else {
                exit.close();
            }
            if (locked) {
                exit.lock();
            } else {
                exit.unlock();
            }
        }

        public String toString() {
            return "Make door to " + exit.getDestinationRoom().title + " " + (open ? "open" : "closed") + ".";
        }
    }
}
