package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;

import java.util.function.Consumer;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp;
    private TextField nameInput;
    private TextField phoneInput;

    public LoginForm(Stage stage, MainApp mainApp) {
        this.stage = stage;
        this.mainApp = mainApp;
    }

    private Scene createLoginForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("User Name:");
        nameInput = new TextField();
        Label phoneLabel = new Label("Phone Number:");
        phoneInput = new TextField();
        Button loginButton = new Button("Login");

        grid.add(nameLabel, 0, 1);
        grid.add(nameInput, 1, 1);
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneInput, 1, 2);
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(e -> handleLogin());

        return new Scene(grid, 400, 200);
    }

    private void handleLogin() {
        String name = nameInput.getText();
        String phone = phoneInput.getText();

        User user = DepeFood.getUser(name, phone);
        if (user == null) {
            showAlert("Login Failed", "Invalid user name or phone number.", Alert.AlertType.ERROR);
        } else {
            mainApp.setUser(user);
            if (user.getRole() == "Admin") {
                mainApp.addScene("AdminMenu", new AdminMenu(stage, mainApp, user).getScene());
                mainApp.setScene(mainApp.getScene("AdminMenu"));
            } else {
                mainApp.addScene("CustomerMenu", new CustomerMenu(stage, mainApp, user).getScene());
                mainApp.setScene(mainApp.getScene("CustomerMenu"));
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return this.createLoginForm();
    }
}
