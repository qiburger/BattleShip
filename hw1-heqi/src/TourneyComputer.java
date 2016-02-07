import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Qi_He on Feb/6/16.
 */
public class TourneyComputer implements Player {
    private int[][] myBoard;
    private int[][] enemyBoard;
    private Location lastPlacement;
    private boolean lastOrientation;
    private Random rand;
    private ArrayList<Location> sequence;
    private ArrayList<Location> randomPattern;
    private ArrayList<Location> subsequence;
    private int counter;
    private ArrayList<Location> pastShots;
    private Location targetCenter;
    private int hitDirection;
    private ArrayList<Location> targetStack;


    public TourneyComputer(){
        myBoard = new int[Game.SIZE][Game.SIZE];
        enemyBoard = new int[Game.SIZE][Game.SIZE];
        lastPlacement = null;
        lastOrientation = false;
        rand = new Random();
        counter = 0;

        sequence = new ArrayList<Location>();
        randomPattern = new ArrayList<Location>();
        subsequence = new ArrayList<Location>();

        sequence.add(new ItemLocation(4,4));
        sequence.add(new ItemLocation(5,5));
        sequence.add(new ItemLocation(3,6));
        sequence.add(new ItemLocation(6,3));
        sequence.add(new ItemLocation(3,3));
        sequence.add(new ItemLocation(6,6));
        sequence.add(new ItemLocation(2,7));
        sequence.add(new ItemLocation(2,2));
        sequence.add(new ItemLocation(7,2));
        sequence.add(new ItemLocation(7,7));
        sequence.add(new ItemLocation(2,4));
        sequence.add(new ItemLocation(7,5));
        sequence.add(new ItemLocation(4,2));
        sequence.add(new ItemLocation(5,7));

        subsequence.add(new ItemLocation(2,7));
        subsequence.add(new ItemLocation(2,2));
        subsequence.add(new ItemLocation(7,2));
        subsequence.add(new ItemLocation(7,7));
        subsequence.add(new ItemLocation(2,4));
        subsequence.add(new ItemLocation(7,5));
        subsequence.add(new ItemLocation(4,2));
        subsequence.add(new ItemLocation(5,7));


        for (int i = 0; i < Game.SIZE; i++){
            for (int j = 0; j < Game.SIZE; j++){
                if ((i + j) % 2 == 0) randomPattern.add(new ItemLocation(i, j));
            }
        }

        Collections.shuffle(randomPattern);

        pastShots = new ArrayList<Location>();


        hitDirection = 0;

        targetStack = new ArrayList<Location>();

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

            // Make sure ships are not touching
            if (shipValid){
                shipValid = noTouching(sl, size, myBoard);
                if (shipValid){
                    shipValid = noEdgeShip(sl, size, myBoard);
                }
            }
        }

        changeBoard(myBoard, sl, size ,horizontal, size);
        lastPlacement = sl;
        lastOrientation = horizontal;

