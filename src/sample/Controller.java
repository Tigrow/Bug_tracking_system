package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Repository.UserRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class Controller {

    @FXML
    TabPane tabPane;

    private UserRepository userRepository;

    public void loadView(){
        File file = loadParams();
        try {
            userRepository = new UserRepository(file);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
            saveParamChanges(file);
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

    public File loadParams() {
        Properties props = new Properties();
        InputStream is = null;
        try {
            File file = new File("prop.properties");
            is = new FileInputStream(file);
        }
        catch ( Exception e ) { is = null; }
        try {
            if (is == null) {
                is = getClass().getResourceAsStream("prop.properties");
            }
            props.load(is);
        }
        catch ( Exception e ) { }
        return new File(props.getProperty("lastDB", "/TEST.db"));
    }

    public void saveParamChanges(File file) {
        try {
            Properties props = new Properties();
            props.setProperty("lastDB", ""+file.getAbsolutePath());
            File f = new File("prop.properties");
            OutputStream out = new FileOutputStream(f);
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public void handleAddProjectAction(Event event) {
        addNewTab();
    }

    private void addNewTab(){
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("resources/projectTab.fxml"));
        Tab tab = new Tab();
        tab.setText("project");
        HBox hbox = loader.load();
        tab.setContent(hbox);
        tabPane.getTabs().add(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
