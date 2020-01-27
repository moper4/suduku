package suduku.c;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import suduku.v.NumButton;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class PopupMenu extends ContextMenu implements Initializable {

    @FXML
    public Button b0, b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private HashSet<NumButton> siblingButtons = new HashSet<>();
    private Button[] popupButtons;
    private NumButton target;
    private NumButton[][] buttons;

    public PopupMenu(NumButton target, NumButton[][] buttons) {
        this.target = target;
        this.buttons = buttons;

        GridPane gridPane = new GridPane();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/suduku/v/menu-view.fxml"));
            loader.setResources(ResourceBundle.getBundle("suduku/v/strings"));
            loader.setRoot(gridPane);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        getItems().add(new CustomMenuItem(gridPane));
        setOnShown(e -> {
            Set<Integer> usedValues = findSibling(target).stream()
                    .map(NumButton::getValue)
                    .filter(it -> it != 0)
                    .collect(Collectors.toSet());

            for (int i = 1; i < 10; i++) {
                popupButtons[i].setTextFill(usedValues.contains(i) ? Color.RED : Color.BLACK);
            }
        });
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popupButtons = new Button[]{b0, b1, b2, b3, b4, b5, b6, b7, b8, b9};
        b0.setOnAction(e -> target.setValue(0));

        for (int i = 1; i < 10; i++) {
            Button button = popupButtons[i];
            int value = Integer.parseInt(button.getText());
            button.setOnAction(e -> {
                target.setValue(value);
                cleanSibling(value);
            });
        }
    }

    private void cleanSibling(int value) {
        siblingButtons.stream().filter(it -> it.getValue() == value).forEach(btn -> btn.setValue(0));
    }

    private Set<NumButton> findSibling(NumButton target) {
        int col = target.getCol();
        int row = target.getRow();

        siblingButtons.clear();
        for (int i = 0; i < 9; i++) {
            siblingButtons.add(buttons[i][col]);
            siblingButtons.add(buttons[row][i]);
            siblingButtons.add(buttons[row / 3 * 3 + i % 3][col / 3 * 3 + i / 3]);
        }
        siblingButtons.remove(target);

        return siblingButtons;
    }
}
