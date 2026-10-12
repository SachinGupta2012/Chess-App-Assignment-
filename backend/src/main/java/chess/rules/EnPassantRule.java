package chess.rules;
import chess.model.*;

public class EnPassantRule implements GameRule{
    public boolean isApplicable(Board board,Move move){
        Piece p=board.getSquare(move.getFrom()).getPiece();
        if(p==null){
            return false;
        }
        if(!(p instanceof Pawn)){
            return false;
        }
        int dr=Math.abs(move.getTo().getRow()-move.getFrom().getRow());
        int dc=Math.abs(move.getTo().getColumn()-move.getFrom().getColumn());
        if(dr==1 && dc==1 && board.isOccupied(move.getTo())==false){
            return true;
        }
        return false;
    }
    public boolean validate(Board board,Move move){
        int capRow=move.getTo().getRow();
        int capCol=move.getFrom().getColumn();
        Position capPos=new Position(capRow,capCol);
        if(board.isOccupied(capPos)==false){
            return false;
        }
        Piece behind=board.getSquare(capPos).getPiece();
        if(!(behind instanceof Pawn)){
            return false;
        }
        Piece mover=board.getSquare(move.getFrom()).getPiece();
        if(behind.getColor()==mover.getColor()){
            return false;
        }
        return true;
    }
}


