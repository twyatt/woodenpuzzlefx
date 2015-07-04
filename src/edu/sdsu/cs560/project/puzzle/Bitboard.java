package edu.sdsu.cs560.project.puzzle;

/**
 * A bitboard provides an efficient means of storing grid occupancy information
 * whereas bits are numbered from left to right, top to bottom, starting at 0
 * and wrapping at the width of the bitboard.
 *
 * For example:
 * A bitboard with a width and height of 3 by 2 would be represented by bits
 * numbered in the following configuration:
 * .-----------.
 * | 0 | 1 | 2 |
 * |---+---+---|
 * | 3 | 4 | 5 |
 * '-----------'
 *
 * The 2D axis of the bitboard has it's origin in the upperleft corner with the
 * X axis increasing to the right and the Y axis increasing downward. The same
 * bitboard shown above, with points (1, 0) and (2, 1) occupied, would look as
 * follows:
 * .-----------.
 * | 0 | 1 | 0 |
 * |---+---+---|
 * | 0 | 0 | 1 |
 * '-----------'
 *
 * The resulting integer value of the above bitboard would be:
 * 0b0000_0000_0000_0000_0000_0000_0010_0010 = 0x22 = 34
 */
public class Bitboard {

	/**
	 * Returns the value of a bitboard by combining the occupancy of the
	 * specified bitboards.
	 *
	 * @param bitboards
	 * @return
	 */
	public static int combine(int... bitboards) {
		int combined = 0;
		for (int bitboard : bitboards) {
			combined |= bitboard;
		}
		return combined;
	}

	public static int draw(int bitboard, int width, int x, int y) {
		return bitboard | 1 << indexOf(width, x, y);
	}

	public static int draw(int bitboard, int width, int x, int y, int w, int h) {
		for (int i = x; i < x + w; i++) {
			for (int j = y; j < y + h; j++) {
				bitboard |= 1 << indexOf(width, i, j);
			}
		}
		return bitboard;
	}

	/**
	 * Returns the value of the bitboard with the specified point erased.
	 *
	 * @param x
	 * @param y
	 */
	public static int erase(int bitboard, int width, int x, int y) {
		return bitboard & ~(1 << indexOf(width, x, y));
	}

	/**
	 * Returns the value of bitboard1 subtracted by bitboard2.
	 *
	 * @param bitboard1
	 * @param bitboard2
	 * @return
	 */
	public static int subtract(int bitboard1, int bitboard2) {
		return bitboard1 & ~bitboard2;
	}

	/**
	 * Determines the bitboard index of the specified point.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static int indexOf(int width, int x, int y) {
		return x + width * y;
	}

	/**
	 * Determines the index of the specified bitboard (i.e. the index of the top
	 * most, left aligned bit).
	 *
	 * For example, the following bitboard would have an index of 1, because the
	 * top-most (first row) has a bit on (in the second column) whereas that
	 * position on the bitboard is index 1:
	 * .-----------.
	 * | 0 | 1 | 0 |
	 * |---+---+---|
	 * | 0 | 0 | 1 |
	 * '-----------'
	 *
	 * @param bitboard
	 * @return
	 */
	public static int indexOf(int bitboard) {
		return Integer.numberOfTrailingZeros(bitboard);
	}

	/**
	 * Determines the value (1 or 0) of the bitboard at the specified X and Y.
	 *
	 * @param bitboard
	 * @param width
	 * @param x
	 * @param y
	 * @return
	 */
	public static int valueAt(int bitboard, int width, int x, int y) {
		return bitboard & (1 << indexOf(width, x, y));
	}

	/**
	 * Returns the value of the bitboard when shifted the specified amount in
	 * X and Y.
	 *
	 * @param x
	 * @param y
	 */
	public static int shift(int bitboard, int width, int x, int y) {
		int shift = indexOf(width, x, y);
		if (shift < 0) {
			return bitboard >>> -shift;
		} else {
			return bitboard << shift;
		}
	}

	/**
	 * Determines if the specified point is occupied.
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isAt(int bitboard, int width, int x, int y) {
		return valueAt(bitboard, width, x, y) != 0;
	}

	/**
	 * Determines if the two bitboards have any points occupied that overlap
	 * each other.
	 *
	 * @param bitboard1
	 * @param bitboard2
	 * @return
	 */
	public static boolean overlaps(int bitboard1, int bitboard2) {
		return (bitboard1 & bitboard2) != 0;
	}

	/**
	 * Determines the position of the first on bit in the specified bitboard.
	 *
	 * @param bitboard
	 * @param width
	 * @return
	 */
	public static Vector2i position(int bitboard, int width) {
		int index = indexOf(bitboard);
		return new Vector2i(index % width, index / width);
	}

	/**
	 * Determines the width and height of the on bits in the specified bitboard.
	 *
	 * @param bitboard
	 * @param width Bitboard width
	 * @param height Bitboard height
	 * @return
	 */
	public static Vector2i size(int bitboard, int width, int height) {
		if (bitboard == 0) return new Vector2i(0, 0);

		int x1 = Integer.MAX_VALUE;
		int x2 = Integer.MIN_VALUE;
		int y1 = Integer.MAX_VALUE;
		int y2 = Integer.MIN_VALUE;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isAt(bitboard, width, x, y)) {
					x1 = Math.min(x1, x);
					x2 = Math.max(x2, x);
					y1 = Math.min(y1, y);
					y2 = Math.max(y2, y);
				}
			}
		}

		return new Vector2i(x2 - x1 + 1, y2 - y1 + 1);
	}

	/**
	 * Determines the "shape" of the on bits in the bitboard. This can be seen
	 * as an ID for the arrangement of on bits.
	 *
	 * For example, the following two bitboards would have the same "shape":
	 *
	 * 000000    000101
	 * 001010 == 001111
	 * 011110    000000
	 *
	 * @param bitboard
	 * @param width
	 * @param height
	 * @return
	 */
	public static int shape(int bitboard, int width, int height) {
		if (bitboard == 0) return 0;

		int x0 = Integer.MAX_VALUE;
		int y0 = Integer.MAX_VALUE;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isAt(bitboard, width, x, y)) {
					x0 = Math.min(x0, x);
					y0 = Math.min(y0, y);
				}
			}
		}

		return Bitboard.shift(bitboard, width, -x0, -y0);
	}

	public static String toString(int bitboard, int width, int height) {
		return toString(bitboard, width, height, "1", "0");
	}

	public static String toString(int bitboard, int width, int height, String occupied, String empty) {
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				builder.append(" ").append(isAt(bitboard, width, x, y) ? occupied : empty);
			}
			builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}
}
