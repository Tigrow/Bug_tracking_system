package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Controller {

    private UserRepository userRepository;

    public void handleKeyInput(KeyEvent keyEvent) {
    }

    public void handleAboutAction(ActionEvent actionEvent) {
    }
    public void handleLoadDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        try {
            userRepository = new UserRepository(file);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleNewDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        try {
            DataBaseHandler.getInstance().CreateDB(file);
            userRepository = new UserRepository(file);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleAddUserAction(ActionEvent actionEvent) {
        showAddUserDialog();
    }
    private void showAddUserDialog(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("resources/showUsersDialog.fxml"));
            VBox page =  loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("User editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ShowUserController controller = loader.getController();
            controller.OnStart(userRepository, dialogStage);
            dialogStage.show();
            //return null;//controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            //return null;
        }
    }

}
