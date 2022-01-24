import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    WelcomeScreen welcomeScreen;

    public void start(Stage primaryStage)
    {
        welcomeScreen = new WelcomeScreen();
        Scene scene = new Scene(welcomeScreen, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
        welcomeScreen.button.setOnAction(e -> {
            FEMSolver fem_solver = new FEMSolver(welcomeScreen.getN());
            fem_solver.start();
            Scene new_scene = new Scene(fem_solver.equation_chart.getChart());
            primaryStage.setScene(new_scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        });

    }

}
