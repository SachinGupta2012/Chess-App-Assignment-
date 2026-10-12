package chess.model;
import chess.enums.MoveType;

public class Move{
    private Position from;
    private Position to;
    private Piece piece;
    private Piece capturedPiece;
    private MoveType moveType;

    public Move(Position from, Position to){
        this.from=from;
        this.to=to;
        this.piece=null;
        this.capturedPiece=null;
        this.moveType=MoveType.NORMAL;
    }

    public Move(Position from, Position to, Piece piece, Piece capturedPiece, MoveType moveType){
        this.from=from;
        this.to=to;
        this.piece=piece;
        this.capturedPiece=capturedPiece;
        this.moveType=moveType;
    }

    public Position getFrom(){
        return from;

    }
    public Position getTo(){
        return to;

    }
    public Piece getPiece(){
        return piece;
    }
    public Piece getCapturedPiece(){
        return capturedPiece;
    }
    public MoveType getMoveType(){
        return moveType;
    }

}

