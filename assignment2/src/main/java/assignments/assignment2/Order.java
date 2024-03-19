package assignments.assignment2;

import java.util.ArrayList;

public class Order {
    // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String orderID;
    private String tanggalPemesanan;
    private double biayaOngkosKirim;
    private Restaurant restaurant;
    private ArrayList<Menu> items;
    private boolean orderFinished;

   public Order(String orderId, String tanggal, double ongkir, Restaurant resto, ArrayList<Menu> items){
       // TODO: buat constructor untuk class ini
       this.orderID = orderId;
       this.tanggalPemesanan = tanggal;
       this.biayaOngkosKirim = ongkir;
       this.restaurant = resto;
       this.items = items;
       this.orderFinished = false;
   }
   
   // TODO: tambahkan methods yang diperlukan untuk class ini
   public String getOrderID(){
       return this.orderID;
   }

   public String getTanggalPemesanan(){
       return this.tanggalPemesanan;
   }

   public double getBiayaOngkosKirim(){
       return this.biayaOngkosKirim;
   }

   public Restaurant getRestaurant(){
       return this.restaurant;
   }

   public boolean getOrderFinished(){
       return this.orderFinished;
   }

   public ArrayList<Menu> getItems() {
       return this.items;
   }

   public void setOrderID(String orderID) {
       this.orderID = orderID;
   }

   public void setTanggalPemesanan(String tanggalPemesanan) {
       this.tanggalPemesanan = tanggalPemesanan;
   }

   public void setBiayaOngkosKirim(double biayaOngkosKirim) {
       this.biayaOngkosKirim = biayaOngkosKirim;
   }

   public void setRestaurant(Restaurant restaurant) {
       this.restaurant = restaurant;
   }

   public void setItems(ArrayList<Menu> items) {
       this.items = items;
   }

   public void setOrderFinished(boolean orderFinished) {
       this.orderFinished = orderFinished;
   }
}