/**
 * Created by Qi_He on Feb/1/16.
 */
public class BattleShipGame implements Game {
    private int[][] player1Board;
    private int[][] player2Board;
    private Player player1;
    private Player player2;
    private int[] ships;

    @Override
    public void initialize(Player p1, Player p2) {

        // Array[a][b] means b = x, a = y
        player1Board = new int[SIZE][SIZE];
        player2Board = new int[SIZE][SIZE];

        int[][] tempBoard;
        int counter;

        // Use an array to help complete the placeship process
        ships = new int[6];

        // Use this index - 1-5 - to represent ships on the board
        ships[0] = 0;
        ships[1] = CARRIER;
        ships[2] = BATTLESHIP;
        ships[3] = SUBMARINE;
        ships[4] = CRUISER;
        ships[5] = DESTROYER;

        // Use a Player array to help iterate
        Player[] players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // Save these player instances for playGame;
        player1 = p1;
        player2 = p2;

        //Repeat ship placement for each player
        //Counter keeps track of which player it is
        counter = 0;

        for (Player p: players){

            //Use tempBoard for each player.
            //Then update instance var at the end
            tempBoard = new int[SIZE][SIZE];

            // Use k to iterate the ships array
            // Note k must start with 1
            for (int k=1; k<6; k++){

                // Ships[k] is the size of the specific ship
                Location shipLocation = p.placeShip(ships[k], false);

                // Retry until location is valid
                while (! (shipIsValid(shipLocation, ships[k], tempBoard))){
                    shipLocation = p.placeShip(ships[k], true);
                }

                //Fill board with this ship - use index to represent the ship at all spots
                if (shipLocation.isShipHorizontal()){
                    for (int j=0; j < ships[k]; j++){
                        tempBoard[shipLocation.getY()][shipLocation.getX()+j] = k;
                    }
                }else{
                    for (int j=0; j < ships[k]; j++){
                        tempBoard[shipLocation.getY()+j][shipLocation.getX()] = k;
                    }
                }
            }

            // Now set the board to appropriate player's board
            if (counter == 0){
                copyBoard(player1Board, tempBoard);
            }else{
                copyBoard(player2Board, tempBoard);
            }
            counter++;
        }

        System.out.println("Game set up");
//        printBoard(player1Board);
        System.out.println("-------------------");
        System.out.println("*******************");
        System.out.println("-------------------");
    }

    @Override
    public Player playGame() {
        System.out.println("Play Game");
        System.out.println("-------------------");
        System.out.println("*******************");
        System.out.println("-------------------");


        while (true) {
            if (playOneMove(player2Board, player1) == 1) {
                return player1;
            }else{
                if (playOneMove(player1Board, player2) == 1){
                    return player2;
                }
            }

        }

    }

    /**
     * Determines if a ship placement is valid
     * @param ship Location of the ship
     * @param size Size of ship
     * @param board Board in consideration
     * @return whether if it's a legal placement
     */
    private boolean shipIsValid(Location ship, int size, int[][] board){
        if (ship.getX() < 0 || ship.getX() >= SIZE) return false;
        if (ship.getY() < 0 || ship.getY() >= SIZE) return false;


        if (ship.isShipHorizontal()){
            if ( (ship.getX() + size - 1)  >= SIZE) return false;

            for (int i=0; i < size; i++){
                if (board[ship.getY()][ship.getX()+i] > 0){
                    return false;
                }
            }
        }else{
            if ( (ship.getY() + size - 1)  >= SIZE) return false;

            for (int j=0; j < size; j++){
                if (board[ship.getY()+j][ship.getX()] > 0){
                    return false;
                }
            }
        }

        return true;
    }

//    /**
//     * Helper function to visualize a board
//     */
//    private void printBoard(int[][] board){
//
//        for(int[] row : board) {
//            for (int l : row) {
//                System.out.print(l);
//                System.out.print("\t");
//            }
//            System.out.println();
//        }
//    }

    /**
     * Check if a board's ships are all sunk - set to 0
     * @param board check this player's board
     * @return if the game just ended
     */
    private boolean gameOver(int[][] board){
        for(int[] row : board) {
            for (int l : row) {
                if (l != 0) return false;
            }
        }
        return true;
    }


    /**
     * Use this to copy a temp board to a player board instance
     * @param destination board
     * @param source board
     */
    private void copyBoard(int[][] destination, int[][] source){
        for (int row = 0; row < source.length; row++){
            System.arraycopy(source[row], 0, destination[row], 0, source[0].length);
        }
    }


    /** Seach the whole board to see if the given shipIndex is still on the board
     * @param board given a board
     * @param shipIndex find this index on the board's values
     * @return true if found, false if not
     */
    private boolean searchBoard(int[][] board, int shipIndex){
        for(int[] row : board) {
            for (int l : row) {
                if (l == shipIndex) return true;
            }
        }
        return false;
    }


    /**
     * Execute one target and record on the target's board
     * @param targetBoard Board to record the impact
     * @param attacker Player who initiates the attack
     * @return 1 if game is over, 0 otherwise
     */
    private int playOneMove(int[][] targetBoard, Player attacker){
        Location shot = attacker.getTarget();

        // Check if valid
        int x = shot.getX();
        int y = shot.getY();
        if ((x < 0 || x >= SIZE) || (y < 0 || y >= Game.SIZE)){
            attacker.setResult(false,false);
            return 0;
        }

        // That's a hit
        if (targetBoard[shot.getY()][shot.getX()] > 0){

            // temp represents the index of the ship that's been hit
            int temp = targetBoard[shot.getY()][shot.getX()];

            // When shot, change the value to 0
            targetBoard[shot.getY()][shot.getX()] = 0;

            // Check if there's still any of this ship left
            if (searchBoard(targetBoard, temp)){
                attacker.setResult(true, false);
            }else{
                // No such ship left - sunk
                attacker.setResult(true, true);
                if (gameOver(targetBoard)) return 1;
            }

            return 0;

        }else{
            // Miss...
            attacker.setResult(false,false);
            return 0;
        }


    }


}
