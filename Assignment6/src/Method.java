import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Anthony Rojas
 * Assignment 6 - Method
 */
public class Method extends UnicastRemoteObject implements MethodInterface {
    public Method() throws RemoteException{
    }

    public int fibonacci(int n){
        int sum = fibonacciHelper(n);
        return sum;
    }
    public int factorial(int n){
        int fact = n;
        for(int i=n-1; i > 0; i--){
            fact = fact * i;
        }
        return fact;
    }

    public int fibonacciHelper(int n){
        if(n<=1){
            return 1;
        }
        else{
            return fibonacciHelper(n-1) + fibonacciHelper(n-2);
        }
    }
}