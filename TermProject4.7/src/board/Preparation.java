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
		System.out.println("put "+Board.maxObj+" objects to the board");

		game.player1.setReady(false);
		game.player2.setReady(false);
	}
	public void drawBoard(Board board) {
		gameTable.getBoardPanel().drawBoard(board);
	}
}
