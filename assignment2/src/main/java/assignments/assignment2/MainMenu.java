package assignments.assignment2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList = new ArrayList<Restaurant>();
    private static ArrayList<User> userList = new ArrayList<User>();
    private static User userLoggedIn;
    
    public static void main(String[] args) {
        boolean programRunning = true;
        while(programRunning){
            printHeader();
            startMenu();
            int command = input.nextInt();
            input.nextLine();

            if(command == 1){
                boolean validLogin = false;
                initUser();
                userLoggedIn = null; 
                while (!validLogin){ 
                    System.out.println("\nSilakan Login:");
                    System.out.print("Nama: ");
                    String nama = input.nextLine();
                    System.out.print("Nomor Telepon: ");
                    String noTelp = input.nextLine();
            
                    // TODO: Validasi input login
                    userLoggedIn = getUser(nama, noTelp);
            
                    if (userLoggedIn != null){
                        System.out.println("Selamat datang " + nama + "!");
                        validLogin = true;
                        break;
                    } else{
                        System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                    }
                }
                
                boolean isLoggedIn = true;
                if(userLoggedIn != null && userLoggedIn.getRole().equals("Customer")){ // Ensure userLoggedIn is not null before accessing its properties
                    while (isLoggedIn){
                        menuCustomer();
                        int commandCust = input.nextInt();
                        input.nextLine();
            
                        switch(commandCust){
                            case 1 -> handleBuatPesanan();
                            case 2 -> handleCetakBill();
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan();
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                } else if (userLoggedIn != null && userLoggedIn.getRole().equals("Admin")) {
                    while (isLoggedIn){
                        menuAdmin();
                        int commandAdmin = input.nextInt();
                        input.nextLine();
            
                        switch(commandAdmin){
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            } else if(command == 2){
                programRunning = false;
            } else {
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }            
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
    }

    public static User getUser(String nama, String nomorTelepon){
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    public static Restaurant getResto(String param){
        for (Restaurant resto : restoList) {
            if (resto.getNamaResto().equals(param)) {
                return resto;
            }
        }
        return null;
    }

    public static boolean validateTanggalOrder(String tanggalOrder){
        String replacedTanggalOrder = tanggalOrder.replaceAll("/", ""); // biar menjadi format DDMMYYYY

        for (int i = 0; i < replacedTanggalOrder.length(); i++) {
            if (!Character.isDigit(replacedTanggalOrder.charAt(i))) {    // Jika input tanggal adalah huruf
                return false;
            }
        }

        String[] dateParts = tanggalOrder.split("/"); // Jika tidak sesuai format DD/MM/YYYY
        if (dateParts.length != 3 || dateParts[0].length() != 2 || dateParts[1].length() != 2 || dateParts[2].length() != 4) {
            return false;
        }

        return true;
    }

    public static void handleBuatPesanan(){
        // TODO: Implementasi method untuk handle ketika customer membuat pesanan
        System.out.println("\n---------------Buat Pesanan---------------");
        ArrayList<Menu> TempOrderMenu = new ArrayList<>();
        String inputNamaResto;
        String tanggalOrder;
        int jumlahPesanan;
        String inputOrder;
        Restaurant resto = null;
        ArrayList<Menu> menuListResto;
        ArrayList<Order> TempOrderHistory = userLoggedIn.getOrderHistory(); //biar tidak reset

        boolean isBuatPesanan = true;
        while (isBuatPesanan){
            System.out.print("Nama Restoran: ");
            inputNamaResto = input.nextLine();

            boolean isRestoAvailable = false;
            for (Restaurant restaurant : restoList){
                if (restaurant.getNamaResto().equals(inputNamaResto)){
                    isRestoAvailable = true;
                    resto = restaurant;
                    break;
                }
            }

            if (isRestoAvailable == false){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalOrder = input.nextLine();
            if (!validateTanggalOrder(tanggalOrder)){
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!\n");
                continue;
            }

            System.out.print("Jumlah Pesanan: ");
            jumlahPesanan = input.nextInt();
            input.nextLine();

            System.out.println("Order:");
            boolean isInputMenuValid = true;
            for (int i = 0; i < jumlahPesanan; i++){
                inputOrder = input.nextLine();
                menuListResto = resto.getMenuList();
                //boolean isOrderValid = false;
                for (Menu menu : menuListResto){
                    if (menu.getNamaMakanan().equals(inputOrder)){
                        TempOrderMenu.add(menu);
                    }
                }
                if (TempOrderMenu.size() == 0){
                    //System.out.println("Mohon memesan menu yang tersedia di Restoran!");
                    isInputMenuValid = false;
                    break;
                }
            }

            if (!isInputMenuValid){
                System.out.println("Mohon memesan menu yang tersedia di Restoran!\n");
                continue;
            }
            String orderID = generateOrderID(inputNamaResto, tanggalOrder, userLoggedIn.getNomorTelepon());
            Order userOrder = new Order(orderID, tanggalOrder, userLoggedIn.getHargaOngkir(), resto, TempOrderMenu);
            TempOrderHistory.add(userOrder);
            userLoggedIn.setOrderList(TempOrderHistory);

            System.out.println("Pesanan dengan ID " + orderID + " diterima!");
            isBuatPesanan = false;
        }
    }

    public static void handleCetakBill(){
        System.out.println("\n---------------Cetak Bill---------------");
        String orderIDBill = "";
        String tanggalOrderBill = "";
        String namaRestoran = "";
        String statusKiriman = "";
        double totalBiaya = 0;
        String pesananString = "";
        ArrayList<Menu> listMakananDiorder;
        ArrayList<Order> orderHistoryUser = userLoggedIn.getOrderHistory();

        boolean isCetakBill = true;
        while (isCetakBill){
            System.out.print("Masukkan Order ID: ");
            orderIDBill = input.nextLine();
            for (Order order : orderHistoryUser){
                if (order.getOrderID().equals(orderIDBill)){
                    Restaurant resto = order.getRestaurant();
                    tanggalOrderBill = orderIDBill.substring(4,6) + "/" + orderIDBill.substring(6, 8) + "/" + orderIDBill.substring(8,12); //Ngambil bagian tanggalnya aja
                    namaRestoran = resto.getNamaResto();
                    boolean status = order.getOrderFinished();
                    if (status == true){
                        statusKiriman = "Finished";
                    }else{
                        statusKiriman = "Not Finished";
                    }
                    listMakananDiorder = order.getItems();
                    for (Menu makanan : listMakananDiorder){
                        totalBiaya += makanan.getHarga();
                        pesananString += "- " + makanan.getNamaMakanan() + " " + String.format("%.0f", makanan.getHarga()) + "\n";
                    }
                    isCetakBill = false;
                    break;
                }
            }
            if (isCetakBill == true){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
        }

        System.out.println("\nBill:\n" +
                "Order ID: " + orderIDBill + "\n" +                    
                "Tanggal Pemesanan: " + tanggalOrderBill + "\n" +
                "Restaurant: " + namaRestoran + "\n" +
                "Lokasi Pengiriman: " + userLoggedIn.getLokasi() + "\n" +
                "Status Pengiriman: " + statusKiriman + "\n" +
                "Pesanan: \n" + pesananString +
                "Biaya Ongkos Kirim: Rp " + String.format("%.0f", userLoggedIn.getHargaOngkir()) + "\n" +
                "Total Biaya: Rp " + String.format("%.0f", totalBiaya + userLoggedIn.getHargaOngkir()));
    }

    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {

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

    public static void handleLihatMenu(){
        // TODO: Implementasi method untuk handle ketika customer ingin melihat menu
        String namaRestoran;
        ArrayList<Menu> menuRestoran = null;
        System.out.println("\n---------------Lihat Menu---------------");
        boolean isLihatMenu = true;
        while(isLihatMenu){
            System.out.print("Nama Restoran: ");
            namaRestoran = input.nextLine();
            for (Restaurant resto : restoList){
                if (resto.getNamaResto().equals(namaRestoran)){
                    menuRestoran = resto.getMenuList();
                    isLihatMenu = false;
                    break;
                }
            }
            if (isLihatMenu){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }

            ///
            Collections.sort(menuRestoran, new Comparator<Menu>() {
                @Override
                public int compare(Menu menu1, Menu menu2) {
                    int compareByPrice = Double.compare(menu1.getHarga(), menu2.getHarga());
                    if (compareByPrice != 0) {
                        return compareByPrice;
                    }
                    return menu1.getNamaMakanan().compareTo(menu2.getNamaMakanan());
                }
            });

            System.out.println("Menu:");
            int index = 1;
            for (Menu menu : menuRestoran) {
                System.out.println(index + ". " + menu.getNamaMakanan() + " " + String.format("%.0f", menu.getHarga()));
                index++;
            }
            ///
        }
    }

    public static void handleUpdateStatusPesanan(){
        // TODO: Implementasi method untuk handle ketika customer ingin update status pesanan
        System.out.println("\n---------------Update Status Pesanan---------------");
        ArrayList<Order> listOrder = userLoggedIn.getOrderHistory();
        String orderID;
        String status;
        boolean isUpdateStatusPesanan = true;
        while (isUpdateStatusPesanan){
            boolean isOrderIDValid = false;
            System.out.print("Order ID: ");
            orderID = input.nextLine();
            for (Order order : listOrder){
                if (order.getOrderID().equals(orderID)){
                    System.out.print("Status: ");
                    status = input.nextLine().toLowerCase();
                    if (status.equals("selesai") && order.getOrderFinished() == false){
                        order.setOrderFinished(true);
                        System.out.println("Status pesanan dengan ID " + orderID + " berhasil diupdate!");
                        isOrderIDValid = true;
                        isUpdateStatusPesanan = false;
                        break;
                    } else{
                        System.out.println("Status pesanan dengan ID " + orderID + " tidak berhasil diupdate!\n");
                        isOrderIDValid = true;
                        break;
                    }
                }
            }
            if (isOrderIDValid == false){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
        }
    }

    public static void handleTambahRestoran(){
        // TODO: Implementasi method untuk handle ketika admin ingin tambah restoran
        System.out.println("\n---------------Tambah Restoran---------------");
        String namaRestoran;
        int jumlahMakanan;
        String inputMenu;
        Restaurant resto;
        ArrayList<Menu> TempDaftarMenu = new ArrayList<>(); //Menyimpan menu sementara

        boolean isTambahRestoran = true;
        while(isTambahRestoran){
            System.out.print("Nama: ");
            namaRestoran = input.nextLine();

            if (restoList.size() == 0){   //Jika restoran kosong
                isTambahRestoran = false;
            }

            if (namaRestoran.length() < 4){
                System.out.println("Nama Restoran tidak valid!\n");
                continue;
            }
            
            boolean isRestoDouble = false;
            for (Restaurant restaurant : restoList){
                if (restaurant.getNamaResto().equals(namaRestoran)){
                    System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda!\n");
                    isRestoDouble = true;
                    break;
                }
            }

            if (isRestoDouble == true){
                continue;
            }

            System.out.print("Jumlah Makanan: ");
            jumlahMakanan = input.nextInt();
            input.nextLine();
            
            resto = new Restaurant(namaRestoran); //bikin object baru dari class Restaurant

            boolean isLetterinInput = false;
            for (int i = 0; i < jumlahMakanan; i++){
                inputMenu = input.nextLine();
                boolean isValidInput = true;

                StringBuilder namaMakananBuilder = new StringBuilder();
                String[] inputMenuSplit = inputMenu.split(" ");  //Jadi NamaMakanan
                for (int j = 0; j < inputMenuSplit.length - 1; j++){
                    namaMakananBuilder.append(inputMenuSplit[j]);
                    if (j < inputMenuSplit.length - 2){
                        namaMakananBuilder.append(" ");
                    }
                }

                String namaMakanan = namaMakananBuilder.toString(); //Jadi namaMakanan
                String hargaMakananString = inputMenuSplit[inputMenuSplit.length -1];   //Jadi hargaMakanan
                for (char c : hargaMakananString.toCharArray()){
                    if (Character.isLetter(c)) {
                        isLetterinInput = true;
                        isValidInput = false; //ngecek apakah ada letter di dalam hargaMakanan
                        break;
                    }
                }

                if (isLetterinInput == true){
                    continue;
                }

                if (isLetterinInput == false && isValidInput == true){
                    double hargaMakanan = Double.parseDouble(hargaMakananString);
                    Menu TempMenu = new Menu (namaMakanan, hargaMakanan);
                    TempDaftarMenu.add(TempMenu);
                }
            }

            if (isLetterinInput == true){
                System.out.println("Harga menu harus bilangan bulat!\n");
                continue;
            }

            resto.setMenuList(TempDaftarMenu);
            restoList.add(resto);
            System.out.println("Restaurant " + namaRestoran + " Berhasil terdaftar.");
            isTambahRestoran = false;
            }
        }
            
    public static void handleHapusRestoran(){
        // TODO: Implementasi method untuk handle ketika admin ingin tambah restoran
        System.out.println("\n---------------Hapus Restoran---------------");
        boolean isHapusResto = true;
        String inputHapusResto;
        while (isHapusResto){
            System.out.print("Nama Restoran: ");
            inputHapusResto = input.nextLine();

            if (restoList == null){
                System.out.println("Belum ada restoran yang terdaftar dalam sistem.\n");
                continue;
            }

            for (Restaurant resto : restoList) {
                if (resto.getNamaResto().toLowerCase().equals(inputHapusResto.toLowerCase())) {
                    restoList.remove(resto);
                    System.out.println("Restoran berhasil dihapus.");
                    isHapusResto = false;
                    break;
                }
            }

            if (isHapusResto == true){
            System.out.println("Restoran tidak terdaftar pada sistem.\n");
            continue;
            }
        }
    }

    public static void initUser(){
       userList = new ArrayList<User>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer", 10000));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer", 60000));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer", 35000));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer", 40000));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer", 20000));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin", 0));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin", 0));
    }

    public static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }
    
}