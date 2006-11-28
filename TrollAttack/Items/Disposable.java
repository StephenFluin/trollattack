/*
 * Nov 11, 2006
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Items;

public interface Disposable {
	/**
	 * Returns the amount of clicks remaining on this 
	 * disposable item before it disappears.
	 * @return
	 */
	int disposalTime = 0;
	public boolean isDone();

	public void decay();
}

