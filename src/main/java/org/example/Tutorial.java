package org.example;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
public class Tutorial {
    public static void getTutorials(String authToken) throws Exception {
        if (authToken == null) {
            System.out.println("You need to login first to get tutorials!");
            return;
        }

        URL url = new URL("http://localhost:8080/tutorials/tutorials");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("authToken", authToken);

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            Scanner scanner = new Scanner(conn.getInputStream());
            String response = scanner.useDelimiter("\\A").next();

            System.out.println("Tutorials: " + response);
        } else {
            System.out.println("Failed to get tutorials! Response code: " + responseCode);
        }

        conn.disconnect();
    }

    public static void assignTutorial(String authToken, String tutorialId) throws Exception{
        if (authToken == null) {
            System.out.println("You need to login first to get tutorials!");
            return;
        }
        String endpoint = "http://localhost:8080/paramedics/assign-tutorial";

        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // set request method and headers
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("authToken", authToken);
        conn.setDoOutput(true);

        // set request body
        String requestBody = "tutorial_id=" + Integer.parseInt(tutorialId);
        byte[] postData = requestBody.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            try {
                wr.write(postData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // read response
        int responseCode = 0;
        try {
            responseCode = conn.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // print response
        System.out.println("Response code: " + responseCode);
        System.out.println("Response body: " + response.toString());
    }

    static void getAssignedTutorial(String authToken) {
        try {
            URL url = new URL("http://localhost:8080/paramedics/paramedic-tutorials");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("authToken", authToken);
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print the response
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
