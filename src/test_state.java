import GameLogic.State;

/**
 * Created by altuz on 20/05/17.
 */
public class test_state {
    public static void main(String args[]){
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

//        int[][] reachability = s.exploreReachability();
//        for(int i = 0; i < maze.length; i++){
//            for(int j = 0; j < maze.length; j++){
//                System.out.printf("%3d\t", reachability[i][j]);
//            }
//            System.out.println();
//        }
        s.shuffleLevel(10);
    }
}
