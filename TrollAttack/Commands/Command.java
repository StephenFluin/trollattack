package TrollAttack.Commands;

import TrollAttack.Being;

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
	public String name;
	boolean peaceful = false;
    public boolean needsPlayer = false;
	public Command() {}
	public String toString() {
		return name;
	}
	
	/**
	 * Creates a command that can only be used when the player is in a
	 * peaceful state.
	 * @param commandName The name of the command.
	 */
	public Command(String commandName) { this(commandName, true);}
	
	public Command(String commandName, boolean mustBePeaceful) {name = commandName; peaceful = mustBePeaceful;}
	public Command(String commandName, int minPosition) {
	    name = commandName; 
	}
	public boolean isPeaceful() {
	    return peaceful;
	}
    public boolean equals(Command command) {
        return command.name.compareToIgnoreCase(name) == 0;
    }
    
	public boolean execute() { return false; }
	public boolean execute(String s) { return this.execute(); }
    
    public boolean execute(Being p) {
        return execute();
    }
    public boolean execute(Being p, String s) {
        return execute(s);
    }
}
