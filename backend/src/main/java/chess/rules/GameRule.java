package chess.rules;
import chess.model.*;

public interface GameRule{
    boolean isApplicable(Board board,Move move);
    boolean validate(Board board,Move move);
}


