import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * Created by Qi_He on Feb/6/16.
 */
public class DumbComputer implements Player {
    private int[][] myBoard;
    private int[][] enemyBoard;
    private Location lastPlacement;
    private boolean lastOrientation;
    private Random rand;

    public DumbComputer(){
        myBoard = new int[Game.SIZE][Game.SIZE];
        enemyBoard = new int[Game.SIZE][Game.SIZE];
        lastPlacement = null;
        lastOrientation = false;
        rand = new Random();
    }


    @Override
    public Location placeShip(int size, boolean retry) {
        boolean shipValid = false;

        Location sl = null;
        boolean horizontal = false;

        if (retry && (lastPlacement != null)){
            changeBoard(myBoard, lastPlacement, size, lastOrientation, 0);
        }

        while(!shipValid){

            int x = rand.nextInt(Game.SIZE);
            int y = rand.nextInt(Game.SIZE);
            int direction = rand.nextInt(2);

            horizontal = direction == 0;
            sl = new ItemLocation(x, y, horizontal);

            shipValid = shipIsValid(sl, size, myBoard);
        }

        changeBoard(myBoard, sl, size ,horizontal, size);
        lastPlacement = sl;
        lastOrientation = horizontal;

        return sl;

    }

    @Override
    public Location getTarget() {
        boolean shotValid = false;
        Location shotLocation;
        int x=0;
        int y=0;

        while(!shotValid) {

            x = rand.nextInt(Game.SIZE);
            y = rand.nextInt(Game.SIZE);

            // check if shot is valid
            if ((x >= 0 && x < Game.SIZE) && (y >= 0 && y < Game.SIZE)) {
                if (enemyBoard[y][x] == 0) {
                    shotValid = true;
                }
            }
        }

        shotLocation = new ItemLocation(x, y);

        enemyBoard[y][x] = 1;

        return shotLocation;
    }

    @Override
    public void setResult(boolean hit, boolean sunk) {
        //DO NOTHING
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
