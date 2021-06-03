public class SequenceAlignment {
	protected int match = 1;			// Match score
	protected int mismatch = -1;		// Mismatch score
	protected int gap = -2;				// Gap penalty
	protected String sequence1, sequence2;
	protected String[] alignment;	    // Final alignment of the two sequences
	protected Node[][] score;			// 2D Node array to contain the score for each nucleotide

	/**
	 * Constructor method that takes in all arguments required for sequence alignment.
	 * It will initiate each node of the score matrix and set default scores and previous
	 * nodes to be processed later for scoring.
	 */
	public SequenceAlignment(String sequence1, String sequence2) {
		this.sequence1 = sequence1;
		this.sequence2 = sequence2;
		// Instantiate new scoring matrix with new nodes from data.
		score = new Node[sequence2.length() + 1][sequence1.length() + 1];
		// Insert new nodes for each location.
		for (int i = 0; i < score.length; i++) {
			for (int j = 0; j < score[i].length; j++) {
				score[i][j] = new Node(i, j);
			}
		}
		// Initialize score values and run comparison method.
		setInitialInfo();
		setNodeInfo();
	}
	
	/**
	 * Set the scores for each node and set the previous node pointer.
	 */
	protected void setInitialInfo() {
		for (int i = 0; i < score.length; i++) {
			for (int j = 0; j < score[i].length; j++) {
				score[i][j].setScore(getScores(i, j));
				score[i][j].setPrevNode(getNodes(i, j));
			}
		}
	}
	
	/**
	 * Initiate first row and column scores based on gap penalty.
	 */
	protected int getScores(int row, int col) {
		if (row == 0 && col != 0) {
			return col * gap;
		} else if (col == 0 && row != 0) {
			return row * gap;
		} else {
			return 0;
		}
	}
	
	/**
	 * Get the specified node based on location for initial previous node setup.
	 */
	protected Node getNodes(int row, int col) {
		if (row == 0 && col != 0) {
			return score[row][col - 1];
		} else if (col == 0 && row != 0) {
			return score[row - 1][col];
		} else {
			return null;
		}
	}
	
	/**
	 * Get the current node and get the nodes above, left, and diagonal nodes from it
	 * and apply scoring based on set score setting.
	 */
	protected void setNodeInfo() {
		Node current = null, top = null, left = null, diagonal = null;
		int topGap, leftGap, diagonalScore;
		// Begin running through 2D array of nodes.
		for (int row = 1; row < score.length; row++) {
			for (int col = 1; col < score[row].length; col++) {
				// Obtain all possible scores for current node.
				current = score[row][col];
				top = score[row - 1][col];
				left = score[row][col - 1];
				diagonal = score[row - 1][col - 1];
				// Obtain both possible gap and match scores for current node.
				topGap = top.getScore() + gap;
				leftGap = left.getScore() + gap;
				// Compare diagonal node and determine if match/mismatch and add appropriate score.
				diagonalScore = diagonal.getScore();
				if (sequence2.charAt(current.getRow() - 1) == sequence1.charAt(current.getCol() - 1)) {
					diagonalScore += match;
				} else {
					diagonalScore += mismatch;
				}
				// Compare all scores with each other and determine which is highest and apply to node.
				if (topGap >= leftGap) {
					if (diagonalScore >= topGap) {
						current.setScore(diagonalScore);
						current.setPrevNode(diagonal);
					} else {
						current.setScore(topGap);
						current.setPrevNode(top);
					}
				} else {
					if (diagonalScore >= leftGap) {
						current.setScore(diagonalScore);
						current.setPrevNode(diagonal);
					} else {
						current.setScore(leftGap);
						current.setPrevNode(left);
					}
				}
			}
		}
	}
	
	/**
	 * Trace back from the last node of sequence and follow up with the next
	 * previous node depending on the scoring and this sequence is placed into
	 * a string array.
	 */
	protected String[] traceback() {
		StringBuffer alignment1 = new StringBuffer();
		StringBuffer alignment2 = new StringBuffer();
		String[] alignments;
		Node current = score[score.length - 1][score[0].length - 1];
		// While there is a node available, begin traceback.
		while (!isNull(current)) {
			// Check if current and previous node assigned are left or above each other.
			// If the position of current - previous is 1, then grab the nucleotide, its a mismatch/match (diagonal).
			// Else place a gap.
			if (current.getCol() - current.getPrevNode().getCol() == 1) {
				alignment1.insert(0, sequence1.charAt(current.getCol() - 1));
			} else {
				alignment1.insert(0, '-');
			}
			if (current.getRow() - current.getPrevNode().getRow() == 1) {
				alignment2.insert(0, sequence2.charAt(current.getRow() - 1));
			} else {
				alignment2.insert(0, '-');
			}
			current = current.getPrevNode();
		}
		alignments = new String[] { alignment1.toString(), alignment2.toString() };
		return alignments;
	}

	/**
	 * Check if previous node is null for trace back method.
	 */
	protected boolean isNull(Node current) {
		return current.getPrevNode() == null;
	}

	/**
	 * Public accessible method to begin the trace back and return the
	 * alignment as a string array.
	 */
	public String[] getAlignment() {
		alignment = traceback();
		return alignment;
	}

	/**
	 * Simple method to add up the score of the aligned sequence.
	 */
	public int getAlignmentScore() {
		int current_score = 0;
		for (int i = 0; i < alignment[0].length(); i++) {
			if (alignment[0].charAt(i) == '-' || alignment[1].charAt(i) == '-') {
				current_score += gap;
			} else if (alignment[0].charAt(i) == alignment[1].charAt(i)) {
				current_score += match;
			} else {
				current_score += mismatch;
			}
		}
		return current_score;
	}
}