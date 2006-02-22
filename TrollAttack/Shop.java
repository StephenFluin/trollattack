package TrollAttack;

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import TrollAttack.Items.Item;

public class Shop extends Room {

	public LinkedList<Item> shopItems = new LinkedList<Item>();
	
	public Shop(int vnum, String title, String description, LinkedList<Exit> exits) {
		 super(vnum, title, description, exits);
	}
	public Shop(Room r) {
		super(r.vnum, r.title, r.description, r.roomExits);
		roomBeings = r.roomBeings;
		setNoWander(getNoWander());
		roomItems = r.roomItems;
		TrollAttack.replaceRoom(r, this);
	}
	
	public String list() {
		String result = Communication.WHITE + "Item Name\tCost\t" + Util.wrapChar + Communication.GREEN;
		for(Item i : shopItems) {
			result += i.getShort() + "\t" + i.cost + "\t" + Util.wrapChar;
		}
		if(shopItems.size() < 1) {
			result += "This shop currently has nothing for sale.";
		}
		return result;
	}
	public Item sell(String name) {
		for(Item i : shopItems) {
			if(Util.contains(i.getName(), name)) {
				return i;
			}
		}
		return null;
	}
	public void addShopItem(Item i) {
		shopItems.add(i);
	}
	public Node toShopNode(Document doc) {
		Node n = doc.createElement("shop");
		if(shopItems.size() < 1) {
			n.appendChild(Util.nCreate(doc, "value", "true"));
		}
		for(Item i : shopItems) {
			n.appendChild(Util.nCreate(doc, "item", i.vnum + ""));
		}
		return n;
	}

}
