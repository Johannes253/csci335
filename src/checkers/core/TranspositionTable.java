package checkers.core;
import java.util.HashMap;

public class TranspositionTable {
    private final HashMap<Integer, TranspositionTableEntry> transpotable;

    public TranspositionTable(HashMap<Integer, TranspositionTableEntry> transpotable) {
        this.transpotable = transpotable;
    }

    public boolean containsKey(Checkerboard board) {
        return transpotable.containsKey(board.hashCode());
    }

    public void insertEntry(Checkerboard board, int score, int depth) {
        transpotable.put(board.hashCode(), new TranspositionTableEntry(score, depth));
    }

    public TranspositionTableEntry getEntry(Checkerboard board) {
        return transpotable.get(board.hashCode());
    }


}

class TranspositionTableEntry {
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