package checkers.evaluators;

import checkers.core.Checkerboard;
import checkers.core.PlayerColor;

import java.util.function.ToIntFunction;

public class ImprovedEvaluator implements ToIntFunction<Checkerboard> {

    private static final int valuePiece = 10;
    private static final int valueKing = 20;
    private static final int valuePos = 2;
    private static final int valueMobility = 5;
    @Override
    public int applyAsInt(Checkerboard board) {
        PlayerColor currentPlayer = board.getCurrentPlayer();
        PlayerColor enemy;

        if(currentPlayer == PlayerColor.RED)
            enemy = PlayerColor.BLACK;
        else
            enemy = PlayerColor.RED;

        int currentPlayerValue = board.numPiecesOf(currentPlayer) * valuePiece +
                board.numKingsOf(currentPlayer) * valueKing +
                getValuePos(board, currentPlayer) + mobilityBonus(board, currentPlayer);

        int enemyValue = board.numPiecesOf(enemy) * valuePiece +
                board.numKingsOf(enemy) * valueKing +
                getValuePos(board, enemy) + mobilityBonus(board, currentPlayer);

        return currentPlayerValue - enemyValue;
    }

    private int getValuePos(Checkerboard board, PlayerColor currentPlayer) {
        int posValue = 0;
        for (int r = board.minRow(); r <= board.maxRow(); r++) {
            for (int c = board.minCol(); c <= board.maxCol(); c++) {
                if(board.colorAt(r, c, currentPlayer))
                 posValue += (currentPlayer == PlayerColor.RED) ? (board.maxRow() - r) * valuePos : r * valuePos;
            }

            }
        return posValue;
    }
    private int mobilityBonus(Checkerboard board, PlayerColor player) {
        return board.getLegalMoves(player).size() * valueMobility;
    }
}
