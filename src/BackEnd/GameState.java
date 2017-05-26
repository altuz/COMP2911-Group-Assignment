package BackEnd;
import Definitions.Movement;
import java.util.ArrayList;

/**
 * Created by Nicholas Mulianto and James Ren on 6/05/17.
 */
public class GameState{
    //private MazeGenerator level;
    private int move_count;
    private int[] player_loc;
    private int[][] maze;
    private ArrayList<int[]> goal_blocks;
    // circular queue for prev state
    private State[] prev_states;
    private int ps_idx;
    private int ps_size;
    /**
     * Creates a new level and initialize player location.
     * If given an int, randomly generates maze.
     * If given a string, generate maze from file.
     * Object checked at MazeGenerator
     * @author Nicholas Mulianto
     * @param o (String or Integer)
     */
    public GameState(Object o) {
    	if(o instanceof GameState){
    		GameState ob = (GameState) o;
    		State cp = new State();
    		cp.setMatrix(ob.maze);
    		cp.setPlayerLoc(ob.player_loc);
    		cp.setEndLocations(ob.goal_blocks);
            initVars();
            this.maze = cp.getMatrix();
            this.player_loc = cp.getPlayerLoc();
            this.goal_blocks = cp.getEndLoc();
    	}
    	else{
    		initVars();
            MazeGenerator level = new MazeGenerator(o);
            this.maze       = level.getMaze();
            this.player_loc = level.getPlayer();
            this.goal_blocks= level.getGoals();
    	}
    }

    public GameState(int a, int b){
    	initVars();
        MazeGenerator level = new MazeGenerator(a, b);
        this.maze       = level.getMaze();
        this.player_loc = level.getPlayer();
        this.goal_blocks= level.getGoals();
    }

    public void initVars(){
        this.move_count = 0;
        this.ps_idx = 0;
        this.ps_size = 0;
        this.prev_states = new State[5];
    }
    
    public void stateCp(GameState st){
    	State cp = new State();
		cp.setMatrix(st.maze);
		cp.setPlayerLoc(st.player_loc);
		cp.setEndLocations(st.goal_blocks);
		initVars();
        this.maze = cp.getMatrix();
        this.player_loc = cp.getPlayerLoc();
        this.goal_blocks = cp.getEndLoc();
    }
    /**
     * Returns true if game is over, false otherwise
     * @author Nicholas Mulianto
     * @return
     */
    public boolean game_over() {
        for (int[] coords : this.goal_blocks) {
            int x = coords[0];
            int y = coords[1];
            if (this.maze[x][y] != 5) return false;
        }
        return true;
    }

    /**
     * Undoes player move. Move back one state.
     * Pops the tail of the queue
     * @author Nicholas Mulianto
     */
    public void undo_move(){
        if(this.ps_size <= 0) return;
        if(this.ps_idx == 0) this.ps_idx = this.prev_states.length;
        State prev_state = this.prev_states[this.ps_idx-1];
        this.maze = prev_state.getMatrix();
        this.player_loc = prev_state.getPlayerLoc();
        this.goal_blocks = prev_state.getEndLoc();
        this.prev_states[this.ps_idx-1] = null;
        this.ps_idx--;
        if(this.ps_idx == 0)
            this.ps_idx = this.prev_states.length;
        this.ps_size--;
    }
    /**
     * Moves the player based on direction.
     * Usage: player_move(Movement.UP);
     *        player_move(Movement.DOWN);
     *        etc
     * @author Nicholas Mulianto
     * @param dir
     * @return false if illegal move
     *         true if legal move
     */
    public boolean player_move(Movement dir) {
        boolean suc = false;
        // create copy of current maze
        State this_state = new State();
        this_state.setMatrix(this.maze);
        this_state.setPlayerLoc(this.player_loc);
        this_state.setEndLocations(this.goal_blocks);
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
        // if move successful add prev state to queue for undoing
        if (suc) {
            this.move_count += 1;
            // add current state over to queue
            if(this.ps_idx == this.prev_states.length) this.ps_idx = 0;
            this.prev_states[this.ps_idx] = this_state;
            // increment queue and wraps around
            this.ps_idx++;
            this.ps_idx = this.ps_idx % this.prev_states.length;
            // increase size and cap it
            if(this.ps_size < this.prev_states.length)
                this.ps_size++;
        }
        return suc;
    }

