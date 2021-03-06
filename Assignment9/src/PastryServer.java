import java.net.*;
import java.io.*;
import java.util.*;

import jdk.nashorn.internal.ir.ReturnNode;
public class PastryServer {
    private static Map<String, String> leafSet;
    private static Map<String, String> routingTable;
    private final static int MY_PASTRY = 1201;
    private final static String MY_IP = "54.183.205.102";
    public static void main(String [] args){
        leafSet = new TreeMap<>();
        leafSet.put("1131", "52.10.42.73");
        leafSet.put("1200", "18.188.49.131");
        leafSet.put("1220", "52.53.151.57");
        leafSet.put("1300", "18.219.11.198");
        routingTable = new TreeMap<>();
        routingTable.put("0001", "54.67.23.56");
        routingTable.put("1201", "54.183.205.102");
        routingTable.put("2020", "18.219.20.177");
        routingTable.put("3001", "13.57.5.2");
        routingTable.put("1013", "18.144.32.67");
        routingTable.put("1100", "18.217.157.190");
        routingTable.put("1300", "18.219.11.198");
        routingTable.put("121", "NULL");
        routingTable.put("1220", "52.53.157.57");
        routingTable.put("123", "NULL");
        routingTable.put("1200", "18.188.49.131");
        routingTable.put("1202", "NULL");
        routingTable.put("1203", "NULL");
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket(32710);
            while (true){
                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                String nodeStr = new String(request.getData());
                nodeStr = nodeStr.replace(" ", "");
                nodeStr = nodeStr.trim();
                String pastry = getPastry(nodeStr);
                DatagramPacket reply = new DatagramPacket(pastry.getBytes(), pastry.getBytes().length, request.getAddress(), request.getPort());
                aSocket.send(reply);
            }
        }catch(SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }catch(IOException e){
            System.out.println("IO: " + e.getMessage());
        }finally{
            if(aSocket != null){
                aSocket.close();
            }
        }
    }
    public static String getPastry(String nodeStr){
        try{
            Integer.parseInt(nodeStr);
        }catch (Exception e){
            return "INVALID_REQUEST";
        }
        String reply;
        if(nodeStr.length() < 1){
            return "NULL";
        }else if(nodeStr.length() > 4){
            return "INVALID_REQUEST";
        }else{
            return getPastryReply(nodeStr, nodeStr.length());
        }
    }

    public static String getPastryReply(String nodeStr, int length){
        if(String.valueOf(MY_PASTRY).startsWith(nodeStr)){
            return String.valueOf(MY_PASTRY) + ":" + MY_IP ;
        }else{
            boolean found = false;
            for(String k : leafSet.keySet()){
                if(k.startsWith(nodeStr)){
                    found = true;
                    return k + ":" + leafSet.get(k);
                }
            }
            if(!found){
                return locateInRoutingTable(nodeStr);
            }
        }
        return "NULL";
    }

    public static String locateInRoutingTable(String pastryStr){
        boolean found = false;
        while(pastryStr.length() > 0){
            for(String k : routingTable.keySet()){
                if(k.startsWith(pastryStr)){
                    return k + ":" + routingTable.get(k);
                }
            }
            pastryStr = pastryStr.substring(0, pastryStr.length()-1);
        }
        return "NULL";
    }

    public static String findInRoutingTable(String pastryStr){
        boolean foundPastry = false;
        String response = "";
        for(String k : routingTable.keySet()){
            if(k.startsWith(pastryStr)){
                foundPastry = true;
                response = k + ":" + routingTable.get(k);
            }
        }
        if(foundPastry != true && pastryStr.length()==1){
            return "NULL";
        }else if(foundPastry != true){
            pastryStr = pastryStr.substring(0, pastryStr.length());
            return findInRoutingTable(pastryStr);
        }else{
            return response;
        }
    }
}
