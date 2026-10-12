package chess.model;
import chess.enums.Color;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{
    public Queen(Color color){
        super(color);
    }
    public boolean isValidMove(Board board,Position from,Position to){
        if(from.getRow()==to.getRow() && from.getColumn()==to.getColumn()){
            return false;
        }
        Piece dest=board.getSquare(to).getPiece();
        if(dest!=null && dest.getColor()==getColor()){
            return false;
        }
        int dr=Math.abs(to.getRow()-from.getRow());
        int dc=Math.abs(to.getColumn()-from.getColumn());
        boolean straight=false;
        if(from.getRow()==to.getRow() || from.getColumn()==to.getColumn()){
            straight=true;
        }
        boolean diagonal=false;
        if(dr==dc){
            diagonal=true;
        }
        if(straight==false && diagonal==false){
            return false;
        }
        Move m=new Move(from,to);
        if(board.isPathClear(m)==false){
            return false;
        }
        return true;
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


