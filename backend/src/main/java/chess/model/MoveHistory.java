package chess.model;
import java.util.ArrayList;
import java.util.List;

public class MoveHistory{
    private List<Move> moves;

    public MoveHistory(){
        moves=new ArrayList<>();
    }
    public void add(Move move){
        moves.add(move);
    }
    public Move undo(){
        if(moves.size()==0){
            return null;
        }
        Move last=moves.get(moves.size()-1);
        moves.remove(moves.size()-1);
        return last;
    }
    public Move getLastMove(){
        if(moves.size()==0){
            return null;
        }
        return moves.get(moves.size()-1);
    }
    public List<Move> getMoves(){
        return moves;
    }
}

