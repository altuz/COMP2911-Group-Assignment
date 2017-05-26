package BackEnd;

import Definitions.Blocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Nicholas Mulianto on 22/05/17.
 */
public class MazeState {
    private int[][] mx;
    private Set<int[]> border;
    private Set<int[]> inner;
    private ArrayList<int[]> box_loc;

    public MazeState(int[][] m){
        this.mx = matrixDeepCopy(m);
        this.border = new HashSet<>();
        this.inner = new HashSet<>();
        this.box_loc = new ArrayList<int[]>();
    }

    public int[][] getMat() { return this.mx; }
    public Set<int[]> getBorders() { return this.border; }
    public Set<int[]> getInner() { return this.inner; }
    public void setBorders(Set<int[]> h) { this.border.addAll(h); }
    public void setInner(Set<int[]> h) { this.inner.addAll(h); }
    public void setBoxes(ArrayList<int[]> h) { this.box_loc.addAll(h); }
    public void expandBorder(int[] i) { this.border.add(i); }
    public int getBoxNum() { return this.box_loc.size(); }
    public ArrayList<int[]> getBoxes() { return this.box_loc; }
    public int getInnNum() { return this.inner.size(); }
    public void addBox(int[] i) {
        this.box_loc.add(i);
        //this.mx[i[0]][i[1]] = Blocks.BOXES.getVal();
    }

    public int[] randomBorder(ThreadLocalRandom j) {
        int i = j.nextInt(0, border.size());
        Iterator<int[]> it = border.iterator();
        while(true){
            if(i == 0) return it.next();
            it.next();
            i--;
        }
    }

    public void removeBorder(int[] i) {
        this.border.remove(i);
    }

    public void removeInner(int[] i) {
        this.inner.remove(i);
    }

    public void addInner(int[] i){
        this.inner.add(i);
        this.mx[i[0]][i[1]] = Blocks.SPACES.getVal();
    }
    /**
     * Creates deep copy of the matrix.
     * In case we fuck up during randomization, better to start fresh
     * @author Nicholas Mulianto
     * @return
     */
    private int[][] matrixDeepCopy(int[][] src){
        int[][] mxcp = new int[src.length][src.length];
        for(int i = 0; i < src.length; i++)
            for(int j = 0; j < src.length; j++)
                mxcp[i][j] = src[i][j];
        return mxcp;
    }

    public int[] randomInner(ThreadLocalRandom j) {
        int i = j.nextInt(0, inner.size());
        Iterator<int[]> it = inner.iterator();
        while(true){
            if(i == 0) {
                int[] n = it.next();
                if(blContains(n)){
                    return randomInner(j);
                }
                return n;
            }
            it.next();
            i--;
        }
    }
    
    public boolean blContains(int[] n){
    	for(int[] i : this.box_loc){
    		if(i[0] == n[0] && i[1] == n[1])
    			return true;
    	}
    	return false;
    }
}
