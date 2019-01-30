package AI;

import AI.structures.Graph;
import AI.structures.Node;
import checkers.logic.Field;
import checkers.logic.GameController;
import checkers.logic.structure.Point;

import java.util.ArrayList;
import java.util.Random;

public class AI {

    private Random random = new Random();
    private GameController gp;

    public AI(GameController gp) {
        this.gp = gp;
    }

    public boolean move(int depth) {
        if (depth == 0) return randomMove();
        return depthMove(depth, new Graph<>(), gp.getField(), gp.getField());
    }

    private boolean depthMove(int depth, Graph<Field> graph, Field lastCase, Field rootField) {
        if (depth == 0) {
            return randomMove();
        }

        var field = lastCase.clone();

        var weigth = (double) field.getBlackCells().size() / (double) field.getWhiteCells().size();
        graph.addNode(new Node<>(field, weigth));

        var possibleMoves = field.thatCanChop(gp.isWhite());
        for (Point point : possibleMoves) {
            var fieldClone = field.clone();
            var enemyMoves = field.possibleMovesWithEnemies(point, gp.isWhite());
            var emptyMoves = field.possibleEmptyMoves(point);

            var toMove = enemyMoves.isEmpty() ? (Point) emptyMoves.toArray()[0] : (Point) enemyMoves.keySet().toArray()[0];
            if (!enemyMoves.isEmpty()) {
                fieldClone.move(point, toMove, enemyMoves.get(toMove));
            } else {
                fieldClone.move(point, toMove);
            }

            var weigthClone = (double) fieldClone.getBlackCells().size() / (double) fieldClone.getWhiteCells().size();
            graph.connectNodes(new Node<>(field, weigth), new Node<>(fieldClone, weigthClone));
            return depthMove(depth - 1, graph, fieldClone, rootField);
        }
        return true;
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
