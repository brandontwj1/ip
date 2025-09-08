package omni.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
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

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
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
        String response = omni.getResponse(input); // change UI object to return String values
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getOmniDialog(response, omniImage)
        );
        userInput.clear();
    }

    public void setGreeting() {
        dialogContainer.getChildren().addAll(DialogBox.getOmniDialog(omni.greet(), omniImage));
    }
}
