import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Parent root = Tools.loadFromFXML("mainApplication.fxml");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Solving cubic equations");
        primaryStage.setWidth(650);
        primaryStage.setHeight(800);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

}
