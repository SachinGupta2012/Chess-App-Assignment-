package chess.model;
import chess.enums.Color;
import java.util.List;


public abstract class Piece{
    private Color color;

    public Piece(Color color){
        this.color=color;
    }
    public Color getColor(){
        return color;
    }
    
    public abstract boolean isValidMove(Board board, Position from, Position to);
    public abstract List<Position> getAvailableMoves(Board board,Position from);
}

