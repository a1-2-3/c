import java.net.*;

public class HelloEcho {
    public static void main(String[] args) {
        String host = "tax.com";
        try {
            InetAddress ip = InetAddress.getByName(host);
            System.out.println("Pinging " + host + " ...");
            if (ip.isReachable(5000)) {
                System.out.println(host + " is reachable");
            } else {
                System.out.println(host + " is not reachable");
            }
        }
        
        
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
