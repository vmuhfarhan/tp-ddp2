package assignments.assignment2;

import java.util.ArrayList;

public class Restaurant {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
     private String namaResto;
     private ArrayList<Menu> menuList;

    public Restaurant(String nama){
        this.namaResto = nama;
        this.menuList = new ArrayList<Menu>();
    }
    
    // TODO: tambahkan methods yang diperlukan untuk class ini
    public String getNamaResto(){
        return this.namaResto;
    }

    public ArrayList<Menu> getMenuList() {
        return this.menuList;
    }

    public void setNamaResto(String namaResto) {
        this.namaResto = namaResto;
    }

    public void setMenuList(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }

}
