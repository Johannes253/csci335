package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import checkers.gui.Checkers;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class NegaMax extends CheckersSearcher {

    private int numNodesExpanded;
    private final ToIntFunction<Checkerboard> evaluator;

    public NegaMax(ToIntFunction<Checkerboard> e) {
        super(e);
        this.evaluator = e;
    }


    @Override
    public int numNodesExpanded() {
        return numNodesExpanded;
    }

    @Override
    public Optional<Duple<Integer, Move>> selectMove(Checkerboard board) {
        Move bestMove = null;
        int bestMoveValue = Integer.MIN_VALUE;
        PlayerColor currentPlayer = board.getCurrentPlayer();

        for(Move move : board.getLegalMoves(currentPlayer)) {
            Checkerboard nextBoardMove = board.duplicate();
            nextBoardMove.move(move);

            int depth = getDepthLimit();
            int value = -negaMax(nextBoardMove, depth - 1);


            if (bestMoveValue < value) {
                bestMoveValue = value;
                bestMove = move;
            }
        }

            return (bestMove == null) ? Optional.empty() : Optional.of(new Duple<>(bestMoveValue, bestMove));
    }

    private int negaMax(Checkerboard board, int depth) {
        numNodesExpanded++;
        System.out.println("Current Depth: " + depth);
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
