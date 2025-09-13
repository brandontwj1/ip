package omni.app;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main GUI window of the Omni application.
 * Handles user input, displays dialog boxes, and manages the interaction
 * between the user interface and the Omni application logic.
 *
 * @author Brandon Tan
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Omni omni;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/ben10.png"));
    private Image omniImage = new Image(this.getClass().getResourceAsStream("/images/omnitrix.png"));

    /**
     * Initializes the MainWindow controller.
     * Sets up the scroll pane to automatically scroll to the bottom when new content is added.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Omni instance into this controller.
     *
     * @param omni The Omni application instance to be used for processing commands.
     */
    public void setOmni(Omni omni) {
        this.omni = omni;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = omni.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getOmniDialog(response, omniImage)
        );
        userInput.clear();

        // TODO: bug fix - does not quit if arguments given with bye command
        if (input.trim().equalsIgnoreCase("bye")) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> Platform.exit());
            pause.play();
        }
    }

    /**
     * Sets the greeting message from Omni and displays it in the dialog container.
     */
    public void setGreeting() {
        dialogContainer.getChildren().addAll(DialogBox.getOmniDialog(omni.greet(), omniImage));
    }
}
