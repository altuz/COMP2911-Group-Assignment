package GameLogic;
import Definitions.Movement;

/**
 * Created by altuz on 6/05/17.
 */
public class GameState{
    //private MazeGenerator level;
    private int move_count;
    private int[] player_loc;
    private int[][] maze;

    /**
     * Creates a new level and initialize player location.
     * If given an int, randomly generates maze.
     * If given a string, generate maze from file.
     * TODO: RANDOM GENERATION
     * Object checked at MazeGenerator
     * @param o (String or Integer)
     */
    public GameState(Object o) {
        this.move_count = 0;

        MazeGenerator level = new MazeGenerator(o);
        this.maze       = level.getMaze();
        this.player_loc = level.getPlayer();

    }

    /**
     * Moves the player based on direction.
     * Usage: player_move(Movement.UP);
     *        player_move(Movement.DOWN);
     *        etc
     * @param dir
     * @return false if illegal move
     *         true if legal move
     */
    public boolean player_move(Movement dir) {
        boolean suc = false;
        switch (dir) {
            case UP:
                suc = move_up();
                break;
            case DOWN:
                suc = move_down();
                break;
            case LEFT:
                suc = move_left();
                break;
            case RIGHT:
                suc = move_right();
                break;
        }
        if (suc) this.move_count += 1;
        return false;
    }

    /**
     * Moves up.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     */
    private boolean move_up() {
        return true;
    }

    /**
     * Moves down.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     */
    private boolean move_down() {
        return true;
    }

    /**
     * Moves left.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     */
    private boolean move_left() {
        return true;
    }

    /**
     * Moves right.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     */
    private boolean move_right() {
        return true;
    }

    public int[][] getMaze() {
        return this.maze;
    }
}
