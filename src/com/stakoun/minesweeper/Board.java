package com.stakoun.minesweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JPanel;

/**
 * The Board class represents the game board containing all mines.
 * 
 * @author Peter Stakoun
 */
public class Board extends JPanel
{
	private int frameLength, boardLength, tileLength, numMines;
	private Game game;
	private Tile[][] tiles;

	/**
	 * The sole constructor for the Board class.
	 * 
	 * @param length
	 */
	public Board(Game game, int frameLength, int boardLength, int numMines)
	{
		// Initialize instance variables
		this.game = game;
		this.frameLength = frameLength;
		this.boardLength = boardLength;
		this.tileLength = frameLength / boardLength;
		this.numMines = numMines;

		// Initialize Tiles
		tiles = new Tile[boardLength][boardLength];
		for (int i = 0; i < boardLength; i++)
			for (int j = 0; j < boardLength; j++)
				tiles[i][j] = new Tile(i, j);
		
		// Add listener for Tile interaction
		addMouseListener(new TileClickListener(this));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// Check win condition
		if (allTilesVisible())
			game.setState(Game.WIN);
		
		// Cast to Graphics2D
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		
		// Draw Tiles
		for (int i = tileLength; i < frameLength; i += tileLength) {
			g2d.drawLine(i, 0, i, frameLength);
			g2d.drawLine(0, i, frameLength, i);
		}
		
		// Fill each Tile if needed
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				// Get current Tile from array
				Tile tile = tiles[i][j];
				
				if (tile.isVisible()) {
					// Color tile red if it has a mine
					if (tiles[i][j].hasMine()) {
						g2d.setColor(Color.RED);
						g2d.fillRect(i*tileLength+1, j*tileLength+1, tileLength-1, tileLength-1);
					// Otherwise, show number of surrounding mines
					} else {
						g2d.setColor(Color.BLACK);
						drawStringInCenter(g2d, String.valueOf(tile.getSurroundingMines()), i*tileLength*2+tileLength, j*tileLength*2+tileLength);
					}
				}
				// Show all mines if game has been lost
				else if (game.getState() == Game.LOSE && tiles[i][j].hasMine()) {
					g2d.setColor(Color.RED);
					g2d.fillRect(i*tileLength+1, j*tileLength+1, tileLength-1, tileLength-1);
				}
				// Color tile gray if it has a flag
				else if (tiles[i][j].hasFlag()) {
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillRect(i*tileLength+1, j*tileLength+1, tileLength-1, tileLength-1);
				}
			}
		}
		
		// Display congratulatory text if game has been won
		if (game.getState() == Game.WIN) {
			g2d.setColor(Color.GREEN);
			g2d.setFont(new Font("default", Font.BOLD, 42));
			drawStringInCenter(g2d, "You win!", frameLength, frameLength);
		}
	}
	
	/**
	 * The reset method clears and resets all tiles on the board.
	 */
	private void reset()
	{
		for (int i = 0; i < boardLength; i++)
			for (int j = 0; j < boardLength; j++)
				tiles[i][j].reset();
		repaint();
	}
	
	/**
	 * The initMines method randomly places mines around the board.
	 * @param initTile
	 */
	private void initMines(Tile initTile)
	{
		for (int i = 0; i < numMines; i++) {
			Tile tile;
			Tile[] surrounding;
			int xDiff, yDiff;
			
			do {
				// Get a random Tile on the Board
				tile = getRandomTile();
				// Get Tiles surrounding tile
				surrounding = getSurroundingTiles(tile);
				// Get the distance from tile to the initial Tile
				xDiff = tile.getX()-initTile.getX();
				yDiff = tile.getY()-initTile.getY();
			// Get a different Tile if tile is not a valid new mine
			} while (tile.hasMine() || tile == initTile
					|| (xDiff < 2 && xDiff > -2 && yDiff < 2 && yDiff > -2));
			
			// Increment suroundingMines for all surrounding mines
			for (Tile t : surrounding)
				t.addSurroundingMine();
			tile.addMine();
		}
	}
	
	/**
	 * The showSurroundingTiles method shows tiles surrounding the given tile
	 * if none of them have mines.
	 * @param tile
	 */
	private void showSurroundingTiles(Tile tile)
	{
		// Escape the method if there are surrounding mines
		if (tile.getSurroundingMines() > 0)
			return;
		
		// Get tiles surrounding the given Tile
		Tile[] surroundingTiles = getSurroundingTiles(tile);
		// Show all surrounding Tiles
		for (Tile t : surroundingTiles)
			if (!t.isVisible())
				showTile(t);
	}
	
	/**
	 * The getSurroundingTiles method returns an array of Tiles
	 * surrounding the given Tile.
	 * @param tile
	 * @return Tiles surrounding the given Tile.
	 */
	private Tile[] getSurroundingTiles(Tile tile)
	{
		// Initialize locations of surrounding Tiles
		int[][] points = new int[][] {
			{tile.getX(), tile.getY()-1},
			{tile.getX(), tile.getY()+1},
			{tile.getX()-1, tile.getY()},
			{tile.getX()+1, tile.getY()},
			{tile.getX()-1, tile.getY()-1},
			{tile.getX()+1, tile.getY()-1},
			{tile.getX()-1, tile.getY()+1},
			{tile.getX()+1, tile.getY()+1}
		};
		
		// Increment numSurrounding with each valid surrounding tile
		int numSurrounding = 0;
		for (int[] point : points) {
			int x = point[0];
			int y = point[1];
			if (x >= 0 && x < boardLength && y >= 0 && y < boardLength)
				numSurrounding++;
		}
		
		// Add all valid Tiles to array
		Tile[] surroundingTiles = new Tile[numSurrounding];
		int i = 0;
		for (int[] point : points) {
			int x = point[0];
			int y = point[1];
			if (x >= 0 && x < boardLength && y >= 0 && y < boardLength) {
				surroundingTiles[i] = tiles[x][y];
				i++;
			}
		}
		
		// Return valid surrounding Tiles
		return surroundingTiles;
	}
	
	/**
	 * The getRandomTile method finds and returns a random Tile.
	 * @return a random Tile on the board.
	 */
	private Tile getRandomTile()
	{
		Random rand = new Random();
		int x = rand.nextInt(boardLength);
		int y = rand.nextInt(boardLength);
		return tiles[x][y];
	}
	
	/**
	 * The allTilesVisible method checks if all the Tiles without mines
	 * on the Board are visible.
	 * @return whether or not all Tiles w/o a mine are visible.
	 */
	private boolean allTilesVisible()
	{
		for (Tile[] tileRow : tiles)
			for (Tile tile : tileRow)
				if (!tile.hasMine() && !tile.isVisible())
					return false;
		return true;
	}
	
	/**
	 * The drawStringInCenter method draws the given String in the center
	 * of the given location using a Graphics2D object.
	 * @param g
	 * @param s
	 * @param w
	 * @param h
	 */
	private void drawStringInCenter(Graphics2D g, String s, int w, int h)
	{
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(s, g);
        int x = (w-(int)r.getWidth())/2;
        int y = (h-(int)r.getHeight())/2+fm.getAscent();
        g.drawString(s, x, y);
	}

	/**
	 * The showTile method handles a click event to show a given Tile.
	 * @param tile
	 */
	private void showTile(Tile tile)
	{
		// Initialize all Tiles on the Board if game is not started
		if (game.getState() == Game.PREGAME) {
			initMines(tile);
			game.setState(Game.INGAME);
		// Reset all Tiles on the Board if game is finished
		} else if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			reset();
			game.setState(Game.PREGAME);
			return;
		}
		
		// Show tile if it doesn't have a flag
		if (!tile.hasFlag()) {
			tile.show();
			// Set game's state to lost if tile has a mine
			if (tile.hasMine())
				game.setState(Game.LOSE);
			// Otherwise, show tile's surrounding Tiles
			else
				showSurroundingTiles(tile);
			repaint();
		}
	}

	/**
	 * The showTile method handles a click event to show a given Tile.
	 * @param x
	 * @param y
	 */
	public void showTile(int x, int y)
	{
		// Get tile from location on screen
		Tile tile = tiles[x/tileLength][y/tileLength];
		// Initialize all Tiles on the Board if game is not started
		if (game.getState() == Game.PREGAME) {
			initMines(tile);
			game.setState(Game.INGAME);
		// Reset all Tiles on the Board if game is finished
		} else if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			reset();
			game.setState(Game.PREGAME);
			return;
		}
		
		// Show tile if it doesn't have a flag
		if (!tile.hasFlag()) {
			tile.show();
			// Set game's state to lost if tile has a mine
			if (tile.hasMine())
				game.setState(Game.LOSE);
			// Otherwise, show tile's surrounding Tiles
			else
				showSurroundingTiles(tile);
			repaint();
		}
	}
	
	/**
	 * The flagTile method handles a click event to flag a given Tile.
	 * @param x
	 * @param y
	 */
	public void flagTile(int x, int y)
	{
		if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			reset();
			game.setState(Game.PREGAME);
			return;
		}
		Tile tile = tiles[x/tileLength][y/tileLength];
		tile.toggleFlag();
		repaint();
	}
	
}

/**
 * The TileClickListener class handles mousePressed events and redirects
 * the information to methods in Board.
 */
class TileClickListener extends MouseAdapter
{
	private Board board;

	/**
	 * The sole constructor for the TileClickListener class.
	 * @param board
	 */
	public TileClickListener(Board board)
	{
		this.board = board;
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		// Handle left click event
		if (e.getButton() == MouseEvent.BUTTON1)
			board.showTile(e.getX(), e.getY());
		// Handle right click event
		else if (e.getButton() == MouseEvent.BUTTON3)
			board.flagTile(e.getX(), e.getY());
	}

}
