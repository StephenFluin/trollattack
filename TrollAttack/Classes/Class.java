/*
 * Created on Nov 1, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
package TrollAttack.Classes;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.TrollAttack;
import TrollAttack.Util;
import TrollAttack.Commands.Ability;
import TrollAttack.Commands.abilities.Scan;


public class Class {
    String name;
    
    public Class(String name) {
        if(name == null) {
            new Exception("Null class names are not allowed!").printStackTrace();
        }
        this.name = name;
        //TrollAttack.debug("A new class '" + name + "' has been created, congratulations!");
    }
    
    Hashtable<Ability, AbilityData> abilitiesData 
        = new Hashtable<Ability, AbilityData>();
    
    public String getName() {
        return name;
    }
    public String listAbilities() {
        String result = "";
        for(Ability ability : abilitiesData.keySet()) {
            result += ability.toString() + Util.wrapChar;
        }
        return result;
    }
    public Ability findAbility(String name) {
        return findAbility(name, 65);
    }
    public Ability findAbility(String name, int level) {
        Hashtable<Ability, AbilityData> data = getAbilityData();
        for(Ability ability : data.keySet()) {
            if(data.get(ability) == null) {
                TrollAttack.error("Unknown ability in playerfile.");
            }
            if(ability.name == null) {
                TrollAttack.error("Ability has no name,, what the?!");
            } else {
                //TrollAttack.debug("Name is " + name + " and abilityname is " + ability.name + ".");
            }
            if(data.get(ability).level <= level 
                        && 
                        ability
                        .name
                        .startsWith(
                                name
                                )) {
                return ability;
            }
        }
        return null;
    }
    public Set<Ability> getAbilityList() {
        return abilitiesData.keySet();
    }
    public Hashtable<Ability, AbilityData> getAbilityData() {
        return abilitiesData;
    }
    
    public Node toNode(Document doc) {
           
        Node m = doc.createElement("class");
        Vector<Node> attribs = new Vector<Node>();
        attribs.add(Util.nCreate(doc, "name", name + ""));
        for(Ability ability : abilitiesData.keySet()) {
            AbilityData data = abilitiesData.get(ability);
            Node abilityNode = doc.createElement("ability");
            abilityNode.appendChild(Util.nCreate(doc, "name", ability.name + ""));
            abilityNode.appendChild(Util.nCreate(doc, "level", data.level + ""));
            abilityNode.appendChild(Util.nCreate(doc, "maxProficiency", data.maxProficiency + ""));
            
            attribs.add(abilityNode);
        }
        for(Node n : attribs) {
            m.appendChild(n);
        }
        
        return m;
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
        n.appendChild(toNode(doc));
        return doc;
    }
    
    public void saveClass() {
        try {
            Util.XMLPrint(toDocument(), "Classes/" + getFileName());
        } catch (Exception e) {
            TrollAttack
                    .error("There was a problem writing the class file.");
            e.printStackTrace();
        }
    }
    public void setAbilityData(Hashtable<Ability, AbilityData> hash) {
        abilitiesData = hash;
        /*for(Ability ability : hash.keySet()) {
            abilitiesData.put(ability, hash.get(ability));
        }*/
                
    }

    public String[] look() {
    String[] items = new String[255];
    return items;
}
    public void setName(String newName) {
        name = newName;
    }
    public void updateAbility(Ability newAbility, int i, float f) {
        if(newAbility != null) {
            abilitiesData.put(newAbility, new AbilityData(i, f));
        }
        
    }
    public Ability deleteAbility(Ability ability) {
        Ability match = null;
        for(Ability ab : abilitiesData.keySet()) {
            if(ab == ability) {
                match = ability;
                break;
            }
        }
        if(match != null) {
           abilitiesData.remove(match);
        }
        return match;
    }
    public String getFileName() {
        return getName() + ".class.xml";
    }
}



