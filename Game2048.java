/*
 * Copyright 1998-2014 Konstantin Bulenkov http://bulenkov.com/about
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author of original work:
 * @author Konstantin Bulenkov
 * Derivative work: 
 * @author Peter Kemper
 * The code has been refactored and reorganized to separate the visualization
 * of the game, the GUI part from the internal game representation.
 */

package edu.wm.cs.cs301.game2048;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * @author Konstantin Bulenkov
 * Derived work
 * @author Peter Kemper
 */
public class Game2048 extends JPanel {
	private static final long serialVersionUID = 1L;
	// constants for game configuration
	// adjust as necessary for testing purposes.
	public static final int WINNING_THRESHOLD = 2048; 	 //game: 2048 
	public static final int NUMBER_OF_INITIAL_TILES = 2; //game: 2 
	public static final int NUMBER_OF_NEW_TILES = 1;     //game: 1 

	private static final Color BG_COLOR = new Color(0xbbada0);
	private static final String FONT_NAME = "Arial";
	private static final int TILE_SIZE = 64;
	private static final int TILES_MARGIN = 16;

	private GameState currentState;
	private int score;
	private boolean manual;
	
	/*
	 * Constructor
	 */
	public Game2048() {
		score = 0;
	}
	public Game2048(boolean manualMode) {
		// set up internal representation of game state
		score = 0;
		currentState = createState(null);
		// set up graphics
		setPreferredSize(new Dimension(340, 400));
		setFocusable(true);
		// operation mode is manual by default, i.e., the user plays the game
		manual = manualMode;

		// set up response to keyboard input
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// possible inputs: 
				// esc: reset game
				// left, right, up, down keys for direction to move tiles
				// need to check if movement resulted into a change of tiles
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					resetGame(NUMBER_OF_INITIAL_TILES);
					// continue in manual mode
					manual = true;
				}
				if (manual && !gameOver()) {
					GameState tmp = Game2048.createState(currentState); 
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						score += currentState.left();
						break;
					case KeyEvent.VK_RIGHT:
						score += currentState.right();
						break;
					case KeyEvent.VK_DOWN:
						score += currentState.down();
						break;
					case KeyEvent.VK_UP:
						score += currentState.up();
						break;
					}
					// if arrangement of tiles changed, add new tiles as needed
					if (!tmp.equals(currentState)) {
						for (int i = 0; i < NUMBER_OF_NEW_TILES; i++) {
							currentState.addTile();
						}
					}
					else {
						System.out.println("No change, no new tile");
					}
				}
				else {
					System.out.println("Not in manual mode");
				}
				repaint();
			}
		});
		// set up the game for the starting situation
		resetGame(NUMBER_OF_INITIAL_TILES);
	}
	/**
	 * Encapsulates which class is used to instantiate
	 * an implementation of the GameState interface.
	 * @param original if not null, the returned object is a copy of this one
	 * @return a newly instantiated object that implements GameState
	 */
	protected static GameState createState(GameState original) {
		return (original == null) ? new State() : new State(original);
	}

	/**
	 * Checks if the game is over. This is either when
	 * the threshold for winning is reached, i.e., 
	 * the tile with the highest value meets or exceeds
	 * 2048 or the board is full and there is no option
	 * left to merge adjacent tiles of same number.
	 * @return true if the game is over, false otherwise
	 */
	protected boolean gameOver() {
		// case 1: the user reached the winning threshold
		// game is over, user wins
		if (currentState.reachedThreshold())
			return true;
		// case 2: the board has  at least one open space left
		// the game is not over, one can place another tile
		// or move tiles around
		if (!currentState.isFull()) 
			return false;
		// case 3: the board is full, 
		// game over, if one can not merge any tiles
		return !currentState.canMerge();
	}
	/**
	 * Reset the board to one that is empty but for 
	 * the given number of initial tiles that are
	 * randomly selected and assigned non-zero values.
	 * The non-zero values are randomly selected to be
	 * either 2 or 4.
	 * @param numInitialTiles gives the number of tiles with non-zero values
	 */
	protected void resetGame(int numInitialTiles) {
		// set internal counters
		score = 0;
		// set up empty board and 
		// add the first 2 tiles at random positions
		currentState.setEmptyBoard();
		for (int i = 0; i < numInitialTiles; i++) {
			currentState.addTile();
		}
	}

	/**
	 * Gives access to the internal state representation.
	 * This method purposely breaks encapsulation to allow 
	 * for an automated player to perform moves.
	 * @return a reference to the internal state.
	 */
	protected GameState getState() {
		return currentState;
	}
	/**
	 * Adds the given value to the score.
	 * @param delta to be added to the current score.
	 */
	protected void addToScore(int delta) {
		score += delta;
	}
	/**
	 * Gets the current score
	 * @return the score
	 */
	protected int getScore() {
		return score;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// draw the background
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// draw all tiles
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				drawTile((Graphics2D)g, currentState.getValue(x, y), x, y);
			}
		}
		// special cases for winning, losing, 
		if (gameOver()) {
			//if (data.reachedThreshold() || myLose) {
			drawMessageWhenGameIsOver((Graphics2D)g);
		}
		// draw the score
		g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
		g.drawString("Score: " + score, 200, 365);
	}

	/**
	 * Draw an individual tile
	 * @param g the graphics object do draw on
	 * @param value the value on the tile, 0 encodes an empty slot
	 * @param x x-coordinate on board, range 0,1,2,3
	 * @param y y-coordinate on board, range 0,1,2,3
	 */
	private void drawTile(Graphics2D g, int value, int x, int y) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		int xOffset = offsetCoors(x);
		int yOffset = offsetCoors(y);
		// Feedback on drawing
		// System.out.println("Drawing tile (x,y,value)=(" + x + "," + y + "," + value +") at (" + xOffset + "," +yOffset +")");
		// draw background
		g.setColor(getBackground(value));
		g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);

		if (value <= 0)
			return; // nothing else to do, background is sufficient

		// draw content value if there is a tile, i.e. value is > 0
		g.setColor(getForeground(value));
		final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
		final Font font = new Font(FONT_NAME, Font.BOLD, size);
		g.setFont(font);
		String content = String.valueOf(value);
		final FontMetrics fm = getFontMetrics(font);
		final int w = fm.stringWidth(content);
		final int h = -(int) fm.getLineMetrics(content, g).getBaselineOffsets()[2];
		g.drawString(content, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
	}
	/**
	 * Draws the message about winning or losing on the board. 
	 * Method is only called when the game is over.
	 * Color selection is hard coded inside this method.
	 * @param g the graphics object do draw on
	 */
	private void drawMessageWhenGameIsOver(Graphics2D g) {
		// draw background for text
		g.setColor(new Color(255, 255, 255, 30));
		g.fillRect(0, 0, getWidth(), getHeight());
		// draw message
		g.setColor(new Color(78, 139, 202));
		g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
		// winning
		if (currentState.reachedThreshold()) {
			g.drawString("You won!", 68, 150);
		}
		// losing
		else {
			g.drawString("Game over!", 50, 130);
			g.drawString("You lose!", 64, 200);
		}
		// instructions on how to continue
		g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
		g.setColor(new Color(128, 128, 128, 128));
		g.drawString("Press ESC to play again", 80, getHeight() - 40);
		
	}
	/** 
	 * Provides background color for a tile with the given value.
	 * The returned color objects are shared and recycled.
	 * The method encapsulates access to the colors array.
	 * @param value of tile
	 * @return a color for that tile
	 */
	private Color getBackground(int value) {
		switch (value) {
		case 2:    return colors[2]; //new Color(0xeee4da);
		case 4:    return colors[3]; // new Color(0xede0c8);
		case 8:    return colors[4]; // new Color(0xf2b179);
		case 16:   return colors[5]; // new Color(0xf59563);
		case 32:   return colors[6]; // new Color(0xf67c5f);
		case 64:   return colors[7]; // new Color(0xf65e3b);
		case 128:  return colors[8]; // new Color(0xedcf72);
		case 256:  return colors[9]; // new Color(0xedcc61);
		case 512:  return colors[10]; // new Color(0xedc850);
		case 1024: return colors[11]; // new Color(0xedc53f);
		case 2048: return colors[12]; // new Color(0xedc22e);
		}
		return colors[13]; // new Color(0xcdc1b4);
	}
	/** 
	 * Provides foreground color for a tile with the given value.
	 * The returned color objects are shared and recycled.
	 * The method encapsulates access to the colors array.
	 * @param value of tile
	 * @return a color for that tile
	 */
	private Color getForeground(int value) {
		return value < 16 ? colors[0] : colors[1];
	}
	// Array with colors used for drawing the tiles
	// 0: foreground color if value is less than 16
	// 1: foreground color if value is >= 16
	// 2: background color if value is 2
	// 3: background color if value is 4
	// 4: background color if value is 8
	// 5: background color if value is 16
	// 6: background color if value is 32
	// 7: background color if value is 64
	// 8: background color if value is 128
	// 9: background color if value is 256
	// 10: background color if value is 512
	// 11: background color if value is 1024
	// 12: background color if value is 2048
	// 13: default color
	private final Color[] colors = {new Color(0x776e65),new Color(0xf9f6f2),
			new Color(0xeee4da), new Color(0xede0c8), new Color(0xf2b179), 
			new Color(0xf59563), new Color(0xf67c5f), new Color(0xf65e3b), 
			new Color(0xedcf72), new Color(0xedcc61), new Color(0xedc850),
			new Color(0xedc53f), new Color(0xedc22e), new Color(0xcdc1b4)};

	private static int offsetCoors(int arg) {
		return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
	}

	/**
	 * Main method to start the game
	 * @param args unused, ignored
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setTitle("2048 Game");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(340, 400);
		frame.setResizable(false);

		if (0 == args.length) {
			// default mode
			System.out.println("Operating in default, manual mode");
			frame.add(new Game2048(true));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
		
		if (1 == args.length) {
			System.out.println("Operating in automated mode");
			Game2048 game2048 = new Game2048(false);
			frame.add(game2048);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			
			switch (args[0]) {
			case "Random":
				System.out.println("Player uses randomized strategy");
				int delay = 2000; // 2000 for normal operation
				ActionListener taskPerformer = 
						new RandomPlayer(game2048, NUMBER_OF_NEW_TILES, frame);
				final Timer timer = new Timer(delay,taskPerformer);
				timer.start();
				break;
			case "Smart":
				System.out.println("Player uses smart strategy to play");
				System.out.println("Implement this one for bonus points");
				System.out.println("Not implemented yet: using manual operation as fallback");
				// continue in manual mode
				game2048.manual = true;
				break;
			default:
				System.out.println("Unknown command line parameter: " + args[0]);
				System.out.println("Not sure what else to do: using manual operation as fallback");
				// continue in manual mode
				game2048.manual = true;
				break;
			}
		}
		
	}
}