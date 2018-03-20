import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

/**
 * @author Anthony Rojas
 * Assignment 6 - Server
 */
public class Assn6Server{
    public static void main(String[] argv){
        try{
            /*
            For an external IP, the program will create a Registry and the RMI will be hosted there.
            * */
            System.setProperty("java.rmi.server.hostname", argv[0]);
            if(argv[0].equalsIgnoreCase("127.0.0.1")){
                Method m = new Method();
                Naming.rebind("rmi://" + argv[0] + "/cecs327", m);
                System.out.println("Server is ready.");
            }else{
                Registry registry = LocateRegistry.getRegistry();
                Method m = new Method();
                registry.rebind("rmi://" + argv[0] + "/cecs327", m);
                System.out.println("Server is ready.");
            }
        }catch(Exception e){
            System.out.println("Server error: " + e.getMessage());
            System.exit(1);
        }
    }
}
