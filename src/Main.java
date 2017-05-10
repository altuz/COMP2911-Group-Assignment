import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;


public class Main extends Application implements EventHandler<ActionEvent>{
	
	

	public static void main(String[] args) {
		launch(args);
	}
	
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Title of the window");
		
		
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(3);
		grid.setHgap(3);
		
		int sampleMap[][] = {{-1, -1, -1, -1, -1, -1},
							{-1, -1,  0,  0, -1, -1},
							{-1,  1,  2,  0, -1, -1},
							{-1, -1,  2,  0, -1, -1},
							{-1, -1,  0,  2,  0, -1},
							{-1,  3,  2,  0,  0, -1},
							{-1,  3,  3,  5,  3, -1},
							{-1, -1, -1, -1, -1, -1}
		};
		
		int rows = 8;
		int cols = 6;
		
		for(int i = 0; i < rows ; i++){
			for(int j = 0; j < cols ; j++){
				Rectangle newRect = new Rectangle(20,20);
				int blockType = sampleMap[i][j];
				switch(blockType){
					case -1: newRect.setFill(Color.BLACK); break;		//immovable
					case 0 : newRect.setFill(Color.ALICEBLUE); break;		//spaces
					case 1 : newRect.setFill(Color.MAROON); break;			//player
					case 2 : newRect.setFill(Color.OLIVE); break;			//boxes
					case 3 : newRect.setFill(Color.BLUEVIOLET); break;		//End points
					case 4 : newRect.setFill(Color.MEDIUMTURQUOISE); break;	//player on endpoint
					case 5 : newRect.setFill(Color.LIME); break;			//box on endpoint
				}
	            GridPane.setConstraints(newRect, j, i);
	            grid.add(newRect,j,i);
			}
		}
		Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

		
	}



	@Override
	public void handle(ActionEvent arg0) {
		
		
	}
	
	

}
