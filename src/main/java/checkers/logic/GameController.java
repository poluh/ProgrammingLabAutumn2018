package checkers.logic;

import AI.AI;
import checkers.UI.Painter;
import checkers.logic.structure.Cell;
import checkers.logic.structure.Point;

import java.util.*;


/**
 * The main class for control game rule and all everything else
 */
public class GameController {
    // Current move
    private boolean isWhite = true;
    // Move in process or user just select cell for move
    private boolean moving = false;
    // Last enemies
    private Map<Point, Point> wasEnemies;
    // Last possible moves
    private Set<Point> wasPossibleMoves;
    private Point wasCurrentPoint;
    private Field field;
    private Painter painter;
    private static final int AI_DEPTH = 0;
    private AI ai = null;
    private Winner winner = Winner.NO_ONE;
    // Count kings of each user's color
    private int whiteKings = 0;
    private int blackKings = 0;

    public enum Winner {
        WHITE,
        BLACK,
        DRAW,
        NO_ONE
    }

    public GameController(Field field, Painter painter) {
        this.field = field;
        this.painter = painter;
        wasPossibleMoves = new HashSet<>();
        wasEnemies = new HashMap<>();
    }

    /**
     * @param point is point on witch user wanna choose or to move
     */
    public void setPoint(Point point) {
        if (!moving) {
            firstSetPoint(point);
        } else {
            secondSetPoint(point);
        }
        // Check winner
        winner = getWinner();
    }

    private void firstSetPoint(Point point) {
        if (!field.thatCanChop(isWhite).contains(point)) return;
        moving = true;
        wasCurrentPoint = point;
        wasEnemies = field.possibleMovesWithEnemies(point, isWhite);
        wasPossibleMoves = wasEnemies.isEmpty() ? field.possibleEmptyMoves(point) : wasEnemies.keySet();
        painter.printStroke(wasPossibleMoves.stream(), false);
        if (wasPossibleMoves.isEmpty()) winner = isWhite ? Winner.BLACK : Winner.WHITE;
    }

    private void secondSetPoint(Point point) {
        moving = false;

        if (!wasPossibleMoves.contains(point)) {
            painter.printStroke(wasPossibleMoves.stream(), true);
            return;
        }
        Point enemy = null;
        if (!wasEnemies.isEmpty()) enemy = wasEnemies.get(point);

        move(point, enemy);
        if (isWhite && point.y == 0) {
            whiteKings++;
        } else if (!isWhite && point.y == 7) {
            blackKings++;
        }
        isWhite = !isWhite;
        if (!wasEnemies.isEmpty() && !field.possibleMovesWithEnemies(point, !isWhite).isEmpty()) {
            isWhite = !isWhite;
            return;
        }

        boolean aiColorIsWhite = false;
        if (ai != null && aiColorIsWhite == isWhite) ai.move(AI_DEPTH);
    }

    private Winner getWinner() {
        if (field.getBlackCells().isEmpty() || field.getWhiteCells().isEmpty()) {
            return field.getBlackCells().isEmpty() ? Winner.WHITE : Winner.BLACK;
        }
        if (whiteKings == field.getWhiteCells().size() && blackKings == field.getBlackCells().size()) {
            if (whiteKings == blackKings) {
                return Winner.DRAW;
            }
            else {
               return whiteKings > blackKings ? Winner.WHITE : Winner.BLACK;
            }
        }
        return Winner.NO_ONE;
    }

    private void move(Point toMove, Point chopPoint) {
        if (chopPoint == null) {
            field.move(wasCurrentPoint, toMove);
        } else {
            field.move(wasCurrentPoint, toMove, chopPoint);
        }
        painter.move(wasCurrentPoint, toMove, chopPoint, isWhite);
        painter.printStroke(wasPossibleMoves.stream(), true);
    }

    public boolean isMoving() {
        return moving;
    }

    public Set<Point> getWasPossibleMoves() {
        return wasPossibleMoves;
    }

    public Field getField() {
        return field;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public void withAI() {
        ai = new AI(this);
    }

    public Winner winner() {
        return winner;
    }
}