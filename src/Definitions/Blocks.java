package Definitions;

import java.util.Hashtable;

/**
 * Created by altuz on 20/05/17.
 * Definitions for game cells.
 * @author Nicholas Mulianto
 */
public enum Blocks {
    PLAYER(1),
    BOXES(2),
    SPACES(0),
    IMMOVABLES(-1),
    END_POINTS(3),
    END_PLAYER(4),
    END_BOXES(5);

    private int val;
    Blocks(int v){ this.val = v; }
    public int getVal(){ return this.val; }
    public static final Blocks values[] = values();

    private static final Hashtable<Integer, Blocks> lookup = new Hashtable<>();
    static {
        for(Blocks b : Blocks.values()){
            lookup.put(b.getVal(), b);
        }
    }
    public static Blocks get(int i){ return lookup.get(i); }
}
