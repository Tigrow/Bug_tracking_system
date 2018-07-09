package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ShowUserController {
    @FXML
    private ListView<User> listView;

    private Stage usersStage;
    private ObservableList<Task> taskList;
    private ObservableList<User> list;

    void OnStart(ObservableList<User> userList, Stage usersStage, ObservableList<Task> taskList) {
        this.taskList = taskList;
        this.usersStage = usersStage;
        this.list = userList;
        listView.setItems(list);
    }

    public void handleAddAction(ActionEvent actionEvent) {
        showDialog(false);
    }

    public void handleEditAction(ActionEvent actionEvent) {
        showDialog(true);
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        if(listView.getSelectionModel().getSelectedIndex()!=-1) {
            int taskCount = (int) taskList.stream().filter(task ->
                    task.getUserId() == listView.getSelectionModel().getSelectedItem().getId()).count();
            if (taskCount == 0 || showDialogQuestion(taskCount)) {
                list.remove(listView.getSelectionModel().getSelectedIndex());
            }
        }
    }

    private void showDialog(boolean editing) {
        User user;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("resources/adduser.fxml"));
        GridPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialogStage = new Stage();
        if (editing) {
            dialogStage.setTitle("Edit user");
            user = listView.getSelectionModel().getSelectedItem();
        } else {
            dialogStage.setTitle("Add user");
            user = new User();
        }

        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(usersStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddUserController controller = loader.getController();
        controller.start(dialogStage, user, editing);
        dialogStage.showAndWait();
        if (controller.isOkClicked()) {
            if (editing)
                list.set(listView.getSelectionModel().getSelectedIndex(), user);
            else
                list.add(user);
        }
    }

    private boolean showDialogQuestion(int count) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("This user have " + Integer.toString(count) + " tasks");
        alert.setContentText("Do you want to delete user?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

}
