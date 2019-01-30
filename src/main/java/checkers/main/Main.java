package checkers.main;

import checkers.UI.Painter;
import checkers.logic.Field;
import checkers.logic.GameController;
import checkers.logic.structure.Point;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        var group = new Group();
        var scene = new Scene(group, Painter.CELL_SIZE * Field.FIELD_SIZE, Painter.CELL_SIZE * Field.FIELD_SIZE);
        var canvas = new Canvas(scene.getWidth(), scene.getHeight());

        var field = new Field();
        field.init();

        group.getChildren().add(canvas);

        var painter = new Painter(canvas.getGraphicsContext2D());
        painter.printField();

        var gp = new GameController(field, painter);
        gp.withAI();

        canvas.setOnMouseClicked(event -> {
            gp.setPoint(new Point((int) event.getX() / Painter.CELL_SIZE, (int) event.getY() / Painter.CELL_SIZE));
            switch (gp.winner()) {
                case BLACK:
                    painter.paintWin(GameController.Winner.BLACK.toString());
                    break;
                case WHITE:
                    painter.paintWin(GameController.Winner.WHITE.toString());
                    break;
                case DRAW:
                    painter.paintWin(GameController.Winner.DRAW.toString());
                    break;
                default:
                    break;
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
