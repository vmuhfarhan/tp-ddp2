package assignments.assignment2;

import java.util.ArrayList;

public class User {
    // TODO: tambahkan attributes yang diperlukan untuk class ini
     private String nama;
     private String nomorTelepon;
     private String email;
     private String lokasi;
     public String role;
     private ArrayList<Order> orderHistory;
     private double hargaOngkir;

    public User(String nama, String nomorTelepon, String email, String lokasi, String role, double hargaOngkir){
        // TODO: buat constructor untuk class ini
        this.nama = nama;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.lokasi = lokasi;
        this.role = role;
        this.orderHistory = new ArrayList<Order>();
        this.hargaOngkir = hargaOngkir;
    }

    // TODO: tambahkan methods yang diperlukan untuk class ini
    public String getNama(){
        return this.nama;
    }

    public String getNomorTelepon(){
        return this.nomorTelepon;
    }

    public String getEmail(){
        return this.email;
    }

    public String getLokasi(){
        return this.lokasi;
    }

    public String getRole(){
        return this.role;
    }

    public ArrayList<Order> getOrderHistory() {
        return this.orderHistory;
    }

    public double getHargaOngkir() {
        return this.hargaOngkir;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setOrderList(ArrayList<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void setHargaOngkir(double hargaOngkir) {
        this.hargaOngkir = hargaOngkir;
    }
}