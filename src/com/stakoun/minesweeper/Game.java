package com.stakoun.minesweeper;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The Game class controls all gameplay.
 * @author Peter Stakoun
 */
public class Game
{
	private int frameLength, boardLength;
	private Dimension boardSize;
	private JFrame frame;
	private Board board;
	
	/**
	 * The sole constructor for the Game class.
	 */
	public Game()
	{
		frameLength = 500;
		boardLength = 10;
		
		// Initialize game board
		boardSize = new Dimension(500, 500);
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		board = new Board(frameLength, boardLength);
		board.setPreferredSize(boardSize);
		frame.add(board);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * The main method creates a new instance of Game.
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Game();
	}

}
