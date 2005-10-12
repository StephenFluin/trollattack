package TrollAttack.Commands;
/*
 * Created on May 7, 2005
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
public abstract class Command {
	String name;
	boolean peaceful = false;
	public Command() {}
	public String toString() {
		return name;
	}
	public Command(String commandName) { this(commandName, true);}
	public Command(String commandName, boolean mustBePeaceful) {name = commandName; peaceful = mustBePeaceful;}
	public Command(String commandName, int minimumState) {
	    name = commandName; 
	}
	public boolean isPeaceful() {
	    return peaceful;
	}
	public void execute() {}
	public void execute(String s) { this.execute(); }
}
