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

public class BinaryCodedDecimalTest extends TestCase {

	public BinaryCodedDecimalTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	/*
	 * Test method for 'TrollAttack.BinaryCodedDecimal.getDouble()'
	 */
	public void testGetDouble() {
		BinaryCodedDecimal t = new BinaryCodedDecimal(32, 1);
		assertEquals(t.getDouble(),(double)3.2);
		
	}

	/*
	 * Test method for 'TrollAttack.BinaryCodedDecimal.getPrettyString()'
	 */
	public void testGetPrettyString() {
		BinaryCodedDecimal t = new BinaryCodedDecimal(3240, 3);
		assertEquals(t.getDouble(),(double)3.24);
		assertEquals(t.getPrettyString(),"3.240");
	}


}

