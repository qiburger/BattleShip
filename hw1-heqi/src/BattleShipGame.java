/**
 * Created by Qi_He on Feb/1/16.
 */
public class BattleShipGame implements Game {
    private int[][] player1Board;
    private int[][] player2Board;
    private int player1ShipLeft;
    private int player2ShipLeft;
    private Player player1;
    private Player player2;
    int[] ships;

    @Override
    public void initialize(Player p1, Player p2) {

        // Array[a][b] means b = x, a = y
        player1Board = new int[SIZE][SIZE];
        player2Board = new int[SIZE][SIZE];

        int[][] tempBoard;
        int counter;

        // Use an array to help complete the placeship process
        ships = new int[5];

        // Use this index - 0-4 - to represent ships on the board
        ships[0] = CARRIER;
        ships[1] = BATTLESHIP;
        ships[2] = SUBMARINE;
        ships[3] = CRUISER;
        ships[4] = DESTROYER;

        // Use a Player array to help iterate
        Player[] players = new Player[2];
        players[0] = p1;
        players[1] = p2;

        // Save these instances for playGame;
        player1 = p1;
        player2 = p2;

        //Repeat ship placement for each player
        //Counter keeps track of which player it is
        counter = 0;

        for (Player p: players){

            //Use tempBoard for each player.
            //Then update instance var at the end
            tempBoard = new int[SIZE][SIZE];

            // Use k to iterate the ships[5] array
            for (int k=0; k<5; k++){

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
                player1Board = tempBoard;
            }else{
                player2Board = tempBoard;
            }
            counter++;
        }

        System.out.println("Game set up");

        for (int k : ships){
            player1ShipLeft += k;
            player2ShipLeft += k;
        }

        printBoard(player1Board);
        printBoard(player2Board);
    }

    @Override
    public Player playGame() {

            Location shot = player1.getTarget();
            return null;

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
                if (board[ship.getY()][ship.getX()+i] == 1){
                    return false;
                }
            }
        }else{
            if ( (ship.getY() + size - 1)  >= SIZE) return false;

            for (int j=0; j < size; j++){
                if (board[ship.getY()+j][ship.getX()] == 1){
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


}
