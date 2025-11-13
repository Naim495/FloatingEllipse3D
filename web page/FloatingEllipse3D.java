// FloatingEllipse3D.java
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FloatingEllipse3D extends Application {
    @Override
    public void start(Stage stage) {
        Group root3D = new Group();

        // 2D Ellipse used as a 3D object (placed inside a SubScene with a PerspectiveCamera)
        Ellipse ellipse = new Ellipse(0, 0, 140, 90); // centerX, centerY, radiusX, radiusY
        ellipse.setFill(Color.WHITE);
        ellipse.setStroke(Color.LIGHTGRAY);
        ellipse.setStrokeWidth(1.2);
        ellipse.setEffect(new DropShadow(20, Color.gray(0, 0.6)));

        // Place ellipse at origin of the 3D group
        ellipse.setTranslateX(0);
        ellipse.setTranslateY(0);
        ellipse.setTranslateZ(0);

        // Put ellipse inside a container group so transforms behave nicer
        Group ellipseHolder = new Group(ellipse);
        // move the object a bit forward so camera can see it nicely
        ellipseHolder.setTranslateZ(200);

        // Add lights
        PointLight point = new PointLight(Color.WHITE);
        point.setTranslateX(-200);
        point.setTranslateY(-100);
        point.setTranslateZ(-300);

        AmbientLight ambient = new AmbientLight(Color.color(0.35, 0.35, 0.35));

        root3D.getChildren().addAll(ellipseHolder, point, ambient);

        // SubScene with depth buffer and a PerspectiveCamera
        SubScene sub = new SubScene(root3D, 900, 600, true, SceneAntialiasing.BALANCED);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-800);   // pull camera back
        camera.setTranslateY(-40);    // slightly above center
        camera.setNearClip(0.1);
        camera.setFarClip(3000);
        sub.setCamera(camera);

        Group root = new Group(sub);
        Scene scene = new Scene(root, 900, 600, Color.web("#222"));

        stage.setScene(scene);
        stage.setTitle("Floating White Ellipse in 3D");
        stage.show();

        // --- Animations ---

        // 1) Smooth up-down floating (Y)
        TranslateTransition floatY = new TranslateTransition(Duration.seconds(2.8), ellipseHolder);
        floatY.setByY(-50);
        floatY.setAutoReverse(true);
        floatY.setCycleCount(Animation.INDEFINITE);
        floatY.setInterpolator(Interpolator.EASE_BOTH);

        // 2) Gentle in-out (Z) bobbing to emphasize 3D
        Timeline bobZ = new Timeline(
            new KeyFrame(Duration.ZERO,    new KeyValue(ellipseHolder.translateZProperty(), 200)),
            new KeyFrame(Duration.seconds(3.6), new KeyValue(ellipseHolder.translateZProperty(), 120))
        );
        bobZ.setAutoReverse(true);
        bobZ.setCycleCount(Animation.INDEFINITE);
        bobZ.setInterpolator(Interpolator.EASE_BOTH);

        // 3) Slow rotation around Y axis for subtle 3D feeling
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        ellipseHolder.getTransforms().add(rotateY);
        Timeline spin = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(rotateY.angleProperty(), 0)),
            new KeyFrame(Duration.seconds(12), new KeyValue(rotateY.angleProperty(), 360))
        );
        spin.setCycleCount(Animation.INDEFINITE);
        spin.setInterpolator(Interpolator.LINEAR);

        floatY.play();
        bobZ.play();
        spin.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
