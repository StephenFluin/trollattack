package TrollAttack;
/*
 * Created on May 26, 2005
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
public class Prompt {
	String promptString;
	public Prompt() {
		this( "<%h/%H %m/%M %v/%V %X>" );
	}
	public Prompt(String s) {
		promptString = s;
	}
	
	public void setPrompt(String s) {
		promptString = s;
	}
	
	/**
	 * Prompt Info
	 * h - Hit Points
	 * H - Max Hit Points
	 * m - Mana Points
	 * M - Max Mana Points
	 * v - Move Points
	 * V - Max Move Points
	 * x - Experience 
	 * X - Experience Until Next Level
	 * @return
	 */
	public String showPrompt(int h, int H, int m, int M, int v, int V, int x, int X) {
		String prompt = promptString.replaceAll("%h", h + "");
		prompt = prompt.replaceAll("%H", H + "");
		prompt = prompt.replaceAll("%m", m + "");
		prompt = prompt.replaceAll("%M", M + "");
		prompt = prompt.replaceAll("%v", v + "");
		prompt = prompt.replaceAll("%V", V + "");
		prompt = prompt.replaceAll("%x", x + "");
		prompt = prompt.replaceAll("%X", X + "");
		return prompt;
	}
	public String getPrompt() {
	    return promptString;
	}
	
	
}
