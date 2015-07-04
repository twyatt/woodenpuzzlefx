package edu.sdsu.cs560.project.puzzle;

import java.util.Arrays;
import java.util.List;

public class Launcher {

	static String[] names;

	public static void main(String[] args) {
		solve(
				"A A B B F\n" +
				"J J E G  \n" +
				"J J E H  \n" +
				"C C D D I",

				"         \n" +
				"      J J\n" +
				"      J J\n" +
				"         "
		);
	}

	public static void solve(String start) {
		solve(
				start,

				"         \n" +
				"      J J\n" +
				"      J J\n" +
				"         "
		);
	}

	public static void solve(String start, String end) {
		Builder builder = new Builder();
		Board puzzle = builder.build(start);
		names = builder.getNames();

		Board solution = builder.build(end);

		System.out.println("Initial configuration:");
		System.out.println(puzzle.toString(names));

		System.out.println("Solution configuration:");
		System.out.println(solution.toString(names));

		Solver solver = new Solver(solution);
		puzzle = solver.solve(puzzle);

		List<String> moves = solver.replay(puzzle);
		if (moves.isEmpty()) {
			System.out.println("No solutions found.");
		} else {
			for (String move : moves) {
				System.out.println(move);
			}
		}

		System.out.println();
		System.out.println("Solution took " + (solver.getDuration() / 1000f) + " seconds and " + moves.size() + " moves.");
	}

}
