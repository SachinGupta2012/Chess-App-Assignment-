package chess.model;
import chess.enums.Color;
import chess.enums.PieceType;

public class Board{
    private Square[][] squares;

    public Board(){
        squares=new Square[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                squares[i][j]=new Square(new Position(i,j));
            }
        }
    }
    public Square getSquare(Position position){
        return squares[position.getRow()][position.getColumn()];

    }
    public boolean isOccupied(Position position){
        return !getSquare(position).isEmpty();
        
    }
    public void movePiece(Move move){
        Square fromSq = getSquare(move.getFrom());
        Square toSq = getSquare(move.getTo());
        toSq.setPiece(fromSq.getPiece());
        fromSq.setPiece(null);
    }
    public void moveRookForCastling(Move move){
        int row=move.getFrom().getRow();
        if(move.getTo().getColumn()==6){
            getSquare(new Position(row,5)).setPiece(getSquare(new Position(row,7)).getPiece());
            getSquare(new Position(row,7)).setPiece(null);
        }else{
            getSquare(new Position(row,3)).setPiece(getSquare(new Position(row,0)).getPiece());
            getSquare(new Position(row,0)).setPiece(null);
        }
    }
    public void undoRookForCastling(Move move){
        int row=move.getFrom().getRow();
        if(move.getTo().getColumn()==6){
            getSquare(new Position(row,7)).setPiece(getSquare(new Position(row,5)).getPiece());
            getSquare(new Position(row,5)).setPiece(null);
        }else{
            getSquare(new Position(row,0)).setPiece(getSquare(new Position(row,3)).getPiece());
            getSquare(new Position(row,3)).setPiece(null);
        }
    }
    public void setupStart(){
        String[] layout={
            "RNBQKBNR",
            "PPPPPPPP",
            "........",
            "........",
            "........",
            "........",
            "pppppppp",
            "rnbqkbnr"
        };
        for(int r=0;r<8;r++){
            String row=layout[r];
            for(int c=0;c<8;c++){
                char ch=row.charAt(c);
                if(ch=='.'){
                    continue;
                }
                Color color;
                if(ch>=65 && ch<=90){
                    color=Color.WHITE;
                }else{
                    color=Color.BLACK;
                }
                char low=Character.toLowerCase(ch);
                PieceType type=typeFromChar(low);
                if(type!=null){
                    getSquare(new Position(r,c)).setPiece(PieceFactory.createPiece(type,color));
                }
            }
        }
    }
    private PieceType typeFromChar(char ch){
        if(ch=='p'){
            return PieceType.PAWN;
        }
        if(ch=='r'){
            return PieceType.ROOK;
        }
        if(ch=='n'){
            return PieceType.KNIGHT;
        }
        if(ch=='b'){
            return PieceType.BISHOP;
        }
        if(ch=='q'){
            return PieceType.QUEEN;
        }
        if(ch=='k'){
            return PieceType.KING;
        }
        return null;
    }
    public boolean isPathClear(Move move){
        int fromRow=move.getFrom().getRow();
        int fromCol=move.getFrom().getColumn();
        int toRow=move.getTo().getRow();
        int toCol=move.getTo().getColumn();
        int rowStep=0;
        int colStep=0;
        if(toRow>fromRow){
            rowStep=1;
        }
        if(toRow<fromRow){
            rowStep=-1;
        }
        if(toCol>fromCol){
            colStep=1;
        }
        if(toCol<fromCol){
            colStep=-1;
        }
        int r=fromRow+rowStep;
        int c=fromCol+colStep;
        while(r!=toRow || c!=toCol){
            Position p=new Position(r,c);
            if(isOccupied(p)){
                return false;
            }
            r=r+rowStep;
            c=c+colStep;
        }
        return true;
    }

}
