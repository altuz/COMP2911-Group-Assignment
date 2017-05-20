package GameLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import Definitions.Blocks;
/**
 * Created by altuz on 6/05/17.
 */
public class MazeGenerator {
    private int[][] maze;
    private int[]   player_location;
    private int[][] target;
    private ArrayList<int[]> end_blocks;

    /**
     * CHECK OBJECT TYPE AND CALLS THE RESPECTIVE FUNCTION
     * Generate maze randomly if given an Integer
     * Generate maze from file if given a String
     * TODO: Random Generation
     * @param o
     */
    public MazeGenerator(Object o) {
        end_blocks = new ArrayList<int[]>();
        if (o instanceof Integer) {
            this.maze = generateRandom((Integer) o);
        }
        else if (o instanceof String) {
            this.maze = generateFromFile((String) o);
        }
    }

    /**
     * TODO: Whole Function
     * http://www-users.cs.umn.edu/~bilal/papers/GIGA16_SOKOBAN.pdf
     * @param size + 2
     * @return
     */
    private int[][] generateRandom(Integer size) {
        int padded_size = size + 2;
        int[][] loc_maze = new int[padded_size][padded_size];
        int[][] act_maze = new int[size][size];
        // initialize
        for(int i = 0; i < size; i++){
            Arrays.fill(act_maze[i], 1);
        }
        // put agent/player in middle
        int mid = (size+1)/2;
        int[] agent = new int[]{ mid, mid };
        act_maze[mid][mid] = Blocks.PLAYER.getVal();

        return loc_maze;
    }

    private void deleteObstacle(int r_idx, int c_idx, int[][] maze){
        maze[r_idx][c_idx] = Blocks.SPACES.getVal();
    }

    /**
     * The sum of the number of obstacle numbers of all empty tiles.
     * @author Nicholas Mulianto
     * @param maze
     * @return
     */
    private int terrain_eval(int[][] maze){
        int TERRAIN = 0;
        for(int i = 0; i < maze.length; i++){
            for(int j = 0; j < maze.length; j++){
                if(Blocks.values[maze[i][j]] != Blocks.SPACES ||
                        Blocks.values[maze[i][j]] != Blocks.PLAYER ||
                        Blocks.values[maze[i][j]] != Blocks.END_POINTS ||
                        Blocks.values[maze[i][j]] != Blocks.END_PLAYER) continue;
                // get number of neighours that are obstacles
                int obstacle = 0;
                ArrayList<Integer> neighbours = new ArrayList<>();
                if(j > 0) neighbours.add(maze[i][j-1]); // check left
                if(j < maze.length - 1) neighbours.add(maze[i][j+1]); // check right
                if(i < 0) neighbours.add(maze[i-1][j]); // check up
                if(i > maze.length - 1) neighbours.add((maze[i+1][j])); // check down
                for(Integer k : neighbours)
                switch(Blocks.values[k]){
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
