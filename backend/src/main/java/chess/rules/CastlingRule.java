package chess.rules;
import chess.model.*;

public class CastlingRule implements GameRule{
    public boolean isApplicable(Board board,Move move){
        Piece p=board.getSquare(move.getFrom()).getPiece();
        if(p==null){
            return false;
        }
        if(!(p instanceof King)){
            return false;
        }
        int dc=Math.abs(move.getTo().getColumn()-move.getFrom().getColumn());
        int dr=Math.abs(move.getTo().getRow()-move.getFrom().getRow());
        if(dr==0 && dc==2){
            return true;
        }
        return false;
    }
    public boolean validate(Board board,Move move){
        if(board.isPathClear(move)==false){
            return false;
        }
        if(board.isOccupied(move.getTo())){
            return false;
        }
        return true;
    }
}


