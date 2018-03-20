import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Anthony Rojas
 * Assignment 6 - Client
 */
public class Assn6Client {
    public static void main(String [] args){
        if(args.length < 3){
            System.out.println("Insufficient arguments. Make sure to run the program in the following way: java <RMI Location> <Method> <Number>");
        }else{
            String rmiLocation = args[0];
            String method = args[1];
            long number = 0;
            try{
                number = Long.parseLong(args[2]);
            }catch(Exception numE){
                System.out.println("Client error: invalid number");
                System.exit(1);
            }
            if(number <= 0){
                System.out.println("Client error: number must be greater than 0.");
                System.exit(1);
            }
            MethodInterface m;
            try{
                if(rmiLocation.contains("127.0.0.1")){
                    m = (MethodInterface) Naming.lookup(rmiLocation);
                }else{
                    m = (MethodInterface) LocateRegistry.getRegistry().lookup(rmiLocation);
                }
                m = (MethodInterface) Naming.lookup(rmiLocation);
                if(method.equalsIgnoreCase("fibonacci")){
                    long result = m.fibonacci(number);
                    System.out.println("Fibonacci of " + number + " is: " + result);
                }else if(method.equalsIgnoreCase("factorial")){
                    long result = m.factorial(number);
                    System.out.println("Factorial of " + number + " is: " + result);
                }else{
                    System.out.println("Client error: invalid method" );
                }
            }catch(Exception e){
                System.out.println("Client error: RMI lookup failed, " + e.getMessage());
            }
        }
    }
}
