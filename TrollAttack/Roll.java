/*
 * Created on Jul 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package TrollAttack;




/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Roll {
    int numberOfDice = 0;
    int sizeOfDice = 0;
    int addition = 0;
    public Roll(String value) {
        //TrollAttack.message("Trying to do math for roll of '" + value + "'.");
        //Pattern pattern = Pattern.compile("(\\d+)d(\\d+)\\+(\\d+)");
        //Matcher matcher = pattern.matcher(value);
        String[] parts = value.split("d");
        if(Util.contains(parts[1], ",")) {
            String[] tmpParts = parts[1].split(",");
            numberOfDice = new Integer(parts[0]).intValue();
            sizeOfDice = new Integer(tmpParts[0]).intValue();
            addition = new Integer(tmpParts[1]).intValue();
        } else {
            numberOfDice = new Integer(parts[0]).intValue();
            sizeOfDice = new Integer(parts[1]).intValue();
            addition = 0;
        }
        
    }
    public int roll() {
        int results = 0;
        for(int i = 0; i < numberOfDice;i++) {
            results += (Math.random() * sizeOfDice)+ 1 + addition;
        }
        return results;
    }
    public String toString() {
        return numberOfDice + "d" + sizeOfDice + "," + addition;
    }
}
