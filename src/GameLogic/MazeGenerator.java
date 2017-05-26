package GameLogic;

import java.io.File;
import java.util.*;
import Definitions.Blocks;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nicholas Mulianto on 6/05/17.
 */
public class MazeGenerator {
    private int[][] maze;
    private int[]   player_location;
    private ArrayList<int[]> end_blocks;
    private final Random random = new Random();

    /**
     * CHECK OBJECT TYPE AND CALLS THE RESPECTIVE FUNCTION
     * Generate maze randomly if given an Integer
     * Generate maze from file if given a String
     * @author Nicholas Mulianto
     * TODONE: Random Generation
     * @param o
     */
    public MazeGenerator(Object o) {
        end_blocks = new ArrayList<int[]>();
        if (o instanceof Integer) {
            this.maze = randomGen((Integer) o, 4);
        }
        else if (o instanceof String) {
            this.maze = generateFromFile((String) o);
        }
    }
    /**
     * Second constructor, randomly generates size * size matrix with lim_box boxes
     * @author Nicholas Mulianto
     * @param size
     * @param lim_box
     */
    public MazeGenerator(int size, int lim_box){
        end_blocks = new ArrayList<int[]>();
        this.maze = randomGen((Integer) size, lim_box);
    }
    /**
     * Randomly generates a maze.
     * Starts with maze full of walls.
     * Randomly remove walls and put box.
     * Returns best map after 2 seconds of search
     * @author Nicholas Mulianto
     * @precondition size >= 0 and 0 < box_max < size^2
     * @param size
     * @param box_max
     * @return
     */
    private int[][] randomGen(int size, int box_max){
    	// one RNG to rule them all
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        // player starts at mid
        int mid = (size+1)/2;
        // initialize maze to all walls except player at mid
        int[][] initial_maze = new int[size][size];
        for(int i = 0; i < size; i++)
            Arrays.fill(initial_maze[i], -1);
        initial_maze[mid][mid] = 1;
        // unbounded open set for search
        // can grow to enormous size but due to timeout it will be fine
        PriorityQueue<MazeState> open_s = new PriorityQueue<>(new Comparator<MazeState>() {
            @Override
            public int compare(MazeState o1, MazeState o2) {
            	// prioritize state with most boxes, else randomize
                if(o1.getBoxNum() - o2.getBoxNum() == 0)  return rng.nextInt(-1, 2);
                else {
                    return o2.getBoxNum() - o1.getBoxNum();
                }
            }
        });
        // placeholder for 'best state'
        // there are two metrics that determines the 'quality' of the maze.
        // terrain and congestion. 
        // congestion can only be calculated after the shuffling phase, so we just base the quality on terrain here.
        State best_ter = new State();
        // initial mazestate
        MazeState init = new MazeState(initial_maze);
        // finds adjacent neighbours of the first movable cell
        for(int j = 0; j < 2; j++){
            int k = (j == 0) ? 1 : -1;
            init.expandBorder(new int[]{ mid, mid + k });
            init.expandBorder(new int[]{ mid + k, mid });
        }
        open_s.add(init);
        // for timeout
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 2000; // set the end time to be 1 seconds after the current time
        // loops as long as system time < endTime
        while(System.currentTimeMillis() <= endTime){
            MazeState m = open_s.poll();
            // roll dice to see if we should insert box or remove wall
            // only put box when we have more than 1 explored box
            int roll = rng.nextInt(0, 5);
            if(roll == 0 && m.getInnNum() > 0 && m.getBoxNum() < box_max) {
                // put box
                for(int i = 0; i < 2; i++){
                	// make a copy of current maze
                    MazeState cp = new MazeState(m.getMat());
                    cp.setBorders(m.getBorders());
                    cp.setBoxes(m.getBoxes());
                    cp.setInner(m.getInner());
                    // find a random empty space to place box
                    int[] box_idx = m.randomInner(rng);
                    cp.removeInner(box_idx);
                    cp.addBox(box_idx);
                    open_s.add(cp);
                    // only consider if theres enough boxes
                    if(cp.getBoxNum() == box_max){
                    	State ns = new State();
                        ns.setMatrix(cp.getMat());
                        ns.addAllBox(cp.getBoxes());
                        // compute terrain
                        ns.compute_terrain();
                        // compare with current best
                        if(ns.getTerrain() > best_ter.getTerrain()) best_ter = ns;
                    }
                }
            }
            else {
                // spawn two copies
                for(int i = 0; i < 2; i++) {
                	// make copy of current maze
                    MazeState cp = new MazeState(m.getMat());
                    cp.setBorders(m.getBorders());
                    cp.setBoxes(m.getBoxes());
                    cp.setInner(m.getInner());
                    // find a random border/wall to remove
                    int[] del_idx = m.randomBorder(rng);
                    // finds adjacent walls of new movable block (expanding border)
                    ArrayList<int[]> aj = getAdjacent(del_idx, size, cp.getMat());
                    for(int[] adjacent : aj)
                        cp.expandBorder(adjacent);
                    cp.removeBorder(del_idx);
                    cp.addInner(del_idx);
                    open_s.add(cp);
                    // only consider if theres enough boxes
                    if(cp.getBoxNum() == box_max){
                    	State ns = new State();
                        ns.setMatrix(cp.getMat());
                        ns.addAllBox(cp.getBoxes());
                        // compute terrain
                        ns.compute_terrain();
                        // compare with current best
                        if(ns.getTerrain() > best_ter.getTerrain()) best_ter = ns;
                    }
                }
                // delete wall
            }
        }
        this.player_location = new int[]{mid, mid};
        best_ter.setPlayerLoc(new int[]{mid, mid});
        best_ter.shuffleLevel(1000, 50);
        this.end_blocks = best_ter.getEndLoc();
        return best_ter.getMatrix();
    }
    /**
     * Given an empty cell, finds adjacent walls
     * @precondition idx[0] and idx[1] is within bounds of m[][]
     * @param idx
     * @param lim
     * @param m
     * @return
     */
    private ArrayList<int[]> getAdjacent(int[] idx, int lim, int[][] m){
        int y = idx[0], x = idx[1];
        ArrayList<int[]> adj = new ArrayList<>();
        for(int j = 0; j < 2; j++){
            int k = (j == 0) ? 1 : -1;
            // idx0 = right, idx1 = bottom, idx2 = left, idx3 = top
            if(x + k >= 0 && x + k < lim && m[y][x + k] == -1)
                adj.add(new int[]{ y, x + k});
            if(y + k >= 0 && y + k < lim && m[y + k][x] == -1)
                adj.add(new int[]{ y + k, x});
        }
        return adj;
    }
    /**
     * TODO: Whole Function,
     * http://www-users.cs.umn.edu/~bilal/papers/GIGA16_SOKOBAN.pdf
     * @param size + 2
     * @return
     */
    private int[][] generateRandom(Integer size) {
        int padded_size = size + 2;
        int maximumBoxes = size/2;
        int[][] loc_maze = new int[padded_size][padded_size];
        int[][] act_maze = new int[size][size];
        
        // initialize
        for(int i = 0; i < size; i++){
            Arrays.fill(act_maze[i], -1);
        }
        
        // put agent/player in middle
        int mid = (size+1)/2;
        int[] agent = new int[]{ mid, mid };
        act_maze[mid][mid] = Blocks.PLAYER.getVal();
        List<MatrixState> open = new ArrayList<MatrixState>();
        List<MatrixState> closed = new ArrayList<MatrixState>();
        MatrixState initMatrix = new MatrixState(act_maze, 0);
        open.add(initMatrix); // add the initial maze to the open list
        long startTime = System.currentTimeMillis();
        long endTime = startTime + 2000; // set the end time to be 2 seconds after the current time
        
        
        // loop to randomly generate matrices
        while (startTime < endTime) {
        	MatrixState currMatrix = open.remove(0); // remove the first index
        	int[][] matrix = currMatrix.getMatrix();
        	boolean changeFlag = false; // a flag to handle if we have made a change
        	closed.add(currMatrix); // add the matrix to the closed list
        	
        	// nested for loop to iterate through the matrix
        	for (int i = 0 ; i < matrix.length ; i++) {
                for (int j = 0 ; j < matrix[i].length ; j++) {
                	// if we find an open space
                	if (matrix[i][j] == 0) {
                		// 50/50 probability of making a change
                		if (random.nextBoolean()) {
                			// 50/50 probability of creating a blank space or a box
                			if (random.nextBoolean()) {
                				// create an open space
                				changeFlag = deleteObstacle(i, j, matrix, size); // attempt to create an open space
                				MatrixState newMatrix = new MatrixState(matrix, currMatrix.getNumBlocks());
            					open.add(newMatrix);
                			} else {
                				// ensures that we have not exceeded the maximum number of boxes we can have in the game
                				if (currMatrix.getNumBlocks() >= maximumBoxes) {
                					// TODO: also store the exact location of all the boxes
                					int numberOfBoxes = currMatrix.getNumBlocks();
                					matrix[i][j] = 3;
                					currMatrix.incrementNumBlocks();
                					changeFlag = true;
                					MatrixState newMatrix = new MatrixState(matrix, numberOfBoxes+1);
                					open.add(newMatrix); // add the new matrix to the open list
                				} else {
                					changeFlag = deleteObstacle(i, j, matrix, size);
                					MatrixState newMatrix = new MatrixState(matrix, currMatrix.getNumBlocks());
                					open.add(newMatrix);
                				}
                			}
                		} else {
                			// do nothing
                			continue;
                		}
                	}
                }
        	}
        	
        	// handles the case that no changes has been made
        	while (!changeFlag) {
				changeFlag = deleteObstacle(mid, mid, matrix, size); // attempt to create an open space
				MatrixState newMatrix = new MatrixState(matrix, currMatrix.getNumBlocks());
				open.add(newMatrix);
        	}
        	
        	startTime = System.currentTimeMillis();
        }
        System.out.println(closed.size());
        return closed.get(closed.size()-1).getMatrix();
    }

