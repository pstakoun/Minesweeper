package com.stakoun.minesweeper;

/**
 * The Board class represents the game board containing all mines.
 * @author Peter Stakoun
 */
public class Board
{
	private Tile[][] tiles;
	
	/**
	 * The sole constructor for the Board class.
	 * @param frameLength
	 * @param boardLength
	 */
	public Board(int length)
	{
		tiles = new Tile[length][length];
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				tiles[i][j] = new Tile();
			}
		}
	}

}
