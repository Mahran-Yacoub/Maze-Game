package gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.Maze;
import main.Maze.Node;

public class MazeControl {

	@FXML
	private Pane mazePane;

	@FXML
	private TextArea outputNumber;

	private Maze mazeGame;

	private int[][] board;

	private ArrayList<Maze.Node> solutions;

	private ArrayList<Maze.Node> bestSolutions;

	private int counterOfSolutions;

	private int counterBestSolutions;

	private boolean isGameExist;

	private Group path;

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will display one solution by one click if there is a solutions
	 * 
	 * @param event
	 */
	@FXML
	void drawSolutions(ActionEvent event) {

		if (isGameExist) {

			if (counterOfSolutions != 0) {

				if (path != null) {

					clearPath();
				}

				Node node = solutions.get(counterOfSolutions - 1);
				path = new Group();
				displaySolution(node, mazeGame.getEnterRowY(), mazeGame.getEnterColumnX(), path, 2);
				this.mazePane.getChildren().add(path);
				counterOfSolutions--;

			} else {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("No  Solutions");
				alert.show();

			}

		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Genertate Game First");
			alert.show();
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will display one solution by using
	 * 
	 * @param node    The solution
	 * @param startY  The row of start path
	 * @param startX  The column of statrt path
	 * @param path
	 * @param counter
	 */
	private void displaySolution(Node node, int startY, int startX, Group path, int counter) {

		int[][] board = node.getBoard();

		Pane pane = this.mazePane;
		double paneWidth = pane.getWidth();
		double paneHeight = pane.getHeight();
		double relationWidth = paneWidth / board[0].length;
		double relationHeight = paneHeight / board.length;

		int[] row = { -1, 0, 1, 0 };
		int[] col = { 0, -1, 0, 1 };

		for (int i = 0; i < row.length; i++) {

			int x = startX + col[i];
			int y = startY + row[i];

			if (x < 0 || y < 0 || x >= board.length || y >= board.length) {

				continue;
			}

			if (board[y][x] == board[startY][startX] + 1) {

				StackPane stack = new StackPane();
				stack.setLayoutX(x * relationWidth);
				stack.setLayoutY(y * relationHeight);

				Rectangle rectangle = new Rectangle(relationWidth, relationHeight);

				rectangle.setFill(Color.AQUA);

				Text numberOfMovement = new Text(counter + "");
				numberOfMovement.setFill(Color.WHITE);
				numberOfMovement.setFont(new Font(50));

				stack.getChildren().addAll(rectangle, numberOfMovement);

				path.getChildren().add(stack);

				displaySolution(node, y, x, path, counter + 1);

				break;
			}

		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	@FXML
	void displayBestSolution(ActionEvent event) {

		if (isGameExist) {

			if (counterBestSolutions != 0) {

				if (path != null) {

					clearPath();
				}

				Node node = bestSolutions.get(counterBestSolutions - 1);
				path = new Group();
				displaySolution(node, mazeGame.getEnterRowY(), mazeGame.getEnterColumnX(), path, 2);
				this.mazePane.getChildren().add(path);
				counterBestSolutions--;

			} else {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("No  Solutions");
				alert.show();

			}

		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Genertate Game First");
			alert.show();
		}

	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////

	/** this method will delete Group Object from Pane Object 
	 * , so Path that display on Screen will remove*/
	private void clearPath() {

		ObservableList<javafx.scene.Node> nodes = this.mazePane.getChildren();

		for (javafx.scene.Node node : nodes) {

			if (node instanceof Group) {

				nodes.remove(node);
				return;
			}

		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to generate maze object and store all data for other
	 * methods
	 */
	@FXML
	void generateMazeGame(ActionEvent event) {

		mazeGame = new Maze(new File("src\\obstacles.txt"));
		solutions = mazeGame.solve();
		bestSolutions = mazeGame.getBsetSolutions();
		board = mazeGame.getBoard();
		getGame(board);
		isGameExist = true;
		counterOfSolutions = mazeGame.getNumberOfSolution();
		counterBestSolutions = bestSolutions.size();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method use board 2D to display it in UI -
	 * 
	 * @param board
	 */
	private void getGame(int[][] board) {

		// 1 step
		Pane pane = this.mazePane;
		pane.getChildren().clear();
		this.outputNumber.clear();

		// 2 step
		double paneWidth = pane.getWidth();
		double paneHeight = pane.getHeight();
		double relationWidth = paneWidth / board[0].length;
		double relationHeight = paneHeight / board.length;

		// step 3 - 4
		getGamePart1(board, pane, relationWidth, relationHeight);

		// step 5
		getGamePart2(board, pane, relationWidth, relationHeight);

	}

	private void getGamePart1(int[][] board, Pane pane, double relationWidth, double relationHeight) {

		// 3 step
		ImagePattern pattern = null;

		try {

			File imageIcon = new File("src\\obstacle.png");
			Image map = new Image(new FileInputStream(imageIcon));
			pattern = new ImagePattern(map);

		} catch (FileNotFoundException e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("icon image set");
			alert.show();
		}

		// 4 step
		for (int i = 0; i < board.length; i++) {

			for (int j = 0; j < board[i].length; j++) {

				if (board[i][j] == -1) {

					Rectangle rectangle = new Rectangle(j * relationWidth, i * relationHeight, relationWidth,
							relationHeight);

					rectangle.setFill(pattern);

					pane.getChildren().add(rectangle);

				}
			}
		}

	}

	private void getGamePart2(int[][] board, Pane pane, double relationWidth, double relationHeight) {

		// 5 step
		try {

			File imageIcon1 = new File("src\\enter.png");
			File imageIcon2 = new File("src\\exit.png");

			Image map1 = new Image(new FileInputStream(imageIcon1));
			Image map2 = new Image(new FileInputStream(imageIcon2));

			ImagePattern pattern1 = new ImagePattern(map1);
			ImagePattern pattern2 = new ImagePattern(map2);

			Rectangle rectangleEnter = new Rectangle(mazeGame.getEnterColumnX() * relationWidth,
					mazeGame.getEnterRowY() * relationHeight, relationWidth, relationHeight);

			rectangleEnter.setFill(pattern1);

			Rectangle rectangleExit = new Rectangle(mazeGame.getExitX() * relationWidth,
					mazeGame.getExitY() * relationHeight, relationWidth, relationHeight);

			rectangleExit.setFill(pattern2);

			pane.getChildren().add(rectangleEnter);
			pane.getChildren().add(rectangleExit);

		} catch (FileNotFoundException e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("icon image enter and exist set");
			alert.show();
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	@FXML
	void getNumberOfSolutions(ActionEvent event) {

		if (isGameExist) {

			int numberOfSolutions = mazeGame.getNumberOfSolution();
			this.outputNumber.setText(numberOfSolutions + "");
			this.outputNumber.setEditable(false);

		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Solved First");
			alert.show();
		}

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Clear output data on A screen , but with out maze object (maze game)
	 * 
	 * @param event
	 */
	@FXML
	void clear(ActionEvent event) {

		if (isGameExist) {

			this.outputNumber.clear();
			if (path != null) {

				clearPath();
				path = null;
			}

			counterBestSolutions = bestSolutions.size();
			counterOfSolutions = solutions.size();
		}

	}

}
