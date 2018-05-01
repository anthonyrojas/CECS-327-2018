import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.net.*;
public class PastryClient {
    public static final String AWS_INITIAL_IP = "54.183.205.102";
    public static final int SERVER_PORT = 32710;
    //timeout time will be 1 seconds
    public static final int MAX_TIME = 1000;
    public static void main(String [] args){
        //String serverReply = getServerReply(randStr, AWS_INITIAL_IP);
        //System.out.println(serverReply);
        ArrayList<Integer> hops = new ArrayList<Integer>();
        Map<Integer, Integer> histogram = initHistogram();
        for(int i=0; i < 1000; i++){
            int hopCount = 0;
            boolean found = false;
            String randStr = generateRandomPastry();
            String ip = AWS_INITIAL_IP;
            //System.out.println(randStr);
            while(found == false){
                hopCount++;
                String reply = getServerReply(randStr, ip);
                reply = reply.replace(" ", "");
                reply = reply.trim();
                if(reply.toUpperCase().contains("NULL")){
                    found = true;
                    hops.add(hopCount);
                }else if(reply.toUpperCase().contains("INVALID")){
                    found = true;
                    hops.add(hopCount);
                }else if(!reply.contains(randStr) && reply.contains(":")){
                    found = false;
                    ip = reply.split(":")[1];
                }else if(reply.contains(randStr)){
                    found = true;
                    hops.add(hopCount);
                }else{
                    found = true;
                    //discard this run
                }
            }
        }
        for(int h : hops){
            int currCount = histogram.get(h);
            histogram.replace(h, currCount + 1);
        }
        for(int h : histogram.keySet()){
            System.out.println(h + " : " + histogram.get(h));
        }
    }

    public static Map initHistogram(){
        Map<Integer, Integer> histogram = new HashMap<Integer, Integer>();
        for(int i=1; i <= 10; i++){
            histogram.put(i, 0);
        }
        return histogram;
    }

    public static String generateRandomPastry(){
        Random rand = new Random();
        String randStr = String.valueOf(rand.nextInt(4));
        while(randStr.length() < 4){
            randStr += String.valueOf(rand.nextInt(4));
        }
        return randStr;
    }
    public static String getServerReply(String randStr, String ip){
        String serverReply = "ERROR";
        DatagramSocket aSocket = null;
        try{
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(MAX_TIME);
            byte [] m = randStr.getBytes();
            InetAddress aHost = InetAddress.getByName(ip);
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, SERVER_PORT);
            aSocket.send(request);
            byte[] buffer = new byte[300];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            serverReply = new String(reply.getData()).trim();
        }catch(SocketException e){
            return e.getMessage();
            //System.out.println("Socket: " + e.getMessage());
        }catch(IOException e){
            return e.getMessage();
            //System.out.println("IO: " + e.getMessage());
        }finally{
            if(aSocket != null){
                aSocket.close();
            }
        }
        return serverReply;
    }
}
