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
import TrollAttack.Area;
import TrollAttack.Being;
import TrollAttack.Communication;
import TrollAttack.Fight;
import TrollAttack.LinkedList;
import TrollAttack.Mobile;
import TrollAttack.Player;
import TrollAttack.Roll;
import TrollAttack.Room;
import TrollAttack.TrollAttack;
import TrollAttack.Util;
import TrollAttack.Items.*;
import TrollAttack.Items.Armor;
import TrollAttack.Items.Food;
import TrollAttack.Items.Item;
import TrollAttack.Items.Weapon;
import TrollAttack.Spells.SpellHandler;


public class CopyOfCommandHandler {
	class aList extends Command {
	    public aList(String s) { super(s, false); }
	    public void execute() {
		    player.tell(Communication.GREEN + "Game Areas:");
		    player.tell(Communication.CYAN + "Filename\t\tLowVnum\tHighVnum\tFrozen\tName" + Communication.WHITE);
		    Area area;
		    while(TrollAttack.gameAreas.itemsRemain()) {
		        area = (Area)TrollAttack.gameAreas.getNext();
		       
		        player.tell(area.filename + (area.filename.length() < 12 ? "\t" : "") + "\t" + area.low+"\t"+area.high + "\t" + "(" + (area.frozen ? "X" : " ") + ")\t" + area.name);
		    }
		    TrollAttack.gameAreas.reset();
		}
	    public void execute(String s) {
	        this.execute();
	    }
	}
	class Cast extends Command {
	    public Cast(String s) {super(s, false);}
	    public void execute() { player.tell("Cast what?"); }
	    public void execute(String s) {
	        try {
	            sh.handleSpell( s );
	        } catch(Exception e) {
	            e.printStackTrace();
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
	            player.tell("You can't do that while fighting!");
	        } else {
		        if(player.getState() == newState) {
		            player.tell("You are already " + player.getDoing() + "!");
		        } else {
		            
		            player.setState( newState );
		            player.tell("You are now " + player.getDoing() + ".");
		            player.roomSay(player.getShort() + " is now " + player.getDoing() + ".");		        }
	        }
	    }
	}
	class Click extends Command {
	    public Click( String s) { super(s, false); }
	    public void execute() {
	        Reset reset;
		    while(TrollAttack.gameResets.itemsRemain()) {
		        reset = (Reset)TrollAttack.gameResets.getNext();
		        reset.run();
		        
		    }
		    player.tell("You hear a loud click as you force the cogs of the world forward a notch.");
		    TrollAttack.gameResets.reset();
	    }
	}
	class Close extends Command {
	    public Close(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Open what?");
	    }
	    public void execute(String s) {
	        int direction = Exit.getDirection(s);
	        player.tell(player.getActualRoom().close(direction));
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
	        player.tell( list ); 
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	// Commands
	class Consider extends Command {
	    public Consider( String s ) { super(s, false); }
	    public void execute() { player.tell("Consider whom?"); }
	    public void execute( String s ) {
	        Being mob = player.getActualRoom().getBeing( s, null );
	        if( mob == null) {
	            player.tell("You don't see that here.");
	        } else {
	            player.tell(mob.getShort() + ": " + mob.prompt());
	        }
	    }
	}
	class CRoll extends Command {
	    public CRoll(String s) { super( s, false); }
	    public void execute() {
	        player.tell("Roll what?");
	    }
	    public void execute(String s) {
	        try {
	            Roll roller = new Roll(s);
	        	player.tell("You rolled '" + s + "' to get " + roller.roll());
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	class Debug1 extends Command {
	    public Debug1(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Testing\rwith\rr's.");
	    }
	}
	class Debug2 extends Command {
	    public Debug2(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Testing\nwith\nn's.");
	    }
	}
	class Drink extends Command {
	    public Drink(String s) { super(s, false); }
	    public void execute() { player.tell("Drink what?");}
	    public void execute(String command) {
	        Item newDrink = player.findItem(command);
	        if(newDrink == null) {
	            player.tell("You don't have that!");
	        } else {
	            player.tell( player.drinkItem( newDrink ) );
	        }
	    }
	}
	class Drop extends Command {
		public Drop(String s) {super(s, false);}
		public void execute() {}
		public void execute(String command) {
		    if(command.compareToIgnoreCase("all") == 0) {
		        player.dropAll();
		        player.tell("You drop everything you have.");
		        player.roomSay("%1 drops everything they have.");
		        return;
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
		            return;
		        }
		    } else {
		        item = player.dropItem( command );
		    }
			if(item == null) {
				player.tell("You don't have that!");
				
			} else {
				player.tell("You drop " + item.getShort() + ".");
				player.roomSay(player.getShort() + " drops " + item.getShort() + ".");
				player.getActualRoom().addItem( item );
			}
		}
	}
	class Eat extends Command {
	    public Eat(String s) { super(s, false); }
	    public void execute() {player.tell("Eat what?");}
	    public void execute(String command) {
	        Item newEat = player.findItem(command);
	        if(newEat == null) {
	            player.tell("You don't have that!");
	        } else {
	            player.tell( player.eatItem( newEat ) );
	        }
	    }
	}
	class Emote extends Command {
	    public Emote(String s) { super(s, true); }
	    public void execute() { player.tell("Emote what?"); }
	    public void execute(String message) {
	        //TrollAttack.message("Emoting " + message + ".");
	        player.getActualRoom().say(player.getShort() + " " + message);
	    }
	}
	class Equipment extends Command {
		public Equipment(String s) { super(s, false); }
		public void execute() { player.tell("Your " + player.getEquipment()); }
		public void execute(String s) { this.execute(); }
	}
	class Favor extends Command {
	    public Favor(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Your current favor: " + player.getFavor());
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	
	/* End of Builder Commands */
	/* Immortal Commands */
	class Force extends Command {
	    public Force(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: force <being> <command>");
	        player.tell("You can force a player from anywhere in the game, and if a player is not found, the room you are currently in will be searched for a mobile that matches the name you gave.");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 2) {
	            execute();
	            return;
	        }
	        String playerName = parts[0];
	        String command = parts[1];
	        for(int i = 2; i < parts.length; i++) {
	            command += " " + parts[i];
	        }
	        while( TrollAttack.gamePlayers.itemsRemain() ) {
	            Player p = (Player)TrollAttack.gamePlayers.getNext();
	            if( Util.contains(p.getName(), playerName)) {
	                String commandResult = p.ch.handleCommand(command);
	                p.tell(player.getShort() + " forces you to '" + command + "'.");
	                player.tell("You force " + p.getShort() + " to '" + command + "' " + (commandResult == null ? "(Invalid Command)" : "") + ".");
	                
	                TrollAttack.gamePlayers.reset();
	                return;
	            }
	        }
	        TrollAttack.gamePlayers.reset();
	        Being victim = player.getActualRoom().getBeing(playerName, player);
	        if(victim == null) {
	            player.tell("You don't see that!");
	        } else {
	            String commandResult = victim.ch.handleCommand(command);
	            player.tell("You force " + victim.getShort() + " to '" + command + "'" + (commandResult == null ? "(Invalid Command)" : "") + ".");
	        }
	    }
	}
	class freeze extends Command {
	    public freeze(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: freeze <area filename>");
	    }

	    public void execute(String s) {
	        while(TrollAttack.gameAreas.itemsRemain())  {
	            Area area = (Area)TrollAttack.gameAreas.getNext();
	            if(area.filename.compareToIgnoreCase(s) == 0) {
	                area.frozen = true;
	                area.save();
	                TrollAttack.reloadWorld();
	                player.tell("You halt the motion of the atoms in " + area.name + ".");
	                break;
	            }
	        }
	        TrollAttack.gameAreas.reset();
	           
	    }

	}
	class Get extends Command {
		public Get(String s) { super(s, false);}
		public void execute() { player.tell("Get what?"); }
		public void execute(String command) {
		    String[] parts = command.split(" ");
		    int count = 0;
		    if(parts.length == 2 && parts[0].compareToIgnoreCase("all") == 0) {
		        count = -1;
		        command = parts[1];
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
					    return;
					}
				} else {
					player.tell("You get " + item.getShort() + ".");
					player.roomSay(player.getShort() + " gets " + item.getShort() + ".");
					player.addItem(item);
					item = null;
				}
		    }
		}
	}
	class Give extends Command {
	    public Give(String s) { super(s, false);}
	    public void execute() {
	        player.tell("Usage: give <item> <being>");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        Item gift = player.getItem(parts[0], true);
	        if(gift == null) {
	            player.tell("You don't have that!");
	        } else {
	            Being victim = player.getActualRoom().getBeing(parts[1], player);
	            if(victim == null) {
	                player.tell("They aren't here!");
	                player.addItem(gift);
	            } else {
	                victim.addItem(gift);
	                player.tell("You give " + gift.getShort() + " to " + victim.getShort(player) + ".");
	                Being[] pBroadcast = new Being[3];
	                pBroadcast[0] = pBroadcast[1] = player;
	        		pBroadcast[2] = victim;
	        		player.getActualRoom().say("%1 gives %2 " + gift.getShort() + ".", pBroadcast);
	            }
	        }
	    }
	}
	class Gold extends Command {
	    public Gold(String s) {
	        super( s, false );
	    }
	    public void execute() {
	        player.tell("You have " + player.gold + " gold coins.");
	    }
	}
	/*class Transport extends Command {
	    public Goto(String s) { super(s, false); }
	    public void execute() {
	        player.tell("You are currently in room " + player.getCurrentRoom() + ".");
	    }
	    public void execute( String s ) {
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
	    public void execute() {
	        player.tell("You are currently in room " + player.getCurrentRoom() + ".");
	    }
	    public void execute( String s ) {
	       int room;
	       int oldRoom = 1;
	        try {
	           try {
	               room = Util.intize(null, s);
	           } catch(NumberFormatException e) {
	               Player p = TrollAttack.getPlayer(s);
	               if(p == null) {
	                   player.tell("This isn't a valid vnum or player name.");
	                   return;
	               } else {
	                   room = p.getCurrentRoom();
	               }
	           }
	           
	           oldRoom = player.getCurrentRoom();
	           player.getActualRoom().removeBeing(player);
	           player.setCurrentRoom(room);
	           if(player.getActualRoom() == null) {
	               if(player.canEdit(room)) {
	                   player.tell("Waving your hand, you form order from swirling chaos, and step into a new reality...");
	               
		               Room newRoom = new Room(
		    	               room,
		    	               "A Freshly Created Room",
		    	               "Change the title of this room by typing \"rtitle <new title>\".   Enter the description of this room by typing \"rdescription <description>\".",
		    	               new LinkedList());
		    	        //player.tell("You have create room " + s + ", type \"goto " + s + "\" to see your new room.");
		    	        TrollAttack.gameRooms.add(newRoom);
		    	        Area.testRoom(newRoom).areaRooms.add(newRoom);
		    	        player.setCurrentRoom(room);
	               } else {
	                   player.setCurrentRoom(oldRoom);
	               }
	           }
	           player.getActualRoom().addBeing(player);
	           player.look();
	       } catch(Exception e) {
	           player.tell("Problem changing rooms!");
	           e.printStackTrace();
	       }
	       
	    }
	}
	class Help extends Command {
		public Help(String s) { super(s, false); }
		public void execute() {
			player.tell("Direction Commands:");
			player.tell("east, west, north, south, up, down");
			player.tell("Player Commands:");
			player.tell("kill, cast, get, drop, prompt, trance");
			player.tell("stand, sit, rest, sleep");
			player.tell("consider, inventory, equiptment, wear, remove");
			player.tell("Game Commands");
			player.tell("look, quit, exit, help");
		}
		public void execute(String s) { this.execute(); }
	}
	class iCreate extends Command {
	    public iCreate(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: icreate <item vnum> <keywords>");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 2) {
	            execute();
	            return;
	        }
	        String keywords = parts[1];
	        for(int i = 2;i < parts.length;i++) {
	            keywords += " " + parts[i];
	        }
	        Item newItem = new Item(
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
	    }
	}
	class iList extends Command {
	    public iList(String s) { super(s, false); }
	    public void execute() {
		    player.tell(Communication.GREEN +"Game Items:");
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
		}
	    public void execute(String s) {
	        this.execute();
	    }
	}
	class Inventory extends Command {
		public Inventory(String s) { super(s, false); }
		public void execute() { player.tell("Your " + player.getInventory()); }
		public void execute(String s) { this.execute(); }
	}
	class InvokeItem extends Command {
	    public InvokeItem(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: invoke <item vnum>");
	    }
	    public void execute(String s) {
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
	        }
	    }
	}
	class InvokeMobile extends Command {
	    public InvokeMobile(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: invoke <mobile vnum>");
	    }
	    public void execute(String s) {
	        Mobile newMobile;
	        try {
	            newMobile = TrollAttack.getMobile(new Integer(s));
	            if(newMobile == null) {
		            player.tell("That isn't an mobile yet!");
		        } else {
		            player.getActualRoom().addBeing(newMobile);
		            player.tell("You invoke " + newMobile.getShort() + ".");
		            player.getActualRoom().say(player.getShort() + " invokes " + newMobile.getShort() + ".",  player);
		        }
	        } catch(NumberFormatException e) {
	            player.tell("That isn't a valid mobile vnum.");
	        }
	       
	    }
	}
	class iSet extends Command {
	    public iSet(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: iset <item name> <attribute> <value>");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 3) {
	            this.execute();
	        } else {
	            Item item = null;
	            try {
	               item = (Item)player.getActualRoom().getItem(parts[0]);
	            } catch(Exception e) {
	                //TrollAttack.error("Possilbe attempt to change player with mset.");
	                e.printStackTrace();
	            }
	            if(item != null) {
		            String attr = parts[1];
		            String value = parts[2];
		            for(int i = 3;i < parts.length;i++) {
		                value += " " + parts[i];
		            }
		            int intValue = 0;
		            try {
		               intValue  = new Integer(value).intValue();
		            } catch(Exception e) {
		                
		            }
		           if(attr.compareToIgnoreCase("name") == 0) {
		                item.name = value;
		            } else if(attr.compareToIgnoreCase("short") == 0) {
		                item.shortDesc = value;
		            } else if(attr.compareToIgnoreCase("long") == 0) {
		                item.longDesc = value;
		            } else if(attr.compareToIgnoreCase("weight") == 0 ) {
		                item.weight = intValue;
		            } else if(attr.compareToIgnoreCase("cost") == 0 ) {
		                item.cost = intValue;
		            } else if(attr.compareToIgnoreCase("type") == 0 ) {
		                Item newItem;
		                if( value.compareToIgnoreCase(Weapon.getItemType()) == 0 && item.getType() != Weapon.getItemType() ) {
		                    newItem = new Weapon(item);
		                } else if(value.compareToIgnoreCase(Armor.getItemType()) == 0 && item.getType() != Armor.getItemType()) {
		                    newItem = new Armor(item);
		                } else if(value.compareToIgnoreCase(Food.getItemType()) == 0 && item.getType() != Food.getItemType()) {
		                    newItem = new Food(item);
		                } else if(value.compareToIgnoreCase(DrinkContainer.getItemType()) == 0 && item.getType() != DrinkContainer.getItemType()) {
		                    newItem = new DrinkContainer(item);
		                } else {
							player.tell("What type do you want to make this item???" + value);
							return;
		                }
		                player.getActualRoom().replaceItem(item, newItem);
		                TrollAttack.replaceItem(item, newItem);
		            } else {
		                if(item.getType().compareToIgnoreCase(Weapon.getItemType())== 0) {
		                    Weapon weapon = (Weapon)item;
		                    if(attr.compareToIgnoreCase("damage") == 0) {
				                weapon.setDamage(value);
				            } else {
			                    player.tell(attr + " is not a valid attribute for this item!");
			                }
		                } else if(item.getType().compareToIgnoreCase(Armor.getItemType()) == 0) {
		                    Armor armor = (Armor)item;
		                    if(attr.compareToIgnoreCase("armorclass") == 0) {
		                        armor.setArmorClass(Util.intize(player, value));
		                    } else if(attr.compareToIgnoreCase("wearlocation") == 0) {
		                        armor.setWearLocation(value);
		                    } else {
			                    player.tell(attr + " is not a valid attribute for this item!");
			                }
		                } else if(item.getType().compareToIgnoreCase(Food.getItemType()) == 0) {
		                    Food food = (Food)item;
		                    if(attr.compareToIgnoreCase("quality") == 0) {
		                        food.setQuality(Util.intize(player, value));
		                    } else {
			                    player.tell(attr + " is not a valid attribute for this item!");
			                }
		                } else if(item.getType().compareToIgnoreCase(DrinkContainer.getItemType()) == 0) {
		                    DrinkContainer drinkcon = (DrinkContainer)item;
		                    if(attr.compareToIgnoreCase("volume") == 0) {
		                        drinkcon.setVolume(Util.intize(player, value));
		                    } else if(attr.compareToIgnoreCase("capacity") == 0) {
		                        drinkcon.setCapacity(Util.intize(player, value));
		                    } else {
			                    player.tell(attr + " is not a valid attribute for this item!");
			                }
		                } else {
		                    player.tell(attr + " is not a valid attribute for this item!");
		                }
		            }
	            } else {
	                player.tell("You don't see that here!");
	            }
	        }
	    }
	}
	class iStat extends Command {
	    public iStat(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: istat <mobile name>");
	    }
	    public void execute(String s) {
	        Item mob = player.getActualRoom().getItem( s );
	        if(mob == null) {
	            player.tell("You can't find that!");
	        } else {
	            builder.iStat(mob);
	        }
	    }
	}
	class Kill extends Command {
		public Kill(String s) { super(s, true); }
		public void execute() { player.tell("Kill what?"); }
		public void execute(String s) {
			//TrollAttack.message("Running the kill command with '" + s + "' for a string.");
		    Being mob = player.getActualRoom().getBeing( s, player );
		    //TrollAttack.message("got here... 145");
			if( mob == null) {
				player.tell("You don't see that here.");
			} else {
				//TrollAttack.message("Starting a fight between " + player.getShort() + " and " + mob.getShort());
			    Fight myFight = new Fight(player, mob );
				myFight.start();
			}
		}
		
	}
	class Level extends Command {
	    public Level(String s) {
	        super( s, false );
	    }
	    public void execute() {
	        player.tell("Current Level: " + player.getLevel());
	        for(int i = player.getLevel() + 1; i < player.getLevel() + 7; i++ ) {
	            player.tell("Level " + i + ": " + Util.experienceLevel(i));
	        }
	    }
	}
	class Load extends Command {
	    public Load(String s) { super(s, false); }
	    public void execute() {
	        //player = Util.readPlayerData();
	        player.tell("Load which player?");
	    }
	    public void Execute(String s) {
	        Communication tmp = player.getCommunication();
	        player.tell("You can't switch players!");
	        
	        
	    }
	}
	class Look extends Command {
		public Look(String s) {super(s, false);}
		public void execute() {
			player.look();
			
		}
		public void execute(String s) {
			Being being = player.getActualRoom().getBeing(s, player);
			if(being == null) {
			    player.tell("You don't see that here.");
			} else {
			    player.tell(being.showBeing());
			}
		}
	}
	class mCreate extends Command {
	    public mCreate(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: mcreate <mobile vnum>");
	    }
	    public void execute(String s) {
	        int vnum = Util.intize(player, s);
	        if(player.canEdit(vnum)) {
		        Mobile newRoom = new Mobile(
		               new Integer(s).intValue(),
		               1,"new mobile",
		               1,1,1, "1d1", "1d1",1,
		               "the new mobile",
		               "A mobile takes its first breaths here.");
		        player.tell("You have create mobile " + s + ".");
		        TrollAttack.gameMobiles.add(newRoom);
		        handleCommand("minvoke " + s);
	        }
	    }
	}
	class mList extends Command {
	    public mList(String s) { super(s, false); }
	    public void execute() {
		    player.tell(Communication.GREEN +"Game Mobiles:");
		    player.tell(Communication.CYAN + "VNUM\tName\t\tShortDesc" + Communication.WHITE);
		    Mobile mobile;
		    int high = player.getActualArea().high;
		    int low = player.getActualArea().low;
		    while(TrollAttack.gameMobiles.itemsRemain()) {
		        mobile = (Mobile)TrollAttack.gameMobiles.getNext();
		        if(mobile.vnum <= high && mobile.vnum >= low) {
		            player.tell(mobile.vnum + "\t" + mobile.name + "\t" + mobile.getShort());
		        }
		    }
		    TrollAttack.gameMobiles.reset();
		}
	    public void execute(String s) {
	        this.execute();
	    }
	}
	class mSet extends Command {
	    public mSet(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: mset <mobile name> <attribute> <value>");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        if(parts.length < 3) {
	            this.execute();
	        } else {
	            Being mobile = null;
	            try {
	               mobile = (Being)player.getActualRoom().getBeing(parts[0], player);
	            } catch(Exception e) {
	                TrollAttack.error("Possilbe attempt to change player with mset.");
	                e.printStackTrace();
	            }
	            if(mobile != null) {
		            String attr = parts[1];
		            String value = parts[2];
		            for(int i = 3;i < parts.length;i++) {
		                value += " " + parts[i];
		            }
		            int intValue = 0;
		            try {
		               intValue  = new Integer(value).intValue();
		            } catch(Exception e) {
		                
		            }
		            if(attr.compareToIgnoreCase("hp") == 0) {
		                mobile.hitPoints = intValue;
		            } else if(attr.compareToIgnoreCase("hp") == 0) {
		                mobile.hitPoints = intValue;
		            } else if(attr.compareToIgnoreCase("maxhp") == 0) {
		                mobile.maxHitPoints = intValue;
		            } else if(attr.compareToIgnoreCase("mana") == 0) {
		                mobile.manaPoints = intValue;
		            } else if(attr.compareToIgnoreCase("maxmana") == 0) {
		                mobile.maxManaPoints = intValue;
		            } else if(attr.compareToIgnoreCase("move") == 0) {
		                mobile.movePoints = intValue;
		            } else if(attr.compareToIgnoreCase("maxmove") == 0) {
		                mobile.maxMovePoints = intValue;
		            } else if(attr.compareToIgnoreCase("name") == 0) {
		                mobile.name = value;
		            } else if(attr.compareToIgnoreCase("short") == 0) {
		                mobile.shortDescription = value;
		            } else if(attr.compareToIgnoreCase("long") == 0) {
		                mobile.longDesc = value;
		            } else if(attr.compareToIgnoreCase("gold") == 0) {
		                mobile.gold = intValue;
		            } else if(attr.compareToIgnoreCase("damagedice") == 0) {
		                mobile.hitDamage = new Roll(value);
		            } else if(attr.compareToIgnoreCase("hitdice") == 0) {
		                mobile.hitSkill = new Roll(value);
		            } else if(attr.compareToIgnoreCase("hitlevel") == 0) {
		                mobile.hitLevel = intValue;
		            } else if(attr.compareToIgnoreCase("level") == 0) {
		                mobile.level = intValue;
		            } else {
		                player.tell(attr + " is not a valid attribute for a mobile!");
		            }
	            } else {
	                player.tell("You don't see that here!");
	            }
	        }
	    }
	}
	
	class mStat extends Command {
	    public mStat(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: mstat <mobile name>");
	    }
	    public void execute(String s) {
	        Being mob = player.getActualRoom().getBeing( s, player );
	        if(mob == null) {
	            player.tell("You can't find that!");
	        } else {
	            builder.mStat(mob);
	        }
	    }
	}
	class myArea extends Command {
	    public myArea(String s) { super(s); }
	    public void execute() {
	        builder.area();
	    }
	}
	class Name extends Command {
	    public Name(String s) { super(s, true); }
	    public void execute() {
	        player.tell("Your name is " + player.getName() + ".");
	    }
	    public void execute(String phrase) {
	        player.tell("You name yourself " + phrase + ".");
	        player.name(phrase);
	    }
	}
	class Open extends Command {
	    public Open(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Open what?");
	    }
	    public void execute(String s) {
	        int direction = Exit.getDirection(s);
	        player.tell(player.getActualRoom().open(direction));
	    }
	}
	class Password extends Command {
	    public Password(String s) { super(s, true); }
	    public void execute() {
	        player.tell("Usage: password <old password> <new password>");
	    }
	    public void execute(String phrase) {
	        String[] parts = phrase.split(" ");
	        if(parts.length > 1 && player.checkPassword(parts[0]) ) {
	            player.setPassword(parts[1]);
	            player.tell("Your password has been changed.");
	        } else {
	            player.tell("Incorrect old password.");
	        }
	    }
	}
	class Prompt extends Command {
	    public Prompt(String s) { super(s, false); }
	    public void execute() { player.tell("Current Prompt: " + player.getPrompt()); }
	    public void execute(String command) {
	        player.setPrompt(command);
	    }
	}
	class Puke extends Command {
	    public Puke(String s) { super(s, false); }
	    public void execute() {
	        if(player.thirst > 8 && player.hunger > 8) {
	            player.tell("You have nothing left in your stomach to puke up!");
	           
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
	        
	        
	    }
	}
	/* End of Immortal Commands */
	class Quit extends Command {
		public Quit(String s) { super(s); }
		public void execute() {
		    player.save();
		    player.quit();
		}
		public void execute( String s ) { this.execute(); }
	}
	class rCreate extends Command {
	    public rCreate(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: rcreate <room vnum>");
	    }
	    public void execute(String s) {
	        Room newRoom = new Room(
	               new Integer(s).intValue(),
	               "A Freshly Created Room",
	               "Change the title of this room by typing \"rtitle <new title>\".   Enter the description of this room by typing \"rdescription <description>\".",
	               new LinkedList());
	        player.tell("You have create room " + s + ", type \"goto " + s + "\" to see your new room.");
	        TrollAttack.gameRooms.add(newRoom);
	    }
	}
	class rEdit extends Command {
	    public rEdit(String s) { super(s); }
	    public void execute() { player.tell("Usage: redit <command>"); player.tell("Commands: exit, bexit, title, description");}
	    public void execute( String s ) {
	        builder.redit(s);
	    }
	}
	class reloadWorld extends Command {
	    public reloadWorld(String s) { super(s, false); }
	    public void execute() {
	        TrollAttack.broadcast("The world is swept into a void, but it quickly reappears as if from a whirl of electricity.");
	        
	        TrollAttack.reloadWorld();
	    }
	}
	class Remove extends Command {
	    public Remove(String s) {
	        super( s, false ); 
	    }
	    public void execute() {player.tell("Remove what?");}
	    public void execute(String command) {
	        player.tell( player.removeItem( command ) );
	    }
	}
	class resetList extends Command {
	    public resetList(String s) { super(s, false); }
		public void execute() {
		    int i = 0;
		    while(TrollAttack.gameResets.itemsRemain()) {
		        Reset reset = (Reset)TrollAttack.gameResets.getNext();
		        player.tell(++i + ": " + reset.toString());
		    }
		    TrollAttack.gameResets.reset();
		}
	}
	class rList extends Command {
	    public rList(String s) { super(s, false); }
	    public void execute() {
		    player.tell(Communication.GREEN +"Game Rooms:");
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
		}
	    public void execute(String s) {
	        this.execute();
	    }
	}
	class Sacrafice extends Command {
	    public Sacrafice(String s) { super(s, false);}
	    public void execute() { player.tell("Sacrafice what?"); }
	    public void execute(String command) {
	      Item i = player.getActualRoom().removeItem( command );
           if(i == null) {
               player.tell("You can't find that here.");
           } else {
               player.tell("You sacrafice " + i.getShort() + " to your deity and receive one gold coin.");
               player.gold++;
               player.roomSay(i.getShort() + " is sacrificed to " + player.getShort() + "'s deity.");
               player.increaseFavor((int)(Math.random() * 3 + 2));
           }
	    }
	}
	class Save extends Command {
	    public Save(String s) { super(s, false); }
	    public void execute() {
	        player.save();
	        player.tell("You save your progress.");
	    }
	    public void Execute(String s) {
	        this.execute();
	    }
	}
	class Savearea extends Command {
	    public Savearea(String s) {
	        super(s, false);
	    }
	    public void execute() {
	        try {
	            player.getArea().save();
	            player.tell("You save your area. (" + player.getArea().filename + ")");
	        } catch(Exception e) {
	            TrollAttack.message("Player probably doesn't have an area.");
	            e.printStackTrace();
	        }
	        
	    }
	    public void execute(String s) {
	        if(player.level >= 60) {
	            if(s.compareToIgnoreCase("all") == 0 ) {
		            for(int i = 1;i <= TrollAttack.gameAreas.length();i++) {
		                Area area = (Area)TrollAttack.gameAreas.find(i);
		                area.save();
		            }
		            TrollAttack.gameAreas.reset();
	            } else {
		            Area area = Area.findArea(s);
		            if(area == null) {
		                player.tell("That isn't an area, check \"alist\".");
		                
		            } else {
		                area.save();
		            }
	            }
	        } else {
	            player.tell("You can't save areas other than your own!");
	        }
	    }
	}
	class Say extends Command {
	    public Say(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Say what?");
	    }
	    public void execute(String phrase) {
	        player.tell("You say, \"" + phrase + "\".");
	        player.roomSay(player.getShort() + " says, \"" + phrase + "\".");
	    }
	}
	class Score extends Command {
	    public Score(String s) { super( s , false); }
	    public void execute() {
	        player.score();
	    }
	}
	class Slay extends Command {
	    public Slay(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: slay <being>");
	    }
	    public void execute(String s) {
	        Being mob = player.getActualRoom().getBeing( s, true, player );
	        if( mob == null) {
				player.tell("You don't see that here.");
			} else {
			    player.tell("You slay " + mob.getShort() + " in cold blood.");
			    Being[] pBroadcast = {player, player, mob};
			    player.getActualRoom().say("%1 slays %2 in cold blood.", pBroadcast);
			}
	    }
	}
	class Switch extends Command {
	    public Switch(String s) { super(s, true); }
	    public void execute() {
	        player.tell("Usage: switch <mobile name>");
	    }
	    public void execute(String s) {
	        if(player.isMobile() && player.switched != null) {
	            player.switched.switchWith(player.switched);
	        }
	        Being being = player.getActualRoom().getBeing(s, player);
	        if(being == null) {
	            player.tell("You don't see that!");
	            return;
	        }
	        Mobile mob = null;
	        Player p = null;
	        try {
	           mob = (Mobile)being;
	           p = (Player)player;
	        } catch (ClassCastException e) {
	            player.tell("That isn't a mobile or you are not a player!");
	            return;
	        }
	        
	        p.switchWith(mob);
	    }
	}
	class Title extends Command {
	    public Title(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: title <new title>");
	    }
	    public void execute(String s) {
	        player.setTitle(s);
	        player.tell("Title changed to '" + s + "'");
	    }
	}
	class Trance extends Command {
	    public Trance(String s) { super(s, true); }
	    public void execute() {
	        player.tell("You enter a trance.");
	        player.increaseManaPoints ( 5 );
	        try {
	            Thread.sleep(4000);
	        } catch(Exception e) {
	            player.tell("You suck.");
	        }
	    }
	    public void execute(String command) {
	        this.execute();
	    }
	}
	class unfreeze extends Command {
	    public unfreeze(String s) { super(s); }
	    public void execute() {
	        player.tell("Usage: unfreeze <area filename>");
	    }
	    public void execute(String s) {
	        while(TrollAttack.gameAreas.itemsRemain())  {
	            Area area = (Area)TrollAttack.gameAreas.getNext();
	            if(area.filename.compareToIgnoreCase(s) == 0) {
	                area.frozen = false;
	                area.save();
	                TrollAttack.reloadWorld();
	                player.tell("You set the atoms of " + area.name + " spinning."); 
	                break;
	            }
	        }
	        TrollAttack.gameAreas.reset();
	           
	    }
	}
	class UnQuit extends Command {
	    public UnQuit(String s) { super(s, false); }
	    public void execute() {
	        player.quit();
	    }
	    public void execute( String s ) { this.execute(); }
	}
	class vAssign extends Command {
	    public vAssign(String s) { super(s, false); }
	    public void execute() {
	        player.tell("Usage: vassign <player> <Low Vnum> <High Vnum>");
	    }
	    public void execute(String s) {
	        String[] parts = s.split(" ");
	        
	        if(parts.length < 2) {
	            this.execute();
	            return;
	        }
	        
	        Being p;
	        if(parts[0].compareToIgnoreCase("self") == 0) {
	            p = player;
	        } else {
	            p = TrollAttack.getPlayer(parts[0]);
	        }
	        if(p == null) {
	            p = DataReader.readPlayerData(parts[0]);
	        }
	        if(p == null) {
	            player.tell("That isn't a player!");
	            return;
	        }
	        Area newArea;
	        if(parts.length == 2) {
	            if(parts[1].compareToIgnoreCase("none") == 0) {
	                newArea = null;
	            } else {
		            newArea = Area.findArea(parts[1]);
		            if(newArea == null) {
		                player.tell("Not a valid area filename!");
		                return;
		            }
	            }
	        } else {
		        newArea = new Area(
		                new Integer(parts[1]).intValue(),
		                new Integer(parts[2]).intValue(),
		                parts[0] + ".xml",
		                p.getShort() + "'s Area In Progress", 15, true);
		        TrollAttack.gameAreas.add(newArea);
	        }
	        p.setArea(newArea);
	        if(newArea == null) {
	            p.setBuilder(false);
	        } else {
	            p.setBuilder(true);
	        }
	        p.rehash();
	        
	        p.save();
	    }
	}
	class Wear extends Command {
		public Wear(String s) { super(s, false); }
		public void execute() {player.tell("Wear what?");}
		public void execute(String command) {
			Item newWear = player.findItem(command);
			if(newWear == null) {
			    player.tell("You don't have that!");
			} else {
			    player.tell( player.wearItem( newWear ) );
			}
			
		}
	}
	class Where extends Command {
	    public Where(String s) { super(s, false); }
	    public void execute() {
	        Area myArea = player.getActualArea();
	        player.tell(Communication.CYAN + "Current Players in " + player.getActualArea().name + ":");
	        while(TrollAttack.gamePlayers.itemsRemain()) {
	            Player p = (Player)TrollAttack.gamePlayers.getNext();
	            if(p.getActualArea() == myArea) {
	                player.tell(Communication.GREEN + p.level+ "\t" + p.getName() + " " + p.title);
	            }
	        }
	        TrollAttack.gamePlayers.reset();
	    }
	}
	class Who extends Command {
	    public Who(String s) { super(s, false); }
	    public void execute() {
	        player.tell(Communication.CYAN + "Current Players:");
	        while(TrollAttack.gamePlayers.itemsRemain()) {
	            Player p = (Player)TrollAttack.gamePlayers.getNext();
	            player.tell(Communication.GREEN + p.level+ "\t" + p.getName() + " " + p.title);
	        }
	        TrollAttack.gamePlayers.reset();
	        
	       
	        
	    }
	    
	}
	Build builder;
	LinkedList commandList;
	Being player;
	SpellHandler sh;
	public CopyOfCommandHandler(Being p) {
		commandList = new LinkedList(false, 0);
		player = p;
		//player.tell("Starting command handler.");
		sh = new SpellHandler(player);
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
		registerCommand(new Score("score"));
		registerCommand(new Remove("remove"));
		registerCommand(new Say("say"));
		registerCommand(new Emote("emote"));
		registerCommand(new Title("title"));
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
		registerCommand(new Load("load"));
		registerCommand(new Help("help"));
		registerCommand(new CommandList("commands"));
		registerCommand(new Help("?"));
		registerCommand(new CRoll("roll"));
		
		registerCommand(new Debug1("debug1"));
		registerCommand(new Debug2("debug2"));
		
		registerCommand(new Quit("quit"));
		registerCommand(new Quit("exit"));
		/* Immortal Commands */
		if(player.level > 60 || player.isMobile()) {
		    registerCommand(new vAssign("vassign"));
		    registerCommand(new reloadWorld("reloadworld"));
		    registerCommand(new freeze("freeze"));
		    registerCommand(new unfreeze("unfreeze"));
		    registerCommand(new UnQuit("unquit"));
		    registerCommand(new Switch("switch"));

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
		player.tell("");
		if(commandString.length() > 0 && command != null) {
		    //player.tell("command peace was " + command.isPeaceful() + " and player peace was " + player.isFighting());
		    if(command.isPeaceful() && !player.isReady()) {
		        player.tell("You can't do that while " + player.getDoing() + "!");
		    } else {
			    if(commandParts.length > 1) {
	
				        try {
				            command.execute(commandParameter);
				        } catch(Exception e) {
				            e.printStackTrace();
				        }
					
				} else {
					command.execute();
				}
		    }
		} else {
			if(commandString.length() != 0) {
			    player.tell("Huh?");
			}
			
		}
		player.tell( player.prompt() );
		if(command == null) {
		    return null;
		} else {
		    return command.name;
		}
	}

	public void registerCommand( Command c) {
		/*
		 if(c == null) {
		    player.tell("c is null!");
		} else {
		    player.tell("registering " + c.name);
		}*/
	    commandList.add(c);
		 
	}
	
}
