package org.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class ParamedicAuth {

    private static final String LOGIN_URL = "http://localhost:8080/paramedics/login";

    public static String login() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        String token = getTokenFromLoginEndpoint(email, password);

        if (token != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(token);

            System.out.println("Login successful! Your auth token is: " + token);
            return responseJson.get("Token").asText();

        } else {
            System.out.println("Login failed. Please check your credentials and try again.");
            return "";
        }
    }

    private static String getTokenFromLoginEndpoint(String email, String password) throws IOException {
        URL url = new URL(LOGIN_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String body = "email=" + email + "&password=" + password;
        conn.getOutputStream().write(body.getBytes());

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String response = "";
            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNextLine()) {
                response += scanner.nextLine();
            }
            scanner.close();
            return response;
        } else {
            return null;
        }
    }

    public static String registration() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name:");
        String name = scanner.nextLine();
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();
        System.out.println("Please enter your date of birth (yyyy-MM-dd):");
        String dateOfBirth = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Are you currently accepting assignments? (true/false)");
        boolean isAccepting = scanner.nextBoolean();


        try {
            URL url = new URL("http://localhost:8080/paramedics/register");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{"
                    + "\"name\":\"" + name + "\","
                    + "\"email\":\"" + email + "\","
                    + "\"date_of_birth\":\"" + dateOfBirth + "\","
                    + "\"password\":\"" + password + "\","
                    + "\"is_accepting\":" + isAccepting
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response code: " + responseCode);

            try (Scanner s = new Scanner(conn.getInputStream())) {
                String responseBody = s.useDelimiter("\\A").next();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseBody);

                System.out.println("Registration successful! Your auth token is: " + responseBody);
                return responseJson.get("Token").asText();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}

