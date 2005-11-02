package TrollAttack;
/*
 * Created on Jul 17, 2005
 *
 * This file is a source file of TrollAttack.  TrollAttack 
 * is a java game imitating smaug muds. TrollAttack
 * and all of the files that make it up are (c) 2005
 * to Stephen Fluin.
 */
/**
 * @author PeEll
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Effect {
    public Being victim = null;

    public Effect(Being being) {
        victim = being;
    }

    public Effect() {
    };

    public void startEffect() {
    }

    public void stopEffect() {
    }

    public class Health extends Effect {
        private int strength;

        public void setStrength(int strength) {
            this.strength = strength;
        }

        public void startEffect() {
            victim.hitPoints += strength;
            victim.maxHitPoints += strength;
        }

        public void stopEffect() {
            victim.maxHitPoints -= strength;
            victim.hitPoints -= strength;
        }
    }

    public class Mana extends Effect {
        private int strength;

        public void setStrength(int strength) {
            this.strength = strength;
        }

        public void startEffect() {
            victim.manaPoints += strength;
            victim.maxManaPoints += strength;
        }

        public void stopEffect() {
            victim.maxManaPoints -= strength;
            victim.manaPoints -= strength;
        }
    }

    public class Move extends Effect {
        private int strength;

        public void setStrength(int strength) {
            this.strength = strength;
        }

        public void startEffect() {
            victim.movePoints += strength;
            victim.maxMovePoints += strength;
        }

        public void stopEffect() {
            victim.maxMovePoints -= strength;
            victim.movePoints -= strength;
        }
    }
}
