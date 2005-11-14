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


import java.io.File;
import java.util.Hashtable;
import java.util.Set;

import TrollAttack.*;
import TrollAttack.Classes.Class;
import TrollAttack.Classes.AbilityData;
import TrollAttack.Items.*;


public class CommandHandler {
	LinkedList commandList;
	Build builder;
	Being player;
	public CommandHandler(Being p) {
		commandList = new LinkedList(false, 0);
		player = p;
		//player.tell("Starting command handler.");
		builder = new Build(player);
		//player.tell("Starting command registration...");
		registerCommand(new CommandMove(player, "north", Exit.NORTH));
		registerCommand(new CommandMove(player, "south", Exit.SOUTH));
		registerCommand(new CommandMove(player, "east", Exit.EAST));
		registerCommand(new CommandMove(player, "west", Exit.WEST));
		registerCommand(new CommandMove(player, "up", Exit.UP));
		registerCommand(new CommandMove(player, "down", Exit.DOWN));
		registerCommand(new CommandMove(player, "northeast", Exit.NORTHEAST));
		registerCommand(new CommandMove(player, "ne", Exit.NORTHEAST));
		registerCommand(new CommandMove(player, "northwest", Exit.NORTHWEST));
		registerCommand(new CommandMove(player, "nw", Exit.NORTHWEST));
		registerCommand(new CommandMove(player, "southeast", Exit.SOUTHEAST));
		registerCommand(new CommandMove(player, "se", Exit.SOUTHEAST));
		registerCommand(new CommandMove(player, "southwest", Exit.SOUTHWEST));
		registerCommand(new CommandMove(player, "sw", Exit.SOUTHWEST));
		registerCommand(new Kill("kill"));
		registerCommand(new Get("get"));
		registerCommand(new Give("give"));
		registerCommand(new Drop("drop"));
		registerCommand(new Sacrafice("sacrafice"));
		registerCommand(new Favor("favor"));
		registerCommand(new Look("look"));
		registerCommand(new Cast("cast"));
		//registerCommand(new Name("name"));
		registerCommand(new Password("password"));
		registerCommand(new Prompt("prompt"));
		registerCommand(new Trance("trance"));
		registerCommand(new Consider("consider"));
		registerCommand(new Inventory("inventory"));
		registerCommand(new Equipment("equiptment"));
		registerCommand(new Wear("wear"));
		registerCommand(new Level("level"));
		registerCommand(new Gold("gold"));
		registerCommand(new Eat("eat"));
		registerCommand(new Drink("drink"));
		registerCommand(new Fill("fill"));
		registerCommand(new Score("score"));
		registerCommand(new Remove("remove"));
        registerCommand(new Follow("follow"));
		registerCommand(new Say("say"));
        registerCommand(new Chat("chat"));
        registerCommand(new Chat("ooc"));
        registerCommand(new Tell("tell"));
		registerCommand(new Emote("emote"));
		registerCommand(new Title("title"));
        registerCommand(new Practice("practice"));
		registerCommand(new ChangeState("wake", 0));
		registerCommand(new ChangeState("stand", 0));
		registerCommand(new ChangeState("sit", 1));
		registerCommand(new ChangeState("rest", 2));
		registerCommand(new ChangeState("sleep", 3));
		registerCommand(new Puke("puke"));
		registerCommand(new Open("open"));
		registerCommand(new Close("close"));
		registerCommand(new Who("who"));
		registerCommand(new Where("where"));
		registerCommand(new Save("save"));
		
		registerCommand(new Help("help"));
		registerCommand(new CommandList("commands"));
		registerCommand(new Help("?"));
		registerCommand(new CRoll("roll"));
		
		//registerCommand(new Debug1("debug1"));
		//registerCommand(new Debug2("debug2"));
		
		registerCommand(new Quit("quit"));
		registerCommand(new Quit("exit"));
        

        
		/* Immortal Commands */
		if(player.level > 55 || player.isMobile()) {
		    registerCommand(new vAssign("vassign"));
		    registerCommand(new reloadWorld("reloadworld"));
		    registerCommand(new freeze("freeze"));
		    registerCommand(new unfreeze("unfreeze"));
		    registerCommand(new UnQuit("unquit"));
		    registerCommand(new Switch("switch"));
            registerCommand(new sSet("sset"));
            registerCommand(new cSet("cset"));
            registerCommand(new cCreate("ccreate"));
            registerCommand(new cStat("cstat"));

		}

		
		/* Builder Commands */
		if(player.isBuilder() || player.level > 60) {
		    
		    /* Room Building Commands */
		    registerCommand(new Force("force"));
		    registerCommand(new Goto("goto"));
		    registerCommand(new Slay("slay"));
		    registerCommand(new rList("rlist"));
		    registerCommand(new Click("resetworld"));
		    registerCommand(new resetList("resets"));
		    registerCommand(new mList("mlist"));
		    registerCommand(new iList("ilist"));
		    registerCommand(new aList("alist"));
		    registerCommand(new myArea("area"));
		    //registerCommand(new rCreate("rcreate"));
		    registerCommand(new mCreate("mcreate"));
		    registerCommand(new iCreate("icreate"));
		    registerCommand(new mSet("mset"));
		    registerCommand(new iStat("istat"));
		    registerCommand(new iSet("iset"));
		    registerCommand(new mStat("mstat"));
		    registerCommand(new rEdit( "redit"));
			
			registerCommand(new Savearea("savearea"));
			registerCommand(new InvokeItem("iinvoke"));
			registerCommand(new InvokeMobile("minvoke"));
		}
        if(player.getBeingClass() != null) {
            registerAbilityList(player.getAbilityList());
        }
        if(player.level >= 65) {
            registerCommand(new Shutdown("shutdown"));
        }
		
	}

	public void registerAbilityList(Set<Ability> abilityList) {
        for(Ability ability : abilityList) {
            if(!ability.isSpell()) {
                registerCommand(ability);
            }
        }
        
    }

