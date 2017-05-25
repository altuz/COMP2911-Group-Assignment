package GameLogic;
import Definitions.Blocks;
import Definitions.Movement;
import jdk.nashorn.internal.ir.Block;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nicholas Mulianto on 20/05/17.
 * Source Paper: http://www-users.cs.umn.edu/~bilal/papers/GIGA16_SOKOBAN.pdf
 */
public class State {
    private int[][] matrix;
    private int[] player_location;
    private ArrayList<int[]> box_locations;
    private ArrayList<int[]> end_locations;
    private double value;
    private int terrain_v;

    /**
     * Initializes state to NOTHING
     * FUCKING NOTHING
     * @author Nicholas Mulianto
     */
    public State(){
        this.terrain_v = 0;
        this.value = 0;
        this.matrix = null;
        this.player_location = null;
        this.box_locations = new ArrayList<int[]>();
        this.end_locations = new ArrayList<int[]>();
    }

    /*
     * Boring book keeping methods.
     * setMatrix - stores given matrix
     * setPlayerLoc - stores given player location
     * addBox - insert box index
     * addEnd - insert goal index
     */
    public void setMatrix(int[][] m){ this.matrix = matrixDeepCopy(m); }
    public void setPlayerLoc(int[] pl){ 
    	this.player_location = new int[]{pl[0], pl[1]};
    	this.matrix[pl[0]][pl[1]] = 1;
    }
    public void addBox(int[] bl){ box_locations.add(bl); }
    public void addEnd(int[] el){ end_locations.add(el); }
    public void setEndLocations(ArrayList<int[]> elocs){
        for(int[] i : elocs){
            this.end_locations.add(i);
        }
    }
    public void addAllBox(ArrayList<int[]> bls){
        for(int[] i : bls) {
            this.matrix[i[0]][i[1]] = Blocks.BOXES.getVal();
        }
        box_locations.addAll(bls);
    }


    /*
     * Boring getters
     */
    public int[][] getMatrix(){ return this.matrix; }
    public int[] getPlayerLoc(){ return this.player_location; }
    public ArrayList getBoxLoc(){ return this.box_locations; }
    public ArrayList getEndLoc(){ return this.end_locations; }
    public int getTerrain() { return this.terrain_v; }

