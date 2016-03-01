/**
 * The Player interface
 * Each player will get to choose where to place the 5 ships and how to take turns shooting at enemy ships
 * @author swapneel
 *
 */
public interface Player {

    /**
     * This method will place a ship on the grid.
     * This method should guarantee correctness of location (no overlaps, no ships over the edge of the board, etc.)
     * @param size the size of the ship to place
     * @param retry if an earlier call to this method returned an invalid position, this method will be called again with retry set to true.
     * @return The Location of the ship
     */
    public Location placeShip(int size, boolean retry);

    /**
     * This method will get a new target.
     * The Player can choose whichever location it wants to aim for next and return that location.
     * @return The Location of the target
     */
    public Location getTarget();

    /**
     * This method will notify the Player of the result of the previous shot
     * @param hit true, if it was a hit; false otherwise
     * @param sunk true, if a ship is sunk; false otherwise
     */
    public void setResult(boolean hit, boolean sunk);

}

