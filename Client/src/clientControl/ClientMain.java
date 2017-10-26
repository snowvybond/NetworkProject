package clientControl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("clientUI.fxml"));
        primaryStage.setTitle("Picture");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 792, 476));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

