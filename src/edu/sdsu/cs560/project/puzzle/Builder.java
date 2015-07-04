package edu.sdsu.cs560.project.puzzle;

import java.io.*;
import java.util.*;

public class Builder {

	private String[] names;

	/**
	 * Reads board from specified file.
	 *
	 * @param file
	 * @return
	 */
	public Board build(File file) throws FileNotFoundException {
		FileInputStream stream = new FileInputStream(file);
		Board board = build(stream);
		try {
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return board;
	}

	/**
	 * Reads board from input stream. Note that the input stream is not closed.
	 *
	 * @param stream
	 * @return
	 */
	public Board build(InputStream stream) {
		// http://stackoverflow.com/a/5445161/196486
		Scanner scanner = new Scanner(stream).useDelimiter("\\A");
		String string = scanner.hasNext() ? scanner.next() : "";
		return build(string);
	}

	/**
	 * Reads board from the provided newline delimited string.
	 *
	 * @param puzzle
	 * @return
	 */
	public Board build(String puzzle) {
		return build(puzzle.split(System.getProperty("line.separator")));
	}

	public Board build(String... lines) {
		Map<String, Integer> blocks = new HashMap<>();

		int width = 0;
		int height = lines.length;
		String[][] array = new String[height][];

		for (int y = 0; y < height; y++) {
			String line = lines[y];
			array[y] = line.split("(?<=\\G.)\\s");
			width = Math.max(width, array[y].length);
		}

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < array[y].length; x++) {
				String name = array[y][x];
				if (!name.trim().isEmpty()) {
					int block = blocks.get(name) == null ? 0 : blocks.get(name);
					int index = x + width * y;
					block |= (1 << index);
					blocks.put(name, block);
				}
			}
		}

		Map<Integer, List<Integer>> groups = new HashMap<>();

		if (names == null) {
			names = blocks.keySet().toArray(new String[blocks.size()]);
		}
		int[] b = new int[names.length];

		for (int i = 0; i < names.length; i++) {
			if (blocks.get(names[i]) != null) {
				int block = blocks.get(names[i]);
				b[i] = block; // build blocks array

				// build groups based on bitboard "shape"
				Integer shape = Bitboard.shape(block, width, height);
				List<Integer> group = groups.get(shape);
				if (group == null) {
					group = new ArrayList<>();
					groups.put(shape, group);
				}
				group.add(block);
			}
		}

		// build groups
		List<Integer> groupValues = new ArrayList<>();
		for (Map.Entry<Integer, List<Integer>> group : groups.entrySet()) {
			int v = 0;
			if (group.getValue().size() > 1) {
				for (int g : group.getValue()) {
					v |= g;
				}
				groupValues.add(v);
			}
		}
		int[] theGroups = new int[groupValues.size()];
		for (int j = 0; j < theGroups.length; j++) {
			theGroups[j] = groupValues.get(j).intValue();
		}
		return new Board(width, height, b, theGroups);
	}

	public String[] getNames() {
		return names;
	}

}
