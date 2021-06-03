
public class Node {
	private Node prevNode;
	private int score;
	private int row;
	private int col;

	/**
	 * Constructor for the node object, takes passed row and column information and stores.
	 */
	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setPrevNode(Node node) {
		this.prevNode = node;
	}

	public Node getPrevNode() {
		return prevNode;
	}
}