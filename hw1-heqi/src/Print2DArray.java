/**
 * Created by Qi_He on Feb/1/16.
 */
public class Print2DArray {

    public static void printRow(int[] row) {
        for (int i : row) {
            System.out.print(i);
            System.out.print("\t");
        }
        System.out.println();
    }
}
