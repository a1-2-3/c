import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class RMIServer {

    public interface Calculator extends Remote {
        double add(double a, double b) throws RemoteException;
        double sub(double a, double b) throws RemoteException;
        double mul(double a, double b) throws RemoteException;
        double div(double a, double b) throws RemoteException;}

    public static class CalculatorImpl extends UnicastRemoteObject implements Calculator {
        protected CalculatorImpl() throws RemoteException {
            super();
        }
        public double add(double a, double b) {
            return a + b;
        }
        public double sub(double a, double b) {
            return a - b;
        }
        public double mul(double a, double b) {
            return a * b;
        }
        public double div(double a, double b) {
            if (b == 0)
                throw new ArithmeticException("Divide by zero");
            return a / b;
        }
    }

    public static void main(String[] args) throws Exception {
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 1099;
        Registry reg;
        try {
            reg = LocateRegistry.createRegistry(port);
        } catch (Exception e) {
            reg = LocateRegistry.getRegistry(port);
        }
        reg.rebind("Calc", new CalculatorImpl());
        System.out.println("Calculator ready on port " + port);
    }
}
