import java.io.*;
import java.util.*;
import java.net.*;
public class Pastry {
    private static Map<Integer, String> leafSet;
    private static Map<Integer, String> routingTable;
    private static int assignedPastry = 1203;
    public static void main(String [] args){
        leafSet = new TreeMap<>();
        leafSet.put(1131, "52.10.42.73");
        leafSet.put(1200, "18.188.49.131");
        leafSet.put(1220, "52.53.151.57");
        leafSet.put(1300, "18.219.11.198");
        routingTable = new HashMap<>();
    }
}