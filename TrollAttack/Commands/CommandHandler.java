package TrollAttack.Commands;
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
import TrollAttack.*;
import TrollAttack.Spells.*;


public class CommandHandler {
	LinkedList commandList;
	SpellHandler sh;
	Player player;
	public CommandHandler(Player p) {
		commandList = new LinkedList(false, 0);
		player = p;
		sh = new SpellHandler(player);
		
		//TrollAttack.print("Starting command registration...");
		registerCommand(new CommandMove(player, "north", CommandMove.NORTH));
		registerCommand(new CommandMove(player, "south", CommandMove.SOUTH));
		registerCommand(new CommandMove(player, "east", CommandMove.EAST));
		registerCommand(new CommandMove(player, "west", CommandMove.WEST));
		registerCommand(new CommandMove(player, "up", CommandMove.UP));
		registerCommand(new CommandMove(player, "down", CommandMove.DOWN));
		registerCommand(new CommandMove(player, "northeast", CommandMove.NORTHEAST));
		registerCommand(new CommandMove(player, "ne", CommandMove.NORTHEAST));
		registerCommand(new CommandMove(player, "northwest", CommandMove.NORTHWEST));
		registerCommand(new CommandMove(player, "nw", CommandMove.NORTHWEST));
		registerCommand(new CommandMove(player, "southeast", CommandMove.SOUTHEAST));
		registerCommand(new CommandMove(player, "se", CommandMove.SOUTHEAST));
		registerCommand(new CommandMove(player, "southwest", CommandMove.SOUTHWEST));
		registerCommand(new CommandMove(player, "sw", CommandMove.SOUTHWEST));
		registerCommand(new Kill("kill"));
		registerCommand(new Get("get"));
		registerCommand(new Drop("drop"));
		registerCommand(new Sacrafice("sacrafice"));
		registerCommand(new Favor("favor"));
		registerCommand(new Look("look"));
		registerCommand(new Cast("cast"));
		registerCommand(new Prompt("prompt"));
		registerCommand(new Trance("trance"));
		registerCommand(new Consider("consider"));
		registerCommand(new Inventory("inventory"));
		registerCommand(new Equipment("equiptment"));
		registerCommand(new Wear("wear"));
		registerCommand(new Level("level"));
		registerCommand(new Remove("remove"));
		registerCommand(new ChangeState("stand", 0));
		registerCommand(new ChangeState("sit", 1));
		registerCommand(new ChangeState("rest", 2));
		registerCommand(new ChangeState("sleep", 3));
		registerCommand(new Save("save"));
		registerCommand(new Load("load"));
		registerCommand(new Help("help"));
		registerCommand(new CommandList("commands"));
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
		    //TrollAttack.print("command peace was " + command.isPeaceful() + " and player peace was " + player.isFighting());
		    if(command.isPeaceful() && !player.isReady()) {
		        TrollAttack.print("You can't do that while " + player.getDoing() + "!");
		    } else {
			    if(commandParts.length > 1) {
	
				        command.execute(commandParameter);
					
				} else {
					command.execute();
				}
		    }
		} else {
			if(commandString.length() != 0) {
			    TrollAttack.print("Huh?");
			}
			
		}
		TrollAttack.print( player.prompt() );
	}
	// Commands
	class Consider extends Command {
	    public Consider( String s ) { super(s, false); }
	    public void execute() { TrollAttack.print("Consider whom?"); }
	    public void execute( String s ) {
	        Mobile mob = TrollAttack.gameRooms[ player.getCurrentRoom()].getMobile( s );
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
			Mobile mob = TrollAttack.gameRooms[ player.getCurrentRoom()].getMobile( s );
			if( mob == null) {
				TrollAttack.print("You don't see that here.");
			} else {
				Fight myFight = new Fight(player, mob );
				myFight.start();
				if(TrollAttack.isGameOver()) {
				    
				}
			}
		}
		
	}
	class Get extends Command {
		public Get(String s) { super(s, false);}
		public void execute() { TrollAttack.print("Get what?"); }
		public void execute(String command) {
			Item item = TrollAttack.gameRooms[	player.getCurrentRoom()].removeItem(	command		);
			if(item == null) {
				TrollAttack.print("You can't find that here!");
			} else {
				TrollAttack.print("You get " + item.getShort() + ".");
				player.addItem(item);
			}
		}
	}
	class Sacrafice extends Command {
	    public Sacrafice(String s) { super(s, false);}
	    public void execute() { TrollAttack.print("Sacrafice what?"); }
	    public void execute(String command) {
	      Item i = TrollAttack.gameRooms[ player.getCurrentRoom()].removeItem( command );
           if(i == null) {
               TrollAttack.print("You can't find that here.");
           } else {
               TrollAttack.print("You sacrafice " + i.getShort() + " to your deity.");
               player.increaseFavor((int)(Math.random() * 3 + 2));
           }
	    }
	}
	class ChangeState extends Command {
	    int newState;
	    public ChangeState(String s, int state){
	        super(s, false);
	        newState = state;
	    }
	    public void execute() {
	        if(player.isFighting()) {
	            TrollAttack.print("You can't do that while fighting!");
	        } else {
		        if(player.getState() == newState) {
		            TrollAttack.print("You are already " + player.getDoing() + "!");
		        } else {
		            
		            player.setState( newState );
		            TrollAttack.print("You are now " + player.getDoing() + ".");
		        }
	        }
	    }
	}
	class Favor extends Command {
	    public Favor(String s) { super(s, false); }
	    public void execute() {
	        TrollAttack.print("Your current favor: " + player.getFavor());
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	class Prompt extends Command {
	    public Prompt(String s) { super(s, false); }
	    public void execute() { TrollAttack.print("Current Prompt: " + player.getPrompt()); }
	    public void execute(String command) {
	        player.setPrompt(command);
	    }
	}
	class Drop extends Command {
		public Drop(String s) {super(s, false);}
		public void execute() {}
		public void execute(String command) {
			Item item = player.dropItem( command );
			if(item == null) {
				TrollAttack.print("You don't have that!");
			} else {
				TrollAttack.print("You drop " + item.getShort() + ".");
				TrollAttack.gameRooms[ player.getCurrentRoom()].addItem( item );
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
			player.look();
			
		}
		public void execute(String s) {
			this.execute();
		}
	}
	class Inventory extends Command {
		public Inventory(String s) { super(s, false); }
		public void execute() { player.pInventory(); }
		public void execute(String s) { this.execute(); }
	}
	class Equipment extends Command {
		public Equipment(String s) { super(s, false); }
		public void execute() { player.pEquipment(); }
		public void execute(String s) { this.execute(); }
	}
	class Trance extends Command {
	    public Trance(String s) { super(s, true); }
	    public void execute() {
	        TrollAttack.print("You enter a trance.");
	        player.increaseManaPoints ( 5 );
	        try {
	            Thread.sleep(4000);
	        } catch(Exception e) {
	            TrollAttack.print("You suck.");
	        }
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	class Wear extends Command {
		public Wear(String s) { super(s, false); }
		public void execute() {TrollAttack.print("Wear what?");}
		public void execute(String command) {
			TrollAttack.print( player.wearItem( command ) );
			
		}
	}
	class Remove extends Command {
	    public Remove(String s) {
	        super( s, false ); 
	    }
	    public void execute() {TrollAttack.print("Remove what?");}
	    public void execute(String command) {
	        TrollAttack.print( player.removeItem( command ) );
	    }
	}
	class Level extends Command {
	    public Level(String s) {
	        super( s, false );
	    }
	    public void execute() {
	        TrollAttack.print("Current Level: " + player.getLevel());
	        for(int i = player.getLevel() + 1; i < player.getLevel() + 7; i++ ) {
	            TrollAttack.print("Level " + i + ": " + Util.experienceLevel(i));
	        }
	    }
	    public void execute( String s) {
	        this.execute();
	    }
	}
	class CommandList extends Command {
	    public CommandList(String s) {
	        super( s, false );
	    }
	    public void execute() {
	        String list = "";
	        for(int i = 1;i <= commandList.length();i++) {
	            list += commandList.find(i) + " ";
	        }
	        TrollAttack.print( list ); 
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	class Save extends Command {
	    public Save(String s) { super(s, true); }
	    public void execute() {
	        Util.save(player);
	    }
	    public void Execute(String s) {
	        this.execute();
	    }
	}
	class Load extends Command {
	    public Load(String s) { super(s, false); }
	    public void execute() {
	        player = Util.load();
	    }
	    public void Execute(String s) {
	        this.execute();
	    }
	}
		
	class Help extends Command {
		public Help(String s) { super(s, false); }
		public void execute() {
			TrollAttack.print("Direction Commands:");
			TrollAttack.print("east, west, north, south, up, down");
			TrollAttack.print("Player Commands:");
			TrollAttack.print("kill, cast, get, drop, prompt, trance");
			TrollAttack.print("stand, sit, rest, sleep");
			TrollAttack.print("consider, inventory, equiptment, wear, remove");
			TrollAttack.print("Game Commands");
			TrollAttack.print("look, quit, exit, help");
		}
		public void execute(String s) { this.execute(); }
	}
	class Quit extends Command {
		public Quit(String s) { super(s); }
		public void execute() {
			TrollAttack.endGame();
		}
		public void execute( String s ) { this.execute(); }
	}
}
