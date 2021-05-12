
package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * JavaFx Element of exit screen supporting the display of Exit Quiz Generator.
 */
public class ExitScreen {
	private Stage window;
	private HBox hbox;

	/**
	 * The public method to launch the exit screen.
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {
		window = new Stage();
		Label goodbyeMessage = new Label("Thank you for using Quiz Generator!");
		BorderPane root = new BorderPane();
		root.setCenter(goodbyeMessage);
		root.setBottom(setBottomBox());
		root.setPadding(new Insets(5, 5, 5, 5));
		Scene scene = new Scene(root, 400, 300);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		window.setMinHeight(400);
		window.setMinWidth(500);
		window.setScene(scene);
		window.setTitle("Exit");
		window.show();
	}

	/**
	 * Private method to help close program.
	 */
	private void closeProgram() {
		System.exit(0);
	}

	/**
	 * Private method to set the bottom box
	 * @return the hbox set at the bottom.
	 */
	private HBox setBottomBox() {
		hbox = new HBox();
		Button button = new Button("Close Program");
		button.setOnAction(e -> closeProgram());
		hbox.setPadding(new Insets(10, 10, 10, 10));
		hbox.getChildren().addAll(button);
		hbox.setAlignment(Pos.CENTER);
		return hbox;
	}
}
