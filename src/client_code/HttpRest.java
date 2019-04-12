package client_code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class HttpRest{
    public static ArrayList<String> executeGet(String IP){
        ArrayList<String> request = new ArrayList<String>();
        try {
            URL url = new URL(IP);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode() + conn.getResponseMessage());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String[] parts = br.readLine().split("(?<=\\}),");
            request = clean_rest(parts, request);
            conn.disconnect();
            return request;

        } catch (IOException e) {
            e.printStackTrace();
        }
    return request;
    }

    public static ArrayList executePost(String IP, String Payload){
        ArrayList<String> request = new ArrayList<String>();
        try {
            URL url = new URL(IP);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(Payload.getBytes());
            os.flush();
            int code = conn.getResponseCode();
            //if (code == 401 || code == 402 || code == 403 || code == 404 || code == 405)
            //    throw new RuntimeException("Failed : HTTP error code");
            if(code == 500)
                    throw new RuntimeException("Failed : HTTP error code");


            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String[] parts = br.readLine().split("(?<=\\}),");
            request = clean_rest(parts, request);

            conn.disconnect();
            return request;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            throw new RuntimeException("Server down!");
        }
        return request;
    }

    public static ArrayList<String> clean_rest(String[] parts, ArrayList request){
        for (String i : parts) {
            if (i.contains("[") )
            {
                request.add(i.replace("[",""));
            }
            else if (i.contains("]")) {
                request.add(i.replace("]",""));
            }
            else{
                request.add(i);
            }
        }
    return request;
    }
}
