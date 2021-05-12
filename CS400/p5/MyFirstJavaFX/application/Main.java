
package application;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    try {
      Label myLabel = new Label("CS400 MyFirstJavaFX");
      ObservableList <String> options= FXCollections.observableArrayList(
        "Option1","Option2","Option3"
      );
      final ComboBox comboBox=new ComboBox<>(options);//create a new comboBox panel
      ImageView image=new ImageView("myphoto.jpg");//create a new image view panel
      Button myButton=new Button("Done");//create a new button panel
      TextField myText= new TextField("This is cool!");//create a new testfield panel
      BorderPane root = new BorderPane();
      root.setTop(myLabel);
      root.setLeft(comboBox);
      root.setBottom(myButton);
      root.setCenter(image);
      root.setRight(myText); //set the orientation   
      Scene scene = new Scene(root, 800, 800);
      scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
      primaryStage.setTitle("Yizhou's First JavaFX program");
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
