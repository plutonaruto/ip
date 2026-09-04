package phin;

import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Displays one user or chatbot message in the conversation.
 */
public class DialogBox extends HBox {
    private static final Image PHIN_AVATAR = new Image(
            DialogBox.class.getResource("/images/phin.png").toExternalForm(), 192, 192, true, true);
    private static final Image USER_AVATAR = new Image(
            DialogBox.class.getResource("/images/user.png").toExternalForm(), 384, 384, true, true);

    private DialogBox(String text, boolean isUser) {
        ImageView avatar = new ImageView(isUser ? USER_AVATAR : PHIN_AVATAR);
        if (isUser) {
            // Frame the face and crown without modifying the supplied full-size image.
            double side = USER_AVATAR.getWidth() * 0.48;
            avatar.setViewport(new Rectangle2D(USER_AVATAR.getWidth() * 0.24,
                    USER_AVATAR.getHeight() * 0.14, side, side));
        } else {
            avatar.setViewport(new Rectangle2D(0, 0, PHIN_AVATAR.getWidth(), PHIN_AVATAR.getHeight() / 2));
        }
        avatar.setFitWidth(48);
        avatar.setFitHeight(48);
        if (isUser) {
            avatar.setClip(new Circle(24, 24, 24));
        } else {
            avatar.setPreserveRatio(true);
        }
        avatar.setAccessibleText(isUser ? "You" : "Phin, underworked, overpaid");

        Label message = new Label(text);
        message.setWrapText(true);
        message.setMinWidth(0);
        // Leave room for the avatar and spacing when the window is narrowed.
        message.maxWidthProperty().bind(widthProperty().subtract(72));
        message.getStyleClass().add(isUser ? "user-message" : "phin-message");
        if (isUser) {
            getChildren().addAll(message, avatar);
        } else {
            getChildren().addAll(avatar, message);
        }
        setSpacing(10);
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
