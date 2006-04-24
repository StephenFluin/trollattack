package TrollAttack;

import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.CommandHandler;
import TrollAttack.Classes.Class;
import TrollAttack.Items.Equipment;
import TrollAttack.Items.Item;
import TrollAttack.Items.Weapon;

/*
 * Created on Mar 7, 2005
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
public class Player extends Being {
    //String prompt = "&G<&R%h&G/&R%H&G-&z%x&G> %T";


    private String password = "";

    public String title = "the newbie";

    Communication communication = null;

    private int lastActive = 0;

    private boolean builder = false;

    private Area area;
    
    public boolean authenticated = false;
    public boolean shouldColor = true;
    public boolean extraFormatting = false;
    public boolean showVnum = false;

    public double timePlayed = 0;

    public Player(Communication com) {
        // Set the default player values here.
        gold = 0;
        hitPoints = 50;
        maxHitPoints = 50;
        manaPoints = 40;
        maxManaPoints = 40;
        movePoints = 30;
        maxMovePoints = 30;
        hitSkill = new Roll("2d3,2");
        hitLevel = 5;
        hitDamage = new Roll("2d3");
        level = 1;
        currentRoom = 1;
        shortDescription = "a player";
        communication = com;
        setCurrentRoom(1);
        ch = new CommandHandler(this);

        isPlayer = true;
        timePlayed = 0;

    }

    public Player(int gold, int hp, int mhp, int mp, int mmp, int vp, int mvp,
            int playerHitLevel, String hitSkillString, String hitDamageString,
            int lev, int room, int exp, boolean isBuilder, String title, String prompt,
            double timePlayed, Area myArea, int hunger, int thirst,
            String shortd, String pass, String className) {
        this.gold = gold;
        hitPoints = hp;
        maxHitPoints = mhp;
        manaPoints = mp;
        maxManaPoints = mmp;
        movePoints = vp;
        maxMovePoints = mvp;
        hitLevel = playerHitLevel;
        hitSkill = new Roll(hitSkillString);
        hitDamage = new Roll(hitDamageString);
        level = lev;
        setCurrentRoom(room);
        experience = exp;
        builder = isBuilder;
        this.timePlayed = timePlayed;
        this.title = title;
        this.setPrompt(prompt);
        setArea(myArea);
        shortDescription = shortd;
        name = shortd;
        
        password = pass;
        isPlayer = true;
        this.hunger = hunger;
        this.thirst = thirst;
        setBeingClass(className);
        ch = new CommandHandler(this);
        //TrollAttack.message("New player has password '" + password + "'.");

    }

    public Player(Player p) {
        hitPoints = p.hitPoints;
        maxHitPoints = p.maxHitPoints;
        manaPoints = p.manaPoints;
        maxManaPoints = p.maxManaPoints;
        movePoints = p.movePoints;
        maxMovePoints = p.maxMovePoints;
        hitSkill = p.hitSkill;
        hitDamage = p.hitDamage;
        level = p.level;
        setCurrentRoom(p.getCurrentRoom());
        experience = p.experience;
        shortDescription = p.shortDescription;
        ch = p.ch;
        password = p.password;
        isPlayer = true;
        timePlayed = p.timePlayed;
        title = p.title;
    }

    public void setCommunication(Communication com) {
        communication = com;
    }

    public Communication getCommunication() {
        return communication;
    }

    public void tell(String s, boolean wrapAtEnd) {
        if (switched != null) {
            communication.print(Communication.CYAN + "BODY:" + s + (wrapAtEnd ? Util.wrapChar : ""));
        } else {
            communication.print(s + (wrapAtEnd ? Util.wrapChar : ""));
        }
    }
    public void name(String newName) {
        shortDescription = newName;
    }

    public void setArea(Area newArea) {
        area = newArea;
    }

    public Area getArea() {
        return area;
    }

    public boolean canEdit(Room room) {
        return canEdit(room.vnum);
    }

    public boolean canEdit(Mobile mobile) {
        return canEdit(mobile.vnum);
    }



    public boolean canEdit(Item item) {
        return canEdit(item.vnum);
    }

    public boolean canEdit(int vnum) {
        if ( ( level > 60
                || (getActualArea().low <= vnum && getActualArea().high >= vnum) ) && vnum > 0) {
            return true;
        } else {
            tell("You don't have permissions to modify this area, your vnum range is "
                    + getActualArea().low + "-" + getActualArea().high + "!");
            return false;
        }
    }

    public void save() {
        Util.savePlayer(this);
    }

    public void quit() {
        quit(false);
    }
    public void quit(boolean forceful) {
        if(getFollowing() != null && !forceful) {
            stopFollowing();
        }
        while(!followers.isEmpty()) {
            Being follower = followers.getFirst();
            follower.stopFollowing();
        }
        getActualRoom().removeBeing(this);
        TrollAttack.gamePlayers.remove(this);
        closeConnection();
    }

    public String getName() {
        return shortDescription;
    }

    public void setPassword(String pass) {
        password = pass;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public boolean checkPassword(String pass) {
        //TrollAttack.message("Comparing " + pass + " to " + password);
        if (password.compareTo(pass) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setLastActive(int time) {
        lastActive = time;
    }

    public int getIdleTime() {
        return (TrollAttack.getTime() - lastActive);
    }

    public boolean isBuilder() {
        return builder;
    }

    public void setBuilder(boolean is) {
        builder = is;
    }

    public String toString() {
        String r = "";
        r += "HP: " + hitPoints + "\n\r" + "MHP: " + maxHitPoints + "\n\r"
                + "MP: " + manaPoints + "\n\r" + "MP: " + maxManaPoints
                + "\n\r";
        return r;
    }

    public Document toDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Turn on validation, and turn off namespaces
        factory.setValidating(false);
        factory.setNamespaceAware(false);
        factory.setIgnoringComments(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Print the document from the DOM tree and feed it an initial
        // indentation of nothing
        Node n = doc.createElement("TrollAttack");
        doc.appendChild(n);
        Node m = doc.createElement("player");
        n.appendChild(m);
        LinkedList<Node> attribs = new LinkedList<Node>();
        attribs.add(Util.nCreate(doc, "name", getName() + ""));
        attribs.add(Util.nCreate(doc, "hitPoints", hitPoints + ""));
        attribs.add(Util.nCreate(doc, "maxHitPoints", maxHitPoints + ""));
        attribs.add(Util.nCreate(doc, "manaPoints", manaPoints + ""));
        attribs.add(Util.nCreate(doc, "maxManaPoints", maxManaPoints + ""));
        attribs.add(Util.nCreate(doc, "movePoints", movePoints + ""));
        attribs.add(Util.nCreate(doc, "maxMovePoints", maxMovePoints + ""));
        attribs.add(Util.nCreate(doc, "hitLevel", hitLevel + ""));
        attribs.add(Util.nCreate(doc, "hitSkill", hitSkill.toString() + ""));
        attribs.add(Util.nCreate(doc, "hitDamage", hitDamage.toString() + ""));
        attribs.add(Util.nCreate(doc, "level", level + ""));
        attribs.add(Util.nCreate(doc, "room", getCurrentRoom() + ""));
        attribs.add(Util.nCreate(doc, "experience", experience + ""));
        attribs.add(Util.nCreate(doc, "password", password + ""));
        attribs.add(Util.nCreate(doc, "builder", this.builder + ""));
        attribs.add(Util.nCreate(doc, "timePlayed", timePlayed + ""));
        attribs.add(Util.nCreate(doc, "hunger", hunger + ""));
        attribs.add(Util.nCreate(doc, "thirst", thirst + ""));
        attribs.add(Util.nCreate(doc, "gold", gold + ""));
        attribs.add(Util.nCreate(doc, "title", title + ""));
        attribs.add(Util.nCreate(doc, "prompt", getPromptString()));
        attribs.add(Util.nCreate(doc, "class", getClassName() + ""));
        attribs.add(Util.nCreate(doc, "strength", strength + ""));
        attribs.add(Util.nCreate(doc, "constitution", constitution + ""));
        attribs.add(Util.nCreate(doc, "charisma", charisma + ""));
        attribs.add(Util.nCreate(doc, "dexterity", dexterity + ""));
        attribs.add(Util.nCreate(doc, "intelligence", intelligence + ""));
        attribs.add(Util.nCreate(doc, "wisdom", wisdom + ""));
        attribs.add(Util.nCreate(doc, "shouldcolor", shouldColor + ""));
        attribs.add(Util.nCreate(doc, "extraformatting", extraFormatting + ""));
        attribs.add(Util.nCreate(doc, "showvnums", showVnum + ""));
        

        if (getArea() != null)
            attribs.add(Util.nCreate(doc, "area", getArea().filename + ""));
        /*
         * Node itemList = doc.createElement("itemList");
         * 
         * for(int i = 0;i < playerItems.getLength();i++) {
         * itemList.appendChild(Util.nCreate(doc, "item", (
         * (Item)(playerItems.getNext()) ).vnum + "")); } attribs.add(itemList);
         */

        for(Node newAttrib : attribs ) {
            m.appendChild(newAttrib);
        }
        Being.addEquipmentNodes(doc, this, m);
        Being.addAbilityNodes(doc, this, m);
        return doc;
    }

    public void closeConnection() {
        communication.close();
    }

    public void handleCommand(String command) {
        ch.handleCommand(command);
    }

    public int getHitDamage() {
        //TrollAttack.error("Reading player hit damage.");
        int hd = hitDamage.roll();
        for(Equipment currentItem : equipment) {
            if (currentItem instanceof Weapon) {
                int adam = ((Weapon) currentItem).getHitDamage();
                // TrollAttack.message("Player is wearing " +
                // currentItem.getShort() + ", helping with " + adam + "
                // damage.");
                hd += adam;
            }
        }
        return hd;
    }

    public boolean isReady() {
        return !(isFighting() || state > 0);
    }

    public void kill(Being killer) {
        kill();
        tell(Communication.RED + (killer.getShort() + " KILLS you!").toUpperCase() );

        setCurrentRoom(1);
        hitPoints = manaPoints = movePoints = 1;
        save();
       
        try{
        	int i = 10;
        	while(i-- > 0) {
    			tell("Your spirit floats rapidly over the astral plane, looking for a way to return.");
        		Thread.sleep(1000);
        	}
        } catch(Exception e) {
        	
        }
        tell("Your spirit finds its way back the planet, and you find yourself again.");
       busyDoing = "";
       getActualRoom().addBeing(this);
    }

    public void score() {
        tell(Util.mStat(this));

    }

    public double getTimePlayed() {
        return timePlayed;
    }
    
    public boolean equals(Player player) {
        return getShort().compareToIgnoreCase(player.getShort()) == 0;
    }

    public String interactiveNewPlayer() throws IOException {
        
        name = "new";
        tell();
        tell(Communication.WHITE + "Picking a name is one of the most important parts of starting to play.  Once you have created your character, you will not be able to change your name, so choose wisely!  Immortals may change your name if they deem it inappropiriate or offensive.");
        tell();
        while (name.compareToIgnoreCase("new") == 0) {
            tell(Communication.WHITE + "Pick a name:");
            name = communication.getLine();
            if (DataReader.readPlayerFile(name) != null) {
                name = "new";
                tell(Communication.WHITE + "That name is taken!");
            }
        }
        tell();
        tell();
        shortDescription = name;
        tell(Communication.WHITE + "Pick a password:");
        setPassword(communication.getLine());
        authenticated = true;
        tell();
        tell();
        boolean finishedWithStep = true;
        while(getBeingClass() == null ) {
            if(!finishedWithStep) {
                tell(Communication.GREEN + "That is not a valid class.");
                tell();
            }
            tell(Communication.WHITE + "What class would you like to be?");
            tell(Communication.WHITE + "Options:");
            for(Class option : TrollAttack.gameClasses) {
                tell(Communication.CYAN + option.getName() + "");
            }
            setBeingClass(communication.getLine());
            finishedWithStep = false;
        }
        
        
        while(!finishedWithStep) {
            tell();
            tell();
            Roll stat = new Roll("3d6");
            strength = stat.roll();
            wisdom = stat.roll();
            constitution = stat.roll();
            charisma = stat.roll();
            dexterity = stat.roll();
            intelligence = stat.roll();
            tell(Communication.WHITE + "You rolled the following:");
            tell(Communication.WHITE + strength + "\tStrength");
            tell(Communication.WHITE + wisdom + "\tWisdom");
            tell(Communication.WHITE + constitution + "\tConstitution");
            tell(Communication.WHITE + charisma + "\tCharisma");
            tell(Communication.WHITE + dexterity +  "\tDexterity");
            tell(Communication.WHITE + intelligence + "\tIntelligence");
            tell(Communication.CYAN + "Keep these? (Y/n)");
            String read = communication.getLine();
            if(read.startsWith("y") || read.startsWith("Y")) {
                finishedWithStep = true;
            }
        }
        
        tell(Communication.CYAN + "Welcome to Troll Attack!  We hope you enjoy your time here.  If you have any questions, send an email to questions@trollattack.com.");
        return password;
    }


	public void setFavor(int i) {
		favor = i;
	}


    


}
