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

    public void start(Stage dialogStage,User user,int priority,boolean editing) {
        this.user = user;
        this.dialogStage = dialogStage;
        if(editing){
            textAreaName.setText(user.getName());
            textAreaPassword.setText(user.getPassword());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void handleOkAction(ActionEvent actionEvent) {
        user.setName(textAreaName.getText());
        user.setPassword(textAreaPassword.getText());
        okClicked = true;
        dialogStage.close();
    }
}
