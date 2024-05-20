package assignments.assignment4.page;

import javafx.collections.ObservableList;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu {
    private Stage stage;
    private MainApp mainApp;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter;
    private User user;
    private ObservableList<String> menuItems;

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.menuItems = FXCollections.observableArrayList(
            DepeFood.getAllMenuItems().stream()
                .map(Menu::getNamaMakanan)
                .collect(Collectors.toList())
        );
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user);
        this.printBillScene = billPrinter.getScene();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
    }

    @Override
    public Scene createBaseMenu() {
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20, 20, 20, 20));
        menuLayout.setAlignment(Pos.CENTER);

        Button addOrderButton = new Button("Create Order");
        Button printBillButton = new Button("Print Bill");
        Button payBillButton = new Button("Pay Bill");
        Button cekSaldoButton = new Button("Check Balance");

        addOrderButton.setOnAction(e -> mainApp.setScene(addOrderScene));
        printBillButton.setOnAction(e -> mainApp.setScene(printBillScene));
        payBillButton.setOnAction(e -> mainApp.setScene(payBillScene));
        cekSaldoButton.setOnAction(e -> mainApp.setScene(cekSaldoScene));

        menuLayout.getChildren().addAll(addOrderButton, printBillButton, payBillButton, cekSaldoButton);
        return new Scene(menuLayout, 400, 300);
    }

    private Scene createTambahPesananForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label restaurantLabel = new Label("Restaurant Name:");
        TextField restaurantField = new TextField();
        Label dateLabel = new Label("Order Date:");
        TextField dateField = new TextField();
        Label menuItemsLabel = new Label("Menu Items:");
        ListView<String> menuItemsList = new ListView<>(menuItems);
        Button submitButton = new Button("Submit Order");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> handleBuatPesanan(restaurantField.getText(), dateField.getText(), menuItemsList.getSelectionModel().getSelectedItems()));
        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(restaurantLabel, restaurantField, dateLabel, dateField, menuItemsLabel, menuItemsList, submitButton, backButton);
        return new Scene(layout, 400, 400);
    }

    private Scene createBillPrinter() {
        return billPrinter.getScene();
    }

    private Scene createBayarBillForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label orderIdLabel = new Label("Order ID:");
        TextField orderIdField = new TextField();
        Label paymentOptionLabel = new Label("Payment Option:");
        ComboBox<String> paymentOptionComboBox = new ComboBox<>(FXCollections.observableArrayList("Credit Card", "Debit Card"));
        Button submitButton = new Button("Pay Bill");
        Button backButton = new Button("Back");

        submitButton.setOnAction(e -> handleBayarBill(orderIdField.getText(), paymentOptionComboBox.getSelectionModel().getSelectedIndex()));
        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(orderIdLabel, orderIdField, paymentOptionLabel, paymentOptionComboBox, submitButton, backButton);
        return new Scene(layout, 400, 400);
    }

    private Scene createCekSaldoScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label balanceLabel = new Label("Current Balance:");
        Label balanceValue = new Label(String.valueOf(user.getSaldo()));
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> mainApp.setScene(scene));

        layout.getChildren().addAll(balanceLabel, balanceValue, backButton);
        return new Scene(layout, 400, 200);
    }

    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
        Restaurant restaurant = DepeFood.getRestaurantByName(namaRestoran);
        if (restaurant == null || tanggalPemesanan.isEmpty() || menuItems.isEmpty()) {
            showAlert("Order Failed", "Invalid restaurant, date, or no menu items selected.", AlertType.ERROR);
        } else {
            DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, 1, menuItems);
            showAlert("Order Success", "Order created successfully.", AlertType.INFORMATION);
        }
    }

    private void handleBayarBill(String orderID, int pilihanPembayaran) {
        String selectedPaymentOption = null;
        if (pilihanPembayaran == 0) {
            selectedPaymentOption = "Credit Card";
        } else if (pilihanPembayaran == 1) {
            selectedPaymentOption = "Debit Card";
        }

        if (orderID.isEmpty() || selectedPaymentOption == null) {
            showAlert("Payment Failed", "Invalid order ID or payment option.", AlertType.ERROR);
        } else {
            DepeFood.handleBayarBill(orderID, selectedPaymentOption);
            showAlert("Payment Success", "Bill paid successfully.", AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refresh() {
        menuItems.setAll(
            DepeFood.getAllMenuItems().stream()
                .map(Menu::getNamaMakanan)
                .collect(Collectors.toList())
        );
    }

    public Scene getScene() {
        return scene;
    }
}
