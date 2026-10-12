package chess.model;
import chess.enums.Color;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Color color){
        super(color);
    }
    public boolean isValidMove(Board board,Position from,Position to){
        int dir=1;
        int startRow=1;
        if(getColor()==Color.BLACK){
            dir=-1;
            startRow=6;
        }
        Piece dest=board.getSquare(to).getPiece();
        if(to.getColumn()==from.getColumn()){
            if(to.getRow()==from.getRow()+dir && dest==null){
                return true;
            }
            if(from.getRow()==startRow && to.getRow()==from.getRow()+2*dir && dest==null){
                Position mid=new Position(from.getRow()+dir,from.getColumn());
                if(board.isOccupied(mid)==false){
                    return true;
                }
            }
            return false;
        }
        if(to.getRow()==from.getRow()+dir){
            int dc=Math.abs(to.getColumn()-from.getColumn());
            if(dc==1){
                if(dest!=null && dest.getColor()!=getColor()){
                    return true;
                }
            }
        }
        return false;
    }
    public List<Position> getAvailableMoves(Board board,Position from){
        List<Position> list=new ArrayList<>();
        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){
                Position to=new Position(r,c);
                if(isValidMove(board,from,to)){
                    list.add(to);
                }
            }
        }
        return list;
    }
}


