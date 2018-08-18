package goodchan.fam_map_client;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import goodchan.fam_map_client.Responses.*;
import goodchan.fam_map_client.Requests.*;

public class Proxy {
    private static Logger logger;

    static {
        logger = Logger.getLogger("Client");
    }

    private String serverHost = "";
    private String serverPort = "";
    Gson gson = new Gson();

    public Proxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public Response register(RegisterRequest registerRequest){
        logger.info("in Proxy Register");
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            logger.info("before connection");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            logger.info("after connection");
            http.setDoOutput(true);
            http.addRequestProperty("Accept", "application/json");
            String request = gson.toJson(registerRequest);
            http.connect();
            OutputStream reqBody = http.getOutputStream();
            writeString(request, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                logger.info("in response, came back okay");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                if (respData.contains("{\"message\":")) {
                    logger.info("return message response");
                    return gson.fromJson(respData, MessageResponse.class);
                }
                else {
                    logger.info("return user response");
                    return gson.fromJson(respData, UserResponse.class);
                }
            } else {
                logger.info("else errror, http not okay");
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageResponse fail = new MessageResponse("Error with connection");
        return fail;
    }


    public Response logIn(LoginRequest loginRequest){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Authorization", ""); //we need authorization TODO is this nessicerry to recieve authorizatio back
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            String reqData = gson.toJson(loginRequest);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                if (respData.contains("{\"message\":")) {
                    return gson.fromJson(respData, MessageResponse.class);
                }
                else {
                    return gson.fromJson(respData, UserResponse.class);
                }
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageResponse fail = new MessageResponse("Error Registering: hit end of login in proxy");
        return fail;
    }

    public Response getEvents(UserResponse userResponse){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", userResponse.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();


            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                if (respData.contains("{\"message\":")) {
                    return gson.fromJson(respData, MessageResponse.class);
                }
                else {
                    EventDataResponse response = gson.fromJson(respData, EventDataResponse.class);
                    return response;
                }
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return new MessageResponse(e.getMessage());
        }
        MessageResponse fail = new MessageResponse("Error Registering: hit end of register in proxy");
        return fail;
    }

    public Response getPeople(UserResponse userResponse){
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person/");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", userResponse.getAuthToken());
            http.addRequestProperty("Accept", "application/json");
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                if (respData.contains("{\"message\":")) {
                    return gson.fromJson(respData, MessageResponse.class);
                }
                else {
                    return gson.fromJson(respData, PersonDataResponse.class);
                }
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        MessageResponse fail = new MessageResponse("Error Registering: hit end of getPeople in proxy");
        return fail;
    }

    /*
            The readString method shows how to read a String from an InputStream.
        */
    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

