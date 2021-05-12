
package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * The JavaFx screen to display error messages.
 *
 */
public class ErrorScreen {

	/**
	 * Public method to launch the error dialogue screen.
	 * 
	 * @param primaryStage Main stage of the program
	 * @param errorMsg error message that needed to be printed
	 */
	public void start(Stage primaryStage, String errorMsg) {
		Button btOk = new Button("Ok");
		Text text = new Text(errorMsg);
		text.setTextAlignment(TextAlignment.CENTER);
		BorderPane border = new BorderPane();

		border.setCenter(text);
		border.setBottom(btOk);
		BorderPane.setAlignment(text, Pos.CENTER);
		BorderPane.setAlignment(btOk, Pos.CENTER);
		border.setPadding(new Insets(10, 10, 10, 10));
		// Set the size of the screen
		Scene scene = new Scene(border, 300, 100);
		// Set the stage of event.
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setMinHeight(200);
		stage.setMinWidth(400);
		stage.setTitle("Warning");
		stage.show();
		stage.setAlwaysOnTop(true);
		btOk.setOnAction(event -> stage.close());
	}
}
