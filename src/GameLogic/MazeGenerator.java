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
            this.maze = shuffleTest();
        }
        else if (o instanceof Integer) {
            this.maze = generateRandom((Integer) o);
            System.out.println("Generating random maze");
        }
        else if (o instanceof String) {
            this.maze = generateFromFile((String) o);
        }
    }

    private int[][] shuffleTest(){
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
        long endTime = startTime + 100; // set the end time to be 2 seconds after the current time
        
        
        // loop to randomly generate matrices
        while (startTime < endTime) {
        	MatrixState currMatrix = open.remove(0); // remove the first index
        	int currBoxNum = currMatrix.getNumBlocks();
        	int[][] matrix = currMatrix.getMatrix();
        	boolean changeFlag = false; // a flag to handle if we have made a change
        	closed.add(currMatrix); // add the matrix to the closed list
        	
        	// nested for loop to iterate through the matrix
        	for (int i = 0 ; i < matrix.length  && !changeFlag; i++) {
                for (int j = 0 ; j < matrix[i].length && !changeFlag; j++) {
                	// if we find an open space
                	if (matrix[i][j] == 0 || matrix[i][j] == 3) {
                		// 50/50 probability of making a change
                		if (random.nextBoolean()) {
                			// 50/50 probability of creating a blank space or a box
                			if (random.nextBoolean()) {
                				// create an open space
                				changeFlag = deleteObstacle(i, j, matrix, size); // attempt to create an open space
                				MatrixState newMatrix = new MatrixState(matrix, currMatrix.getNumBlocks());
            					open.add(newMatrix);
                			} else {
                				//System.out.println("Maximum number of boxes " + maximumBoxes);
                				// ensures that we have not exceeded the maximum number of boxes we can have in the game
                				if (currBoxNum <= maximumBoxes) {
                					System.out.println("Creating a box");
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
        
        int last = closed.size()-1;
        MatrixState bestMatrix = closed.get(last);
        int[][] bestState = bestMatrix.getMatrix();
       
        return bestState;
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
        	
        	System.out.println(randomRowIdx + " " + randomColIdx);
        	
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
