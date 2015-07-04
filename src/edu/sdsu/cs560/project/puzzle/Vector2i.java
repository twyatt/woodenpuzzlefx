package edu.sdsu.cs560.project.puzzle;

public class Vector2i {

	public int x;
	public int y;

	public Vector2i() {
		x = 0;
		y = 0;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Vector2i that = (Vector2i) o;

		if (x != that.x) return false;
		return y == that.y;

	}

	@Override
	public int hashCode() {
		return 31 * x + y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
