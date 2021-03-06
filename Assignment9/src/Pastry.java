import java.io.*;
import java.util.*;
import java.net.*;
public class Pastry {
    private static Map<Integer, String> leafSet;
    private static Map<Integer, String> routingTable;
    private static int assignedPastry = 1203;
    public static void main(String [] args) {
        leafSet = new TreeMap<>();
        leafSet.put(1131, "52.10.42.73");
        leafSet.put(1200, "18.188.49.131");
        leafSet.put(1220, "52.53.151.57");
        leafSet.put(1300, "18.219.11.198");
        routingTable = new HashMap<>();
        routingTable.put(0001, "54.67.23.56");
        routingTable.put(1201, "54.183.205.102");
        routingTable.put(2020, "18.219.20.177");
        routingTable.put(3001, "13.57.5.2");
        routingTable.put(1013, "18.144.32.67");
        routingTable.put(1100, "18.217.157.190");
        routingTable.put(1300, "18.219.11.198");
        routingTable.put(121, "NULL");
        routingTable.put(1220, "52.53.157.57");
        routingTable.put(123, "NULL");
        routingTable.put(1200, "18.188.49.131");
        routingTable.put(1202, "NULL");
        routingTable.put(1203, "NULL");
    }
}