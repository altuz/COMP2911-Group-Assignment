package GameLogic;

import java.util.ArrayList;

/**
 * Created by altuz on 20/05/17.
 */
public class State {
    private int[][] matrix;
    private int[] player_location;
    private ArrayList<int[]> box_locations;
    private ArrayList<int[]> end_locations;

    public State(){
        matrix = null;
        player_location = null;
        box_locations = new ArrayList<int[]>();
        end_locations = new ArrayList<int[]>();
    }

    public void setMatrix(int[][] m){ this.matrix = m.clone(); }
    public void setPlayerLoc(int[] pl){ this.player_location = pl.clone(); }
    public void addBox(int[] bl){ box_locations.add(bl); }
    public void addEnd(int[] el){ end_locations.add(el); }

    public int[][] getMatrix(){ return this.matrix; }
    public int[] getPlayerLoc(){ return this.player_location; }
    public ArrayList getBoxLoc(){ return this.box_locations; }
    public ArrayList getEndLoc(){ return this.end_locations; }
}
