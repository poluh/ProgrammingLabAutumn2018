package checkers.UI;

import checkers.logic.structure.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.stream.Stream;

public class Painter {

    private static final Color WHITE_CELL = Color.WHITESMOKE;
    private static final Color BLACK_CELL = Color.DARKGRAY;
    private static final Color EMPTY_CELL = Color.BLACK;

    public static final int DEFAULT_FIELD_SIZE = 8;
    public static final int CELL_SIZE = 100;

    private GraphicsContext gc;

    public Painter(GraphicsContext gc) {
        this.gc = gc;
    }

    public void printField() {
        gc.setFill(EMPTY_CELL);
        for (var x = 0; x < DEFAULT_FIELD_SIZE; ++x) {
            for (var y = 0; y < DEFAULT_FIELD_SIZE; ++y) {
                if ((x + y) % 2 == 0) {
                    var point = new Point(x, y);
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
            gc.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        } else {
            gc.fillOval(point.x * CELL_SIZE + 5, point.y * CELL_SIZE + 5, CELL_SIZE - 10, CELL_SIZE - 10);
        }
    }

    public void printStroke(Stream<Point> points, boolean clear) {
        gc.setLineWidth(2);
        gc.setStroke(clear ? EMPTY_CELL : Color.RED);
        points.forEach(point -> gc.strokeRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE));
    }

    public void move(Point from, Point toMove, Point chopPoint, boolean isWhite) {
        if (chopPoint != null) printCell(chopPoint, isWhite, true);
        printCell(from, isWhite, true);
        printCell(toMove, isWhite, false);
    }

    public void paintWin(String winner) {
        var width = gc.getCanvas().getWidth();
        var height = gc.getCanvas().getHeight();

        gc.setFill(Color.PAPAYAWHIP);
        gc.fillRect(200, 200, width - 400, height - 400);
        gc.setFill(Color.SILVER);
        gc.fillText(winner, width / 2, height / 2);
    }

}
