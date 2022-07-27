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
 * An automated player for a Game2048 graphical user interface.
 * A RandomPlayer is expected to be operated with a Timer object
 * that feeds ActionEvents into its actionPerformed method in a
 * repeated manner with a delay in between.
 * 
 * @author Peter Kemper
 *
 */
public class RandomPlayer implements ActionListener {

	Game2048 game2048;
	int numberOfNewTiles;
	JFrame frame;
	boolean operational;

	/**
	 * The default constructor does not lead to an operational game. 
	 * Use the parameterized instructor instead.
	 */
	public RandomPlayer() {
		// incomplete, can't operate without a GameState
		operational = false;
	}

	/**
	 * The player plays the game.  
	 * @param game is the game to play can not be null
	 * @param numberOfNewTiles gives the number of tiles to add after a move
	 * @param frame is used to trigger a repaint, can be null
	 */
	public RandomPlayer(Game2048 game, int numberOfNewTiles, JFrame frame) {
		game2048 = game;
		this.numberOfNewTiles = numberOfNewTiles;
		this.frame = frame;
		checkOperational();	
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
	public void actionPerformed(ActionEvent evt) {
		if (!operational) {
			System.out.println("RandomPlayer: object not operational, can't perform");
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
			Random random = new Random();
			int choice;
			int count = 1;
			while (tmp.equals(state)) {
				choice = random.nextInt(4);
				switch (choice) {
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
				default:
					//System.out.println("Player: down, attempt: " + count);
					game2048.addToScore(state.down());
					break;
				}
				count++;
			}
			// if arrangement of tiles changed, add new tiles as needed
			if (!tmp.equals(state)) {
				for (int i = 0; i < numberOfNewTiles; i++) {
					state.addTile();
				}
			}
			// the frame may be null in a testing environment
			if (null != frame)
				frame.repaint();
			else 
				System.out.println("RandomPlayer: frame is null, skipping repaint");
		}
	}
}
