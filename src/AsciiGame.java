import Definitions.Movement;
import GameLogic.*;

import java.util.Scanner;

/**
 * Created by altuz on 8/05/17.
 */
public class AsciiGame {
    public static void main(String args[]) {
        // change the path to the test_maze as necessary.
        // System.out.println("mod neg = " + (-1 % 5));
        GameState test_game = new GameState(8, 4);
        Scanner in = new Scanner(System.in);
        print_maze(test_game.getMaze());
        while (!test_game.game_over()) {
            char key_input = in.nextLine().toUpperCase().charAt(0);
            // w a s d for movement
            switch (key_input) {
                case 'W' : test_game.player_move(Movement.UP); break;
                case 'A' : test_game.player_move(Movement.LEFT); break;
                case 'S' : test_game.player_move(Movement.DOWN); break;
                case 'D' : test_game.player_move(Movement.RIGHT); break;
                case 'E' : test_game.undo_move(); break;
            }
            System.out.println("--------------------------------------------------------------------------");
            System.out.println("----------------------------MOVING PLAYER---------------------------------");
            System.out.println("--------------------------------------------------------------------------");
            print_maze(test_game.getMaze());
        }
        System.out.println("GAME FINISHED");
    }

    public static void print_maze(int[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                System.out.printf("%2d ", maze[i][j]);
            }
            System.out.println();
        }
    }
}
