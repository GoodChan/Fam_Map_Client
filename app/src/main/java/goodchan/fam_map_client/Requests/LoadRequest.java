package goodchan.fam_map_client.Requests;

import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.Model.User;

public class LoadRequest {
    User users[];
    Person persons[];
    Event events[];

    /**
     * Constructs a /load response of users, persons and events.
     * @param users
     * @param persons
     * @param events
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}

