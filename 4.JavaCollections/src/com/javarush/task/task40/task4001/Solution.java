package com.javarush.task.task40.task4001;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/* 
POST, а не GET
*/

public class Solution {
    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();
        solution.sendPost(new URL("https://enzh5xsck9ie.x.pipedream.net"), "name=zapp&mood=good&locale=&id=777");
    }

    public void sendPost(URL url, String urlParameters) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        //// HTTP POST request
        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(urlParameters);
        out.close();

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String responseLine;
        StringBuilder response = new StringBuilder();

        while ((responseLine = bufferedReader.readLine()) != null) {
            response.append(responseLine);
        }
        bufferedReader.close();

        System.out.println("Response: " + response.toString());
    }
}
