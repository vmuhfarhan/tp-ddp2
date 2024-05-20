package assignments.assignment4.components;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;

public class BillPrinter {
    private Stage stage;
    private MainApp mainApp;
    private User user;

    public BillPrinter(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
    }

    private Scene createBillPrinterForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setAlignment(Pos.CENTER);

        Label label = new Label("Enter Order ID to print the bill:");
        TextField orderIdField = new TextField();
        Button printButton = new Button("Print Bill");

        TextArea billDetails = new TextArea();
        billDetails.setEditable(false);

        printButton.setOnAction(e -> printBill(orderIdField.getText(), billDetails));

        layout.getChildren().addAll(label, orderIdField, printButton, billDetails);
        return new Scene(layout, 400, 300);
    }

    private void printBill(String orderId, TextArea billDetails) {
        Order order = DepeFood.getOrderOrNull(orderId);
        if (order == null) {
            billDetails.setText("Invalid Order ID");
        } else {
            StringBuilder bill = new StringBuilder();
            bill.append("Order ID: ").append(order.getOrderId()).append("\n");
            bill.append("Customer: ").append(user).append("\n\n");
            bill.append("Items:\n");
            for (Menu item : order.getItems()) {
                bill.append(item.getNamaMakanan()).append(" - ").append(item.getHarga()).append("\n");
            }
            bill.append("\nTotal: ").append(order.getTotalHarga());
            billDetails.setText(bill.toString());
        }
    }

    public Scene getScene() {
        return this.createBillPrinterForm();
    }

    public class MenuItem {
        private final StringProperty itemName;
        private final StringProperty price;

        public MenuItem(String itemName, String price) {
            this.itemName = new SimpleStringProperty(itemName);
            this.price = new SimpleStringProperty(price);
        }

        public StringProperty itemNameProperty() {
            return itemName;
        }

        public StringProperty priceProperty() {
            return price;
        }

        public String getItemName() {
            return itemName.get();
        }

        public String getPrice() {
            return price.get();
        }
    }
}
