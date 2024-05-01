package assignments.assignment3.systemCLI;

import java.util.Scanner;

public abstract class UserSystemCLI {
    protected Scanner input;

    public UserSystemCLI(Scanner input){
        this.input = input;
    }
    
    public void run() {
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            displayMenu();
            int command = input.nextInt();
            input.nextLine();
            isLoggedIn = handleMenu(command);
        }
    }
    abstract void displayMenu();
    abstract boolean handleMenu(int command);
}
