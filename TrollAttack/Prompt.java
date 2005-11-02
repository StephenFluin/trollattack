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
    String promptString;

    public Prompt() {
        this("&W<&R%h&G/&R%H &B%m&G/&B%M  &P%X&W>");
    }

    public Prompt(String s) {
        promptString = s;
    }

    public void setPrompt(String s) {
        promptString = s;
    }

    /**
     * Prompt Info h - Hit Points H - Max Hit Points m - Mana Points M - Max
     * Mana Points v - Move Points V - Max Move Points x - Experience X -
     * Experience Until Next Level
     * 
     * @return
     */
    public String showPrompt(int h, int H, int m, int M, int v, int V, int x,
            int X) {
        String prompt = promptString.replaceAll("%h", h + "");
        prompt = prompt.replaceAll("%H", H + "");
        prompt = prompt.replaceAll("%m", m + "");
        prompt = prompt.replaceAll("%M", M + "");
        prompt = prompt.replaceAll("%v", v + "");
        prompt = prompt.replaceAll("%V", V + "");
        prompt = prompt.replaceAll("%x", x + "");
        prompt = prompt.replaceAll("%X", X + "");
        prompt = color(prompt);
        return prompt;
    }

    public String color(String s) {
        // Lights
        s = s.replaceAll("&C", Communication.CYAN);
        s = s.replaceAll("&P", Communication.PURPLE);
        s = s.replaceAll("&W", Communication.WHITE);
        s = s.replaceAll("&Y", Communication.YELLOW);
        s = s.replaceAll("&G", Communication.GREEN);
        s = s.replaceAll("&R", Communication.RED);
        s = s.replaceAll("&B", Communication.BLUE);
        s = s.replaceAll("&A", Communication.GREY);

        // Darks
        s = s.replaceAll("&DC", Communication.DARKCYAN);
        s = s.replaceAll("&DP", Communication.DARKPURPLE);
        s = s.replaceAll("&DW", Communication.DARKWHITE);
        s = s.replaceAll("&DY", Communication.DARKYELLOW);
        s = s.replaceAll("&DG", Communication.DARKGREEN);
        s = s.replaceAll("&DR", Communication.DARKRED);
        s = s.replaceAll("&DB", Communication.DARKBLUE);
        s = s.replaceAll("&DA", Communication.DARKGREY);
        return s;
    }

    public String getPrompt() {
        return promptString;
    }

}
