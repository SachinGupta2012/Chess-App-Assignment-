package chess.rules;
import chess.model.*;
import chess.enums.GameStatus;
import chess.enums.Color;

public class StandardGameStateEvaluator implements GameStateEvaluator{
    public GameStatus evaluate(Board board,Color color){
        boolean inCheck=isKingInCheck(board,color);
        boolean hasMove=hasAnySafeMove(board,color);
        if(inCheck && hasMove==false){
            return GameStatus.CHECKMATE;
        }
        if(inCheck==false && hasMove==false){
            return GameStatus.STALEMATE;
        }
        if(inCheck){
            return GameStatus.CHECK;
        }
        return GameStatus.IN_PROGRESS;
    }

    public boolean isKingInCheck(Board board,Color color){
        Position kingPos=null;
        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){
                Position p=new Position(r,c);
                Piece pc=board.getSquare(p).getPiece();
                if(pc!=null && pc instanceof King && pc.getColor()==color){
                    kingPos=p;
                }
            }
        }
        if(kingPos==null){
            return true;
        }
        Color enemy=Color.WHITE;
        if(color==Color.WHITE){
            enemy=Color.BLACK;
        }
        return isSquareAttacked(board,kingPos,enemy);
    }

    public boolean isSquareAttacked(Board board,Position target,Color byColor){
        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){
                Position from=new Position(r,c);
                Piece pc=board.getSquare(from).getPiece();
                if(pc!=null && pc.getColor()==byColor){
                    if(attacks(board,from,target,pc)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasAnySafeMove(Board board,Color color){
        for(int r=0;r<8;r++){
            for(int c=0;c<8;c++){
                Position from=new Position(r,c);
                Piece pc=board.getSquare(from).getPiece();
                if(pc!=null && pc.getColor()==color){
                    for(int tr=0;tr<8;tr++){
                        for(int tc=0;tc<8;tc++){
                            Position to=new Position(tr,tc);
                            if(pc.isValidMove(board,from,to)){
                                if(isSafeAfterMove(board,from,to,color)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isSafeAfterMove(Board board,Position from,Position to,Color color){
        Square fromSq=board.getSquare(from);
        Square toSq=board.getSquare(to);
        Piece moving=fromSq.getPiece();
        Piece captured=toSq.getPiece();
        toSq.setPiece(moving);
        fromSq.setPiece(null);
        boolean safe=isKingInCheck(board,color)==false;
        fromSq.setPiece(moving);
        toSq.setPiece(captured);
        return safe;
    }

    private boolean attacks(Board board,Position from,Position target,Piece piece){
        if(piece instanceof Pawn){
            int dir=1;
            if(piece.getColor()==Color.BLACK){
                dir=-1;
            }
            int row=from.getRow()+dir;
            int colDiff=Math.abs(target.getColumn()-from.getColumn());
            return target.getRow()==row && colDiff==1;
        }
        if(piece instanceof King){
            int dr=Math.abs(target.getRow()-from.getRow());
            int dc=Math.abs(target.getColumn()-from.getColumn());
            return dr<=1 && dc<=1 && (dr+dc)>0;
        }
        return piece.isValidMove(board,from,target);
    }
}



