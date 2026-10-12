package chess.model;

import chess.enums.Color;
import chess.enums.GameStatus;
import chess.enums.MoveType;
import chess.rules.BasicMoveValidator;
import chess.rules.MoveValidator;
import chess.rules.StandardGameStateEvaluator;

public class ChessGame{
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Color currentTurn;
    private GameStatus status;
    private MoveHistory moveHistory;
    private MoveValidator validator;
    private StandardGameStateEvaluator evaluator;

    public ChessGame(Player whitePlayer,Player blackPlayer){
        this.board=new Board();
        this.board.setupStart();
        this.whitePlayer=whitePlayer;
        this.blackPlayer=blackPlayer;
        this.currentTurn=Color.WHITE;
        this.status=GameStatus.IN_PROGRESS;
        this.moveHistory=new MoveHistory();
        this.validator=new BasicMoveValidator();
        this.evaluator=new StandardGameStateEvaluator();
    }

    public String makeMove(Move move,String promotionPiece){
        Position from=move.getFrom();
        Position to=move.getTo();
        Piece piece=board.getSquare(from).getPiece();

        if(isGameOver()){
            return "Game is already over";
        }
        if(piece==null){
            return "No piece at that square";
        }
        if(piece.getColor()!=currentTurn){
            return "It is not your turn";
        }
        if(validator.validate(board,move)==false){
            return "That piece cannot move there";
        }

        MoveType type=getMoveType(move,piece);
        Piece captured=board.getSquare(to).getPiece();
        if(captured instanceof King){
            return "You cannot capture the king";
        }

        Move savedMove=new Move(from,to,piece,captured,type);
        makeMoveOnBoard(savedMove,promotionPiece);

        if(evaluator.isKingInCheck(board,piece.getColor())){
            undoMoveOnBoard(savedMove);
            return "Move would leave king in check";
        }

        moveHistory.add(savedMove);
        changeTurn();
        status=evaluator.evaluate(board,currentTurn);
        return null;
    }

    public Move undoLast(){
        Move last=moveHistory.undo();
        if(last==null){
            return null;
        }
        undoMoveOnBoard(last);
        changeTurn();
        status=evaluator.evaluate(board,currentTurn);
        return last;
    }

    private MoveType getMoveType(Move move,Piece piece){
        if(piece instanceof King && Math.abs(move.getTo().getColumn()-move.getFrom().getColumn())==2){
            return MoveType.CASTLING;
        }
        if(piece instanceof Pawn && isLastRow(piece,move.getTo())){
            return MoveType.PROMOTION;
        }
        if(board.isOccupied(move.getTo())){
            return MoveType.CAPTURE;
        }
        return MoveType.NORMAL;
    }

    private void makeMoveOnBoard(Move move,String promotionPiece){
        board.movePiece(move);
        if(move.getMoveType()==MoveType.CASTLING){
            board.moveRookForCastling(move);
        }
        if(move.getMoveType()==MoveType.PROMOTION){
            Piece newPiece=PieceFactory.createPromotionPiece(promotionPiece,move.getPiece().getColor());
            board.getSquare(move.getTo()).setPiece(newPiece);
        }
    }

    private void undoMoveOnBoard(Move move){
        board.getSquare(move.getFrom()).setPiece(move.getPiece());
        board.getSquare(move.getTo()).setPiece(move.getCapturedPiece());
        if(move.getMoveType()==MoveType.CASTLING){
            board.undoRookForCastling(move);
        }
    }

    private boolean isLastRow(Piece piece,Position to){
        if(piece.getColor()==Color.WHITE && to.getRow()==7){
            return true;
        }
        return piece.getColor()==Color.BLACK && to.getRow()==0;
    }

    private void changeTurn(){
        if(currentTurn==Color.WHITE){
            currentTurn=Color.BLACK;
        }else{
            currentTurn=Color.WHITE;
        }
    }

    public boolean isGameOver(){
        return status==GameStatus.CHECKMATE || status==GameStatus.STALEMATE || status==GameStatus.DRAW;
    }

    public Board getBoard(){
        return board;
    }

    public MoveHistory getMoveHistory(){
        return moveHistory;
    }

    public Color getCurrentTurn(){
        return currentTurn;
    }

    public GameStatus getStatus(){
        return status;
    }
}
