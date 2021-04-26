package cs310.games;

import java.util.Scanner;

public class PlayTicTacToe1 {
	public PlayTicTacToe1() {
		g = new TicTacToe1();
	}

	public void doComputerMove() {
		printBoard();  // TODO: call cs310.games.TicTacToe's printBoard here
		Best compMove = g.chooseMove(TicTacToe.COMPUTER, 0);  // depth 0 call
		System.out.println("Computer plays ROW = " + compMove.row + " COL = " + compMove.column);
		g.playMove(TicTacToe.COMPUTER, compMove.row, compMove.column);
	}

	public void doHumanMove() {
		boolean legal;
		printBoard();  // TODO: call cs310.games.TicTacToe's printBoard here
		do {
			System.out.println("row: ");
			int row = scan.nextInt();
			System.out.println("column: ");
			int col = scan.nextInt();
			legal = g.playMove(TicTacToe.HUMAN, row, col);
			if (!legal)
				System.out.println("Illegal move, try again");
		} while (!legal);
	}
	
	// return true if game is continuing, false if done
	boolean checkAndReportStatus() {
		if (g.isAWin(TicTacToe.COMPUTER)) {
			System.out.println("Computer says: I WIN!!");
			return false; // game is done
		}
		if (g.isAWin(TicTacToe.HUMAN)) {
			System.out.println("Computer says: You WIN!!");
			return false; // game is done
		}
		if (g.isADraw()) {
			System.out.println(" Game is a DRAW");
			return false;
		}
		System.out.println("game continuing");
		return true;
	}

	// do one round of playing the game, return true at end of game
	public boolean getAndMakeMoves() {
		// let computer go first...
		doComputerMove();
		System.out.println("back from doComputerMove");
		// System.out.println("count = " + t.getCount());
		if (!checkAndReportStatus())
			return false; // game over
		doHumanMove();
		if (!checkAndReportStatus())
			return false; // game over
		return true;
	}

	void printBoard() {
		// TODO: stop using this encapsulation-busting method
		// move it to cs310.games.TicTacToe
		int[][] board = g.getBoard();  
		StringBuilder boardStr = new StringBuilder("");
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				String spot;
				switch (board[row][col]) {
				case TicTacToe.HUMAN:
					spot = " " + humanSide + " ";
					break;
				case TicTacToe.COMPUTER:
					spot = " " + computerSide + " ";
					break;
				case TicTacToe.EMPTY:
					spot = "   ";
					break;
				default:
					System.out.println("Bad board entry in printBoard");
					return;
				}
				boardStr.append(spot);
				if (col < 2)
					boardStr.append("|");
			}
			if (row < 2)
				boardStr.append("\n-----------\n");
		}
		System.out.println(boardStr);

	}

	void playOneGame() {
		boolean continueGame = true;
		g.clearBoard();
		while (continueGame) {
			continueGame = getAndMakeMoves();
		}
	}

	public static void main(String[] args) {
		PlayTicTacToe1 ui = new PlayTicTacToe1();
		ui.playOneGame();
	}

	private TicTacToe1 g; // g for game
	private Scanner scan = new Scanner(System.in);
	private String computerSide = "O";
	private String humanSide = "X";

}
