package BackEnd;

public class MatrixState {
	private int[][] matrix; // the matrix
	private int numBlocks; // the total number of blocks in the matrix
	
	public MatrixState(int[][] currMatrix, int numBlocks) {
		this.matrix = currMatrix;
		this.numBlocks = numBlocks;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int getNumBlocks() {
		return numBlocks;
	}

	public void incrementNumBlocks() {
		this.numBlocks += 1; // increments the number of boxes
	}
}
