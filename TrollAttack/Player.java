package TrollAttack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Commands.Ability;
import TrollAttack.Commands.CommandHandler;
import TrollAttack.Classes.Class;
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
    private CommandHandler backupCH = null;

    private String password = "";

    public String title = "the newbie";

    Communication communication = null;

    private int lastActive = 0;

    private boolean builder = false;

    private Area area;
    
    public boolean authenticated = false;

    public double timePlayed = 0;
    private java.util.LinkedList<Ability> playerAbilities;

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

    public void tell(String s) {
        if (switched != null) {
            communication.print("BODY:" + s);
        } else {
            communication.print(s);
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

    public void switchWith(Being being) {
      if (being == this && switched != null && backupCH != null) {
            // unswitch
            
            switched.setSwitched(null);
            setSwitched( null );
            ch = backupCH;
        }/* else {
            // switch
            backupCH = ch;
            ch = being.ch;
            being.switched = this;
            switched = being;
        }*/
    }

    public boolean canEdit(Item item) {
        return canEdit(item.vnum);
    }

    public boolean canEdit(int vnum) {
        if (level > 60
                || (getActualArea().low <= vnum && getActualArea().high >= vnum)) {
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
        getActualRoom().removeBeing(this);
        TrollAttack.gamePlayers.delete(this);
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
        LinkedList attribs = new LinkedList();
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
        attribs.add(Util.nCreate(doc, "prompt", getPrompt()));
        attribs.add(Util.nCreate(doc, "class", getClassName() + ""));
        

        if (getArea() != null)
            attribs.add(Util.nCreate(doc, "area", getArea().filename + ""));
        /*
         * Node itemList = doc.createElement("itemList");
         * 
         * for(int i = 0;i < playerItems.getLength();i++) {
         * itemList.appendChild(Util.nCreate(doc, "item", (
         * (Item)(playerItems.getNext()) ).vnum + "")); } attribs.add(itemList);
         */

        boolean stillMore = true;
        while (stillMore) {
            Node newAttrib = (Node) attribs.getNext();
            if (newAttrib == null) {
                stillMore = false;
            } else {
                m.appendChild(newAttrib);

            }
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
        Item currentItem;
        while (equipment.itemsRemain()) {
            currentItem = (Item) equipment.getNext();
            if (currentItem.getClass() == Weapon.class) {
                int adam = ((Weapon) currentItem).getHitDamage();
                // TrollAttack.message("Player is wearing " +
                // currentItem.getShort() + ", helping with " + adam + "
                // damage.");
                hd += adam;
            }
        }
        equipment.reset();
        return hd;
    }

    public boolean isReady() {
        return !(isFighting() || state > 0);
    }

    public void kill(Being killer) {
        kill();
        tell(killer.getShort() + " kills you!");

        setCurrentRoom(1);
        hitPoints = manaPoints = movePoints = 1;
        save();
        authenticated = false;
        //	    communication.authenticate(this);
        closeConnection();
    }

    public void score() {
        tell(Util.mStat(this));

    }

    public double getTimePlayed() {
        return timePlayed;
    }



}
