package GameLogic;

import javafx.application.Application;
import Definitions.Movement;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import GameLogic.GameState; 


public class graphicsProcessor extends Application{
	public final int W = 600;
	public final int H = 600;
	

	public static void main(String ...args) {
		launch(args);
	}

	/*
	 * Function below generates a window and grid based on 
	 * the maze generated in the backend
	 * Currently reads a file that has hardcoded the maze
	 * 
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Game");
		Pane root = new Pane();
		root.setPrefSize(W, H);
		MainMenu mainmenu = new MainMenu(W, H);
		
		Scene scene = new Scene(mainmenu, W, H);
		//create string object containing file name
		Object o = "test_maze.txt";
		//generate game state to display
		GameState state = new GameState(o); 
		primaryStage.setResizable(false);
		//show grid initially
        primaryStage.setScene(scene);
        
        if(mainmenu.getStart().isVisible() == true) {
        	mainmenu.getGrid().setVisible(true);
            showGrid(state.getMaze(),mainmenu.getGrid(),primaryStage);
        }
        //control mechanism, takes keybaord events in the form of up,down,left,right keys only
        scene.setOnKeyPressed(event -> {
        	if(mainmenu.getStart().isVisible() == true && mainmenu.getStart().getOpacity() == 1) {
        	//call backend functions to change the maze array depending on key pressed
        	if(event.getCode() == KeyCode.RIGHT) {
        		state.player_move(Movement.RIGHT);
        	} else if(event.getCode() == KeyCode.LEFT) {
        		state.player_move(Movement.LEFT);
        	} else if(event.getCode() == KeyCode.UP) {
        		state.player_move(Movement.UP);
        	} else if (event.getCode() == KeyCode.DOWN) {
        		state.player_move(Movement.DOWN);
        	}
        	showGrid(state.getMaze(),mainmenu.getGrid(),primaryStage);
        	}
        });

	}


	/*
	 * function that takes in maze to display it
	 * 
	 * @precondition: The columns and rows of a maze are uniform i.e. mazes must be n x m with not variation in between
	 * 
	 */
	public void showGrid(int [][] map, GridPane grid, Stage primaryStage) {
		int rows = map.length;
		int cols = map[0].length;
		for(int i = 0; i < rows ; i++){
			for(int j = 0; j < cols ; j++){
				Rectangle newRect = new Rectangle(40,40);
				newRect.setArcHeight(19);
				newRect.setArcWidth(19);
				int blockType = map[i][j];
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
	            primaryStage.show();
			}
		}
	}	
}	