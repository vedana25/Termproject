package board;

import gui.Table;


public class Preparation extends Thread {
	Board game;
	Table gameTable;
	
	public Preparation(Board game,Table gameTable) {
		this.game=game;
		this.gameTable=gameTable;
	}

	String input=null;

	public void run() {
		
		baseBoard.drawBattleBoard(BoardManager.ENTITIES_ONBOARD, Board.round);
		game.player1.setReady(true);
		game.player2.setReady(true);
	}
	public void drawBoard(Board board) {
		gameTable.getBoardPanel().drawBoard(board);
	}
}
