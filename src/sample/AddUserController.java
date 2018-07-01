package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class AddUserController {
    @FXML
    private Button buttonOK;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextArea textAreaName;
    @FXML
    private TextArea textAreaPassword;

    private boolean okClicked = false;
    private Stage dialogStage;
    private User user;

    public void handleCancelAction(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public User isOkClicked() {
        return user;
    }

    public void handleOkAction(ActionEvent actionEvent) {
        user = new User();
        user.setName(textAreaName.getText());
        user.setPassword(textAreaPassword.getText());
        dialogStage.close();
    }
}
