import java.rmi.*;

/**
 * @author Anthony Rojas
 * Assignment 6 - Method Interface
 */
public interface MethodInterface extends Remote {
    public int fibonacci(int n) throws RemoteException;
    public int factorial(int n) throws RemoteException;
}