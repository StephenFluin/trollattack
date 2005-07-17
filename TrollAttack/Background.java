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
			for(int i = 0;i < TrollAttack.gameItems.getLength(); i++) {
				
			}
			//TrollAttack.message("Starting item background.");
			// Mobile routines
			/**
			 * Every 60 seconds, CLICK!
			 * (2 seconds for testing purposes)
			 */
			if(time % 60 == 0 ) {
			    //TrollAttack.message("CLICK");
			    Reset reset;
			    while(TrollAttack.gameResets.itemsRemain()) {
			        reset = (Reset)TrollAttack.gameResets.getNext();
			        reset.run();
			    }
			    TrollAttack.gameResets.reset();
			}
			
			/**
			 * Every 60 seconds, refresh mobiles that are gone.
			 * Every 10 seconds, increase healths by 2.
			 */
			
			/*if(time % 30 == 0) {
			    TrollAttack.deadies.handleResurrection();
			}*/
			//TrollAttack.message("Starting healing background.");
			if(time % 10 == 0) {
			    TrollAttack.healBeings();
			}
			
			// Punt players idle for more than 20 minutes every 10 minutes.
			if(time % (60 ) == 0 ) {
			    TrollAttack.puntIdlePlayers(60*20);
			}
			
			//TrollAttack.message("Starting room background.");
			// Who cares what happens in rooms
			//for(int i = 0;i < TrollAttack.gameRooms.length; i++) {
			//	
			//}
			if(time % (60 * 5) == 0) {
			    //TrollAttack.broadcast("A healing wind continues to blow accross the realm.");
			}
			

			try{
			    Thread.sleep(1000);
			} catch( Exception e ) {
			   TrollAttack.message("Can't sleep, clowns will eat me.");
			   e.printStackTrace();
			}
			time++;
			//TrollAttack.message("Slept all the way to " + time + ".");
		}
	}
	public int getTime() {
	    return time;
	    
	}
}
