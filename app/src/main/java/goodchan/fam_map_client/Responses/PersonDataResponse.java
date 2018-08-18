package goodchan.fam_map_client.Responses;

import java.util.ArrayList;

import goodchan.fam_map_client.Model.Person;

public class PersonDataResponse extends Response {
    private ArrayList<Person> data;

    /**
     * Constructs the array associated with the data message. Used in /person and /event.
     * @param data
     */
    public PersonDataResponse(ArrayList<Person> data) {
        this.data = data;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }
}
