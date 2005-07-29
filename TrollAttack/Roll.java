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
        String[] parts = {};
        try {
            parts = value.split("d");
        } catch(Exception e) {
            TrollAttack.error("Invalid roll type given");
            e.printStackTrace();
        }
        if(parts.length < 2) {
            numberOfDice = new Integer(parts[0]).intValue();;
            sizeOfDice = 1;
            addition = 0;
        } else if(Util.contains(parts[1], ",")) {
            String[] tmpParts = parts[1].split(",");
            numberOfDice = new Integer(parts[0]).intValue();
            sizeOfDice = new Integer(tmpParts[0]).intValue();
            addition = new Integer(tmpParts[1]).intValue();
        } else {
           try{ numberOfDice = new Integer(parts[0]).intValue();
            sizeOfDice = new Integer(parts[1]).intValue();
            addition = 0;
           } catch(Exception e) {
               // Bad number format, try and ignore.
           }
        }
        
    }
    public int roll() {
        int results = 0;
        for(int i = 0; i < numberOfDice;i++) {
            results += (Math.random() * sizeOfDice)+ 1 + addition;
        }
        return results;
    }
    public int getAverage() {
        double results = 0;
        results = numberOfDice * (sizeOfDice / 2 + addition + .5);
        return (int)results;
    }
    public String toString() {
        return numberOfDice + "d" + sizeOfDice + "," + addition;
    }
}
