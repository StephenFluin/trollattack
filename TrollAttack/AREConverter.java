package TrollAttack;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
 * Created on Sep 19, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */

/**
 * @author PeEll
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AREConverter {

    public static void main(String[] args) {
        
        
        if(args.length < 1) {
            print("Convert what file?");
            return;
        }
        File areaFile = new File(args[0]);
        if(!areaFile.canRead()) {
            print("This file is not readable!");
	        return;
	    }
        if(!areaFile.isFile()) {
            print("This isn't a file!");
            return;
        }
        print("Reading: '" + areaFile.getAbsolutePath() + "'.");
        
        EasyReader in = new EasyReader(areaFile.getAbsolutePath());
        
        String area = "";
        while(!in.isEOF()) {
            area += in.stringInputLine() + "\n";
        }
        String pattern = ".*" +
                 "#AREA\\s*(.*?)~.*" +
                 "#AUTHOR\\s*(.*?)~.*" +
                 "#RANGES\n(.*?)\n\\$.*" +
                 "#FLAGS\n(.*?)\n.*" +
                 "#ECONOMY\\s*(.*?)\n.*" +
                 "#MOBILES\n(.*?)" +
                 "#OBJECTS\n(.*?)" +
                 "#ROOMS\n(.*?)" +
                 "#RESETS\n(.*?)S" +
                 "#SHOPS\n(.*?).*" +
                 "#SPECIALS\n(.*?)\nS.*";
        Matcher sections = parse(area, pattern);
        if(!sections.matches()) {
            print("Invalid area file!");
            return;
        }
        String AREA, AUTHOR, RANGES, FLAGS, ECONOMY, MOBILES, OBJECTS, ROOMS, RESETS, SHOPS, SPECIALS;
        AREA = sections.group(1);
        AUTHOR = sections.group(2);
        RANGES = sections.group(3);
        FLAGS = sections.group(4);
        ECONOMY = sections.group(5);
        MOBILES = sections.group(6);
        OBJECTS = sections.group(7);
        ROOMS = sections.group(8);
        RESETS = sections.group(9);
        SHOPS = sections.group(10);
        SPECIALS = sections.group(11);
        
        
    }
    public static void print(String s) {
        System.out.println(s);
    }
    public static String parseSingle(String s, String pattern) {
       Matcher m = parse(s, ".*?" + pattern + ".*?");
        String result = "";
        print(m.matches() ? "matched" : "not matched!");
        result = m.group(1);
        return result;
    }
    public static Matcher parse(String s, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL + Pattern.MULTILINE + Pattern.UNIX_LINES);
        Matcher  m = p.matcher(s);
        return m;
    }
}
