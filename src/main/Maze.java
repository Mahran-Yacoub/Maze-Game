package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This Class is Used to create Objects to represent Maze Game
 * 
 * @author Mahran Yacoub
 *
 */
public class Maze {

	/** The Dimension of Maze Game / 2D Array */
	private final int size;

	/**
	 * row and col represent The selection move that can we choose South , West
	 * ,North , East in Order
	 */
	public static final int[] row = { -1, 0, 1, 0 };
	public static final int[] col = { 0, -1, 0, 1 };

	private int numberOfSolution;

	private int minimumMovements = Integer.MAX_VALUE;

	private ArrayList<Node> solutions = new ArrayList<>();

	/** The Column Number of exit splot */
	private final int exitX;

	/** The Row Number of exit splot */
	private final int exitY;

	/** The Column Number of enter splot */
	private final int enterRowY;

	/** The Row Number of enter splot */
	private final int enterColumnX;

	/** The 2D array that will represent Maze Game */
	private int[][] board;
	
	private File obstacles;

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * The Constructor take file and read first line tp get 
	 * initial inforamtion and initialize an object
	 * 
	 * @param file
	 */
	public Maze(File file) {

		int size = 0;
		int enterColumnX = 0;
		int enterRowY = 0;
		int exitColumnX = 0;
		int exitRowY = 0;

		try {

			Scanner scan = new Scanner(file);

			if (scan.hasNextLine()) {

				String line = scan.nextLine();
				String[] parts = line.split(",");

				size = new Integer(parts[0]);
				enterRowY = new Integer(parts[1]);
				enterColumnX = new Integer(parts[2]);
				exitRowY = new Integer(parts[3]);
				exitColumnX = new Integer(parts[4]);

			}

			scan.close();

		} catch (IOException e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Read File Error");
			alert.show();

		} catch (Exception e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Enter Correct Numbers");
			alert.show();
		}

		this.size = size;
		this.enterColumnX= enterColumnX;
		this.enterRowY = enterRowY;
		this.exitX = exitColumnX;
		this.exitY = exitRowY;

		obstacles = file;

	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will check each selection from the above , that is valid to be
	 * part of a solution / solution or not .
	 * 
	 * @param board The 2D array that represent Maze Game
	 * @param x     The column Number of selection
	 * @param y     The row Number of Selection
	 * @return true :valid , false :invalid
	 * 
	 */
	private boolean isValidSelection(int[][] board,  int y , int x) {

		if (x < 0 || y < 0 || x >= size || y >= size || board[y][x] != 0) {

			return false;
		}

		return true;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will check if we got a solution or not By check if we reach the
	 * exit point or not.
	 * 
	 * @param x The column Number of selection
	 * @param y The row Number of Selection
	 * @return true :reach a solution , false : Not
	 * 
	 */
	private boolean isAsolution(int y , int x) {

		return (x == exitX && y == exitY) ? true : false;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/** This method is used to print given 2D array */
	private void print(int[][] board) {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {

				System.out.print(" " + board[i][j] + " ");
			}

			System.out.println();
		}

		System.out.println("-------------------------------------------");
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method is used to print all solution that can be reach from solved a
	 * maze game if there are solutions (it may be un solveable )
	 */
	public void printSolutions() {

		if (this.solutions.size() != 0) {

			for (Node node : this.solutions) {

				print(node.getBoard());

			}

		} else {

			System.out.println("There is no solutions");

		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * print the best solution , that is has minimum movements May be there more
	 * than one best solution and may be there is no solutions for a maxe game
	 */
	public void printBsetSolution() {

		if (this.solutions.size() != 0) {

			for (Node node : this.solutions) {

				if (node.getNumberOfMovement() == minimumMovements) {

					System.out.println("The best solution has : " + node.getNumberOfMovement() + " Movements");
					print(node.getBoard());
				}
			}

		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will solve a maze game that this object represent and store
	 * solutions
	 */
	public ArrayList<Node> solve() {

		startMazeSolution();
		getSolutions(board, enterRowY, enterColumnX, 2);
		return solutions;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This is Arecursion method that apply backtracking techiniques to solve maze
	 * game and store solutions is ArrayList and store number of movements in 2D
	 * array of each solution
	 * 
	 * @param board
	 * @param rowY
	 * @param columnX
	 * @param numberOfMovement
	 */
	private void getSolutions(int[][] board, int rowY, int columnX, int numberOfMovement) {

		for (int i = 0; i < col.length; i++) {

			int newX = columnX + col[i];
			int newY = rowY + row[i];

			if (isValidSelection(board,newY,newX)) {

				board[newY][newX] = numberOfMovement;

				if (isAsolution(newY,newX)) {

					numberOfSolution++;

					if (numberOfMovement < minimumMovements) {

						minimumMovements = numberOfMovement;
					}

					solutions.add(new Node(board, numberOfMovement));

				} else {

					getSolutions(board, newY, newX, numberOfMovement + 1);

				}

				board[newY][newX] = 0;
			}

		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method set initial things to solve Maze Game
	 */
	private void startMazeSolution() {

		numberOfSolution = 0;
		solutions.clear();
		minimumMovements = Integer.MAX_VALUE;

		int[][] board = new int[size][size];
		fillBoard(board, this.obstacles);
		board[enterRowY][enterColumnX] = 1;
		board[exitY][exitX] = 0;

		this.board = board;

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	public ArrayList<Node> getBsetSolutions() {

		ArrayList<Node> bestSolutions = new ArrayList<Maze.Node>();

		if (this.solutions.size() != 0) {

			for (Node node : this.solutions) {

				if (node.getNumberOfMovement() == minimumMovements) {

					bestSolutions.add(node);
				}
			}

		}

		return bestSolutions;

	}

	public ArrayList<Node> getSolutions() {
		
		return solutions;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	private void fillBoard(int[][] board, File obstacles) {

		Scanner scan = null;

		try {

			scan = new Scanner(obstacles);
			//skip first line
			scan.nextLine();

		} catch (IOException e) {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("fill File Error");
			alert.show();
		}

		for (int i = 0; i < board.length; i++) {

			String line = scan.nextLine();
			String[] parts = line.split("\t");

			for (int j = 0; j < board[i].length; j++) {

				int part = new Integer(parts[j]);

				board[i][j] = part;

			}
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////

	public int getNumberOfSolution() {
		return numberOfSolution;
	}

	public int getExitX() {
		return exitX;
	}

	public int getExitY() {
		return exitY;
	}

	public int getEnterRowY() {
		return enterRowY;
	}

	public int getEnterColumnX() {
		return enterColumnX;
	}

	public int[][] getBoard() {
		return board;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Inner Class to to create an object contains 2D array of a solution 
	 * and number of movement of each solution 
	 * 
	 * @author Mahran
	 *
	 */
	public static class Node {

		private int[][] board;
		private int numberOfMovement;

		public Node(int[][] board, int numberOfMovement) {

			this.board = copy(board);
			this.numberOfMovement = numberOfMovement;
		}

		private int[][] copy(int[][] board2) {

			int[][] copyOfBoard = new int[board2.length][board2.length];

			for (int i = 0; i < board2.length; i++) {

				for (int j = 0; j < board2[i].length; j++) {

					copyOfBoard[i][j] = board2[i][j];
				}
			}

			return copyOfBoard;
		}

		public int[][] getBoard() {
			return board;
		}

		public void setBoard(int[][] board) {
			this.board = board;
		}

		public int getNumberOfMovement() {
			return numberOfMovement;
		}

		public void setNumberOfMovement(int numberOfMovement) {
			this.numberOfMovement = numberOfMovement;
		}

	}

}
