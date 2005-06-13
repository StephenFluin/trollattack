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
	boolean peaceful = true;
	public Command() {}
	public String toString() {
		return name;
	}
	public Command(String commandName) { this(commandName, true);}
	public Command(String commandName, boolean b) {name = commandName; peaceful = b;}
	public boolean isPeaceful() {
	    return peaceful;
	}
	public void execute() {}
	public void execute(String s) {}
}
