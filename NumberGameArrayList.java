package project2;


import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/*********************************************************************
 * NumberGameArrayList class that implements the NumberSlider
 * interface
 * @author Jessica Kressner
 * @author Natalie Taylor
 * @version November 2nd, 2021
 */
public class NumberGameArrayList implements NumberSlider {
	/**
	 * two-dimensional arraylist
	 */
	private int grid[][];

	/**
	 * random number object
	 */
	private static Random ranum;

	/**
	 * number of rows
	 */
	private static int rows = 4;

	/**
	 * number of columns
	 */
	private static int columns = 4;

	/**
	 * winningValue which is the value that must be on the board to win
	 */
	private static int winningValue = 1024;

	/**
	 * gameStatus of the game that is being played
	 */
	private GameStatus gameStatus;

	/**
	 * stack
	 */
	private Stack<int[][]> stack;

	/**************************************************
	 * Constructor for the NumberGameArrayList class
	 */
	public NumberGameArrayList() {
		// creates a new grid two-dimensional array
		grid = new int[rows][columns];

		// creates a new stack
		stack = new Stack<>();

		// updates gameStatus
		gameStatus = GameStatus.IN_PROGRESS;

		// places two random values on the game board
		placeRandomValue();
		placeRandomValue();
	}
	public int GetRows()
	{
		int rows = 0;
		if (grid !=null)
		{rows = grid.length;}
		return rows;
	}
	public int GetColumns()
	{
		return grid[0].length;
	}

	public int GetGoal()
	{
		return winningValue;
	}
	public void SetGoal(int goal) {
		winningValue = goal;
	}

