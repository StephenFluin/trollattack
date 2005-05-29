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
	int hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, hitSkill, hitDamage, experience, level;
	
	String shortDescription, name;
	boolean isFighting = false, isPlayer = false;
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
		return myPrompt.showPrompt(hitPoints, maxHitPoints, manaPoints, maxManaPoints, movePoints, maxMovePoints, experience, experience);
	}
	public String getPrompt() {
	    return myPrompt.getPrompt();
	}
	public void setPrompt(String s) {
	    myPrompt.setPrompt(s);
	}
	public boolean isFighting() {
	    return isFighting();
	}
	public void setIsFighting(boolean b) {
	    isFighting = b;
	}
	public int getExperience() {
	    return experience;
	}
	public void increaseExperience(int e) {
	    experience += e;
	}
	
	public int getLevel() {
	    return level;
	}
	
}
