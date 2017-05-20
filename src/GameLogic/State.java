package GameLogic;

import Definitions.Blocks;
import Definitions.Movement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

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

    /**
     * Shuffles Level..
     * a.k.a move boxes till max_move
     * After moves done, new position of boxes becomes the end zones and original box location is restored
     * TODO: Make it work better....
     * @param max_move
     */
    public void shuffleLevel(int max_move){
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int[][] mx_cp = new int[this.matrix.length][this.matrix.length];
        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                mx_cp[i][j] = this.matrix[i][j];
            }
        }
        ArrayList<int[]> bl_cp = new ArrayList<>(this.box_locations);
        ArrayList<int[]> el_cp = new ArrayList<>(this.end_locations);
        Queue<int[]> set = new LinkedList<>(bl_cp);
        // for each blocks
        while(!set.isEmpty()){
            int[] bl  = set.poll();
            int[] bl2 = bl.clone();
            int i = 0;
            while(i < max_move){
                int[] bl3 = bl.clone();
                int[][] reachability = exploreReachability();
                for(int l = 0; l < reachability.length; l++){
                    for(int m = 0; m < reachability.length; m++){
                        System.out.printf("%3d\t", reachability[l][m]);
                    }
                    System.out.println();
                }
                System.out.println();

                boolean[] adjacent = new boolean[]{true, true, true, true};
                int[][]   adj_tile = new int[4][2];
                int y = bl[0], x = bl[1];
                for(int j = 0; j < 2; j++){
                    int k = (j == 0) ? 1 : -1;
                    // idx0 = right, idx1 = bottom, idx2 = left, idx3 = top
                    adj_tile[j*2] = new int[]{ y, x + k};
                    adj_tile[j*2 + 1] = new int[]{ y + k, x};
                    if(x + k < 0 || x + k > matrix.length - 1)
                        adjacent[j*2] = false;
                    else if(x - k < 0 || x - k > matrix.length - 1)
                        adjacent[j*2] = false;
                    else if(reachability[y][x + k] != 1 || reachability[y][x - k] == -1)
                        adjacent[j*2] = false;
                    if(y + k < 0 || y + k > matrix.length - 1)
                        adjacent[j*2 + 1] = false;
                    else if(y - k < 0 || y - k > matrix.length - 1)
                        adjacent[j*2 + 1] = false;
                    else if(reachability[y + k][x] != 1 || reachability[y - k][x] == -1)
                        adjacent[j*2 + 1] = false;
                }
                // roll till you get valid adjacent box
                boolean unreachable = false;
                for(int j = 0; j < 4; j++)
                    unreachable = unreachable | adjacent[j];
                if(unreachable == false) {
                    if (i == 0) {
//                        set.add(bl);
                    }
                    break;
                }
                int reachable = -1;
                while(true){
                    int k = rng.nextInt(0, 4);
                    if(adjacent[k] == true) {
                        reachable = k;
                        break;
                    }
                }
                // set player location to that adjacent tile
                this.player_location = adj_tile[reachable];
                Movement dir = null;
                switch(reachable){
                    case 0: dir = Movement.LEFT; break;// MOVE LEFT
                    case 1: dir = Movement.UP; break;// MOVE UP
                    case 2: dir = Movement.RIGHT; break;// MOVE RIGHT
                    case 3: dir = Movement.DOWN; break;// MOVE BOTTOM
                }
                moveBox(dir, bl);
                i++;
                this.matrix[bl3[0]][bl3[1]] = 0;
                this.matrix[bl[0]][bl[1]] = 2;
                this.player_location = bl3;

                System.out.println(i);
            }
        }
        overlayMap(mx_cp);
        printGameMap();
    }
    // Overlay old map with the current map
    // New box locations become end zones
    public void overlayMap(int[][] mx){
        for(int[] bl : this.box_locations)
            mx[bl[0]][bl[1]] += 3;
        this.matrix = mx.clone();
    }
    /**
     * From the current map state, find the list of blocks reachable by player.
     * Returns a reachability matrix.
     *  -1 = Walls/Box
     *  0 = Unexplored
     *  1 = Reachable
     * @author Nicholas Mulianto
     * @return
     */
    public int[][] exploreReachability(){
        int[][] reachability = new int[this.matrix.length][this.matrix.length];
        // 0 have not been visited
        // 1 visited and reachable
        // -1 can't be visited
        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                switch(Blocks.get(this.matrix[i][j])){
                    case BOXES:
                    case IMMOVABLES:
                    case END_BOXES: reachability[i][j] = -1;
                }
            }
        }
        // DFS
        Stack<int[]> stack = new Stack<int[]>();
        stack.push(this.player_location);
        reachability[this.player_location[0]][this.player_location[1]] = 1;
        while(!stack.empty()){
            int[] cur = stack.pop();
            int y = cur[0], x = cur[1];
            // visited
            // check adjacent tiles
            for(int i = 0; i < 2; i++){
                int j = (i == 0) ? 1 : -1;
                if(x + j >= 0 && x + j <= matrix.length - 1) {
                    if(reachability[y][x+j] == 0) stack.add(new int[]{y, x+j});
                }
                if(y + j >= 0 && y + j <= matrix.length - 1) {
                    if(reachability[y+j][x] == 0) stack.add(new int[]{y+j, x});
                }
            }
            reachability[y][x] = 1;
        }
        return reachability;
    }

    private void moveBox(Movement dir, int[] box_loc){
        int y, x;
        y = box_loc[0];
        x = box_loc[1];
        switch(dir){
            case UP: y -= 1; break;
            case DOWN: y += 1; break;
            case LEFT: x -= 1; break;
            case RIGHT: x += 1; break;
        }
        box_loc[0] = y;
        box_loc[1] = x;
    }

    public void printGameMap() {
        // nested loop to print out the 2D array
        for (int i = 0 ; i < this.matrix.length ; i++) {
            for (int j = 0 ; j < this.matrix[i].length ; j++) {
                System.out.format("%5d", this.matrix[i][j]);
            }
            System.out.println("");
        }
    }
}
