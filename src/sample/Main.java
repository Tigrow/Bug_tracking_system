package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("resources/menu.fxml"));
        VBox page =  loader.load();
        primaryStage.setTitle("Bug Tracking System");
        primaryStage.setScene(new Scene(page, 750, 500));
        primaryStage.show();

        Controller controller = loader.getController();
        controller.loadParams(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
