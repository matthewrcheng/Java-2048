/**
 * 
 */
package edu.wm.cs.cs301.game2048;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * All of the contents of this file are the same as RandomPlayer.java
 * except for replacing Random with Smart, adding "int moves;", and changing actionPerformed.
 * 
 *
 */
public class SmartPlayer implements ActionListener {

	Game2048 game2048;
	int numberOfNewTiles;
	JFrame frame;
	boolean operational;
	
	int moves;

	/**
	 * The default constructor does not lead to an operational game. 
	 * Use the parameterized instructor instead.
	 */
	public SmartPlayer() {
		// incomplete, can't operate without a GameState
		operational = false;
	}

	/**
	 * The player plays the game.  
	 * @param game is the game to play can not be null
	 * @param numberOfNewTiles gives the number of tiles to add after a move
	 * @param frame is used to trigger a repaint, can be null
	 */
	public SmartPlayer(Game2048 game, int numberOfNewTiles, JFrame frame) {
		game2048 = game;
		this.numberOfNewTiles = numberOfNewTiles;
		this.frame = frame;
		checkOperational();
		this.moves = 0;
	}

	private void checkOperational() {
		if (game2048 != null && numberOfNewTiles > 0)
			operational = true;
	}
	public void setGame(Game2048 game) {
		game2048 = game;
		checkOperational();	
	}
	public void setNumberOfNewTiles(int numberOfNewTiles) {
		this.numberOfNewTiles = numberOfNewTiles;
		checkOperational();	
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
		checkOperational();	
	}
	
	@Override
	// This strategy involves keeping the highest value tiles to the top right
	public void actionPerformed(ActionEvent evt) {
		if (!operational) {
			System.out.println("SmartPlayer: object not operational, can't perform");
			return;
		}
		if (game2048.gameOver()) {
			System.out.println("Actionlistener recognizes game is over");
			((Timer)evt.getSource()).stop();
		}
		else {
			// create two GameStates, just like with random player
			GameState state = game2048.getState();
			GameState tmp = game2048.createState(state);
			
			// still uses random in case the default move is not possible
			Random random = new Random();
			
			// this algorithm will involve up and right moves whenever possible
			// so left and down will keep track of any left or down moves
			boolean left = false;
			boolean down = false;
			
			// count keeps track of attempts in a single turn to make a move
			int count = 0;
			
			// choice is used to decide between left and down if neither right nor
			// up are possible
			int choice;
			
			// this will run as long as a move has not been made, this will ensure that
			// nothing happens if the move chosen is not possible and will force the 
			// algorithm to select a random move
			while (tmp.equals(state)) {
				// if the previous move was left, then we move all the tiles right again
				// in order to keep high value tiles in the top right
				if (left == true) {
					game2048.addToScore(state.right());
					left = false;
				}
				// same as above, we must return high value tiles to the top right
				if (down == true) {
					game2048.addToScore(state.up());
					down = false;
				}
				// as long as have not tried both up and right...
				if (count < 2) {
					// if we are currently on an odd move or up was already attempted, try right
					if (moves % 2 == 1 || count == 1) {
						game2048.addToScore(state.right());
					}
					// if we are on an even move or right was already attempted, try up
					else {
						game2048.addToScore(state.up());
					}
				}
				// if we have tried both right and up and neither is possible, then we
				// ensure the game keeps going by randomly selecting down or left
				else {
					choice = random.nextInt(2);
					if (choice == 0) {
						game2048.addToScore(state.left());
						left = true;
					}
					else {
						game2048.addToScore(state.down());
						down = true;
					}
				}
				count++;
			}
			// if arrangement of tiles changed, add new tiles as needed
			if (!tmp.equals(state)) {
				for (int i = 0; i < numberOfNewTiles; i++) {
					state.addTile();
					// a move has been made so increment moves
					moves++;
				}
			}
			// the frame may be null in a testing environment
			if (null != frame)
				frame.repaint();
			else 
				System.out.println("SmartPlayer: frame is null, skipping repaint");
		}
	}
}
	
	/* 
	 * I had two different attempts at creating a smart player. This second one looks
	 * ahead at each possible move and determines which one would add the most points to
	 * the score. Although the random tile would be different, I was hoping that this would
	 * end up performing well. However, it ended up overcrowding the board in many occasions
	 * so the one I created above ended up performing better in the end. I decided to leave
	 * this one in to just in case there are any smaller changes I could make that would
	 * cause it to perform better. It is also worth noting that it uses the algorithm from
	 * above in case the best move ends up not being possible.
	*/
	
	/*
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (!operational) {
			System.out.println("SmartPlayer: object not operational, can't perform");
			return;
		}
		if (game2048.gameOver()) {
			System.out.println("Actionlistener recognizes game is over");
			((Timer)evt.getSource()).stop();
		}
		else {
			GameState state = game2048.getState();
			GameState tmp = game2048.createState(state);
			// pick a move
			int[] scores = new int[4];
			
			GameState leftState = game2048.createState(state);
			scores[0] = leftState.left();
			GameState rightState = game2048.createState(state);
			scores[1] = rightState.right();
			GameState upState = game2048.createState(state);
			scores[2] = upState.up();
			GameState downState = game2048.createState(state);
			scores[3] = downState.down();
			
			int choice = 0;
			for (int i = 1; i < 4; i++) {
				if (scores[i] > scores[choice]) {
					choice = i;
				}
			}
			
			switch (choice) {
			default:
			case 0:
				//System.out.println("Player: left, attempt: " + count);
				game2048.addToScore(state.left());
				break;
			case 1:
				//System.out.println("Player: right, attempt: " + count);
				game2048.addToScore(state.right());
				break;
			case 2: 
				//System.out.println("Player: up, attempt: " + count);
				game2048.addToScore(state.up());
				break;
			case 3:
				//System.out.println("Player: down, attempt: " + count);
				game2048.addToScore(state.down());
				break;
			}
			
			Random random = new Random();
			boolean left = false;
			boolean down = false;
			int count = 0;
			while (tmp.equals(state)) {
				if (left == true) {
					game2048.addToScore(state.right());
					left = false;
				}
				if (down == true) {
					game2048.addToScore(state.up());
					down = false;
				}
				if (count < 2) {
					if (moves % 2 == 1) {
						game2048.addToScore(state.right());
					}
					else {
						game2048.addToScore(state.up());
					}
				}
				else {
					choice = random.nextInt(2);
					if (choice == 0) {
						game2048.addToScore(state.left());
						left = true;
					}
					else {
						game2048.addToScore(state.down());
						down = true;
					}
				}
				count++;
			}
			// if arrangement of tiles changed, add new tiles as needed
			if (!tmp.equals(state)) {
				for (int i = 0; i < numberOfNewTiles; i++) {
					state.addTile();
					moves++;
				}
			}
			// the frame may be null in a testing environment
			if (null != frame)
				frame.repaint();
			else 
				System.out.println("SmartPlayer: frame is null, skipping repaint");
		}
	}
	*/