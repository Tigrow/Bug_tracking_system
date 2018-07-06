package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import sample.Repository.ProjectRepository;
import sample.Repository.TaskRepository;

public class ProjectController {
    public TextField textFieldName;
    public ListView<Task> listViewTask;
    public TextField textFieldSubject;
    public TextField textFieldType;
    public ChoiceBox choiceBoxPriority;
    public ChoiceBox choiceBoxExecutor;
    public TextArea textAreaDescription;

    private Tab tab;
    private Project project;
    private ProjectRepository projectRepository;
    private TaskRepository taskRepository;
    private ObservableList<Task> list;

    void setData(Tab tab, Project project, ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.tab = tab;
        this.project = project;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        tab.setText(project.getName());
        textFieldName.setText(project.getName());
        list = FXCollections.observableList(
                taskRepository.query("where project_id = "+ Integer.toString(project.getId())));
        listViewTask.setItems(list);
        listViewTask.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null)
                            setText(item.getSubject());
                        else
                            setText(null);
                    }
                };
            }
        });
        listViewTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            textFieldSubject.setText(newValue.getSubject());
            textFieldType.setText(newValue.getType());
            //TODO choiceBoxPriority and choiceBoxExecutor
            textAreaDescription.setText(newValue.getDescription());
        });
    }

    private void saveAll(){
        projectRepository.update(project);
    }

    public void handleSaveAction(ActionEvent actionEvent) {
        saveAll();
    }

    public void handleAddTaskAction(ActionEvent actionEvent) {
        Task task = new Task();
        task.setProjectId(project.getId());
        task.setSubject("new Task");
        task = taskRepository.add(task);
        list.add(task);
    }

    public void handleKeyReleasedProjectAction(KeyEvent keyEvent) {
        project.setName(textFieldName.getText());
        tab.setText(project.getName());
    }

    public void handleKeyReleasedSubjectAction(KeyEvent keyEvent) {
        //TODO

    }
}
