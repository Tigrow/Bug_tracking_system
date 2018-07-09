package sample;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import java.util.Optional;
import java.util.Properties;

public class Controller {

    @FXML
    private TabPane tabPane;

    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private Stage primaryStage;
    private boolean isFileDbLoaded = false;
    private ObservableList<Project> projectList;
    private ObservableList<Task> taskList;
    private ObservableList<User> userList;


    public void handleLoadDBAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null)
            loadDataBase(file);

    }

    public void handleNewDBAction() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null)
            loadDataBase(file);
    }

    public void handleAddUserAction() {
        showAddUserDialog();
    }

    public void handleAddProjectAction() {
        Project project = new Project();
        project = projectRepository.add(project);
        projectList.add(project);
        addTabProject(project);
    }

    public void handleDeleteProjectAction() {
        if(tabPane.getSelectionModel().getSelectedIndex()!=-1) {
            int taskCount = (int) taskList.stream().filter(task ->
                    task.getProjectId() == projectList.get(tabPane.getSelectionModel()
                            .getSelectedIndex()).getId()).count();
            if (taskCount == 0 || showDialogQuestion(taskCount)) {
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getProjectId() ==
                            projectList.get(tabPane.getSelectionModel().getSelectedIndex()).getId()) {
                        taskList.remove(i);
                    }
                }
                projectList.remove(tabPane.getSelectionModel().getSelectedIndex());
                tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            }
        }
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
            controller.OnStart(userList, dialogStage, taskList);
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
                            c.getRemoved().forEach(project->projectRepository.remove(project));
                        }
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            isFileDbLoaded = false;
        }
    }

    private boolean showDialogQuestion(int count){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("There are "+Integer.toString(count)+" tasks in the project");
        alert.setContentText("Do you want to delete this?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
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
            props.store(out,"last used file path");
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
