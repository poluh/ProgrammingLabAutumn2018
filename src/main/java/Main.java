import checkers.UI.Painter;
import checkers.logic.Field;
import checkers.logic.GameProcessor;
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
        var scene = new Scene(group, 400, 400);
        var canvas = new Canvas(400, 400);

        var field = new Field();
        field.init();

        group.getChildren().add(canvas);

        var painter = new Painter(canvas.getGraphicsContext2D());
        painter.printField();

        var gp = new GameProcessor(field, painter);

        canvas.setOnMouseClicked(event ->
                gp.setPoint(new Point((int) event.getX() / 50, (int) event.getY() / 50)));

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
