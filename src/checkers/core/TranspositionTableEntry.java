package checkers.core;

public class TranspositionTableEntry {
    private final int depth;
    private final int score;

    public TranspositionTableEntry(int score, int depth) {
        this.score = score;
        this.depth = depth;
    }

    public int getScore() {
        return score;
    }

    public int getDepth() {
        return depth;
    }
}
