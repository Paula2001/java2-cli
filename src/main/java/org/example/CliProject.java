package org.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

@Command(name = "cli-project", mixinStandardHelpOptions = true, version = "1.0",
        description = "A CLI project that consumes REST endpoints and sends token with each request.")
public class CliProject implements Runnable {

    @Option(names = {"-t", "--token"}, description = "Auth token")
    private String authToken;

    public static void main(String[] args) {
        CommandLine.run(new CliProject(), args);
    }

    @Override
    public void run() {
        System.out.println("Welcome to the CLI project!");
        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("Select your language preference (1 - English, 2 - Czech): ");
            int languageChoice = scanner.nextInt();

            Locale locale = languageChoice == 1 ? Locale.ENGLISH : new Locale("cs", "CZ");

            ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle", locale);
            System.out.println(messages.getString("menu.login"));
            System.out.println(messages.getString("menu.register"));
            System.out.println(messages.getString("menu.get_tutorials"));
            System.out.println(messages.getString("menu.assign_tutorial"));
            System.out.println(messages.getString("menu.get_assigned_tutorial"));
            System.out.println(messages.getString("menu.exit"));

            int option = scanner.nextInt();

            switch (option) {
                case 0:
                    System.out.println("Goodbye!");
                    System.exit(0);
                case 1:
                    try {
                        authToken = ParamedicAuth.login();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 2:
                        authToken = ParamedicAuth.registration();
                    break;
                case 3:
                    try {
                        Tutorial.getTutorials(authToken);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    System.out.println("please insert tutorial idw");
                    Scanner s = new Scanner(System.in);
                    String id = s.next();
                    try {
                        Tutorial.assignTutorial(authToken, id);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    Tutorial.getAssignedTutorial(authToken);
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void register() {
        // Implement register logic here
        System.out.println("You selected option 2 - Register paramedic");
    }

    private void getTutorials() {
        // Implement get tutorials logic here
        System.out.println("You selected option 3 - Get tutorials");
    }

    private void assignTutorial() {
        // Implement assign tutorial logic here
        System.out.println("You selected option 4 - Assign tutorial to paramedic");
    }

    private void getAssignedTutorial() {
        // Implement get assigned tutorial logic here
        System.out.println("You selected option 5 - Get the tutorial assigned");
    }
}
