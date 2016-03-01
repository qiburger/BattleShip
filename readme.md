
How it works:

	- Boards:
		- Represented by 2d array of int
			- x is the horizontal index, and y is vertical 
			- Represented as board[y][x]


		- In game class, the value of each location is the "ship index"
			-       ships[1] = CARRIER;
			        ships[2] = BATTLESHIP;
			        ships[3] = SUBMARINE;
			        ships[4] = CRUISER;
			        ships[5] = DESTROYER;
			- When shot, the game changes the location's value to 0


		- In player class, however, it's more user friendly to represent their board using the size of the ship.
			- So a size 5 horiztonal ship would show up as 5	5	5	5	5

		- We also keep track of the enemy's board in each Player. The value at each location is represented using this scheme:
			- 0 is unknown, 1 is shot, 2 is hit, 3 is the shot that sank a ship

			- This design is due to the fact that we don't know enemy ship placements at first, and we don't know which boat we sank when there are connected/adjacent boats


	- Keeping track of boards:
		- Since we don't know how Swap's Game interface will be implemented, we let the player class keep track of its own ship placement and past shots.
		- Human player class will print out the board for the user whenever it's helpful
			- it will also print out an enemy board to help the user decide where to shoot
			- of course, the enemy board is only based on information from setResults


	- Set results:
		- For Human Player, because we keep track of the last shot and a private enemy board, we can show whenever a ship is sunk. Then the user can guess the size of the boat.


	- Checking if a location it's valid:
		- For ships:
			- Both Player and Game implementations will make sure the ship placement is legal. If not (something goes wrong), the player implementation also handles it via retry!!

		- For shots:
			- Both game and player implementations check if a shot is valid. Player will ask user/computer to retry, and the game will simply deem the shot as WASTED. Note that any repeat shots are also WASTED shots in the game!!


	- Helper methods:
		- All private, shouldn't break interface.
		- See javadoc for their use
		- shipIsValid and changeBoard are two major ones
			- first one just checks if the location is within bounds and no overlaps
			- second one changes the value on a 2D array board. Works for both ship and shot


	- Finally, game play:
		- Initialize with the help of private ship array and player array. 
		- Game starts when place ship is all set
		- Because the way we set up the board (see above), we can easily search the board to see if a ship is on the board or if the game is over
		- For hits, simply change the value of the location to 0. 


How to play as Human Player:
	- Simply run the Test class to play Human vs Computer (a dumb one).
	- Then follow the instructions to place ship and take shots. Should be quite straight forward.
	- Check above if you are confused about the representation of boards.


How the computer works:
	- Dumb computer is just places ships and make shots randomly, as long as the locations are valid and not repeated.


How to debug / cheat:
	- Un-comment the printBoard method in my Game implementation. Then feel free to print any board during game

Tournament Strat:
	- Place ships without touching each other or the edges
	- Hunt and Target
	- For hunt:
		- Manually implement the prob model used in http://www.datagenetics.com/blog/december32011/