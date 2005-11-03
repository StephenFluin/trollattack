package TrollAttack;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class XMLHandler {
    public Hashtable sections;

    static int loopStart, loopIncrement;

    Node node, kid, Kid;

    NodeList kids;

    public XMLHandler(Document doc) {
        this(doc, new Hashtable());
    }

    public XMLHandler(Document doc, Hashtable secs) {
        sections = secs;
        if (doc == null) {
            return;
        }
        node = doc;
        kids = node.getChildNodes();
        // Kid is root item
        Kid = kids.item(0);
        NamedNodeMap attribs = Kid.getAttributes();
        if (attribs.getNamedItem("hacked") != null) {
            loopStart = 1;
            loopIncrement = 2;
        } else {
            loopStart = 0;
            loopIncrement = 1;
        }

        hashProcess(Kid, sections, 0);
        //TrollAttack.message(sections.toString());
        /*
         * I would like section to look like this: item=List[x] { [1]: {vnum=1,
         * name=sword} [2]: {vnum=2, name=sword2, ... , item=List[2] {...}}
         */
    }

    
    // Take a node and a hashtable and populate the hashtable with all of the
    // children of the node.
    public void hashProcess(Node n, Hashtable hash, int depth) {
        NodeList kids = n.getChildNodes();
        Node kid;

        //Look through all of the childnotes EG: all of the attribs of an item
        for (int j = loopStart; j < kids.getLength(); j += loopIncrement) {
            kid = kids.item(j);
            String depths = "";
            for (int t = 0; t < depth; t++) {
                depths += "    ";
            }
            //TrollAttack.message(depths + kid.getNodeName());

            Object currentValue = hash.get(kid.getNodeName());
            if (currentValue != null) {
                if (currentValue.getClass() == LinkedList.class) {
                    LinkedList list = (LinkedList) currentValue;
                    if (kid.getChildNodes().item(0).getNodeType() != Node.TEXT_NODE) {
                        Hashtable hashy = new Hashtable();
                        hashProcess(kid, hashy, depth + 1);
                        list.add(hashy);
                    } else {
                        list.add(kid.getChildNodes().item(0).getNodeValue());
                    }
                } else {
                    Object tmp = currentValue;
                    LinkedList ll = new LinkedList();
                    hash.put(kid.getNodeName(), ll);
                    ll.add(tmp);
                    if (kid.getChildNodes().item(0).getNodeType() != Node.TEXT_NODE) {
                        Hashtable hashy = new Hashtable();
                        hashProcess(kid, hashy, depth + 1);
                        ll.add(hashy);
                    } else {
                        if (kid.getChildNodes().item(0).getNodeValue() == null) {
                            TrollAttack
                                    .error("TRYING TO INSERT A NULL!!! WHY WOULD YOU DO THIS?! -- Ocurred at node "
                                            + kid.getNodeName()
                                            + " with value "
                                            + kid.getNodeValue());
                            if (kid.getChildNodes().item(0) == null) {
                                TrollAttack
                                        .error("The first child of that bad thing was null.");
                            } else {
                                TrollAttack
                                        .error("The value of the first child of that bad thing was null!");
                                TrollAttack.error("the kid node has "
                                        + kid.getChildNodes().getLength()
                                        + " children.");
                            }
                        } else {
                            //TrollAttack.debug("This means that the child of the node we are on isn't a text node, and it doesn't have a null value, the value is " + kid.getChildNodes().item(0).getNodeValue());
                        }
                        ll.add(kid.getChildNodes().item(0).getNodeValue());
                    }
                }
            } else {
                if (kid.getChildNodes().getLength() > (loopStart + loopIncrement)) {
                    Hashtable hashy = new Hashtable();
                    hashProcess(kid, hashy, depth + 1);
                    hash.put(kid.getNodeName(), hashy);
                } else {
                    //TrollAttack.message(kid.getNodeName());
                    if (kid.getChildNodes().item(loopStart) == null) {
                        /*
                         * TrollAttack.debug("Node " + kid.getNodeName() + " has
                         * null child in XMLHandler."); TrollAttack.debug("This
                         * means that there is a formatting error with the file
                         * that is being loaded."); TrollAttack.debug("You can
                         * fix this possible error by finding the '" +
                         * kid.getNodeName() + "' node from the parent node '" +
                         * kid.getParentNode().getNodeName() + "' and making
                         * sure it has a non-null child.");
                         */
                        continue;
                    }
                    if (kid.getChildNodes().item(loopStart).getNodeType() == Node.TEXT_NODE) {
                        hash.put(kid.getNodeName(), kid.getChildNodes().item(0)
                                .getNodeValue());
                    } else {
                        //TrollAttack.message("Probably processing exit " +
                        // kid.getNodeName() + ".");
                        Hashtable hashy = new Hashtable();
                        hashProcess(kid, hashy, depth + 1);
                        hash.put(kid.getNodeName(), hashy);
                        //TrollAttack.message(hashy.toString());
                    }
                }
            }
        }
    }
}
