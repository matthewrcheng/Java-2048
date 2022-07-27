package edu.wm.cs.cs301.game2048;

import java.util.Arrays;
import java.util.Random;

public class State implements GameState {

	public int[][] gameArray = new int[4][4];
	
	public Random random = new Random();
	
	public State(GameState original) {
		// TODO Auto-generated constructor stub
		// copy each value from original into the new GameState.gameArray
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gameArray[i][j] = original.getValue(j,i);
			}
		}
	}
	
	// TODO Auto-generated constructor stub 
	public State() {
		// since there is no original to copy, create a GameState with all 0's
		this.setEmptyBoard();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(gameArray);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		// returns true if the two objects are references to the same thing
		// or if all of their elements in gameArray are the same
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		return Arrays.deepEquals(gameArray, other.gameArray);
	}

	@Override
	public int getValue(int xCoordinate, int yCoordinate) {
		// TODO Auto-generated method stub
		/**
		 * Gets the value of the tile on the board at the given position.
		 * Index is zero based.
		 * @param xCoordinate for drawing on x-axis, range 0,1,2,3
		 * @param yCoordinate for drawing on y-axis, range 0,1,2,3
		 * @return the value at the given (x,y) position
		 */
		return this.gameArray[yCoordinate][xCoordinate];
	}

	@Override
	public void setValue(int xCoordinate, int yCoordinate, int value) {
		// TODO Auto-generated method stub
		/**
		 * Sets the value of the tile on the board at the given position.
		 * Index is zero based.
		 * @param xCoordinate for drawing on x-axis, range 0,1,2,3
		 * @param yCoordinate for drawing on y-axis, range 0,1,2,3
		 * @param value for the tile, expected values are 0 and powers of 2
		 */
		this.gameArray[yCoordinate][xCoordinate] = value;
	}

	@Override
	public void setEmptyBoard() {
		// TODO Auto-generated method stub
		/**
		 * Sets the values of all tiles on the board to 0.
		 */
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				this.setValue(i,j,0);
			}
		}
	}

	@Override
	public boolean addTile() {
		// TODO Auto-generated method stub
		/**
		 * Randomly selects a tile on the board that currently carries
		 * a value of 0 and sets it to a randomly selected value of either
		 * 2 or 4. 
		 * @return true if value setting worked, false otherwise
		 */
		
		// create a 2D array that can hold the coordinates of each tile
		int[][] zeroHolder = new int[16][2];
		// initialize a counter that will be used to keep track of the
		// amount of new tiles as well as index the zeroHolder
		int counter = 0;
		// two more integers that will be used as the coordinate of choice
		// and the value of choice for the added tile
		int choiceCoord, choiceVal;
		// go through each tile and if its value is zero, add its coordinates
		// to the zero holder, then increment the counter
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.getValue(j,i) == 0) {
					zeroHolder[counter][0] = j;
					zeroHolder[counter][1] = i;
					counter++;
				}
			}
		}
		// if there are any 0 tiles left, then use random to determine the
		// coordinate and whether the new tile will be a 2 or a 4
		// I do not use isFull() here, as all of the tiles must be checked
		// anyways, so I don't want to go through each tile twice
		if (counter > 0) {
			choiceCoord = random.nextInt(counter);
			choiceVal = 1+random.nextInt(2);
			this.setValue(zeroHolder[choiceCoord][0], zeroHolder[choiceCoord][1], 2*choiceVal);
			return true;
		}
		// if no tile was added, return false instead
		return false;
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		/**
		 * Tells if all tiles on the board have a value greater than 0.
		 * @return true if the board is full, false otherwise
		 */
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.getValue(i,j) == 0) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canMerge() {
		// TODO Auto-generated method stub
		/**
		 * Tells if there is at least one row or column with a pair 
		 * of tiles that carry the same value and could be merged
		 * by one of the four move operations (left, right, up, down).
		 * @return true if a merge of tiles is possible, false otherwise
		 */
		int toCheck;
		int k,l;
		// Goes through each tile and checks if there are any matches to the 
		// right or down. If there is a 0, it will keep checking for a match,
		// however, if there is a value that is not equal to 0 or that of the
		// tile, then it will stop looking and begin checking the next tile.
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				k = i+1;
				l = j+1;
				toCheck = this.getValue(j,i);
				while(k < 4) {
					if (this.getValue(j,k) == toCheck) {
						// if at any point there is a possible match, return true
						return true;
					}
					if (this.getValue(j,k) != 0) {
						break;
					}
					k++;
				}
				while(l < 4) {
					if (this.getValue(l,i) == toCheck) {
						// same as above
						return true;
					}
					if (this.getValue(l,i) != 0) {
						break;
					}
					l++;
				}
			}
		}
		// if all tiles have been checked and there are no options left,
		// then it must be the case that no tiles can merge, so return false
		return false;
	}

	@Override
	public boolean reachedThreshold() {
		// TODO Auto-generated method stub
		/**
		 * Tells if the highest value of a tile reached the threshold
		 * to win the game, i.e., if the max value is greater of equal 2048.
		 * @return true if the threshold is reached, false otherwise
		 */
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (this.getValue(i,j) == Game2048.WINNING_THRESHOLD) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int left() {
		// TODO Auto-generated method stub
		/**
		 * Moving all tiles as much to the left as possible
		 * and merging tiles according to the rules of 2048.
		 * The method returns the sum of values of all tiles
		 * that merged.
		 * @return total extra points for the score from this move
		 */
		int i,j,walk,value,newValue;
		// start score count
		int count = 0;
		// move through each tile starting from the upper left corner
		// since left-most tiles have precedence, do each row from 
		// left to right
		for (i = 1; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				value = this.getValue(i, j);
				// if the tile is not 0, then determine where to move it
				if (value != 0) {
					walk = i-1;
					// check spaces to the left of the tile
					while (walk >= 0) {
						// if we find a space that is not 0...
						if (this.getValue(walk, j) != 0) {
							// if the value is the same as the current tile,
							// double it and replace the original with 0
							if (this.getValue(walk, j) == value) {
								newValue = value*2;
								this.setValue(walk+1, j, 0);
								this.setValue(walk, j, newValue);
								// add the value to the score count
								count += newValue;
							} 
							// if the value is different, then the tile has
							// moved as far as it can
							else {
								break;
							}
						}
						// if the space is 0, then we move the tile to that space
						else {
							this.setValue(walk+1, j, 0);
							this.setValue(walk, j, value);
						}
						walk--;
					}
				}
			}
		}
		return count;
	}
	
	@Override
	public int right() {
		// TODO Auto-generated method stub
		/**
		 * Moving all tiles as much to the right as possible
		 * and merging tiles according to the rules of 2048.
		 * The method returns the sum of values of all tiles
		 * that merged.
		 * @return total extra points for the score from this move
		 */
		
		// this works the same as left, but it starts in the top right instead
		// it checks each row going from right to left, and when checking for
		// locations to move, it walks to the right instead of the left
		int i,j,walk,value,newValue;
		int count = 0;
		for (i = 2; i >= 0; i--) {
			for (j = 0; j < 4; j++) {
				value = this.getValue(i, j);
				// determine how to handle nonzero tile
				if (value != 0) {
					walk = i+1;
					while (walk < 4) {
						// if tile is not 0...
						if (this.getValue(walk, j) != 0) {
							if (this.getValue(walk, j) == value) {
								newValue = value*2;
								this.setValue(walk-1, j, 0);
								this.setValue(walk, j, newValue);
								count += newValue;
							} 
							else {
								break;
							}
						}
						// move to fill a 0 space
						else {
							this.setValue(walk-1, j, 0);
							this.setValue(walk, j, value);
						}
						walk++;
					}
				}
			}
		}
		return count;
	}

	@Override
	public int down() {
		// TODO Auto-generated method stub
		/**
		 * Moving all tiles as much down as possible
		 * and merging tiles according to the rules of 2048.
		 * The method returns the sum of values of all tiles
		 * that merged.
		 * @return total extra points for the score from this move
		 */
		int i,j,walk,value,newValue;
		int count = 0;
		for (i = 0; i < 4; i++) {
			for (j = 2; j >= 0; j--) {
				value = this.getValue(i, j);
				// if there is actually a value, check it
				if (value != 0) {
					walk = j+1;
					while (walk < 4) {
						if (this.getValue(i, walk) != 0) {
							if (this.getValue(i, walk) == value) {
								newValue = value*2;
								this.setValue(i, walk-1, 0);
								this.setValue(i, walk, newValue);
								count += newValue;
							} 
							else {
								break;
							}
						}
						else {
							this.setValue(i, walk-1, 0);
							this.setValue(i, walk, value);
						}
						walk++;
					}
				}
			}
		}
		return count;
	}
	
	@Override
	public int up() {
		// TODO Auto-generated method stub
		/**
		 * Moving all tiles as much up as possible
		 * and merging tiles according to the rules of 2048.
		 * The method returns the sum of values of all tiles
		 * that merged.
		 * @return total extra points for the score from this move
		 */
		int i,j,walk,value,newValue;
		int count = 0;
		for (i = 0; i < 4; i++) {
			for (j = 1; j < 4; j++) {
				value = this.getValue(i, j);
				// if there is actually a value, check it
				if (value != 0) {
					walk = j-1;
					while (walk >= 0) {
						if (this.getValue(i, walk) != 0) {
							if (this.getValue(i, walk) == value) {
								newValue = value*2;
								this.setValue(i, walk+1, 0);
								this.setValue(i, walk, newValue);
								count += newValue;
							} 
							else {
								break;
							}
						}
						else {
							this.setValue(i, walk+1, 0);
							this.setValue(i, walk, value);
						}
						walk--;
					}
				}
			}
		}
		return count;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
