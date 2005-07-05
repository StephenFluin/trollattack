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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LinkedList {
	private Node head;
	private Node tail;
	private boolean allowDuplicates = true;
	private int length = 0;
	private int internalPointer = 0;
	private Node internalPointerNode;
	/**
	 * 	Sort type of 0 indicates no sorting
	 *  Sort type of 1 indicates ascending sort.
	 *  Sort type of -1 indicates descending sort.
	 */
	private int sortType = 0;
	public LinkedList() {
	    this(true, 0);
	}
	public LinkedList(boolean dupes, int sorty) {
		allowDuplicates = dupes;
		sortType = sorty;
		tail = new Node(null, null);
		head = new Node(null, tail);
		internalPointerNode = head;
	}
	
	public String toString() {
		Node current = head;
		int i=0;
		String myString = "List[" + this.length() + "] {";
		current = current.getPointer();
		while( current != tail ) {
			myString += "\n\r    [" + (++i) + "]: ";
			myString += current.getData().toString();
			current = current.getPointer();
		}
		myString += "\n\r}";
			
		return myString;
	}
	private void addNode(Object o) {
		Node current = head;
		if( allowDuplicates || !this.lookup(o) ) {
			switch(sortType) {
				case 0:
					// Adds the new node to the end of the list.
					while(current.getPointer() != tail) {
						current = current.getPointer();
					}
					current.setPointer(new Node(o,tail));
					length++;
					break;
				case -1:
					while( current.getPointer().getData() != null && ( current.getPointer().getData().toString().compareTo( o.toString() ) > 0 ) ) {
						//Lab3.print(current);
						current = current.getPointer();
					}
					length++;
					current.setPointer(new Node(o,current.getPointer()));
					break;
				case 1:
					while( current.getPointer().getData() != null &&  current.getPointer().getData().toString().compareTo( o.toString() ) < 0 ) {
						current = current.getPointer();
					}
					current.setPointer( new Node(o,current.getPointer()));
					length++;
					break;
			}
			
			//Lab3.print("attempting to add node " + o.toString());
		}
	}
	
	
	
	// Their commands (and <em>my</em> wrappers)

    public int length() {
		return length;
	}
    public int getLength() {
        return this.length();
    }
        // returns the count of data nodes currently in the list

    public Object first() {
    	return head.getPointer().getData();
    }
        // returns the *data* from the first data node in the list

    public Object last() {
    	Node current = head;
    	while(current.getPointer() != tail) {
    		current = current.getPointer();
    	}
    	return current.getData();
    }
        // returns the *data* from the last data node in the list

	/**	
	 *	searches the List for an Object identical to obj and returns
	 *	true if found, false otherwise.
	 */ 
	public boolean lookup(Object obj) {
		Node current = head.getPointer();
		
		while( current != tail && !current.getData().equals( obj )) {
			//Lab3.print("Comparing " + obj.toString() + current.getData().toString() + " is " + current.getData().equals( obj ) );
			current = current.getPointer();
		}
		if(current == tail) {
			return false;
		} else {
			return true;
		}
	}	
	public Object find(int n) {
		Node current = head;
		if(n > this.length() || n < 1) {
			System.out.println("Exception(\"OUT OF RANGE!\")");
			return null;
		}
		while(n > 0) {
			n--;
			current = current.getPointer();
		}
		return current.getData();
	}
        // counting from the start of the list, returns the *data* from the
        // nth node in the list; if the list is not at least n long, 
        // null is returned; find(1) returns the first data; 
        // find(0) and find(-n) returns null 

    public int find(Object obj) {
		Node current = head;
		int n = 0;
		while(current != tail && !current.getData().equals(obj)) {
			
			current = current.getPointer();
			n++;
		}
		if(current == tail) {
			return 0;
		} else {
			return n;
		}
    }

    public void delete(Object obj) {
        this.reset();
        Node current = head.getPointer();
		Node previous = head;
		while( current != tail && !current.getData().equals( obj ) ) {
			//Lab3.print(current.getData());
			previous = current;
			current = current.getPointer();
		}
		if(current == tail) {
			return;
		} else {
			previous.setPointer(current.getPointer());
			length--;
			return;
		}
		
    }

    public void delete(int n) {
		Node current = head;
		Node previous = head;
		if(n > this.length() || n < 1) {
			System.out.println("Exception(\"OUT OF RANGE!\")");
			return;
		}
		while(n > 0) {
			n--;
			previous = current;
			current = current.getPointer();
		}
		previous.setPointer(current.getPointer());
		return;
    }

    public void add(Object obj) {
    	this.addNode(obj);
    	reset();
	}
    public Object getNext() {
    	Object myReturn = null;
        if(internalPointerNode.getPointer() != null ) {
            myReturn = internalPointerNode.getPointer().getData();
            internalPointer++;
    	    internalPointerNode = internalPointerNode.getPointer();
    	    
    	} 
    	return myReturn;
    }
    public Object getPrevious() {
    	if(internalPointer <= this.length()) {
    		return null;
    	}
    	internalPointer--;
    	return this.find(internalPointer);
    }
    public void reset() {
    	internalPointer = 0;
    	internalPointerNode = head;
    	
    }
    
    /**
     * getClosest
     * Used to find the closest matching command in the list.
     * Such as no should math north as long as north is before
     * nod in the list.
     * @return Command.
     */
    public Object getClosest(String s) {
    	Node previous = head;
    	Node current = head;
    	
    	while(current.getPointer() != tail) {
    		current = current.getPointer();
    		/*TrollAttack.error(current.toString());
    		TrollAttack.error(current.getData().toString());
    		if(current.getData().toString() == null) {
    		    //TrollAttack.error("No current!!! HELP!");
    		}*/
    		if(current.getData().toString().startsWith(s)) {
    			return current.getData();
    		}
    		
    	}
    	return null;
    }
}
