import java.io.*;
import java.util.ArrayList;

/**
 * Created by Qi_He on Feb/1/16.
 */
public class Test {
    public static void main(String[] args) {
        int counter =0;

        for (int i=0; i < 1000; i++){
            BattleShipGame bs = new BattleShipGame();
            Player p1 = new TourneyComputer();
            Player p2 = new DumbComputer();

            bs.initialize(p1, p2);
            Player winner = bs.playGame();
            if (winner.getClass() == p1.getClass()){
                counter++;
            }

        }

        System.out.println(counter);
    }
}
