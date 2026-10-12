package chess.controller;

import chess.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/games")
public class GameRestController{
    private final GameService gameService;

    public GameRestController(GameService gameService){
        this.gameService=gameService;
    }

    @GetMapping("/{id}/board")
    public Map<String,Object> board(@PathVariable String id){
        Map<String,Object> out=new HashMap<>();
        out.put("board",gameService.boardRows(id));
        out.put("turn",gameService.getGame(id).getCurrentTurn().toString());
        out.put("status",gameService.getGame(id).getStatus().toString());
        out.put("moves",gameService.history(id));
        return out;
    }

    @GetMapping("/{id}/history")
    public Map<String,Object> history(@PathVariable String id){
        Map<String,Object> out=new HashMap<>();
        out.put("moves",gameService.history(id));
        return out;
    }
}

