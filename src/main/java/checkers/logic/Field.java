package checkers.logic;

import checkers.logic.structure.Cell;
import checkers.logic.structure.Point;

import java.util.*;

public class Field implements Cloneable {
    private Map<Point, Cell> cells;
    private Set<Cell> whiteCells;
    private Set<Cell> blackCells;

    public Field() {
        cells = new HashMap<>();
        whiteCells = new HashSet<>();
        blackCells = new HashSet<>();
    }

    public void init() {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                var point = new Point(x, y);
                var cell = new Cell(point);
                if ((x + y) % 2 == 0 && (y < 3 || y > 4)) cell.setWhite(y > 4);
                cells.put(point, cell);
            }
        }
    }

    public Set<Point> thatCanChop(boolean isWhite) {
        var cellsCheck = isWhite ? whiteCells : blackCells;
        var res = new HashSet<Point>();

        for (var cell : cellsCheck) {
            if (!possibleMovesWithEnemies(cell.getPoint(), isWhite).isEmpty()) {
                res.add(cell.getPoint());
            } else if (!possibleEmptyMoves(cell.getPoint()).isEmpty()) {
                res.add(cell.getPoint());
            }
        }
        return res;
    }

    public Set<Point> possibleEmptyMoves(Point point) {
        var res = new HashSet<Point>();
        if (!cells.containsKey(point) || cells.get(point).isEmpty()) return res;

        var cell = cells.get(point);
        int dy = cell.isWhite() ? -1 : 1;
        for (var dx = -1; dx < 2; dx += 2) {
            var pointCheck = new Point(point.x + dx, point.y + dy);
            if (isNormalPoss(pointCheck) && cells.get(pointCheck).isEmpty()) {
                res.add(pointCheck);
            }
        }
        return res;
    }

    public Map<Point, Point> possibleMovesWithEnemies(Point point, boolean isWhite) {
        var res = new HashMap<Point, Point>();
        if (!cells.containsKey(point) || cells.get(point).isEmpty()) return res;

        for (var dx = -1; dx < 2; dx += 2) {
            for (var dy = -1; dy < 2; dy += 2) {
                var pointCheck = new Point(point.x + dx, point.y + dy);
                var pointMove = new Point(point.x + dx + dx, point.y + dy + dy);

                if (!isNormalPoss(pointCheck) || !isNormalPoss(pointMove)) break;

                var cellCheck = cells.get(pointCheck);
                var cellToMove = cells.get(pointMove);

                if (!cellCheck.isEmpty() && cellToMove.isEmpty() && cellCheck.isWhite() == !isWhite) {
                    res.put(pointCheck, pointMove);
                }
            }
        }
        return res;
    }

    public void move(Point oldPos, Point newPos) {
        move(oldPos, newPos, null, false);
    }

    public void move(Point oldPos, Point newPos, Point chopPoint) {
        move(oldPos, newPos, chopPoint, true);
    }

    private void move(Point oldPos, Point newPos, Point chopPoint, boolean chop) {
        if (!isNormalPoss(oldPos) || !isNormalPoss(newPos) || (chop && !isNormalPoss(chopPoint))) return;
        if (chop) cells.get(chopPoint).setEmpty(true);

        cells.put(newPos, cells.get(newPos));
        cells.get(oldPos).setEmpty(true);
    }

    public boolean isNormalPoss(Point point) {
        return point.x >= 0 && point.x < 8 && point.y >= 0 && point.y < 8;
    }

    public Set<Cell> getWhiteCells() {
        return whiteCells;
    }

    public Set<Cell> getBlackCells() {
        return blackCells;
    }

    private void setCells(Map<Point, Cell> cells) {
        this.cells = cells;
    }

    private void setWhiteCells(Set<Cell> whiteCells) {
        this.whiteCells = whiteCells;
    }

    private void setBlackCells(Set<Cell> blackCells) {
        this.blackCells = blackCells;
    }

    @Override
    public Field clone() {
        var field = new Field();
        field.setCells(cells);
        field.setWhiteCells(whiteCells);
        field.setBlackCells(blackCells);
        return field;
    }
}
