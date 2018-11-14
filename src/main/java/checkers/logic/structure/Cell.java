package checkers.logic.structure;

import java.util.Objects;

public class Cell {

    private Point point;
    private boolean isWhite;
    private boolean isKing = false;
    private boolean isEmpty = true;

    public Cell(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setWhite(boolean white) {
        isWhite = white;
        isEmpty = false;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        var cell = (Cell) o;
        return point.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, isWhite(), isKing());
    }
}
