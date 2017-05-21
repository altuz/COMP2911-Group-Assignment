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
        if (o == null) {
            this.maze = shuffleTest();
        }
        else if (o instanceof Integer) {
            this.maze = generateRandom((Integer) o);
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
        s.shuffleLevel(100, 10);
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
