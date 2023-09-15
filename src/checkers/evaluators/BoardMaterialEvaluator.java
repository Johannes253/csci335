package checkers.evaluators;
import checkers.core.Checkerboard;
import checkers.core.PlayerColor;
import java.util.function.ToIntFunction;

public class BoardMaterialEvaluator implements ToIntFunction<Checkerboard> {
    @Override
    public int applyAsInt(Checkerboard board) {
        PlayerColor currentPlayer = board.getCurrentPlayer();
        PlayerColor enemy;
        if(currentPlayer == PlayerColor.RED)
            enemy = PlayerColor.BLACK;
        else
            enemy = PlayerColor.RED;

        int pieceDifference = board.numPiecesOf(currentPlayer) - board.numPiecesOf(enemy);

        return pieceDifference;
    }
}
