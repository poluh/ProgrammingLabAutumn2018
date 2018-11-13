package checkers.logic;

import java.util.Objects;

public class Cell {
    private int x;
    private int y;

    private boolean isWhite;
    private boolean isKing = false;

    public Cell(int x, int y, boolean isWhite) {
        this.x = x;
        this.y = y;
        this.isWhite = isWhite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return getX() == cell.getX() &&
                getY() == cell.getY() &&
                isWhite() == cell.isWhite() &&
                isKing() == cell.isKing();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), isWhite(), isKing());
    }
}
