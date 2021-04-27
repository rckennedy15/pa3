package cs310.games;

import java.util.Scanner;

public class PlayNim3 {
	public PlayNim3() {
		g = new Nim3();
	}

	public void doComputerMove() {
		System.out.println(g.toString());
		BestMove compMove = g.chooseMove(Nim3.COMPUTER, 0);  // depth 0 call
		System.out.println("Computer plays ROW = " + compMove.i + " STARS = " + compMove.j);
		g.makeMove(Nim3.COMPUTER, compMove.i, compMove.j);
	}

	public void doHumanMove() {
		boolean legal;
		System.out.println(g.toString());
		do {
			System.out.println("row: ");
			int row = scan.nextInt();
			System.out.println("number: ");
			int num = scan.nextInt();
			legal = g.makeMove(Nim3.HUMAN, row, num);
			if (!legal)
				System.out.println("Illegal move, try again");
		} while (!legal);
	}

	// return true if game is continuing, false if done
	boolean checkAndReportStatus() {
		if (g.isWin(Nim3.COMPUTER)) {
			System.out.println("Computer says: I WIN!!");
			return false; // game is done
		}
		if (g.isWin(Nim3.HUMAN)) {
			System.out.println("Computer says: You WIN!!");
			return false; // game is done
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



	void playOneGame() {
		boolean continueGame = true;
		g.init();
		while (continueGame) {
			continueGame = getAndMakeMoves();
		}
	}

	public static void main(String[] args) {
		PlayNim3 ui = new PlayNim3();
		ui.playOneGame();
	}

	private Nim3 g; // g for game
	private Scanner scan = new Scanner(System.in);

}
