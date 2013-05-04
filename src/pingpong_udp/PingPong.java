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
    
    private static final int server_port = 4242; // No one else will use this portnumber?
    private static final int buffer_length = 1024;
    
    public static void server () throws Exception {
        DatagramSocket sock = new DatagramSocket(server_port);
        byte[] buffer = new byte[buffer_length];
        DatagramPacket packet;
        boolean running = true;
        while (running) {
            // Prepare a buffer to hold the next message
            packet = new DatagramPacket(buffer,buffer.length);
            sock.receive(packet);
            // Interpret the message as a string with given length
            String content = new String(packet.getData(),0,packet.getLength());
            System.out.println("Received: "+content);
            if (content.equals("END"))
                running = false;
            InetAddress client_addr = packet.getAddress();
            int client_port = packet.getPort();
            content += " BACK";
            buffer = content.getBytes();
            packet = new DatagramPacket(buffer,content.length(),client_addr,client_port);
            sock.send(packet);
        }
        sock.close();
    }

    public static void client ( String server_host ) throws Exception {
        System.out.println("Connecting to host "+server_host);
        InetAddress server_addr = InetAddress.getByName(server_host);
        DatagramSocket sock = new DatagramSocket();
        byte[] buffer;
        DatagramPacket packet;
        for (int m=0; m<10; m++) {
            String message = "BALL "+m;
            buffer = message.getBytes();
            packet = new DatagramPacket(buffer,message.length(),server_addr,server_port);
            sock.send(packet);
            packet = new DatagramPacket(buffer,buffer.length);
            sock.receive(packet);
            String content = new String(packet.getData(),0,packet.getLength());
            System.out.println("Received: "+content);
            Thread.sleep(1000);
        }
        // Sending the final message
        String end_message = "END";
        buffer = end_message.getBytes();
        packet = new DatagramPacket(buffer,end_message.length(),server_addr,server_port);
        sock.send(packet);
        sock.close();
    }    
}
