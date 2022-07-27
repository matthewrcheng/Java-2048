package edu.wm.cs.cs301.game2048;

public interface GameState {

	///// Methods to get/set the values on the board
	/**
	 * Gets the value of the tile on the board at the given position.
	 * Index is zero based.
	 * @param xCoordinate for drawing on x-axis, range 0,1,2,3
	 * @param yCoordinate for drawing on y-axis, range 0,1,2,3
	 * @return the value at the given (x,y) position
	 */
	int getValue(int xCoordinate, int yCoordinate);
	/**
	 * Sets the value of the tile on the board at the given position.
	 * Index is zero based.
	 * @param xCoordinate for drawing on x-axis, range 0,1,2,3
	 * @param yCoordinate for drawing on y-axis, range 0,1,2,3
	 * @param value for the tile, expected values are 0 and powers of 2
	 */
	void setValue(int xCoordinate, int yCoordinate, int value);
	/**
	 * Sets the values of all tiles on the board to 0.
	 */
	void setEmptyBoard();
	/**
	 * Randomly selects a tile on the board that currently carries
	 * a value of 0 and sets it to a randomly selected value of either
	 * 2 or 4. 
	 * @return true if value setting worked, false otherwise
	 */
	boolean addTile();
	/**
	 * Tells if all tiles on the board have a value greater than 0.
	 * @return true if the board is full, false otherwise
	 */
	boolean isFull();
	/**
	 * Tells if there is at least one row or column with a pair 
	 * of tiles that carry the same value and could be merged
	 * by one of the four move operations (left, right, up, down).
	 * @return true if a merge of tiles is possible, false otherwise
	 */
	boolean canMerge();
	/**
	 * Tells if the highest value of a tile reached the threshold
	 * to win the game, i.e., if the max value is greater of equal 2048.
	 * @return true if the threshold is reached, false otherwise
	 */
	boolean reachedThreshold();
	
	//// Move operations //////////////////////////////
	/**
	 * Moving all tiles as much to the left as possible
	 * and merging tiles according to the rules of 2048.
	 * The method returns the sum of values of all tiles
	 * that merged.
	 * @return total extra points for the score from this move
	 */
	int left();
	/**
	 * Moving all tiles as much to the right as possible
	 * and merging tiles according to the rules of 2048.
	 * The method returns the sum of values of all tiles
	 * that merged.
	 * @return total extra points for the score from this move
	 */
	int right();
	/**
	 * Moving all tiles as much down as possible
	 * and merging tiles according to the rules of 2048.
	 * The method returns the sum of values of all tiles
	 * that merged.
	 * @return total extra points for the score from this move
	 */
	int down();
	/**
	 * Moving all tiles as much up as possible
	 * and merging tiles according to the rules of 2048.
	 * The method returns the sum of values of all tiles
	 * that merged.
	 * @return total extra points for the score from this move
	 */
	int up();

}