        return sl;

    }

    @Override
    public Location getTarget() {

        if (targetStack.isEmpty()) {
            Location shot = hunt();
            pastShots.add(shot);
            enemyBoard[shot.getY()][shot.getX()] = 1;
            return shot;
        }else{
            Location shot = target();

            if (shotIsValid(shot)){
                pastShots.add(shot);
                enemyBoard[shot.getY()][shot.getX()] = 1;
                return shot;
            }else {
                shot = hunt();
                pastShots.add(shot);
                enemyBoard[shot.getY()][shot.getX()] = 1;
                return shot;
            }
        }
    }

    @Override
    public void setResult(boolean hit, boolean sunk) {

        Location lastShot = pastShots.get(pastShots.size() - 1);

        if (sunk){
            // Use 3 to show the shot that sunk the ship
            changeBoard(enemyBoard, lastShot, 1, false, 3);
            targetCenter = lastShot;
            addTargetToStack(lastShot);

        }else {
            if (hit){
                changeBoard(enemyBoard, lastShot, 1, false, 2);
                targetCenter = lastShot;
                addTargetToStack(lastShot);
            }

        }
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
     * Helper function to determine whether ships/shots are touching (immediately to the left, right, up, down)
     * @param location intended ship/shot placement
     * @param size size of ship; when it's a shot - size = 1
     * @param board current board, mine or enemy's
     * @return true if no ship is touching
     */
    private boolean noTouching(Location location, int size, int[][] board){


        if (location.isShipHorizontal()){

            if (location.getX() > 0) {
                if (board[location.getY()][location.getX() - 1] > 0) {
                    return false;
                }
            }
            if (location.getX() + size < (Game.SIZE)) {
                if (board[location.getY()][location.getX() + size] > 0) {
                    return false;
                }
            }

            for (int i=0; i < size; i++){
                if (location.getY() < (Game.SIZE - 1)){

                    if (board[location.getY()+1][location.getX()+i] > 0) {
                        return false;
                    }
                }
                if (location.getY() > 0){
                    if (board[location.getY()-1][location.getX()+i] > 0) {
                        return false;
                    }
                }
            }
        }
        else{
            if (location.getY() > 0) {
                if (board[location.getY() - 1][location.getX()] > 0) {
                    return false;
                }
            }
            if (location.getY() + size < (Game.SIZE)) {
                if (board[location.getY() + size][location.getX()] > 0) {
                    return false;
                }
            }

            for (int j=0; j < size; j++){
                if (location.getX() < (Game.SIZE - 1)){

                    if (board[location.getY() + j][location.getX() + 1] > 0) {
                        return false;
                    }
                }
                if (location.getX() > 0){
                    if (board[location.getY() + j][location.getX() - 1] > 0) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Similiar to above but to make sure the ship does not touch the edge
     * @return true if the ship does not touch the edge
     */
    private boolean noEdgeShip(Location location, int size, int[][] board){
        if (location.getX()==0 || location.getX() == 9){
            return false;
        }
        if (location.isShipHorizontal() && (location.getX()+size-1) == 9){
            return false;
        }
        if (location.getY()==0 || location.getY() == 9){
            return false;
        }
        if ((!location.isShipHorizontal()) && (location.getY()+size-1) == 9){
            return false;
        }

        return true;
    }


    /**
     * @param center use this to find all adjacent shots
     * @return possible adjacent shot - STILL NEED TO CHECK if it's VALID!!!!
     */
    private Location findAdjacentShot(Location center){
        int x = center.getX();
        int y = center.getY();

        Location shot = new ItemLocation(x-1, y-1);
        if (!shotIsValid(shot)){
            shot = new ItemLocation(x+1, y-1);
        }
        if (!shotIsValid(shot)){
            shot = new ItemLocation(x-1, y+1);
        }
        if (!shotIsValid(shot)){
            shot = new ItemLocation(x+1, y+1);
        }

        return shot;

    }

    /**
     * Check our stored previous shots on "enemyBoard" to see if shot is valid
     * @param shot Location to be tested
     * @return true if it's a valid shot - no repeat or out of bounds
     */
    private boolean shotIsValid(Location shot){
        int x = shot.getX();
        int y = shot.getY();

        if ((x >= 0 && x < Game.SIZE) && (y >= 0 && y < Game.SIZE)){
            if (enemyBoard[y][x] == 0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
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


    /**
     * Recursively find the next valid adjacent shot
     * If the subsequence is empty, then return an invalid shot
     * @return the next valid adj shot
     */
    private Location getNextAdjacentShot() {

        if (subsequence.isEmpty()){
            Location out = new ItemLocation(4,4);
            return out;
        }

        Location center = subsequence.get(0);
        Location adjShot = findAdjacentShot(center);

        if (shotIsValid(adjShot)) {
            subsequence.add(adjShot);
            counter++;
            return adjShot;
        } else {
            subsequence.remove(center);
            counter++;
            return getNextAdjacentShot();
        }
    }


    /**
     * Implement the "target" in hunt and target
     * WHen running out of shot options, set hunt as true!
     * @return next shot to target
     */
    private Location target(){
        Location target = targetStack.get(targetStack.size()-1);
        targetStack.remove(target);

        while(! shotIsValid(target)){
            if (targetStack.isEmpty()){
                return target;
            }
            target = targetStack.get(targetStack.size()-1);
            targetStack.remove(target);
        }

        return target;
    }

    /**
     * Implement the "hunt" in hunt and target
     * @return next shot to target
     */
    private Location hunt(){
        if (!sequence.isEmpty()) {
            Location shot = sequence.get(0);
            sequence.remove(shot);

            while (!shotIsValid(shot) && (!sequence.isEmpty())) {
                shot = sequence.get(0);
                sequence.remove(shot);
            }

            if (shotIsValid(shot)){
                return shot;
            }

        }
        if (counter < 55) {
            Location shot = getNextAdjacentShot();
            if (shotIsValid(shot)){
                return shot;
            }
        }

        if (randomPattern.isEmpty()){

            return getRandomShot();
        }


        Location shot = randomPattern.get(0);
        randomPattern.remove(shot);

        while ((!shotIsValid(shot))) {
            if (randomPattern.isEmpty()) return getRandomShot();

            shot = randomPattern.get(0);
            randomPattern.remove(shot);
        }

        return shot;
    }

    private void addTargetToStack(Location hit){
        int x = hit.getX();
        int y = hit.getY();

        targetStack.add(new ItemLocation(x-1, y));
        targetStack.add(new ItemLocation(x+1, y));
        targetStack.add(new ItemLocation(x, y-1));
        targetStack.add(new ItemLocation(x, y+1));

    }

    private Location getRandomShot(){
        Location shot = new ItemLocation(rand.nextInt(Game.SIZE), rand.nextInt(Game.SIZE));

        while(!shotIsValid(shot)) {

            shot = new ItemLocation(rand.nextInt(Game.SIZE), rand.nextInt(Game.SIZE));
        }
        return shot;
    }
}
