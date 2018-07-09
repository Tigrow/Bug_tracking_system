package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

public class ProjectController {
    public TextField textFieldName;
    public ListView<Task> listViewTask;
    public TextField textFieldSubject;
    public TextField textFieldType;
    public ChoiceBox<String> choiceBoxPriority;
    public ChoiceBox<User> choiceBoxExecutor;
    public TextArea textAreaDescription;

    private Tab tab;
    private Project project;
    private ObservableList<Task> taskList;

    void setData(Tab tab, Project project, ObservableList<Task> taskList, ObservableList<User> userList) {
        this.tab = tab;
        this.project = project;
        this.taskList = taskList;
        setUIDisable(true);
        choiceBoxExecutor.setItems(userList);
        choiceBoxPriority.setItems(FXCollections.observableArrayList("low", "medium", "hight"));
        tab.setText(project.getName());
        textFieldName.setText(project.getName());
        listViewTask.setItems(taskList.filtered(user -> user.getProjectId() == project.getId()));
        listViewTask.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new ListCell<Task>() {
                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null)
                            setText(item.getSubject());
                        else
                            setText(null);
                    }
                };
            }
        });
        listViewTask.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                textFieldSubject.setText(newValue.getSubject());
                textFieldType.setText(newValue.getType());
                textAreaDescription.setText(newValue.getDescription());
                choiceBoxPriority.getSelectionModel().select(newValue.getPriority());
                for (int i = 0; i < choiceBoxExecutor.getItems().size(); i++) {
                    if (choiceBoxExecutor.getItems().get(i).getId() == newValue.getUserId()) {
                        choiceBoxExecutor.getSelectionModel().select(i);
                        break;
                    } else {
                        choiceBoxExecutor.getSelectionModel().clearSelection();
                    }
                }
                setUIDisable(false);
            } else {
                setUIDisable(true);
            }
        });
        choiceBoxExecutor.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                listViewTask.getSelectionModel().getSelectedItem().setUserId(newValue.getId());
        });

        choiceBoxPriority.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                listViewTask.getSelectionModel()
                        .getSelectedItem().setPriority(choiceBoxPriority.getSelectionModel().getSelectedIndex());
        });

        textFieldSubject.setOnKeyReleased(event ->
                taskList.forEach(task -> {
                    listViewTask.getSelectionModel().getSelectedItem().setSubject(textFieldSubject.getText());
                    listViewTask.refresh();
                }));

        textAreaDescription.setOnKeyReleased(event ->
                listViewTask.getSelectionModel().getSelectedItem().setDescription(textAreaDescription.getText()));

        textFieldType.setOnKeyReleased(event ->
                listViewTask.getSelectionModel().getSelectedItem().setType(textFieldType.getText()));
    }

    private void setUIDisable(boolean b) {
        textFieldType.setDisable(b);
        textFieldSubject.setDisable(b);
        textAreaDescription.setDisable(b);
        choiceBoxExecutor.setDisable(b);
        choiceBoxPriority.setDisable(b);
        if (b) {
            choiceBoxPriority.getSelectionModel().clearSelection();
            choiceBoxExecutor.getSelectionModel().clearSelection();
        }
    }

    public void handleAddTaskAction(ActionEvent actionEvent) {
        Task task = new Task();
        task.setProjectId(project.getId());
        task.setSubject("new Task");
        taskList.add(task);
    }

    public void handleKeyReleasedProjectAction(KeyEvent keyEvent) {
        project.setName(textFieldName.getText());
        tab.setText(project.getName());
    }

    public void handleDeleteAction(ActionEvent actionEvent) {
        if (listViewTask.getSelectionModel().getSelectedItem() != null)
            taskList.remove(listViewTask.getSelectionModel().getSelectedIndex());
    }
}
