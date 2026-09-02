package phin;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one user or chatbot message in the conversation.
 */
public class DialogBox extends HBox {
    private DialogBox(String text, boolean isUser) {
        Label message = new Label(text);
        message.setWrapText(true);
        message.setMaxWidth(430);
        message.getStyleClass().add(isUser ? "user-message" : "phin-message");
        getChildren().add(message);
        setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        getStyleClass().add("dialog-box");
    }

    /**
     * Creates a right-aligned user message.
     *
     * @param text User input to display.
     * @return Styled dialog box.
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, true);
    }

    /**
     * Creates a left-aligned Phin response.
     *
     * @param text Chatbot response to display.
     * @return Styled dialog box.
     */
    public static DialogBox getPhinDialog(String text) {
        return new DialogBox(text, false);
    }
}
