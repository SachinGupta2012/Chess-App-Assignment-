package chess.rules;
import chess.model.*;
import java.util.ArrayList;
import java.util.List;

public class BasicMoveValidator implements MoveValidator{
    private List<GameRule> rules;

    public BasicMoveValidator(){
        rules=new ArrayList<>();
        rules.add(new PromotionRule());
        rules.add(new EnPassantRule());
        rules.add(new CastlingRule());
    }
    public boolean validate(Board board,Move move){
        Position from=move.getFrom();
        Position to=move.getTo();
        Piece p=board.getSquare(from).getPiece();
        if(p==null){
            return false;
        }
        if(p.isValidMove(board,from,to)==false){
            return false;
        }
        for(int i=0;i<rules.size();i++){
            GameRule r=rules.get(i);
            if(r.isApplicable(board,move)){
                if(r.validate(board,move)==false){
                    return false;
                }
            }
        }
        return true;
    }
}


