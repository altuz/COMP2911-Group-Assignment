package GameLogic;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

	
public class MainMenu extends Parent {
	private VBox main;
	private VBox optionmenu;
	private VBox gameoptions;
	private VBox option;
	private VBox start;
	private GridPane grid;
	
	public GridPane getGrid() {
		return this.grid;
	}
	
	public VBox getMain() {
		return this.main;
	}
	
	public VBox getStart() {
		return this.start;
	}
	
	public VBox getGameOptions() {
		return this.gameoptions;
	}
	
	public VBox getOption() {
		return this.option;
	}
	
	public VBox getOptionMenu() {
		return this.optionmenu;
	}
	
	public MainMenu(int W, int H) {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(3);
		grid.setHgap(3);
		grid.setVisible(false);
		this.grid = grid;

		this.main = new VBox(4);
		main.setPrefSize(W, H);
		main.setAlignment(Pos.CENTER);
		
		this.optionmenu = new VBox(3);
		optionmenu.setPrefSize(W, H);
		optionmenu.setAlignment(Pos.CENTER);
		
		this.gameoptions = new VBox(5);
		gameoptions.setPrefSize(W, H);
		gameoptions.setAlignment(Pos.CENTER);
		
		this.option = new VBox(3);
		option.setPrefSize(W, H);
		option.setAlignment(Pos.CENTER);
		this.start = new VBox(1);

	
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
			
		//Implement after game works
		Button restart = new Button("RESTART");
		restart.setOnMouseClicked(event -> {
			
		});
		
		grid.setAlignment(Pos.CENTER);
		
		main.getChildren().addAll(newgame, options1, tutorial, exit);
		optionmenu.getChildren().addAll(sound, back1, exit1);
		gameoptions.getChildren().addAll(resume, restart, options, mainmenu, exit2);
		option.getChildren().addAll(sound1, back, exit3);
		start.getChildren().addAll(grid);

		start.setOnKeyPressed (event -> {
			if(event.getCode() == KeyCode.ESCAPE) {
				if(gameoptions.isVisible() == false && option.isVisible() == false) {
				gameoptions.setVisible(true);
				start.setOpacity(0.5);
				}
			}
		});
		
		getChildren().addAll(main);
	}
}


