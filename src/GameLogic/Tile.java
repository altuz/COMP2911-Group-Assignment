package GameLogic;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane{
	
		public static final int TILE_SIZE = 50;
		private static final double W = 800;
		private static final double H = 600;
		private static final int ARC_FACTOR = 2;
		private static final int DARKNESS_FACTOR= 20; //DARKNESS_FACTOR
		
		public int x,y;
		
		private Rectangle border = new Rectangle(TILE_SIZE -4,TILE_SIZE -8);
		//private Text text = new Text();
		private int blockType;	
		
		public Tile(int x, int y, int blockType){
			this.x = x;
			this.y = y;
			this.blockType = blockType;
			Color blockColour = this.generateColour(blockType, 0);
			//this.border.setStroke(Color.rgb(255, 242, 204));
			//this.text.setText(hasBomb ? "X" : "");
			//text.setFont(Font.font(18));
			//text.setVisible(false);
			this.border.setFill(blockColour);
			
			this.border.setArcHeight((double)(TILE_SIZE/ARC_FACTOR));
			this.border.setArcWidth((double) (TILE_SIZE/ARC_FACTOR));
			
			Rectangle shadow = new Rectangle(TILE_SIZE -4,TILE_SIZE -8);
			shadow.setArcHeight((double)(TILE_SIZE/ARC_FACTOR));
			shadow.setArcWidth((double) (TILE_SIZE/ARC_FACTOR));
			shadow.setFill(generateColour(this.blockType, DARKNESS_FACTOR));
			//shadow.setTranslateX(x*TILE_SIZE);
			shadow.setTranslateY(4);
			
			getChildren().addAll(shadow, border);
			setTranslateX(x*TILE_SIZE);
			setTranslateY(y*TILE_SIZE);
			
		}
		
		public Color generateColour(int blockType, int dF){
			Color newColor = Color.BLACK;
			switch(blockType){
				case -1: newColor = Color.rgb(59-dF, 56-dF, 56-dF); break;		//immovable
				case 0 : newColor = Color.rgb(219-dF,219-dF,219-dF); break;		//spaces
				case 1 : newColor = Color.rgb(197-dF, 90-dF, 17); break;		//player
				case 2 : newColor = Color.rgb(169-dF, 209-dF, 142-dF); break;	//boxes
				case 3 : newColor = Color.rgb(157-dF, 195-dF, 230-dF); break;	//End points
				case 4 : newColor = Color.rgb(142-dF, 65-dF, 12); break;		//player on endpoint
				case 5 : newColor = Color.rgb(117-dF, 180-dF, 74-dF); break;		//box on endpoint
			}
			
			return newColor;
		}
}