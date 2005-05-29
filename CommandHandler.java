/*
 * Created on May 7, 2005
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
public class CommandHandler {
	LinkedList commandList;
	SpellHandler sh;
	public CommandHandler() {
		commandList = new LinkedList(false, 0);
		sh = new SpellHandler();
		//TrollAttack.print("Starting command registration...");
		registerCommand(new CommandMove("north", CommandMove.NORTH));
		registerCommand(new CommandMove("south", CommandMove.SOUTH));
		registerCommand(new CommandMove("east", CommandMove.EAST));
		registerCommand(new CommandMove("west", CommandMove.WEST));
		registerCommand(new CommandMove("up", CommandMove.UP));
		registerCommand(new CommandMove("down", CommandMove.DOWN));
		registerCommand(new CommandMove("northeast", CommandMove.NORTHEAST));
		registerCommand(new CommandMove("ne", CommandMove.NORTHEAST));
		registerCommand(new CommandMove("northwest", CommandMove.NORTHWEST));
		registerCommand(new CommandMove("nw", CommandMove.NORTHWEST));
		registerCommand(new CommandMove("southeast", CommandMove.SOUTHEAST));
		registerCommand(new CommandMove("se", CommandMove.SOUTHEAST));
		registerCommand(new CommandMove("southwest", CommandMove.SOUTHWEST));
		registerCommand(new CommandMove("sw", CommandMove.SOUTHWEST));
		registerCommand(new Kill("kill"));
		registerCommand(new Get("get"));
		registerCommand(new Drop("drop"));
		registerCommand(new Look("look"));
		registerCommand(new Cast("cast"));
		registerCommand(new Prompt("prompt"));
		registerCommand(new Consider("consider"));
		registerCommand(new Inventory("inventory"));
		registerCommand(new Equipment("equiptment"));
		registerCommand(new Wield("wield"));
		registerCommand(new Help("help"));
		registerCommand(new Help("?"));
		registerCommand(new Quit("quit"));
		registerCommand(new Quit("exit"));
	}

	public void registerCommand( Command c) {
		/*
		 if(c == null) {
		    TrollAttack.print("c is null!");
		} else {
		    TrollAttack.print("registering " + c.name);
		}*/
	    commandList.add(c);
		 
	}
	public void handleCommand( String commandString) {
		String[] commandParts = commandString.split(" ");
		String commandParameter = "";
		if(commandParts.length > 0 ) {
		    commandString = commandParts[0];
		    if(commandParts.length > 1 ) {
		        for(int i = 1; i < commandParts.length; i ++ ) {
		            if(i > 1) {
		                commandParameter += " ";
		            }
		            commandParameter += commandParts[i];
		        }
		    }
		} else {
		    commandString = "";
		}
		
		
		Command command = (Command)commandList.getClosest(commandString);
		if(commandString.length() > 0 && command != null) {
		    //TrollAttack.print("command peace was " + command.isPeaceful() + " and player peace was " + TrollAttack.player.isFighting());
		    if(command.isPeaceful() && TrollAttack.player.isFighting()) {
		        TrollAttack.print("You can't do that while fighting!");
		    } else {
			    if(commandParts.length > 1) {
	
				        command.execute(commandParameter);
					
				} else {
					command.execute();
				}
		    }
		} else {
			TrollAttack.print("Huh?");
		}
		
	}
	// Commands
	class Consider extends Command {
	    public Consider( String s ) { super(s, false); }
	    public void execute() { TrollAttack.print("Consider whom?"); }
	    public void execute( String s ) {
	        Mobile mob = TrollAttack.gameRooms[ TrollAttack.player.getCurrentRoom()].getMobile( s );
	        if( mob == null) {
	            TrollAttack.print("You don't see that here.");
	        } else {
	            TrollAttack.print(mob.getShort() + ": " + mob.prompt());
	        }
	    }
	}
	class Kill extends Command {
		public Kill(String s) { super(s); }
		public void execute() { TrollAttack.print("Kill what?"); }
		public void execute(String s) {
			Mobile mob = TrollAttack.gameRooms[ TrollAttack.player.getCurrentRoom()].getMobile( s );
			if( mob == null) {
				TrollAttack.print("You don't see that here.");
			} else {
				Fight myFight = new Fight(TrollAttack.player, mob );
				myFight.start();

			}
		}
		
	}
	class Get extends Command {
		public Get(String s) { super(s, false);}
		public void execute() { TrollAttack.print("Get what?"); }
		public void execute(String command) {
			Item item = TrollAttack.gameRooms[	TrollAttack.player.getCurrentRoom()].removeItem(	command		);
			if(item == null) {
				TrollAttack.print("You can't find that here!");
			} else {
				TrollAttack.print("You get " + item.getShort() + ".");
				TrollAttack.player.addItem(item);
			}
		}
	}
	class Prompt extends Command {
	    public Prompt(String s) { super(s, false); }
	    public void execute() { TrollAttack.print("Current Prompt: " + TrollAttack.player.getPrompt()); }
	    public void execute(String command) {
	        TrollAttack.player.setPrompt(command);
	    }
	}
	class Drop extends Command {
		public Drop(String s) {super(s, false);}
		public void execute() {}
		public void execute(String command) {
			Item item = TrollAttack.player.dropItem( command );
			if(item == null) {
				TrollAttack.print("You don't have that!");
			} else {
				TrollAttack.print("You drop " + item.getShort() + ".");
				TrollAttack.gameRooms[ TrollAttack.player.getCurrentRoom()].addItem( item );
			}
		}
	}
	class Cast extends Command {
	    public Cast(String s) {super(s, false);}
	    public void execute() { TrollAttack.print("Cast what?"); }
	    public void execute(String s) {
	        sh.handleSpell( s );
	    }
	}
	class Look extends Command {
		public Look(String s) {super(s, false);}
		public void execute() {
			TrollAttack.look();
			
		}
		public void execute(String s) {
			this.execute();
		}
	}
	class Inventory extends Command {
		public Inventory(String s) { super(s, false); }
		public void execute() { TrollAttack.player.pInventory(); }
		public void execute(String s) { this.execute(); }
	}
	class Equipment extends Command {
		public Equipment(String s) { super(s, false); }
		public void execute() { TrollAttack.player.pEquipment(); }
		public void execute(String s) { this.execute(); }
	}
	class Wield extends Command {
		public Wield(String s) { super(s, false); }
		public void execute() {TrollAttack.print("Wield what?");}
		public void execute(String command) {
			TrollAttack.print( TrollAttack.player.wieldItem( command ) );
			
		}
	}
	class Help extends Command {
		public Help(String s) { super(s, false); }
		public void execute() {
			TrollAttack.print("Direction Commands:");
			TrollAttack.print("(E)ast, (W)est, (N)orth, (S)outh, (U)p, (D)own");
			TrollAttack.print("Game Commands:");
			TrollAttack.print("(L)ook, Quit/Exit");
		}
		public void execute(String s) { this.execute(); }
	}
	class Quit extends Command {
		public Quit(String s) { super(s); }
		public void execute() {
			TrollAttack.gameOver = true;
		}
		public void execute( String s ) { this.execute(); }
	}
}
