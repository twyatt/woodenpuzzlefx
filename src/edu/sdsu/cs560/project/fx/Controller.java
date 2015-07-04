package edu.sdsu.cs560.project.fx;


import java.util.List;

import edu.sdsu.cs560.project.puzzle.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {

	private static final int WIDTH  = 5;
	private static final int HEIGHT = 4;

	@FXML
	private List<Rectangle> grid, grid1;

	@FXML
	private Rectangle red, orange, yellow, green, teal,
						blue, indigo, violet, magenta, black, white;

	@FXML
	private Rectangle red1, orange1, yellow1, green1, teal1,
						blue1, indigo1, violet1, magenta1, black1, white1;

	@FXML
	private Color color = Color.RED;

	@FXML
	private Button solve, solve1;


	@FXML
	public void clicked(MouseEvent event) {
		if (red.equals((Rectangle) event.getTarget())) {
			color = Color.RED;
		} else if (orange.equals((Rectangle) event.getTarget())) {
			color = Color.ORANGE;
		} else if (yellow.equals((Rectangle) event.getTarget())) {
			color = Color.YELLOW;
		} else if (green.equals((Rectangle) event.getTarget())) {
			color = Color.GREEN;
		} else if (teal.equals((Rectangle) event.getTarget())) {
			color = Color.TEAL;
		} else if (blue.equals((Rectangle) event.getTarget())) {
			color = Color.DODGERBLUE;
		} else if (indigo.equals((Rectangle) event.getTarget())) {
			color = Color.INDIGO;
		} else if (violet.equals((Rectangle) event.getTarget())) {
			color = Color.VIOLET;
		} else if (magenta.equals((Rectangle) event.getTarget())) {
			color = Color.MAGENTA;
		} else if (black.equals((Rectangle) event.getTarget())) {
			color = Color.BLACK;
		} else if (white.equals((Rectangle) event.getTarget())) {
			color = Color.WHITE;
		}
	}

	@FXML
	public void clicked1(MouseEvent event) {
		if (red1.equals((Rectangle) event.getTarget())) {
			color = Color.RED;
		} else if (orange1.equals((Rectangle) event.getTarget())) {
			color = Color.ORANGE;
		} else if (yellow1.equals((Rectangle) event.getTarget())) {
			color = Color.YELLOW;
		} else if (green1.equals((Rectangle) event.getTarget())) {
			color = Color.GREEN;
		} else if (teal1.equals((Rectangle) event.getTarget())) {
			color = Color.TEAL;
		} else if (blue1.equals((Rectangle) event.getTarget())) {
			color = Color.DODGERBLUE;
		} else if (indigo1.equals((Rectangle) event.getTarget())) {
			color = Color.INDIGO;
		} else if (violet1.equals((Rectangle) event.getTarget())) {
			color = Color.VIOLET;
		} else if (magenta1.equals((Rectangle) event.getTarget())) {
			color = Color.MAGENTA;
		} else if (black1.equals((Rectangle) event.getTarget())) {
			color = Color.BLACK;
		} else if (white1.equals((Rectangle) event.getTarget())) {
			color = Color.WHITE;
		}
	}

	@FXML
	public void changeColor(MouseEvent event) {
		for(Rectangle rec : grid)
			if(rec.equals((Rectangle)event.getTarget())) rec.setFill(color);
	}

	@FXML
	public void changeColor1(MouseEvent event) {
		for(Rectangle rec : grid1)
			if(rec.equals((Rectangle)event.getTarget())) rec.setFill(color);
	}

	@FXML
	public void solve(ActionEvent event) {
		System.out.println("Solving now...");
		int i=0;
		String [] board = new String[20];
		for(Rectangle rec : grid) {
			if (rec.getFill() == Color.RED) {
				board[i] = "A";
				i++;
			}
			else if(rec.getFill() == Color.ORANGE) {
				board[i] = "B";
				i++;
			}
			else if(rec.getFill() == Color.YELLOW) {
				board[i] = "C";
				i++;
			}
			else if(rec.getFill() == Color.GREEN) {
				board[i] = "D";
				i++;
			}
			else if(rec.getFill() == Color.TEAL) {
				board[i] = "E";
				i++;
			}
			else if(rec.getFill() == Color.DODGERBLUE) {
				board[i] = "F";
				i++;
			}
			else if(rec.getFill() == Color.INDIGO) {
				board[i] = "G";
				i++;
			}
			else if(rec.getFill() == Color.VIOLET) {
				board[i] = "H";
				i++;
			}
			else if(rec.getFill() == Color.MAGENTA) {
				board[i] = "I";
				i++;
			}
			else if(rec.getFill() == Color.BLACK) {
				board[i] = "J";
				i++;
			}
			else if(rec.getFill() == Color.WHITE) {
				board[i] = "_";
				i++;
			}
		}
		int j=0;
		String [] board1 = new String[20];
		for(Rectangle rec1 : grid1) {
			if (rec1.getFill() == Color.RED) {
				board1[j] = "A";
				j++;
			}
			else if(rec1.getFill() == Color.ORANGE) {
				board1[j] = "B";
				j++;
			}
			else if(rec1.getFill() == Color.YELLOW) {
				board1[j] = "C";
				j++;
			}
			else if(rec1.getFill() == Color.GREEN) {
				board1[j] = "D";
				j++;
			}
			else if(rec1.getFill() == Color.TEAL) {
				board1[j] = "E";
				j++;
			}
			else if(rec1.getFill() == Color.DODGERBLUE) {
				board1[j] = "F";
				j++;
			}
			else if(rec1.getFill() == Color.INDIGO) {
				board1[j] = "G";
				j++;
			}
			else if(rec1.getFill() == Color.VIOLET) {
				board1[j] = "H";
				j++;
			}
			else if(rec1.getFill() == Color.MAGENTA) {
				board1[j] = "I";
				j++;
			}
			else if(rec1.getFill() == Color.BLACK) {
				board1[j] = "J";
				j++;
			}
			else if(rec1.getFill() == Color.WHITE) {
				board1[j] = "_";
				j++;
			}
		}

//		for(int m=0;m<board1.length;m++)
//			System.out.print(board[m]+" ");
//		System.out.println();
//		for(int k=0;k<board1.length;k++)
//				System.out.print(board1[k]+" ");
//		System.out.println();

		String start = toString(board);
		String end = toString(board1);

		Launcher.solve(start, end);
	}

	public static String toString(String[] board) {
		int i = 0;
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < HEIGHT; y++) {
			if (y != 0) {
				builder.append(System.getProperty("line.separator"));
			}

			for (int x = 0; x < WIDTH; x++) {
				if (x != 0) {
					builder.append(" ");
				}
				builder.append(board[i++]);
			}
		}
		return builder.toString().replace('_', ' ');
	}
}
