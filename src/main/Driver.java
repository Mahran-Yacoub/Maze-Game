package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Driver extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("./../MazeGUI.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Maze Game");
		primaryStage.show();

	}

	//////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** Main Method to start A program*/
	public static void main(String[] args) {
		
		Application.launch(args);
	}
}
