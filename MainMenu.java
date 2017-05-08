import com.sun.glass.ui.MenuItem;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainMenu extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("Main Menu");
		Pane root = new Pane();
		root.setPrefSize(300, 300);	
		/*Rectangle bg = new Rectangle(300, 300);
		bg.setFill(Color.BLACK);*/
		
		Button start = new Button("START");
		Button options = new Button("OPTIONS");
		Button tutorial = new Button("TUTORIAL");
		Button exit = new Button("EXIT");
		exit.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		VBox menuBox = new VBox(5, start, options, tutorial, exit);
		menuBox.setAlignment(Pos.TOP_CENTER);
		menuBox.setTranslateY(70);
		
		Scene scene = new Scene(menuBox, 300, 300);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//temporary to test
	public static void main(String[] args) {
		launch(args);
	}
}