    /**
     * TODO: Delete an item in the matrix randomly
     * 
     * @param r_idx
     * @param c_idx
     * @param maze
     */
    private boolean deleteObstacle(int r_idx, int c_idx, int[][] maze, int maxSize){
    	int rowMin, rowMax, columnMin, columnMax;
    	int attempt = 0;
    	
    	// Set the maximum size for row index
    	if (r_idx == 0) {
    		rowMin = 0;
    	} else {
    		rowMin = r_idx-1;
    	} 
    	
    	if (r_idx == maxSize) {
    		rowMax = maxSize;
    	} else {
    		rowMax = r_idx+1;
    	}
    	
    	// Set the maximum size for column index
    	if (c_idx == 0) {
    		columnMin = 0;
    	} else {
    		columnMin = c_idx-1;
    	} 
    	
    	if (c_idx == maxSize) {
    		columnMax = maxSize;
    	} else {
    		columnMax = c_idx+1;
    	}
    	
    	// attempts to delete an obstacle 4 times
    	while (attempt < 8 ) {
    		int randomRowIdx = ThreadLocalRandom.current().nextInt(rowMin, rowMax); // generates a random row value
        	int randomColIdx = ThreadLocalRandom.current().nextInt(columnMin, columnMax); // generates a random column value
        	
        	if (maze[randomRowIdx][randomColIdx] == -1) {
        		maze[randomRowIdx][randomColIdx] = 0;
        	}
        	
        	attempt++;
        	return true;
    	}
    	
        return false;
    }

