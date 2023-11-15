import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is listening on port 1234...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Read the protocol message from the client
            String protocolMessage = reader.readLine();

            // Process the protocol message and send the result back
            String resultMessage = processProtocolMessage(protocolMessage);
            writer.println(resultMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processProtocolMessage(String protocolMessage) {
        String[] tokens = protocolMessage.split("\\s+");

        if (tokens.length == 3 && (tokens[0].equals("ADD") || tokens[0].equals("SUB") || tokens[0].equals("MUL") || tokens[0].equals("DIV"))) {
            try {
                double operand1 = Double.parseDouble(tokens[1]);
                double operand2 = Double.parseDouble(tokens[2]);

                switch (tokens[0]) {
                    case "ADD":
                        return "result: " + (operand1 + operand2);
                    case "SUB":
                        return "result: " + (operand1 - operand2);
                    case "MUL":
                        return "result: " + (operand1 * operand2);
                    case "DIV":
                        if (operand2 != 0) {
                            return "result: " + (operand1 / operand2);
                        } else {
                            return "ERROR: Division by zero";
                        }
                    default:
                        return "ERROR: Invalid operator";
                }

            } catch (NumberFormatException e) {
                return "ERROR: Invalid operand format";
            }
        } else {
            return "ERROR: Invalid message format";
        }
    }
}