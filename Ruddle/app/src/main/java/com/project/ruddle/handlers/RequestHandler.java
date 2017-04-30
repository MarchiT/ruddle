package com.project.ruddle.handlers;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestHandler {
    private static final String HEADER_KEY = "Content-Type";
    private static final String HEADER_VALUE = "application/x-www-form-urlencoded";

    private static void outputPost(HttpURLConnection connection, String urlParams) throws IOException {
        OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
        request.write(urlParams);
        request.flush();
        request.close();
        Integer status = connection.getResponseCode();
        Log.i("POST Response Code:", status.toString());
    }

    private static String inputRead(HttpURLConnection connection) throws IOException {
        String line = "";
        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }
        isr.close();
        reader.close();

        return response.toString();
    }

    public static String sendGet(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty(HEADER_KEY, HEADER_VALUE);

            connection.setRequestMethod("GET");

            return inputRead(connection);
        } catch (IOException e) {
            Log.e("HTTP GET:", e.toString());
            return e.toString();
        }
    }

    public static Boolean sendPostStatus(String stringUrl, JSONObject urlParams) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty(HEADER_KEY, HEADER_VALUE);

            connection.setRequestMethod("POST");

            outputPost(connection, urlParams.toString());
            return true;
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
            return false;
        }
    }

    public static String sendPostMessage(String stringUrl, JSONObject urlParams) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestProperty(HEADER_KEY, HEADER_VALUE);

            connection.setRequestMethod("POST");

            outputPost(connection, urlParams.toString());

            return inputRead(connection);
        } catch (IOException e) {
            Log.e("HTTP POST:", e.toString());
            return e.toString();
        }
    }


}
