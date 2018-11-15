package checkers.logic;

import checkers.logic.structure.Cell;
import checkers.logic.structure.Point;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Field implements Cloneable {
    private Map<Point, Cell> cells;

    public Field() {
        cells = new HashMap<>();
    }

    public void init() {
        for (int x = 0; x < 8; ++x) {
            for (int y = 0; y < 8; ++y) {
                var point = new Point(x, y);
                var cell = new Cell(point);
                if ((x + y) % 2 == 0 && (y < 3 || y > 4)) {
                    cell.setWhite(y > 4);
                    cell.setEmpty(false);
                }
                cells.put(point, cell);
            }
        }
    }

    public Set<Point> thatCanChop(boolean isWhite) {
        var res = new HashSet<Point>();
        var checkCells = isWhite ? getWhiteCells() : getBlackCells();

        var enemiesEmpty = true;

        for (Cell cell : checkCells) {
            var point = cell.getPoint();
            if (!possibleMovesWithEnemies(point, isWhite).isEmpty()) {
                System.out.println(point);
                enemiesEmpty = false;
                res.add(point);
            } else if (enemiesEmpty && !possibleEmptyMoves(point).isEmpty()) {
                res.add(point);
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
                var forCheck = new Point(point.x + dx, point.y + dy);
                var toMove = new Point(forCheck.x + dx, forCheck.y + dy);

                if (!isNormalPoss(forCheck) || !isNormalPoss(toMove)) break;

                var cellCheck = cells.get(forCheck);
                var cellToMove = cells.get(toMove);

                if (!cellCheck.isEmpty() && cellCheck.isWhite() == !isWhite && cellToMove.isEmpty()) {
                    res.put(toMove, forCheck);
                }
            }
        }
        return res;
    }

    public void move(Point newPos, Point toMove) {
        move(newPos, toMove, null, false);
    }

    public void move(Point newPos, Point toMove, Point chopPoint) {
        move(newPos, toMove, chopPoint, true);
    }

    private void move(Point oldPos, Point toMove, Point chopPoint, boolean chop) {
        if (!isNormalPoss(oldPos) || !isNormalPoss(toMove) || (chop && !isNormalPoss(chopPoint))) return;

        if (chop) cells.get(chopPoint).setEmpty(true);

        cells.put(toMove, cells.get(oldPos));
        cells.put(oldPos, new Cell(oldPos));
    }

    public boolean isNormalPoss(Point point) {
        return point.x >= 0 && point.x < 8 && point.y >= 0 && point.y < 8;
    }

    public Set<Cell> getWhiteCells() {
        return cells.values().stream().filter(Cell::isWhite).collect(Collectors.toSet());
    }

    public Set<Cell> getBlackCells() {
        return cells.values().stream().filter(cell -> !cell.isWhite() && !cell.isEmpty()).collect(Collectors.toSet());
    }

    private void setCells(Map<Point, Cell> cells) {
        this.cells = cells;
    }

    public String print() {
        var sb = new StringBuilder();
        for (Point cell : cells.keySet()) {
            sb.append(cell.x).append(" ").append(cell.y).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Field clone() {
        var field = new Field();
        field.setCells(cells);
        return field;
    }
}
