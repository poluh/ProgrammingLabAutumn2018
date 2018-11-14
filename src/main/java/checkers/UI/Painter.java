package checkers.UI;

import checkers.logic.structure.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {

    private static final Color WHITE_CELL = Color.WHITESMOKE;
    private static final Color BLACK_CELL = Color.DARKGRAY;
    private static final Color EMPTY_CELL = Color.BLACK;
    public static final int CELL_SIZE = 50;

    private GraphicsContext gc;

    public Painter(GraphicsContext gc) {
        this.gc = gc;
    }

    public void printField() {
        gc.setFill(EMPTY_CELL);
        for (var x = 0; x < 8; ++x) {
            for (var y = 0; y < 8; ++y) {
                if ((x + y) % 2 == 0) {
                    var point = new Point(x * CELL_SIZE, y * CELL_SIZE);
                    printCell(point, false, true);
                    printCell(point, y > 4, y > 2 && y < 5);
                }
            }
        }
    }

    public void printCell(Point point, boolean isWhite, boolean empty) {
        var color = empty ? EMPTY_CELL : isWhite ? WHITE_CELL : BLACK_CELL;
        gc.setFill(color);
        if (empty) {
            gc.fillRect(point.x, point.y, CELL_SIZE, CELL_SIZE);
        } else {
            gc.fillOval(point.x + 5, point.y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    public void move(Point oldPoint, Point newPoint, boolean isWhite) {
        printCell(oldPoint, isWhite, true);
        printCell(newPoint, isWhite, false);
    }
}
