package GameLogic;

import Definitions.Blocks;
import Definitions.Movement;
import jdk.nashorn.internal.ir.Block;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nicholas Mulianto on 20/05/17.
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
     * calls shuffleHelper
     * due to randomness, shuffleHelper might not produce a good solution.
     * if too many of the boxes stay at the same place, repeat.
     * @param max_move
     */
    public void shuffleLevel(int max_move, int max_stationary){
        // deep copy of relevant fields
        int[][] mxcp = matrixDeepCopy(this.matrix);
        ArrayList<int[]> blcp = blDeepCopy(this.box_locations);
        int[] plcp = new int[]{this.player_location[0], this.player_location[1]};
        // another copy for 'queue'
        for(int i = 0; i < max_move; i++){
            shuffleHelper(max_move);
            int j = 0;
            for(int[] bl : blcp){
                if(this.matrix[bl[0]][bl[1]] == Blocks.END_BOXES.getVal()) {
                    j++;
                }

            }
            if(j <= max_stationary) break;
            this.matrix = matrixDeepCopy(mxcp);
            this.box_locations = blDeepCopy(blcp);
            this.end_locations = new ArrayList<>();
            this.player_location = new int[]{plcp[0], plcp[1]};
        }
        this.player_location = new int[]{plcp[0], plcp[1]};
    }

    /**
     * Shuffles Level..
     * a.k.a move boxes till max_move
     * After moves done, new position of boxes becomes the end zones and original box location is restored
     * @author Nicholas Mulianto
     * @param max_move
     */
    private void shuffleHelper(int max_move){
        // initialize rng seed to current time
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        // deep copy of relevant fields
        int[][] mxcp = matrixDeepCopy(this.matrix);
        ArrayList<int[]> blcp = blDeepCopy(this.box_locations);
        // another copy for 'queue'
        ArrayList<Pushability> queue = pushabilityList();

        for(int i = 0; i < max_move; i++){
            if(queue.isEmpty()) break;
            // compute reachability
            int[][] reachability = exploreReachability();
            for(Pushability push : queue){
                int pushDirs = push.findPushableDirections(this.matrix, reachability);
                push.setPushableDirs(pushDirs);
            }
            // sort queue increasing order
            queue.sort(
                    new Comparator<Pushability>() {
                        @Override
                        public int compare(Pushability o1, Pushability o2) {
                            if(o1.getPushableDirs() == o2.getPushableDirs())
                                return ThreadLocalRandom.current().nextInt(-1, 2);
                            return o2.getPushableDirs() - o1.getPushableDirs();
                        }
                    }
            );
            // remove first item in queue
            Pushability first = queue.remove(0);
            if(first.getPushableDirs() == 0) continue;
            ArrayList<Movement> dirs = first.getDirs();
            int random_idx = (dirs.size() == 0) ? 0 : rng.nextInt(0, dirs.size());
            Movement dir = dirs.get(random_idx);
            int[] o_pos = first.getPos();
            int[] n_pos = moveBox(dir, first.getPos());
            this.matrix[o_pos[0]][o_pos[1]] = Blocks.PLAYER.getVal();
            this.matrix[n_pos[0]][n_pos[1]] = Blocks.BOXES.getVal();
            this.matrix[this.player_location[0]][this.player_location[1]] = Blocks.SPACES.getVal();
            this.player_location = o_pos;
            Pushability new_stuff = new Pushability(n_pos);
            queue.add(new_stuff);
        }
        overlayMap(mxcp, blcp);
    }
    /**
     * Overlay old map with current map
     * New box locations become end zones
     * @author Nicholas Mulianto
     * @param mx
     * @param bl_cp
     */
    public void overlayMap(int[][] mx, ArrayList<int[]> bl_cp){
        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                if(this.matrix[i][j] == Blocks.BOXES.getVal()) {
                    mx[i][j] += Blocks.END_POINTS.getVal();
                    end_locations.add(new int[]{i, j});
                }
            }
        }
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

    /**
     * move box in specified direction
     * @author Nicholas Mulianto
     * @param dir
     * @param box_loc
     * @return
     */
    private int[] moveBox(Movement dir, int[] box_loc){
        int y, x;
        y = box_loc[0];
        x = box_loc[1];
        switch(dir){
            case UP: y -= 1; break;
            case DOWN: y += 1; break;
            case LEFT: x -= 1; break;
            case RIGHT: x += 1; break;
        }
        return new int[]{y, x};
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

    /**
     * Creates deep copy of the matrix.
     * In case we fuck up during randomization, better to start fresh
     * @author Nicholas Mulianto
     * @return
     */
    private int[][] matrixDeepCopy(int[][] src){
        int[][] mxcp = new int[src.length][src.length];
        for(int i = 0; i < src.length; i++)
            for(int j = 0; j < src.length; j++)
                mxcp[i][j] = src[i][j];
        return mxcp;
    }
    /**
     * Creates deep copy of the boxes location list.
     * In case we fuck up during randomization, better to start fresh
     * @author Nicholas Mulianto
     * @return
     */
    private ArrayList<int[]> blDeepCopy(ArrayList<int[]> src){
        ArrayList<int[]> blcp = new ArrayList<>();
        for(int i = 0; i < src.size(); i++){
            int[] n = new int[2];
            int[] o = src.get(i);
            n[0] = o[0];
            n[1] = o[1];
            blcp.add(n);
        }
        return blcp;
    }
    /**
     * Creates a list of pushability object based on the current box location.
     * @author Nicholas Mulianto
     * @return
     */
    private ArrayList<Pushability> pushabilityList(){
        ArrayList<Pushability> plist = new ArrayList<>();
        for(int i = 0; i < this.box_locations.size(); i++){
            Pushability n = new Pushability(this.box_locations.get(i));
            plist.add(n);
        }
        return plist;
    }
}
