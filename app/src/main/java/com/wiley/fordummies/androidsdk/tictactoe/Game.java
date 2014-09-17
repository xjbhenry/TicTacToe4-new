package com.wiley.fordummies.androidsdk.tictactoe;

public class Game {
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;

	private enum STATE { Inactive, Active, Won, Draw };
	private STATE gameState=STATE.Inactive;;
	
	private Symbol currentSymbol=null;
	
	private enum PLAYER {Player1, Player2};
	private PLAYER currentPlayer=PLAYER.Player1;
	private PLAYER winningPlayer=PLAYER.Player1;
	
	private String PlayerOneName=null, PlayerTwoName=null;
	
	private GameGrid gameGrid=null;
	
	private int playCount=0;
	
	Game(){ //Constructor
		gameGrid = new GameGrid();
		gameState = STATE.Active;
		currentSymbol=Symbol.SymbolXCreate();
	}
	
	GameGrid getGameGrid(){
		return gameGrid;
	}
	void setPlayerNames(String FirstPlayer, String SecondPlayer){
		PlayerOneName=FirstPlayer; PlayerTwoName=SecondPlayer;
	}
	String getPlayerOneName(){return PlayerOneName;}
	String getPlayerTwoName(){return PlayerTwoName;}
	String getCurrentPlayerName(){
		if (currentPlayer==PLAYER.Player1) return PlayerOneName; else return PlayerTwoName;
	}
	String getWinningPlayerName(){
		if (winningPlayer==PLAYER.Player1) return PlayerOneName; else return PlayerTwoName;
	}
	public Symbol getCurrentSymbol(){
		return currentSymbol;
	}
	public boolean play(int x, int y){
		boolean successfulPlay=false;
		if ((gameGrid.getValueAtLocation(x, y)==Symbol.SymbolBlankCreate())){
			successfulPlay = true;
			playCount++;
			gameGrid.setValueAtLocation(x, y, currentSymbol);
			checkResultAndSetState();
			if(gameState == STATE.Active){// if the game is still active
				// Swap symbols and players
				if(currentSymbol == Symbol.SymbolXCreate()) 
					currentSymbol=Symbol.SymbolOCreate(); 
				else 
					currentSymbol=Symbol.SymbolXCreate();
				if(currentPlayer==PLAYER.Player1) currentPlayer=PLAYER.Player2; else currentPlayer=PLAYER.Player1;
			}
		}
		return successfulPlay;
	}
	
	private void checkResultAndSetState(){
		if(gameGrid.isRowFilled(0)||
		   gameGrid.isRowFilled(1)||
		   gameGrid.isRowFilled(2)||
		   gameGrid.isColumnFilled(0)||
		   gameGrid.isColumnFilled(1)||
		   gameGrid.isColumnFilled(2)||
		   gameGrid.isLeftToRightDiagonalFilled()||
		   gameGrid.isRightToLeftDiagonalFilled()){
			winningPlayer = currentPlayer;
			gameState = STATE.Won;
		}else if (playCount==9){
			gameState = STATE.Draw;  
		} /* else, leave state as is */
	}
	
	boolean isActive(){
		return gameState==STATE.Active;
	}
	boolean isWon(){
		return gameState==STATE.Won;
	}
	boolean isDrawn(){
		return gameState==STATE.Draw;
	}
	public int getPlayCount(){
		return playCount;
	}

}
