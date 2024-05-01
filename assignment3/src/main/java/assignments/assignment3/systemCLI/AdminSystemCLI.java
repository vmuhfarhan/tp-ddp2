package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import assignments.assignment2.Menu;
import assignments.assignment2.Restaurant;
import assignments.assignment3.MainMenu;

//TODO: Extends Abstract yang diberikan
public class AdminSystemCLI extends UserSystemCLI{
    ArrayList<Restaurant> restoList = MainMenu.getRestoList();
    private static final Scanner input = new Scanner(System.in);

    public AdminSystemCLI(Scanner input) {
        super(input);
        //TODO Auto-generated constructor stu
    }

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    public boolean handleMenu(int command){
        switch(command){
            case 1 -> handleTambahRestoran();
            case 2 -> handleHapusRestoran();
            case 3 -> {return false;}
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    //TODO: Tambahkan modifier dan buatlah metode ini mengoverride dari Abstract class
    public void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void handleTambahRestoran(){
        ArrayList<Restaurant> restoList = MainMenu.getRestoList();
        System.out.println("--------------Tambah Restoran---------------");
        Restaurant restaurant = null;
        while (restaurant == null) {
            String namaRestaurant = getValidRestaurantName();
            restaurant = new Restaurant(namaRestaurant);
            restaurant = handleTambahMenuRestaurant(restaurant);
        }
        restoList.add(restaurant);
        MainMenu.setRestoList(restoList);
        System.out.print("Restaurant " + restaurant.getNama() + " Berhasil terdaftar." );
    }

    public static Restaurant handleTambahMenuRestaurant(Restaurant restoran){
        ArrayList<Menu> menuList = restoran.getMenu();
        System.out.print("Jumlah Makanan: ");
        int jumlahMenu = Integer.parseInt(input.nextLine().trim());
        boolean isMenuValid = true;
        for(int i = 0; i < jumlahMenu; i++){
            String inputValue = input.nextLine().trim();
            String[] splitter = inputValue.split(" ");
            String hargaStr = splitter[splitter.length-1];
            boolean isDigit = checkIsDigit(hargaStr);
            String namaMenu = String.join(" ", Arrays.copyOfRange(splitter, 0, splitter.length - 1));
            if(isDigit){
                int hargaMenu = Integer.parseInt(hargaStr);
                menuList.add(new Menu(namaMenu, hargaMenu));
            }
            else{
                isMenuValid = false;
            }
        }
        if(!isMenuValid){
            System.out.println("Harga menu harus bilangan bulat!");
            System.out.println();
        }else{
            restoran.setMenu(menuList);
        }

        return isMenuValid? restoran : null; 
    }

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

    public static boolean checkIsDigit(String digits){
        return  digits.chars().allMatch(Character::isDigit);
    }

    public static void handleHapusRestoran(){
        ArrayList<Restaurant> restoList = MainMenu.getRestoList();
        System.out.println("--------------Hapus Restoran----------------");
        boolean isActionDeleteEnded = false;
        while (!isActionDeleteEnded) {
            System.out.print("Nama Restoran: ");
            String restaurantName = input.nextLine().trim();
            boolean isRestaurantExist = restoList.stream().anyMatch(restaurant -> restaurant.getNama().toLowerCase().equals(restaurantName.toLowerCase()));
            if(!isRestaurantExist){
                System.out.println("Restoran tidak terdaftar pada sistem.");
                System.out.println();
            }
            else{
                restoList = new ArrayList<>(restoList.stream().filter(restaurant-> !restaurant.getNama().toLowerCase().equals(restaurantName.toLowerCase())).toList());
                System.out.println("Restoran berhasil dihapus");
                MainMenu.setRestoList(restoList);
                isActionDeleteEnded = true;
            }
        }
    }

}
