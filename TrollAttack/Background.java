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
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Background extends Thread {
    int time = 0;

    // Order by most frequent to least frequent for organization.
    public void run() {
        while (!TrollAttack.gameOver) {
            
            if (time % 10 == 0) {
                TrollAttack.healBeings();
                TrollAttack.wanderAndDecay();
                
            }
            
            /**
             * Every 60 seconds, CLICK!
             */
            if (time % 60 == 0) {
                for (Reset reset : TrollAttack.gameResets) {
                    reset.run();
                }

                TrollAttack.puntIdlePlayers(TrollAttack.maxIdleTime);
            }
            if (time % (60 * 6) == 0) {
                TrollAttack.agePlayers(.1);
            }
            if (time % (60 * 30) == 0) {
                TrollAttack.hungerStrike(6);
               
            }


            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                TrollAttack.message("Can't sleep, clowns will eat me.");
                e.printStackTrace();
            }
            time++;
            //TrollAttack.message("Slept all the way to " + time + ".");
        }
        TrollAttack.message("Background shutting down.");
    }

    public int getTime() {
        return time;

    }
}
