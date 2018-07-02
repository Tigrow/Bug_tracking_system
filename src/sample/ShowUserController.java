package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Repository.UserRepository;

import java.io.IOException;

public class ShowUserController {
    @FXML
    private ListView<User> listView;

    private UserRepository userRepository;
    private Stage usersStage;

    void OnStart(UserRepository userRepository, Stage usersStage){
        this.usersStage = usersStage;
        this.userRepository = userRepository;
        ObservableList<User> list = FXCollections.observableList(userRepository.query(""));
        listView.setItems(list);
    }

    public void handleAddAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("resources/adduser.fxml"));
        GridPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialogStage = new Stage();
        dialogStage.setTitle("User");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(usersStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddUserController controller = loader.getController();
        //controller.OnStart(userRepository, dialogStage);
        dialogStage.showAndWait();
    }

    public void handleEditAction(ActionEvent actionEvent) {
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
    }
}
