package edu.sdsu.cs560.project.puzzle;

/**
 * Represents the movement of a block.
 */
public class Movement {

	public enum Direction {
		UP   ( 0, -1),
		RIGHT( 1,  0),
		DOWN ( 0,  1),
		LEFT (-1,  0),
		;
		public final int x;
		public final int y;
		Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public int index;
	public Direction direction;

	/**
	 * Constructor for a block movement using the specified block index and
	 * movement direction.
	 *
	 * @param index Block index
	 * @param direction Direction of movement
	 */
	public Movement(int index, Direction direction) {
		this.index = index;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() +
				'{' +
				"index=" + index + ", " +
				"direction=" + direction +
				'}';
	}
}
