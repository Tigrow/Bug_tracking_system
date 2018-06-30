package sample;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
    public void handleKeyInput(KeyEvent keyEvent) {
        //
    }

    public void handleAboutAction(ActionEvent actionEvent) {
    }

    public void handleNewDBAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DB files (*.DB)", "*.DB");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);
        try {
            DataBaseHandler.getInstance().CreateDB(file);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
