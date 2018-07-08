package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;
import sample.Repository.ProjectRepository;
import sample.Repository.TaskRepository;
import sample.Repository.UserRepository;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

public class Controller {

    @FXML
    private TabPane tabPane;

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private Stage primaryStage;
    private User LoginedUser;
    private boolean isFileDbLoaded = false;
    private boolean isUserLogined = false;
    private ObservableList<Project> projectList;
    private ObservableList<Task> taskList;
    private ObservableList<User> userList;


    public void handleLoadDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null)
            loadDataBase(file);

    }

    public void handleNewDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null)
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
            controller.OnStart(userList, dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setTitleName(File file) {
        primaryStage.setTitle("Bug Tracking System" + " [" +
                file.getAbsolutePath() +
                "]");
    }

    private void loadDataBase(File file) {
        try {
            if (file != null) {
                DataBaseHandler.getInstance().CreateDB(file);
                userRepository = new UserRepository(file);
                projectRepository = new ProjectRepository(file);
                taskRepository = new TaskRepository(file);
                saveParamChanges(file);
                setTitleName(file);
                projectList = FXCollections.observableList(projectRepository.query(""));
                userList = FXCollections.observableList(userRepository.query(""));
                taskList = FXCollections.observableList(taskRepository.query(""));
                tabPane.getTabs().clear();
                for (Project aProjectList : projectList) {
                    addTabProject(aProjectList);
                }
                userList.addListener((ListChangeListener<User>) c -> {
                    while (c.next()) {
                        if (c.wasReplaced()) {
                            for (int i = c.getFrom(); i < c.getTo(); i++) {
                                userRepository.update(c.getList().get(i));
                            }
                        } else {
                            if (c.wasRemoved()) {
                                c.getRemoved().forEach(user -> userRepository.remove(user));
                            }
                            if (c.wasAdded()) {
                                int userId = userRepository.add(c.getList().get(c.getFrom())).getId();
                                c.getList().get(c.getFrom()).setId(userId);
                            }
                        }
                    }
                });
                taskList.addListener((ListChangeListener<Task>) c -> {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            int taskId = taskRepository.add(c.getList().get(c.getFrom())).getId();
                            c.getList().get(c.getFrom()).setId(taskId);
                        }
                        if (c.wasRemoved()) {
                            c.getRemoved().forEach(task -> taskRepository.remove(task));
                        }
                    }
                });
                projectList.addListener((ListChangeListener<Project>) c -> {
                    while(c.next()){
                        if (c.wasAdded()) {
                            int projectId = projectRepository.add(c.getList().get(c.getFrom())).getId();
                            c.getList().get(c.getFrom()).setId(projectId);
                        }
                        if (c.wasRemoved()) {
                            c.getRemoved().forEach(project -> projectRepository.remove(project));
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            isFileDbLoaded = false;
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
        primaryStage.setOnCloseRequest(event -> saveDB());
    }

    private void saveDB() {
        projectList.forEach(project ->
                projectRepository.update(project));
        taskList.forEach(task ->
                taskRepository.update(task));
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
            projectController.setData(tab, project, taskList, userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
