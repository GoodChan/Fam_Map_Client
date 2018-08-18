package goodchan.fam_map_client;

import android.provider.Contacts;

import com.google.gson.Gson;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.Model.SuperModel;
import goodchan.fam_map_client.Model.User;
import goodchan.fam_map_client.Requests.LoginRequest;
import goodchan.fam_map_client.Requests.RegisterRequest;
import static org.junit.Assert.*;
import goodchan.fam_map_client.Responses.*;

public class ProxyUnitTest {
    Proxy proxy = new Proxy("localhost", "8080"); // my laptop's IP address
    LoginRequest loginRequest = new LoginRequest("clientUser2", "client");
    //emulator 10.0.2.2
    UserResponse userResponse = null;

    @Test
    public void register() {
        RegisterRequest registerRequest = new RegisterRequest("clientUser2", "client", "client@client.com",
                "firstanme", "lastname", "f");

        Response response = proxy.register(registerRequest);
        if (response.getClass() == UserResponse.class) {
            UserResponse userResponse = (UserResponse)response;
            assertEquals(userResponse.getUserName(), registerRequest.getUserName());
            System.out.println("Proxy register test PersonID: " + userResponse.getPersonID() +
                    "   AuthToken: " + userResponse.getAuthToken());
            this.userResponse = userResponse;
        }
        else {
            MessageResponse messageResponse = (MessageResponse)response;
            System.out.println(messageResponse.getMessage());
        }
    }

    @Test
    public void logIn() {
        Response response = proxy.logIn(loginRequest);
        if (response.getClass() == UserResponse.class) {
            UserResponse userResponse = (UserResponse)response;
            assertEquals(userResponse.getUserName(), loginRequest.getUserName());
            System.out.println("Proxy register test PersonID: " + userResponse.getPersonID() +
                    "   AuthToken: " + userResponse.getAuthToken());
            this.userResponse = userResponse;
        }
        else {
            MessageResponse messageResponse = (MessageResponse)response;
            System.out.println(messageResponse.getMessage());
        }
    }

    @Test
    public void getEvents() {
        Gson gson = new Gson();
        UserResponse userResponseEvents = (UserResponse)proxy.logIn(loginRequest);
        Response response = proxy.getEvents(userResponseEvents);
        if (response.getClass() == EventDataResponse.class) {
            EventDataResponse dataResponse = (EventDataResponse)response;
           // ArrayList<Event> eventArray = dataResponse.getData();
            System.out.println(gson.toJson(dataResponse));
        }
        else {
            MessageResponse messageResponse = (MessageResponse)response;
            System.out.println(messageResponse.getMessage());
        }
    }

    @Test
    public void getPeople() {
        Gson gson = new Gson();
        UserResponse userResponsePeople = (UserResponse)proxy.logIn(loginRequest);
        Response response = proxy.getPeople(userResponsePeople);
        if (response.getClass() == PersonDataResponse.class) {
            PersonDataResponse dataResponse = (PersonDataResponse)response;
            ArrayList<Person> peopleSuperModelArray = dataResponse.getData();
            System.out.println(gson.toJson(peopleSuperModelArray));
        }
        else {
            MessageResponse messageResponse = (MessageResponse)response;
            System.out.println(messageResponse.getMessage());
        }
    }
}