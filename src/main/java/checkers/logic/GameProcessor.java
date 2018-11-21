package checkers.logic;

import AI.AI;
import checkers.UI.Painter;
import checkers.logic.structure.Point;

import java.util.*;

public class GameProcessor {
    private boolean isWhite = true;
    private boolean moving = false;
    private Map<Point, Point> wasEnemies;
    private Set<Point> wasPossibleMoves;
    private Point wasCurrentPoint;
    private Field field;
    private Painter painter;
    private AI ai = null;
    private boolean aiColorIsWhite = false;

    public GameProcessor(Field field, Painter painter) {
        this.field = field;
        this.painter = painter;
        wasPossibleMoves = new HashSet<>();
        wasEnemies = new HashMap<>();
    }

    public void setPoint(Point point) {
        field.printPoints();
        if (!moving) {
            if (!field.thatCanChop(isWhite).contains(point)) return;

            moving = true;

            wasCurrentPoint = point;

            wasEnemies = field.possibleMovesWithEnemies(point, isWhite);
            wasPossibleMoves = wasEnemies.isEmpty() ? field.possibleEmptyMoves(point) : wasEnemies.keySet();

            painter.printStroke(wasPossibleMoves.stream(), false);
        } else {

            moving = false;

            if (!wasPossibleMoves.contains(point)) {
                painter.printStroke(wasPossibleMoves.stream(), true);
                return;
            }

            Point enemy = null;
            if (!wasEnemies.isEmpty()) enemy = wasEnemies.get(point);

            move(point, enemy);
            isWhite = !isWhite;

            if (!wasEnemies.isEmpty() && !field.possibleMovesWithEnemies(point, !isWhite).isEmpty()) {
                isWhite = !isWhite;
                return;
            }

            if (ai != null && aiColorIsWhite == isWhite) ai.move(0);
        }
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
}