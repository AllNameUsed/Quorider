package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class BarriorPoint extends StackPane {
    // Position on the game board:
    int xpos;
    int ypos;
    // Actual coordinates on screen:
    double xcoord;
    double ycoord;
    int newxpos;
    int newypos;
    double newxcoord;
    double newycoord;

    public int used;
    public void moveOnBoard(int x, int y) {
        if (this.used == 0) {
            Main.barriorGroup.getChildren().remove(Main.barriorPoints[x][y]);
            return;
        }
        if (x == 0) {
            relocate(0, y * (Main.TILE_SIZE) + (y - 1) * 15);
        } else if (y == 0) {
            relocate(x * (Main.TILE_SIZE) + (x - 1) * 15, 0);
        } else if (x == 9) {
            relocate(825, y * (Main.TILE_SIZE) + (y - 1) * 15);
        } else if (y == 9) {
            relocate(x * (Main.TILE_SIZE) + (x - 1) * 15, 825);
        } else {
            relocate(x * (Main.TILE_SIZE) + (x - 1) * 15, y * (Main.TILE_SIZE) + (y - 1) * 15);
        }
    }
    public int translateCoords(double pixel) {
        return (int) ((pixel + 30) / 95);
    }

    private void drawxBarrior(int from,int to,int y) {
        Main.barriorPoints[from][y].used --;
        Main.barriorPoints[to][y].used --;
        int x = Math.min(from, to);
        Rectangle first = new Rectangle(80,15);
        first.setFill(Color.MEDIUMPURPLE);
        first.relocate(x*95,y*80+(y-1)*15);
        Main.barriorGroup.getChildren().add(first);
        x++;
        Rectangle second = new Rectangle(80,15);
        second.setFill(Color.MEDIUMPURPLE);
        second.relocate(x*95,y*80+(y-1)*15);
        Main.barriorGroup.getChildren().add(second);
        updateTileInfo(Main.barriorPoints[from][y],Main.barriorPoints[to][y]);
        if (Main.barriorPoints[from][y].used == 0) {
            Main.barriorGroup.getChildren().remove(Main.barriorPoints[from][y]);
        }
        if (Main.barriorPoints[to][y].used == 0) {
            Main.barriorGroup.getChildren().remove(Main.barriorPoints[to][y]);
        }
    }
    private void updateTileInfo(BarriorPoint one, BarriorPoint two) {
        if (one.ypos == two.ypos) {
            // Horizontal:
            int x = Math.min(one.xpos,two.xpos);
            int y = Math.min(one.ypos, two.ypos) - 1;
            Main.board[x][y].moveDown = false;
            Main.board[x][y+1].moveUp = false;
            Main.board[x + 1][y].moveDown = false;
            Main.board[x + 1][y+1].moveUp = false;
        } else {
            // Vertical:
            int x = Math.min(one.xpos,two.xpos);
            int y = Math.min(one.ypos, two.ypos);
            Main.board[x][y].moveLeft = false;
            Main.board[x][y+1].moveLeft = false;
            Main.board[x - 1][y].moveRight = false;
            Main.board[x - 1][y+1].moveRight = false;
        }
    }
    private void drawyBarrior(int from, int to, int x) {
        Main.barriorPoints[x][from].used --;
        Main.barriorPoints[x][to].used --;
        int y = Math.min(from, to);
        Rectangle first = new Rectangle(15,80);
        first.setFill(Color.MEDIUMPURPLE);
        first.relocate(x*80 + (x-1)*15,y*95);
        Main.barriorGroup.getChildren().add(first);
        y++;
        Rectangle sec = new Rectangle(15,80);
        sec.setFill(Color.MEDIUMPURPLE);
        sec.relocate(x*80 + (x-1)*15,y*95);
        Main.barriorGroup.getChildren().add(sec);
        updateTileInfo(Main.barriorPoints[x][from],Main.barriorPoints[x][to]);
        if (Main.barriorPoints[x][from].used == 0) {
            Main.barriorGroup.getChildren().remove(Main.barriorPoints[x][from]);
        }
        if (Main.barriorPoints[x][to].used == 0) {
            Main.barriorGroup.getChildren().remove(Main.barriorPoints[x][to]);
        }
    }
    public BarriorPoint(int x, int y) {
        this.xpos = x;
        this.ypos = y;
        this.used = 4;
        Rectangle bg =  new Rectangle(15, 15);
        bg.setFill(Color.BROWN);
        moveOnBoard(x,y);
        getChildren().add(bg);

        setOnMousePressed(e ->{
            this.xcoord = e.getSceneX();
            this.ycoord = e.getSceneY();
            int oldxpos = translateCoords(xcoord);
            int oldypos = translateCoords(ycoord);
            this.xcoord = oldxpos * (Main.TILE_SIZE) + (oldxpos - 1) * 15;
            this.ycoord = oldypos * (Main.TILE_SIZE) + (oldypos - 1) * 15;
        });
        setOnMouseDragged(e -> {
            this.newxcoord = e.getSceneX();
            this.newycoord = e.getSceneY();
            this.newxpos = translateCoords(newxcoord);
            this.newypos = translateCoords(newycoord);

            if (newxpos == xpos && ypos == newypos) {
                moveOnBoard(xpos,ypos);
                bg.setHeight(15);
                bg.setWidth(15);
            } else if (newxpos == xpos) {
                // Same col:
                if (newypos > ypos) {
                    // Dragged down:
                    moveOnBoard(xpos,ypos);
                    bg.setHeight(newycoord - ycoord + 15);
                    bg.setWidth(15);
                } else {
                    // Dragged up:
                    relocate(xpos * (Main.TILE_SIZE) + (xpos - 1) * 15, newycoord);
                    bg.setHeight(ycoord - newycoord + 15);
                    bg.setWidth(15);
                }

            } else {
                // Same row:
                if (newxpos > xpos) {
                    // Dragged right:
                    moveOnBoard(xpos,ypos);
                    bg.setWidth(newxcoord - xcoord + 15);
                    bg.setHeight(15);
                } else {
                    // Dragged left:
                    relocate(newxcoord, ypos * (Main.TILE_SIZE) + (ypos - 1) * 15);
                    bg.setWidth(xcoord - newxcoord + 15);
                    bg.setHeight(15);
                }
            }
        });
        setOnMouseReleased(e -> {
            int diffinx = Math.abs(newxpos - xpos);
            int diffiny = Math.abs(newypos - ypos);
            if (diffiny == 0) {//Same row:
                if (diffinx != 2) {
                    // Reset:
                    moveOnBoard(xpos,ypos);
                    bg.setHeight(15);
                    bg.setWidth(15);
                } else {
                    drawxBarrior(newxpos, xpos, ypos);
                    // Reset:
                    moveOnBoard(xpos,ypos);
                    bg.setHeight(15);
                    bg.setWidth(15);
                }
            } else if (diffinx == 0) {// Same col
                if (diffiny != 2) {
                    // Reset:
                    moveOnBoard(xpos,ypos);
                    bg.setHeight(15);
                    bg.setWidth(15);
                } else {
                    drawyBarrior(newypos,ypos,xpos);
                    // Reset:
                    moveOnBoard(xpos,ypos);
                    bg.setHeight(15);
                    bg.setWidth(15);
                }
            } else {
                // Reset:
                moveOnBoard(xpos,ypos);
                bg.setHeight(15);
                bg.setWidth(15);
            }

        });
    }
}
