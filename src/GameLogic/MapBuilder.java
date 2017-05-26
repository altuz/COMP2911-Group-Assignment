package GameLogic;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MapBuilder {
	private static final double W = 600;
	private static final double H = 600;
	public static final int TILE_SIZE = 50;
	private static final int X_TILES = (int) (W / TILE_SIZE);
	private static final int Y_TILES = (int) (W / TILE_SIZE);
	private Pane root;
	private Tile[][] grid = new Tile[X_TILES][Y_TILES];
	
	public MapBuilder(){
		Pane root = new Pane();
		root.setPrefSize(W, H);
		root.setBackground(new Background(new BackgroundFill(Color.rgb(255, 242, 204), CornerRadii.EMPTY, Insets.EMPTY)));
		this.root = root;
	}
	public static int[][] getSampleMap() {
		int sampleMap[][] = {
				{-1, -1, -1, -1, -1, -1},
				{-1, -1,  0,  0, -1, -1},
				{-1,  1,  2,  0, -1, -1},
				{-1, -1,  2,  0, -1, -1},
				{-1, -1,  0,  2,  0, -1},
				{-1,  3,  2,  0,  0, -1},
				{-1,  3,  3,  5,  3, -1},
				{-1, -1, -1, -1, -1, -1}
		};
		return sampleMap;
	}
	public static void setSampleMap(int sampleMap[][]) {
		MapBuilder.sampleMap = sampleMap;
	}
	private static int sampleMap[][] = {
			{-1, -1, -1, -1, -1, -1},
			{-1, -1,  0,  0, -1, -1},
			{-1,  1,  2,  0, -1, -1},
			{-1, -1,  2,  0, -1, -1},
			{-1, -1,  0,  2,  0, -1},
			{-1,  3,  2,  0,  0, -1},
			{-1,  3,  3,  5,  3, -1},
			{-1, -1, -1, -1, -1, -1}
	};
	
	private static int sampleMap1[][] = {
			{-2,-2,-2,-2,-2,-2},
			{-2,-2, 1, 0,-2,-2},
			{-2,-2,-2, 2,-2,-2},
			{-2,-2,-2, 0,-2,-2},
			{-2,-2,-2, 0,-2,-2},
			{-2,-2,-2, 0,-2,-2},
			{-2,-2,-2, 3,-2,-2},
			{-2,-2,-2,-2,-2,-2},
	};
	
	private static int sampleMap2[][] = {
			{-2,-2,-2,-2,-2,-2},
			{-2, 1, 0, 0, 0,-2},
			{-2,-2,-2,-2, 0,-2},
			{-2, 0, 0, 0, 0,-2},
			{-2, 0,-2,-2,-2,-2},
			{-2, 0,-2,-2,-2,-2},
			{-2, 0, 2, 0, 3,-2},
			{-2,-2,-2,-2,-2,-2}
	};
	
	private static int sampleMap3[][] = {
			{-2,-2,-2,-2,-2,-2},
			{-2, 1, 0, 0, 0,-2},
			{-2,-2,-2, 0, 0,-2},
			{-2,-2,-2, 2, 0,-2},
			{-2, 3, 0, 2, 0,-2},
			{-2,-2,-2, 0, 0,-2},
			{-2,-2,-2, 0, 0,-2},
			{-2, 3, 0, 0, 0,-2},
	};
	
	private static int sampleMap4[][] = {
			{-2,-2,-2,-2, 0,-2},
			{-2,-2,-2, 0, 0,-2},
			{-2,-2, 0, 0, 1,-2},
			{-2, 0, 0,-2, 0,-2},
			{-2, 0,-2,-2, 2,-2},
			{-2, 0, 0, 0, 2, 3},
			{-2,-2,-2,-2, 0,-2},
			{-2,-2,-2,-2, 3,-2},
	};
}
