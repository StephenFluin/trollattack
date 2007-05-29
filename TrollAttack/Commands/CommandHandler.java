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
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Vector;
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
		commandList = new LinkedList();
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
		registerCommand(new Put("put"));
		registerCommand(new Give("give"));
		registerCommand(new Drop("drop"));
		registerCommand(new Sacrifice("sacrifice"));
		registerCommand(new Favor("favor"));
		registerCommand(new Look("look"));
		registerCommand(new Cast("cast"));
		//registerCommand(new Name("name"));
		registerCommand(new Password("password"));
		registerCommand(new Prompt("prompt"));
		registerCommand(new Trance("trance"));
		registerCommand(new CountGold("gold"));
		registerCommand(new Consider("consider"));
		registerCommand(new Examine("examine"));
		registerCommand(new Inventory("inventory"));
		registerCommand(new ShowEquipment("equipment"));
		registerCommand(new Wear("wear"));
		registerCommand(new Level("level"));
		registerCommand(new Eat("eat"));
		registerCommand(new Drink("drink"));
		registerCommand(new Fill("fill"));
		registerCommand(new List("list"));
		registerCommand(new Buy("buy"));
		registerCommand(new Configure("configure"));
		registerCommand(new Score("score"));
		registerCommand(new Report("report"));
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
		registerCommand(new ChangeState("sit", 2));
		registerCommand(new ChangeState("rest", 3));
		registerCommand(new ChangeState("sleep", 4));
		registerCommand(new Puke("puke"));
		registerCommand(new Open("open"));
		registerCommand(new Close("close"));
		registerCommand(new Who("who"));
        registerCommand(new Version("version"));
		registerCommand(new Where("where"));
		registerCommand(new Save("save"));
		registerCommand(new Bug("bug"));
		
		registerCommand(new Help("help"));
		registerCommand(new CommandList("commands"));
		registerCommand(new Help("?"));
		registerCommand(new CRoll("roll"));
		
		
		registerCommand(new Command("kiss", 3) { 
			public boolean execute() {
				player.tell("usage: kiss [being]");
				return false;
			}
			public boolean execute(String command) {
				Being b = player.getActualRoom().getBeing(command, player);
				if(b != null) {
					player.tell("You kiss " + b.getShort() + ".");
					Being[] bList = {player, b};
					player.getActualRoom().say("%1 kisses %2.", bList);
				} else {
					player.tell("You don't see them here!");
					return false;
				}
				return true;
			}
		});
		
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
		    registerCommand(new aSet("aset"));
            registerCommand(new sSet("sset"));
            registerCommand(new cSet("cset"));
            registerCommand(new cCreate("ccreate"));
            registerCommand(new cStat("cstat"));
            registerCommand(new Transport("transport"));

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
		    registerCommand(new rStat( "rstat"));
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
	    
	    String[] commandParts = Util.split(commandString);
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
		/*if(player instanceof Mobile) {
			TrollAttack.debug(player.getShort() + " is trying to " + command.name);
		}*/
		if(commandString.length() > 0 && command != null) {
		    //player.tell("command peace was " + command.isPeaceful() + " and player peace was " + player.isFighting());
		    if(command.maxPosition < player.getPosition()) {
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
				/**
				 * Fast paths
				 * 1  10  5
				 * 13	  12
				 * 4  11  6 
				 */
			   
				if(Character.getNumericValue(commandString.charAt(0)) == -1 && commandString.length() >= 3 ) {
					
				    for(Character tmp : commandString.toCharArray()) {
				    	int dir = Character.getNumericValue(tmp);
				    	switch(dir) {
					    	case 1: 
					    		handleCommand("nw");
					    		break;
					    	case 10:
					    		handleCommand("n");
					    		break;
					    	case 5:
					    		handleCommand("ne");
					    		break;
					    	case 13: 
					    		handleCommand("w");
					    		break;
					    	case 12:
					    		handleCommand("e");
					    		break;
					    	case 4:
					    		handleCommand("sw");
					    		break;
					    	case 11: 
					    		handleCommand("s");
					    		break;
					    	case 6:
					    		handleCommand("se");
					    		break;
				    	}
				    	
				    }    

					
				} else {
					player.tell("Huh?");
				}
			    
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
	    public Consider( String s ) { super(s, 3); }
	    public boolean execute() { 
            player.tell("Consider whom?"); 
            return false;
        }
	    public boolean execute( String s ) {
	        Being mob = player.getActualRoom().getBeing( s, player );
	        if( mob == null) {
	            player.tell("You don't see that here.");
                return false;
	        } else {
	        	double comparison = (((float)mob.hitPoints) - ((float)player.hitPoints)) / ((float)player.hitPoints);
	        	String result;
	        	if(comparison > .2) {
	        		result = mob.getShort() + " looks much tougher than you." + Util.wrapChar;
	        	} else if(comparison < -.2) {
	        		result = "You are much tougher than " + mob.getShort() + "." + Util.wrapChar;
	        	} else {
	        		result = mob.getShort() + " looks about as tough as you." + Util.wrapChar;
	        	}
	        	
	        	/*
	        	 * Data for comparison:
	        	   Level 2 Player: 
	        	   	Hit Points:     67/67
					Damage Dice:    2d7,2   Hit Dice:       2d7,2
					Hit Level (Minimum roll to hit):        5
				   Tim:
					Hit Points:     420/420
					Damage Dice:    2d7     Hit Dice:       1d50
					Hit Level (Minimum roll to hit):        0
				   Manson Guard:
				    Hit Points:     600/600
					Damage Dice:    3d6     Hit Dice:       4d20
					Hit Level (Minimum roll to hit):        30
					
					3d6 = 3-18
					10/3 = 3.33333
					1-(3.3333/6) < .50
					.50 * getAverageHitDamage() * 5 rounds / your HP
					
					1d6,6
					hitlevel 7
					
					
					
	        	 */
	        	  
	        	double pHitProbability = player.getHitProbability();
	        	double mHitProbability = mob.getHitProbability();
	        	
	        	double pRounds = (double)player.hitPoints/(mHitProbability * mob.getAverageHitDamage());
	        	//TrollAttack.debug(player.getShort() + " will last about " + pRounds + " rounds. (" + player.hitPoints + ", " + mHitProbability + ", " + mob.getAverageHitDamage() + ")");
	        	double mRounds = (double)mob.hitPoints	/ (pHitProbability + player.getAverageHitDamage());
	        	//TrollAttack.debug(mob.getShort() + " will last about " + mRounds + " rounds. (" + mob.hitPoints + ", " + pHitProbability + ", " + player.getAverageHitDamage() + ")");
	        	
	        	comparison = pRounds / mRounds;
	        	
	        	if(comparison < 1.2) {
	        		result += mob.getShort() + " looks like he could hurt you a lot.";
	        	} else if(comparison > 2) {
	        		result += mob.getShort() + " probably couldn't hurt you very much.";
	        	} else {
	        		result += mob.getShort() + " could hurt you a fair amount.";
	        	}
	        	
	        	
	            player.tell(result);
	        }
            return true;
            
	    }
	}
	class Kill extends Command {
		public Kill(String s) { super(s, 0); }
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
				if(mob instanceof Player && !mob.getName().equalsIgnoreCase(s)) {
					player.tell("To attack a player, you must type their full name!");
					//TrollAttack.debug(mob.getName() + "!=" + s);
					return false;
				}
				//TrollAttack.message("Starting a fight between " + player.getShort() + " and " + mob.getShort());
			    Fight.ensureFight(player,mob);
			}
            return true;
		}
		
	}
	class Slay extends Command {
	    public Slay(String s) { super(s, 3); }
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
			    Being[] pBroadcast = {player, mob};
			    player.getActualRoom().say(Communication.RED + "%1 slays %2 in cold blood.", pBroadcast);
			}
            return true;
	    }
	}
	class Get extends Command {
		public Get(String s) { super(s, 3);}
		public boolean execute() { 
            player.tell("Usage: get <amount>|all <item> [container]" + Util.wrapChar +
            			"Examples:get all sack" + Util.wrapChar +
            			"         get all" + Util.wrapChar +
            			"         get 3 gold" + Util.wrapChar +
            			"         get sword");
            
            return false;
        }
        
		/**
		 * Here be dragons.
		 */
		public boolean execute(String command) {
		    // Split the command so we can "get 3 gold"
			String[] parts = Util.split(command);
			int amount = 0;
			if(parts[0].equalsIgnoreCase("all")) {
			    amount = -1;
			} else {
				try{
					amount = Util.intize(parts[0]);
					if(parts.length < 2) {
						player.tell("Get " + amount + " what?");
						return false;
					}
				} catch(NumberFormatException e) {
					amount = 1;
					// This means that the command was a simple "get sword" or something like that.
				}
			}
			
			/**
			 * If a container was given, set up location and command correctly.
			 */
			Container con = null;
			String location;
			if((parts.length == 2 && (amount == 1 || amount == -1)) || (parts.length == 3 && amount != 1)) {
				
				con = (Container)player.findItem(parts[parts.length-1], Container.class);
				if(con == null) {
					con = (Container)player.getActualRoom().getItem(parts[parts.length-1], Container.class);
					if(con == null) {
						player.tell("You can't find a container like that.");
					return execute();
				}
			}
			location = "in " + con.getShort();
				command = parts[parts.length-2];
			} else {
				location = "here";
				command = parts[parts.length-1];
			}
		    
		    
		    /**
		     * Handle commands involving gold.
		     */
		    if(command.equalsIgnoreCase("gold")) {
		    	Gold item = (Gold)player.getActualRoom().getItem(command, false, Gold.class);
		    	if(item == null) {
		    		player.tell("You don't see any gold here.");
		    		return false;
		    	}
		    	/**
		    	 * Note: THis means that "Get all gold" and "get gold" will make you pick up all the gold.
		    	 */
		    	if( amount < 0 || (amount < 2 && parts.length < 2)) {
		    		amount = item.getCost();
		    	}
		    	if(item.getCost() >= amount) {
		    		if(item.getCost()!= amount) {
		    			player.getActualRoom().addItem(new Gold(item.getCost()-amount));
		    		}
		    		player.gold += amount;
		    		player.tell("You pick up " + amount + " in gold.");
		    		player.roomSay("%1 picks up " + amount + " in gold.");
		    		player.getActualRoom().roomItems.remove(item);
		    		return true;
		    	} else {
		    		player.tell("There isn't that much gold here, there is only " + item.getCost() + "!");
		    		return false;
		    	}
		    	
		    }
		    
		    /**
		     * Setup main loop for commands involving "all";
		     */
		    if(command.equalsIgnoreCase("all")) {
		    	command = "";
		    }
		    
		    
		    /**
		     * Main loop, this happens for each item (ie. 3 times for "get 3 
		     * sword", n times for "get all" or "get all s") 
		     */
		    Vector<Item> itemsToGet = new Vector<Item>();
		    Iterator<Item> it;
		    if(con == null) {
	    		it = player.getActualRoom().roomItems.iterator();
	    	} else {
	    		it = con.contents.iterator();
	    	}
		    while(it.hasNext() && amount != 0) {
		    	Item item= it.next();
		    	if(!Util.contains(item.getName(), command)) {
		    		continue;
		    	}
				if((item.getWeight() + player.getCarryingWeight()) > player.getCarryingMax()) {
					if(amount < 1) {
						continue;
					} else if(amount == 1) {
						player.tell("You aren't strong enough to carry " + item.getShort() + ", it weighs " + item.getWeight() + ".");
						return false;
					}
				} else {
					if(con == null) {
						player.tell("You get " + item.getShort() + ".");
						player.roomSay(player.getShort() + " gets " + item.getShort() + ".");
					} else {
						player.tell("You get " + item.getShort() + " from " + con.getShort() + ".");
						player.roomSay(player.getShort() + " gets " + item.getShort() + " from " + con.getShort() + ".");
					}
					itemsToGet.add(item);
					amount--;
				}
			}
			if(itemsToGet.size() == 0) {
				if(con != null) {
					player.tell("There is nothing here you can get!");
				} else {
					player.tell("You can't find that here.");
				}
				return false;
			} else if(amount > 0) {
				player.tell("You couldn't find " + amount + " of that.");
			}
			for(Item i : itemsToGet) {
				if(con == null) {
					player.getActualRoom().roomItems.remove(i);
					player.addItem(i);
				} else {
					con.contents.remove(i);
					player.addItem(i);
				}
			}
            return true;
		}
	}
	class Put extends Command{
		public Put(String s) { super(s, 3);}
		public boolean execute() {
			player.tell("Usage: put <item> <container>");
			return false;
		}
		public boolean execute(String s) {
			String[] parts = Util.split(s);
			if(parts.length != 2) {
				return execute();
			}
			int amount;
			if(parts[0].equalsIgnoreCase("all")) {
				amount = -1;
				parts[0] = "";
			} else {
				amount = 1;
			}
			Container con = (Container)player.findItem(parts[1],Container.class);
			if(con == null) {
				player.tell("You don't have a container like that.");
				return execute();
			}
			
			while(amount-- != 0) {
				Item item = Util.findMember(player.beingItems, parts[0], null, con);
				
				if(item == null) {
					if(amount < -2) {
						return true;
					} else if(amount == -2) {
						player.tell("There is nothing to put in " + con.getShort());
						return false;
					}
					player.tell("You don't have an item like that.");
					return execute();
				}
				
				if(con == item) {
					player.tell("You can't put an item inside itself!");
					return execute();
				}
				if(con.getRemainingCapacity() < 1) {
					player.tell(Util.uppercaseFirst(con.getShort()) + " doesn't have enough room to fit that. You have " + con.contents.size() + " / " + con.capacity + " items already in it.");
					return false;
				}
				
				player.beingItems.remove(item);
				
				con.add(item);
				player.tell("You put " + item.getShort() + " in " + con.getShort() + ".");
				player.roomSay("%1 puts " + item.getShort() + " in " + con.getShort() + ".");
			}
			return true;
		}
	}	
	class Give extends Command {
	
	    public Give(String s) { super(s, 3);}
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
	            if(victim == null || victim == player) {
	                player.tell("They aren't here!");
	                player.addItem(gift);
                    return false;
	            } else {
	                victim.addItem(gift);
	                player.tell("You give " + gift.getShort() + " to " + victim.getShort(player) + ".");
	        		player.getActualRoom().say("%1 gives %2 " + gift.getShort() + ".", new Being[] {player, victim});
	            }
	        }
            return true;
	    }
	}
	class Sacrifice extends Command {
	    public Sacrifice(String s) { super(s, 1);}
	    public boolean execute() { 
            player.tell("Sacrifice what?"); 
            return false;
        }
	    public boolean execute(String command) {
	    	int count;
	    	if(command.compareToIgnoreCase("all") == 0) {
		        count = -1;
		        command = "";
		    } else {
		        count = 1;
		    }
	    	Item item = null;
	    	while( ( count-- != 0 ) && (item == null ) ) {
		        item = player.getActualRoom().getItem(	command		);

				if(item == null) {
					if(count == 0) {
					    player.tell("You can't find that here!");
					} else {
					    return false;
					}
				} else {
			        player.tell(player.sacrifice(item));
					item = null;
				}
	    	}
	    	return true;
    	}
	}
	class ChangeState extends Command {
	    int newState;
	    public ChangeState(String s, int state){
	        super(s, 4);
	        newState = state;
	    }
	    public boolean execute() {
	        if(player.isFighting()) {
	            player.tell("You can't do that while fighting!");
               return false;
	        } else {
		        if(player.getPosition() == newState) {
		            player.tell("You are already " + player.getDoing() + "!");
                   return false;
		        } else {
		            
		            player.setPosition( newState );
		            player.tell("You are now " + player.getDoing() + ".");
		            player.roomSay(player.getShort() + " is now " + player.getDoing() + ".");		        
		        }
	        }
            return true;
	    }
	}
	class Favor extends Command {
	    public Favor(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Your current favor: " + player.getFavor());
            return true;
	    }
	}
	class Prompt extends Command {
	    public Prompt(String s) { super(s); }
	    public boolean execute() { 
            player.tell("Current Prompt: " + Util.escapeColors(player.getPromptString())); 
            return false;
        }
	    public boolean execute(String command) {
	        player.setPrompt(command);
            return true;
	    }
	}
	class Drop extends Command {
		public Drop(String s) {super(s, 3);}
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
		        if(player.gold >= amount && amount >= 0) {
		            item = player.detachGoldItem(amount);
		        } else {
		            player.tell("You don't have that much!");
		            return false;
		        }
		    } else {
		        item = player.removeItem( command );
		    }
			if(item == null) {
				player.tell("You don't have that!");
               return false;
				
			} else {
				player.tell("You drop " + item.getShort() + ".");
				player.roomSay("%1 drops " + item.getShort() + ".");
				player.getActualRoom().addItem( item );
			}
            return true;
		}
	}
	class Cast extends Command {
	    public Cast(String s) {super(s, 3);}
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
		public Look(String s) {super(s, 3);}
		public boolean execute() {
		    player.look();
            return true;
			
		}
		public boolean execute(String s) {
			Being being = player.getActualRoom().getBeing(s, player);
			if(being == null) {
			    player.tell("You don't see that here.");
               return false;
			} else {
				player.tell(being.showBeing());
			}
            return true;
		}
	}
	class Inventory extends Command {
		public Inventory(String s) { super(s, 3); }
		public boolean execute() { 
            player.tell("Your " + player.getInventory(), false);
            return true;
        }
	}
	class ShowEquipment extends Command {
		public ShowEquipment(String s) { super(s, 3); }
		public boolean execute() { 
            player.tell("Your " + player.getEquipment(), false); 
            return true;
        }
	}
	class Trance extends Command {
	    public Trance(String s) { super(s, 3); }
	    public boolean execute() {
	        player.tell("You enter a trance.");
	        player.roomSay("%1 enters a trance.");
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
		public Wear(String s) { super(s, 2); }
		public boolean execute() {
            player.tell("Wear what?");
            return false;
        }
		public boolean execute(String command) {
			if(command.equals("all")) {
				Vector<Item> itemList = new Vector<Item>();
				for(Item i : player.beingItems) {

					if(i instanceof Equipment) {
						itemList.add(i);
					}
				}
				for(Item i : itemList) {
					player.wearItem(i);
				}
				return true;
			} else {
				Item newWear = player.findItem(command);
				if(newWear == null) {
				    player.tell("You don't have that!");
	               return false;
				} else {
				    player.wearItem( newWear );
	         
				}
	            return true;
			}
		}
	}
	class Eat extends Command {
	    public Eat(String s) { super(s, 2); }
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
	            player.eatItem( newEat );
	        }
            return true;
	    }
	}
	class Drink extends Command {
	    public Drink(String s) { super(s, 2); }
	    public boolean execute() { 
            player.tell("Drink what?");
            return false;
        }
	    public boolean execute(String command) {
	        Item newDrink = player.findItem(command, DrinkContainer.class);
	        if(newDrink == null) {
	        	//TrollAttack.debug("didn't find a drinkcon");
	            newDrink = player.getActualRoom().getItem(command, Fountain.class);
	        }
	        if(newDrink == null) {
	            player.tell("You can't find a drink like that!");
	            return false;
	        } else {
	           player.drinkItem( newDrink );

	        }
            return true;
	    }
	}
	class Examine extends Command {
		public Examine(String s) { super(s, 3); }
		public boolean execute() {
			player.tell("Examine what?" + Util.wrapChar + "Usage: examine <item>");
			return false;
		}
		public boolean execute(String command) {
			Item newItem = player.findItem(command);
			if(newItem == null) {
				newItem = player.getActualRoom().getItem(command);
			}
			if(newItem == null) {
				player.tell("You can't find an item like that!");
				return false;
			}
			String msg = "You examine " + newItem.getShort() + "." + Util.wrapChar +
						Util.uppercaseFirst(newItem.getShort()) + " is a " + newItem.getType() + ".";
			if(newItem instanceof Container) {
				Container c = (Container)newItem;
				msg += Util.wrapChar + "Contents:" + Util.wrapChar;
				for(Item i : c.contents) {
					msg += i.getShort() + Util.wrapChar;
				}
				if(c.contents.size() == 0) {
					msg += Communication.CYAN + "Empty";
				}
			}
			player.tell(msg);
			
			
			return true;
			
		}
	}
	class Fill extends Command {
	    public Fill(String s) { super(s, 2); }
	    public boolean execute() { 
            player.tell("Usage: fill <container> <source>");
            return false;
        }
	    public boolean execute(String command) {
	        String[] parts = Util.split(command);
	        if(parts.length < 2) {
	            return execute();
	        }
	        Item container = player.findItem(parts[0], DrinkContainer.class);
	        Item fountain = player.getActualRoom().getItem(parts[1], Fountain.class);
	        DrinkContainer newContainer;
	        Fountain newFountain;
	        if(container == null) {
	            player.tell("You don't have that a container named that!");
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
	            player.tell("You can't find a water source named that!");
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
	        super( s, 2); 
	    }
	    public boolean execute() {
            player.tell("Remove what?");
            return false;
        }
	    public boolean execute(String command) {
	        
            player.unwearItem( command );
            return true;
	    }
	}
    class Follow extends Command {
        public Follow(String s) { super(s, 0); }
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
	    public CRoll(String s) { super( s); }
	    public boolean execute() {
	        player.tell("Roll what?");
            return false;
	    }
	    public boolean execute(String s) {
	        try {
	            Roll roller = new Roll(s);
	        	player.tell("You rolled '" + s + "' to get " + roller.roll());
                
	        } catch(NumberFormatException e) {
	        	player.tell("This isn't a valid roll.");
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
            return true;
	    }
	}
	class CountGold extends Command {
	    public CountGold(String s) {
	        super( s);
	    }
	    public boolean execute() {
	        player.tell("You have " + player.gold + " gold coins.");
            return true;
	    }
	}
	class List extends Command {
		public List(String s) { super( s , 1); }
		public boolean execute() {
			if(player.getActualRoom() instanceof Shop) {
				Shop s = (Shop)player.getActualRoom();
				player.tell(s.list() + Util.wrapChar + Communication.RED + "You currently have: " + player.gold + " gold.");
				return true;
			} else {
				player.tell("This isn't a shop.");
				return false;
			}
		}
	}
	class Buy extends Command {
		/*
		 * Note: shops don't support putting portals with different costs 
		 * yet, but they should support portals with different items in 
		 * them.
		 */
		public Buy(String s) { super( s, 1); }
		public boolean execute() {
			player.tell("Usage: buy <item>");
			return false;
		}
		public boolean execute(String s) {
			if(!(player.getActualRoom() instanceof Shop)) {
				player.tell("This isn't a shop.");
				return false;
			}
			Shop shop = (Shop)player.getActualRoom();
			for(Item i : shop.shopItems) {
				if(Util.contains(i.getName(), s)) {
					if(player.gold > i.getCost()) {
						player.gold -= i.getCost();
						player.addItem((Item)i.clone());
						player.tell("You purchase " + i.getShort() + ".");
						player.roomSay("%1 buys " + i.getShort() + ".");
						return true;
					} else {
						player.tell("You don't have enough money to buy that!");
						return false;
					}
				}
			}
			player.tell("That isn't for sale.");
			return false;
		}
	}
	class Configure extends Command {
		public Configure(String s) { super(s); }
		public boolean execute() {
			if(!(player instanceof Player)) {
				player.tell("Not a player!");
				return false;
			}
			Player p = (Player) player;
			String string = "Usage: configure <command> True|False" + Util.wrapChar +
						"Current Settings:" + Util.wrapChar;
			String table = "( )\tCommand\tOption" + Util.wrapChar;
			for(String[] cfgName : Player.configList) {
				table += "(" + (p.getConfig(cfgName[0]) ? "X" : " ") + ")\t" + cfgName[0] + "\t" + cfgName[1] + Util.wrapChar;
			}
			table = Util.table(table);
			string += table;
			player.tell(string);
			return false;
		}
		public boolean execute(String command) {
			if(!(player instanceof Player)) {
				return false;
			}
			Player p = (Player)player;

			String[] parts = Util.split(command);
			if(parts.length != 2) {
				return execute();
			}
			boolean result = false;
			for(String[] conf : Player.configList) {
				if(Util.contains(conf[0],parts[0])) {
					p.setConfig(conf[0], new Boolean(parts[1]).booleanValue());
					result = true;
				}
			}
			if(!result) {
				player.tell("Not a valid setting.");
				return execute();
			}
			player.tell("You change your settings.");
			return true;
		}
	}
	class Score extends Command {
	    public Score(String s) { super( s); }
	    public boolean execute() {
	        player.score();
            return true;
	    }
	}
	class Report extends Command {
		public Report(String s) { super(s, 3); }
		public boolean execute() {
			String report = "HP: " + player.hitPoints + "/" + player.maxHitPoints + 
						" Mana: " + player.manaPoints + "/" + player.maxManaPoints +
						" Move: " + player.movePoints + "/" + player.maxMovePoints;
			player.tell("You report: " + report);
			player.roomSay("%1 reports: " + report);
			return true;
		}
	}
	class Level extends Command {
	    public Level(String s) {
	        super( s );
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
	        super( s );
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
	    public Say(String s) { super(s, 3); }
	    public boolean execute() {
	        player.tell("Say what?");
            return false;
	    }
	    public boolean execute(String phrase) {
            phrase = Util.escapeColors(phrase);
	        player.tell(Communication.CYAN + "You say, \"" + phrase + "\".");
	        player.roomSay(Communication.CYAN + player.getShort() + " says, \"" + phrase + "\".");
            return true;
	    }
	}
    class Chat extends Command {
        public Chat(String s) { super(s); }
        public boolean execute() {
            player.tell("Chat what?");
            return false;
        }
        public boolean execute(String s) {
        	s = Util.escapeColors(s);
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
        public Tell(String s) { super(s); }
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
            player.tell(Communication.WHITE + "You tell " + victim.getShort() + ", \"" + message + "\".");
            return true;
        }
    }
	class Open extends Command {
	    public Open(String s) { super(s, 1); }
	    public boolean execute() {
	        player.tell("Open what?");
            return false;
	    }
	    public boolean execute(String s) {
            return player.open(s);
	    }
	}
	class Close extends Command {
	    public Close(String s) { super(s, 1); }
	    public boolean execute() {
	        player.tell("Close what?");
            return false;
	    }
	    public boolean execute(String s) {
            return player.close(s);
	    }
	}
	class Title extends Command {
	    public Title(String s) { super(s); }
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
        public sSet(String s) { super(s); }
        public boolean execute() {
            player.tell("Usage: sset <ability> <proficiency>");
            player.tell("       sset all 100");
            player.tell("       sset scan 80");
            return false;
        }
        public boolean execute(String command) {
            String[] parts = Util.split(command);
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
        public Practice(String s) { super(s,2); }
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
            java.util.Vector<Ability> list = new java.util.Vector<Ability>();
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
            java.util.Vector<Being> beings = player.getActualRoom().getRoomBeings();
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
	    public Puke(String s) { super(s, 3); }
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
	        player.roomSay("%1 pukes all over the floor.");
            return true;
	    }
	}
	class Who extends Command {
	    public Who(String s) { super(s); }
	    public boolean execute() {
	        player.tell(Communication.CYAN + "Current Players:");
	        for(Player p  : TrollAttack.gamePlayers) {
	            player.tell(Communication.GREEN + p.level+ "\t" + p.getName() + " " + p.title);
	        }
            return true;
	       
	        
	    }
	    
	}
    class Version extends Command {
        public Version(String s) { super(s); }
        public boolean execute() {
            player.tell("Version: " + TrollAttack.version + ".");
            return true;
        }
    }
	class Where extends Command {
	    public Where(String s) { super(s); }
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
	    public Emote(String s) { super(s, 4); }
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
	class Password extends Command {
	    public Password(String s) { super(s,0); }
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
	    public Save(String s) { super(s, 4); }
	    public boolean execute() {
	        player.save();
	        player.tell("You save your progress.");
            return true;
	    }
	}
	
	class Bug extends Command {
		public Bug(String s) { super(s); }
		public boolean execute() {
			player.tell("Usage: bug <message>");
			return false;
		}
		public boolean execute(String s) {
			try {
				OutputStreamWriter f = new FileWriter("bug-log.txt", true);
				f.write(player.getName() +"(" + player.getCurrentRoom() + "):" + s + "\n");
				f.flush();
				f.close();
				player.tell("Bug logged, thanks for your feedback!");
			} catch(Exception e) {
				return false;
			}
			return true;
		}
	}
	
	class Help extends Command {
		public Help(String s) { super(s); }
		public boolean execute() {
			player.tell("Direction Commands:");
			player.tell("east, west, north, south, up, down");
			player.tell("Player Commands:");
			player.tell("kill, cast, get, drop, prompt, trance");
			player.tell("stand, sit, rest, sleep");
			player.tell("consider, inventory, equipment, wear, remove");
			player.tell("Game Commands");
			player.tell("look, quit, exit, help");
            return true;
		}
	}
	
	/* Builder Commands */
	class Goto extends Command {
	    public Goto(String s) { super(s, 4); }
	    public boolean execute() {
	        player.tell("You are currently in room " + player.getCurrentRoom() + ".");
            return true;
	    }
	    public boolean execute( String s ) {
	       int room;
	        try {
	           try {
	               // Try to create an integer out of the room first.
	        	   room = Util.intize(null, s);
	           } catch(NumberFormatException e) {
	        	   // If we weren't given an valid integer, then try and find an active player name.
	               Player p = TrollAttack.getPlayer(s);
	               if(p == null) {
	                   player.tell("This isn't a valid vnum or player name.");
	                   return false;
	               } else {
	            	   // We have a valid player, so set room to be where they are so we can go there.
	                   room = p.getCurrentRoom();
	               }
	           }
	           player.transport(room);
	       } catch(Exception e) {
	           player.tell("Problem changing rooms (invalid vnum)!");
	           e.printStackTrace();
               return false;
	       }
           player.getActualRoom().say("%1 arrives in a whirl of smoke.", player);
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
	    public aList(String s) { super(s); }
	    public boolean execute() {
		    
		    String table = Communication.CYAN + "Filename\tLowVnum\tHighVnum\tFrozen\tName";
		    for(Area area : TrollAttack.gameAreas) {
		        table += Util.wrapChar +  Communication.WHITE + area.filename + "\t" + area.low+"\t"+area.high + "\t" + "(" + (area.frozen ? "X" : " ") + ")\t" + area.name;
		    }
		    table = Util.table(table);
		    player.tell(Communication.GREEN + "Game Areas:" + Util.wrapChar + table);
            return true;
		}
	}
	class iList extends Command {
	    public iList(String s) { super(s); }
	    public boolean execute() {
   
			String table = Communication.CYAN + "VNUM\tName\tShortDesc";
			int high, low;
			high = player.getActualArea().high;
			low = player.getActualArea().low;
			for(Item item : TrollAttack.gameItems) {
				if(item.vnum <= high && item.vnum >= low) {
					table += Util.wrapChar + Communication.WHITE + item.vnum + "\t" + item.getName() + "\t" + item.getShort();
				}
			}
			table = Util.table(table);
			player.tell(Communication.GREEN +"Items in the VNUM range of this area:" + Util.wrapChar + table);
			return true;
		}
	}
	class mList extends Command {
	    public mList(String s) { super(s); }
	    public boolean execute() {
		    player.tell(Communication.GREEN +"Mobiles in the VNUM range of this area:");
		    player.tell(Communication.CYAN + "VNUM\tName\t\t\tShortDesc" + Communication.WHITE);
		    int high = player.getActualArea().high;
		    int low = player.getActualArea().low;
		    for(Mobile mobile : TrollAttack.gameMobiles) {
                
		        if(mobile.vnum <= high && mobile.vnum >= low) {
                    String tabs = "";
                    for(int i = mobile.name.length(); i<24;i += 8) {
                        tabs += "\t";
                    }
		            player.tell(mobile.vnum + "\t" + mobile.name + tabs + mobile.getShort());
		        }
		    }
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
            return true;
        }
	}
	class rList extends Command {
	    public rList(String s) { super(s); }
	    public boolean execute() {
		    String result = Communication.GREEN +"Rooms in the VNUM range of this area:" + Util.wrapChar;
		    result += Communication.CYAN + "VNUM\tTitle" + Util.wrapChar + Communication.WHITE;
		    int high = player.getActualArea().high;
		    int low = player.getActualArea().low;
		    for(Room room : TrollAttack.gameRooms) {
		        if(room.vnum >= low && room.vnum  <= high) {
		        	result += room.vnum + "\t" + room.title + Util.wrapChar;
		        }
		    }
		    player.tell(result);
            return true;
		}
	}
	class resetList extends Command {
	    public resetList(String s) { super(s); }
		public boolean execute() {
		    int i = 0;
		    for(Reset reset : TrollAttack.gameResets) {
		        player.tell(++i + ": " + reset.toString());
		    }
            return true;
		}
	}
	class Click extends Command {
	    public Click( String s) { super(s); }
	    public boolean execute() {
            for(Reset reset : TrollAttack.gameResets) {
		        reset.run();
		        
		    }
		    player.tell("You hear a loud click as you force the cogs of the world forward a notch.");
            return true;
	    }
	}
	class vAssign extends Command {
	    public vAssign(String s) { super(s); }
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
		        try{
		        	newArea = new Area(
		        
		                Util.intize(parts[1]),
		                Util.intize(parts[2]),
		                p.getShort() + ".xml",
		                p.getShort() + "'s Area In Progress", 15, true);
		        	TrollAttack.gameAreas.add(newArea);
		        } catch(NumberFormatException e) {
		        	player.tell("Not a valid vnum range.");
		        	return false;
		        }
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
	        if(newArea == null) {
	        	player.tell("You remove " + p.getShort() + "'s area.");
	        	p.interrupt(player.getShort() + " removes you of your building abilities.");
	        	return true;
	        }
	        
        	player.tell("You assign " + p.getShort() + " " + newArea.filename +"(" + newArea.low + "-" + newArea.high + ").");
        	p.interrupt(player.getShort() + " assigns you the area " + newArea.filename + "("  + newArea.low + "-" + newArea.high + ").");
        	return true;
	        
	    }
	}
	class reloadWorld extends Command {
	    public reloadWorld(String s) { super(s); }
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
	        Item newItem;
	        try{
	        	newItem = TrollAttack.getItem(new Integer(parts[0]).intValue());
	        } catch(NumberFormatException e) {
	        	return execute();
	        }
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
	    public mCreate(String s) { super(s); }
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
		               "A mobile takes its first breaths here.");
		        newRoom.setWanderer(false);
		        newRoom.setAggressive(false);
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
            player.tell("Commands: exit, bexit, title, description, nowander, nofloor, destroy");
            player.tell("Examples:");
            player.tell("redit bexit east 300");
            player.tell("redit title The Dungeon");
            return false;
        }
	    public boolean execute( String s ) {
	        builder.redit(s);
            return true;
	    }
	}
	class rStat extends Command {
		public rStat(String s) { super(s); }
		public boolean execute() {
			builder.rStat(player.getActualRoom());
			return true;
		}
	}
	class mStat extends Command {
	    public mStat(String s) { super(s); }
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
	    public iStat(String s) { super(s); }
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
	    public mSet(String s) { super(s); }
	    public boolean execute() {
	        player.tell("Usage: mset <mobile name> <attribute> <value>");
	        player.tell("Possible attributes:");
	        player.tell("hp, maxhp, mana, maxmana, move, maxmove,");
	        player.tell("name, short, long, gold, damagedice, hitdice,");
	        player.tell("hitlevel, level, trainer, wanderer, reroll");
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
	    public iSet(String s) { super(s); }
	    public boolean execute() {
	        return execute("");
	    }
	    public boolean execute(String s) {
	    	return builder.iSet(s.split(" "));
	        
	    }
	}
	class myArea extends Command {
	    public myArea(String s) { super(s); }
	    public boolean execute() {
	    	if(player.getArea() == null) {
	            player.tell("You don't have an area!");
	        } else {
	        	player.tell(builder.area());
	        }
          return false;
	    }
	}
	class aSet extends Command {
		public aSet(String s) { super(s); }
		public boolean execute() {
			player.tell("Usage: aset <command>" + Util.wrapChar + 
					"Possible Commands: name filename low high" + Util.wrapChar +
					builder.area());
			return false;
		}
		public boolean execute(String s) {
			if(player.getArea() == null) {
				player.tell("You don't have an area!");
				return false;
			}
			builder.aSet(s);
			return true;
		}
		
	}
	class Savearea extends Command {
	    public Savearea(String s) {
	        super(s);
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
	            	int i = 0;
		            for(Area area : TrollAttack.gameAreas ) {
		                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
		                i++;
		            }
		            player.tell("You save " + i + " areas.");
	            } else {
		            Area area = Area.findArea(s,TrollAttack.gameAreas);
		            if(area == null) {
		                player.tell("That isn't an area, check \"alist\".");
                        return false;
		                
		            } else {
		                area.save(TrollAttack.gameRooms, TrollAttack.gameMobiles, TrollAttack.gameItems);
		                player.tell("You save '" + area.filename + "'.");
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
		            newMobile.setCurrentRoom(player.getCurrentRoom());
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
	    public Switch(String s) { super(s, 0); }
	    public boolean execute() {
	        player.tell("Usage: switch <mobile name>");
            return false;
	    }
	    public boolean execute(String s) {
	        // If you are a player that is already switched, you should switch back.
	    	if(player.isMobile() && player.switched != null) {
	            player.switched.switchWith(player.switched);
	            player.tell("You switch back to your former self.");
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
	        player.tell("You switch with " + mob.getShort());
            return true;
	    }
	}
	class UnQuit extends Command {
	    public UnQuit(String s) { super(s); }
	    public boolean execute() {
	        
            player.quit();
            return true;
	    }
	}
    class Shutdown extends Command {
        public Shutdown(String s) { super(s); }
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
        public cCreate(String s) { super(s); }
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
        public cStat(String s) { super(s); }
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
        public cSet(String s) { super(s); }
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
    class Transport extends Command {
        public Transport(String s) { super(s); }
        public boolean execute() {
            player.tell("Usage: transport <player> <vnum>");
            return false;
        }
        public boolean execute(String s) {
            String[] parts = Util.split(s);
            
            if(parts.length != 2) {
                return execute();
            }
            Player victim = null;
            for(Player p : TrollAttack.gamePlayers) {
                if(Util.contains(p.getShort(), parts[0]) && p.level < player.level) {
                    victim = p;
                }
            }
            if(victim == null) {
                player.tell("That isn't an online player!");
                return false;
            }
            Being[] ignores = {player, victim};
            Room oldRoom = victim.getActualRoom();
            int room = Util.intize(parts[1]);
            TrollAttack.getRoom(room).say("%2 enters the room, being pulled by a strange force.", ignores);
            victim.transport(room);
            victim.tell("You are pulled by a strange force, your surroundings leave you and you find yourself somewhere else.");
            player.tell("You transfer " + victim.getShort() + " to " + victim.getActualRoom().title + "(" + victim.getActualRoom().vnum + ").");

            oldRoom.say("%2 is pulled by a strange force from the room.", ignores);
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
            if(spell.maxPosition > player.getPosition()) {
                player.tell("You can't cast that while " + player.getDoing() + "!");
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
