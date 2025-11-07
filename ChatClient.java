import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
    public static void main(String[] a) throws Exception {
        String mode = (a.length > 0) ? a[0] : "tcp";
        String host = (a.length > 1) ? a[1] : "127.0.0.1";
        int port = (a.length > 2) ? Integer.parseInt(a[2]) : 5000;
        if (mode.equals("tcp"))
            runTCP(host, port);
        else
            runUDP(host, port);
    }

    static void runTCP(String host, int port) throws Exception {
        Socket s = new Socket(host, port);
        var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        var out = new PrintWriter(s.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);
        System.out.print("Name: ");
        String name = sc.nextLine();
        out.println(name);
        new Thread(() -> {
            try {
                String l;
                while ((l = in.readLine()) != null)
                    System.out.println(l);
            } catch (Exception e) {
            }
        }).start();
        while (true) {
            String line = sc.nextLine();
            out.println(line);
            if (line.equals("/quit"))
                break;
        }
        s.close();
        sc.close();
    }

    static void runUDP(String host, int port) throws Exception {
        DatagramSocket ds = new DatagramSocket();
        InetAddress ip = InetAddress.getByName(host);
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            try {
                byte[] b = new byte[1024];
                while (true) {
                    DatagramPacket p = new DatagramPacket(b, b.length);
                    ds.receive(p);
                    System.out.println(new String(p.getData(), 0, p.getLength()));
                }
            } catch (Exception e) {
            }
        }).start();
        System.out.print("Name: ");
        String name = sc.nextLine();
        while (true) {
            String line = sc.nextLine();
            if (line.equals("/quit"))
                break;
            String msg = name + ": " + line;
            ds.send(new DatagramPacket(msg.getBytes(), msg.length(), ip, port));
        }
        ds.close();
        sc.close();
    }
}
