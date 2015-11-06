package com.stakoun.minesweeper;

/**
 * The Tile class represents each spot on the board.
 * @author Peter Stakoun
 */
public class Tile
{
	private boolean isMine, hasFlag, isVisible;
	
	/**
	 * The sole constructor for the Tile class.
	 */
	public Tile()
	{
		
	}
	
	public void addMine() { isMine = true; }
	
	public void toggleFlag() {
		if (hasFlag) { hasFlag = false; }
		else { hasFlag = true; }
	}
	
	public void show() { isVisible = true; }
	
	public boolean isMine() { return isMine; }
	
	public boolean hasFlag() { return hasFlag; }
	
	public boolean isVisible() { return isVisible; }

}
