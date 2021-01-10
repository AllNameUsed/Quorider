package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Piece extends StackPane {
    double X, Y;
    private double mouseX, mouseY;
    public int numberOfBarrior;

    private PieceType type;
    public void move(int x, int y) {
        this.X = x;
        this.Y = y;
        relocate(x, y);
    }
    public PieceType getType() {
        return type;
    }
    public void abortMove() {
        relocate(X, Y);
    }
    public Piece(PieceType type, int x, int y) {
        this.type = type;
        numberOfBarrior = 10;

        move(x * (Main.TILE_SIZE + 15), y * (Main.TILE_SIZE + 15));
        Ellipse bg =  new Ellipse(Main.TILE_SIZE * 0.5, Main.TILE_SIZE * 0.5);
        bg.setFill(type == PieceType.ONE ? Color.LIGHTBLUE : Color.ORANGE);
        getChildren().addAll(bg);

        setOnMousePressed(e ->{
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + X, e.getSceneY() - mouseY + Y);
        });
    }
}
