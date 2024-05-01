package assignments.assignment3.systemCLI;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import assignments.assignment1.OrderGenerator;
import assignments.assignment2.Menu;
import assignments.assignment2.Order;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;
import assignments.assignment3.User;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;

//TODO: Extends abstract class yang diberikan
public class CustomerSystemCLI extends UserSystemCLI{
    private static final Scanner input = new Scanner(System.in);

    public CustomerSystemCLI(Scanner input) {
        super(input);
        //TODO Auto-generated constructor stub
    }

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    public boolean handleMenu(int choice){
        switch(choice){
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    //Method untuk membuat pesanan
    public static void handleBuatPesanan(){
        User userLoggedIn = MainMenu.getUserLoggedIn();
        ArrayList<Order> orderHistory = userLoggedIn.getOrderHistory();
        System.out.println("\n--------------Buat Pesanan----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String restaurantName = input.nextLine().trim();
            Restaurant restaurant = getRestaurantByName(restaurantName);
            if(restaurant == null){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            String tanggalPemesanan = input.nextLine().trim();
            if(!OrderGenerator.validateDate(tanggalPemesanan)){
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY)!");
                System.out.println();
                continue;
            }
            System.out.print("Jumlah Pesanan: ");
            int jumlahPesanan = Integer.parseInt(input.nextLine().trim());
            System.out.println("Order: ");
            List<String> listMenuPesananRequest = new ArrayList<>();
            for(int i=0; i < jumlahPesanan; i++){
                listMenuPesananRequest.add(input.nextLine().trim());
            }
            if(! validateRequestPesanan(restaurant, listMenuPesananRequest)){
                System.out.println("Mohon memesan menu yang tersedia di Restoran!");
                continue;
            };
            Order order = new Order(
                    OrderGenerator.generateOrderID(restaurantName, tanggalPemesanan, userLoggedIn.getNomorTelepon()),
                    tanggalPemesanan, 
                    OrderGenerator.calculateDeliveryCost(userLoggedIn.getLokasi()), 
                    restaurant, 
                    getMenuRequest(restaurant, listMenuPesananRequest));
            System.out.printf("Pesanan dengan ID %s diterima!", order.getOrderId());
            orderHistory.add(order);
            userLoggedIn.setOrderHistory(orderHistory);
            return;
        }
    }

    //Method untuk melakukan validasi terhadap pesanan yang dibuat
    public static boolean validateRequestPesanan(Restaurant restaurant, List<String> listMenuPesananRequest){
        return listMenuPesananRequest.stream().allMatch(pesanan -> 
            restaurant.getMenu().stream().anyMatch(menu -> menu.getNamaMakanan().equals(pesanan))
        );
    }

    //Method untuk mendapatkan Array berisi Menu yang di-request
    public static Menu[] getMenuRequest(Restaurant restaurant, List<String> listMenuPesananRequest){
        Menu[] menu = new Menu[listMenuPesananRequest.size()];
        for(int i=0;i<menu.length;i++){
            for(Menu existMenu : restaurant.getMenu()){
                if(existMenu.getNamaMakanan().equals(listMenuPesananRequest.get(i))){
                    menu[i] = existMenu;
                }
            }
        }
        return menu;
    }

    //Method untuk mendapatkan Menu Pesanan
    public static String getMenuPesananOutput(Order order){
        StringBuilder pesananBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('\u0000');
        decimalFormat.setDecimalFormatSymbols(symbols);
        for (Menu menu : order.getSortedMenu()) {
            pesananBuilder.append("- ").append(menu.getNamaMakanan()).append(" ").append(decimalFormat.format(menu.getHarga())).append("\n");
        }
        if (pesananBuilder.length() > 0) {
            pesananBuilder.deleteCharAt(pesananBuilder.length() - 1);
        }
        return pesananBuilder.toString();
    }

    //Method untuk menampilkan Bill Pesanan
    public static String outputBillPesanan(Order order) {
        User userLoggedIn = MainMenu.getUserLoggedIn();
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return String.format("Bill:%n" +
                         "Order ID: %s%n" +
                         "Tanggal Pemesanan: %s%n" + 
                         "Restaurant: %s%n" +
                         "Lokasi Pengiriman: %s%n" +
                         "Status Pengiriman: %s%n"+
                         "Pesanan:%n%s%n"+
                         "Biaya Ongkos Kirim: Rp %s%n"+
                         "Total Biaya: Rp %s%n",
                         order.getOrderId(),
                         order.getTanggal(),
                         order.getRestaurant().getNama(),
                         userLoggedIn.getLokasi(),
                         !order.getOrderFinished()? "Not Finished":"Finished",
                         getMenuPesananOutput(order),
                         (long) order.getOngkir(),
                         (long) order.getTotalHarga()
                         );
    }

    //Method untuk Mendapatkan Restoran dari Nama
    public static Restaurant getRestaurantByName(String name){
        ArrayList<Restaurant> restoList = MainMenu.getRestoList();
        Optional<Restaurant> restaurantMatched = restoList.stream().filter(restoran -> restoran.getNama().toLowerCase().equals(name.toLowerCase())).findFirst();
        if(restaurantMatched.isPresent()){
            return restaurantMatched.get();
        }
        return null;
    }

    //Method Untuk CetakBill
    public static void handleCetakBill(){
        System.out.println("\n--------------Cetak Bill----------------");
        while (true) {
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine().trim();
            Order order = getOrderOrNull(orderId);
            if(order == null){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
            System.out.println("");
            System.out.print(outputBillPesanan(order));
            return;
        }
    }

    //Method untuk ngecek apakah terdapat order atau tidak
    public static Order getOrderOrNull(String orderId) {
        ArrayList<User> userList = MainMenu.getUserList();
        for (User user : userList) {
            for (Order order : user.getOrderHistory()) {
                if (order.getOrderId().equals(orderId)) {
                    return order;
                }
            }
        }
        return null;
    }

    //Method untuk Lihat Menu
    public static void handleLihatMenu(){
        System.out.println("\n--------------Lihat Menu----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            String restaurantName = input.nextLine().trim();
            Restaurant restaurant = getRestaurantByName(restaurantName);
            if(restaurant == null){
                System.out.println("Restoran tidak terdaftar pada sistem.\n");
                continue;
            }
            System.out.print(restaurant.printMenu());
            return;
        }
    }

    //Method unutk mengupdate statuspesanan order
    public static void handleUpdateStatusPesanan(){
        System.out.println("\n--------------Update Status Pesanan---------------");
        while (true) {
            System.out.print("Order ID: ");
            String orderId = input.nextLine().trim();
            Order order = getOrderOrNull(orderId);
            if(order == null){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
            System.out.print("Status: ");
            String newStatus = input.nextLine().trim();
            if(newStatus.toLowerCase().equals("SELESAI".toLowerCase())){
                if(order.getOrderFinished() == true){
                    System.out.printf("Status pesanan dengan ID %s tidak berhasil diupdate!", order.getOrderId());
                }
                else{
                    System.out.printf("Status pesanan dengan ID %s berhasil diupdate!", order.getOrderId());
                    order.setOrderFinished(true);

                }
            }
            return;
        }
    }

    //Method untuk mengecek apakah sebuah String digit semua atau tidak
    public static boolean checkIsDigit(String digits){
        return digits.chars().allMatch(Character::isDigit);
    }
    
    //Method untuk mendapatkan restaurant name yang valid
    public static String getValidRestaurantName() {
        ArrayList<Restaurant> restoList = MainMenu.getRestoList();
        String name = "";
        boolean isRestaurantNameValid = false;
    
        while (!isRestaurantNameValid) {
            System.out.print("Nama: ");
            String inputName = input.nextLine().trim();
            boolean isRestaurantExist = restoList.stream()
                    .anyMatch(restoran -> restoran.getNama().toLowerCase().equals(inputName.toLowerCase()));
            boolean isRestaurantNameLengthValid = inputName.length() >= 4;
    
            if (isRestaurantExist) {
                System.out.printf("Restoran dengan nama %s sudah pernah terdaftar. Mohon masukkan nama yang berbeda!%n", inputName);
                System.out.println();
            } else if (!isRestaurantNameLengthValid) {
                System.out.println("Nama Restoran tidak valid!");
                System.out.println();
            } else {
                name = inputName;
                isRestaurantNameValid = true;
            }
        }
        return name;
    }

    //Method untuk melakukan bayar bill
    public static void handleBayarBill(){
        User userLoggedIn = MainMenu.getUserLoggedIn();
        System.out.println("\n--------------Bayar Bill---------------");
        boolean isBillValid = false;

        while (!isBillValid){
            System.out.print("Masukkan Order ID: ");
            String orderId = input.nextLine().trim();
            Order order = getOrderOrNull(orderId);
            if(order == null){
                System.out.println("Order ID tidak dapat ditemukan.\n");
                continue;
            }
            if(order.getOrderFinished()){
                System.out.println("Pesanan dengan ID ini sudah lunas!\n");
                continue;
            }
            System.out.println();
            System.out.println(outputBillPesanan(order));
            System.out.println("Opsi Pembayaran: ");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit");
            System.out.print("Pilihan Metode Pembayaran: ");
            int choiceCard = input.nextInt();
            input.nextLine();
            Restaurant resto = order.getRestaurant();
            long tempRestoSaldo = resto.getSaldo();
            long tempUserSaldo = userLoggedIn.getSaldo();
            if (choiceCard == 1){
                CreditCardPayment payment = new CreditCardPayment();
                System.out.println("Berhasil membayar Bill sebesar Rp " + (long) order.getTotalHarga() + " dengan biaya transaksi sebesar Rp " + (long) payment.countTransactionFee((long) order.getTotalHarga()));
                order.setOrderFinished(true);
                userLoggedIn.setSaldo(tempUserSaldo - payment.processPayment((long) order.getTotalHarga()));
                resto.setSaldo(tempRestoSaldo + (long) order.getTotalHarga());
                isBillValid = true;
            } else if (choiceCard == 2){
                DebitPayment payment = new DebitPayment(userLoggedIn.getSaldo());
                long saldo = payment.processPayment((long) order.getTotalHarga());
                if (saldo == 0){
                    continue;
                }
                System.out.println("Berhasil membayar Bill sebesar Rp " + order.getTotalHarga());
                order.setOrderFinished(true);
                userLoggedIn.setSaldo(saldo);
                resto.setSaldo(tempRestoSaldo + (long) order.getTotalHarga());
                isBillValid = true;
            } else{
                System.out.println("Harap masukkan metode pembayaran yang sesuai!\n");
                continue;
            }
        }
    }

    //Method untuk mengecek saldo
    public static void handleCekSaldo(){
        System.out.println("\n--------------Cek Saldo---------------");
        User userLoggedIn = MainMenu.getUserLoggedIn();
        System.out.println("\nSisa saldo sebesar Rp " + userLoggedIn.getSaldo());
    }
}
