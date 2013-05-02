package pingpong_udp;

import java.io.*;
import java.net.*;

/**
 *
 * @author Peter Sturm, University of Trier
 */
public class PingPong {

    /**
     * @param args = host address of server, empty in case of server
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            server();
        else
            client(args[0]);
    }
    
    public static void server () throws Exception {
        DatagramSocket sock = new DatagramSocket(4242);
        byte[] message = new byte[1024];
        DatagramPacket packet;
        boolean running = true;
        while (running) {
            packet = new DatagramPacket(message,message.length);
            sock.receive(packet);
            String content = new String(packet.getData());
            System.out.println("Received: "+content);
            if (content.equals("END"))
                running = false;
            InetAddress client_addr = packet.getAddress();
            int client_port = packet.getPort();
            content += " BACK";
            message = content.getBytes();
            packet = new DatagramPacket(message,message.length,client_addr,client_port);
            sock.send(packet);
        }
        sock.close();
    }

    public static void client ( String server_host ) throws Exception {
        System.out.println("Connecting to host "+server_host);
        InetAddress server_addr = InetAddress.getByName(server_host);
        DatagramSocket sock = new DatagramSocket();
        byte[] message = new byte[1024];
        DatagramPacket packet;
        for (int m=0; m<10; m++) {
            message = new String("BALL"+m).getBytes();
            packet = new DatagramPacket(message,message.length,server_addr,4242);
            sock.send(packet);
            packet = new DatagramPacket(message,message.length);
            sock.receive(packet);
            String content = new String(packet.getData());
            System.out.println("Received: "+content);
        }
        message = new String("END").getBytes();
        packet = new DatagramPacket(message,message.length,server_addr,4242);
        sock.send(packet);
        sock.close();
    }    
}
