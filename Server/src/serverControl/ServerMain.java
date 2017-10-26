package serverControl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("serverUI.fxml"));
        primaryStage.setTitle("Picture Server");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 561, 325));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
