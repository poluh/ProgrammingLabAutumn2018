package AI;

import checkers.logic.Field;
import checkers.logic.GameProcessor;
import checkers.logic.structure.Point;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class AI {

    private Random random = new Random();
    private GameProcessor gp;

    public AI(GameProcessor gp) {
        this.gp = gp;
    }

    public boolean move(int depth) {
        if (depth == 0) return randomMove();
        return false;
    }

    private boolean randomMove() {
        var isWhite = gp.isWhite();
        var allPossibleMoves = gp.getField().thatCanChop(isWhite);

        if (allPossibleMoves.isEmpty()) return false;

        var randomPoint = new ArrayList<>(allPossibleMoves).get(random.nextInt(allPossibleMoves.size()));

        gp.setPoint(randomPoint);

        var possibleMoves = gp.getWasPossibleMoves();
        var randomMovePoint = new ArrayList<>(possibleMoves).get(random.nextInt(possibleMoves.size()));

        gp.setPoint(randomMovePoint);

        if (isWhite == gp.isWhite()) return randomMove();
        return true;
    }

}
