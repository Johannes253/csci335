package checkers.core;

import java.util.HashMap;

public class TranspositionTable {

    private final HashMap<Integer, TranspositionTableEntry> transpoTable = new HashMap<>();

    public boolean containsKey(Checkerboard board) {
        return transpoTable.containsKey(board.hashCode());
    }

    public void insertEntry(Checkerboard board, int score, int depth, BoundType boundType) {
        transpoTable.put(board.hashCode(), new TranspositionTableEntry(score, depth, boundType));
    }

    public TranspositionTableEntry getEntry(Checkerboard board) {
        return transpoTable.get(board.hashCode());
    }
}