	/**
	 * Reset the game logic to handle a board of a given dimension
	 *
	 * @param height the number of rows in the board
	 * @param width the number of columns in the board
	 * @param winningValue the value that must appear on the board to
	 * win the game
	 * @throws IllegalArgumentException if the parameters of height, width,
	 * and winningValue are negative or if the winningValue is not a power
	 * of two
	 */
	@Override
	public void resizeBoard(int height, int width, int winningValue) {
		// checks for valid parameters
		if (height < 0 || width < 0 || winningValue < 0 ||
				!(isPowerOfTwo(winningValue)))
			throw new IllegalArgumentException();

		// sets winningValue to the static variable winningValue
		this.winningValue = winningValue;
		Cell cell = new Cell();

		// creates a new board with the given dimensions
		grid = new int[height][width];
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				cell.row = r;
				cell.column = c;
				cell.value = grid[r][c];
			}
		}

		// updates gameStatus
		gameStatus = gameStatus.IN_PROGRESS;
	}

	/**
	 * Private helper method that determines if the winningValue is
	 * a power of two
	 *
	 * @param winningValue
	 * @return true when the winningValue is a power of two
	 */
	private boolean isPowerOfTwo(int winningValue) {
		int i = 1;
		while (i < winningValue)
			i = i * 2;
		return i == winningValue;
	}

	/**
	 * Remove all numbered tiles from the board and place
	 * two non-zero numbers at a random location
	 */
	@Override
	public void reset() {
		// creates a new cell object
		Cell cell = new Cell();

		// iterates through the two-dimensional array
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				cell.setValue(0);
			}
		}

		// places two random values on the game board
		placeRandomValue();
		placeRandomValue();

		// updates gameStatus
		gameStatus = GameStatus.IN_PROGRESS;

	}

	/**
	 * Set the game board to the desired values in the 2D array
	 *
	 * @param ref
	 */
	@Override
	public void setValues(int[][] ref) {
		// creates a new cell object
		Cell cell = new Cell();

		// creates a new grid that is the same dimensions as ref
		grid = new int[ref.length][ref[0].length];

		//iterates through the two-dimensional array
		for (int r = 0; r < ref.length; r++) {
			for (int c = 0; c < ref[0].length; c++) {
				// sets the value in each row and column to the same as ref
				cell.row = r;
				cell.column = c;
				grid[r][c] = ref[r][c];
				cell.value = grid[r][c];
			}
		}

		// updates gameStatus if the game is lost
		if (ifLost()) {
			gameStatus = GameStatus.USER_LOST;
		}
	}

	/**
	 * Insert a random tile into an empty spot on the game board
	 *
	 * @return a Cell object with its row, column, and value
	 * attributes initialized properly
	 * @throws IllegalStateException when the board has no empty cell
	 */

	@Override
	public Cell placeRandomValue() {
		// creates a new arraylist of empty spots
		ArrayList emptySpot = new ArrayList();

		// creates a new random object
		Random ranum = new Random();

		// iterates through the two-dimensional array
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				// if the tile is zero then add to emptySpot arraylist
				if (grid[r][c] == 0) {
					emptySpot.add(new Cell(r, c, 0));
				}
			}
		}

		// creates a new Cell object
		Cell ranumCell = null;

		// if the arraylist is not zero then it returns the randomCell
		if (emptySpot.size() != 0) {
			ranumCell = (Cell) emptySpot.get(ranum.nextInt(emptySpot.size()));
			ranumCell.setValue((int) Math.pow(2, ranum.nextInt(2) + 1));
			grid[ranumCell.getRow()][ranumCell.getColumn()] = ranumCell.getValue();
		}

		// updates gameStatus if game is lost
		if (ifLost()) {
			gameStatus = GameStatus.USER_LOST;

		}
		return ranumCell;
	}

	/**
	 * Slide all the tiles in the board in the requested direction and
	 * place a random value on the board. The random value should be
	 * a 2 or a 4
	 *
	 * @param dir move direction of the tiles
	 * @return true when the board changes
	 */
	@Override
	public boolean slide(SlideDirection dir) {
		// creates a new boolean variable that is for if the board changes
		boolean boardChanged = false;
		// creates a copy of the grid and pushes it back
		int[][] acopy = new int[grid.length][grid[0].length];
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				acopy[r][c] = grid[r][c];
			}
		}
		stack.push(acopy);
		// slides a certain direction depending on the direction of the slide
		if (dir == SlideDirection.RIGHT) boardChanged = moveRight();

		else if (dir == SlideDirection.LEFT) boardChanged = moveLeft();

		else if (dir == SlideDirection.UP) boardChanged = moveUp();

		else if (dir == SlideDirection.DOWN) boardChanged = moveDown();
		// updates gameStatus if the game is lost
		if (ifLost()) {
			gameStatus = GameStatus.USER_LOST;
		}

		// checks if the game is in progress
		if (gameStatus != GameStatus.USER_WON && gameStatus !=
				GameStatus.USER_LOST) {
			if (boardChanged) {
				// places a random value on the game board
				placeRandomValue();

			}
		}

		return boardChanged;
	}

	/**************************************************************************
	 * Creates a mirrored array called AfterSlide that takes out
	 * the each row of the grid and combines the values to the left.
	 * @return boardChanged - whether or not a move has been made
	 ***************************************************************************/
	private boolean moveLeft() {
		// creates a boolean variable that is for if the board changes
		boolean boardChanged = false;

		// creates an integer for the length of the row and the column
		int endRow = grid.length;
		int endColumn = grid[0].length;

		// creates an array of the length of the column
		int[] AfterSlide = new int[grid[0].length];

		// iterates through the rows
		for (int r = 0; r < endRow; r++) {

			// the array after the slide
			AfterSlide = slideAction(grid[r]);

			// if the before and after array don't equal the
			// board changed
			if (!AreArraysEqual(grid[r], AfterSlide)) {
				boardChanged = true;

				// iterates through the columns
				for (int c = 0; c < endColumn; c++) {
					//c = number of columns and the index of rows
					grid[r][c] = AfterSlide[c];

					// updates gameStatus if the winningValue is
					// on the board
					if (grid[r][c] == winningValue) {
						gameStatus = GameStatus.USER_WON;
					}
				}
			}
		}

		return boardChanged;
	}

	/****************************************************************
	 * Creates a mirrored array called AfterSlide that takes out
	 * each row of the grid and combines the values to the right.
	 * @return boardChanged - whether or not a move has been made
	 **************************************************************/
	private boolean moveRight() {

		// creates a boolean variable for if the board changed
		boolean boardChanged = false;

		// creates an integer variable for the length of the
		// rows and columns
		int endRow= grid.length;
		int endColumn = grid[0].length;

		// creates an array for after the slide
		int[] AfterSlide = new int[grid[0].length];

		// iterates through the number of rows
		for (int r = 0; r < endRow; r++) {

			// creates an array for the reverse of the array
			int[] reverseRow = new int[grid[0].length];

			// clones the array of rows
			reverseRow = grid[r].clone();

			// reverse the reverseRow array
			ReverseArray(reverseRow);

			// array after the slide
			AfterSlide = slideAction(reverseRow);

			// if the before and after array do not equal the
			// board changed
			if (!AreArraysEqual(reverseRow, AfterSlide)) {
				boardChanged = true;

				// reverse the array after the slide
				ReverseArray(AfterSlide);

				// iterate through the number of columns
				for (int c = 0; c < endColumn; c++) {

					// sets the game board equal to the array values
					// after the slide
					grid[r][c] = AfterSlide[c];

					// updates the gameStatus if the winningValue is
					// on the board
					if (grid[r][c] == winningValue) {
						gameStatus = GameStatus.USER_WON;
					}
				}
			}
		}

		return boardChanged;
	}

	/****************************************************************
	 * Creates a mirrored array called AfterSlide that takes out
	 * each column of the grid and combines the values towards the bottom.
	 * @return boardChanged - whether or not a move has been made
	 **************************************************************/
	private boolean moveDown() {
		// creates a boolean variable for if the board changed
		boolean boardChanged = false;

		// creates an integer variable for the number of rows and columns
		int endRow = grid.length;
		int endColumn = grid[0].length;

		// creates an array for after the slide
		int[] AfterSlide = new int[grid[0].length];

		// iterates through the number of columns
		for (int c = 0; c < endColumn; c++) {

			// creates an array of the column values
			int[] colArray = new int[grid.length];

			// iterates through the number of rows
			for (int r = 0; r < endRow; r++) {
				// sets the value of the game board to the
				// column array
				colArray[r] = grid[r][c];
			}

			// reverses the column array
			ReverseArray(colArray);

			// sets the array after the slide
			AfterSlide = slideAction(colArray);

			// if the before and after array are not equal then the
			// board changed
			if (!AreArraysEqual(colArray, AfterSlide))
				boardChanged = true;

			// if the board changed reverse the array after the slide
			if (boardChanged) {
				ReverseArray(AfterSlide);

				// iterates through the number of rows
				for (int r = 0; r < endRow; r++) {

					// sets the value back to grid
					grid[r][c] = AfterSlide[r];

					// updates the gameStatus if the winningValue
					// is on the board
					if (grid[r][c] == winningValue)
						gameStatus = GameStatus.USER_WON;
				}
			}

		}

		return boardChanged;
	}

	/****************************************************************
	 * Creates a mirrored array called AfterSlide that takes out
	 * each column of the grid and combines the values towards the top.
	 * @return boardChanged - whether or not a move has been made
	 **************************************************************/
	private boolean moveUp() {
		// creates a boolean variable for the board changed
		boolean boardChanged = false;

		// creates an integer variable for the number of rows and columns
		int endRow = grid.length;
		int endColumn = grid[0].length;

		// creates an array for after the slide
		int[] AfterSlide = new int[grid[0].length];

		// iterates through the number of columns
		for (int c = 0; c < endColumn; c++) {

			// creates an array of columns
			int[] colArray = new int[grid.length];

			// iterates through the number of rows
			for (int r = 0; r < endRow; r++) {

				// sets the column array to the value of the board
				colArray[r] = grid[r][c];
			}

			// sets the after array to the slide column array
			AfterSlide = slideAction(colArray);

			// if the before and after array are equal board changed
			if (!AreArraysEqual(colArray, AfterSlide))
				boardChanged = true;

			// if the board changed go through the number of rows
			if (boardChanged) {
				for (int r = 0; r < endRow; r++) {
					grid[r][c] = AfterSlide[r];

					// updates gameStatus if the winningValue is
					// on the board
					if (grid[r][c] == winningValue)
						gameStatus = GameStatus.USER_WON;
				}
			}
		}
		return boardChanged;
	}

	/***********************************************
	 * Creates two mirrored arrays. noEmpty takes in
	 * the nonzero values in the grid, while finalArray
	 * holds the entire array to be put back into the
	 * grid.
	 * @return finalArray - the array to be put back into
	 * the grid.
	 **************************************************/
	public int[] slideAction(int[] refarray) {
		// gets the size of the array
		int arraySize = refarray.length;
		int x = 0;

		// creates an array for no empty values
		int[] noEmpty = new int[arraySize];

		// creates an array of values
		int[] finalArray = new int[arraySize];

		// iterates through the array
		for (int i = 0; i < arraySize; i++) {
			// checks if the value is not zero
			if (refarray[i] != 0) {
				// add a value to the nonEmpty array
				noEmpty[x++] = refarray[i];
			}
		}
		// combine values of the board
		x = 0;

		// iterate through the array
		for (int i = 0; i < arraySize; i++) {

			// if the value is not zero then combine values
			if (noEmpty[i] != 0) {
				// add the last array value to array
				if (i + 1 == arraySize) {
					finalArray[x] = noEmpty[i];

					// if the value is the same then multiply by 2
				} else if (noEmpty[i] == noEmpty[i + 1]) {
					finalArray[x++] = noEmpty[i++] * 2;
				} else {
					// add value to array
					finalArray[x++] = noEmpty[i];
				}
			}
		}
		return finalArray;
	}

	/****************************************************
	 * determines whether the two arrays are equal.
	 * @param a
	 * @param b
	 * @return isEqual - returns true or false
	 ******************************************************/
	private boolean AreArraysEqual ( int[] a, int[] b){
		    // creates a boolean variable for if the arrays are equal
			boolean ArraysAreEqual = true;

			// checks if the array lengths are the same
			if (a.length != b.length) {
				ArraysAreEqual = false;
			} else {
				// Checks if the values are the same
				for (int i = 0; i < a.length; i++) {
					if (a[i] != b[i]) {
						// breaks out of the array if the
						// arrays are not equal
						ArraysAreEqual = false;
						break;
					}
				}
			}
			return ArraysAreEqual;
		}

	/************************************************************
	 * This method splits the array into two and proceedes to
	 * rearrange the values in the opposite spots.
	 * @param refArray - the given array from the grid
	 *************************************************************/
	private void ReverseArray ( int[] refArray){
		// splits array into 2 sections and reverses array
			for (int i = 0; i < refArray.length / 2;
				 i++) {
				int temp = refArray[i];
				refArray[i] = refArray[refArray.length - i - 1];
				refArray[refArray.length - i - 1] = temp;
			}
		}

	/***********************************************************
	 * This method goes through the entire grid to find if there
	 * are any possible moves. If the player loses, the game ends.
	 * @return playerLost - if it returns true, game over.
	 **********************************************************/
	private boolean ifLost() {
		// creates a boolean variable that is that the game is lost
		boolean playerLost = true;
		// checks if there are zeros on the board
		if (getNonEmptyTiles().size() > 0) {
			// checks if any move can be made
			for (int r = 0; r < grid.length; r++) {
				for (int c = 0; c < grid[0].length - 1; c++) {
					if (grid[r][c] == grid[r][c + 1]) {
						// break out of loop if there are more moves
						playerLost = false;
						break;
					}
				}
			}
			// checks if moves can be made
			if (playerLost) {
				for (int c = 0; c < grid[0].length; c++) {
					for (int r = 0; r < grid.length - 1; r++) {
						if (grid[r][c] == grid[r + 1][c]) {
							playerLost = false;
							break;
						}
					}
					// if game is not lost break
					if (!playerLost) break;
				}
			}
			// if the game is not lost if there is empty tiles
			if(playerLost){
				if ((grid.length*grid[0].length)- getNonEmptyTiles().size()>0){
					playerLost=false;
				}
			}
		}

		return playerLost;
	}

		/**
		 * Adds all non-zero tiles to an arraylist
		 * @return an arraylist of cells. Each cell holds the
		 * (row, column) and value of a tile
		 */
		@Override
		public ArrayList<Cell> getNonEmptyTiles () {
			// creates an arraylist of nonEmptyCells
			ArrayList<Cell> nonEmptyCells = new ArrayList<>();
			// creates a cell object
			Cell cell = new Cell();
			// iterates through the two-dimensional array
			for (int r = 0; r < grid.length; r++) {
				for (int c = 0; c < grid[r].length; c++) {
					// adds the cell to the arraylist if it is not zero
					if (grid[r][c] != 0) {
						cell.row = r;
						cell.column = c;
						cell.value = grid[r][c];
						nonEmptyCells.add(new Cell(cell.row, cell.column, cell.value));
					}
				}
			}
			return nonEmptyCells;
		}

		/**
		 * Return the current state of the game
		 * @return one of the possible values of GameStatus enum
		 */
		@Override
		public GameStatus getStatus () {
			return gameStatus;
		}

	/********************************************
	 * Uses a stack to remove the previous board.
	 **********************************************/
	@Override
	public void undo() {
			// if the stack is not empty then place into the stack
		    if (!stack.empty()) setValues(stack.pop());
	}
}
