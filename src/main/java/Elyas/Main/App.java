package Elyas.Main;

import Elyas.view.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Elyas Syoufi
 *         <h2>main launch application for the sensor experiment.</h2>
 *         <p>
 *         Created as a project for COMP 3203, Introduction to Networking. this
 *         application simulates an experiment with sensor placement along a 1
 *         dimensional line. the application supports visualization in terms of
 *         sensor movement, sensor location, and plotting the number of moves
 *         for a number of sensors.
 *         </p>
 */
public class App extends Application {
	private static Stage mainStage;

	@Override
	public void start(Stage stage) {
		mainStage = stage;
		try {
			FXMLLoader ldr = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
			Parent root = (Parent) ldr.load();
			// load the main controller; injected from the FXML file
			final MainController appCtrl = (MainController) ldr.getController();

			Scene scene = new Scene(root, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

			stage.setScene(scene);
			stage.show();

			// shutting down the application from the 'x' button
			scene.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent ev) {
					if (!appCtrl.shutdown()) {
						ev.consume();
					}
				}
			});

			appCtrl.initializeDrawArea();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// entry point to application
	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getMainStage() {
		return mainStage;
	}
}
