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
import sample.Repository.ProjectRepository;
import sample.Repository.UserRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class Controller {

    @FXML
    private TabPane tabPane;

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private Stage primaryStage;
    private boolean isFileDbLoaded = false;
    private boolean isUserLogined = false;

    public void handleAboutAction(ActionEvent actionEvent) {
    }
    public void handleLoadDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        try {
            if(file!=null){
            userRepository = new UserRepository(file);
            projectRepository = new ProjectRepository(file);
            saveParamChanges(file);
            setTitleName(file);
            isFileDbLoaded = true;
            }
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
            if(file!=null){
            DataBaseHandler.getInstance().CreateDB(file);
            userRepository = new UserRepository(file);
            projectRepository = new ProjectRepository(file);
            saveParamChanges(file);
            setTitleName(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleAddUserAction(ActionEvent actionEvent) {
        showAddUserDialog();
    }

    public void handleAddProjectAction(Event event) {
        addNewProject();
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

    private void setTitleName(File file){
        primaryStage.setTitle("Bug Tracking System"+" [" +
                file.getAbsolutePath() +
                "]");
    }

    void loadParams(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Properties props = new Properties();
        InputStream is;
        try {
            File file = new File("prop.properties");
            is = new FileInputStream(file);
            if (is == null) {
                is = getClass().getResourceAsStream("prop.properties");
            }
            props.load(is);
            File fileDB = new File(props.getProperty("lastDB", "/TEST.db"));
            DataBaseHandler.getInstance().CreateDB(fileDB);
            userRepository = new UserRepository(fileDB);
            setTitleName(fileDB);
        }
        catch ( Exception e ) { }
    }

    private void saveParamChanges(File file) {
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

    private void addNewProject(){
        try {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("resources/projectTab.fxml"));
        Tab tab = new Tab();
        tab.setText("project");
        HBox hbox = loader.load();
        tab.setContent(hbox);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
