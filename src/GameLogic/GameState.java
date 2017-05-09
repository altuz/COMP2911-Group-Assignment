package GameLogic;
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
        this.goal_blocks= level.getGoals();
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
     * @return True if the move was successful
     * 		   False if the move was unsuccessful
     *
     * @author James Ren
     */
    private boolean move_up() {
        int currLocation[] = this.player_loc; // gets the player location
        int rowNum = currLocation[0]; // gets the row that the player is standing on
        int columnNum = currLocation[1]; // gets the column that the player is standing on
        int boxRowNum = 0;

        // Handles the case that the player wants to move out of bounds
        if(rowNum == 0) {
            System.out.println("Movement Err: Moving out of bounds");
            return false;
        }

        int blockNum = this.maze[rowNum-1][columnNum]; // gets the block status above the player's position
		/*
		 * Gets the tile that the player current standing on:
		 * -> 1 == player is currently standing on a blank tile
		 * -> 4 == player is currently standing on an end point
		 */
        int playerCurrPosition = this.maze[rowNum][columnNum];

        switch (blockNum) {
            case -1: // the block above is an immovable object
                System.out.println("Movement Err: Cannot move UPWARDS because there is an immovable object");
                return false;
            case 0: // the block above is an empty tile
                if (playerCurrPosition == 1) { // handles the case that the player is standing on an empty tile
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum-1][columnNum] = 1; // move the player to the tile above
                    this.player_loc[0] = rowNum-1; // update the player's location
                } else {
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum-1][columnNum] = 1;
                    this.player_loc[0] = rowNum-1;
                }
                break;
            case 2: // the block above is a box
                boxRowNum = rowNum-1;

                // handles the case that the box movement is successful
                if (moveBox(columnNum, boxRowNum, Movement.UP)) {
                    if (playerCurrPosition == 1) {
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum-1][columnNum] = 1; // move the player to the tile above
                        this.player_loc[0] = rowNum-1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum-1][columnNum] = 1;
                        this.player_loc[0] = rowNum-1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
            case 3: // handles the case that the tile above is an end point
                if (playerCurrPosition == 1) {
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum-1][columnNum] = 4; // move the player to the tile above
                    this.player_loc[0] = rowNum-1; // update the player's location
                } else { // the player is currently standing on an end point
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum-1][columnNum] = 4;
                    this.player_loc[0] = rowNum-1;
                }
            case 5: // handles the case that that the tile above has a box on an end point
                boxRowNum = rowNum-1;

                // handles the case that the box movement is successful
                if (moveBox(boxRowNum, rowNum, Movement.UP)) {
                    if (playerCurrPosition == 1) { // default case
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum-1][columnNum] = 1; // move the player to the tile above
                        this.player_loc[0] = rowNum-1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum-1][columnNum] = 1;
                        this.player_loc[0] = rowNum-1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }

        }

        return true;
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
        int columnNum = currLocation[1]; // gets the column that the player is standing on
        int rowMax = this.maze.length; // gets the maximum row length of the 2D matrix
        int boxRowNum = 0;

        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(rowNum >= rowMax) {
            System.out.println("Movement Err: Moving out of bounds");
            return false;
        }

        int blockNum = this.maze[rowNum+1][columnNum]; // gets the block status below the player's position
		/*
		 * Gets the tile that the player current standing on:
		 * -> 1 == player is currently standing on a blank tile
		 * -> 4 == player is currently standing on an end point
		 */
        int playerCurrPosition = this.maze[rowNum][columnNum];

        switch (blockNum) {
            case -1: // the block below is an immovable object
                System.out.println("Movement Err: Cannot move UPWARDS because there is an immovable object");
                return false;
            case 0: // the block below is an empty tile
                if (playerCurrPosition == 1) { // handles the case that the player is standing on an empty tile
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum+1][columnNum] = 1; // move the player to the tile below
                    this.player_loc[0] = rowNum+1; // update the player's location
                } else {
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum+1][columnNum] = 1;
                    this.player_loc[0] = rowNum+1;
                }
                break;
            case 2: // the block below is a box
                // check that we can move the block
                boxRowNum = rowNum+1;
                // handles the case that the box movement is successful
                if (moveBox(columnNum, boxRowNum, Movement.DOWN)) {
                    if (playerCurrPosition == 1) {
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum+1][columnNum] = 1; // move the player to the tile above
                        this.player_loc[0] = rowNum+1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum+1][columnNum] = 1;
                        this.player_loc[0] = rowNum+1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
            case 3: // handles the case that the tile above is an end point
                if (playerCurrPosition == 1) {
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum+1][columnNum] = 4; // move the player to the tile above
                    this.player_loc[0] = rowNum+1;  // update the player's location
                } else { // the player is currently standing on an end point
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum+1][columnNum] = 4;
                    this.player_loc[0] = rowNum+1;
                }
            case 5: // handles the case that that the tile above has a box on an end point
                boxRowNum = rowNum+1;

                // handles the case that the box movement is successful
                if (moveBox(columnNum, boxRowNum, Movement.DOWN)) {
                    if (playerCurrPosition == 1) { // default case
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum+1][columnNum] = 1; // move the player to the tile above
                        this.player_loc[0] = rowNum+1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum+1][columnNum] = 1;
                        this.player_loc[0] = rowNum+1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
        }

        return true;
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
        int columnNum = currLocation[1]; // gets the column that the player is standing on
        int boxColNum = 0;

        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(columnNum == 0) {
            System.out.println("Movement Err: Moving out of bounds");
            return false;
        }

        int blockNum = this.maze[rowNum][columnNum-1]; // gets the block status to the left of player's position
		/*
		 * Gets the tile that the player current standing on:
		 * -> 1 == player is currently standing on a blank tile
		 * -> 4 == player is currently standing on an end point
		 */
        int playerCurrPosition = this.maze[rowNum][columnNum];

        switch (blockNum) {
            case -1: // the block to the left is an immovable object
                System.out.println("Movement Err: Cannot move UPWARDS because there is an immovable object");
                return false;
            case 0: // the block to the left is an empty tile
                if (playerCurrPosition == 1) { // handles the case that the player is standing on an empty tile
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum][columnNum-1] = 1; // move the player to the tile to the left
                    this.player_loc[1] = columnNum-1; // update the player's location
                } else {
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum][columnNum-1] = 1;
                    this.player_loc[1] = columnNum-1;
                }
                break;
            case 2: // the block to the left is a box
                // check that we can move the block
                boxColNum = columnNum-1;
                // handles the case that the box movement is successful
                if (moveBox(boxColNum,rowNum, Movement.LEFT)) {
                    if (playerCurrPosition == 1) {
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum][columnNum-1] = 1; // move the player to the tile above
                        this.player_loc[1] = columnNum-1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum][columnNum-1] = 1;
                        this.player_loc[1] = columnNum-1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
            case 3: // handles the case that the tile above is an end point
                if (playerCurrPosition == 1) {
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum][columnNum-1] = 4; // move the player to the tile above
                    this.player_loc[1] = columnNum-1; // update the player's location
                } else { // the player is currently standing on an end point
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum][columnNum-1] = 4;
                    this.player_loc[1] = columnNum-1;
                }
            case 5: // handles the case that that the tile above has a box on an end point
                boxColNum = columnNum-1;

                // handles the case that the box movement is successful
                if (moveBox(boxColNum,rowNum, Movement.LEFT)) {
                    if (playerCurrPosition == 1) { // default case
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum][columnNum-1] = 1; // move the player to the tile above
                        this.player_loc[1] = columnNum-1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum][columnNum-1] = 1;
                        this.player_loc[1] = columnNum-1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
        }

        return true;
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
        int columnNum = currLocation[1]; // gets the column that the player is standing on
        int colMax = this.maze[0].length; //gets the maximum column size
        int boxColNum = 0;

        // Handles the case that the player wants to move out of bounds
        // To counter null pointer exceptions
        if(columnNum >= colMax) {
            System.out.println("Movement Err: Moving out of bounds");
            return false;
        }

        int blockNum = this.maze[rowNum][columnNum+1]; // gets the block status to the left of player's position
		/*
		 * Gets the tile that the player current standing on:
		 * -> 1 == player is currently standing on a blank tile
		 * -> 4 == player is currently standing on an end point
		 */
        int playerCurrPosition = this.maze[rowNum][columnNum];

        switch (blockNum) {
            case -1: // the block to the left is an immovable object
                System.out.println("Movement Err: Cannot move UPWARDS because there is an immovable object");
                return false;
            case 0: // the block to the left is an empty tile
                if (playerCurrPosition == 1) { // handles the case that the player is standing on an empty tile
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum][columnNum+1] = 1; // move the player to the tile to the left
                    this.player_loc[1] = columnNum+1; // update the player's location
                } else {
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum][columnNum+1] = 1;
                    this.player_loc[1] = columnNum+1;
                }
                break;
            case 2: // the block to the left is a box
                // check that we can move the block
                boxColNum = columnNum+1;
                // handles the case that the box movement is successful
                if (moveBox(boxColNum,rowNum, Movement.RIGHT)) {
                    if (playerCurrPosition == 1) {
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum][columnNum+1] = 1; // move the player to the tile above
                        this.player_loc[1] = columnNum+1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum][columnNum+1] = 1;
                        this.player_loc[1] = columnNum+1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
            case 3: // handles the case that the tile above is an end point
                if (playerCurrPosition == 1) {
                    this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                    this.maze[rowNum][columnNum+1] = 4; // move the player to the tile above
                    this.player_loc[1] = columnNum+1; // update the player's location
                } else { // the player is currently standing on an end point
                    this.maze[rowNum][columnNum] = 3; // reset end point
                    this.maze[rowNum][columnNum+1] = 4;
                    this.player_loc[1] = columnNum+1;
                }
            case 5: // handles the case that that the tile above has a box on an end point
                boxColNum = columnNum+1;

                // handles the case that the box movement is successful
                if (moveBox(boxColNum,rowNum, Movement.RIGHT)) {
                    if (playerCurrPosition == 1) { // default case
                        this.maze[rowNum][columnNum] = 0; // change the player's current position in the maze to a blank tile
                        this.maze[rowNum][columnNum+1] = 1; // move the player to the tile above
                        this.player_loc[1] = columnNum+1; // update the player's location
                    }
                    else if (playerCurrPosition == 4) {
                        this.maze[rowNum][columnNum] = 3; // reset end point
                        this.maze[rowNum][columnNum+1] = 1;
                        this.player_loc[1] = columnNum+1;
                    }
                    break;
                } else { // box movement was not successful
                    System.out.println("Movement Err: Cannot push box");
                    return false;
                }
        }

        return true;
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
    private boolean moveBox(int boxColumnNum, int boxRowNum, Movement dir) {
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
                        System.out.println("Movement Err: Box cannot move UPWARDS because there is another box"); // For testing - comment out if needed
                        return false;
                }

                break;
            case DOWN: // moving the box down
                int rowsMax = this.maze.length; //gets the maximum row length

                // check if the box is moving out of bounds
                if (boxRowNum >= rowsMax) {
                    System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }

                tile = this.maze[boxRowNum+1][boxColumnNum]; // gets the tile that is below the box
                switch (tile) {
                    case -1: // the case that the tile is an immovable object
                        System.out.println("Movement Err: Box cannot move UPWARDS because there is an immoavable object"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move DOWN because there is another box"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move UPWARDS because there is another box"); // For testing - comment out if needed
                        return false;
                }
                break;
            case LEFT: // moving the box left - reduce the column number by 1
                // check if the box is moving out of bounds
                if (boxColumnNum == 0) {
                    System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }

                tile = this.maze[boxRowNum][boxColumnNum-1]; // gets the tile that is to the left of the box

                switch (tile) {
                    case -1: // case that the tile to the left of the box is an immoavable object
                        System.out.println("Movement Err: Box cannot move LEFT because there is an immoavable object"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move LEFT because there is another box"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move LEFT because there is another box"); // For testing - comment out if needed
                        return false;
                }
                break;
            case RIGHT: // moving the box right
                int colsMax = this.maze[0].length; // gets the maximum column length

                if (boxColumnNum >= colsMax) {
                    System.out.println("Movement Err: Moving out of bounds");
                    return false;
                }

                tile = this.maze[boxRowNum][boxColumnNum+1]; // gets the tile that is to the right RIGHT of the box

                switch(tile) {
                    case -1: // case that the tile to the left of the box is an immoavable object
                        System.out.println("Movement Err: Box cannot move LEFT because there is an immoavable object"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move RIGHT because there is another box"); // For testing - comment out if needed
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
                        System.out.println("Movement Err: Box cannot move RIGHT because there is another box"); // For testing - comment out if needed
                        return false;
                }
        }

        return false;
    }

    public int[][] getMaze() {
        return this.maze;
    }
}