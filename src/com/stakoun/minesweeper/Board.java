package com.stakoun.minesweeper;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * The Board class represents the game board containing all mines.
 * @author Peter Stakoun
 */
public class Board extends JPanel
{
	private int frameLength, boardLength, tileLength;
	private Tile[][] tiles;
	
	/**
	 * The sole constructor for the Board class.
	 * @param length
	 */
	public Board(int frameLength, int boardLength)
	{
		this.frameLength = frameLength;
		this.boardLength = boardLength;
		this.tileLength = frameLength/boardLength;
		tiles = new Tile[boardLength][boardLength];
		for (int i = 0; i < boardLength; i++) {
			for (int j = 0; j < boardLength; j++) {
				tiles[i][j] = new Tile();
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i = tileLength; i < frameLength; i += tileLength) {
			g2d.drawLine(i, 0, i, frameLength);
			g2d.drawLine(0, i, frameLength, i);
		}
	}

}
