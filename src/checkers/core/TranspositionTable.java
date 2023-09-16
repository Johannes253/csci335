package checkers.core;
import java.util.HashMap;

public class TranspositionTable {
    private final HashMap<Integer, TranspositionTableEntry> transpotable = new HashMap<Integer, TranspositionTableEntry>();

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

