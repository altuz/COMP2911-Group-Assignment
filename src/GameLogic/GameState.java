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

    public GameState(Object o) {
        this.move_count = 0;

        MazeGenerator level = new MazeGenerator(o);
        this.maze       = level.getMaze();
        this.player_loc = level.getPlayer();
    }

    public boolean player_move(Movement dir) {
        boolean suc;
        switch (dir) {
            case UP: suc = move_up();
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
        }

        return false;
    }

    private boolean move_up() {
        return true;
    }

    private boolean move_down() {
        return true;
    }

    private boolean move_left() {
        return true;
    }

    private boolean move_right() {
        return true;
    }
}
