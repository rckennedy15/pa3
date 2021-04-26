package cs310.games;

public class Nim {
	public static final int HUMAN = 0;
	public static final int COMPUTER = 1;
	public static final int UNCLEAR = 2;

	final static int NUM_ROWS = 3;
	private final static int SZ_ROW0 = 5;
	private final static int SZ_ROW1 = 3;
	private final static int SZ_ROW2 = 1;

	// These fields represent the actual position at any time.
	private int[] heap = new int[NUM_ROWS];
	private int nextPlayer;

	/**
	 * Construct an instance of the cs310.games.Nim Game
	 */
	public Nim() {
	}

	/**
	 * Set up the position ready to play.
	 */
	public void init() {
		heap[0] = SZ_ROW0;
		heap[1] = SZ_ROW1;
		heap[2] = SZ_ROW2;
		nextPlayer = COMPUTER;
	}
	
	/**
	 * Has the player of side side won? Returns true or false
	 * 
	 * @return true iff side side has won.
	 */
	public boolean isWin(int side) {
		// win happens with no stars left for other player, now nextPlayer
		// for example, HUMAN took last star, so nextPlayer == COMPUTER now
		return getStarsLeft() == 0 && side == nextPlayer;
	}

	/**
	 * Make a move
	 * 
	 * @param side of player making move
	 * @param row 
	 * @param number of stars taken.
	 * @returns false if move is illegal.
	 */
	public boolean makeMove(int side, int row, int number) {
		if (side != nextPlayer) {
			return false; // wrong player played
		}
		if (!isLegal(row, number)) {
			return false;
		} else {
			nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);
			heap[row] = heap[row] - number;
		}
		return true;
	}
	
	/**
	 * What are the rules of the game? How are moves entered interactively?
	 * 
	 * @return a String with this information.
	 */
	public String help() {
		StringBuffer s = new StringBuffer("\ncs310.games.Nim is the name of the game.\n");
		s.append("The board contains three ");
		s.append("rows of stars.\nA move removes stars (at least one) ");
		s.append("from a single row.\nThe player who takes the last star loses.\n");
		s.append("Type Xn (or xn) at the terminal to remove n stars from row X.\n");
		s.append("? displays the current position, q quits.\n");
		return s.toString();
	}

	/**
	 * This method displays current position and next player
	 * i.e., the full current state of the game
	 */
	public String toString() {
		StringBuilder board = new StringBuilder("");

		for (int i = 0; i < NUM_ROWS; i++) {
			char c = (char) ((int) 'A' + i);
			board.append(c + ": ");
			for (int j = heap[i]; j > 0; j--) {
				board.append("* ");
			}
			board.append('\n');
		}
		board.append("next to play: "+ (nextPlayer==0?"HUMAN":"COMPUTER"));

		return board.toString();
	}

	/**
	 * Compute the total number of stars left.
	 */
	private int getStarsLeft() {
		return (heap[0] + heap[1] + heap[2]);
	}

	private boolean isLegal(int row, int stars) {
		return 0 <= row && row <= 2 && stars >= 1 && stars <= heap[row];
	}

	// unit test (not part of API, just a test of it)
	public static void main(String[] args) {

		Nim g = new Nim();

		g.init();
		System.out.println("Start of game:");
		System.out.println(g);

		System.out.println("play with hard coded moves");
		try {
			System.out.println("doing move: A4");
			g.makeMove(Nim.COMPUTER, 0, 4);
			System.out.println(g);
			System.out.println("doing move: B2");
			g.makeMove(Nim.HUMAN, 1, 2);
			System.out.println(g);
			System.out.println("doing move: C1");
			g.makeMove(Nim.COMPUTER, 2, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim.COMPUTER));
			System.out.println("doing move: B1");
			g.makeMove(Nim.HUMAN, 1, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim.COMPUTER));
			System.out.println("doing move: A1");
			g.makeMove(Nim.COMPUTER, 0, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim.COMPUTER));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
