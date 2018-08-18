package goodchan.fam_map_client.Responses;

import java.util.ArrayList;

import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.SuperModel;

public class EventDataResponse extends Response {
    private ArrayList<Event> data;

    /**
     * Constructs the array associated with the data message. Used in /person and /event.
     * @param data
     */
    public EventDataResponse(ArrayList<Event> data) {
        this.data = data;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }
}
