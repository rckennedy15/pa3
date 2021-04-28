package cs310.games;

import java.util.HashMap;
import java.util.Map;

public class Nim4DP {
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

	// Map of Position to it's calculated value (0 or 3)
	private Map<Position, Integer> store = new HashMap<>();

	/**
	 * An internal container class to hold the game state
	 */
	private final class Position {
		private int[] heap;
		private int playerNum;

		Position(int[] heap, int playerNum) {
			this.heap = new int[NUM_ROWS];
			System.arraycopy(heap, 0, this.heap, 0, heap.length);
			this.playerNum = playerNum;
		}

		@Override
		public boolean equals(Object that) {
			if (!(that instanceof Position))
				return false;

			for (int i = 0; i < this.heap.length; i++) {
				if (this.heap[i] != ((Position) that).heap[i])
					return false;
			}

			if (this.playerNum != ((Position) that).playerNum)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int hashval = 0;

			for (int i = 0; i < this.heap.length; i++) {
				hashval += i * heap[i];
			}
			return hashval;
		}
	}

	/**
	 * Construct an instance of the cs310.games.Nim Game
	 */
	public Nim4DP() {
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
	 * @return false if move is illegal.
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

	/**
	 * @param side either 0 for HUMAN or 1 for COMPUTER
	 * @param depth keeps track of recursion depth
	 * @return the BestMove (container class) with either a 0 for a HUMAN win or a 3 for a COMPUTER win
	 */
	public BestMove chooseMove(int side, int depth) {
		final int HUMAN_WIN = 0;
		final int DRAW = 1;
		final int UNCLEAR = 2;
		final int COMPUTER_WIN = 3;

		int opp; // The other side
		BestMove reply; // Opponent's best reply
		int bestRow = -1; // Initialize running value with out-of-range value
		int bestNum = -1;
		int value;
		Position thisPosition = new Position(heap, side);

		// if ran out of stars (all heaps empty) BASE CASE
		int totalStars = 0;
		for (int i : heap) {
			totalStars += i;
		}
		if (totalStars == 0) {
			if (nextPlayer == HUMAN) {
				return new BestMove(HUMAN_WIN);
			} else {
				return new BestMove(COMPUTER_WIN);
			}
		}

		// Don't look up top-level value: at top level, we need to explore moves
		// out from here to find the best move to make)
		if (depth > 0) {
			Integer lookupVal = store.get(thisPosition);
			if (lookupVal != null)
				return new BestMove(lookupVal);
		}

		// Initialize running values with out-of-range values (good software practice)
		// Here also to ensure that *some* move is chosen, even if a hopeless case
		if (side == COMPUTER) {
			opp = HUMAN;
			value = HUMAN_WIN - 1; // impossibly low value
		} else {
			opp = COMPUTER;
			value = COMPUTER_WIN + 1; // impossibly high value
		}

		for (int row = 0; row < heap.length; row++) {
			for (int numStars = 1; numStars <= heap[row]; numStars++) {
				int origNextPlayer = nextPlayer;
				// does trial move AND changes nextPlayer number
				makeMove(side, row, numStars);
//				System.out.println("\u001B[36m after loop, returning value " + value + " at level " + depth + "\u001B[0m");

				// recursive call
				reply = chooseMove(opp, depth + 1);

				// update min or max
				if (side == COMPUTER && reply.val > value || side == HUMAN && reply.val < value) {
					value = reply.val;
					bestRow = row;
					bestNum = numStars;
				}

				// undo Move, restore player number
				nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);
				heap[row] = heap[row] + numStars;
			}
		}
		store.put(thisPosition, value);
		return new BestMove(value, bestRow, bestNum);
	}




	// unit test (not part of API, just a test of it)
	public static void main(String[] args) {

		Nim4DP g = new Nim4DP();

		g.init();
		System.out.println("Start of game:");
		System.out.println(g);

		System.out.println("play with hard coded moves");
		try {
			System.out.println("doing move: A4");
			g.makeMove(Nim4DP.COMPUTER, 0, 4);
			System.out.println(g);
			System.out.println("doing move: B2");
			g.makeMove(Nim4DP.HUMAN, 1, 2);
			System.out.println(g);
			System.out.println("doing move: C1");
			g.makeMove(Nim4DP.COMPUTER, 2, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim4DP.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim4DP.COMPUTER));
			System.out.println("doing move: B1");
			g.makeMove(Nim4DP.HUMAN, 1, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim4DP.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim4DP.COMPUTER));
			System.out.println("doing move: A1");
			g.makeMove(Nim4DP.COMPUTER, 0, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isWin(Nim4DP.HUMAN));
			System.out.println("Computer won? " + g.isWin(Nim4DP.COMPUTER));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
