package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;


public class Main extends Application {
    public static final int TILE_SIZE = 80;
    public static final int SIZE = 9;
    public static Button P1victory;
    public static Button P2victory;
    public Stage vic;
    private Pane root;

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    public static Group barriorGroup = new Group();

    public static Tile[][] board = new Tile[SIZE][SIZE];
    public static BarriorPoint[][] barriorPoints = new BarriorPoint[10][10];

    private Parent board() {
        root = new Pane();
        root.setPrefSize(840,840);
        root.getChildren().addAll(tileGroup, pieceGroup,barriorGroup);

        // Generate all tiles and game pieces
        for (int i = 0 ; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Tile tile = new Tile(i, j);
                board[i][j] = tile;
                tileGroup.getChildren().add(tile);
                Piece piece = null;
                if (i == 4 && j == 0) {
                    piece = makePiece(PieceType.TWO,i,j);
                } else if (i == 4 && j == 8) {
                    piece = makePiece(PieceType.ONE,i,j);
                }
                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }
        }
        for (int i = 1; i < 9; i ++) {
            BarriorPoint bp = new BarriorPoint(i,0);
            barriorPoints[i][0] = bp;
            barriorGroup.getChildren().add(bp);
        }
        for (int i = 1; i < 9; i ++) {
            BarriorPoint bp = new BarriorPoint(i,9);
            barriorPoints[i][9] = bp;
            barriorGroup.getChildren().add(bp);
        }
        for (int i = 1; i < 9; i ++) {
            BarriorPoint bp = new BarriorPoint(0,i);
            barriorPoints[0][i] = bp;
            barriorGroup.getChildren().add(bp);
        }
        for (int i = 1; i < 9; i ++) {
            BarriorPoint bp = new BarriorPoint(9,i);
            barriorPoints[9][i] = bp;
            barriorGroup.getChildren().add(bp);
        }

        for (int i = 1; i < 9; i ++) {
            for (int j = 1; j < 9;j++) {
                BarriorPoint bp = new BarriorPoint(i,j);
                barriorPoints[i][j] = bp;
                barriorGroup.getChildren().add(bp);
            }
        }
        return root;
    }
    public static Piece makePiece (PieceType type, int x, int y) {
        Piece p = new Piece(type,x,y);

        p.setOnMouseReleased(e -> {
            int newx = boardPos(p.getLayoutX());
            int newy = boardPos(p.getLayoutY());
            boolean move_legal = tryMove(p, newx, newy);
            int x0 = boardPos(p.X);
            int y0 = boardPos(p.Y);
            if (move_legal) {
                p.move(newx* (Main.TILE_SIZE + 15), newy* (Main.TILE_SIZE + 15));
                board[x0][y0].setPiece(null);
                board[newx][newy].setPiece(p);
            } else {
                p.abortMove();
            }
        });
        return p;
    }
    private void createVicStage(String s, Stage stage) {
        Button restart = new Button("Restart?");
        restart.setOnAction(e -> {
            vic.close();
            restart(stage);
        });
        Text t = new Text();
        t.setText(s);
        t.setFont(Font.font ("Verdana", 80));
        t.setFill(Color.RED);
        VBox vBox = new VBox(t,restart);
        Scene scene = new Scene(vBox);
        vic.setScene(scene);
        vic.show();
    }

    void startGame(Stage stage) {
        Scene scene = new Scene(board());
        vic = new Stage(StageStyle.DECORATED);

        P1victory = new Button("");
        P1victory.setOnAction(e-> {
            createVicStage("Congratulations to BLUE !!!! ",stage);
        });

        P2victory = new Button("");
        P2victory.setOnAction(e-> {
            createVicStage("Congratulations to ORANGE !!!! ",stage);
        });

        stage.setTitle("Quoridor!");
        stage.setScene(scene);
        stage.show();
    }
    void restart(Stage stage) {
        cleanup();
        startGame(stage);
    }
    void cleanup() {
        root = new Pane();
        tileGroup = new Group();;
        pieceGroup  = new Group();;
        barriorGroup  = new Group();;
        board = new Tile[9][9];
        barriorPoints = new BarriorPoint[10][10];
    }
    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);
    }
    public static boolean tryMove(Piece p, int x, int y) {
        int oldx = boardPos(p.X);
        int oldy = boardPos(p.Y);
        if (oldy == y) {
            // move horizontal
            if (oldx > x) {
                // move left:
                if (!board[oldx][oldy].moveLeft) {
                    return false;
                }
            } else if (x > oldx) {
                // move right:
                if (!board[oldx][oldy].moveRight) {
                    return false;
                }
            } else {
                return false;
            }
        } else if (oldx == x) {
            // move vertical
            if (oldy > y) {
                // moove up:
                if (!board[oldx][oldy].moveUp) {
                    return false;
                }
            } else if (y > oldy) {
                // move down:
                if (!board[oldx][oldy].moveDown) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        int xd = (x > oldx ? (x - oldx): (oldx - x));
        int yd = (y > oldy ? (y - oldy): (oldy - y));
        if (xd == 1 && yd == 0) {
            return true;
        } else if (xd == 0 && yd == 1) {
            return true;
        } else if (xd == 2 && yd == 0) {
            int x0 = (oldx + x)/2;
            if (board[x0][y].hasPiece() && board[x0][y].getPiece().getType() != p.getType()) {
                return true;
            }
            return false;
        } else if (xd == 0 && yd == 2) {
            int y0 = (oldy + y)/2;
            if (board[x][y0].hasPiece() && board[x][y0].getPiece().getType() != p.getType()) {
                return true;
            }
            return false;
        } else if (board[x][y].hasPiece()) {
            return false;
        } else if (xd > 1 || yd > 1) {
            return false;
        }
        return false;
    }
    private static int boardPos(double pixel) {
        return (int) (pixel + (TILE_SIZE + 15)/2) / (TILE_SIZE + 15);
    }
    public static void main(String[] args) {
        launch(args);
    }
}
