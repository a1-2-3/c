import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {
    public static void main(String[] a) throws Exception {
        String mode = (a.length > 0) ? a[0] : "tcp";
        int port = (a.length > 1) ? Integer.parseInt(a[1]) : 5000;
        if (mode.equals("tcp"))
            runTCP(port);
        else
            runUDP(port);
    }

    static void runTCP(int port) throws Exception {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("TCP Chat server:" + port);
        Set<PrintWriter> outs = ConcurrentHashMap.newKeySet();
        while (true) {
            Socket s = ss.accept();
            new Thread(() -> {
                try (s) {
                    var in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    var out = new PrintWriter(s.getOutputStream(), true);
                    outs.add(out);
                    String name = in.readLine();
                    broadcast(outs, "[join] " + name, null);
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.equals("/quit"))
                            break;
                        broadcast(outs, name + ": " + line, out);
                    }
                    outs.remove(out);
                    broadcast(outs, "[left] " + name, null);
                } catch (Exception e) {
                }
            }).start();
        }
    }

    static void broadcast(Set<PrintWriter> outs, String msg, PrintWriter skip) {
        for (PrintWriter o : outs)
            if (o != skip)
                o.println(msg);
    }

    static void runUDP(int port) throws Exception {
        DatagramSocket ds = new DatagramSocket(port);
        System.out.println("UDP Chat server:" + port);
        byte[] buf = new byte[1024];
        Set<SocketAddress> clients = new HashSet<>();
        while (true) {
            DatagramPacket p = new DatagramPacket(buf, buf.length);
            ds.receive(p);
            String msg = new String(p.getData(), 0, p.getLength()).trim();
            clients.add(p.getSocketAddress());
            for (SocketAddress c : clients)
                if (!c.equals(p.getSocketAddress()))
                    ds.send(new DatagramPacket(msg.getBytes(), msg.length(), c));
        }
    }
}
