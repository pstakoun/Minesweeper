package com.stakoun.minesweeper;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The Game class controls all gameplay.
 * @author Peter Stakoun
 */
public class Game
{
	// Declare game state constants
	public static final int PREGAME = 0;
	public static final int INGAME = 1;
	public static final int WIN = 2;
	public static final int LOSE = 3;
	
	private int frameLength, boardLength, numMines, state;
	private Dimension boardSize;
	private JFrame frame;
	private Board board;

	/**
	 * The main constructor for the Game class.
	 */
	public Game(int frameLength, int boardLength, int numMines)
	{
		// Initialize sizes
		this.frameLength = frameLength;
		this.boardLength = boardLength;
		this.numMines = numMines;
		
		// Initialize game board
		boardSize = new Dimension(frameLength, frameLength);
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board(this, frameLength, boardLength, numMines);
		board.setPreferredSize(boardSize);
		frame.add(board);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		// Initialize game state
		state = PREGAME;
	}

	/**
	 * The default constructor for the Game class.
	 */
	public Game()
	{
		// Initialize sizes
		frameLength = 500;
		boardLength = 10;
		numMines = 15;
		
		// Initialize game board
		boardSize = new Dimension(frameLength, frameLength);
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board(this, frameLength, boardLength, numMines);
		board.setPreferredSize(boardSize);
		frame.add(board);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		// Initialize game state
		state = PREGAME;
	}
	
	/**
	 * The main method creates a new instance of the Game class.
	 * @param args
	 */
	public static void main(String[] args)
	{
		try {
			int a = Integer.parseInt(args[0]);
			int b = Integer.parseInt(args[1]);
			int c = Integer.parseInt(args[2]);
			if (a % b != 0 || a < 0 || b < a || c < 0)
				throw new Exception();
			new Game(a, b, c);
		} catch (Exception e) {
			System.out.println("Invalid arguments!");
			new Game();
		}
	}
	
	public void setState(int state) { this.state = state; }
	
	public int getState() { return state; }

}
