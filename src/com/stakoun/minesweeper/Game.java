package com.stakoun.minesweeper;

/**
 * The Game class controls all gameplay.
 * @author Peter Stakoun
 */
public class Game
{
	private int width, height;
	private Board board;
	
	/**
	 * The sole constructor for the Game class.
	 */
	public Game()
	{
		// Initialize game board
		board = new Board(width, height);
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
