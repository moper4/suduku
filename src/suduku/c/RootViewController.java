package suduku.c;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import suduku.u.Algorithm;
import suduku.v.NumButton;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class RootViewController implements Initializable {
    public NumButton b00, b01, b02, b03, b04, b05, b06, b07, b08,
            b10, b11, b12, b13, b14, b15, b16, b17, b18,
            b20, b21, b22, b23, b24, b25, b26, b27, b28,
            b30, b31, b32, b33, b34, b35, b36, b37, b38,
            b40, b41, b42, b43, b44, b45, b46, b47, b48,
            b50, b51, b52, b53, b54, b55, b56, b57, b58,
            b60, b61, b62, b63, b64, b65, b66, b67, b68,
            b70, b71, b72, b73, b74, b75, b76, b77, b78,
            b80, b81, b82, b83, b84, b85, b86, b87, b88;

    public Button calcBtn;
    public Button clearBtn;

    private final int[][] matrix = new int[9][9];// data model [row][col]
    private NumButton[][] buttons;// [row][col]

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttons = new NumButton[][]{
                {b00, b01, b02, b03, b04, b05, b06, b07, b08},
                {b10, b11, b12, b13, b14, b15, b16, b17, b18},
                {b20, b21, b22, b23, b24, b25, b26, b27, b28},
                {b30, b31, b32, b33, b34, b35, b36, b37, b38},
                {b40, b41, b42, b43, b44, b45, b46, b47, b48},
                {b50, b51, b52, b53, b54, b55, b56, b57, b58},
                {b60, b61, b62, b63, b64, b65, b66, b67, b68},
                {b70, b71, b72, b73, b74, b75, b76, b77, b78},
                {b80, b81, b82, b83, b84, b85, b86, b87, b88}
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                NumButton button = buttons[i][j];
                button.setMatrix(matrix);
                button.setRow(i);
                button.setCol(j);
                button.setContextMenu(new PopupMenu(button, buttons));
            }
        }

        calcBtn.disableProperty().bind(NumButton.selectCount.lessThan(17));
        calcBtn.setOnAction(e -> onCalc());
        clearBtn.setOnAction(e -> onClear());
    }

    private void onCalc() {
        // 虽然测试过的最难的数独耗时也没有超过50毫秒，但阻塞UI线程终究时不妥的
        CompletableFuture.supplyAsync(() -> Algorithm.calc(matrix)).thenAcceptAsync(this::paint, Platform::runLater);
        Arrays.stream(buttons).flatMap(Arrays::stream).forEach(b -> b.setDisable(true));
    }

    private void paint(int[][] matrix) {
        if (matrix != null) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    buttons[i][j].setValue(matrix[i][j]);
                }
            }
        }
    }

    private void onClear() {
        Arrays.stream(buttons).flatMap(Arrays::stream).peek(b -> b.setValue(0)).forEach(b -> b.setDisable(false));
    }
}
