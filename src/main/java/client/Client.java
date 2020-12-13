package client;

import clientmodel.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import servermodel.*;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1488)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                 BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
                ObjectMapper mapper = new ObjectMapper();

                System.out.println("Waiting for players...");

                int firstCost = mapper.readValue(reader.readLine(), Card.class).getCost();

                System.out.printf("You got: %d.%n", firstCost);

                System.out.println("Waiting for turn...");

                while (true) {
                    RoundNotification roundNotification = mapper.readValue(reader.readLine(), RoundNotification.class);
                    if (!roundNotification.isNextRound()) break;

                    boolean isNext;
                    while (true) {
                        System.out.print("More? (yes/no)> ");

                        String answer = consoleReader.readLine();
                        if (answer.toLowerCase().equals("yes")) {
                            isNext = true;
                            break;
                        } else if (answer.toLowerCase().equals("no")) {
                            isNext = false;
                            break;
                        }
                    }

                    writer.println(mapper.writeValueAsString(new Answer(isNext)));

                    if (isNext) {
                        Card card = mapper.readValue(reader.readLine(), Card.class);
                        System.out.printf("You got: %d%n", card.getCost());
                        Points points = mapper.readValue(reader.readLine(), Points.class);
                        System.out.printf("Current score: %d%n", points.getPoints());
                        if (points.getPoints() >= 21) {
                            break;
                        }
                    } else {
                        System.out.println("Waiting for the result...");
                        break;
                    }
                }

                Result answer = mapper.readValue(reader.readLine(), Result.class);

                System.out.println(answer);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
