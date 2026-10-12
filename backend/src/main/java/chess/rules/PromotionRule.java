package chess.rules;
import chess.model.*;
import chess.enums.Color;

public class PromotionRule implements GameRule{
    public boolean isApplicable(Board board,Move move){
        Piece p=board.getSquare(move.getFrom()).getPiece();
        if(p==null){
            return false;
        }
        if(!(p instanceof Pawn)){
            return false;
        }
        int toRow=move.getTo().getRow();
        if(p.getColor()==Color.WHITE && toRow==7){
            return true;
        }
        if(p.getColor()==Color.BLACK && toRow==0){
            return true;
        }
        return false;
    }
    public boolean validate(Board board,Move move){
        return true;
    }
}


