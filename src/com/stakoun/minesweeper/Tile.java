package com.stakoun.minesweeper;

/**
 * The Tile class represents each spot on the board.
 * @author Peter Stakoun
 */
public class Tile
{
	private boolean isMine, hasFlag, isVisible;
	private int surroundingMines;
	
	/**
	 * The sole constructor for the Tile class.
	 */
	public Tile()
	{
		surroundingMines = 0;
	}
	
	public void addMine() { isMine = true; }
	
	public void toggleFlag() {
		if (hasFlag) { hasFlag = false; }
		else { hasFlag = true; }
	}
	
	public void show() { isVisible = true; }
	
	public void addSurroundingMine() { surroundingMines++; }
	
	public boolean isMine() { return isMine; }
	
	public boolean hasFlag() { return hasFlag; }
	
	public boolean isVisible() { return isVisible; }
	
	public int getSurroundingMines() { return surroundingMines; }

}
