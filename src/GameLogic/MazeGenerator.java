package GameLogic;

import java.io.File;
import java.util.*;
import Definitions.Blocks;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by altuz on 6/05/17.
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
     * TODO: Random Generation
     * @param o
     */
    public MazeGenerator(Object o) {
        end_blocks = new ArrayList<int[]>();
        if (o == null) {
            this.maze = shuffleTest(null);
        }
        else if (o instanceof Integer) {
            this.maze = randomGen((Integer) o, 4);
        }
        else if (o instanceof String) {
            this.maze = generateFromFile((String) o);
        }
    }

    public MazeGenerator(int size, int lim_box){
        this.maze = randomGen(size, lim_box);

    }

    private int[][] shuffleTest(int[][] mx){
        int[][] maze = new int[][]{
                {-1, -1, -1, -1, -1, 0, -1, 0},
                {-1, -1, 0, -1, -1, 0, 0, 0},
                {0, 0, 2, 0, 2, 0, -1, -1},
                {-1, -1, 0, 2, 1, 0, -1, 0},
                {0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, -1, 0, 0, 0, 0, 0},
                {0, 2, 0, 0, 0, -1, 0, -1},
                {0, 0, -1, -1, -1, -1, -1, -1}
        };
        if(mx != null) maze = mx;
        State s = new State();
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze.length; j++){
                if(maze[i][j] == 2) s.addBox(new int[]{i, j});
            }
        }
        s.setMatrix(maze);
        s.setPlayerLoc(new int[]{3, 4});
        s.shuffleLevel(1000, 20);
        //this.player_location = s.getPlayerLoc();
        //this.end_blocks = s.getEndLoc();
        int[][] p_maze = wallPadding(s.getMatrix());
        return p_maze;
    }

    private int[][] randomGen(int size, int box_max){
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int mid = (size+1)/2;
        int[][] initial_maze = new int[size][size];
        for(int i = 0; i < size; i++)
            Arrays.fill(initial_maze[i], -1);
        initial_maze[mid][mid] = 1;

        PriorityQueue<MazeState> open_s = new PriorityQueue<>(new Comparator<MazeState>() {
            @Override
            public int compare(MazeState o1, MazeState o2) {
                if(o1.getBoxNum() - o2.getBoxNum() == 0)  return rng.nextInt(-1, 2);
                else {
                    return o2.getBoxNum() - o1.getBoxNum();
                }
            }
        });
        State best_ter = new State();

        MazeState init = new MazeState(initial_maze);
        for(int j = 0; j < 2; j++){
            int k = (j == 0) ? 1 : -1;
            // idx0 = right, idx1 = bottom, idx2 = left, idx3 = top
            init.expandBorder(new int[]{ mid, mid + k});
            init.expandBorder(new int[]{ mid + k, mid});
        }

        open_s.add(init);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + 1500; // set the end time to be 1 seconds after the current time
        while(System.currentTimeMillis() <= endTime){
            MazeState m = open_s.poll();
            int roll = rng.nextInt(0, 3);
            if(roll == 0 && m.getInnNum() > 7 && m.getBoxNum() < box_max) {
                // put box
                for(int i = 0; i < 2; i++){
                    MazeState cp = new MazeState(m.getMat());
                    cp.setBorders(m.getBorders());
                    cp.setBoxes(m.getBoxes());
                    cp.setInner(m.getInner());
                    int[] box_idx = m.randomInner(rng);
                    cp.removeInner(box_idx);
                    cp.addBox(box_idx);
                    open_s.add(cp);

                    State ns = new State();
                    ns.setMatrix(cp.getMat());
                    ns.addAllBox(cp.getBoxes());
                    ns.compute_terrain();
                    if(ns.getTerrain() > best_ter.getTerrain() && cp.getBoxNum() == box_max) best_ter = ns;
                }
            }
            else {
                // spawn two copies
                for(int i = 0; i < 2; i++) {
                    MazeState cp = new MazeState(m.getMat());
                    cp.setBorders(m.getBorders());
                    cp.setBoxes(m.getBoxes());
                    cp.setInner(m.getInner());
                    int[] del_idx = m.randomBorder(rng);
                    ArrayList<int[]> aj = getAdjacent(del_idx, size, cp.getMat());
                    for(int[] adjacent : aj)
                        cp.expandBorder(adjacent);
                    cp.removeBorder(del_idx);
                    cp.addInner(del_idx);
                    open_s.add(cp);

                    State ns = new State();
                    ns.setMatrix(cp.getMat());
                    ns.addAllBox(cp.getBoxes());
                    ns.compute_terrain();
                    if(ns.getTerrain() > best_ter.getTerrain() && cp.getBoxNum() == box_max) best_ter = ns;
                }
                // delete wall
            }
        }
        initial_maze = best_ter.getMatrix();
        return shuffleTest(initial_maze);
    }

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
    private int[][] wallPadding(int[][] lvl){
        int padded_size = lvl.length + 2;
        int[][] padded_maze = new int[padded_size][padded_size];
        for(int i = 0; i < lvl.length; i++){
            for(int j = 0; j < lvl.length; j++){
                padded_maze[i+1][j+1] = lvl[i][j];
                switch (Blocks.get(padded_maze[i+1][j+1])) {
                    case PLAYER: player_location = new int[] {i + 1, j + 1};
                        break;
                    case END_POINTS:
                    case END_PLAYER:
                    case END_BOXES: end_blocks.add(new int[] {i + 1, j + 1});
                    default:
                }
            }
        }
        for(int i = 0; i < padded_size; i++){
            padded_maze[0][i] = padded_maze[i][0] = Blocks.IMMOVABLES.getVal();
            padded_maze[padded_size-1][i] = padded_maze[i][padded_size-1] = Blocks.IMMOVABLES.getVal();
        }

        return padded_maze;
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
