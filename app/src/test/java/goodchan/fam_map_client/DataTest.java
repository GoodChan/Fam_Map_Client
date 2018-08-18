package goodchan.fam_map_client;

import org.junit.Test;

import java.util.ArrayList;

import goodchan.fam_map_client.Model.*;
import goodchan.fam_map_client.Responses.*;

import goodchan.fam_map_client.Model.Data;

public class DataTest {

    @Test
    private void extractUserDataToRAM() {
        UserResponse userResponse = new UserResponse("b3bfcf2f-8505-448d-8187-7026821c808e",
                "clientUser", "61df201b-796d-4c46-942f-567eaefd47b4");
        Event event = new Event("ed0d89ce-a3c4-495e-9aa0-08c389b773ae", "clientUser",
                "988b0c0c-1831-47fa-bc97-67cc21d27918", "76.4167", "-81.1",
                "Canada", "Grise Fiord", "marriage", "1914");
        Event event2 = new Event("97bd182b-742c-477b-8e66-58ab3f2ccf6a", "clientUser",
                "3180e2d1-a2ce-4715-b1d8-de41b01f9828", "76.4167", "-81.1",
                "Canada", "Grise Fiord", "birth", "1892");
        ArrayList<Event> events = new ArrayList<Event>();
        events.add(event);
        events.add(event2);

        Person person = new Person("94433aef-b8bf-45df-8279-a38af87a0cca", "clientUser",
                "Kermit", "Mccawley", "m", "",
                "", "06eacaf4-8d54-45ae-b64a-a0a6ccc25782");
        Person person2 = new Person("df20a8f4-8971-428b-903f-8c9eebc3842d", "clientUser",
                "firstanme", "lastname", "f", "fa5d7550-f451-4624-ba49-0ddf27755f8b",
                "0db59a86-22b3-4f39-84e4-721f044efd0c", "");

        ArrayList<Person> people = new ArrayList<Person>();
        people.add(person);
        people.add(person2);

        Data.extractUserDataToRAM(userResponse, events, people);
    }
}
