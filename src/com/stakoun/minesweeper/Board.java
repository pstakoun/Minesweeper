package com.stakoun.minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		this.game = game;
		this.frameLength = frameLength;
		this.boardLength = boardLength;
		this.tileLength = frameLength / boardLength;
		this.numMines = numMines;
		tiles = new Tile[boardLength][boardLength];
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				tiles[i][j] = new Tile();
			}
		}
		addMouseListener(new TileClickListener(this));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		for (int i = tileLength; i < frameLength; i += tileLength) {
			g2d.drawLine(i, 0, i, frameLength);
			g2d.drawLine(0, i, frameLength, i);
		}
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				if (tiles[i][j].isVisible()) {
					if (tiles[i][j].hasMine()) {
						g2d.setColor(Color.RED);
						g2d.fillRect(i*tileLength, j*tileLength, tileLength, tileLength);
					} else {
						g2d.setColor(Color.BLACK);
						g2d.drawString(String.valueOf(tiles[i][j].getSurroundingMines()), i*tileLength+tileLength/2, j*tileLength+tileLength/2);
					}
				}
				else if (tiles[i][j].hasFlag()) {
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillRect(i*tileLength, j*tileLength, tileLength, tileLength);
				}
			}
		}
	}

	public void showTile(int x, int y)
	{
		Tile tile = tiles[x/tileLength][y/tileLength];
		if (game.getState() == Game.PREGAME) {
			initMines(tile);
			game.setState(Game.INGAME);
		}
		if (!tile.hasFlag()) {
			tile.show();
			repaint();
			if (tile.hasMine()) {
				game.setState(Game.POSTGAME);
			}
		}
	}
	
	public void initMines(Tile initTile)
	{
		for (int i = 0; i < numMines; i++) {
			Tile tile;
			do {
				tile = getRandomTile();
			} while (tile.hasMine() || tile == initTile);
			tile.addMine();
		}
	}

	public void flagTile(int x, int y)
	{
		Tile tile = tiles[x/tileLength][y/tileLength];
		tile.toggleFlag();
		repaint();
	}
	
	private Tile getRandomTile()
	{
		Random rand = new Random();
		int x = rand.nextInt(boardLength);
		int y = rand.nextInt(boardLength);
		return tiles[x][y];
	}
	
}

class TileClickListener extends MouseAdapter
{
	private Board board;

	public TileClickListener(Board board)
	{
		this.board = board;
	}

	public void mousePressed(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON1) {
			board.showTile(e.getX(), e.getY());
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			board.flagTile(e.getX(), e.getY());
		}
	}

}
