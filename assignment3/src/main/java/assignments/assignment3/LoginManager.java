package assignments.assignment3;

import assignments.assignment3.systemCLI.AdminSystemCLI;
import assignments.assignment3.systemCLI.CustomerSystemCLI;
import assignments.assignment3.systemCLI.UserSystemCLI;

import java.util.Scanner;

public class LoginManager {
    private final AdminSystemCLI adminSystem;
    private final CustomerSystemCLI customerSystem;

    public LoginManager(Scanner input) {
        this.adminSystem = new AdminSystemCLI(input);
        this.customerSystem = new CustomerSystemCLI(input);
    }

    //TODO: Solve the error :) (It's actually easy if you have done the other TODOs)
    public UserSystemCLI getSystem(String role){
        if(role.equals("Customer")){
            return customerSystem;
        }else{
            return adminSystem;
        }
    }
}
