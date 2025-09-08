package omni.app;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The main JavaFX application class for the Omni task management GUI.
 * Handles the initialization and setup of the primary stage and scene.
 * Sets up the FXML loader and connects the controller with the Omni instance.
 *
 * @author Brandon Tan
 */
public class Main extends Application {

    Path filePath = Paths.get("data", "tasks.txt");
    private Omni omni = new Omni(filePath);

    /**
     * Starts the JavaFX application by setting up the primary stage.
     * Loads the FXML layout, creates the scene, and initializes the controller
     * with the Omni instance and greeting message.
     *
     * @param stage The primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setOmni(omni);
            fxmlLoader.<MainWindow>getController().setGreeting();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
