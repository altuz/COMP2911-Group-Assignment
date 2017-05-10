	import javafx.application.Application;
	import javafx.geometry.Insets;
	import javafx.geometry.Pos;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.input.KeyCode;
	import javafx.scene.layout.GridPane;
	import javafx.scene.layout.Pane;
	import javafx.scene.layout.VBox;
	import javafx.scene.paint.Color;
	import javafx.scene.shape.Rectangle;
	import javafx.stage.Stage;

	public class MainMenu extends Application {
		private mainMenu mainMenu;
		
		@Override
		public void start(Stage primaryStage) throws Exception {
			Pane root = new Pane();
			root.setPrefSize(300, 300);
			mainMenu = new mainMenu();
			mainMenu.setVisible(true);
			root.getChildren().addAll(mainMenu);
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
	private class mainMenu extends Parent {
		public mainMenu() {
			VBox main = new VBox(4);
			main.setTranslateX(95);
			main.setTranslateY(75);
			
			VBox optionmenu = new VBox(3);
			optionmenu.setTranslateX(95);
			optionmenu.setTranslateY(80);
			
			VBox gameoptions = new VBox(4);
			gameoptions.setTranslateX(95);
			gameoptions.setTranslateY(80);
			
			VBox option = new VBox(4);
				option.setTranslateX(95);
			option.setTranslateY(80);

			VBox start = new VBox(1);
			
			startGame game = new startGame();
	
			Button newgame = new Button("NEW GAME");
			newgame.setOnMouseClicked(event -> {
				getChildren().add(start);
				getChildren().add(gameoptions);
				getChildren().add(option);
				gameoptions.setVisible(false);
				option.setVisible(false);
				start.setOpacity(1);
				getChildren().remove(main);
				start.requestFocus();
			});
			
			Button tutorial = new Button("TUTORIAL");
			
			Button resume = new Button("RESUME");
			resume.setOnMouseClicked(event -> {
				gameoptions.setVisible(false);
				start.setOpacity(1);
			});
			
			Button options = new Button("OPTIONS");
			options.setOnMouseClicked(event -> {
				option.setVisible(true);
				gameoptions.setVisible(false);
			});
			
			Button options1 = new Button("OPTIONS");
			options1.setOnMouseClicked(event -> {
				getChildren().add(optionmenu);
				getChildren().remove(main);
			});
			
			//Does not work yet
			Button sound = new Button("SOUND");
			Button sound1 = new Button("SOUND");
			
			Button mainmenu = new Button("MAIN MENU");
			mainmenu.setOnMouseClicked(event -> {
				getChildren().add(main);
				getChildren().remove(gameoptions);
				getChildren().remove(option);
				getChildren().remove(start);
			});
			
			Button back = new Button("BACK");
			back.setOnMouseClicked(event -> {
				gameoptions.setVisible(true);
				option.setVisible(false);
				getChildren().remove(options);
			});
			
			Button back1 = new Button("BACK");
			back1.setOnMouseClicked(event -> {
				getChildren().add(main);
				getChildren().remove(optionmenu);
			});
			
			Button exit = new Button("EXIT");
			exit.setOnMouseClicked(event -> {
				System.exit(0);
			});
			
			Button exit1 = new Button("EXIT");
			exit1.setOnMouseClicked(event -> {
				System.exit(0);
			});
			
			Button exit2 = new Button("EXIT");
			exit2.setOnMouseClicked(event -> {
				System.exit(0);
			});
			
			Button exit3 = new Button("EXIT");
			exit3.setOnMouseClicked(event -> {
				System.exit(0);
			});
			
			game.grid.setAlignment(Pos.CENTER);
			
			main.getChildren().addAll(newgame, options1, tutorial, exit);
			optionmenu.getChildren().addAll(sound, back1, exit1);
			gameoptions.getChildren().addAll(resume, options, mainmenu, exit2);
			option.getChildren().addAll(sound1, mainmenu, back, exit3);
			start.getChildren().addAll(game.grid);

			start.setOnKeyPressed (event -> {
				if(event.getCode() == KeyCode.ESCAPE) {
					if(gameoptions.isVisible() == false) {
					gameoptions.setVisible(true);
					start.setOpacity(0.7);
					} else {
					gameoptions.setVisible(false);
					start.setOpacity(1);
					}
				}
			});
			
			/*start.setOnMouseClicked(event -> {
				if(gameoptions.isVisible() == false) {
					gameoptions.setVisible(true);
					start.setOpacity(0.7);
				} else {
					gameoptions.setVisible(false);
					start.setOpacity(1);
				}
			});*/
			getChildren().addAll(main);
		}
	}
	private class startGame extends Parent {
		private GridPane grid;
		public startGame() {
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
			grid.setPrefSize(300, 300);
			this.grid = grid;
		}
	}	
}