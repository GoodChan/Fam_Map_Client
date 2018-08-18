package goodchan.fam_map_client.PersonActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.Model.Pair;
import goodchan.fam_map_client.R;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyViewHolder> {
    public Context context;
    ArrayList<Pair> familyList = new ArrayList<>();
    public String personId;
    public int index = 0;

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_person, viewGroup, false);
        index = i;
        Person p = (Person)familyList.get(i).getFirst();
        return new FamilyViewHolder(view, familyList.get(i), context, i, p.getPersonID());
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder personLifeEventsViewHolder, int i) {
        personLifeEventsViewHolder.bind(familyList.get(i), i);
    }

    @Override
    public int getItemCount() {
        if (familyList == null) {
            return 0;
        }
        return familyList.size();
    }

    public void clear() {
        familyList = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void fill() {
        familyList = Data.familyConnections.get(personId);
        this.notifyDataSetChanged();
    }
}
