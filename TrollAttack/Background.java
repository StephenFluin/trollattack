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
public class Background extends Thread {
	int time = 0;
	public void run() {
		while(!TrollAttack.gameOver) {
			// Item routines
			for(int i = 0;i < TrollAttack.gameItems.length; i++) {
				
			}
			
			// Mobile routines
			/**
			 * Every 60 seconds, refresh mobiles that are gone.
			 * Every 10 seconds, increase healths by 2.
			 */
			if(time % 30 == 0) {
			    TrollAttack.deadies.handleResurrection();
			}
			if(time % 10 == 0) {
			    TrollAttack.healMobiles();
			}
		
			// Who cares what happens in rooms
			for(int i = 0;i < TrollAttack.gameRooms.length; i++) {
				
			}
			
			// Make all persons better every 10 seconds.
			if(time % 10 == 0) {
			   TrollAttack.healAllPlayers(1);
			}
			try{
			    Thread.sleep(1000);
			} catch( Exception e ) {
			    
			}
			time++;
		}
	}
}
