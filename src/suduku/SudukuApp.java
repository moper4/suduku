package suduku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class SudukuApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("suduku/v/strings");

        Parent root = FXMLLoader.load(getClass().getResource("/suduku/v/root-view.fxml"), bundle);
        root.getStylesheets().add(getClass().getResource("/suduku/v/style.css").toExternalForm());

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle(bundle.getString("title"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
