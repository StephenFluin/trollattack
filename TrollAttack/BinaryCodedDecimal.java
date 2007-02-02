/*
 * Jan 9, 2007
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack;

import junit.framework.TestCase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class BinaryCodedDecimal {
	private int number;
	private int offset;
	
	public BinaryCodedDecimal(String integer) {
		throw new NotImplementedException();
	}
	public BinaryCodedDecimal(int number, int offset) {
		this.number = number;
		this.offset = offset;
	}
	public double getDouble() {
		return (double)number / Math.pow(10,offset);
	}
	public String getPrettyString() {
		return (int)(number/Math.pow(10,offset)) + "." + (number - ((int)(number/Math.pow(10,offset)))*Math.pow(10,offset));
	}
	
	public static void main(String[] args) {
		//Run Unit Tests
		TestCase t = new BinaryCodedDecimalTest(null);
		t.run();
	}
	
	
}

