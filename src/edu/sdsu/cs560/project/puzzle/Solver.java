package edu.sdsu.cs560.project.puzzle;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Solver {

	/**
	 * Array of block movement directions.
	 */
	static final Movement.Direction[] DIRECTIONS = Movement.Direction.values();

	private final Set<Board> visited = new HashSet<>();
	private final Queue<Board> queue = new LinkedList<>();

	private final Board solution;

	private long duration;

	/**
	 * Constructor for the solver that will search for the specified board as a
	 * solution.
	 *
	 * @param solution
	 */
	public Solver(Board solution) {
		this.solution = solution;
	}

	/**
	 * Initiates the solution search by adding the starting puzzle configuration
	 * to the queue and marking it as visited.
	 *
	 * @param puzzle
	 * @return
	 */
	public Board solve(Board puzzle) {
		long start = System.nanoTime();
		queue.add(puzzle);
		visited.add(puzzle);
		Board solution = solve();
		duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
		return solution;
	}

	/**
	 * Retrieves the time it took to solve the puzzle.
	 *
	 * @return Solution time in milliseconds.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Generates a list of block movements that were taken to get to the
	 * specified puzzle.
	 *
	 * @param puzzle
	 * @return
	 */
	public List<String> replay(Board puzzle) {
		List<String> replay = new ArrayList<>();

		List<Movement> moves = new ArrayList<>();
		while (puzzle != null) {
			if (puzzle.parent == null) break;
			moves.add(puzzle.movement);
			puzzle = puzzle.parent;
		}
		Collections.reverse(moves);

		for (Movement move : moves) {
			Vector2i position = Bitboard.position(puzzle.blocks[move.index], puzzle.width);
			replay.add("Move piece at coordinates " + position + " one unit " + move.direction.toString().toLowerCase());
			puzzle = puzzle.move(move.index, move.direction);
		}

		return replay;
	}

	/**
	 * Continually tries new puzzle configurations until a solution is found, at
	 * which point it returns a list of movements to the solution puzzle
	 * configuration.
	 *
	 * Breadth-first search algorithm is used for finding the solution.
	 * http://en.wikipedia.org/wiki/Breadth-first_search
	 *
	 * @return
	 */
	private Board solve() {
		while (!queue.isEmpty()) {
			Board puzzle = queue.poll();

			if (isSolution(puzzle)) {
				queue.clear();
				return puzzle;
			}

			queueNextPuzzles(puzzle);
		}
		return null;
	}

	/**
	 * Queues puzzles of possible configurations for processing, based on the
	 * provided puzzle configuration.
	 *
	 * @param puzzle
	 */
	private void queueNextPuzzles(Board puzzle) {
		for (int i = 0; i < puzzle.blocks.length; i++) {
			for (int j = 0; j < DIRECTIONS.length; j++) {
				Board moved = puzzle.move(i, DIRECTIONS[j]);

				if (moved != null && !visited.contains(moved)) {
					queue.add(moved);
					visited.add(moved);
				}
			}
		}
	}

	/**
	 * Determines if the puzzle specified is a solution.
	 *
	 * @param puzzle
	 * @return
	 */
	private boolean isSolution(Board puzzle) {
		for (int i = 0; i < solution.blocks.length; i++) {
			if (solution.blocks[i] != 0 && solution.blocks[i] != puzzle.blocks[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Generates an ordered list of block movements that were taken to get to
	 * the specified puzzle configuration.
	 *
	 * @param puzzle
	 * @return
	 */
	public List<Movement> getMovements(Board puzzle) {
		List<Movement> moves = new ArrayList<>();
		while (puzzle != null) {
			if (puzzle.movement != null) {
				moves.add(puzzle.movement);
			}
			puzzle = puzzle.parent;
		}
		Collections.reverse(moves);
		return moves;
	}

}
