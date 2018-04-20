import java.net.*;
import java.io.*;
import java.util.*;
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
        String reply;
        switch (nodeStr.length()){
            case 0: reply = "NULL";
                break;
            case 1:
                reply = getPastryReply(nodeStr, 1);
                break;
            case 2:
                reply = getPastryReply(nodeStr, 2);
                break;
            case 3:
                reply = getPastryReply(nodeStr, 3);
                break;
            case 4:
                reply = getPastryReply(nodeStr, 4);
                break;
            default: reply = "INVALID_REQUEST";
        }

        return reply;
    }

    public static String getPastryReply(String nodeStr, int length){
        if(String.valueOf(MY_PASTRY).startsWith(nodeStr)){
            return String.valueOf(MY_PASTRY) + ":" + MY_IP ;
        }else{
            for(String k : leafSet.keySet()){
                if(k.startsWith(nodeStr)){
                    return k + ":" + leafSet.get(k);
                }
            }
            for(String k : routingTable.keySet()){
                if(k.startsWith(nodeStr)){
                    return k  + ":" + routingTable.get(k);
                }
            }
        }
        return "NULL";
    }
}
