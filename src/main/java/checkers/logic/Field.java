package checkers.logic;

import checkers.logic.structure.Cell;
import checkers.logic.structure.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Field implements Cloneable {

    public static final int FIELD_SIZE = 8;
    public static final int FIELD_CENTER = FIELD_SIZE / 2;
    private Map<Point, Cell> cells;

    public Field() {
        cells = new HashMap<>();
    }

    public void init() {
        for (int x = 0; x < FIELD_SIZE; ++x) {
            for (int y = 0; y < FIELD_SIZE; ++y) {
                var point = new Point(x, y);
                var cell = new Cell(point);

                // Staggered and set color for cells
                if ((x + y) % 2 == 0 && (y < FIELD_CENTER - 1 || y > FIELD_CENTER)) {
                    cell.setWhite(y > FIELD_CENTER);
                    cell.setEmpty(false);
                }
                cells.put(point, cell);
            }
        }
    }


    /**
     * @param isWhite for check each cells such color
     * @return Set of points, that can chop-chop
     */
    public Set<Point> thatCanChop(boolean isWhite) {
        Set<Point> res;
        var checkCells = isWhite ? getWhiteCells() : getBlackCells();

        // Find all cells, that can move and kill anybody
        res = checkCells
                .stream()
                .filter(cell -> !possibleMovesWithEnemies(cell.getPoint(), isWhite).isEmpty())
                .map(Cell::getPoint).collect(Collectors.toSet());

        // Else find just way for move
        if (res.isEmpty())
            res = checkCells
                    .stream()
                    .filter(cell -> !possibleEmptyMoves(cell.getPoint()).isEmpty())
                    .map(Cell::getPoint).collect(Collectors.toSet());

        return res;
    }


    /**
     * @param point to search around her empty cells
     * @return a set of points to which can chop
     */
    public Set<Point> possibleEmptyMoves(Point point) {
        var res = new HashSet<Point>();
        if (!cells.containsKey(point) || cells.get(point).isEmpty()) return res;

        var cell = cells.get(point);

        // If is White we can just gonna down or up for black
        int dy = cell.isWhite() ? -1 : 1;
        for (var dx = -1; dx < 2; dx += 2) {
            var pointCheck = new Point(point.x + dx, point.y + dy);
            if (isNormalPoss(pointCheck) && cells.get(pointCheck).isEmpty()) {
                res.add(pointCheck);
            }
        }
        return res;
    }

    /**
     * @param point to search around her enemies
     * @param isWhite to correctly identify enemy's color
     * @return map of point to move and enemy position
     */
    public Map<Point, Point> possibleMovesWithEnemies(Point point, boolean isWhite) {
        var res = new HashMap<Point, Point>();
        if (!cells.containsKey(point) || cells.get(point).isEmpty()) return res;

        for (var dx = -1; dx < 2; dx += 2) {
            for (var dy = -1; dy < 2; dy += 2) {
                var enemyPoint = new Point(point.x + dx, point.y + dy);
                var pointToMove = new Point(enemyPoint.x + dx, enemyPoint.y + dy);

                if (isNormalPoss(pointToMove) && isNormalPoss(enemyPoint)) {
                    var cellToMove = cells.get(pointToMove);
                    var enemyCell = cells.get(enemyPoint);

                    if (cellToMove.isEmpty() && enemyCell.isWhite() != isWhite && !enemyCell.isEmpty()) {
                        res.put(pointToMove, enemyPoint);
                    }
                }
            }
        }
        return res;
    }

    public void move(Point from, Point toMove) {
        move(from, toMove, null, false);
    }

    public void move(Point from, Point toMove, Point chopPoint) {
        move(from, toMove, chopPoint, true);
    }

    /**
     * @param from point from chop
     * @param toMove point to chop
     * @param cutPoint point that can be cut
     * @param isCut cut move or jst move
     */
    private void move(Point from, Point toMove, Point cutPoint, boolean isCut) {
        if (!isNormalPoss(from) || !isNormalPoss(toMove) || (isCut && !isNormalPoss(cutPoint))) return;

        if (isCut) cells.get(cutPoint).setEmpty(true);

        var cell = cells.get(from);
        cell.setPoint(toMove);

        cells.put(toMove, cell);
        cells.put(from, new Cell(from));
    }

    public boolean isNormalPoss(Point point) {
        return point.x >= 0 && point.x < FIELD_SIZE && point.y >= 0 && point.y < FIELD_SIZE;
    }

    public Set<Cell> getWhiteCells() {
        return cells.values().stream().filter(cell -> cell.isWhite() && !cell.isEmpty()).collect(Collectors.toSet());
    }

    public Set<Cell> getBlackCells() {
        return cells.values().stream().filter(cell -> !cell.isWhite() && !cell.isEmpty()).collect(Collectors.toSet());
    }

    private void setCells(Map<Point, Cell> cells) {
        this.cells = cells;
    }


    /**
     * Print all cells to console
     */
    public void printPoints() {
        cells.values().forEach(System.out::println);
    }

    @Override
    public Field clone() {
        var field = new Field();
        field.setCells(new HashMap<>(this.cells));
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field)) return false;
        Field field = (Field) o;
        return Objects.equals(cells, field.cells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cells);
    }
}
