package chess.model;
import chess.enums.Color;


public class Player{
    private String name;
    private Color color;

    public Move makeMove(Position from, Position to){
        return new Move(from, to);

    }
    public Player(String name, Color color){
        this.name=name;
        this.color=color;
    }

    public String getName(){
        return name;
    }
    public Color getColor(){
        return color;
    }
}

