package FrontEnd;
import BackEnd.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainMenu extends Parent {
	private VBox main;
	private VBox optionmenu;
	private VBox gameoptions;
	private VBox soundoption;
	private VBox start;
	private VBox levelcomplete;
	private VBox tutorial;
	private Pane grid;
	private Pane tutorialmap;
	private GameState game;
	
	public GameState getGame(){
		return this.game;
	}
/**
 * VBox of screen for level complete
 * @return levelcomplete
 */
	public VBox getLevelComplete() {
		return this.levelcomplete;
	}

/**
 * Pane of screen for tutorial
 * @return tutorial
 */
	public VBox getTutorial() {
		return this.tutorial;
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
 * GridPane of map GUI
 * @return grid 
 */
	public Pane getTutorialMap() {
		return this.tutorialmap;
	}
/**
 * This constructor sets up the	Buttons and various VBox which acts as screens for main menus 
 * @param W width
 * @param H height
 */
	public MainMenu(int W, int H, SoundEffects s) {
		this.game = null;
		Pane tutorialmap = new Pane();
		tutorialmap.setPrefSize(W, H);
		tutorialmap.setBackground(new Background(new BackgroundFill(Color.rgb(255, 242, 204), CornerRadii.EMPTY, Insets.EMPTY)));
		this.tutorialmap = tutorialmap;
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
		
		this.gameoptions = new VBox(6);
		gameoptions.setPrefSize(W, H);
		gameoptions.setAlignment(Pos.CENTER);
		
		this.soundoption = new VBox(3);
		soundoption.setPrefSize(W, H);
		soundoption.setAlignment(Pos.CENTER);
		
		this.levelcomplete = new VBox(1);
		levelcomplete.setPrefSize(W, H);
		levelcomplete.setAlignment(Pos.CENTER);
		
		this.tutorial = new VBox(1);
		tutorial.setBackground(new Background(new BackgroundFill(Color.rgb(255, 242, 204), CornerRadii.EMPTY, Insets.EMPTY)));
		tutorial.setVisible(false);
		//TODO
		Button newgame = new Button("NEW GAME", s);
		newgame.setOnMouseClicked(event -> {
			getChildren().add(start);
			getChildren().add(gameoptions);
			getChildren().add(levelcomplete);
			
			levelcomplete.setVisible(false);
			levelcomplete.requestFocus();
			gameoptions.setVisible(false);

			start.setOpacity(1);
			start.requestFocus();
			main.setVisible(false);
		});
		
		newgame.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button tutorialbtn = new Button("TUTORIAL", s);
		Button resume = new Button("RESUME", s);
		resume.setOnMouseClicked(event -> {
			gameoptions.setVisible(false);
			start.setOpacity(1);
		});
		
		tutorialbtn.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
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
			gameoptions.setVisible(false);
			getChildren().add(soundoption);
		});
		
		sound.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		
		Button mainmenu = new Button("HOME", s);


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
		
		Button back2 = new Button("BACK", s);
		back2.setOnMouseClicked(event -> {
			gameoptions.setVisible(true);
			getChildren().remove(soundoption);
		});

		back2.setOnMousePressed(event -> {
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
		Button cont = new Button("CONTINUE", s);
		Button undo = new Button("UNDO", s);
		Button soundoff = new Button("SOUND OFF", s);
		soundoff.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		Button soundon = new Button("SOUND ON", s);
		soundon.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		soundoff.setOnMousePressed(event -> {
			optionmenu.getChildren().remove(back1);
			optionmenu.getChildren().remove(soundoff);
			optionmenu.getChildren().addAll(soundon, back1);
			s.unmute(s.getThud(), s.getMove(), s.getLevelComplete(), s.getMouseHover(), s.getMouseClicked());
		});
		soundon.setOnMousePressed(event -> {
			optionmenu.getChildren().remove(back1);
			optionmenu.getChildren().remove(soundon);
			optionmenu.getChildren().addAll(soundoff, back1);
			s.muteSound(s.getThud(), s.getMove(), s.getLevelComplete(), s.getMouseHover(), s.getMouseClicked());
		});
	
		Button musicon = new Button("MUSIC ON", s);
		musicon.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		Button musicoff = new Button("MUSIC OFF", s);
		musicoff.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		musicon.setOnMousePressed(event -> {
			int k = 0;
			for(int i = 0; i < optionmenu.getChildren().size(); i++) {
				if(optionmenu.getChildren().get(i).equals(soundon)) {
					optionmenu.getChildren().remove(back1);
					optionmenu.getChildren().remove(soundon);
					optionmenu.getChildren().remove(musicon);
					optionmenu.getChildren().addAll(musicoff, soundon, back1);
					s.mutemusic(s.getMusic());
					k = 1;
				}
			}
			
			if(k == 0) {
				optionmenu.getChildren().remove(back1);
				optionmenu.getChildren().remove(soundoff);
				optionmenu.getChildren().remove(musicon);
				optionmenu.getChildren().addAll(musicoff, soundoff, back1);
				s.mutemusic(s.getMusic());
			};
			
		});
		
		musicoff.setOnMousePressed(event -> {
			int k = 0;
			for(int i = 0; i < optionmenu.getChildren().size(); i++) {
				if(optionmenu.getChildren().get(i).equals(soundon)) {
					optionmenu.getChildren().remove(back1);
					optionmenu.getChildren().remove(soundon);
					optionmenu.getChildren().remove(musicoff);
					optionmenu.getChildren().addAll(musicon, soundon, back1);
					s.unmutemusic(s.getMusic());
					k = 1;
				}
			}
			if(k == 0) {
				optionmenu.getChildren().remove(back1);
				optionmenu.getChildren().remove(soundoff);
				optionmenu.getChildren().remove(musicoff);
				optionmenu.getChildren().addAll(musicon, soundoff, back1);
				s.unmutemusic(s.getMusic());
			}
			
		});
		
		Button soundoff1 = new Button("SOUND OFF", s);
		soundoff1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		Button soundon1 = new Button("SOUND ON", s);
		soundon1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		soundoff1.setOnMousePressed(event -> {
			soundoption.getChildren().remove(back2);
			soundoption.getChildren().remove(soundoff1);
			soundoption.getChildren().addAll(soundon1, back2);
			s.unmute(s.getThud(), s.getMove(), s.getLevelComplete(), s.getMouseHover(), s.getMouseClicked());
		});
		soundon1.setOnMousePressed(event -> {
			soundoption.getChildren().remove(back2);
			soundoption.getChildren().remove(soundon1);
			soundoption.getChildren().addAll(soundoff1, back2);
			s.muteSound(s.getThud(), s.getMove(), s.getLevelComplete(), s.getMouseHover(), s.getMouseClicked());
		});
	
		Button musicon1 = new Button("MUSIC ON", s);
		musicon1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		Button musicoff1 = new Button("MUSIC OFF", s);
		musicoff1.setOnMousePressed(event -> {
			s.getMouseClicked().stop();
			s.getMouseClicked().play();
		});
		musicon1.setOnMousePressed(event -> {
			int k = 0;
			for(int i = 0; i < soundoption.getChildren().size(); i++) {
				if(soundoption.getChildren().get(i).equals(soundon1)) {
					soundoption.getChildren().remove(back2);
					soundoption.getChildren().remove(soundon1);
					soundoption.getChildren().remove(musicon1);
					soundoption.getChildren().addAll(musicoff1, soundon1, back2);
					s.mutemusic(s.getMusic());
					k = 1;
				}
			}
			
			if(k == 0) {
				soundoption.getChildren().remove(back2);
				soundoption.getChildren().remove(soundoff1);
				soundoption.getChildren().remove(musicon1);
				soundoption.getChildren().addAll(musicoff1, soundoff1, back2);
				s.mutemusic(s.getMusic());
			};
			
		});
		
		musicoff1.setOnMousePressed(event -> {
			int k = 0;
			for(int i = 0; i < soundoption.getChildren().size(); i++) {
				if(soundoption.getChildren().get(i).equals(soundon1)) {
					soundoption.getChildren().remove(back2);
					soundoption.getChildren().remove(soundon1);
					soundoption.getChildren().remove(musicoff1);
					soundoption.getChildren().addAll(musicon1, soundon1, back2);
					s.unmutemusic(s.getMusic());
					k = 1;
				}
			}
			if(k == 0) {
				soundoption.getChildren().remove(back2);
				soundoption.getChildren().remove(soundoff1);
				soundoption.getChildren().remove(musicoff1);
				soundoption.getChildren().addAll(musicon1, soundoff1, back2);
				s.unmutemusic(s.getMusic());
			}
			
		});
		
		main.getChildren().addAll(newgame, options1, tutorialbtn, exit);
		optionmenu.getChildren().addAll(musicon, soundon, back1);
		gameoptions.getChildren().addAll(resume, undo, restart, sound, mainmenu, exit2);
		soundoption.getChildren().addAll(musicon1, soundon1, back2);
		levelcomplete.getChildren().addAll(cont);
		start.getChildren().addAll(grid);
		tutorial.getChildren().addAll(tutorialmap);
		start.setOnKeyPressed (event -> {
			if(event.getCode() == KeyCode.ESCAPE) {
				if(gameoptions.isVisible() == false && soundoption.isVisible() == false && levelcomplete.isVisible() == false) {
					gameoptions.setVisible(true);
					start.setOpacity(0.5);
					s.getMouseClicked().stop();
					s.getMouseClicked().play();
				}
			}
		});
		
		getChildren().addAll(main);
	}

	public void startFunc(){
		getChildren().add(start);
		getChildren().add(gameoptions);
		getChildren().add(levelcomplete);
		
		levelcomplete.setVisible(false);
		levelcomplete.requestFocus();
		gameoptions.setVisible(false);

		start.setOpacity(1);
		start.requestFocus();
		main.setVisible(false);
	}
	
	public void startTut(){
		getChildren().add(tutorial);
		getChildren().add(gameoptions);
		getChildren().add(levelcomplete);
		
		levelcomplete.setVisible(false);
		levelcomplete.requestFocus();
		gameoptions.setVisible(false);

		tutorial.setOpacity(1);
		tutorial.requestFocus();
		main.setVisible(false);
	}
	
	public void homeFunc(){
		main.setVisible(true);
		getChildren().remove(gameoptions);
		getChildren().remove(start);
		getChildren().remove(levelcomplete);
	}
}
