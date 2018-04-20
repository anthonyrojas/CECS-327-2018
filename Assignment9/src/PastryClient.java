import java.net.*;
import java.io.*;
public class PastryClient {
    public static void main(String [] args){
        if(args.length < 2){
            System.out.println("Insufficient amount of arguments " +
                    "\n You must enter: java PastryClient <pastry id wrapped in quotes> <IP address>");
            System.exit(1);
        }else{
            DatagramSocket aSocket = null;
            try{
                aSocket = new DatagramSocket();
                byte [] m = args[0].getBytes();
                InetAddress aHost = InetAddress.getByName(args[1]);
                int serverPort = 32710;
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                aSocket.send(request);
                byte [] buffer = new byte[300];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                //DatagramPacket reply = new DatagramPacket(args[0].getBytes(), args[0].getBytes().length);
                aSocket.receive(reply);
                System.out.println("\t" + new String(reply.getData()));
            }catch(SocketException e){
                System.out.println("Socket: " + e.getMessage());
            }catch (IOException e){
                System.out.println("IO: " + e.getMessage());
            }finally {
                if(aSocket != null){
                    aSocket.close();
                }
            }
        }
    }
}
