package goodchan.fam_map_client.PersonActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.logging.Logger;
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.Pair;
import goodchan.fam_map_client.R;

public class LifeEventsAdapter extends RecyclerView.Adapter<LifeEventsViewHolder> {
    public Context context;
    ArrayList<Pair> lifeEvents = new ArrayList<>();
    public String personId;
    ArrayList<Event> events = new ArrayList<>();

    private static Logger logger;
    static {
        logger = Logger.getLogger("Client");
    }

    @NonNull
    @Override
    public LifeEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_person, viewGroup, false);

        return new LifeEventsViewHolder(view, lifeEvents.get(i), view.getContext(), i, events.get(i).getEventID());
    }

    @Override
    public void onBindViewHolder(@NonNull LifeEventsViewHolder personLifeEventsViewHolder, int i) {
        personLifeEventsViewHolder.bind(lifeEvents.get(i), i);
    }

    @Override
    public int getItemCount() {
        return lifeEvents.size();
    }

    public void clear() {
        logger.info("clear life events adapter");
        lifeEvents = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void fill() {
        String first = "";
        String second = "";
        for (Event e : Data.events.get(personId)) {
            events.add(e);
            first = (e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + "(" + e.getYear() + ")");
            second = (Data.people.get(personId).getFirstName() + " " + Data.people.get(personId).getLastName());
            lifeEvents.add(new Pair(first, second));
        }
        this.notifyDataSetChanged();
    }
}