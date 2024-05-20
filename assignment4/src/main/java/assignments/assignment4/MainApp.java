package assignments.assignment4;

import java.util.HashMap;
import java.util.Map;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import assignments.assignment4.components.form.LoginForm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage window;
    private Map<String, Scene> allScenes = new HashMap<>();
    private static User user;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("DepeFood Ordering System");
        DepeFood.initUser(); // Initialize users

        // Initialize all scenes
        Scene loginScene = new LoginForm(window, this).getScene();

        // Populate all scenes map
        allScenes.put("Login", loginScene);

        // Set the initial scene of the application to the login scene
        setScene(loginScene);
        window.show();
    }

    public void setUser(User newUser) {
        user = newUser;
    }

    public void setScene(Scene scene) {
        window.setScene(scene);
    }

    public Scene getScene(String sceneName) {
        return allScenes.get(sceneName);
    }

    public void addScene(String sceneName, Scene scene) {
        allScenes.put(sceneName, scene);
    }

    public void logout() {
        setUser(null); // Clear the current user
        setScene(getScene("Login")); // Switch to the login scene
    }

    public static void main(String[] args) {
        launch(args);
    }
}
