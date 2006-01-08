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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Prompt {
    static final String defaultPrompt = "&W<&R%h&G/&R%H &B%m&G/&B%M &P%X&W>";
    private String promptString;

    public Prompt() {
        this(defaultPrompt);
    }

    public Prompt(String s) {
        promptString = s;
    }

    public void setPrompt(String s) {
        if(s.length() < 1) {
            setPrompt(defaultPrompt);
        } else {   
            promptString = s;
        }
    }

    /**
     * Prompt Info h - Hit Points H - Max Hit Points m - Mana Points M - Max
     * Mana Points v - Move Points V - Max Move Points x - Experience X -
     * Experience Until Next Level
     * 
     * @return
     */
    public String showPrompt(int hp, int HP, int mana, int MANA, int move, int MOVE, int experience,
            int experienceToLevel, int roomVnum, int gold) {
        String prompt = promptString.replaceAll("%h", hp + "");
        prompt = prompt.replaceAll("%H", HP + "");
        prompt = prompt.replaceAll("%m", mana + "");
        prompt = prompt.replaceAll("%M", MANA + "");
        prompt = prompt.replaceAll("%v", move + "");
        prompt = prompt.replaceAll("%V", MOVE + "");
        prompt = prompt.replaceAll("%x", experience + "");
        prompt = prompt.replaceAll("%X", experienceToLevel + "");
        prompt = prompt.replaceAll("%r", roomVnum + "");
        prompt = prompt.replaceAll("%g", gold + "");
        return prompt;
    }


    public String getPrompt() {
        return promptString;
    }

}
