import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Qi_He on Feb/3/16.
 */
public class HumanPlayer implements Player {
    private int[][] myBoard;
    private int[][] enemyBoard;
    private Scanner sc;

    private Location lastPlacement;
    private boolean lastOrientation;
    private ArrayList<Location> pastShots;

    /**
     *  initialize a human player
     */
    public HumanPlayer(){
        sc = new Scanner(System.in);

        myBoard = new int[Game.SIZE][Game.SIZE];
        enemyBoard = new int[Game.SIZE][Game.SIZE];

        // Use these to track last moves
        lastPlacement = null;
        lastOrientation = false;
        pastShots = new ArrayList<Location>();
    }

    @Override
    public Location placeShip(int size, boolean retry) {
        Location sl = null;
        boolean shipValid = false;
        boolean horizontal = false;

        if (retry && (lastPlacement != null)){
            System.out.println("Last placement was invalid, let's try again");
            changeBoard(myBoard, lastPlacement, size, lastOrientation, 0);
        }

        System.out.println("Game board is: " + Game.SIZE + " by " + Game.SIZE);

        // Keep asking for ship placement until it's valid
        while(!shipValid){
            System.out.println("Current board looks like:");
            printBoard(myBoard);

            System.out.println("Please place ship of size: " + size);

            int x = getHumanInt("Enter x cordinate as an int from 0 - " + (Game.SIZE-1));
            int y = getHumanInt("Enter y cordinate as an int from 0 - " + (Game.SIZE-1));
            int direction = getHumanInt("Enter 0 if the ship is horizontal, 1 otherwise");
            while(direction<0 || direction > 1){
                direction = getHumanInt("Enter 0 if the ship is horizontal, 1 otherwise");
            }

            horizontal = direction == 0;
            sl = new ItemLocation(x, y, horizontal);

            shipValid = shipIsValid(sl, size, myBoard);
            if (! shipValid) System.out.println("Not a valid ship placement");
        }

        changeBoard(myBoard, sl, size ,horizontal, size);
        lastPlacement = sl;
        lastOrientation = horizontal;

        if (size == 2) {
            System.out.println("Assume proper placement, your board will be:");
            printBoard(myBoard);
        }
        return sl;
    }

    @Override
    public Location getTarget() {
        boolean shotValid = false;
        Location shotLocation = null;

        System.out.println("-------------------");
        System.out.println("*******************");
        System.out.println("-------------------");
        System.out.println("Game board is: " + Game.SIZE + " by " + Game.SIZE);
        System.out.println("Time to take a shot");

        while(!shotValid){

            System.out.println("Enemy board looks like: ");
            printBoard(enemyBoard);
            System.out.println("0 is unknown, 1 is shot, 2 is hit, 3 is the shot that sank a ship");

            System.out.println("Please take a shot...");

            int x = getHumanInt("Enter x cordinate as an int from 0 - " + (Game.SIZE-1));
            int y = getHumanInt("Enter y cordinate as an int from 0 - " + (Game.SIZE-1));

            // check if shot is valid
            if ((x >= 0 && x < Game.SIZE) && (y >= 0 && y < Game.SIZE)){
                if (enemyBoard[y][x] == 0){
                    shotValid = true;
                }else{
                    System.out.println("Location already shot. Please retry: ");
                }
            }else{
                System.out.println("Shot invalid - please re-enter");
            }

            shotLocation = new ItemLocation(x, y);

            if (shotValid) {
                enemyBoard[y][x] = 1;
                pastShots.add(shotLocation);
            }
        }

        return shotLocation;
    }

    @Override
    public void setResult(boolean hit, boolean sunk) {

        //In case something is wrong and no shot was previously given
        if (pastShots.size() == 0){
            System.out.println("No shot fired yet");
            return;
        }
        System.out.println("Checking if you hit something...");
        Location lastShot = pastShots.get(pastShots.size() - 1);

        if (sunk){
            System.out.println("Sunk!");
            // Use 3 to show the shot that sunk the ship
            changeBoard(enemyBoard, lastShot, 1, false, 3);
        }else {
            if (hit){
                System.out.println("Hit!");
                // Use 2 to show the shot that hit a ship
                // Otherwise, all shots are represented by 1
                changeBoard(enemyBoard, lastShot, 1, false, 2);
            }else{
                System.out.println("Nah you missed");
            }
        }

        System.out.println("Now the enemy's board looks like:");
        printBoard(enemyBoard);
        System.out.println("where 0 is unknown, 1 is shot & miss, 2 is hit, 3 is the shot that sank a ship");
    }

    /**
     * Same as in BattleShipGame - determines if ship placement is valid
     */
    private boolean shipIsValid(Location ship, int size, int[][] board){
        if (ship.getX() < 0 || ship.getX() >= Game.SIZE) return false;
        if (ship.getY() < 0 || ship.getY() >= Game.SIZE) return false;


        if (ship.isShipHorizontal()){
            if ( (ship.getX() + size - 1)  >= Game.SIZE) return false;

            for (int i=0; i < size; i++){
                if (board[ship.getY()][ship.getX()+i] > 0){
                    return false;
                }
            }
        }else{
            if ( (ship.getY() + size - 1)  >= Game.SIZE) return false;

            for (int j=0; j < size; j++){
                if (board[ship.getY()+j][ship.getX()] > 0){
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Helper function to visualize a board
     */
    private void printBoard(int[][] board){

        for(int[] row : board) {
            for (int l : row) {
                System.out.print(l);
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    /**
     * Helper function to get int input from human
     * @param msg String message to display for human user
     * @return int we are looking for from user
     */
    private int getHumanInt(String msg){
        System.out.println(msg);

        while (!sc.hasNextInt()) {
            System.out.println("That's not an int!");
            sc.next();
        }

        return sc.nextInt();
    }

    /**
     * Use this method to make changes on the board - add ship, remove ship, add shot, etc
     * @param targetBoard user's or enemy's board
     * @param startLocation Location object to be changed
     * @param size Size of the change - For adding ships, size of ship
     * @param horizontal For editing ships - size of ship
     * @param content What you would like to change the board into
     */
    private void changeBoard(int[][] targetBoard, Location startLocation, int size, boolean horizontal, int content){
        if (horizontal){
            for (int j=0; j < size; j++){
                targetBoard[startLocation.getY()][startLocation.getX()+j] = content;
            }
        }else{
            for (int j=0; j < size; j++){
                targetBoard[startLocation.getY()+j][startLocation.getX()] = content;
            }
        }
    }

}
