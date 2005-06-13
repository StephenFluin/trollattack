package TrollAttack;
/*
 * Created on May 9, 2005
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
public class Being {
	int hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, hitSkill, hitDamage, experience, level, favor;
	
	String shortDescription, name;
	boolean isPlayer = false;
	Being beingFighting = null;
	private Prompt myPrompt = new Prompt();
	public String getShort() {
		return shortDescription;
	}
	public String getName() {
	    return name;
	}
	public int getHitDamage() {
	    return hitDamage;
	}
	public String prompt() {
		return myPrompt.showPrompt(hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, experience, Util.experienceLevel(level + 1) - experience);
	}
	public String getPrompt() {
	    return myPrompt.getPrompt();
	}
	public void setPrompt(String s) {
	    myPrompt.setPrompt(s);
	}
	public boolean isReady() {
	    return (!this.isFighting());
	}
	public boolean isFighting() {
	    if(beingFighting == null) {
	        return false;
	    } else {
	        return true;
	    }
	}
	public void setBeingFighting(Being b) {
	    beingFighting = b;
	}
	public int getExperience() {
	    return experience;
	}
	public int getFavor() {
	    return favor;
	}
	public void increaseFavor(int increase) {
	    this.favor += increase;
	}
	public void decreaseFavor(int decrease) {
	    this.favor -= decrease;
	}
	
	/**
	 * Level Calculation
	 * The leveling is cacluated as such:
	 * 2^(level-2) * 2000
	 */
	public void increaseExperience(int e) {
	    experience += e;
	    if(experience > Util.experienceLevel(level) ) {
	        TrollAttack.print("You have attained level " + ++level + ".");
	        int healthIncrease = (int)(Math.random() * 10) + 10;
	        int manaIncrease = (int)(Math.random() * 10) + 10;
	        int moveIncrease = (int)(Math.random() * 10) + 10;
	        hitPoints += healthIncrease;
	        maxHitPoints += healthIncrease;
	        manaPoints += manaIncrease;
	        maxManaPoints += manaIncrease;
	        movePoints += moveIncrease;
	        maxMovePoints += moveIncrease;
	        hitDamage += (int)(Math.random() * 4);
	        hitSkill -= (int)(Math.random() * 6);
	    }
	}
	
	public int getLevel() {
	    return level;
	}
	
}
