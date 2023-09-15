package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {

    private int numNodesExpanded;
    private final ToIntFunction<Checkerboard> evaluator;

    public NegaMax(ToIntFunction<Checkerboard> e, int numNodesExpanded, ToIntFunction<Checkerboard> evaluator) {
        super(e);
        this.numNodesExpanded = numNodesExpanded;
        this.evaluator = evaluator;
    }

    @Override
    public int numNodesExpanded() {
        return numNodesExpanded;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        return Optional.empty();
    }

    private int negaMax(Checkerboard board, int depth) {
        numNodesExpanded++;

        if (board.gameOver() || depth == 0)
            return evaluator.applyAsInt(board);


        int max = Integer.MIN_VALUE;

        for (Checkerboard nextBoard : board.getNextBoards()) {
            int value = -negaMax(nextBoard, depth - 1);

            if(max < value)
                max = value;

        }

        return max;
    }
}
