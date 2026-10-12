package chess.rules;
import chess.model.*;
import chess.enums.GameStatus;
import chess.enums.Color;

public interface GameStateEvaluator{
    GameStatus evaluate(Board board,Color color);
}



