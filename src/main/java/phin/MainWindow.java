package phin;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Handles user interaction in the main chatbot window.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Phin phin;

    /**
     * Connects the interface to the chatbot core and shows its greeting.
     *
     * @param phin Chatbot instance used for this window.
     */
    public void setPhin(Phin phin) {
        this.phin = phin;
        String greeting = phin.isReady()
                ? "I'm Phin. Apparently I have to deal with this. What do you want?"
                : phin.getResponse("").trim();
        dialogContainer.getChildren().add(DialogBox.getPhinDialog(greeting));
    }

    /**
     * Keeps the newest message visible as the dialog grows.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Sends nonblank input to Phin when the button or Enter key is used.
     */
    @FXML
    public void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank() || phin == null) {
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getPhinDialog(phin.getResponse(input).trim()));
        userInput.clear();

        if (input.equals("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> Platform.exit());
            pause.play();
        }
    }
}
