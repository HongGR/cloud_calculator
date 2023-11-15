import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CalculatorClient {
    private static final String CONFIG_FILE_PATH = "server_config.txt";

    public static void main(String[] args) {
        // Read server information from the configuration file
        String serverIP = "localhost";
        int serverPort = 1234;

        try (BufferedReader configFileReader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            serverIP = configFileReader.readLine();
            serverPort = Integer.parseInt(configFileReader.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading config file. Using default values (localhost, 1234).");
        }

        try (Socket socket = new Socket(serverIP, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Get arithmetic expression from the user
            System.out.println("Enter arithmetic expression (e.g., 10 + 10, 10 - 5, 10 * 10, 20 / 4):");
            String expression = reader.readLine();

            // Convert the expression to the protocol format and send it to the server
            String protocolMessage = convertToProtocol(expression);
            writer.println(protocolMessage);

            // Receive and process the server's response
            try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String response = serverReader.readLine();
                System.out.println("Server response: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String convertToProtocol(String expression) {
        // Convert the arithmetic expression to the protocol format (e.g., ADD 10 10)
        String[] tokens = expression.split("\\s+");
        
        if (tokens.length == 3) {
            String operator = tokens[1];
            String operand1 = tokens[0];
            String operand2 = tokens[2];

            switch (operator) {
                case "+":
                    return "ADD " + operand1 + " " + operand2;
                case "-":
                    return "SUB " + operand1 + " " + operand2;
                case "*":
                    return "MUL " + operand1 + " " + operand2;
                case "/":
                    return "DIV " + operand1 + " " + operand2;
                default:
                    System.out.println("Unsupported operator. Using default ADD operator.");
                    return "ADD " + operand1 + " " + operand2;
            }
        } else {
            System.out.println("Invalid expression format. Using default ADD operator.");
            return "ADD 0 0";
        }
    }
}