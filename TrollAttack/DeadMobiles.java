package TrollAttack;
/*
 * Created on May 26, 2005
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

public class DeadMobiles {
	private Mobile[] mobs = new Mobile[255];
	private int[] roms = new int[255];
	private int[] refs = new int[255];
	public DeadMobiles() {
		
	}
	public void add(Mobile m, int r) {
		int i;
		for(i = 0; mobs[i] != null; i++) {
		}
		if(i > 254) {
			TrollAttack.error( "ARRAY OVERFLOW!!!" );
		}
		mobs[i] = m;
		roms[i] = r;
		refs[i] = m.getRespawnTime();
		//TrollAttack.error(m.getShort() + " joins the netherworld.");
	}
	public void handleResurrection() {
		for(int i = 0;i < mobs.length; i++ ) {
			if(mobs[i] != null ) {
				if( refs[i]-- == 0 ) {
				    mobs[i].hitPoints = mobs[i].maxHitPoints;
				    mobs[i].manaPoints = mobs[i].maxManaPoints;
				    mobs[i].movePoints = mobs[i].maxMovePoints;
					TrollAttack.getRoom(roms[i]).addMobile( mobs[i] );
					//TrollAttack.error("A mobile returns from the netherworld");
					mobs[i] = null;
				}
			}
		}
	}
}