    /**
     * Helper function for movements.
     * Given a direction and the new row/col index, moves the player to that direction.
     * @author James Ren, Refactored by Nicholas Mulianto
     * @param new_row
     * @param new_col
     * @param dir
     * @return success
     */
    private boolean move_player(int new_row, int new_col, Movement dir) {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int columnNum = currLocation[1]; // gets the column that the player is standing on
        int boxRowNum = 0;
        int boxColNum = 0;
        // finds axis of movement, true = x axis, false = y axis
        boolean axis = true;
        switch (dir) {
            case UP:
            case DOWN:
                axis = true;
                break;
            case LEFT:
            case RIGHT:
                axis = false;
                break;
        }

        int blockNum = this.maze[new_row][new_col]; // gets the block status above the player's position
        /*
         * Gets the tile that the player current standing on:
         * -> 1 == player is currently standing on a blank tile
         * -> 4 == player is currently standing on an end point
         */
        int playerCurrPosition = this.maze[rowNum][columnNum];
        // System.out.println("The player is: " + playerCurrPosition);

        switch (blockNum) {
            case -1: // the block above is an immovable object
                // System.out.println("Movement Err: Cannot move UP because there is an immovable object");
                return false;
            case 0: // the block above is an empty tile
                if (playerCurrPosition == 1) { // handles the case that the player is standing on an empty tile
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[new_row][new_col] = 1; // move the player to the tile above
                    this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                    this.player_loc[1] = (axis) ? columnNum : new_col;
                } else {
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[new_row][new_col] = 1;
                    this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                    this.player_loc[1] = (axis) ? columnNum : new_col;
                }
                break;
            case 2: // the block above is a box
                boxRowNum = (axis) ? new_row : rowNum;
                boxColNum = (axis) ? columnNum : new_col;
                // System.out.printf("new_row = %d, new_col = %d\n", new_row, new_col);
                // System.out.printf("boxRowNum = %d, boxColNum = %d\n", boxRowNum, boxColNum);
                // handles the case that the box movement is successful
                if (moveBox(boxRowNum, boxColNum, dir)) {
                    if (playerCurrPosition == 1) {
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[new_row][new_col] = 1; // move the player to the tile above
                        this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                        this.player_loc[1] = (axis) ? columnNum : new_col;
                    }
                    // handles the case that the player is standing on an endpoint
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[new_row][new_col] = 1;
                        this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                        this.player_loc[1] = (axis) ? columnNum : new_col;
                    }
                    break;
                } else { // box movement was not successful

                    return false;
                }
            case 3: // handles the case that the tile above is an end point
                // System.out.println("Case 3");
                if (playerCurrPosition == 1) {
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[new_row][new_col] = 4; // move the player to the tile above
                    this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                    this.player_loc[1] = (axis) ? columnNum : new_col;
                } else { // the player is currently standing on an end point
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[new_row][new_col] = 4;
                    this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                    this.player_loc[1] = (axis) ? columnNum : new_col;
                }
                break;
            case 5: // handles the case that that the tile above has a box on an end point
                boxRowNum = (axis) ? new_row : rowNum;
                boxColNum = (axis) ? columnNum : new_col;
                // handles the case that the box movement is successful
                if (moveBox(boxRowNum, boxColNum, dir)) {
                    if (playerCurrPosition == 1) { // default case
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[new_row][new_col] = 4; // move the player to the tile above
                        this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                        this.player_loc[1] = (axis) ? columnNum : new_col;
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[new_row][new_col] = 4;
                        this.player_loc[0] = (axis) ? new_row : rowNum; // update the player's location
                        this.player_loc[1] = (axis) ? columnNum : new_col;
                    }
                    break;
                } else { // box movement was not successful
                    return false;
                }

        }
        return true;
    }

    /**
     * Moves up.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return True if the move was successful
     * 		   False if the move was unsuccessful
     *
     * @author James Ren
     */
    private boolean move_up() {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int colNum = currLocation[1];
        // Handles the case that the player wants to move out of bounds
        if(rowNum == 0) {
            // System.out.println("Movement Err: Moving out of bounds");
            return false;
        }
        // System.out.printf("new_row = %d, new_col = %d\n", rowNum-1, colNum);

        return move_player(rowNum-1, colNum, Movement.UP);
    }

    /**
     * Moves down.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     *
     * @author James Ren
     */
    private boolean move_down() {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int colNum = currLocation[1]; // gets the column that the player is standing on
        int rowMax = this.maze.length; // gets the maximum row length of the 2D matrix
        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(rowNum >= rowMax) {
            // System.out.println("Movement Err: Moving out of bounds");
            return false;
        }
        // System.out.printf("new_row = %d, new_col = %d\n", rowNum+1, colNum);
        return move_player(rowNum+1, colNum, Movement.DOWN);
    }

    /**
     * Moves left.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     *
     * @author James Ren
     */
    private boolean move_left() {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int colNum = currLocation[1]; // gets the column that the player is standing on
        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(colNum == 0) {
            // System.out.println("Movement Err: Moving out of bounds");
            return false;
        }
        // System.out.printf("new_row = %d, new_col = %d\n", rowNum, colNum-1);
        return move_player(rowNum, colNum-1, Movement.LEFT);
    }

    /**
     * Moves right.
     * TODO: CHECK BOUNDARIES AND EDGE CASES (WHEN MOVING BLOCK OR WHEN PLAYER MOVES ALONE)
     * @return success
     *
     * @author James Ren
     */
    private boolean move_right() {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int colNum = currLocation[1]; // gets the column that the player is standing on
        int colMax = this.maze[0].length; //gets the maximum column size
        //System.out.println("Total number of columns: " + colMax);
        //System.out.println("Player location: " + columnNum);
        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(colNum+1 >= colMax) {
            // System.out.println("Movement Err: Moving out of bounds");
            return false;
        }
        // System.out.printf("new_row = %d, new_col = %d\n", rowNum, colNum+1);
        return move_player(rowNum, colNum+1, Movement.RIGHT);
    }

    /**
     * Checks if the box is able to move to the given position
     *
     * @param boxColumnNum - the column index that the box is sitting on in the 2D array
     * @param boxRowNum - the row index that the box is sitting on in the 2D array
     * @param dir - the direction that the box needs to move
     * @return true - the movement is successful
     * 		   false - the movement is not successful
     *
     * @author James Ren
     */
    private boolean moveBox(int boxRowNum, int boxColumnNum, Movement dir) {
    	/*
         * Gets the number of the tile that the box is on:
         * -> 2 == box is not on the end point
         * -> 5 == box is on the end point
         */
        int boxType = this.maze[boxRowNum][boxColumnNum]; 
        int tile = 0; 
        
        switch (dir) {
            case UP: // moving the box up
                // handles the case that the box wants to move out of bounds
                if (boxRowNum == 0) {
                    System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }
                
                tile = this.maze[boxRowNum-1][boxColumnNum]; // gets the tile above the box
                
                switch (tile) {
                    case -1: // the tile above is an immovable object
                        System.out.println("Movement Err: Box cannot move UPWARDS because there is an immoavable object"); // For testing - comment out if needed
                        return false; 
                    case 0: // the tile above the box is empty
                        if (boxType == 2) {
                            this.maze[boxRowNum-1][boxColumnNum] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else {
                            // handles the case that the box is on the finish line
                            this.maze[boxRowNum-1][boxColumnNum] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 3; // reset the endpoint that the box was sitting on
                            return true;
                        }
                    case 2: // the tile above is another box
                        System.out.println("Movement Err: Box cannot move UPWARDS because there is another box"); // For testing - comment out if needed
                        return false;
                    case 3: // the tile above is an end point
                        this.maze[boxRowNum-1][boxColumnNum] = 5; // marks that a box is on an end point
                        
                        // handles the case that the box is sitting on a current end point
                        if (boxType == 2) {
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else {
                            this.maze[boxRowNum][boxColumnNum] = 3; // restore the end point
                            return true;
                        }
                    case 5: // the tile above is another box
                        // System.out.println("Movement Err: Box cannot move UPWARDS because there is another box"); // For testing - comment out if needed
                        return false;
                }
                
                break;
            case DOWN: // moving the box down
                int rowsMax = this.maze.length; //gets the maximum row length
                
                // check if the box is moving out of bounds
                if (boxRowNum >= rowsMax) {
                    // System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }
                
                tile = this.maze[boxRowNum+1][boxColumnNum]; // gets the tile that is below the box
                switch (tile) {
                    case -1: // the case that the tile is an immovable object
                        // System.out.println("Movement Err: Box cannot move DOWN because there is an immoavable object"); // For testing - comment out if needed
                        return false;
                    case 0: // the case that the tile is an empty object
                        if (boxType == 2) { // default case
                            this.maze[boxRowNum+1][boxColumnNum] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else { // else statement to handle the case that the box is sitting on an end point
                            this.maze[boxRowNum+1][boxColumnNum] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 3;
                            return true;
                        }
                    case 2: // the tile below is another box
                        // System.out.println("Movement Err: Box cannot move DOWN because there is another box"); // For testing - comment out if needed
                        return false;
                    case 3: // the tile above is an end point
                        this.maze[boxRowNum+1][boxColumnNum] = 5; // marks that a box is on an end point
                        
                        // handles the case that the box is sitting on a current end point
                        if (boxType == 2) {
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else {
                            this.maze[boxRowNum][boxColumnNum] = 3; // restore the end point
                            return true;
                        }
                    case 5: // the tile above is another box
                        // System.out.println("Movement Err: Box cannot move UPWARDS because there is another box"); // For testing - comment out if needed
                        return false;
                }
                break;
            case LEFT: // moving the box left - reduce the column number by 1
                // check if the box is moving out of bounds
                if (boxColumnNum == 0) {
                    // System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }
                
                tile = this.maze[boxRowNum][boxColumnNum-1]; // gets the tile that is to the left of the box
                
                switch (tile) {
                    case -1: // case that the tile to the left of the box is an immoavable object
                        // System.out.println("Movement Err: Box cannot move LEFT because there is an immoavable object"); // For testing - comment out if needed
                        return false;
                    case 0: // case tile to the left is an empty object
                        if (boxType == 2) { // default case
                            this.maze[boxRowNum][boxColumnNum-1] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else { // else statement to handle the case that the box is sitting on an end point
                            this.maze[boxRowNum][boxColumnNum-1] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 3; // reset the end point
                            return true;
                        }
                    case 2: // the tile to the left is another box
                        // System.out.println("Movement Err: Box cannot move LEFT because there is another box"); // For testing - comment out if needed
                        return false;
                    case 3: // the tile to the left is an end point
                        this.maze[boxRowNum][boxColumnNum-1] = 5; // marks that a box is on an end point
                        
                        // handles the case that the box is sitting on a current end point
                        if (boxType == 2) {
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else {
                            this.maze[boxRowNum][boxColumnNum] = 3; // restore the end point
                            return true;
                        }
                    case 5: // the tile to the left is another box
                        // System.out.println("Movement Err: Box cannot move LEFT because there is another box"); // For testing - comment out if needed
                        return false;
                }
                break;
            case RIGHT: // moving the box right
                int colsMax = this.maze[0].length; // gets the maximum column length
                
                if (boxColumnNum >= colsMax) {
                    // System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }
                
                tile = this.maze[boxRowNum][boxColumnNum+1]; // gets the tile that is to the right RIGHT of the box
                
                switch(tile) {
                    case -1: // case that the tile to the left of the box is an immoavable object
                        // System.out.println("Movement Err: Box cannot move LEFT because there is an immoavable object"); // For testing - comment out if needed
                        return false;
                    case 0: //case tile to the right is an empty object
                        if (boxType == 2) { // default case
                            this.maze[boxRowNum][boxColumnNum+1] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else { // else statement to handle the case that the box is sitting on an end point
                            this.maze[boxRowNum][boxColumnNum+1] = 2;
                            this.maze[boxRowNum][boxColumnNum] = 3; // reset the end point
                            return true;
                        }
                    case 2: // the tile to the right is another box
                        // System.out.println("Movement Err: Box cannot move RIGHT because there is another box"); // For testing - comment out if needed
                        return false;
                    case 3: // the tile to the right is an end point
                        this.maze[boxRowNum][boxColumnNum+1] = 5; // marks that a box is on an end point
                        
                        // handles the case that the box is sitting on a current end point
                        if (boxType == 2) {
                            this.maze[boxRowNum][boxColumnNum] = 0;
                            return true;
                        } else {
                            this.maze[boxRowNum][boxColumnNum] = 3; // restore the end point
                            return true;
                        }
                    case 5: // the tile to the right is a box on an end point
                        // System.out.println("Movement Err: Box cannot move RIGHT because there is another box"); // For testing - comment out if needed
                        return false;
                }
        }
        
        return false;
    }

    /**
     * Method to print out the entire game map
     */
    public void printGameMap() {
        // nested loop to print out the 2D array
        for (int i = 0 ; i < this.maze.length ; i++) {
            for (int j = 0 ; j < this.maze[i].length ; j++) {
                System.out.format("%5d", this.maze[i][j]);
            }
            System.out.println("");
        }
    }

    public int[][] getMaze() {
        return this.maze;
    }
    
    public int getMoveCount() {
    	return this.move_count;
    }
    
}