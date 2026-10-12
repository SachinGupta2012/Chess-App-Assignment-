package chess.model;
import chess.enums.PieceType;
import chess.enums.Color;

public class PieceFactory{
    public static Piece createPiece(PieceType type, Color color){
        if(type==PieceType.PAWN){
            return new Pawn(color);
        }
        if(type==PieceType.ROOK){
            return new Rook(color);
        }
        if(type==PieceType.KNIGHT){
            return new Knight(color);
        }
        if(type==PieceType.BISHOP){
            return new Bishop(color);
        }
        if(type==PieceType.QUEEN){
            return new Queen(color);
        }
        if(type==PieceType.KING){
            return new King(color);
        }
        return null;
    }

    public static Piece createPromotionPiece(String value,Color color){
        if("ROOK".equals(value)){
            return createPiece(PieceType.ROOK,color);
        }
        if("BISHOP".equals(value)){
            return createPiece(PieceType.BISHOP,color);
        }
        if("KNIGHT".equals(value)){
            return createPiece(PieceType.KNIGHT,color);
        }
        return createPiece(PieceType.QUEEN,color);
    }
}

