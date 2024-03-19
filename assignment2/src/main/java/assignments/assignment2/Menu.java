package assignments.assignment2;

public class Menu {
    // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String namaMakanan;
    private double harga;

   public Menu(String namaMakanan, double harga){
       // TODO: buat constructor untuk class ini
       this.namaMakanan = namaMakanan;
       this.harga = harga;
   }

   // TODO: tambahkan methods yang diperlukan untuk class ini
   public String getNamaMakanan(){
       return this.namaMakanan;
   }

   public double getHarga(){
       return this.harga;
   }

   public void setNamaMakanan(String namaMakanan) {
       this.namaMakanan = namaMakanan;
   }

   public void setHarga(double harga) {
       this.harga = harga;
   }
}