package goodchan.fam_map_client.SearchActivity;

import android.content.Context;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.Pair;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.PersonActivity.LifeEventsViewHolder;
import goodchan.fam_map_client.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    ArrayList<Event> events = Data.eventsList;
    Collection<Person> peopleCollection = Data.people.values();
    ArrayList<Person> people = new ArrayList<>(peopleCollection);

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_person, viewGroup, false);

        Integer index = null;
        if (i > events.size()) {
            index = i - events.size(); //adjust for second array
        }
        if (index == null) {
            return new SearchViewHolder(view, events.get(i), view.getContext());
        }
        else {
            return new SearchViewHolder(view, people.get(index), view.getContext());

        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        Integer index = null;
        if (i >= events.size()) {
            index = i - events.size(); //adjust for second array
        }
        if (index == null) {
            searchViewHolder.bind(events.get(i));
        }
        else {
            if (index < people.size()) {
                searchViewHolder.bind(people.get(index));
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.size() + people.size();
    }

    public void updateSearch(String currSearch) {
        events = Data.eventsList;
        peopleCollection = Data.people.values();
        people = new ArrayList<>(peopleCollection);

        ArrayList<Event> newEvents = new ArrayList<>();
        ArrayList<Person> newPeople = new ArrayList<>();
        // if search box is empty show not search results
        if (currSearch == null || currSearch.equals("")) {
            events = newEvents;
            people = newPeople;
            this.notifyDataSetChanged();
            return;
        }

        for (Event e : events) {
            if (e.getCity().toLowerCase().contains(currSearch.toLowerCase()) ||
                    e.getYear().contains(currSearch) ||
                    e.getEventType().toLowerCase().contains(currSearch.toLowerCase()) ||
                    e.getCountry().toLowerCase().contains(currSearch.toLowerCase())) {
                newEvents.add(e);
            }
        }

        for (Person p : people) {
            if (p.getFirstName().toLowerCase().contains(currSearch.toLowerCase()) ||
                    p.getLastName().toLowerCase().contains(currSearch.toLowerCase())) {
                newPeople.add(p);
            }
        }

        events = newEvents;
        people = newPeople;
        this.notifyDataSetChanged();
    }
}