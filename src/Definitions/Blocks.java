package Definitions;

/**
 * Created by altuz on 20/05/17.
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
}
