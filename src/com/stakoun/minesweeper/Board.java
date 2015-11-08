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
				tiles[i][j] = new Tile(i, j);
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
				Tile tile = tiles[i][j];
				if (tile.isVisible()) {
					if (tiles[i][j].hasMine()) {
						g2d.setColor(Color.RED);
						g2d.fillRect(i*tileLength+1, j*tileLength+1, tileLength-1, tileLength-1);
					} else {
						g2d.setColor(Color.BLACK);
						g2d.drawString(String.valueOf(tile.getSurroundingMines()), i*tileLength+tileLength/2, j*tileLength+tileLength/2);
						System.out.println(tile.getSurroundingMines());
					}
				}
				else if (tiles[i][j].hasFlag()) {
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillRect(i*tileLength+1, j*tileLength+1, tileLength-1, tileLength-1);
				}
			}
		}
	}
	
	private void resetTiles()
	{
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				tiles[i][j].reset();
			}
		}
		repaint();
	}
	
	private void initMines(Tile initTile)
	{
		for (int i = 0; i < numMines; i++) {
			Tile tile;
			Tile[] surrounding;
			int xDiff, yDiff;
			do {
				tile = getRandomTile();
				surrounding = getSurroundingTiles(tile);
				xDiff = tile.getX()-initTile.getX();
				yDiff = tile.getY()-initTile.getY();
			} while (tile.hasMine() || tile == initTile
					|| (xDiff < 2 && xDiff > -2
					&& yDiff < 2 && yDiff > -2));
			for (Tile t : surrounding)
				t.addSurroundingMine();
			tile.addMine();
		}
	}
	
	private void showSurroundingTiles(Tile tile)
	{
		if (tile.getSurroundingMines() > 0)
			return;
		Tile[] surroundingTiles = getSurroundingTiles(tile);
		for (Tile t : surroundingTiles)
			if (!t.isVisible())
				showTile(t);
	}
	
	private Tile[] getSurroundingTiles(Tile tile)
	{
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
		
		int numSurrounding = 0;
		for (int[] point : points) {
			int x = point[0];
			int y = point[1];
			if (x >= 0 && x < boardLength && y >= 0 && y < boardLength)
				numSurrounding++;
		}
		
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
		
		return surroundingTiles;
	}
	
	private Tile getRandomTile()
	{
		Random rand = new Random();
		int x = rand.nextInt(boardLength);
		int y = rand.nextInt(boardLength);
		return tiles[x][y];
	}

	private void showTile(Tile tile)
	{
		if (game.getState() == Game.PREGAME) {
			initMines(tile);
			game.setState(Game.INGAME);
		} else if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			resetTiles();
			game.setState(Game.PREGAME);
			return;
		}
		if (!tile.hasFlag()) {
			tile.show();
			if (tile.getSurroundingMines() == 0)
				showSurroundingTiles(tile);
			repaint();
			if (tile.hasMine()) {
				game.setState(Game.LOSE);
			}
		}
	}

	public void showTile(int x, int y)
	{
		Tile tile = tiles[x/tileLength][y/tileLength];
		if (game.getState() == Game.PREGAME) {
			initMines(tile);
			game.setState(Game.INGAME);
		} else if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			resetTiles();
			game.setState(Game.PREGAME);
			return;
		}
		if (!tile.hasFlag()) {
			tile.show();
			showSurroundingTiles(tile);
			repaint();
			if (tile.hasMine()) {
				game.setState(Game.LOSE);
			}
		}
	}
	
	public void flagTile(int x, int y)
	{
		if (game.getState() == Game.LOSE || game.getState() == Game.WIN) {
			resetTiles();
			game.setState(Game.PREGAME);
			return;
		}
		Tile tile = tiles[x/tileLength][y/tileLength];
		tile.toggleFlag();
		repaint();
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
