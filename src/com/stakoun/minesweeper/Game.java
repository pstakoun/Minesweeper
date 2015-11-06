package com.stakoun.minesweeper;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Game class controls all gameplay.
 * @author Peter Stakoun
 */
public class Game
{
	private int frameLength;
	private Dimension frameSize;
	private JFrame frame;
	private JPanel panel;
	private int boardLength;
	private Board board;
	private int tileLength;
	
	/**
	 * The sole constructor for the Game class.
	 */
	public Game()
	{
		// Initialize game frame and panel
		frameSize = new Dimension(frameLength, frameLength);
		frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(frameSize);
		panel = new JPanel();
		panel.setSize(frameSize);
		frame.add(panel);
		frame.setVisible(true);
		
		// Initialize game board
		boardLength = 20;
		board = new Board(boardLength);
		
		// Initialize tiles
		tileLength = frameLength/boardLength;
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