    /**
     * calls shuffleHelper up to max_shuffle number of times.
     * Each shuffle can take up to max_move number of times
     *      - due to randomness, shuffleHelper might not produce a good solution
     *      - if too many of the boxes stay at the same place, repeat.
     * @author Nicholas Mulianto
     * @param max_move
     * @param max_shuffle
     */
    public void shuffleLevel(int max_move, int max_shuffle){
        // deep copy of relevant fields
        int[][] mxcp = matrixDeepCopy(this.matrix);
        ArrayList<int[]> blcp = blDeepCopy(this.box_locations);
        int[] plcp = new int[]{this.player_location[0], this.player_location[1]};
        EvalLevel bestLevel = new EvalLevel(null, 0, null);
        // another copy for 'queue'
        for(int i = 0; i < max_shuffle; i++){
            // shuffle matrix
            shuffleHelper(max_move);
            // evaluate metrics
            int cong = congestion_eval();
            int terr = terrain_eval();
            double levl = level_eval(terr, cong);
            // compare this level's metric with previous iterations
            // if metric is larger, we assume it is a more interesting level
            // not sure if actually true but paper says its a good assumption
            if(levl > bestLevel.getEval()) {
                int[][] mcp = matrixDeepCopy(this.matrix);
                bestLevel = new EvalLevel(mcp, levl, this.end_locations);
            }
            // reinstate original matrix, box location and player location
            // reinitialize end locations
            this.matrix = matrixDeepCopy(mxcp);
            this.box_locations = blDeepCopy(blcp);
            this.end_locations = new ArrayList<>();
            this.player_location = new int[]{plcp[0], plcp[1]};
        }
        // overwrite level with best found level
        this.player_location = new int[]{plcp[0], plcp[1]};
        this.matrix = bestLevel.getMx();
        this.value = bestLevel.getEval();
        this.end_locations = bestLevel.getEnds();
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
        // hashtable stores the end position of the boxes
        // the key refers to the box's corresponding index in the box_locations arraylist
        Hashtable<Integer, int[]> eloc = new Hashtable<>();
        // init hashtable
        for(int i = 0; i < blcp.size(); i++){
        	eloc.put(i, blcp.get(i));
        }
        // repeat until max moves or until no boxes can be moved
        for(int i = 0; i < max_move; i++){
            if(queue.isEmpty()) break;
            // compute reachability matrix
            int[][] reachability = exploreReachability();
            // for each box, compute their 'pushability'
            // aka how many ways box can be pushed
            for(Pushability push : queue){
                int pushDirs = push.findPushableDirections(this.matrix, reachability);
                push.setPushableDirs(pushDirs);
                // System.out.println(pushDirs);
            }
            // sort queue decreasing order of pushability
            // if items have same degree of pushability we randomize the order
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
            // update hashtable
            eloc.put(first.getId(), first.getPos());
            // if not pushable then don't put it back to queue
            if(first.getPushableDirs() == 0) continue;
            ArrayList<Movement> dirs = first.getDirs();
            // choose random direction from the list of possible push directions
            int random_idx = (dirs.size() == 0) ? 0 : rng.nextInt(0, dirs.size());
            Movement dir = dirs.get(random_idx);
            // o_pos = original pos, n_pos = new pos
            int[] o_pos = first.getPos();
            int[] n_pos = moveBox(dir, first.getPos());
            // update matrix cells
            this.matrix[o_pos[0]][o_pos[1]] = Blocks.PLAYER.getVal();
            this.matrix[n_pos[0]][n_pos[1]] = Blocks.BOXES.getVal();
            this.matrix[this.player_location[0]][this.player_location[1]] = Blocks.SPACES.getVal();
            this.player_location = o_pos;
            // put new box location to queue
            Pushability new_stuff = new Pushability(n_pos, first.getId());
            queue.add(new_stuff);
            
        }
        // overlay finished map with original map
        overlayMap(mxcp, blcp, eloc);
    }
    /**
     * 'Overlays' old map with current map
     * New box locations become end zones
     * this.matrix is the new matrix
     * param mx is the old matrix
     * @author Nicholas Mulianto
     * @param mx
     * @param bl_cp
     */
    public void overlayMap(int[][] mx, ArrayList<int[]> bl_cp, Hashtable<Integer, int[]> eloc){
        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                if(this.matrix[i][j] == Blocks.BOXES.getVal()) {
                    // for each index (i, j) of the new matrix
                    // if the value in said index is a box, we make that same index in the old matrix an end zone
                    mx[i][j] += Blocks.END_POINTS.getVal();
                }
            }
        }
        // store end locations
        for(int i = 0; i < eloc.keySet().size(); i++){
            end_locations.add(eloc.get(i));
        }
        this.matrix = mx;
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

    public void compute_terrain(){
        int ter = terrain_eval();
        this.terrain_v = ter;
    }
    /**
     * The sum of the number of obstacle numbers of all empty tiles.
     * @author Nicholas Mulianto
     * @param
     * @return
     */
    private int terrain_eval(){
        int TERRAIN = 0;
        for(int i = 0; i < this.matrix.length; i++){
            for(int j = 0; j < this.matrix.length; j++){
                if( this.matrix[i][j] == Blocks.SPACES.getVal() ||
                        this.matrix[i][j] == Blocks.PLAYER.getVal() ||
                        this.matrix[i][j] == Blocks.END_POINTS.getVal() ||
                        this.matrix[i][j] == Blocks.END_PLAYER.getVal()) {; }
                else continue;
                // get number of neighours that are obstacles
                int obstacle = 0;
                ArrayList<Integer> neighbours = new ArrayList<>();
                if(j > 0) neighbours.add(this.matrix[i][j-1]); // check left
                if(j < this.matrix.length - 1) neighbours.add(this.matrix[i][j+1]); // check right
                if(i < 0) neighbours.add(this.matrix[i-1][j]); // check up
                if(i > this.matrix.length - 1) neighbours.add((this.matrix[i+1][j])); // check down
                for(Integer k : neighbours)
                    switch(Blocks.get(k)){
                        case BOXES:
                        case END_BOXES:
                        case IMMOVABLES: obstacle++;
                        default: break;
                    }
                TERRAIN += obstacle;
            }
        }
        return TERRAIN;
    }

    /**
     * Calculates the congestion from a box location to it's respective end zone.
     * @author Nicholas Mulianto
     * @param
     * @return
     */
    private int congestion_eval(){
        // constants in the equation stated in the journal
        int alpha = 4;
        int beta = 4;
        int gamma = 1;
        int CONGESTION = 0;
        for(int i = 0; i < this.end_locations.size(); i++){
            // s = #boxes, g = #goals, o = #walls
            int s = 0, g = 0, o = 0;
            int[] bi = this.box_locations.get(i);
            int[] bf = this.end_locations.get(i);
            int bi_y = bi[0], bi_x = bi[1];
            int bf_y = bi[0], bf_x = bf[1];
            // rearrange the X and Y coordinates
            // such that x_range[0] <= x_range[1], same with y_range
            int[] y_range = (bi_y < bf_y) ? new int[]{ bi_y, bf_y } : new int[]{ bf_y, bi_y };
            int[] x_range = (bi_x < bf_x) ? new int[]{ bi_x, bf_x } : new int[]{ bf_x, bi_x };

            for(int y = y_range[0]; y <= y_range[1]; y++){
                for(int x = x_range[0]; x <= x_range[1]; x++){
                    switch(Blocks.get(this.matrix[y][x])){
                        case END_BOXES:
                            // penalize if BOX already at GOAL by not adding
                            //s--;
                            break;
                        case BOXES:
                            s++;
                            break;
                        case END_POINTS:
                        case END_PLAYER:
                            g++;
                            break;
                        case IMMOVABLES:
                            o++;
                            break;
                    }
                }
            }
            // again, see the linked paper on top
            CONGESTION += (alpha * s + beta * g + gamma * o);
        }
        return CONGESTION;
    }

    /**
     * Evaluate level given the terrain metric and congestion metric
     * See linked article for detail
     * @author Nicholas Mulianto
     * @param terrain
     * @param congestion
     * @return
     */
    private double level_eval(int terrain, int congestion){
        // normalization vector
        int k = 200;
        double combined = terrain * congestion;
        double c_root = Math.sqrt(combined);
        double value = c_root / k;
        return value;
    }
    /**
     * move box in specified direction
     * returns new box index
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

    /**
     * prints ascii game map
     * @author Nicholas Mulianto
     */
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
            Pushability n = new Pushability(this.box_locations.get(i), i);
            plist.add(n);
        }
        return plist;
    }

    /**
     * private class to store best level
     */
    private class EvalLevel{
        private int[][] mx;
        private double eval;
        private ArrayList<int[]> ends;

        private EvalLevel(int[][] m, double e, ArrayList<int[]> i){
            this.mx = m;
            this.eval = e;
            this.ends = new ArrayList<int[]>();
            if(i == null) return;
            for(int[] j : i){
                this.ends.add(new int[]{j[0], j[1]});
            }
        }

        private double getEval(){ return this.eval; }
        private int[][] getMx(){ return this.mx; }
        private ArrayList<int[]> getEnds() { return this.ends; }
    }
}

