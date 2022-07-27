package edu.wm.cs.cs301.game2048;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

/**
 * Tests in this class rely on more methods to be implemented correctly
 * in the State.java class.
 * @author peter kemper
 *
 */
class TestStateAdvanced {
	/**
	 * Private method to encapsulate how to instantiate the System under Test,
	 * here an implementation of GameState.java
	 */
	private GameState createState() {
		//return null;
		//return new State();
		return Game2048.createState(null);
	}
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#setEmptyBoard()}.
	 * SUT sets or resets the state to an empty board, so all values
	 * need to be zero. This test requires a working State.equals() method.
	 */
	@Test
	final void testSetEmptyBoardAndEquals() {
		// check if setBoard and getBoard are consistent here with equals method
		int[] input = {0,0,0,0,0,0,0,0,
				       0,0,0,0,0,0,0,0};
		int[] other = {0,0,0,0,0,0,0,0,
			       0,0,0,0,0,0,0,2};
		// scenario 1: two empty states should be equal
		GameState sut1 = createState();
		sut1.setEmptyBoard();
		GameState sut2 = createState();
		sut2.setEmptyBoard();
		assertTrue(sut1.equals(sut2)); // check equals method
		// scenario 2: set the board of one state, check for equality
		// check set board, get board and equals
		// input array is for empty board
		setBoard(sut1, input);
		assertTrue(sut1.equals(sut2)); 
		int[] tmp = new int[16];
		getBoard(sut1, tmp);
		assertTrue(Arrays.equals(input, tmp));
		assertFalse(Arrays.equals(other, tmp));
		// same for a non zero array
		setBoard(sut1, other);
		assertFalse(sut1.equals(sut2)); 
		tmp = new int[16];
		getBoard(sut1, tmp);
		assertFalse(Arrays.equals(input, tmp));
		assertTrue(Arrays.equals(other, tmp));
		// scenario 3: set the board of a non-zero state to empty
		// sut currently carries other as content
		sut1.setEmptyBoard();
		getBoard(sut1, tmp);
		assertTrue(Arrays.equals(input, tmp));
		assertFalse(Arrays.equals(other, tmp));
	}
	
