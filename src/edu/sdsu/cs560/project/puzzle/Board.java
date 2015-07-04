package edu.sdsu.cs560.project.puzzle;

public class Board implements Comparable<Board> {

	public static final String EMPTY_CELL_TEXT = "_";

	/**
	 * Parent to this board.
	 */
	public final Board parent;

	/**
	 * The movement used to get to this board configuration.
	 */
	public final Movement movement;

	/*
	 * Dimensions of the board.
	 */
	public final int width;
	public final int height;

	/*
	 * Bitboards for the edges of the wooden puzzle (used for edge detection).
	 */
	private int top;
	private int right;
	private int bottom;
	private int left;

	/**
	 * Bitboard that keeps track of puzzle occupancy.
	 */
	public int occupied;

	/**
	 * Array of bitboards that store group occupancy for the blocks.
	 */
	public int[] groups;

	/**
	 * Array of bitboards that store occupancy information for each block.
	 */
	public int[] blocks;

	private int hash;

	/**
	 * Constructor for a new board associated to the specified parent board.
	 *
	 * Blocks and occupancy bitboards will be copied over from the parent board.
	 *
	 * @param parent
	 * @param movement
	 */
	public Board(Board parent, Movement movement) {
		this.parent   = parent;
		this.movement = movement;

		width  = parent.width;
		height = parent.height;

		top    = parent.top;
		right  = parent.right;
		bottom = parent.bottom;
		left   = parent.left;

		occupied = parent.occupied;

		groups = new int[parent.groups.length];
		System.arraycopy(parent.groups, 0, groups, 0, groups.length);

		blocks = new int[parent.blocks.length];
		System.arraycopy(parent.blocks, 0, blocks, 0, blocks.length);
	}

	/**
	 * Constructor for a new starting board (that will not have an association
	 * to a parent board).
	 *
	 * @param width
	 * @param height
	 * @param blocks
	 */
	public Board(int width, int height, int[] blocks, int[] groups) {
		if (width * height > Integer.SIZE) {
			throw new IllegalArgumentException("Requested board size exceeds maximum supported.");
		}

		parent   = null;
		movement = null;

		this.width  = width;
		this.height = height;

		top    = Bitboard.draw(0, width,         0,          0, width,      1);
		right  = Bitboard.draw(0, width, width - 1,          0,     1, height);
		bottom = Bitboard.draw(0, width,         0, height - 1, width,      1);
		left   = Bitboard.draw(0, width,         0,          0,     1, height);

		occupied = Bitboard.combine(blocks);
		this.groups = groups;
		this.blocks = blocks;
	}

	/**
	 * Searches for the specified block name in the provided array of block
	 * names.
	 *
	 * @param name
	 * @param names
	 * @return Index of block or -1 if block index is not found.
	 */
	public static int indexOf(String name, String[] names) {
		for (int i = 0; i < names.length; i++) {
			String block = names[i];
			if (block.equals(name)) return i;
		}
		return -1;
	}

	/**
	 * Moves the block with the specified index in the specified direction and
	 * returns the new board configuration. No changes will occur to this board.
	 *
	 * If the move is not possible (either because of board bounds or block
	 * overlap) then null is returned.
	 *
	 * @param index
	 * @param direction
	 * @return
	 */
	public Board move(int index, Movement.Direction direction) {
		int block = blocks[index];

		if (Movement.Direction.UP.equals(direction) && Bitboard.overlaps(top, block)) {
			return null;
		}
		if (Movement.Direction.RIGHT.equals(direction) && Bitboard.overlaps(right, block)) {
			return null;
		}
		if (Movement.Direction.DOWN.equals(direction) && Bitboard.overlaps(bottom, block)) {
			return null;
		}
		if (Movement.Direction.LEFT.equals(direction) && Bitboard.overlaps(left, block)) {
			return null;
		}

		int occupied = Bitboard.subtract(this.occupied, block);
		int moved = Bitboard.shift(block, width, direction.x, direction.y);
		if (Bitboard.overlaps(occupied, moved)) {
			return null;
		}

		int group = getMembership(index);

		Board puzzle = new Board(this, new Movement(index, direction));
		puzzle.blocks[index] = moved;
		puzzle.occupied = occupied | moved;
		if (group != -1) {
			puzzle.groups[group] = Bitboard.subtract(groups[group], block) | moved;
		}
		return puzzle;
	}

	/**
	 * Retrieves the group index that the block with the specified index is a
	 * member of.
	 *
	 * @param i
	 * @return Group index or -1 if block with specified index is not in a group
	 */
	public int getMembership(int i) {
		for (int j = 0; j < groups.length; j++) {
			if (Bitboard.overlaps(blocks[i], groups[j])) {
				return j;
			}
		}
		return -1;
	}

	@Override
	public int compareTo(Board o) {
		int thisValue = hashCode();
		int thatValue = o.hashCode();
		return (thisValue < thatValue ? -1 : (thisValue == thatValue ? 0 : 1));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Board that = (Board) o;

		if (blocks.length != that.blocks.length) return false;
		if (groups.length != that.groups.length) return false;

		for (int i = 0; i < groups.length; i++) {
			if (groups[i] != that.groups[i]) return false;
		}

		for (int i = 0; i < blocks.length; i++) {
			if (getMembership(i) == -1) {
				if (blocks[i] != that.blocks[i]) return false;
			}
		}

		return true;
	}

	@Override
	public int hashCode() {
		int h = hash;
		if (h == 0) {
			for (int i = 0; i < groups.length; i++) {
				h = 31*h + groups[i];
			}
			for (int i = 0; i < blocks.length; i++) {
				if (getMembership(i) == -1) {
					h = 31*h + Integer.numberOfTrailingZeros(blocks[i]);
				}
			}
			hash = h;
		}
		return h;
	}

	@Override
	public String toString() {
		return toString(null);
	}

	public String toString(String[] names) {
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				builder.append(" ");
				boolean found = false;
				for (int i = 0; i < blocks.length; i++) {
					int block = blocks[i];
					if (Bitboard.isAt(block, width, x, y)) {
						if (names == null) {
							builder.append(i);
						} else {
							builder.append(names[i]);
						}
						found = true;
						break;
					}
				}
				if (!found) {
					builder.append(EMPTY_CELL_TEXT);
				}
			}
			builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}

}
