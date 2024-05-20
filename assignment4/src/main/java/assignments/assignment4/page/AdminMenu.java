package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.Menu;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private MainApp mainApp;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private ObservableList<String> restaurantNames;

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
        this.restaurantNames = FXCollections.observableArrayList(
            DepeFood.getAllRestaurants().stream()
                .map(Restaurant::getNama)
                .collect(Collectors.toList())
        );
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20, 20, 20, 20));
        menuLayout.setAlignment(Pos.CENTER);

        Button addRestaurantButton = new Button("Add Restaurant");
        Button addMenuButton = new Button("Add Menu Item");
        Button viewRestaurantsButton = new Button("View Restaurants");

        addRestaurantButton.setOnAction(e -> mainApp.setScene(addRestaurantScene));
        addMenuButton.setOnAction(e -> mainApp.setScene(addMenuScene));
        viewRestaurantsButton.setOnAction(e -> {
            refresh();
            mainApp.setScene(viewRestaurantsScene); 
        });

        menuLayout.getChildren().addAll(addRestaurantButton, addMenuButton, viewRestaurantsButton);
        return new Scene(menuLayout, 400, 300);
    }

    private Scene createAddRestaurantForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Restaurant Name:");
        TextField nameField = new TextField();
        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> handleTambahRestoran(nameField.getText()));
        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(nameLabel, nameField, submitButton, backButton);
        return new Scene(layout, 400, 300);
    }
    
    private Scene createAddMenuForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label restaurantLabel = new Label("Restaurant Name:");
        TextField restaurantField = new TextField();
        Label itemLabel = new Label("Menu Item Name:");
        TextField itemField = new TextField();
        Label priceLabel = new Label("Price:");
        TextField priceField = new TextField();
        Button submitButton = new Button("Add Menu Item");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> handleTambahMenuRestoran(
            DepeFood.getRestaurantByName(restaurantField.getText()), 
            itemField.getText(), 
            priceField.getText().isEmpty() ? -1 : Double.parseDouble(priceField.getText())
        ));
        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(restaurantLabel, restaurantField, itemLabel, itemField, priceLabel, priceField, submitButton, backButton);
        return new Scene(layout, 400, 300);
    }

    private Scene createViewRestaurantsForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        ListView<String> restaurantListView = new ListView<>(restaurantNames);
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(new Label("Registered Restaurants:"), restaurantListView, backButton);
        return new Scene(layout, 400, 300);
    }

    private void handleTambahRestoran(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            showAlert("Add Restaurant Failed", "Restaurant name cannot be empty.", Alert.AlertType.ERROR);
            return;
        }

        Restaurant existingRestaurant = DepeFood.getRestaurantByName(nama);
        if (existingRestaurant != null) {
            showAlert("Add Restaurant Failed", "Restaurant already exists.", Alert.AlertType.ERROR);
        } else {
            Restaurant newRestaurant = new Restaurant(nama);
            DepeFood.addRestaurant(newRestaurant);
            restaurantNames.add(newRestaurant.getNama());
            showAlert("Add Restaurant Success", "Restaurant added successfully.", Alert.AlertType.INFORMATION);
        }
    }

    private void handleTambahMenuRestoran(Restaurant restaurant, String itemName, double price) {
        if (restaurant == null || itemName.isEmpty() || price <= 0) {
            showAlert("Add Menu Item Failed", "Invalid restaurant, item name, or price.", Alert.AlertType.ERROR);
        } else {
            restaurant.addMenu(new Menu(itemName, price));
            showAlert("Add Menu Item Success", "Menu item added successfully.", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refresh() {
        restaurantNames.setAll(
            DepeFood.getAllRestaurants().stream()
                .map(Restaurant::getNama)
                .collect(Collectors.toList())
        );
    }

    public Scene getScene() {
        return scene;
    }
}