    /**
     * READS FILE AND GENERATE MAZE.
     * SPLITS ON WHITE SPACE A.K.A MAZE FILES HAVE TO BE SPACE SEPARATED
     * TODO: More test mazes (at least 3 mazes)
     * @param file_name
     * @return
     */
    private int[][] generateFromFile(String file_name) {
        ArrayList<String> input = new ArrayList<String>();
        try {
            Scanner in = new Scanner(new File(file_name));
            while (in.hasNext()) {
                // read whole line and split on any character
                String 	 line = in.nextLine();
                input.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int[][] loc_maze = new int[input.size()][input.size()];
        for (int i = 0; i < input.size(); i++) {
            String[] blocks = input.get(i).split("\\s+");
            for (int j = 0; j < blocks.length; j++) {
                int curr_block = Integer.parseInt(blocks[j]);
                switch (curr_block) {
                    case 1: player_location = new int[] {i, j};
                            break;
                    case 3:
                    case 4:
                    case 5: end_blocks.add(new int[] {i, j});
                    default:
                }
                loc_maze[i][j] = curr_block;
            }
        }

        return loc_maze;
    }
    
    /**
     * Returns maze
     * @return
     */
    public int[][] getMaze() {
        return this.maze;
    }

    /**
     * Returns player location
     * @return
     */
    public int[] getPlayer() {
        return this.player_location;
    }

    /**
     * Return goal locations
     * @return
     */
    public ArrayList<int[]> getGoals() {
        return this.end_blocks;
    }
}
