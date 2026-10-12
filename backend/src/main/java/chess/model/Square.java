package chess.model;

public class Square{
    private Position position;
    private Piece piece;

    public Square(Position position){
        this.position=position;
        this.piece=null;
    }
    public Position getPosition(){
        return position;

    }
    public Piece getPiece(){
        return piece;
    }
    public void setPiece(Piece piece){
        this.piece=piece;
    }

    public boolean isEmpty(){
        return piece==null;

    }

    
}
