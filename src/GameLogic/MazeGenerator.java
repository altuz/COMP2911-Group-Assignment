package GameLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by altuz on 6/05/17.
 */
public class MazeGenerator {
    int[][] maze;
    int[]   player_location;
    int[][] target;

    /**
     * CHECK OBJECT TYPE AND CALLS THE RESPECTIVE FUNCTION
     * Generate maze randomly if given an Integer
     * Generate maze from file if given a String
     * TODO: Random Generation
     * @param o
     */
    public MazeGenerator(Object o) {
        if (o instanceof Integer) {
            this.maze = generateRandom((Integer) o);
        }
        else if (o instanceof String) {
            this.maze = generateFromFile((String) o);
        }
    }

    /**
     * TODO: Whole Function
     * @param size
     * @return
     */
    private int[][] generateRandom(Integer size) {
        int[][] loc_maze = new int[][] { {1, 0}, {0, 0} };
        this.player_location = new int[] {0, 1};
        return loc_maze;
    }

    /**
     * READS FILE AND GENERATE MAZE.
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
            String[] blocks = input.get(i).split(".");
            for (int j = 0; j < blocks.length; j++) {
                int curr_block = Integer.parseInt(blocks[j]);
                if (curr_block == 1) player_location = new int[] {i, j};
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
}