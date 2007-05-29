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
	
	/**
	 * Allows a command to have a commandHandler, dealing with subcommands.
	 * Perhaps this is not a good idea, lets try it and see!
	 */
	public CommandHandler commandHandler = null;
	
	
	
	
	public int maxPosition;
	
    public boolean needsPlayer = false;
	public Command() {}
	public String toString() {
		return name;
	}
	
	/**
	 * Creates a command that can be used at any time, except during death
	 * (max position = sleeping = 4).
	 * @param commandName The name of the command.
	 */
	public Command(String commandName) { this(commandName, 4);}
	
	/**
	 * Creates a command that can only be used if the player is in 
	 * position maxPos or less.
	 * (See Being for master list)
	 * 0 = Standing
	 * 1 = Fighting
	 * 2 = Sitting
	 * 3 = Resting / Laying Down
	 * 4 = Sleeping
	 * 5 = Dead
	 * @param commandName
	 * @param maxPos
	 */
	public Command(String commandName, int maxPos) {
		name = commandName; 
		maxPosition = maxPos;
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
