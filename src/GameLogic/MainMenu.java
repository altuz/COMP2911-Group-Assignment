package GameLogic;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MainMenu extends Parent {
	private VBox main;
	private VBox optionmenu;
	private VBox gameoptions;
	private VBox soundoption;
	private VBox start;
	private VBox levelcomplete;
	private Pane grid;

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
	public Pane getGrid() {
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
 * Returns music and sound option 
 * @return option
 */
	public VBox getSoundOption() {
		return this.soundoption;
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
	public MainMenu(int W, int H, SoundEffects s) {
		Pane grid = new Pane();
		grid.setPrefSize(W, H);
		grid.setVisible(false);
		this.grid = grid;
		this.start = new VBox(1);
		start.setBackground(new Background(new BackgroundFill(Color.rgb(255, 242, 204), CornerRadii.EMPTY, Insets.EMPTY)));
		this.main = new VBox(4);
		main.setPrefSize(W, H);
		main.setAlignment(Pos.CENTER);
		
		this.optionmenu = new VBox(3);
		optionmenu.setPrefSize(W, H);
		optionmenu.setAlignment(Pos.CENTER);
		
		this.gameoptions = new VBox(5);
		gameoptions.setPrefSize(W, H);
		gameoptions.setAlignment(Pos.CENTER);
		
		this.soundoption = new VBox(3);
		soundoption.setPrefSize(W, H);
		soundoption.setAlignment(Pos.CENTER);
		
		this.levelcomplete = new VBox(1);
		levelcomplete.setPrefSize(W, H);
		levelcomplete.setAlignment(Pos.CENTER);
		
		//TODO
		Button newgame = new Button("NEW GAME", s);
		newgame.setOnMouseClicked(event -> {
			getChildren().add(start);
			getChildren().add(gameoptions);
			//getChildren().add(option);
			getChildren().add(levelcomplete);
			levelcomplete.setVisible(false);
			levelcomplete.requestFocus();
			gameoptions.setVisible(false);
			//option.setVisible(false);
			start.setOpacity(1);
			start.requestFocus();
			main.setVisible(false);
		});
		
		newgame.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button tutorial = new Button("TUTORIAL", s);
		tutorial.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button resume = new Button("RESUME", s);
		resume.setOnMouseClicked(event -> {
			gameoptions.setVisible(false);
			start.setOpacity(1);
		});
		resume.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		/*Button options = new Button("OPTIONS");
		options.setOnMouseClicked(event -> {
			option.setVisible(true);
			gameoptions.setVisible(false);
		});*/
		
		Button options1 = new Button("OPTIONS", s);
		options1.setOnMouseClicked(event -> {
			getChildren().add(optionmenu);
			main.setVisible(false);
		});
		
		options1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		//Does not work yet
		Button sound = new Button("SOUND", s);
		sound.setOnMouseClicked(event -> {
			optionmenu.setVisible(false);
			soundoption.setVisible(true);
		});
		sound.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		Button sound1 = new Button("SOUND", s);
		sound1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button mainmenu = new Button("MAIN MENU", s);
		mainmenu.setOnMouseClicked(event -> {
			main.setVisible(true);
			getChildren().remove(gameoptions);
			getChildren().remove(start);
		});
		mainmenu.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button back = new Button("BACK", s);
		back.setOnMouseClicked(event -> {
			gameoptions.setVisible(true);
		});
		back.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button back1 = new Button("BACK", s);
		back1.setOnMouseClicked(event -> {
			main.setVisible(true);
			getChildren().remove(optionmenu);
		});
		back1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button exit = new Button("EXIT", s);
		exit.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		exit.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button exit1 = new Button("EXIT", s);
		exit1.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		exit1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button exit2 = new Button("EXIT", s);
		exit2.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		exit2.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button exit3 = new Button("EXIT", s);
		exit3.setOnMouseClicked(event -> {
			System.exit(0);
		});
		
		exit3.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
			
		//TODO
		Button restart = new Button("RESTART", s);
		restart.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button cont = new Button("CONTINUE", s);
		cont.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		main.getChildren().addAll(newgame, options1, tutorial, exit);
		optionmenu.getChildren().addAll(sound, back1, exit1);
		gameoptions.getChildren().addAll(resume, restart, sound1, mainmenu, exit2);
		start.getChildren().addAll(grid);
		levelcomplete.getChildren().addAll(cont);
		start.setOnKeyPressed (event -> {
			if(event.getCode() == KeyCode.ESCAPE) {
				if(gameoptions.isVisible() == false && /*option.isVisible() == false &&*/ levelcomplete.isVisible() == false) {
					gameoptions.setVisible(true);
					start.setOpacity(0.5);
					s.getMouseClicked().stop();
					s.getMouseClicked().play();
				}
			}
		});
		
		getChildren().addAll(main);
	}

}


