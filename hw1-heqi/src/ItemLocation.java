/**
 * Created by Qi_He on Feb/1/16.
 */
public class ItemLocation implements Location {
    private int x;
    private int y;
    private boolean horizontal;

    /**
     * Constructor for placing a ship
     * @param a - x cordinate
     * @param b - y cordinate
     * @param direction - direction of the ship
     */
    public ItemLocation(int a, int b, boolean direction){
        x = a;
        y = b;
        horizontal = direction;
    }

    /**
     * For placing a shot
     * @param a - x cordinate
     * @param b - y cordinate
     */
    public ItemLocation(int a, int b){
        x = a;
        y = b;
        horizontal = false;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean isShipHorizontal() {
        return horizontal;
    }
}
