package chess.dto;

public class MoveMessage {
    private String gameId;
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;
    private String type;
    private String color;
    private String promotionPiece;

    public MoveMessage() {
        type="MOVE";
    }
    public String getGameId() {
        return gameId;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public int getFromRow() {
        return fromRow;
    }
    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }
    public int getFromCol() {
        return fromCol;
    }
    public void setFromCol(int fromCol) {
        this.fromCol = fromCol;
    }
    public int getToRow() {
        return toRow;
    }
    public void setToRow(int toRow) {
        this.toRow = toRow;
    }
    public int getToCol() {
        return toCol;
    }
    public void setToCol(int toCol) {
        this.toCol = toCol;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getPromotionPiece() {
        return promotionPiece;
    }
    public void setPromotionPiece(String promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
}
