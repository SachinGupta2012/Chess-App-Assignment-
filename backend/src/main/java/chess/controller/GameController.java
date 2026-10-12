package chess.controller;

import chess.dto.MoveMessage;
import chess.model.Move;
import chess.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GameController{
    private final GameService gameService;
    private final SimpMessagingTemplate messaging;

    public GameController(GameService gameService,SimpMessagingTemplate messaging){
        this.gameService=gameService;
        this.messaging=messaging;
    }

    @MessageMapping("/game.join")
    public void join(MoveMessage msg){
        gameService.getGame(msg.getGameId());
        sendGame(msg.getGameId());
    }

    @MessageMapping("/game.move")
    public void move(MoveMessage msg){
        String error=gameService.makeMove(
            msg.getGameId(),
            msg.getFromRow(),
            msg.getFromCol(),
            msg.getToRow(),
            msg.getToCol(),
            msg.getColor(),
            msg.getPromotionPiece()
        );

        if(error!=null){
            sendError(msg.getGameId(),error);
            return;
        }
        sendGame(msg.getGameId());
    }

    @MessageMapping("/game.undo")
    public void undo(MoveMessage msg){
        Move move=gameService.undo(msg.getGameId(),msg.getColor());
        if(move==null){
            sendError(msg.getGameId(),"Cannot undo now");
            return;
        }
        sendGame(msg.getGameId());
    }

    private void sendGame(String gameId){
        Map<String,Object> data=new HashMap<>();
        data.put("type","GAME");
        data.put("gameId",gameId);
        data.put("board",gameService.boardRows(gameId));
        data.put("moves",gameService.history(gameId));
        data.put("turn",gameService.getGame(gameId).getCurrentTurn().toString());
        data.put("status",gameService.getGame(gameId).getStatus().toString());
        messaging.convertAndSend("/topic/game."+gameId,data);
    }

    private void sendError(String gameId,String error){
        Map<String,Object> data=new HashMap<>();
        data.put("type","ERROR");
        data.put("gameId",gameId);
        data.put("error",error);
        messaging.convertAndSend("/topic/game."+gameId,data);
    }
}
