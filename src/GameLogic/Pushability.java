package GameLogic;

import Definitions.Movement;

import java.util.ArrayList;

/**
 * Created by altuz on 21/05/17.
 * Defines how a box can be pushed.
 * Which directions it can be pushed, etc.
 * @author Nicholas Mulianto
 */
public class Pushability {
    private int[] box_pos;
    private int pushable_dirs;
    private int id;
    private ArrayList<Movement> dirs;

    /**
     * a[] is the index of the box
     * b is the ID of the box in the arraylist in State object
     * @param a
     * @param b
     */
    public Pushability(int[] a, int b){
        this.dirs = null;
        this.box_pos = new int[2];
        this.id = b;
        this.box_pos[0] = a[0];
        this.box_pos[1] = a[1];
        pushable_dirs = Integer.MAX_VALUE;
    }

    public void setPushableDirs(int i){
        this.pushable_dirs = (i == 0) ? i : i;
    }

    public void setPos(int[] a){
        this.box_pos[0] = a[0];
        this.box_pos[1] = a[1];
    }

    public int getId(){ return this.id; }

    public int getPushableDirs(){
        return this.pushable_dirs;
    }

    public int[] getPos(){
        return this.box_pos;
    }

    /**
     * From the game matrix, finds how many ways we can push a box.
     * @author Nicholas Mulianto
     * @param matrix
     * @param reachability
     * @return
     */
    public int findPushableDirections(int[][] matrix, int[][] reachability){
        int y = this.box_pos[0], x = this.box_pos[1];
        boolean[] adjacent = new boolean[]{true, true, true, true};
        int[][]   adj_tile = new int[4][2];

        for(int j = 0; j < 2; j++){
            int k = (j == 0) ? 1 : -1;
            // idx0 = right, idx1 = bottom, idx2 = left, idx3 = top
            adj_tile[j*2] = new int[]{ y, x + k};
            adj_tile[j*2 + 1] = new int[]{ y + k, x};
            // bounds check
            // check what is on the other side
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
        int ret = 0;
        this.dirs = new ArrayList<>();
        for(int i = 0; i < adjacent.length; i++){
            if(adjacent[i]) {
                Movement dir = null;
                switch(i){
                    case 0: dir = Movement.LEFT; break;// MOVE LEFT
                    case 1: dir = Movement.UP; break;// MOVE UP
                    case 2: dir = Movement.RIGHT; break;// MOVE RIGHT
                    case 3: dir = Movement.DOWN; break;// MOVE BOTTOM
                }
                this.dirs.add(dir);
                ret++;
            }
        }
        return ret;
    }

    public ArrayList<Movement> getDirs(){
        return this.dirs;
    }
}
