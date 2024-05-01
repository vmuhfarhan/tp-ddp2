package assignments.assignment3;

import assignments.assignment2.Order;
import assignments.assignment3.payment.DepeFoodPaymentSystem;

import java.util.ArrayList;

public class User {
    
    private String nama;
    private String nomorTelepon;
    private String email;
    private String lokasi;
    private ArrayList<Order> orderHistory;
    public String role;
    private long saldo;
    private DepeFoodPaymentSystem payment;

    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem payment, long saldo){
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        this.payment = payment;
        this.saldo = saldo;
        orderHistory = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }
    public String getNama() {
        return nama;
    }
    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

    public String getLokasi() {
        return lokasi;
    }
    public String getNomorTelepon() {
        return nomorTelepon;
    }
    public DepeFoodPaymentSystem getPayment() {
        return payment;
    }

    public long getSaldo(){
        return saldo;
    }
    public void addOrderHistory(Order order){
        orderHistory.add(order);
    }
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
    public void setOrderHistory(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public boolean isOrderBelongsToUser(String orderId) {
        for (Order order : orderHistory) {
            if (order.getOrderId().equals(orderId)) {
                return true;
            }
        }
        return false;
    }
}
