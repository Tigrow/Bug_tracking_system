package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Repository.UserRepository;

import java.io.IOException;

public class ShowUserController {
    @FXML
    private ListView<User> listView;

    private UserRepository userRepository;
    private Stage usersStage;
    private ObservableList<User> list;

    void OnStart(UserRepository userRepository, Stage usersStage){
        this.usersStage = usersStage;
        this.userRepository = userRepository;
        list = FXCollections.observableList(userRepository.query(""));
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
        dialogStage.setTitle("Add user");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(usersStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddUserController controller = loader.getController();
        User user = new User();
        controller.start(dialogStage,user,0,false);
        dialogStage.showAndWait();
        if(controller.isOkClicked()){
            userRepository.add(user);
            list.add(user);
        }
    }

    public void handleEditAction(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("resources/adduser.fxml"));
        GridPane page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit user");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(usersStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AddUserController controller = loader.getController();
        User user = listView.getSelectionModel().getSelectedItem();
        controller.start(dialogStage,user,0,true);
        dialogStage.showAndWait();
        if(controller.isOkClicked()){
            userRepository.update(user);
            list.set(listView.getSelectionModel().getSelectedIndex(),user);
        }
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        userRepository.remove(listView.getSelectionModel().getSelectedItem());
        list.remove(listView.getSelectionModel().getSelectedIndex());
    }
}
