package TrollAttack.Spells;
import TrollAttack.*;
/*
 * Created on May 29, 2005
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
public class SpellHandler {
	LinkedList spellList;
	public Player player;
	public SpellHandler(Player play) {
		spellList = new LinkedList(false, 0);
		player = play;
		
		//player.tell("Starting spell registration...");
		registerSpell(new Heal("heal"));
		registerSpell(new MagicMissile("magic missile"));

	}

	public void registerSpell( Spell c) {
	    spellList.add(c);
	}
	public void handleSpell( String spellString) {
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
		
		
		Spell spell = (Spell)spellList.getClosest(spellString);
		boolean results = false;
		if(spellString.length() > 0 && spell != null) {
		    if(spell.isPeaceful() && player.isFighting()) {
		        player.tell("You can't cast that while fighting!");
		    } else {
			    if(spell.getCost() > player.getManaPoints()) {
			        player.tell("You don't have enough mana for that !");
			        
			    } else {
			        if(spellParts.length > 1) {
			            results = spell.execute(player, spellParameter);
						
					} else {
					   // player.tell("casting the single spell");
						results = spell.execute(player);
					}
			    }
			    if(results) {
			        player.decreaseManaPoints( spell.getCost() );
			        //player.tell("You feel drained by " + spell.cost);
			    }
			    
		    }
		} else {
			player.tell("Cast what spell?");
		}
		
	}
	// Spells
	class MagicMissile extends Spell {
	    double probability = .6;
	    int strength = 5;
	    public MagicMissile(String s) { super( s, 5, false); }
	    public boolean execute() {
	        player.tell("You can't do this to yourself!");
	        return false;
	    }
	    public boolean run(String s) {
	        Mobile mob = TrollAttack.gameRooms[ player.getCurrentRoom()].getMobile( s );
			if( mob == null) {
				player.tell("You don't see that here.");
				return false;
			} else {

				player.tell("You manifest a ball of violent light, rocketing towards " + mob.getShort() + ".");
				mob.increaseHitPoints(-strength);
				if(player.isFighting()) {
				    
				} else {
				    Fight myFight = new Fight(player, mob );
					myFight.start();
				}
				return true;
			   
		   	}
	    }
	}
	class Heal extends Spell {
	    private int strength = 10;
	    
	    public Heal(String s) { super(s, 6, false); }
		public boolean run() {
	        player.tell("You pass a healing hand over yourself.");
	        player.increaseHitPoints( strength );
		    return true;
		}
		public boolean run( String s ) {
		   	if( s.compareToIgnoreCase("self") == 0 ) {
		   	    return this.run();
		   	}
			Mobile mob = TrollAttack.gameRooms[ player.getCurrentRoom()].getMobile( s );
			if( mob == null) {
				player.tell("You don't see that here.");
				return false;
			} else {

				player.tell("You pass a healing hand over " + mob.getShort() + ".");
				mob.increaseHitPoints( strength );
				return true;
			   
		   	}
		}
	}
}
