package checkers.core;



public class TranspositionTableEntry {
    private final int depth;
    private final int score;
    private final BoundType boundType;

    public TranspositionTableEntry(int score, int depth, BoundType boundType) {
        this.score = score;
        this.depth = depth;
        this.boundType = boundType;
    }

    public int getScore() {
        return score;
    }

    public int getDepth() {
        return depth;
    }

    public BoundType getBoundType() {
        return boundType;
    }
}