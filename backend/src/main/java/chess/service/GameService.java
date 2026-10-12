package chess.service;

import chess.model.Board;
import chess.model.ChessGame;
import chess.model.Move;
import chess.model.Piece;
import chess.model.Player;
import chess.model.Position;
import chess.enums.Color;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService{
    private final Map<String,ChessGame> games=new ConcurrentHashMap<>();

    public ChessGame createGame(String gameId){
        ChessGame g=new ChessGame(new Player("white",Color.WHITE),new Player("black",Color.BLACK));
        games.put(gameId,g);
        return g;
    }

    public ChessGame getGame(String gameId){
        if(games.containsKey(gameId)==false){
            createGame(gameId);
        }
        return games.get(gameId);
    }

    public Board getBoard(String gameId){
        return getGame(gameId).getBoard();
    }

    public List<List<String>> boardRows(String gameId){
        Board b=getBoard(gameId);
        List<List<String>> rows=new ArrayList<>();
        for(int r=7;r>=0;r--){
            List<String> row=new ArrayList<>();
            for(int c=0;c<8;c++){
                Position pos=new Position(r,c);
                if(b.isOccupied(pos)){
                    Piece p=b.getSquare(pos).getPiece();
                    String s=p.getColor().toString().charAt(0)+"_"+p.getClass().getSimpleName();
                    row.add(s);
                }else{
                    row.add("");
                }
            }
            rows.add(row);
        }
        return rows;
    }

    public String makeMove(String gameId,Move move,String color,String promotionPiece){
        ChessGame g=getGame(gameId);
        if(color!=null && color.isEmpty()==false){
            if(color.equals(g.getCurrentTurn().toString())==false){
                return "Not your turn";
            }
        }
        return g.makeMove(move,promotionPiece);
    }

    public String makeMove(String gameId,int fromRow,int fromCol,int toRow,int toCol,String color,String promotionPiece){
        Move move=new Move(new Position(fromRow,fromCol),new Position(toRow,toCol));
        return makeMove(gameId,move,color,promotionPiece);
    }

    public Move undo(String gameId,String color){
        ChessGame g=getGame(gameId);
        Move last=g.getMoveHistory().getLastMove();
        if(last==null){
            return null;
        }
        if(color!=null && color.isEmpty()==false){
            String lastColor=last.getPiece().getColor().toString();
            if(color.equals(lastColor)==false){
                return null;
            }
        }
        return g.undoLast();
    }

    public List<String> history(String gameId){
        List<String> out=new ArrayList<>();
        List<Move> moves=getGame(gameId).getMoveHistory().getMoves();
        for(int i=0;i<moves.size();i++){
            Move m=moves.get(i);
            String s=(i+1)+". "+name(m.getFrom())+"-"+name(m.getTo());
            out.add(s);
        }
        return out;
    }

    private String name(Position p){
        char file=(char)('a'+p.getColumn());
        int rank=p.getRow()+1;
        return ""+file+rank;
    }
}

