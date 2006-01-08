package TrollAttack;
/*
 * Created on Jul 7, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */

import TrollAttack.Items.Item;
import java.util.LinkedList;

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Exit {
    private Room home;

    private int destination;

    private int direction;

    private boolean isDoor, isLockable;

    private boolean open, locked;

    private Item key;

    public static String[] directionList = { "", "east", "west", "north",
            "south", "up", "down", "northeast", "southwest", "northwest",
            "southeast" };

    public Exit(int dest, int direction) {
        this(dest, direction, false, false, null);
    }

    public Exit(int dest, int direct, boolean isDoor, boolean isLocked,
            Item doorKey) {
        destination = dest;
        direction = direct;
        this.isDoor = isDoor;
        locked = isLocked;
        key = doorKey;

    }

    public static int EAST = 1, WEST = 2, NORTH = 3, SOUTH = 4, UP = 5,
            DOWN = 6, NORTHEAST = 7, NORTHWEST = 9, SOUTHWEST = 8,
            SOUTHEAST = 10;

    public static LinkedList sortExitList(LinkedList exitList) {
        return exitList;
    }

    /*
     * directionList[EAST] = "east"; directionList[WEST] = "west";
     * directionList[NORTH] = "north"; directionList[SOUTH] = "south";
     * directionList[UP] = "up"; directionList[DOWN] = "down";
     * directionList[NORTHEAST] = "northeast"; directionList[NORTHWEST] =
     * "northwest"; directionList[SOUTHEAST] = "southeast";
     * directionList[SOUTHWEST] = "southwest"; I WISH!
     */

    public boolean isOpen() {
        if (isDoor) {
            return open;
        } else {
            return true;
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLockable(Item doorKey) {
        if (doorKey == null) {
            isLockable = false;
            key = null;
        } else {
            isLockable = true;
            key = doorKey;
        }

    }

    public Exit getOtherExit() {
        return getDestinationRoom().getExit(
                Exit.directionOpposite(direction));
    }

    public Room getDestinationRoom() {
        return TrollAttack.getRoom(getDestination());
    }

    public void setDoor(boolean door) {
        isDoor = door;
    }

    public String open() {
        return open(true);
    }

    public String close() {
        return close(true);
    }

    public String open(boolean reciprocol) {
        if (isDoor) {
            if (open) {
                return "The door is already open!";
            } else {
                if (locked) {
                    return "The door is locked.";
                } else {
                    if (reciprocol) {
                        getOtherExit().open(false);

                    }
                    open = true;
                    return "You open the door";
                }
            }

        } else {
            return "This isn't a door!";
        }
    }

    public String close(boolean reciprocol) {
        if (isDoor) {
            if (!open) {
                return "The door is already closed!";
            } else {
                open = false;
                if (reciprocol) {
                    getOtherExit().close(false);

                }
                return "You close the door";
            }

        } else {
            return "This isn't a door!";
        }
    }

    public boolean isDoor() {
        return isDoor;
    }

    public boolean isLockable() {
        return isLockable;
    }

    public void lock() {
        locked = true;
    }

    public void unlock() {
        locked = false;
    }

    public Item getKey() {
        return key;
    }

    public int getDestination() {
        return destination;
    }

    public Room getRoom() {
        return home;
    }

    public void setRoom(Room room) {
        home = room;
    }

    public int getDirection() {
        return direction;
    }

    public String getDirectionName() {
        return directionName(direction);
    }

    public static int getDirection(String name) {
        for (int i = 0; i < directionList.length; i++) {
            if (directionList[i].startsWith(name)) {
                return i;
            }
        }
        return 0;
    }

    public static String directionName(int direction) {

        return directionList[direction];
    }

    public static int directionOpposite(int direction) {
        if (direction % 2 == 0) {
            return direction - 1;
        } else {
            return direction + 1;
        }
    }

    public String toString() {
        if (direction > 9) {
            return direction + "";
        } else {
            return "0" + direction;
        }
    }
}
