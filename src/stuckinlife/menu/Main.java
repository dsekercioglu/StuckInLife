package stuckinlife.menu;


import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.GUISimplifier;

import java.io.FileInputStream;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        /*Button button = GUISimplifier.newButton("Play!",
                Color.DARKGOLDENROD,
                (a) -> {
                    //PApplet.main(StuckInLife.class.getName());
                    stage.close();
                },
                (a) -> {
                    System.out.println("hover started");
                },
                (a) -> {
                    System.out.println("hover ended");
                });

         */
        FileInputStream input = new FileInputStream("img/chars/Dragon.gif");
        Image image = new Image(input, 256, 256, true, false);
        ImageView imageView = new ImageView(image);


        Button button = new Button("Home", imageView);


        Label label = GUISimplifier.newLabel("Label comes here");
        TextArea textArea = GUISimplifier.newTextArea("Default text", 200, 400, true);

        Node[] nodes = {imageView, label, textArea};

        VBox root = GUISimplifier.newVBox(10, 10, Color.BEIGE, nodes);

        stage.setTitle("Title");
        Scene scene = new Scene(root, 800, 800);

        stage.setScene(scene);
        stage.setResizable(false);

        imageView.setOnMousePressed(mouseEvent -> {
            //System.out.println("Main.start");
            (new SecondStage()).show();
        });

        imageView.setCursor(Cursor.CROSSHAIR);

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                stage.close();
            }
        });

        stage.show();
    }

}