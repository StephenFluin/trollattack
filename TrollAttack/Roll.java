package TrollAttack;
/*
 * Created on Jul 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Roll {
    int numberOfDice = 0;

    int sizeOfDice = 0;

    int addition = 0;

    public Roll(String value) {
        //TrollAttack.message("Trying to do math for roll of '" + value +
        // "'.");
        //Pattern pattern = Pattern.compile("(\\d+)d(\\d+)\\+(\\d+)");
        //Matcher matcher = pattern.matcher(value);
        String[] parts = {};
        try {
            parts = value.split("d");
        } catch (Exception e) {
            TrollAttack.error("Invalid roll type given - " + value);
            e.printStackTrace();
        }
        if (parts.length < 2) {
            if (parts.length < 1) {
                numberOfDice = 0;
            } else {
                numberOfDice = new Integer(parts[0]).intValue();
                if(numberOfDice == 0 && !(parts[0].startsWith("0"))) {
                	throw(new NumberFormatException("Doesn't look like a valid number."));
                } else {
                	//TrollAttack.debug("Rolled a real exact zero.");
                }
            }
            sizeOfDice = 1;
            addition = 0;
        } else if (Util.contains(parts[1], ",")) {
            String[] tmpParts = parts[1].split(",");
            numberOfDice = new Integer(parts[0]).intValue();
            sizeOfDice = new Integer(tmpParts[0]).intValue();
            addition = new Integer(tmpParts[1]).intValue();
        } else {
        	if(parts[0].compareTo("") == 0 || parts[1].compareTo("") == 0) {
        		throw(new NumberFormatException("Doesn't look like a valid number."));
        	}
            try {
                
            	numberOfDice = new Integer(parts[0]).intValue();
                sizeOfDice = new Integer(parts[1]).intValue();
                addition = 0;
            } catch (Exception e) {
                // Bad number format, try and ignore.
            }
        }

    }

    public int roll() {
        int results = 0;
        if(sizeOfDice <= 1) {
        	return numberOfDice + addition;
        }
        for (int i = 0; i < numberOfDice; i++) {
            results += (Math.random() * sizeOfDice) + 1 + addition;
        }
        return results;
    }

    public int getAverage() {
        double results = 0;
        results = numberOfDice * (sizeOfDice / 2 + addition + .5);
        return (int) results;
    }

    public String toString() {
        return numberOfDice + "d" + sizeOfDice + (addition != 0 ? ("," + addition) : "");
    }
}
