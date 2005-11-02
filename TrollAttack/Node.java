package TrollAttack;
/*
 * Created on Mar 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Stephen Fluin (3221417), 1:25 PM Discussion
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Node {
    private Object data;

    private Node ptr;

    public Node(Object d, Node p) {
        data = d;
        ptr = p;
    }

    public boolean isEndNode() {
        if (ptr == null) {
            return true;
        } else {
            return false;
        }
    }

    public Node getPointer() {
        return ptr;
    }

    public Object getData() {
        return data;
    }

    public void setPointer(Node p) {
        ptr = p;
    }

    public void setData(Object o) {
        data = o;
    }
}
