package com.stakoun.minesweeper;

/**
 * The Tile class represents each spot on the board.
 * @author Peter Stakoun
 */
public class Tile
{
	private boolean isMine, hasFlag;
	
	/**
	 * The sole constructor for the Tile class.
	 */
	public Tile()
	{
		
	}
	
	public void addMine() { isMine = true; }
	
	public void addFlag() { hasFlag = true; }
	
	public void removeFlag() { hasFlag = false; }
	
	public boolean isMine() { return isMine; }
	
	public boolean hasFlag() { return hasFlag; }

}