	/**
	 * Test method for equals method of the State class.
	 * SUT needs to support an equals method that is reflexive,
	 * symmetric and transitive.
	 */
	@Test
	final void testEquals() {
		// check if setBoard and getBoard are consistent here with equals method
		int[] input = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] other = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,2};
		// scenario 1: two empty states should be equal
		GameState sut1 = createState();
		sut1.setEmptyBoard();
		GameState sut2 = createState();
		sut2.setEmptyBoard();
		GameState sut3 = createState();
		sut3.setEmptyBoard();
		// check equals method
		// reflexive
		assertTrue(sut1.equals(sut1));
		assertTrue(sut2.equals(sut2));
		assertTrue(sut3.equals(sut3));
		// symmetric
		assertTrue(sut1.equals(sut2));
		assertTrue(sut2.equals(sut1));
		// transitive
		assertTrue(sut1.equals(sut2));
		assertTrue(sut2.equals(sut3));
		assertTrue(sut3.equals(sut1));
		// scenario 2: set the board of one state, check for equality
		// check set board, get board and equals
		// input array is for empty board
		setBoard(sut1, input);
		assertTrue(sut1.equals(sut2)); 
		int[] tmp = new int[16];
		getBoard(sut1, tmp);
		assertTrue(Arrays.equals(input, tmp));
		assertFalse(Arrays.equals(other, tmp));
		// set sut1 for non-zero board
		setBoard(sut1, other);
		assertFalse(sut1.equals(sut2));
		assertFalse(sut2.equals(sut1));
		// set sut2 to same
		setBoard(sut2, other);
		assertTrue(sut1.equals(sut2));
		assertTrue(sut2.equals(sut1));
		// set sut3 to same but use setValue
		sut3.setValue(3, 3, 2);
		assertTrue(sut3.equals(sut1));
		assertTrue(sut1.equals(sut3));
		assertTrue(sut2.equals(sut3));
		// set values in sut1,2,3 to different values
		// check for confusion of positions, values
		sut1.setValue(1, 2, 4);
		sut2.setValue(2, 1, 4);
		sut3.setValue(1, 2, 2);
		assertTrue(sut1.equals(sut1));
		assertFalse(sut1.equals(sut2));
		assertFalse(sut1.equals(sut3));
		assertTrue(sut2.equals(sut2));
		assertFalse(sut2.equals(sut1));
		assertFalse(sut2.equals(sut3));
		assertTrue(sut3.equals(sut3));
		assertFalse(sut3.equals(sut1));
		assertFalse(sut3.equals(sut1));
		// set values to same
		sut1.setValue(1, 2, 4);
		sut2.setValue(2, 1, 0); // reset
		sut2.setValue(1, 2, 4); // set
		sut3.setValue(1, 2, 4); // reset & set
		sut1.setValue(2, 3, 16);
		sut2.setValue(2, 3, 16);
		sut3.setValue(2, 3, 16);
		assertTrue(sut1.equals(sut1));
		assertTrue(sut1.equals(sut2));
		assertTrue(sut1.equals(sut3));
		assertTrue(sut2.equals(sut2));
		assertTrue(sut2.equals(sut1));
		assertTrue(sut2.equals(sut3));
		assertTrue(sut3.equals(sut3));
		assertTrue(sut3.equals(sut1));
		assertTrue(sut3.equals(sut1));

	}

	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#addTile()}.
	 * We need to test if the method identifies and fills empty tiles with 
	 * a value of 2 or 4 at random positions. 
	 */
	@Test
	final void testAddTileIsRandom() {
		// scenario: if we start from an empty board
		// and add 3 tiles randomly and then do this again
		// the outcomes should be different
		int[] s1 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] s2 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] s3 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] result = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		GameState sut = createState();
		sut.setEmptyBoard();
		// round 1: obtain state vectors
		sut.addTile();
		getBoard(sut, s1);
		sut.addTile();
		getBoard(sut, s2);
		sut.addTile();
		getBoard(sut, s3);
		// sanity check
		// all 3 need to be different to an empty board and
		// differ from each other, 
		// result is empty at this moment
		assertFalse(Arrays.equals(s1, result));
		assertFalse(Arrays.equals(s2, result));
		assertFalse(Arrays.equals(s3, result));
		assertFalse(Arrays.equals(s1, s2));
		assertFalse(Arrays.equals(s2, s3));
		// round 2: repeat procedure
		sut.setEmptyBoard();
		sut.addTile();
		getBoard(sut, result);
		// result after adding a random tile is expected to be different
		// chances to randomly pick same position & value: 1/16 * 1/2
		boolean b1 = Arrays.equals(s1, result);
		sut.addTile();
		getBoard(sut, result);
		// chances to randomly pick same position & value: 1/15 * 1/2
		boolean b2 = Arrays.equals(s2, result);
		sut.addTile();
		getBoard(sut, result);
		// chances to randomly pick same position & value: 1/14 * 1/2
		boolean b3 = Arrays.equals(s3, result);
		// Just print warnings because conditions are occasionally satisfied
		// even by a correct implementation
		if (b1)
			System.out.println("TestStateAdvanced.testAddTileIsRandom:" 
					+ "Warning, addTile picks same position and value for first tile");
		if (b2)
			System.out.println("TestStateAdvanced.testAddTileIsRandom:" 
					+ "Warning, addTile picks same position and value for 2nd tile");
		if (b3)
			System.out.println("TestStateAdvanced.testAddTileIsRandom:" 
					+ "Warning, addTile picks same position and value for 3rd tile");
		// checked condition, the sequence of the first 3 states is identical
		// this is sufficiently unlikely: 1/16 * 1/2 * 1/15 * 1/2 * 1/14 * 1/2 
		assertFalse(b1 && b2 && b3); 
	}
	
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#addTile()}.
	 * We need to test if the method identifies and fills empty tiles with 
	 * a value of 2 or 4 at random positions. 
	 */
	@Test
	final void testAddTileIsRandom2() {
		// scenario 1: if we start from an empty board
		// and add one tile randomly
		// we repeat this 4 times and expect that the 
		// 4th outcome is not identical to all 3 predecessors
		int[] s1 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] s2 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] s3 = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		int[] result = {0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0};
		GameState sut = createState();
		// obtain state vectors
		sut.setEmptyBoard();
		sut.addTile();
		getBoard(sut, s1);
		// reset and repeat
		sut.setEmptyBoard();
		sut.addTile();
		getBoard(sut, s2);
		// reset and repeat
		sut.setEmptyBoard();
		sut.addTile();
		getBoard(sut, s3);
		// sanity check
		// all 3 need to be different to an empty board and
		// result is empty at this moment
		assertFalse(Arrays.equals(s1, result));
		assertFalse(Arrays.equals(s2, result));
		assertFalse(Arrays.equals(s3, result));
		// result after adding a random tile is expected to be different
		// chances to randomly pick same position & value: 1/16 * 1/2
		sut.setEmptyBoard();
		sut.addTile();
		getBoard(sut, result);
		boolean b1 = Arrays.equals(s1, result);
		boolean b2 = Arrays.equals(s2, result);
		boolean b3 = Arrays.equals(s3, result);
		// Just print warnings because conditions are occasionally satisfied
		// even by a correct implementation
		if (b1)
			System.out.println("TestStateAdvanced.testAddTileIsRandom2:" 
					+ "Warning, addTile picks same position and value for first tile");
		if (b2)
			System.out.println("TestStateAdvanced.testAddTileIsRandom2:" 
					+ "Warning, addTile picks same position and value for 2nd tile");
		if (b3)
			System.out.println("TestStateAdvanced.testAddTileIsRandom2:" 
					+ "Warning, addTile picks same position and value for 3rd tile");
		// checked condition: at least 2 out of the first 3 states are identical to the 4th
		// this is sufficiently unlikely 
		assertFalse((b1 && b2) || (b1 && b3) || (b2 && b3)); 
	}
	/**
	 * Test method for {@link edu.wm.cs.cs301.game2048.State#addTile()}.
	 * We need to test if the method identifies and fills empty tiles with 
	 * a value of 2 or 4 and picks the location in a randomized manner.
	 * The test requires several other methods to work correctly, 
	 * i.e., setEmptyBoard, setValue, getValue, isFull.
	 */
	@Test
	final void testAddTileAtRandomPosition() {
		long first = observeOrderOfTileSelections(); 
		long second = observeOrderOfTileSelections();
		// number of permutations: 6! = 720, that is on average 1/720 will be a false positive
		// for an error here, so if test fails, rerun to see if error is stable
		System.out.println("First sequence " + first + ", second sequence " + second);
		assertTrue(first != second, "First sequence " + first + ", second sequence " + second);
	}
	/**
	 * Observe in which order tiles are placed on 6 empty slots.
	 * @return an integer encoding for the observed order
	 */
	private long observeOrderOfTileSelections() {
		// setup state with a full board of 8s
		GameState sut = createFullBoard(8);
		// set the 4 corners to 0
		sut.setValue(0, 0, 0); // findPosition: 1
		sut.setValue(0, 3, 0); // findPosition: 13
		sut.setValue(3, 0, 0); // findPosition: 4
		sut.setValue(3, 3, 0); // findPosition: 16
		// plus 2 more positions for good measure
		sut.setValue(1, 2, 0); // findPosition: 11 
		sut.setValue(2, 3, 0); // findPosition: 15
		// let the method pick an order
		long order = 0;
		for (int i = 0; i < 6; i++) {
			assertFalse(sut.isFull());
			assertTrue(sut.addTile());
			order = order * 100 + findPositionAndFlip(sut);
		}
		return order;
	}
	/**
	 * Finds the first position of 2 or 4 and flips it into an 8
	 * @param sut the state under test
	 * @return position of flipped tile, range 1,2,...,16
	 */
	private int findPositionAndFlip(GameState sut) {
		// go through board, find a 2 or 4
		// flip it to 8, report position
		// note: values on board are 0, 2, 4, 8
		int v;
		for (int i = 0; i < 16; i++) {
			v = sut.getValue(i/4,i%4);
			if (v == 2 || v == 4) {
				sut.setValue(i/4, i%4, 8);
				return i+1; // range 1,2,..,16
			}
		}
		fail("Should have found a position");
		return Integer.MIN_VALUE;
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
	
	private void setBoard(GameState sut, int[] input) {
		// WARNING: setValue permutes rows and columns,
		// x is for columns, y is for rows
		// to create the given number pattern on the board
		// as given in input, we need to permute this
		for (int i = 0; i < input.length; i++) {
			sut.setValue(i%4, i/4, input[i]);
		}
	}

	private void getBoard(GameState sut, int[] output) {
		for (int i = 0; i < output.length; i++) {
			output[i] = sut.getValue(i%4, i/4);
		}
	}
}
