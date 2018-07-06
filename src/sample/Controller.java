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
import sample.Repository.TaskRepository;
import sample.Repository.UserRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Controller {

    @FXML
    private TabPane tabPane;

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private Stage primaryStage;
    private boolean isFileDbLoaded = false;
    private boolean isUserLogined = false;
    private List<Project> projectList;


    public void handleLoadDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        loadDataBase(file);

    }

    public void handleNewDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        loadDataBase(file);
    }

    public void handleAddUserAction(ActionEvent actionEvent) {
        showAddUserDialog();
    }

    public void handleAddProjectAction(Event event) {
        Project project = new Project();
        project = projectRepository.add(project);
        projectList.add(project);
        addTabProject(project);
    }

    public void handleDeleteProjectAction(ActionEvent actionEvent) {
        projectRepository.remove(projectList.get(tabPane.getSelectionModel().getSelectedIndex()));
        projectList.remove(tabPane.getSelectionModel().getSelectedIndex());
        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
    }

    private void showAddUserDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("resources/showUsersDialog.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("User editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(null);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ShowUserController controller = loader.getController();
            controller.OnStart(userRepository, dialogStage);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitleName(File file) {
        primaryStage.setTitle("Bug Tracking System" + " [" +
                file.getAbsolutePath() +
                "]");
    }

    private void loadDataBase(File file){
        try {
            if (file != null) {
                DataBaseHandler.getInstance().CreateDB(file);
                userRepository = new UserRepository(file);
                projectRepository = new ProjectRepository(file);
                taskRepository = new TaskRepository(file);
                saveParamChanges(file);
                setTitleName(file);
                isFileDbLoaded = true;
                tabPane.getTabs().clear();
                projectList = projectRepository.query("");
                for (Project aProjectList : projectList) {
                    addTabProject(aProjectList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void loadParams(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Properties props = new Properties();
        InputStream is;
        try {
            File file = new File("prop.properties");
            is = new FileInputStream(file);
            props.load(is);
            File fileDB = new File(props.getProperty("lastDB", "/TEST.db"));
            loadDataBase(fileDB);
        } catch (Exception ignored) {
        }
    }

    private void saveParamChanges(File file) {
        try {
            Properties props = new Properties();
            props.setProperty("lastDB", "" + file.getAbsolutePath());
            File f = new File("prop.properties");
            OutputStream out = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTabProject(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("resources/projectTab.fxml"));
            Tab tab = new Tab();
            HBox hbox = loader.load();
            tab.setContent(hbox);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            ProjectController projectController = loader.getController();
            projectController.setData(tab, project, projectRepository,taskRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
