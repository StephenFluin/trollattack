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
	public Command() {}
	public String toString() {
		return name;
	}
	public Command(String commandName) {}
	
	public void execute() {}
	public void execute(String s) {}
}
