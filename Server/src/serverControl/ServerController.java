package serverControl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import model.DatabaseHandle;

public class ServerController {
    @FXML
    protected Button start;
    @FXML
    public void startBtn(ActionEvent event) {
        DatabaseHandle.connect();
    }
}
