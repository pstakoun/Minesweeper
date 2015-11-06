package com.stakoun.minesweeper;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * The Board class represents the game board containing all mines.
 * 
 * @author Peter Stakoun
 */
public class Board extends JPanel
{
	private int frameLength, boardLength, tileLength;
	private Tile[][] tiles;

	/**
	 * The sole constructor for the Board class.
	 * 
	 * @param length
	 */
	public Board(int frameLength, int boardLength)
	{
		this.frameLength = frameLength;
		this.boardLength = boardLength;
		this.tileLength = frameLength / boardLength;
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
		System.out.println("paint");
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				if (tiles[i][j].isVisible()) {
					g2d.fillRect(i*tileLength, j*tileLength, tileLength, tileLength);
				}
				else if (tiles[i][j].hasFlag()) {
					g2d.setColor(Color.RED);
					g2d.fillRect(i*tileLength, j*tileLength, tileLength, tileLength);
					g2d.setColor(Color.BLACK);
				}
			}
		}
	}

	public void showTile(int x, int y)
	{
		Tile tile = tiles[x/tileLength][y/tileLength];
		tile.show();
		repaint();
	}

	public void flagTile(int x, int y)
	{
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
