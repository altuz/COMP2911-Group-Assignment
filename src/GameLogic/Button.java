package GameLogic;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Button extends StackPane {
	// Need to use text instead of string because getFont etc 
	// dont work on type String
	
	private Text text;
	public Button(String name) {
		//Set text font and color
		text = new Text(name);
		//text.setFont(text.getFont().font(15));
		text.setFont(Font.font("", FontWeight.BOLD, 12));
		text.setFill(Color.WHITE);
		
		//Rectangle buttons
		Rectangle bg = new Rectangle(115, 35);
		bg.setFill(Color.BLACK);
		
		//Center text
		setAlignment(Pos.CENTER);
		
		//Adds the background rectangle (or button) before text
		getChildren().addAll(bg, text);
		
		//Button animation when mouse hovers <--- need to change/edit this for keyboard
		setOnMouseEntered(event -> {
		bg.setTranslateX(8);
		bg.setFill(Color.LIGHTGREY);
		text.setTranslateX(10);
		text.setFill(Color.BLACK);
		});
		
		setOnMouseExited(event -> {
		bg.setTranslateX(0);
		bg.setFill(Color.BLACK);
		text.setTranslateX(0);
		text.setFill(Color.WHITE);
		});
	}
}
