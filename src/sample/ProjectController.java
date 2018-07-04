package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import sample.Repository.ProjectRepository;

public class ProjectController {
    public TextField textFieldName;

    private Tab tab;
    private Project project;
    private ProjectRepository projectRepository;

    void setData(Tab tab, Project project, ProjectRepository projectRepository) {
        this.tab = tab;
        this.project = project;
        this.projectRepository = projectRepository;
        tab.setText(project.getName());
        textFieldName.setText(project.getName());
    }

    public void handleKeyReleasedAction(KeyEvent keyEvent) {
        project.setName(textFieldName.getText());
        tab.setText(project.getName());
    }

    private void saveAll(){
        projectRepository.update(project);
    }

    public void handleSaveAction(ActionEvent actionEvent) {
        saveAll();
    }
}
