package BackEnd;

import Definitions.Movement;

import java.util.ArrayList;

/**
 * Created by altuz on 21/05/17.
 */
public class Pushability {
    private int[] box_pos;
    private int pushable_dirs;
    private int id;
    private ArrayList<Movement> dirs;

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

    public int findPushableDirections(int[][] matrix, int[][] reachability){
        int y = this.box_pos[0], x = this.box_pos[1];
        boolean[] adjacent = new boolean[]{true, true, true, true};
        int[][]   adj_tile = new int[4][2];

        for(int j = 0; j < 2; j++){
            int k = (j == 0) ? 1 : -1;
            // idx0 = right, idx1 = bottom, idx2 = left, idx3 = top
            adj_tile[j*2] = new int[]{ y, x + k};
            adj_tile[j*2 + 1] = new int[]{ y + k, x};
            // check boundary
            if(x + k < 0 || x + k > matrix.length - 1)
                adjacent[j*2] = false;
            // check boundary of other side
            else if(x - k < 0 || x - k > matrix.length - 1)
                adjacent[j*2] = false;
            // check if side reachable
            else if(reachability[y][x + k] != 1)
                adjacent[j*2] = false;
            // check if other side movable
            else if(matrix[y][x - k] != 0)
                adjacent[j*2] = false;
            // check boundary
            if(y + k < 0 || y + k > matrix.length - 1)
                adjacent[j*2 + 1] = false;
            // check boundary of other side
            else if(y - k < 0 || y - k > matrix.length - 1)
                adjacent[j*2 + 1] = false;
            // check if side reachable
            else if(reachability[y + k][x] != 1)
                adjacent[j*2 + 1] = false;
            // check if other side movable
            else if(matrix[y - k][x] != 0)
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