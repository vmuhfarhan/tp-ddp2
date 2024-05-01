package assignments.assignment1;

import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /* 
    Anda boleh membuat method baru sesuai kebutuhan Anda
    Namun, Anda tidak boleh menghapus ataupun memodifikasi return type method yang sudah ada.
    */

    // *** Method untuk menampilkan banner (1x execute saat awal program) ***
    public static void showBanner(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
    }

    // *** Method untuk menampilkan menu (selalu ter-execute sampai option = exit) ***
    public static void showMenu(){
        System.out.println("Pilih menu:");
        System.out.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");
        System.out.println("----------------------------------------");
    }

    /*
     * Method ini digunakan untuk membuat ID
     * dari nama restoran, tanggal order, dan nomor telepon
     * 
     * @return String Order ID dengan format sesuai pada dokumen soal
     */
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) { // pass by value (parameter input)

        String replacedNamaRestoran = namaRestoran.toUpperCase().replaceAll("\\s", ""); // Biar uppercase + ga ada spasi ga jelas yang bisa salah ID
        String namaRestoranID = replacedNamaRestoran.substring(0, 4); // first 4 digit ID pakai nama restoran (4 digit awal)

        String tanggalOrderID = tanggalOrder.replaceAll("/", ""); // biar jadi format DDMMYYYY untuk next 8 digit ID

        int sumOfNoTelepon = 0;
        for (int i = 0; i < noTelepon.length(); i++){                       // menghitung penjumlahan dari setiap digit nomor telepon
            int digit = Character.getNumericValue(noTelepon.charAt(i));
            sumOfNoTelepon += digit;
        }

        String noTeleponID;
        int moduloSumOfNoTelepon = sumOfNoTelepon % 100;                // next 2 digit, hasil modulo sumNoTelepon dengan 100
        if (Integer.toString(moduloSumOfNoTelepon).length() < 2){
            noTeleponID = "0" + Integer.toString(moduloSumOfNoTelepon);     // jika hasil modulo hanya 1 digit, tambah 0 di depan
        } else {
            noTeleponID = Integer.toString(moduloSumOfNoTelepon); 
        }

        String tempID = namaRestoranID + tanggalOrderID + noTeleponID;  // ID sementara 
        String CODE39 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";         // menggunakan INDEX sebagai value dari sebuah karakter (urut dari 0)

        int checkSum1 = 0; // untuk menyimpan total nilai numerik karakter index Genap
        int checkSum2 = 0; // untuk menyimpan total nilai numerik karakter index Ganjil
        for (int i = 0; i < tempID.length(); i++){
            if (i % 2 == 0){
                checkSum1 += CODE39.indexOf(tempID.charAt(i));      // menghitung checkSum sesuai dengan ganjil/genap
            } else if (i % 2 == 1) {
                checkSum2 += CODE39.indexOf(tempID.charAt(i));
            }
        }

        String digitGenapID = "" + CODE39.charAt(checkSum1%36);     
        String digitGanjilID = "" + CODE39.charAt(checkSum2%36);    // meng-assign nilai untuk 2 digit terakhir sesuai dengan CODE39
        String digitID = digitGenapID + digitGanjilID;

        return namaRestoranID + tanggalOrderID + noTeleponID + digitID;  //return OrderID
        
    }

    /*
     * Method ini digunakan untuk membuat bill
     * dari order id dan lokasi
     * 
     * @return String Bill dengan format sesuai di bawah:
     *          Bill:
     *          Order ID: [Order ID]
     *          Tanggal Pemesanan: [Tanggal Pemesanan]
     *          Lokasi Pengiriman: [Kode Lokasi]
     *          Biaya Ongkos Kirim: [Total Ongkos Kirim]
     */
    public static String generateBill(String OrderID, String lokasi){
        String orderIDBill = OrderID; //OrderID
        String tanggalOrderBill = OrderID.substring(4,6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(8,12); //Ngambil bagian tanggalnya aja
        String lokasiBill = "";
        String hargaBill = "";

        if (lokasi.toUpperCase().equals("P")){
            lokasiBill = "P";
            hargaBill = "Rp 10.000";
        } else if (lokasi.toUpperCase().equals("U")){
            lokasiBill = "U";
            hargaBill = "Rp 20.000";
        } else if (lokasi.toUpperCase().equals("T")){    //Meng-assign nilai dari variabel lokasiBill dan hargaBill sesuai dengan input lokasi
            lokasiBill = "T";
            hargaBill = "Rp 35.000";
        } else if (lokasi.toUpperCase().equals("S")){
            lokasiBill = "S";
            hargaBill = "Rp 40.000";
        } else if (lokasi.toUpperCase().equals("B")){
            lokasiBill = "B";
            hargaBill = "Rp 60.000";
        }

        return "Bill:\n" +
                "Order ID: " + orderIDBill + "\n" +                     // return Bill
                "Tanggal Pemesanan: " + tanggalOrderBill + "\n" +
                "Lokasi Pengiriman: " + lokasiBill + "\n" +
                "Biaya Ongkos Kirim: " + hargaBill + "\n" +
                "";
    }

    //*** Method ini untuk menghitung total penjumlahan dari nilai numerik suatu karakter berdasarkan CODE39  ***//
    public static String checkSum(String firstPart){

        String CODE39 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";        // Patokan untuk nilai numerik dari sebuah karakter

        int checkSum1 = 0; //Genap
        int checkSum2 = 0; //Ganjil

        for (int i = 0; i < firstPart.length(); i++){
            if (i % 2 == 0){
                checkSum1 += CODE39.indexOf(firstPart.charAt(i));       //Menghitung checksum, yaitu penjumlahan nilai numerik tiap karakter ganjil dan genap
            } else if (i % 2 == 1) {
                checkSum2 += CODE39.indexOf(firstPart.charAt(i));
            }
        }

        String digitGenapID = "" + CODE39.charAt(checkSum1%36);         // digit genap (2 dari terakhir)
        String digitGanjilID = "" + CODE39.charAt(checkSum2%36);        // digit ganjil (digit terakhir)
        String digitID = digitGenapID + digitGanjilID;

        return digitID; // 2 digit terakhir orderID
    }

    //*** Method ini untuk memvalidasi Nama Restoran, apakah sesuai dengan aturan atau tidak ***/
    public static boolean validateNamaRestoran (String namaRestoran){
        boolean validNamaRestoran = true;
        String replacedNamaRestoran = namaRestoran.replaceAll("\\s", "");

        if (replacedNamaRestoran.length() < 4) {
            validNamaRestoran = false;              // Jika panjang nama restoran < 4
            return validNamaRestoran;
        }

        for (int i = 0; i < 4; i++) {
            char ch = replacedNamaRestoran.charAt(i);       // Jika 4 karakter pertama nama restoran bukan huruf atau angka
            if (!Character.isLetterOrDigit(ch)) { 
                validNamaRestoran = false;
                break;
            }
        }

        return validNamaRestoran;
    }

    //*** Method ini untuk memvalidasi Tanggal Order, apakah sesuai dengan aturan atau tidak ***/
    public static boolean validateTanggalOrder (String tanggalOrder){
        boolean validTanggalOrder = true;
        String replacedTanggalOrder = tanggalOrder.replaceAll("/", ""); // biar menjadi format DDMMYYYY

        for (int i = 0; i < replacedTanggalOrder.length(); i++) {
            if (!Character.isDigit(replacedTanggalOrder.charAt(i))) {    // Jika input tanggal adalah huruf
                validTanggalOrder = false;
                return validTanggalOrder;
            }
        }

        String[] dateParts = tanggalOrder.split("/"); // Jika tidak sesuai format DD/MM/YYYY
        if (dateParts.length != 3 || dateParts[0].length() != 2 || dateParts[1].length() != 2 || dateParts[2].length() != 4) {
            return validTanggalOrder = false;
        }
        
        return validTanggalOrder;
    }
    
    //*** Method ini untuk memvalidasi Nomor Telepon, apakah sesuai dengan aturan atau tidak ***/
    public static boolean validateNoTelepon (String noTelepon){
        boolean validNoTelepon = true;

        for (int i = 0; i < noTelepon.length(); i++) {          //Jika ada digit nomor telepon yang bukan angka
            if (!Character.isDigit(noTelepon.charAt(i))) {
                validNoTelepon = false;
                break;
            }
        }

        return validNoTelepon;
    }

    //*** Method ini untuk memvalidasi Order ID, apakah sesuai dengan aturan atau tidak ***/
    public static boolean validateOrderID (String orderID){
        boolean validOrderID = true;

        if (orderID.length() != 16) {
            System.out.println("Order ID minimal 16 karakter");         // jika order id kurang dari 16 karakter
            validOrderID = false;
            return validOrderID;
        }

        for (int i = 0; i < orderID.length(); i++) {
            char ch = orderID.charAt(i);                        // Jika ada karakter di dalam Order ID yang bukan angka atau huruf
            if (!Character.isLetterOrDigit(ch)) { 
                System.out.println("Silahkan masukkan Order ID yang valid!");
                validOrderID = false;
                return validOrderID;
            }
        }

        String firstPart = orderID.substring(0, 14);        // memisahkan orderID menjadi 2 bagian, yaitu bagian awal dan 2 digit terakhir
        String lastPart = orderID.substring(14);                    

        String lastDigitID = checkSum(firstPart);

        if (!lastPart.equals(lastDigitID)){                     // Jika 2 digit terakhir tidak sesuai dengan perhitungan checkSum orderiD
            System.out.println("Silahkan masukkan Order ID yang valid!");
            validOrderID = false;
        }

        return validOrderID;
    }

    //*** Method ini untuk memvalidasi Lokasi, apakah sesuai dengan aturan atau tidak ***/
    public static boolean validateLokasi (String lokasi){
        boolean validLokasi = false;
        String listLokasi = "PUTSB";

        if (lokasi.length() != 1){        // Jika panjang lokasi tidak sama dengan 1
            return validLokasi;
        }

        for (int i = 0; i < listLokasi.length(); i++){
            String currentString = Character.toString(listLokasi.charAt(i));
            if (currentString.equals(lokasi.toUpperCase())){            // Jika lokasi bukan P,U,T,S, ataupun B
                validLokasi = true;
                return validLokasi;
            }
        }

        return validLokasi;
    }

    public static void main(String[] args){
    boolean working = true;
    String namaRestoran = "";
    String tanggalOrder = "";
    String noTelepon = "";
    showBanner();
    while (working) {
        showMenu();
        System.out.print("Pilihan menu: ");
        int option = input.nextInt();
        input.nextLine();
        if (option == 1) {
            boolean working1 = true;
            while (working1){
                System.out.println();
                System.out.print("Nama Restoran: ");
                namaRestoran = input.nextLine();
                if (validateNamaRestoran(namaRestoran) == false){
                    System.out.println("Nama Restoran tidak valid!");
                    continue;
                }
                System.out.print("Tanggal Pemesanan: ");
                tanggalOrder = input.nextLine();
                if (validateTanggalOrder(tanggalOrder) == false){
                    System.out.println("Tanggal Pemesanan dalam format DD/MM/YYYY!");
                    continue;
                }
                System.out.print("No. Telpon: ");
                noTelepon = input.nextLine();
                if (validateNoTelepon(noTelepon) == false){
                    System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif.");
                    continue;
                }
                System.out.println("Order ID " + generateOrderID(namaRestoran, tanggalOrder, noTelepon) + " diterima!");
                System.out.println("----------------------------------------");
                working1 = false;
            }
        } else if (option == 2){
            boolean working2 = true;
            while (working2){
                System.out.println();
                System.out.print("Order ID: ");
                String inputOrderID = input.nextLine();
                if (validateOrderID(inputOrderID) == false){
                    continue;
                } 
                System.out.print("Lokasi Pengiriman: ");
                String lokasi = input.nextLine();
                if (validateLokasi(lokasi) == false){
                    System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!");
                    continue;
                }
                System.out.println(generateBill(inputOrderID, lokasi));
                System.out.println("----------------------------------------");
                working2 = false;
            }
        } else{
            System.out.println("Terima kasih telah menggunakan DepeFood!");
            working = false;
            }
        }
    }
}