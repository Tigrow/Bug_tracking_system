package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
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

    public void start(Stage dialogStage,User user,int priority,boolean editing) {
        this.user = user;
        this.dialogStage = dialogStage;
        if(editing){
            textFieldName.setText(user.getName());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void handleOkAction(ActionEvent actionEvent) {
        user.setName(textFieldName.getText());
        user.setPassword(passwordField.getPromptText());
        okClicked = true;
        dialogStage.close();
    }
}
