package FrontEnd;

import javafx.application.Application;
import javafx.geometry.Insets;
import BackEnd.GameState;
import Definitions.Movement;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage; 


public class graphicsProcessor extends Application{
	private static final int W = 600;
	private static final int H = 600;
	public static final int TILE_SIZE = 50;
	
	private static final int X_TILES = (int) (W / TILE_SIZE);
	private static final int Y_TILES = (int) (W / TILE_SIZE);
	private Tile[][] grid = new Tile[X_TILES][Y_TILES];
	
	public static void main(String ...args) {
		launch(args);
	}

	public static boolean isTutorial = false;
	public static int tutLevel = 1;
	/**
	 * Function below generates a window and grid based on 
	 * the maze generated in the backend
	 * Currently reads a file that has hardcoded the maze
	 * 
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Game");
		SoundEffects sound = new SoundEffects();
		MainMenu mainmenu = new MainMenu(W, H, sound);
		Scene scene = new Scene(mainmenu, W, H);
		Object o = "src/Resources/hack";
		//generate game state to display
		GameState state = new GameState(o);
		GameState statecp = new GameState(state);
		primaryStage.setResizable(false);
		//show grid initially
        primaryStage.setScene(scene);
        
        if(mainmenu.getStart().isVisible() == true) {
        	mainmenu.getGrid().setVisible(true);
        	createMap(state.getMaze(),mainmenu.getGrid(),primaryStage);
        }

        //control mechanism, takes keybaord events in the form of up,down,left,right keys only
        mainmenu.getStart().setOnKeyPressed(event -> {
        	if(mainmenu.getStart().getOpacity() == 1) {
        		if(event.getCode() != KeyCode.ESCAPE) {
        			//call backend functions to change the maze array depending on key pressed
        			if(event.getCode() == KeyCode.RIGHT) {
        				state.player_move(Movement.RIGHT);
        			} else if(event.getCode() == KeyCode.LEFT) {
        				state.player_move(Movement.LEFT);
        			} else if(event.getCode() == KeyCode.UP) {
        				state.player_move(Movement.UP);
        			} else if (event.getCode() == KeyCode.DOWN) {
        				state.player_move(Movement.DOWN);
        			} else if(event.getCode() == KeyCode.E){
        				state.undo_move();
        			} else if(event.getCode() == KeyCode.R){
        				state.stateCp(statecp);
        			}
        			createMap(state.getMaze(),mainmenu.getGrid(),primaryStage);
        			sound.soundEffects(state, sound);
        		} else {
    				if(mainmenu.getGameOptions().isVisible() == false && 
    						mainmenu.getLevelComplete().isVisible() == false) {
    					mainmenu.getGameOptions().setVisible(true);
    					mainmenu.getStart().setOpacity(0.5);
    					sound.getMouseClicked().stop();
    					sound.getMouseClicked().play();
    				}
        		}
        		if(state.game_over() == true) {
        			sound.getLevelComplete().stop();
        	    	sound.getLevelComplete().play();
        			if(isTutorial){
        				tutLevel++;
        				if(tutLevel > 4) {
        					tutLevel = 1;
        					isTutorial = false;
        				}
        			}
        			mainmenu.getLevelComplete().setVisible(true);
        			mainmenu.getStart().setOpacity(0.5);
        		}   
        	}
        });
        
        mainmenu.getMain().getChildren().get(0).setOnMouseClicked(event -> {
        	GameState next_level = new GameState(10, 6);
        	state.stateCp(next_level);
        	statecp.stateCp(next_level);
        	mainmenu.startFunc();
        	createMap(state.getMaze(),mainmenu.getGrid(),primaryStage);
        });
        
        mainmenu.getGameOptions().getChildren().get(1).setOnMouseClicked(event -> {
        	state.undo_move();
        	createMap(state.getMaze(), mainmenu.getGrid(), primaryStage);
        });
        
        mainmenu.getGameOptions().getChildren().get(2).setOnMouseClicked(event -> {
        	state.stateCp(statecp);
        	mainmenu.getStart().setOpacity(1);
        	createMap(state.getMaze(), mainmenu.getGrid(), primaryStage);
        	mainmenu.getGameOptions().setVisible(false);
        });
        
        // level complete
        mainmenu.getLevelComplete().getChildren().get(0).setOnMouseClicked(event ->{
        	if(isTutorial == false) {
        		GameState next_level = new GameState(10, 6);
            	state.stateCp(next_level);
            	statecp.stateCp(next_level);
            	createMap(state.getMaze(), mainmenu.getGrid(), primaryStage);
            	mainmenu.getLevelComplete().setVisible(false);
    			mainmenu.getStart().setOpacity(1);
        	}
        	else {
        		GameState next_level = new GameState("src/TutorialMaps/map"+tutLevel+".txt");
        		state.stateCp(next_level);
            	statecp.stateCp(next_level);
            	createMap(state.getMaze(),mainmenu.getGrid(),primaryStage);
            	mainmenu.getLevelComplete().setVisible(false);
    			mainmenu.getStart().setOpacity(1);
        	}
        });
        
		mainmenu.getMain().getChildren().get(2).setOnMouseClicked(event -> {
			isTutorial = true;
			GameState next_level = new GameState("src/TutorialMaps/map1.txt");
        	state.stateCp(next_level);
        	statecp.stateCp(next_level);
        	mainmenu.startFunc();
        	createMap(state.getMaze(),mainmenu.getGrid(),primaryStage);
		});
		
		mainmenu.getGameOptions().getChildren().get(4).setOnMouseClicked(event -> {
			mainmenu.homeFunc();
			isTutorial = false;
		});
		
		mainmenu.getLevelComplete().getChildren().get(1).setOnMouseClicked(event -> {
			mainmenu.homeFunc();
			isTutorial = false;
		});
		
	}

	/**
	 * function that takes in maze to display it
	 * @param map[][] integer map
	 * @param root the pane containing the map/grid
	 * @param primarystage the current stage 
	 * @precondition: The columns and rows of a maze are uniform i.e. mazes must be n x m with not variation in between
	 * 
	 */
	public void createMap(int [][] map,Pane root,  Stage primaryStage) {
		int rows = map.length;
		int cols = map[0].length;
		root.getChildren().clear();
		root.setPrefSize(W,H);
		root.setTranslateX(52);
		root.setTranslateY(52);
		root.setBackground(new Background(new BackgroundFill(Color.rgb(255, 242, 204), CornerRadii.EMPTY, Insets.EMPTY)));
		for(int y = 0; y < rows; y++){
			for(int x = 0; x < cols; x++){
				Tile tile = new Tile(x,y, map[y][x]);
				grid[x][y] = tile;
				root.getChildren().add(tile);
			}
		}		
		primaryStage.show();
	}
	
	public boolean completedlevel(int [][] map, SoundEffects sound) {
		int rows = map.length;
		int cols = map[0].length;
		for(int y = 0; y < rows; y++){
			for(int x = 0; x < cols; x++){
				if(map[x][y] == 3 || map[x][y] == 4) {
					return false;
				}
			}
		}
		return true;
	}
	
}	