    private void registerCommand( Command c) {
		/*
		 if(c == null) {
		    player.tell("c is null!");
		} else {
		    player.tell("registering " + c.name);
		}*/
	    commandList.add(c);
		 
	}
	public String handleCommand( String commandString) {
	    player.setLastActive(TrollAttack.getTime());
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
		    //player.tell("command peace was " + command.isPeaceful() + " and player peace was " + player.isFighting());
		    if(command.isPeaceful() && !player.isReady()) {
		        player.tell("You can't do that while " + player.getDoing() + "!");
		    } else {
			    if(commandParts.length > 1) {
	
				        try {
				            if(command.needsPlayer) {
				                command.execute(player, commandParameter);
                            } else {
                                command.execute(commandParameter);
                            }
				        } catch(Exception e) {
				            e.printStackTrace();
				        }
					
				} else {
				    try {
                        if(command.needsPlayer) {
                            command.execute(player);
                        } else {
                            command.execute();
                        }
				    } catch(Exception e) {
			            e.printStackTrace();
			        }
				}
		    }
		} else {
			if(commandString.length() != 0) {
			    player.tell("Huh?");
			}
			
		}
		player.prompt();
		if(command == null) {
		    return null;
		} else {
		    return command.name;
		}
	}
	
    // Commands
	class Consider extends Command {
	    public Consider( String s ) { super(s, false); }
	    public boolean execute() { 
            player.tell("Consider whom?"); 
            return false;
        }
	    public boolean execute( String s ) {
	        Being mob = player.getActualRoom().getBeing( s, null );
	        if( mob == null) {
	            player.tell("You don't see that here.");
                return false;
	        } else {
	            player.tell(mob.getShort() + ": " + mob.getPrompt());
	        }
            return true;
            
	    }
	}
	class Kill extends Command {
		public Kill(String s) { super(s, true); }
		public boolean execute() { 
            player.tell("Kill what?"); 
            return false;
        }
		public boolean execute(String s) {
			//TrollAttack.message("Running the kill command with '" + s + "' for a string.");
		    Being mob = player.getActualRoom().getBeing( s, player );
		    //TrollAttack.message("got here... 145");
			if( mob == null) {
				player.tell("You don't see that here.");
               return false;
			} else {
				//TrollAttack.message("Starting a fight between " + player.getShort() + " and " + mob.getShort());
			    Fight myFight = new Fight(player, mob );
				myFight.start();
			}
            return true;
		}
		
	}
	class Slay extends Command {
	    public Slay(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: slay <being>");
            return false;
	    }
	    public boolean execute(String s) {
	        Being mob = player.getActualRoom().getBeing( s, true, player );
	        if( mob == null) {
				player.tell("You don't see that here.");
                return false;
			} else {
			    player.tell("You slay " + mob.getShort() + " in cold blood.");
			    Being[] pBroadcast = {player, player, mob};
			    player.getActualRoom().say(Communication.RED + "%1 slays %2 in cold blood.", pBroadcast);
			}
            return true;
	    }
	}
	class Get extends Command {
		public Get(String s) { super(s, false);}
		public boolean execute() { 
            player.tell("Get what?"); 
            return false;
        }
        
		public boolean execute(String command) {
		    String[] parts = command.split(" ");
		    int count = 0;
		    if(parts[0].compareToIgnoreCase("all") == 0) {
		        count = -1;
                if(parts.length == 1) {
                    command = "";
                } else {
                    command = parts[1];
                }
		    } else {
		        count = 1;
		    }
	       
		    Item item = null;
		    while(count-- != 0 && item == null) {
		        item = player.getActualRoom().removeItem(	command		);
		    
				if(item == null) {
					if(count == 0) {
					    player.tell("You can't find that here!");
					} else {
					    return false;
					}
				} else {
					player.tell("You get " + item.getShort() + ".");
					player.roomSay(player.getShort() + " gets " + item.getShort() + ".");
					player.addItem(item);
					item = null;
				}
		    }
            return true;
		}
	}
	class Give extends Command {
	    public Give(String s) { super(s, false);}
	    public boolean execute() {
	        player.tell("Usage: give <item> <being>");
            return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        Item gift = player.getItem(parts[0], true);
	        if(gift == null) {
	            player.tell("You don't have that!");
                return false;
	        } else {
	            Being victim = player.getActualRoom().getBeing(parts[1], player);
	            if(victim == null) {
	                player.tell("They aren't here!");
	                player.addItem(gift);
                    return false;
	            } else {
	                victim.addItem(gift);
	                player.tell("You give " + gift.getShort() + " to " + victim.getShort(player) + ".");
	                Being[] pBroadcast = new Being[3];
	                pBroadcast[0] = pBroadcast[1] = player;
	        		pBroadcast[2] = victim;
	        		player.getActualRoom().say("%1 gives %2 " + gift.getShort() + ".", pBroadcast);
	            }
	        }
            return true;
	    }
	}
	class Sacrafice extends Command {
	    public Sacrafice(String s) { super(s, false);}
	    public boolean execute() { 
            player.tell("Sacrafice what?"); 
            return false;
        }
	    public boolean execute(String command) {
	      Item i = player.getActualRoom().removeItem( command );
           if(i == null) {
               player.tell("You can't find that here.");
               return false;
           } else {
               player.tell("You sacrafice " + i.getShort() + " to your deity and receive one gold coin.");
               player.gold++;
               player.roomSay(i.getShort() + " is sacrificed to " + player.getShort() + "'s deity.");
               player.increaseFavor((int)(Math.random() * 3 + 2));
           }
           return true;
	    }
	}
	class ChangeState extends Command {
	    int newState;
	    public ChangeState(String s, int state){
	        super(s, false);
	        newState = state;
	    }
	    public boolean execute() {
	        if(player.isFighting()) {
	            player.tell("You can't do that while fighting!");
               return false;
	        } else {
		        if(player.getState() == newState) {
		            player.tell("You are already " + player.getDoing() + "!");
                   return false;
		        } else {
		            
		            player.setState( newState );
		            player.tell("You are now " + player.getDoing() + ".");
		            player.roomSay(player.getShort() + " is now " + player.getDoing() + ".");		        }
	        }
            return true;
	    }
	}
	class Favor extends Command {
	    public Favor(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Your current favor: " + player.getFavor());
            return true;
	    }
	}
	class Prompt extends Command {
	    public Prompt(String s) { super(s, false); }
	    public boolean execute() { 
            player.tell("Current Prompt: " + player.getPromptString()); 
            return false;
        }
	    public boolean execute(String command) {
	        player.setPrompt(command);
            return true;
	    }
	}
	class Drop extends Command {
		public Drop(String s) {super(s, false);}
		public boolean execute() {
		    player.tell("Usage: drop <item>");
            return false;
        }
		public boolean execute(String command) {
		    if(command.compareToIgnoreCase("all") == 0) {
		        player.dropAll();
		        player.tell("You drop everything you have.");
		        player.roomSay("%1 drops everything they have.");
		        return true;
		    }
		    
		    String[] parts = command.split(" ");
		    Item item;
		    if(parts.length == 2 && ( parts[1].compareToIgnoreCase("gold") == 0 || parts[1].startsWith("coin"))) {
		        int amount = Util.intize(player, parts[0]);
		        if(player.level > 60 && player.gold < amount) {
		            player.gold = amount;
		        }
		        if(player.gold >= amount) {
		            item = player.dropGold(amount);
		        } else {
		            player.tell("You don't have that much!");
		            return false;
		        }
		    } else {
		        item = player.dropItem( command );
		    }
			if(item == null) {
				player.tell("You don't have that!");
               return false;
				
			} else {
				player.tell("You drop " + item.getShort() + ".");
				player.roomSay(player.getShort() + " drops " + item.getShort() + ".");
				player.getActualRoom().addItem( item );
			}
            return true;
		}
	}
	class Cast extends Command {
	    public Cast(String s) {super(s, false);}
	    public boolean execute() { 
            player.tell("Cast what?"); 
            return false;
        }
	    public boolean execute(String spellName) {
	        try {
	            handleSpell( player, spellName );
	        } catch(Exception e) {
	            e.printStackTrace();
                return false;
	        }
            return true;
	    }
	}
	class Look extends Command {
		public Look(String s) {super(s, false);}
		public boolean execute() {
		    if(player.state > 2) {
			    player.tell("You can't do that while sleeping!");
               return false;
			} else {
			    player.look();
			}
            return true;
			
		}
		public boolean execute(String s) {
			Being being = player.getActualRoom().getBeing(s, player);
			if(being == null) {
			    player.tell("You don't see that here.");
               return false;
			} else if(player.state > 2) {
			    player.tell("You can't do that while sleeping!");
               return false;
			} else {
				player.tell(being.showBeing());
			}
            return true;
		}
	}
	class Inventory extends Command {
		public Inventory(String s) { super(s, false); }
		public boolean execute() { 
            player.tell("Your " + player.getInventory(), false);
            return true;
        }
	}
	class Equipment extends Command {
		public Equipment(String s) { super(s, false); }
		public boolean execute() { 
            player.tell("Your " + player.getEquipment(), false); 
            return true;
        }
	}
	class Trance extends Command {
	    public Trance(String s) { super(s, true); }
	    public boolean execute() {
	        player.tell("You enter a trance.");
	        player.increaseManaPoints ( 5 );
	        try {
	            Thread.sleep(4000);
	        } catch(Exception e) {
	            player.tell("You suck.");
	        }
            return true;
	    }
	}
	class Wear extends Command {
		public Wear(String s) { super(s, false); }
		public boolean execute() {
            player.tell("Wear what?");
            return false;
        }
		public boolean execute(String command) {
			Item newWear = player.findItem(command);
			if(newWear == null) {
			    player.tell("You don't have that!");
               return false;
			} else {
			    player.tell( player.wearItem( newWear ) );
                player.roomSay("%1 wears something.");
			}
            return true;
			
		}
	}
	class Eat extends Command {
	    public Eat(String s) { super(s, false); }
	    public boolean execute() {
            player.tell("Eat what?");
            return false;
        }
	    public boolean execute(String command) {
	        Item newEat = player.findItem(command);
	        if(newEat == null) {
	            player.tell("You don't have that!");
               return false;
	        } else {
	            player.tell( player.eatItem( newEat ) );
	        }
            return true;
	    }
	}
	class Drink extends Command {
	    public Drink(String s) { super(s, false); }
	    public boolean execute() { 
            player.tell("Drink what?");
            return false;
        }
	    public boolean execute(String command) {
	        Item newDrink = player.findItem(command);
	        if(newDrink == null) {
	            newDrink = player.getActualRoom().findItem(command);
	        }
	        if(newDrink == null) {
	            player.tell("You can't find that!");
	            return false;
	        } else {
	            player.tell( player.drinkItem( newDrink ) );
	        }
            return true;
	    }
	}
	class Fill extends Command {
	    public Fill(String s) { super(s, false); }
	    public boolean execute() { 
            player.tell("Usage: fill <container> <source>");
            return false;
        }
	    public boolean execute(String command) {
	        String[] parts = command.split(" ");
	        if(parts.length < 2) {
	            return execute();
	        }
	        Item container = player.findItem(parts[0]);
	        Item fountain = player.getActualRoom().findItem(parts[1]);
	        DrinkContainer newContainer;
	        Fountain newFountain;
	        if(container == null) {
	            player.tell("You don't have that!");
	            return false;
	        }
            try {
	            newContainer = (DrinkContainer)container;
	            if(newContainer.getCapacity() == newContainer.getVolume()) {
	                player.tell(newContainer.getShort() + " is already full!");
	                return false;
	            }
	        } catch(ClassCastException e) {
	            player.tell(Util.uppercaseFirst(container.getShort()) + " isn't a container!");
	            return false;
	        }
	        if(fountain == null) {
	            player.tell("You can't find that!");
	            return false;
	        }
            try {
	            newFountain = (Fountain)fountain;
	        } catch(ClassCastException e) {
	            player.tell(Util.uppercaseFirst(container.getShort()) + " isn't a water source!");
	            return false;
	        }
	        
	        newContainer.setVolume(newContainer.getCapacity());
	        player.tell("You fill " + newContainer.getShort() + " from " + newFountain.getShort());
	        player.roomSay("%1 fills " + newContainer.getShort() + " from " + newFountain.getShort());
            return true;
	    }
	}
	class Remove extends Command {
	    public Remove(String s) {
	        super( s, false ); 
	    }
	    public boolean execute() {
            player.tell("Remove what?");
            return false;
        }
	    public boolean execute(String command) {
	        
            //@TODO! make the command do the work here, so we
            // can get a better return value.
            player.tell( player.removeItem( command ) );
            return true;
	    }
	}
    class Follow extends Command {
        public Follow(String s) { super(s, false); }
        public boolean execute() {
            player.tell("usage: follow <being>");
            player.tell("To stop following someone, type 'follow self'");
            if(player.getFollowing() != null) {
                player.tell("You are currently following " + player.getFollowing().getShort() + ".");
            }
            return false;
        }
        public boolean execute(String s) {
            String[] parts = Util.split(s);
            if(parts.length != 1) {
                return execute();
            }
            Being victim = player.getActualRoom().getBeing(parts[0], player);
            if(victim == null) {
                player.tell("You don't see that being here.");
                return false;
            }
            if(victim == player) {
                if(player.getFollowing() == null ) {
                    player.tell("You aren't following anyone.");
                    return false;
                } else {
                    player.stopFollowing();
                }
            } else {
                player.startFollowing(victim);
            }
            return true;
        }
    }
	class CRoll extends Command {
	    public CRoll(String s) { super( s, false); }
	    public boolean execute() {
	        player.tell("Roll what?");
            return false;
	    }
	    public boolean execute(String s) {
	        try {
	            Roll roller = new Roll(s);
	        	player.tell("You rolled '" + s + "' to get " + roller.roll());
                
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
            return true;
	    }
	}
	class Gold extends Command {
	    public Gold(String s) {
	        super( s, false );
	    }
	    public boolean execute() {
	        player.tell("You have " + player.gold + " gold coins.");
            return true;
	    }
	}
	class Score extends Command {
	    public Score(String s) { super( s , false); }
	    public boolean execute() {
	        player.score();
            return true;
	    }
	}
	class Level extends Command {
	    public Level(String s) {
	        super( s, false );
	    }
	    public boolean execute() {
	        player.tell("Current Level: " + player.getLevel());
	        for(int i = player.getLevel() + 1; i < player.getLevel() + 7; i++ ) {
	            player.tell("Level " + i + ": " + Util.experienceLevel(i));
	        }
            return true;
	    }
	}
	class CommandList extends Command {
	    public CommandList(String s) {
	        super( s, false );
	    }
	    public boolean execute() {
	        String list = "";
	        for(int i = 1;i <= commandList.length();i++) {
	            list += commandList.find(i) + " ";
	        }
	        player.tell( list );
            return true;
	    }
	}
	class Say extends Command {
	    public Say(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Say what?");
            return false;
	    }
	    public boolean execute(String phrase) {
	        player.tell(Communication.CYAN + "You say, \"" + phrase + "\".");
	        player.roomSay(Communication.CYAN + player.getShort() + " says, \"" + phrase + "\".");
            return true;
	    }
	}
    class Chat extends Command {
        public Chat(String s) { super(s, false); }
        public boolean execute() {
            player.tell("Chat what?");
            return false;
        }
        public boolean execute(String s) {
            for(Player p : TrollAttack.gamePlayers) {
                if(p == player) {
                    player.tell(Communication.CYAN + "You chat, \"" + s + "\".");
                } else {
                    p.interrupt(Communication.CYAN + player.getShort() + " chats, \"" + s + "\".");
                }
                
            }
            return true;
        }
    }
    class Tell extends Command {
        public Tell(String s) { super(s, false); }
        public boolean execute() {
            player.tell("Usage: tell <player> <message>");
            return false;
        }
        public boolean execute(String s) {
            String[] parts = s.split(" ");
            if(parts.length < 2) {
                return execute();
            }
            String message = parts[1];
            for(int i = 2;i<parts.length;i++) {
                message += " " + parts[i];
            }
            Player victim = null;
            for(Player p : TrollAttack.gamePlayers) {
                if(p.getShort().toLowerCase().startsWith(parts[0].toLowerCase())) {
                    victim = p;
                    p.interrupt(Communication.WHITE + player.getShort() + " tells you, \"" + message + "\".");
                    break;
                } else {
                    
                }
                
            }
            if(victim == null) {
                player.tell("He/She isn't online right now.");
            }
            player.tell(Communication.WHITE + "You tell " + victim.getShort() + ", \"" + s + "\".");
            return true;
        }
    }
	class Open extends Command {
	    public Open(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Open what?");
            return false;
	    }
	    public boolean execute(String s) {
            return player.open(s);
	    }
	}
	class Close extends Command {
	    public Close(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Close what?");
            return false;
	    }
	    public boolean execute(String s) {
            return player.close(s);
	    }
	}
	class Title extends Command {
	    public Title(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: title <new title>");
            return false;
	    }
	    public boolean execute(String s) {
	        player.setTitle(s);
	        player.tell("Title changed to '" + s + "'");
            return true;
	    }
	}
    class sSet extends Command {
        public sSet(String s) { super(s,false); }
        public boolean execute() {
            player.tell("Usage: sset <ability> <proficiency>");
            player.tell("       sset all 100");
            player.tell("       sset scan 80");
            return false;
        }
        public boolean execute(String command) {
            String[] parts = command.split(" ");
            if(parts.length != 2) {
                return execute();
            }
            if(parts[0].compareToIgnoreCase("all") == 0) {
                for(Ability ability : TrollAttack.abilityHandler.getList()) {    
                    player.practice(ability, new Float(parts[1]), false);
                }
            } else {
                Ability ability = TrollAttack.abilityHandler.find(parts[0]);
                player.practice(ability, new Float(parts[1]), false);
            }
            player.rehash();
            return true;
        }
    }
    class Practice extends Command {
        public Practice(String s) { super(s,true); }
        public boolean execute() {
            if(player.getBeingClass() == null) {
                player.tell("Unclassed players can't learn skills.");
                return false;
            }
            
            // SKILLS
            player.tell(Communication.BLUE +    "--------------" +
                    Communication.CYAN + "Skills" +
                    Communication.BLUE +        "--------------");
            
            Set<Ability> classAbilties = player.getBeingClass().getAbilityData().keySet();
            
            Set<Ability> beingAbilities = player.getAbilitiesData().keySet();
            java.util.LinkedList<Ability> list = new java.util.LinkedList<Ability>();
            for(Ability ability : classAbilties) {
                list.add(ability);
            }
            for(Ability ability : beingAbilities) {
                if(!list.contains(ability)) {
                    list.add(ability);
                }
            }
            int count = 0;
            for(Ability ability : list) {
                AbilityData data = player.getBeingClass().getAbilityData().get(ability);
                if( (data == null || data.level <= player.level || player.level > 60 ) && !ability.isSpell()) {
                    player.tell(Communication.GREEN + ability.name + "\t\t" + player.getProficiency(ability));
                    count++;
                }
            }
            if(count == 0 ) {
                player.tell("  " + Communication.BLUE + "(none)");
            }
            
            // SPELLS
            player.tell(Communication.BLUE +    "--------------" +
                    Communication.CYAN + "Spells" +
                    Communication.BLUE +        "--------------");
            
            count = 0;
            for(Ability ability : list) {
                AbilityData data = player.getBeingClass().getAbilityData().get(ability);
                if( (data == null || data.level <= player.level || player.level > 60 ) && ability.isSpell()) {
                    player.tell(Communication.GREEN + ability.name + "\t\t" + player.getProficiency(ability));
                    count++;
                }
            }
            if(count == 0 ) {
                player.tell("  " + Communication.BLUE + "(none)");
            }
            return false;
            
        }
        public boolean execute(String command) {
            if(player.getBeingClass() == null) {
                return execute();
            }
            Being trainer = null;
            java.util.LinkedList<Being> beings = player.getActualRoom().getRoomBeings();
            for(Being being : beings) {
                if(being.canTeach) {
                    trainer = being;
                    break;
                }
            }
            if(trainer == null) {
                player.tell("There is no one here to teach you.");
                return false;
            }
            Hashtable<Ability, AbilityData> data = player.getBeingClass().getAbilityData();
            Set<Ability> list = data.keySet();
            for(Ability ability : list) {
                if(data.get(ability).level <= player.level && ability.name.startsWith(command)) {
                    if(player.getProficiency(ability) > 0) {
                        trainer.ch.handleCommand("say I'm sorry " + player.getShort() + ", but there is nothing more I can teach you about this.");
                        return false;
                    } else {
                        player.practice(ability);
                        player.tell("You learn " + ability.name + ".");
                        trainer.ch.handleCommand("say I have taught you everything I can about this " + player.getShort() + ", use this knowledge wisely.");
                        player.rehash();
                        return true;
                    }
                }
            }
            player.tell("You aren't ready to learn that yet.");
            return false;
        }
    }
	class Puke extends Command {
	    public Puke(String s) { super(s, false); }
	    public boolean execute() {
	        if(player.thirst > 8 && player.hunger > 8) {
	            player.tell("You have nothing left in your stomach to puke up!");
	            return false;
	        } else if(player.hunger > 8) {
	            player.tell("You shove your fingers down your throat and puke up some of the remaining drink from your stomach.");
	            player.thirst++;
	        } else if(player.thirst > 8) {
	            player.tell("You shove your fingers down your throat and throw up some of the food you have eaten.");
	            player.hunger++;
	        } else { 
	            player.tell("You shove your fingers down your throat and puke up some of the food and drink that you have eaten.");
	            player.thirst++;
	            player.hunger++;
	        }
	        
            return true;
	    }
	}
	class Who extends Command {
	    public Who(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell(Communication.CYAN + "Current Players:");
	        for(Player p  : TrollAttack.gamePlayers) {
	            player.tell(Communication.GREEN + p.level+ "\t" + p.getName() + " " + p.title);
	        }
            return true;
	       
	        
	    }
	    
	}
	class Where extends Command {
	    public Where(String s) { super(s, false); }
	    public boolean execute() {
	        Area myArea = player.getActualArea();
	        player.tell(Communication.CYAN + "Current Players in " + player.getActualArea().name + ":");
	        for(Player p : TrollAttack.gamePlayers) {
	            if(p.getActualArea() == myArea) {
	                player.tell(Communication.GREEN + p.level+ "\t" + p.getName() + " " + p.title);
	            }
	        }
            return true;
	    }
	}
	class Emote extends Command {
	    public Emote(String s) { super(s, true); }
	    public boolean execute() { 
            player.tell("Emote what?"); 
            return false;
        }
	    public boolean execute(String message) {
	        //TrollAttack.message("Emoting " + message + ".");
            if(!message.endsWith(".") && !message.endsWith("!") && !message.endsWith("?")) {
                message = message + ".";
            }
            message = Communication.DARKCYAN + player.getShort() + " " + message;
            player.tell(message);
	        player.getActualRoom().say(message, player);
            return true;
	    }
	}
	class Name extends Command {
	    public Name(String s) { super(s, true); }
	    public boolean execute() {
	        player.tell("Your name is " + player.getName() + ".");
            return false;
	    }
	    public boolean execute(String phrase) {
	        player.tell("You name yourself " + phrase + ".");
	        player.name(phrase);
            return true;
	    }
	}
	class Password extends Command {
	    public Password(String s) { super(s, true); }
	    public boolean execute() {
	        player.tell("Usage: password <old password> <new password>");
            return false;
	    }
	    public boolean execute(String phrase) {
	        String[] parts = phrase.split(" ");
	        if(parts.length > 1 && player.checkPassword(parts[0]) ) {
	            player.setPassword(parts[1]);
	            player.tell("Your password has been changed.");
	        } else {
	            player.tell("Incorrect old password.");
                return false;
	        }
            return true;
	    }
	}
	class Save extends Command {
	    public Save(String s) { super(s, false); }
	    public boolean execute() {
	        player.save();
	        player.tell("You save your progress.");
            return true;
	    }
	}
	/*class Debug1 extends Command {
	    public Debug1(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Testing\rwith\rr's.");
	    }
	}
	class Debug2 extends Command {
	    public Debug2(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Testing\nwith\nn's.");
	    }
	}
	class Load extends Command {
	    public Load(String s) { super(s, false); }
	    public boolean execute() {
	        //player = Util.readPlayerData();
	        player.tell("Load which player?");
	    }
	    public boolean execute(String s) {
	        //Communication tmp = player.getCommunication();
	        player.tell("You can't switch players!");
	        
	        
	    }
	}*/
	class Help extends Command {
		public Help(String s) { super(s, false); }
		public boolean execute() {
			player.tell("Direction Commands:");
			player.tell("east, west, north, south, up, down");
			player.tell("Player Commands:");
			player.tell("kill, cast, get, drop, prompt, trance");
			player.tell("stand, sit, rest, sleep");
			player.tell("consider, inventory, equiptment, wear, remove");
			player.tell("Game Commands");
			player.tell("look, quit, exit, help");
            return true;
		}
	}
	/*class Transport extends Command {
	    public Goto(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("You are currently in room " + player.getCurrentRoom() + ".");
	    }
	    public boolean execute( String s ) {
	       int room;
	       int oldRoom = 1;
	        try {
	           room = Util.intize(null, s);
	           if(room == Integer.MIN_VALUE) {
	               Player p = TrollAttack.getPlayer(s);
	               if(p == null) {
	                   player.tell("This isn't a valid vnum or player name.");
	                   return;
	               } else {
	                   room = p.getCurrentRoom();
	               }
	           }
	           
	           oldRoom = player.getCurrentRoom();
	           player.getActualRoom().removePlayer(player);
	           player.setCurrentRoom(room);
	           if(player.getActualRoom() == null) {
	               if(player.canEdit(room)) {
	                   player.tell("Waving your hand, you form order from swirling chaos, and step into a new reality...");
	               
		               Room newRoom = new Room(
		    	               room,
		    	               "A Freshly Created Room",
		    	               "Change the title of this room by typing \"rtitle <new title>\".   Enter the description of this room by typing \"rdescription <description>\".",
		    	               0,0,0,0,0,0,0,0,0,0);
		    	        //player.tell("You have create room " + s + ", type \"goto " + s + "\" to see your new room.");
		    	        TrollAttack.gameRooms.add(newRoom);
		    	        Area.testRoom(newRoom).rooms.add(newRoom);
		    	        player.setCurrentRoom(room);
	               } else {
	                   player.setCurrentRoom(oldRoom);
	               }
	           }
	           player.getActualRoom().addPlayer(player);
	           player.look();
	       } catch(Exception e) {
	           player.tell("Problem changing rooms!");
	           e.printStackTrace();
	       }
	       
	    }
	}*/
	
	/* Builder Commands */
	class Goto extends Command {
	    public Goto(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("You are currently in room " + player.getCurrentRoom() + ".");
            return true;
	    }
	    public boolean execute( String s ) {
	       int room;
	       int oldRoom = 1;
	        try {
	           try {
	               room = Util.intize(null, s);
	           } catch(NumberFormatException e) {
	               Player p = TrollAttack.getPlayer(s);
	               if(p == null) {
	                   player.tell("This isn't a valid vnum or player name.");
	                   return false;
	               } else {
	                   room = p.getCurrentRoom();
	               }
	           }
	           
	           oldRoom = player.getCurrentRoom();
	           player.getActualRoom().removeBeing(player);
               player.getActualRoom().say(player.getShort() + " disappears in a whirl of smoke.");
	           Room newRoom = TrollAttack.getRoom(room);
	           if(newRoom == null) {
	               if(player.canEdit(room)) {
	                   player.tell("Waving your hand, you form order from swirling chaos, and step into a new reality...");
	               
		               newRoom = new Room(
		    	               room,
		    	               "A Freshly Created Room",
		    	               "Change the title of this room by typing \"redit title <new title>\".   Enter the description of this room by typing \"redit desc <description>\".",
		    	               new java.util.LinkedList<Exit>());
		    	        //player.tell("You have create room " + s + ", type \"goto " + s + "\" to see your new room.");
		    	        TrollAttack.gameRooms.add(newRoom);
		    	        Area.test(room, TrollAttack.gameAreas).areaRooms.add(newRoom);
		    	        player.setCurrentRoom(newRoom);
	               } else {
	                   player.setCurrentRoom(oldRoom);
                       player.tell("You don't have permission to goto that room!");
	               }
	           } else {
	               player.setCurrentRoom(newRoom);
               }
	           player.getActualRoom().addBeing(player);
	           player.look();
	       } catch(Exception e) {
	           player.tell("Problem changing rooms (invalid vnum)!");
	           e.printStackTrace();
               return false;
	       }
           player.getActualRoom().say(player.getShort() + " arrives in a whirl of smoke.", player);
           return true;
	       
	    }
	}
	class freeze extends Command {
	    public freeze(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: freeze <area filename>");
            return false;
	    }

	    public boolean execute(String s) {
	        for(Area area : TrollAttack.gameAreas ) {
	            if(area.filename.compareToIgnoreCase(s) == 0) {
	                area.frozen = true;
	                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
	                TrollAttack.reloadWorld();
	                player.tell("You halt the motion of the atoms in " + area.name + ".");
	                break;
	            }
	        }
	        return true;
	    }

	}
	class unfreeze extends Command {
	    public unfreeze(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: unfreeze <area filename>");
            return false;
	    }
	    public boolean execute(String s) {
	        for( Area area : TrollAttack.gameAreas) {
	            if(area.filename.compareToIgnoreCase(s) == 0) {
	                area.frozen = false;
	                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
	                TrollAttack.reloadWorld();
	                player.tell("You set the atoms of " + area.name + " spinning."); 
	                break;
	            }
	        }
	        return true;
	    }
	}
	class aList extends Command {
	    public aList(String s) { super(s, false); }
	    public boolean execute() {
		    player.tell(Communication.GREEN + "Game Areas:");
		    player.tell(Communication.CYAN + "Filename\t\tLowVnum\tHighVnum\tFrozen\tName" + Communication.WHITE);
		    for(Area area : TrollAttack.gameAreas) {
		        player.tell(area.filename + (area.filename.length() < 14 ? "\t" : "") + "\t" + area.low+"\t"+area.high + "\t\t" + "(" + (area.frozen ? "X" : " ") + ")\t" + area.name);
		    }
            return true;
		}
	}
	class iList extends Command {
	    public iList(String s) { super(s, false); }
	    public boolean execute() {
		    player.tell(Communication.GREEN +"Items in the VNUM range of this area:");
		    player.tell(Communication.CYAN + "VNUM\tName\t\tShortDesc" + Communication.WHITE);
		    Item item;
		    int high, low;
		    high = player.getActualArea().high;
		    low = player.getActualArea().low;
		    while(TrollAttack.gameItems.itemsRemain()) {
		        item = (Item)TrollAttack.gameItems.getNext();
		        if(item.vnum <= high && item.vnum >= low) {
		            player.tell(item.vnum + "\t" + item.name + "\t" + item.getShort());
		        }
		    }
		    TrollAttack.gameItems.reset();
            return true;
		}
	}
	class mList extends Command {
	    public mList(String s) { super(s, false); }
	    public boolean execute() {
		    player.tell(Communication.GREEN +"Mobiles in the VNUM range of this area:");
		    player.tell(Communication.CYAN + "VNUM\tName\t\t\tShortDesc" + Communication.WHITE);
		    Mobile mobile;
		    int high = player.getActualArea().high;
		    int low = player.getActualArea().low;
		    while(TrollAttack.gameMobiles.itemsRemain()) {
		        mobile = (Mobile)TrollAttack.gameMobiles.getNext();
                
		        if(mobile.vnum <= high && mobile.vnum >= low) {
                    String tabs = "";
                    for(int i = mobile.name.length(); i<24;i += 8) {
                        tabs += "\t";
                    }
		            player.tell(mobile.vnum + "\t" + mobile.name + tabs + mobile.getShort());
		        }
		    }
		    TrollAttack.gameMobiles.reset();
            return true;
		}
        public boolean execute(String s) {
            player.tell(Communication.GREEN +"Beings actually in this area:");
            player.tell(Communication.CYAN + "VNUM\tName\t\t\tShortDesc" + Communication.WHITE);
            for(Room room : player.getActualArea().areaRooms) {
                for(Being being : room.roomBeings) {
                    String tabs = "";
                    if(being == null) {
                        player.tell("NULL BEING");
                        continue;
                    }
                    for(int i = being.name.length(); i<24;i += 8) {
                        tabs += "\t";
                    }
                    player.tell(being.getVnum() + "\t" + being.name + tabs + being.getShort());
                }
            }
            TrollAttack.gameMobiles.reset();
            return true;
        }
	}
	class rList extends Command {
	    public rList(String s) { super(s, false); }
	    public boolean execute() {
		    player.tell(Communication.GREEN +"Rooms in the VNUM range of this area:");
		    player.tell(Communication.CYAN + "VNUM\tTitle" + Communication.WHITE);
		    Room room;
		    int high = player.getActualArea().high;
		    int low = player.getActualArea().low;
		    while(TrollAttack.gameRooms.itemsRemain()) {
		        room = (Room)TrollAttack.gameRooms.getNext();
		        if(room.vnum >= low && room.vnum  <= high) {
		            player.tell(room.vnum + "\t" + room.title);
		        }
		    }
		    TrollAttack.gameRooms.reset();
            return true;
		}
	}
	class resetList extends Command {
	    public resetList(String s) { super(s, false); }
		public boolean execute() {
		    int i = 0;
		    for(Reset reset : TrollAttack.gameResets) {
		        player.tell(++i + ": " + reset.toString());
		    }
            return true;
		}
	}
	class Click extends Command {
	    public Click( String s) { super(s, false); }
	    public boolean execute() {
            for(Reset reset : TrollAttack.gameResets) {
		        reset.run();
		        
		    }
		    player.tell("You hear a loud click as you force the cogs of the world forward a notch.");
            return true;
	    }
	}
	class vAssign extends Command {
	    public vAssign(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: vassign <player> <Low Vnum> <High Vnum>");
	        player.tell("Or:    vassign <player> <filename>");
            return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        
	        if(parts.length < 1 || parts.length > 3) {
	            return this.execute();
	        }
	        
	        Being p;
	        if(parts[0].compareToIgnoreCase("self") == 0) {
	            p = player;
	        } else {
	            p = TrollAttack.getPlayer(parts[0]);
	        }
	        if(p == null) {
	            p = DataReader.readPlayerFile(parts[0]);
	        }
	        if(p == null) {
	            player.tell("That isn't a player!");
	            return false;
	        }
	        Area newArea;
	        if(parts.length == 2) {
	            if(parts[1].compareToIgnoreCase("none") == 0) {
	                newArea = null;
	            } else {
		            newArea = Area.findArea(parts[1], TrollAttack.gameAreas);
		            if(newArea == null) {
		                player.tell("Not a valid area filename!");
		                return false;
		            }
	            }
	        } else {
		        newArea = new Area(
		                new Integer(parts[1]).intValue(),
		                new Integer(parts[2]).intValue(),
		                p.getShort() + ".xml",
		                p.getShort() + "'s Area In Progress", 15, true);
		        TrollAttack.gameAreas.add(newArea);
	        }
	        try{
	            p.setArea(newArea);
		        if(newArea == null) {
		            p.setBuilder(false);
		        } else {
		            p.setBuilder(true);
		        }
		        p.rehash();
		        
		        p.save();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
            player.tell("You assign " + p.getShort() + " " + newArea.filename +"(" + newArea.low + "-" + newArea.high + ").");
	        return true;
	    }
	}
	class reloadWorld extends Command {
	    public reloadWorld(String s) { super(s, false); }
	    public boolean execute() {
	        TrollAttack.broadcast("The world is swept into a void, but it quickly reappears as if from a whirl of electricity.");
	        TrollAttack.reloadWorld();
            return true;
	    }
	}

	class iCreate extends Command {
	    public iCreate(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: icreate <item vnum> <keywords>");
            return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 2) {
	            return execute();
	            
	        }
	        String keywords = parts[1];
	        for(int i = 2;i < parts.length;i++) {
	            keywords += " " + parts[i];
	        }
            Item newItem = TrollAttack.getItem(new Integer(parts[0]).intValue());
            if(newItem != null) {
                player.tell("That vnum is already in use. Type 'ilist' to see what items you have already created in this area.");
                return false;
            }
	        newItem = new Item(
	               new Integer(parts[0]).intValue(),
	               1,
	               1,
	               keywords,
	               "a shiny object",
	               "A shiny object floats where you left it."
	               );
	        player.tell("You have created an item referenced as '" + keywords + "'.");
	        TrollAttack.gameItems.add(newItem);
	        player.getActualRoom().addItem(newItem);
            return true;
	    }
	}
	class mCreate extends Command {
	    public mCreate(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: mcreate <mobile vnum>");
            return false;
	    }
	    public boolean execute(String s) {
	        int vnum = Util.intize(player, s);
	        if(player.canEdit(vnum)) {
                Mobile newRoom = TrollAttack.getMobile(new Integer(s).intValue());
                if(newRoom != null) {
                    player.tell("That vnum is already in use, consult 'mlist' to see a list of mobiles that have already been created in this area.");
                    return false;
                }
		        newRoom = new Mobile(
		               new Integer(s).intValue(),
		               1,"new mobile",
		               1,1,1,1,1,1,1, "1d1", "1d1",1,
		               "the new mobile",
		               "A mobile takes its first breaths here.", false);
		        player.tell("You have create mobile " + s + ".");
		        TrollAttack.gameMobiles.add(newRoom);
		        handleCommand("minvoke " + s);
	        }
            return true;
	    }
	}
	class rEdit extends Command {
	    public rEdit(String s) { super(s); }
	    public boolean execute() { 
            player.tell("Usage: redit <command>"); 
            player.tell("Commands: exit, bexit, title, description");
            return false;
        }
	    public boolean execute( String s ) {
	        builder.redit(s);
            return true;
	    }
	}
	
	class mStat extends Command {
	    public mStat(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: mstat <mobile name>");
            return false;
	    }
	    public boolean execute(String s) {
	        Being mob = player.getActualRoom().getBeing( s, player );
	        if(mob == null) {
	            player.tell("You can't find that!");
	        } else {
	            builder.mStat(mob);
	        }
            return true;
	    }
	}
	class iStat extends Command {
	    public iStat(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: istat <mobile name>");
            return false;
	    }
	    public boolean execute(String s) {
            
	        Item mob = player.getActualRoom().getItem( s );
	        if(mob == null) {
	            player.tell("You can't find that!");
	        } else {
	            builder.iStat(mob);
	        }
            return true;
	    }
	}
	class mSet extends Command {
	    public mSet(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: mset <mobile name> <attribute> <value>");
	        player.tell("Possible attributes:");
	        player.tell("hp, maxhp, mana, maxmana, move, maxmove,");
	        player.tell("name, short, long, gold, damagedice, hitdice,");
	        player.tell("hitlevel, level, trainer, wanderer");
	        return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 3) {
	            this.execute();
	        } else {
	            builder.mSet(parts);
	        }
            return true;
	    }
	}
	class iSet extends Command {
	    public iSet(String s) { super(s, false); }
	    public boolean execute() {
	        player.tell("Usage: iset <item name> <attribute> <value>");
            return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 3) {
	            this.execute();
	        }  else {
	            builder.iSet(parts);
	        }
            return true;
	        
	    }
	}
	class myArea extends Command {
	    public myArea(String s) { super(s); }
	    public boolean execute() {
	            return execute("");
	    }
	    public boolean execute(String s) {
	        if(player.getArea() == null) {
	            player.tell("You don't have an area!");
	            return false;
	        }
	        builder.aSet(player.getArea(), s );
            return true;
	    }
	}
	class Savearea extends Command {
	    public Savearea(String s) {
	        super(s, false);
	    }
	    public boolean execute() {
	        try {
	            player.getArea().save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
	            player.tell("You save your area. (" + player.getArea().filename + ")");
	        } catch(Exception e) {
	            TrollAttack.message("Player probably doesn't have an area.");
	            e.printStackTrace();
                return false;
	        }
            return true;
            
	        
	    }
	    public boolean execute(String s) {
	        if(player.level >= 60) {
	            if(s.compareToIgnoreCase("all") == 0 ) {
		            for(Area area : TrollAttack.gameAreas ) {
		                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
		            }
	            } else {
		            Area area = Area.findArea(s,TrollAttack.gameAreas);
		            if(area == null) {
		                player.tell("That isn't an area, check \"alist\".");
                        return false;
		                
		            } else {
		                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
		            }
	            }
	        } else {
	            player.tell("You can't save areas other than your own!");
                return false;
	        }
            return true;
	    }
	}
	class InvokeItem extends Command {
	    public InvokeItem(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: invoke <item vnum>");
            return false;
	    }
	    public boolean execute(String s) {
	        Item newItem;
	        try {
	            newItem = TrollAttack.getItem(new Integer(s));
	       
		        if(newItem == null) {
		            player.tell("That isn't an item yet!");
		        } else {
		            player.getActualRoom().addItem(newItem);
		            player.tell("You invoke " + newItem.getShort() + ".");
		        }
	        } catch(NumberFormatException e) {
	            player.tell("That isn't a valid item vnum.");
                return false;
	        }
            return true;
	    }
	}
	class InvokeMobile extends Command {
	    public InvokeMobile(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: invoke <mobile vnum>");
            return false;
	    }
	    public boolean execute(String s) {
	        Mobile newMobile;
	        try {
	            newMobile = TrollAttack.getMobile(new Integer(s));
	            if(newMobile == null) {
		            player.tell("That isn't an mobile yet!");
                    return false;
		        } else {
		            player.getActualRoom().addBeing(newMobile);
		            player.tell("You invoke " + newMobile.getShort() + ".");
		            player.getActualRoom().say(player.getShort() + " invokes " + newMobile.getShort() + ".",  player);
		        }
	        } catch(NumberFormatException e) {
	            player.tell("That isn't a valid mobile vnum.");
                return false;
	        }
            return true;
	    }
	}
	
	/* End of Builder Commands */
	/* Immortal Commands */
	class Force extends Command {
	    public Force(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: force <being> <command>");
	        player.tell("You can force a player from anywhere in the game, and if a player is not found, the room you are currently in will be searched for a mobile that matches the name you gave.");
            return false;
	    }
	    public boolean execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 2) {
	            return execute();
	            
	        }
	        String playerName = parts[0];
	        String command = parts[1];
	        for(int i = 2; i < parts.length; i++) {
	            command += " " + parts[i];
	        }
	        for(Player p : TrollAttack.gamePlayers) {
	            if( Util.contains(p.getName(), playerName)) {
	                String commandResult = p.ch.handleCommand(command);
	                p.tell(player.getShort() + " forces you to '" + command + "'.");
	                player.tell("You force " + p.getShort() + " to '" + command + "' " + (commandResult == null ? "(Invalid Command)" : "") + ".");
	                
	                return true;
	            }
	        }
	        Being victim = player.getActualRoom().getBeing(playerName, player);
	        if(victim == null) {
	            player.tell("You don't see that!");
                return false;
	        } else {
	            String commandResult = victim.ch.handleCommand(command);
	            player.tell("You force " + victim.getShort() + " to '" + command + "'" + (commandResult == null ? "(Invalid Command)" : "") + ".");
	        }
            return true;
	    }
	}
	class Switch extends Command {
	    public Switch(String s) { super(s, true); }
	    public boolean execute() {
	        player.tell("Usage: switch <mobile name>");
            return false;
	    }
	    public boolean execute(String s) {
	        if(player.isMobile() && player.switched != null) {
	            player.switched.switchWith(player.switched);
	        }
	        Being being = player.getActualRoom().getBeing(s, player);
	        if(being == null) {
	            player.tell("You don't see that!");
	            return false;
	        }
	        Mobile mob = null;
	        Player p = null;
	        try {
	           mob = (Mobile)being;
	           p = (Player)player;
	        } catch (ClassCastException e) {
	            player.tell("That isn't a mobile or you are not a player!");
	            return false;
	        }
	        
	        p.switchWith(mob);
            return true;
	    }
	}
	class UnQuit extends Command {
	    public UnQuit(String s) { super(s, false); }
	    public boolean execute() {
	        
            player.quit();
            return true;
	    }
	}
    class Shutdown extends Command {
        public Shutdown(String s) { super(s, true); }
        public boolean execute() {
            player.tell("Usage: shutdown yes");
            return false;
        }
        public boolean execute(String s) {
            if(s.length() > 0) {
                TrollAttack.shutdown();
                return true;
            } else {
                return execute();
            }
        }
    }
    class cCreate extends Command {
        public cCreate(String s) { super(s, false); }
        public boolean execute() {
            player.tell("Usage: ccreate <classname>");
            return false;
        }
        public boolean execute(String s) {
            Class newClass = new Class(s);
            TrollAttack.gameClasses.add(newClass);
            player.tell("You add a new class '" + s + "' to the game.");
            return true;
        }
    }
    class cStat extends Command {
        public cStat(String s) { super(s, false); }
        public boolean execute() {
            player.tell("Use: cstat <classname> for specific details about a class.");
            player.tell(Communication.CYAN + "Current classes in the game:");
            String list = "";
            for(Class beingClass : TrollAttack.gameClasses) {
                list += beingClass.getName() + " ";
            }
            player.tell(list);
            return true;
        }
        public boolean execute(String s) {
            for(Class beingClass : TrollAttack.gameClasses) {
                if(beingClass.getName().toLowerCase().startsWith(s.toLowerCase())) {
                    player.tell(Communication.GREEN + "Class: " + beingClass.getName());
                    player.tell(Communication.WHITE + "Name\t\t\tMin.Level\tMax.Proficiency");
                    for(Ability ability : beingClass.getAbilityData().keySet()) {
                        player.tell(ability.name + "\t" + beingClass.getAbilityData().get(ability).level + "\t" +
                                beingClass.getAbilityData().get(ability).maxProficiency);
                    }
                    return true;
                }
            }
            player.tell("That isn't a valid class name.");
            return false;
        }
    }
    class cSet extends Command {
        public cSet(String s) { super(s, false); }
        public boolean execute() {
            player.tell("Usage: cset <classname> <command> <value>");
            player.tell("Usage: cset <classname> add <ability> <minimum level> <maximum proficiency>");
            player.tell("Examples: cset warrior delete 'magic missile'");
            player.tell("          cset warrior add 'magic missile' 50 20");
            player.tell("          cset warrior name marrior");
            return false;
        }
        public boolean execute(String s) {
            String[] parts = Util.split(s);
            if(parts.length < 3) {
                return execute();
            }
            Class setClass = null;
            for(Class beingClass : TrollAttack.gameClasses) {
                if(beingClass.getName().toLowerCase().startsWith(parts[0].toLowerCase())) {
                    setClass = beingClass;
                    break;
                }
            }
            if(setClass == null) {
                player.tell("That is not a known class!");
                return false;
            }
            if("name".startsWith(parts[1])) {
                String oldFilename = setClass.getFileName();
                player.tell("You change the name of the " + setClass.getName() + " class to " + parts[2] + ".");
                setClass.setName(parts[2]);
                File oldFile = new File("Classes/" + oldFilename);
                TrollAttack.message("Deleting file Classes/" + oldFilename + ".");
                oldFile.delete();
            } else if ("add".startsWith(parts[1])) {
                if(parts.length != 5) {
                    return execute();
                }
                Ability newAbility = TrollAttack.abilityHandler.find(parts[2]);
                
                setClass.updateAbility(newAbility , Util.intize(parts[3]),new Float(parts[4]).floatValue());
                player.tell("You add/update the ability '" + newAbility.name + "'.");
            } else if ("delete".startsWith(parts[1])) {
                Ability result = setClass.deleteAbility(TrollAttack.abilityHandler.find(parts[2]));
                if(result == null) {
                    player.tell("There is no ability by that name currently associated with this class.");
                } else {
                    player.tell("You delete the " + result.name + " ability from the " + setClass.getName() + " class.");
                }
            } else if ("edit".startsWith(parts[1])) {
                if(parts.length != 5) {
                    return execute();
                }
                Ability newAbility = TrollAttack.abilityHandler.find(parts[2]);
                setClass.updateAbility( newAbility ,Util.intize(parts[3]),new Float(parts[4]).floatValue());
                player.tell("You add/update the ability '" + newAbility.name + "'.");
            }
            setClass.saveClass();
            return true;
        }
    }
    /* End of Immortal Commands */
	class Quit extends Command {
		public Quit(String s) { super(s); }
		public boolean execute() {
		    player.save();
            player.tell("You close your eyes and go into a deep sleep.");
		    player.quit();
            return true;
		}
	}
    
    public static boolean handleSpell(Being player, String spellString) {
        String[] spellParts = spellString.split(" ");
        String spellParameter = "";
        if(spellParts.length > 0 ) {
            spellString = spellParts[0];
            if(spellParts.length > 1 ) {
                for(int i = 1; i < spellParts.length; i ++ ) {
                    if(i > 1) {
                        spellParameter += " ";
                    }
                    spellParameter += spellParts[i];
                }
            }
        } else {
            spellString = "";
        }
        Spell spell = null;
        for(Ability tmpSpell : player.getAbilityList()) {
            if(tmpSpell.isSpell() && tmpSpell.name.startsWith(spellString)) {
                spell = (Spell)tmpSpell;
                break;
            } else {
                //player.tell(tmpSpell.name + " is either not a spell or doesn't match, '" + spellString + "'.");
            }
        }
        boolean results = false;
        if(spellString.length() > 0 && spell != null) {
            if(spell.isPeaceful() && player.isFighting()) {
                player.tell("You can't cast that while fighting!");
            } else {
                if(spell.getCost() > player.getManaPoints()) {
                    player.tell("You don't have enough mana for that!");
                    
                } else {
                    if(spellParts.length > 1) {
                        results = spell.execute(player, spellParameter);
                        
                    } else {
                        results = spell.execute(player);
                    }
                }
                
            }
        } else {
            player.tell("Cast what spell?");
            return false;
        }
        return results;
        
    }

}
