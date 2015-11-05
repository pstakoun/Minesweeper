package com.stakoun.minesweeper;

/**
 * The Board class represents the game board containing all mines.
 * @author Peter Stakoun
 */
public class Board
{
	private int width, height;
	private Tile[][] tiles;
	
	/**
	 * The sole constructor for the Board class.
	 * @param width
	 * @param height
	 */
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		tiles = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				tiles[i][j] = new Tile();
			}
		}
	}

}
