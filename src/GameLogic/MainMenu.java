package GameLogic;
import java.awt.Color;
import javax.swing.BorderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class MainMenu extends Parent {
	private VBox main;
	private VBox optionmenu;
	private VBox gameoptions;
	private VBox option;
	private VBox start;
	private VBox levelcomplete;
	private GridPane grid;

/**
 * VBox of screen for level complete
 * @return levelcomplete
 */
	public VBox getLevelComplete() {
		return this.levelcomplete;
	}
/**
 * GridPane of map GUI
 * @return grid 
 */
	public GridPane getGrid() {
		return this.grid;
	}
/**
 * Returns main menu - starting screen
 * @return main
 */
	public VBox getMain() {
		return this.main;
	}
/**
 * Returns VBox containing game GUI	
 * @return start
 */
	public VBox getStart() {
		return this.start;
	}
/**
 * Returns in game menu screen
 * @return gameoptions
 */
	public VBox getGameOptions() {
		return this.gameoptions;
	}
/**
 * Returns in game options screen for sound etc
 * @return option
 */
	public VBox getOption() {
		return this.option;
	}
/**
 * Returns main menu options screen 	
 * @return optionmenu
 */
	public VBox getOptionMenu() {
		return this.optionmenu;
	}
	
/**
 * This constructor sets up the	Buttons and various VBox which acts as screens for main menus 
 * @param W width
 * @param H height
 */
	public MainMenu(int W, int H) {
		GridPane grid = new GridPane();
		grid.setPrefSize(W, H);
		grid.setPadding(new Insets(10,10,10,10));
		grid.setVgap(3);
		grid.setHgap(3);
		grid.setVisible(false);
		
		this.grid = grid;
		this.start = new VBox(1);
		
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
		
		this.levelcomplete = new VBox(1);
		levelcomplete.setPrefSize(W, H);
		levelcomplete.setAlignment(Pos.CENTER);
	
		Button newgame = new Button("NEW GAME");
		newgame.setOnMouseClicked(event -> {
			getChildren().remove(main);
			getChildren().add(start);
			getChildren().add(gameoptions);
			getChildren().add(option);
			getChildren().add(levelcomplete);
			levelcomplete.setVisible(false);
			levelcomplete.requestFocus();
			gameoptions.setVisible(false);
			option.setVisible(false);
			start.setOpacity(1);
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
			
		//TODO
		Button restart = new Button("RESTART");
		restart.setOnMouseClicked(event -> {
			
		});
		
		Button cont = new Button("CONTINUE");
		grid.setAlignment(Pos.CENTER);
		
		main.getChildren().addAll(newgame, options1, tutorial, exit);
		optionmenu.getChildren().addAll(sound, back1, exit1);
		gameoptions.getChildren().addAll(resume, restart, options, mainmenu, exit2);
		option.getChildren().addAll(sound1, back, exit3);
		start.getChildren().addAll(grid);
		levelcomplete.getChildren().addAll(cont);
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


