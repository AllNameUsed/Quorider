package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.DuplicateFormatFlagsException;

public class Tile extends Rectangle {

    private Piece piece;

    private int xpos;
    private int ypos;

    public boolean moveUp;
    public boolean moveDown;
    public boolean moveRight;
    public boolean moveLeft;



    public boolean hasPiece() {
        return piece != null;
    }
    public Piece getPiece() {
        return piece;
    }
    public void setPiece(Piece p) {
        this.piece = p;
        if (p != null && p.getType() == PieceType.ONE) {
            if (this.ypos == 0) {
                Main.P1victory.fire();
            }
        } else if (p != null && p.getType() == PieceType.TWO){
            if (this.ypos == 8) {
                Main.P2victory.fire();
            }
        }
    }

    public Tile(int x, int y) {
        this.xpos = x;
        this.ypos = y;
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        this.moveUp = true;
        this.moveDown = true;
        this.moveRight = true;
        this.moveLeft = true;
        if (x == 0) {
            this.moveLeft = false;
        }
        if (x == 8) {
            this.moveRight = false;
        }
        if (y == 0) {
            this.moveUp = false;
        }
        if (y == 8) {
            this.moveDown = false;
        }
        relocate(x * (Main.TILE_SIZE + 15), y * (Main.TILE_SIZE + 15));
        setFill(Color.BLACK);
    }
}
