package TrollAttack;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Push {
	public int direction;
	public int wait;
	public String message;
	public Being tmpBeing;
	
	public Push(int direction, int wait, String message) {
		this.direction = direction;
		this.wait = wait;
		this.message = message;
	}

	
	public void execute(Being m) {
		PushPerson p = new PushPerson(this, m);
	}
	public class PushPerson extends Thread {
		Push p;
		Being b;
		public PushPerson(Push p, Being b) {
			this.p = p;
			this.b = b;
			this.start();
			
		}
		public void run() {
			Room tmpRoom = b.getActualRoom();
			try{
				Thread.sleep(p.wait);
			}catch(Exception e) {
				e.printStackTrace();
			}
			if(b.getActualRoom() == tmpRoom) {
				b.tell(message);
				b.move(direction);
			}
		}
	}
	public Node toNode(Document doc) {
		Node n = doc.createElement("push");
		n.appendChild(Util.nCreate(doc, "direction", Exit.directionName(direction)));
		n.appendChild(Util.nCreate(doc, "wait", wait + ""));
		n.appendChild(Util.nCreate(doc, "message", message));
		return n;
	}
}
