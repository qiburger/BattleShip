/**
 * The Location interface to specify how x and y coordinates are represented.
 * This can be used to represent the location of a ship or a shot.
 * If the location is a shot, the isShipHorizontal() method can return an arbitrary value.
 * @author swapneel
 *
 */
public interface Location {

    /**
     * Gets the x coordinate.
     * It should return a number between 0 and 9.
     * The top-left position will be 0, 0.
     * @return the x coordinate
     */
    public int getX();

    /**
     * Gets the y coordinate.
     * It should return a number between 0 and 9.
     * The top-left position will be 0, 0.
     * @return the y coordinate
     */
    public int getY();

    /**
     * This method will indicate whether the ship is horizontal or vertical.
     * Can return an arbitrary value if the location is used to indicate a shot (and not a ship)
     * @return true if ship is horizontal, false otherwise
     */
    public boolean isShipHorizontal();


}
