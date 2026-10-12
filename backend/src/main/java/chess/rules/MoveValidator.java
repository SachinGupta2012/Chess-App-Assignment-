package chess.rules;
import chess.model.*;

public interface MoveValidator{
    boolean validate(Board board,Move move);
}


