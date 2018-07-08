package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
    @FXML
    private Button buttonOK;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField textFieldName;
    @FXML
    private PasswordField passwordField;

    private boolean okClicked = false;
    private Stage dialogStage;
    private User user;

    public void handleCancelAction(ActionEvent actionEvent) {
        dialogStage.close();
    }

    void start(Stage dialogStage, User user, boolean editing) {
        this.user = user;
        this.dialogStage = dialogStage;
        if(editing){
            textFieldName.setText(user.getName());
        }
    }

    boolean isOkClicked() {
        return okClicked;
    }

    public void handleOkAction(ActionEvent actionEvent) {
        user.setName(textFieldName.getText());
        user.setPassword(passwordField.getText());
        okClicked = true;
        dialogStage.close();
    }
}
