import java.rmi.registry.*;
import java.util.*;
public class RMIClient { 
    public static void main(String[] args) throws Exception {
        String host = (args.length > 0) ? args[0] : "127.0.0.1";
        int port = (args.length > 1) ? Integer.parseInt(args[1]) : 1099;
        RMIServer.Calculator calc = (RMIServer.Calculator) LocateRegistry.getRegistry(host, port).lookup("Calc");
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter op(+,-,*,/) or quit: ");
            String op = sc.next();
            if (op.equals("quit"))
                break;
            System.out.print("a: ");
            double a = sc.nextDouble();
            System.out.print("b: ");
            double b = sc.nextDouble();
            double r = switch (op) {
                case "+":
                    yield calc.add(a, b);
                case "-":
                    yield calc.sub(a, b);
                case "*":
                    yield calc.mul(a, b);
                case "/":
                    yield calc.div(a, b);
                default:
                    yield Double.NaN;
            };
            System.out.println("Result=" + r);
        }
        sc.close();
    }
}
