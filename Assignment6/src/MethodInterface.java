import java.rmi.*;

/**
 * @author Anthony Rojas
 * Assignment 6 - Method Interface
 */
public interface MethodInterface extends Remote {
    public long fibonacci(long n) throws RemoteException;
    public long factorial(long n) throws RemoteException;
}