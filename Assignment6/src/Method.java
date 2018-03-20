import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Anthony Rojas
 * Assignment 6 - Method
 */
public class Method extends UnicastRemoteObject implements MethodInterface {
    public Method() throws RemoteException{
    }

    public long fibonacci(long n){
        long sum = fibonacciHelper(n);
        return sum;
    }
    public long factorial(long n){
        long fact = n;
        for(long i=n-1; i > 0; i--){
            fact = fact * i;
        }
        return fact;
    }

    public long fibonacciHelper(long n){
        if(n<=1){
            return 1;
        }
        else{
            return fibonacciHelper(n-1) + fibonacciHelper(n-2);
        }
    }
}