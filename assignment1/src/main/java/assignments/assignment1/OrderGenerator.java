package assignments.assignment1;

import java.util.Scanner;

public class OrderGenerator {
    private static final Scanner input = new Scanner(System.in);

    /* 
    Anda boleh membuat method baru sesuai kebutuhan Anda
    Namun, Anda tidak boleh menghapus ataupun memodifikasi return type method yang sudah ada.
    */

    /*
     * Method  ini untuk menampilkan menu
     */
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
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {

        String replacedNamaRestoran = namaRestoran.toUpperCase().replaceAll("\\s", "");
        String namaRestoranID = replacedNamaRestoran.substring(0, 4);

        String tanggalOrderID = tanggalOrder.replaceAll("/", "");

        int sumOfNoTelepon = 0;
        for (int i = 0; i < noTelepon.length(); i++){
            int digit = Character.getNumericValue(noTelepon.charAt(i));
            sumOfNoTelepon += digit;
        }

        String noTeleponID;
        int moduloSumOfNoTelepon = sumOfNoTelepon % 100;
        if (Integer.toString(moduloSumOfNoTelepon).length() < 2){
            noTeleponID = "0" + Integer.toString(moduloSumOfNoTelepon);
        } else {
            noTeleponID = Integer.toString(moduloSumOfNoTelepon);
        }

        String tempID = namaRestoranID + tanggalOrderID + noTeleponID; //HOLY1802202453
        String CODE39 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 

        int checkSum1 = 0; //Genap
        int checkSum2 = 0; //Ganjil
        for (int i = 0; i < tempID.length(); i++){
            if (i % 2 == 0){
                checkSum1 += CODE39.indexOf(tempID.charAt(i));
            } else if (i % 2 == 1) {
                checkSum2 += CODE39.indexOf(tempID.charAt(i));
            }
        }

        String digitGenapID = "" + CODE39.charAt(checkSum1%36);
        String digitGanjilID = "" + CODE39.charAt(checkSum2%36);
        String digitID = digitGenapID + digitGanjilID;

        return namaRestoranID + tanggalOrderID + noTeleponID + digitID;
        
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
        String orderIDBill = OrderID;
        String tanggalOrderBill = OrderID.substring(4,6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(8,12);
        String lokasiBill = "";
        String hargaBill = "";

        if (lokasi.toUpperCase().equals("P")){
            lokasiBill = "P";
            hargaBill = "Rp 10.000";
        } else if (lokasi.toUpperCase().equals("U")){
            lokasiBill = "U";
            hargaBill = "Rp 20.000";
        } else if (lokasi.toUpperCase().equals("T")){
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
                "Order ID: " + orderIDBill + "\n" +
                "Tanggal Pemesanan: " + tanggalOrderBill + "\n" +
                "Lokasi Pengiriman: " + lokasiBill + "\n" +
                "Biaya Ongkos Kirim: " + hargaBill + "\n" +
                "";
    }

    public static String checkSum(String firstPart){

        String CODE39 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 

        int checkSum1 = 0; //Genap
        int checkSum2 = 0; //Ganjil

        for (int i = 0; i < firstPart.length(); i++){
            if (i % 2 == 0){
                checkSum1 += CODE39.indexOf(firstPart.charAt(i));
            } else if (i % 2 == 1) {
                checkSum2 += CODE39.indexOf(firstPart.charAt(i));
            }
        }

        String digitGenapID = "" + CODE39.charAt(checkSum1%36);
        String digitGanjilID = "" + CODE39.charAt(checkSum2%36);
        String digitID = digitGenapID + digitGanjilID;

        return digitID;
    }

    public static boolean validateNamaRestoran (String namaRestoran){
        boolean validNamaRestoran = true;
        String replacedNamaRestoran = namaRestoran.replaceAll("\\s", "");

        if (replacedNamaRestoran.length() < 4) {
            validNamaRestoran = false;
            return validNamaRestoran;
        }

        for (int i = 0; i < 4; i++) {
            char ch = replacedNamaRestoran.charAt(i); //bisa disederhanakan
            if (!Character.isLetterOrDigit(ch)) { 
                validNamaRestoran = false;
                break;
            }
        }

        return validNamaRestoran;
    }

    public static boolean validateTanggalOrder (String tanggalOrder){
        boolean validTanggalOrder = true; // (18/02/2024)
        String replacedTanggalOrder = tanggalOrder.replaceAll("/", ""); //18022024

        for (int i = 0; i < replacedTanggalOrder.length(); i++) {
            if (!Character.isDigit(replacedTanggalOrder.charAt(i))) {
                validTanggalOrder = false;
                return validTanggalOrder;
            }
        }

        String[] dateParts = tanggalOrder.split("/");
        if (dateParts.length != 3 || dateParts[0].length() != 2 || dateParts[1].length() != 2 || dateParts[2].length() != 4) {
            return validTanggalOrder = false;
        }
        
        return validTanggalOrder;
    }
    
    public static boolean validateNoTelepon (String noTelepon){
        boolean validNoTelepon = true;

        for (int i = 0; i < noTelepon.length(); i++) {
            if (!Character.isDigit(noTelepon.charAt(i))) {
                validNoTelepon = false;
                break;
            }
        }

        return validNoTelepon;
    }

    public static boolean validateOrderID (String orderID){
        boolean validOrderID = true;

        if (orderID.length() != 16) {
            System.out.println("Order ID minimal 16 karakter");
            validOrderID = false;
            return validOrderID;
        }

        for (int i = 0; i < orderID.length(); i++) {
            char ch = orderID.charAt(i); //bisa disederhanakan
            if (!Character.isLetterOrDigit(ch)) { 
                System.out.println("Silahkan masukkan Order ID yang valid!");
                validOrderID = false;
                return validOrderID;
            }
        }

        String firstPart = orderID.substring(0, 14);
        String lastPart = orderID.substring(14);

        String lastDigitID = checkSum(firstPart);

        if (!lastPart.equals(lastDigitID)){
            System.out.println("Silahkan masukkan Order ID yang valid!");
            validOrderID = false;
        }

        return validOrderID;
    }

    public static boolean validateLokasi (String lokasi){
        boolean validLokasi = false;
        String listLokasi = "PUTSB";

        if (lokasi.length() != 1){
            return validLokasi;
        }

        for (int i = 0; i < listLokasi.length(); i++){
            String currentString = Character.toString(listLokasi.charAt(i));
            if (currentString.equals(lokasi.toUpperCase())){
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