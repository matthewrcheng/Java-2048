package edu.wm.cs.cs301.game2048;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.wm.cs.cs301.game2048.GameState;

/**
 * 
 */

/**
 * A set of basic Junit 5 tests for the State class.
 * 
 * @author Peter Kemper
 *
 */
class TestState {
	/**
	 * Private method to encapsulate how to instantiate the System under Test,
	 * here an implementation of GameState.java
	 */
	private GameState createState() {
		//return new State();
		return Game2048.createState(null);
	}

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#setEmptyBoard()}.
	 * SUT sets or resets the state to an empty board, so all values
	 * need to be zero.
	 */
	@Test
	final void testSetEmptyBoard() {
		// setup state, check if all values are zero
		GameState sut = createState();
		// call method, note: constructor not guaranteed to init board
		sut.setEmptyBoard();
		// check result
		assertEquals(0, sut.getValue(0, 0));
		assertEquals(0, sut.getValue(1, 1));
		assertEquals(0, sut.getValue(1, 2));
		assertEquals(0, sut.getValue(2, 1));
		assertEquals(0, sumOfValues(sut));
		// set some values and check if values stick
		sut.setValue(0, 0, 2);
		assertEquals(2, sumOfValues(sut));
		assertEquals(2, sut.getValue(0, 0));
		sut.setValue(1, 1, 2);
		assertEquals(4, sumOfValues(sut));
		assertEquals(2, sut.getValue(1, 1));
		sut.setValue(1, 2, 2);
		assertEquals(6, sumOfValues(sut));
		assertEquals(2, sut.getValue(1, 2));
		sut.setValue(2, 1, 2);
		assertEquals(8, sumOfValues(sut));
		assertEquals(2, sut.getValue(2, 1));
		// call method and check if all values are zero
		sut.setEmptyBoard();
		assertEquals(0, sut.getValue(0, 0));
		assertEquals(0, sut.getValue(1, 1));
		assertEquals(0, sut.getValue(1, 2));
		assertEquals(0, sut.getValue(2, 1));
		assertEquals(0, sumOfValues(sut));
	}
	/**
	 * Sum up all values on the board
	 * @param s the state to consider
	 * @return sum of values
	 */
	private int sumOfValues(GameState s) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				result += s.getValue(i, j);
			}
		}
		return result;
	}
	

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#getValue(int, int)}.
	 * We need to test if we can set values and if values are reported consistently.
	 */
	@Test
	final void testGetValue() {
		// setup state with empty board
		GameState sut = createState();
		sut.setEmptyBoard();
		// smoke test: 
		// check if this works for position (0,0) with a value of 2
		checkGivenPosition(sut,0,0,2);
		// smoke test:
		// check if this works for corner case (3,3) with a value of 4
		checkGivenPosition(sut,3,3,4);
		// range test: check all positions with a value of 8
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				checkGivenPosition(sut, i, j, 8);
			}
		}
	}
	/**
	 * Check if we can set/get the value at the given position.
	 * Initially board must be empty.
	 * @param sut the state under test
	 * @param x index for position
	 * @param y index for position
	 * @param value the value to put in place
	 */
	private void checkGivenPosition(GameState sut, int x, int y, int value) {
		// check if value is zero
		assertEquals(0, sut.getValue(x, y));
		// set value
		sut.setValue(x, y, value);
		// check if value is present
		assertEquals(value, sut.getValue(x, y));
		// set it to something else
		sut.setValue(x, y, value*2);
		// check if value is present
		assertEquals(value*2, sut.getValue(x, y));
		// set value again
		sut.setValue(x, y, value);
		// check if value is present
		assertEquals(value, sut.getValue(x, y));
		// reset to zero
		sut.setValue(x, y, 0);
		assertEquals(0, sut.getValue(x, y));
	}

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#reachedThreshold()}.
	 * We need to test if the state recognizes when the 2048 threshold for 
	 * winning the game is reached. The test requires several other 
	 * methods to work correctly, i.e., setEmptyBoard, addTile, left,
	 * right, up, down, setValue, getValue.
	 */
	@Test
	final void testReachedThreshold() {
		// setup state
		GameState sut = createState();
		// scenario 1: 
		// check that empty board is below threshold
		sut.setEmptyBoard();
		assertFalse(sut.reachedThreshold());
		// scenario 2:
		// put some tiles on it, perform each operation once
		// 4 operations not enough to reach 2048
		sut.addTile();
		sut.addTile();
		sut.addTile();
		sut.addTile();
		sut.left();
		sut.up();
		sut.right();
		sut.down();
		assertFalse(sut.reachedThreshold());
		// scenario 3: 
		// board is partially filled
		// add 2 tiles with 1024 in the top left corner, top row
		// move left to merge the 1024 tiles into a 2048 tile
		// check that value is 2048
		// confirm that threshold is reached
		sut.setValue(0, 0, 1024);
		sut.setValue(1, 0, 1024);
		sut.left();
		assertEquals(2048, sut.getValue(0, 0));
		assertTrue(sut.reachedThreshold());
		// scenario 4:
		// reset board after threshold is reached, 
		// check that threshold is not reached anymore
		sut.setEmptyBoard();
		assertFalse(sut.reachedThreshold());
	}

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#addTile()}.
	 * We need to test if the method identifies and fills empty tiles with 
	 * a value of 2 or 4. 
	 * The test requires several other methods to work correctly, 
	 * i.e., setEmptyBoard, setValue, getValue, isFull.
	 */
	@Test
	final void testAddTile() {
		// setup state
		GameState sut = createState();
		// scenario 1: just 1 tile added to empty board
		sut.setEmptyBoard();
		int sum = sumOfValues(sut);
		assertEquals(0, sum);
		int[] input = new int[16];
		getBoard(sut, input);
		assertTrue(sut.addTile()); // must be successful
		sum = sumOfValues(sut);
		assertTrue(2 == sum || 4 == sum);
		int[] output = new int[16];
		getBoard(sut, input);
		assertEquals(1, countDifferences(input, output));
		
		// scenario 2: iteratively add tiles to the board till full
		sut.setEmptyBoard();
		sum = 0;
		int v;
		for (int i = 0; i < 16; i++) {
			assertTrue(sut.addTile());  // must be successful
			v = sumOfValues(sut);
			// either a 2 or a 4 is added on an empty slot
			assertTrue(sum + 2 == v || sum + 4 == v);
			sum = v;
		}
		// board is full now
		assertFalse(sut.addTile()); // must not be successful
		assertTrue(sut.isFull());
		sut.setValue(1, 2, 0);
		assertTrue(sut.addTile());  // must be successful
		v = sut.getValue(1, 2); // either a 2 or a 4 is added on the empty slot
		assertTrue(2 == v || 4 == v);
	}
	

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#isFull()}.
	 * We need to test if the method recognizes when all tiles are nonzero
	 * or not. 
	 * The test requires several other methods to work correctly, 
	 * i.e., setEmptyBoard, addTile, setValue.
	 */
	@Test
	final void testIsFull() {
		// setup state
		GameState sut = createState();
		// scenario 1: empty board 
		sut.setEmptyBoard();
		assertFalse(sut.isFull());
		// scenario 2: one tile on the board
		sut.setValue(0, 0, 2);
		assertFalse(sut.isFull());
		// scenario 3: a few randomly placed tiles on the board
		sut.setEmptyBoard();
		for (int i = 0; i < 6; i++) {
			sut.addTile();
		}
		assertFalse(sut.isFull());
		// scenario 4: a full board, fill it, reset 1 position, set again
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				sut.setValue(i, j, 2);
			}
		}
		assertTrue(sut.isFull());
		sut.setValue(0, 0, 0);
		assertFalse(sut.isFull());
		sut.setValue(0, 0, 4);
		assertTrue(sut.isFull());
		sut.setValue(1, 2, 0);
		assertFalse(sut.isFull());
		sut.setValue(1, 2, 4);
		assertTrue(sut.isFull());
	}

	////////////////////////////////////////////////////////////////
	/////////////// tests for move methods /////////////////////////
	////////////////////////////////////////////////////////////////
	/* General considerations
	 * A move method pushes tiles to one side, e.g., left to the left
	 * side. Adjacent tiles of same value need to be merged.
	 * A merge adds on to the return score. 
	 * If the board has an empty slot, a new randomly select tile will
	 * receive either a value of 2 or 4.
	 * This makes the outcome a bit hard to compare with an expected
	 * result as we need to tolerate a difference by 1 tile.
	 * 
	 * What is the difference between the 
	 * sum of tiles before and after a move?
	 * SumBefore == SumAfter + Move_returnvalue - ValueAddedTile
	 * If the tiles don't move no tile will be added, so
	 * the sums are the same, return value is 0, value added is zero
	 * If the tiles do move but nothing is merged, the return
	 * value is zero, but there is a tile added if the board
	 * is not completely filled
	 * 
	 */
	/**
	 * Test method for all move methods on an empty board.
	 * Regardless of direction, the board needs to stay the same.
	 */
	@Test
	final void testAllMovesOnEmptyBoard() {
		// setup state with empty board
		GameState sut = createState();
		sut.setEmptyBoard();
		int score;
		assertEquals(0, sumOfValues(sut));
		// left move
		score = sut.left();
		assertEquals(0, score);
		assertEquals(0, sumOfValues(sut));
		// right move
		score = sut.right();
		assertEquals(0, score);
		assertEquals(0, sumOfValues(sut));
		// up move
		score = sut.up();
		assertEquals(0, score);
		assertEquals(0, sumOfValues(sut));
		// right move
		score = sut.down();
		assertEquals(0, score);
		assertEquals(0, sumOfValues(sut));
	}
	/**
	 * Test method for all move methods on a board with 1 tile
	 * in a corner. The tile should end up in the opposite
	 * corner, no merging can take place and a new tile 
	 * should be added at a random position.
	 * Try this for all four moves.
	 */
	@Test
	final void testAllMovesOnBoardWithOneTileCornerCases() {
		// setup state with empty board
		GameState sut = createState();
		sut.setEmptyBoard();
		int score;
		assertEquals(0, sumOfValues(sut));
		// place tile of value 2, left move
		// top right corner (0,3) -> (0,0)
		sut.setValue(3, 0, 2);
		score = sut.left();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(0, 0));
		assertTrue(2 == sumOfValues(sut));
		// place tile of value 2, right move
		// top left corner (0,0) -> (0,3)
		sut.setEmptyBoard();
		sut.setValue(0, 0, 2);
		score = sut.right();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(3, 0));
		assertTrue(2 == sumOfValues(sut));
		// place tile of value 2, up move
		// bottom left corner (3,0) -> (0,0)
		sut.setEmptyBoard();
		sut.setValue(0, 3, 2);
		score = sut.up();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(0, 0));
		assertTrue(2 == sumOfValues(sut));
		// place tile of value 2, down move
		// top left corner (0,0) -> (3,0)
		sut.setEmptyBoard();
		sut.setValue(0, 0, 2);
		score = sut.down();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(0, 3));
		assertTrue(2 == sumOfValues(sut));
	}
	/**
	 * Test method for all move methods on a board with 1 tile
	 * in a middle position. The tile should end up on 1 side,
	 * no merging can take place and a new tile 
	 * should be added at a random position.
	 * Try this for all four moves.
	 */
	@Test
	final void testAllMovesOnBoardWithOneTileMiddlePosition() {
		// setup state with empty board
		GameState sut = createState();
		sut.setEmptyBoard();
		int score;
		assertEquals(0, sumOfValues(sut));
		// place tile of value 2, left move
		// (1,2) -> (1,0)
		sut.setValue(2, 1, 2);
		score = sut.left();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(0, 1));
		assertEquals(2, sumOfValues(sut));
		// place tile of value 2, right move
		// (1,2) -> (1,3)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		score = sut.right();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(3, 1));
		assertEquals(2, sumOfValues(sut));
		// place tile of value 2, up move
		// (1,2) -> (0,2)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		score = sut.up();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(2, 0));
		assertEquals(2, sumOfValues(sut));
		// place tile of value 2, down move
		// (1,2) -> (3,2)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		score = sut.down();
		assertEquals(0, score);
		assertTrue(2 == sut.getValue(2, 3));
		assertEquals(2, sumOfValues(sut));
	}
	/**
	 * Test method for all move methods on a board with 2 tiles
	 * that do merge. The merged tile should end up on 1 side.
	 * Try this for all four moves.
	 */
	@Test
	final void testAllMovesOnBoardWithTwoTilesMerging() {
		// setup state with empty board
		GameState sut = createState();
		sut.setEmptyBoard();
		int score;
		assertEquals(0, sumOfValues(sut));
		// place 2 tiles of value 2, left move
		// (1,2),(1,3) -> (1,0)
		// 2 tiles of value 2 are merged, score is 4
		sut.setValue(2, 1, 2);
		sut.setValue(3, 1, 2);
		score = sut.left();
		assertEquals(4, score);
		assertTrue(4 == sut.getValue(0, 1));
		assertEquals(4, sumOfValues(sut));
		// place 2 tiles of value 2, right move
		// (1,2),(1,3)  -> (1,3)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		sut.setValue(3, 1, 2);
		score = sut.right();
		assertEquals(4, score);
		assertTrue(4 == sut.getValue(3, 1));
		assertEquals(4, sumOfValues(sut));
		// place 2 tiles of value 2, up move
		// (1,2),(2,2) -> (0,2)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		sut.setValue(2, 2, 2);
		score = sut.up();
		assertEquals(4, score);
		assertTrue(4 == sut.getValue(2, 0));
		assertEquals(4, sumOfValues(sut));
		// place 2 tiles of value 2, down move
		// (1,2),(2,2) -> (3,2)
		sut.setEmptyBoard();
		sut.setValue(2, 1, 2);
		sut.setValue(2, 2, 2);
		score = sut.down();
		assertEquals(4, score); 
		assertTrue(4 == sut.getValue(2, 3));
		assertEquals(4, sumOfValues(sut));
	}
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#left()}.
	 * The left operation needs to move tiles to the left.
	 * It needs to return the sum of merged values, i.e. the increment
	 * to the score the user gets for this move.
	 */
	@Test
	final void testLeft() {
		// test case: full board with a value of 2
		testLeft(2); 
		// similar for values of 4, 8, 16, 32
		testLeft(4); 
		testLeft(8); 
		testLeft(16); 
		testLeft(32); 
	}
	/**
	 * Evaluates left, left, up, up on a full board
	 * for the given value
	 * @param value to be placed all over board
	 */
	private void testLeft(int value) {
		GameState sut = createFullBoard(value);
		// resulting state is 2 columns with 4s plus a random tile of 2 or 4
		final int result = sut.left();
		assertEquals(value*16, result); // 2 columns of 4 tiles with value 4
		final int mergedResult = value * 2;
		assertEquals(mergedResult, sut.getValue(0, 0));
		assertEquals(mergedResult, sut.getValue(0, 1));
		assertEquals(mergedResult, sut.getValue(0, 2));
		assertEquals(mergedResult, sut.getValue(0, 3));
		assertEquals(mergedResult, sut.getValue(1, 0));
		assertEquals(mergedResult, sut.getValue(1, 1));
		assertEquals(mergedResult, sut.getValue(1, 2));
		assertEquals(mergedResult, sut.getValue(1, 3));
		assertEquals(result, sumOfValues(sut));
		sut.left();
		assertEquals(mergedResult*2, sut.getValue(0, 0));
		assertEquals(mergedResult*2, sut.getValue(0, 1));
		assertEquals(mergedResult*2, sut.getValue(0, 2));
		assertEquals(mergedResult*2, sut.getValue(0, 3));
		sut.up();
		assertEquals(mergedResult*4, sut.getValue(0, 0));
		assertEquals(mergedResult*4, sut.getValue(0, 1));
		sut.up();
		assertEquals(mergedResult*8, sut.getValue(0, 0));
	}
	
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#down()}.
	 * The down operation needs to move tiles down.
	 * It needs to return the sum of merged values, i.e. the increment
	 * to the score the user gets for this move.
	 */
	@Test
	final void testDown() {
		// test case: full board with a value of 2
		testDown(2); 
		// similar for values of 4, 8, 16, 32
		testDown(4); 
		testDown(8); 
		testDown(16); 
		testDown(32); 
	}
	/**
	 * Evaluates down, down, left, left on a full board
	 * for the given value
	 * @param value to be placed all over board
	 */
	private void testDown(int value) {
		GameState sut = createFullBoard(value);
		// resulting state is 2 rows with 4s plus a random tile of 2 or 4
		final int result = sut.down();
		assertEquals(value*16, result); // 2 rows of 4 tiles with value 4
		final int mergedResult = value * 2;
		assertEquals(mergedResult, sut.getValue(0, 2));
		assertEquals(mergedResult, sut.getValue(1, 2));
		assertEquals(mergedResult, sut.getValue(2, 2));
		assertEquals(mergedResult, sut.getValue(3, 2));
		assertEquals(mergedResult, sut.getValue(0, 3));
		assertEquals(mergedResult, sut.getValue(1, 3));
		assertEquals(mergedResult, sut.getValue(2, 3));
		assertEquals(mergedResult, sut.getValue(3, 3));
		assertEquals(result, sumOfValues(sut));
		sut.down();
		assertEquals(mergedResult*2, sut.getValue(0, 3));
		assertEquals(mergedResult*2, sut.getValue(1, 3));
		assertEquals(mergedResult*2, sut.getValue(2, 3));
		assertEquals(mergedResult*2, sut.getValue(3, 3));
		sut.left();
		assertEquals(mergedResult*4, sut.getValue(0, 3));
		assertEquals(mergedResult*4, sut.getValue(1, 3));
		sut.left();
		assertEquals(mergedResult*8, sut.getValue(0, 3));
	}

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#right()}.
	 * The right operation needs to move tiles to the right.
	 * It needs to return the sum of merged values, i.e. the increment
	 * to the score the user gets for this move.
	 */
	@Test
	final void testRight() {
		// test case: full board with a value of 2
		testRight(2);
		// similar for values of 4, 8, 16, 32
		testRight(4);
		testRight(8);
		testRight(16);
		testRight(32);
	}

	/**
	 * Evaluates right, right, up, up on a full board
	 * for the given value
	 * @param value to be placed all over board
	 */
	private void testRight(int value) {
		GameState sut = createFullBoard(value);
		// resulting state is 2 columns with 4s plus a random tile of 2 or 4
		final int result = sut.right();
		assertEquals(value*16, result); // 2 columns of 4 tiles with value 4
		final int mergedResult = value * 2;
		assertEquals(mergedResult, sut.getValue(2, 0));
		assertEquals(mergedResult, sut.getValue(2, 1));
		assertEquals(mergedResult, sut.getValue(2, 2));
		assertEquals(mergedResult, sut.getValue(2, 3));
		assertEquals(mergedResult, sut.getValue(3, 0));
		assertEquals(mergedResult, sut.getValue(3, 1));
		assertEquals(mergedResult, sut.getValue(3, 2));
		assertEquals(mergedResult, sut.getValue(3, 3));
		assertEquals(result, sumOfValues(sut)); 
		sut.right();
		assertEquals(mergedResult*2, sut.getValue(3, 0));
		assertEquals(mergedResult*2, sut.getValue(3, 1));
		assertEquals(mergedResult*2, sut.getValue(3, 2));
		assertEquals(mergedResult*2, sut.getValue(3, 3));
		sut.up();
		assertEquals(mergedResult*4, sut.getValue(3, 0));
		assertEquals(mergedResult*4, sut.getValue(3, 1));
		sut.up();
		assertEquals(mergedResult*8, sut.getValue(3, 0));
	}
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#up()}.
	 * The up operation needs to move tiles up.
	 * It needs to return the sum of merged values, i.e. the increment
	 * to the score the user gets for this move.
	 */
	@Test
	final void testUp() {
		// test case: full board with a value of 2
		testUp(2);
		// similar for values of 4, 8, 16, 32
		testUp(4);
		testUp(8);
		testUp(16);
		testUp(32);
	}
	/**
	 * Evaluates up, up, left, left on a full board
	 * for the given value
	 * @param value to be placed all over board
	 */
	private void testUp(int value) {
		// setup state
		GameState sut = createFullBoard(value);
		// resulting state is 2 rows with 4s plus a random tile of 2 or 4
		final int result = sut.up();
		assertEquals(value*16, result); // 2 rows of 4 tiles with value 4
		final int mergedResult = value * 2;
		assertEquals(mergedResult, sut.getValue(0, 0));
		assertEquals(mergedResult, sut.getValue(1, 0));
		assertEquals(mergedResult, sut.getValue(2, 0));
		assertEquals(mergedResult, sut.getValue(3, 0));
		assertEquals(mergedResult, sut.getValue(0, 1));
		assertEquals(mergedResult, sut.getValue(1, 1));
		assertEquals(mergedResult, sut.getValue(2, 1));
		assertEquals(mergedResult, sut.getValue(3, 1));
		assertEquals(result, sumOfValues(sut));
		sut.up();
		assertEquals(mergedResult*2, sut.getValue(0, 0));
		assertEquals(mergedResult*2, sut.getValue(1, 0));
		assertEquals(mergedResult*2, sut.getValue(2, 0));
		assertEquals(mergedResult*2, sut.getValue(3, 0));
		sut.left();
		assertEquals(mergedResult*4, sut.getValue(0, 0));
		assertEquals(mergedResult*4, sut.getValue(1, 0));
		sut.left();
		assertEquals(mergedResult*8, sut.getValue(0, 0));
	}
	/**
	 * Creates a state with all tiles set to the given value
	 * @param value for all tiles
	 * @return newly allocated state with all tiles set to given value
	 */
	private GameState createFullBoard(int value) {
		GameState sut = createState();
		sut.setEmptyBoard();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				sut.setValue(i, j, value);
			}
		}
		return sut;
	}
	
	
	private int countDifferences(int[] output, int[] result) {
		int c = 0;
		for (int i = 0; i < result.length; i++) {
			if (output[i] != result[i])
				c++;
		}
		return c;
	}


	private void getBoard(GameState sut, int[] output) {
		for (int i = 0; i < output.length; i++) {
			output[i] = sut.getValue(i%4, i/4);
		}
	}

}
