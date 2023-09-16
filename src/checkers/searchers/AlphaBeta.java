package checkers.searchers;

import checkers.core.Checkerboard;
import checkers.core.CheckersSearcher;
import checkers.core.Move;
import checkers.core.PlayerColor;
import core.Duple;

import java.util.Optional;
import java.util.function.ToIntFunction;

public class AlphaBeta extends CheckersSearcher {

    private int numNodesExpanded;
    private final ToIntFunction<Checkerboard> evaluator;

    public AlphaBeta(ToIntFunction<Checkerboard> e) {
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
        int alpha = Integer.MAX_VALUE;
        int beta = Integer.MIN_VALUE;


        for(Move move : board.getLegalMoves(currentPlayer)) {
            Checkerboard nextBoardMove = board.duplicate();
            nextBoardMove.move(move);

            int depth = getDepthLimit();
            int value = -alphaBeta(nextBoardMove, depth - 1, alpha, beta);


            if (bestMoveValue < value) {
                bestMoveValue = value;
                bestMove = move;
            }
            if(value > alpha)
                alpha = value;
        }

            return (bestMove == null) ? Optional.empty() : Optional.of(new Duple<>(bestMoveValue, bestMove));
    }

    private int alphaBeta(Checkerboard board, int depth, int alpha, int beta) {
        numNodesExpanded++;
        int max = Integer.MIN_VALUE;

        if (board.gameOver() || depth == 0)
            return evaluator.applyAsInt(board);




        for (Checkerboard nextBoard : board.getNextBoards()) {
            int value = -alphaBeta(nextBoard, depth - 1, alpha, beta);

            if(max < value)
                max = value;

            if(value < alpha)
                alpha = value;

            if(alpha >= beta)
                break;
        }

        return max;
    }
}
