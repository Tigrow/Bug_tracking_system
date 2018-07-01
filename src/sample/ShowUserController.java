package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import sample.Repository.UserRepository;

public class ShowUserController {
    @FXML
    private ListView<User> listView;

    public void OnStart(UserRepository userRepository){
        ObservableList<User> list = FXCollections.observableList(userRepository.query(""));
        listView.setItems(list);
    }
}
