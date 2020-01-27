package suduku.v;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;

public class NumButton extends Button {
    public static SimpleIntegerProperty selectCount = new SimpleIntegerProperty(0);

    private int[][] matrix;
    private int column;
    private int row;
    private int value;

    public NumButton() {
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);
        setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ContextMenu contextMenu = getContextMenu();
                if (contextMenu != null) {
                    contextMenu.show(this, e.getScreenX(), e.getScreenY());
                }
            }
        });
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getCol() {
        return column;
    }

    public void setCol(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (this.value == 0 && value != 0) {
            selectCount.set(selectCount.get() + 1);
        } else if (this.value != 0 && value == 0) {
            selectCount.set(selectCount.get() - 1);
        }

        setText(value == 0 ? "" : Integer.toString(value));
        this.value = value;
        matrix[row][column] = value;
    }
}
