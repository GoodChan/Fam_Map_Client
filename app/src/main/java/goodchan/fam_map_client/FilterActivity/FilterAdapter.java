package goodchan.fam_map_client.FilterActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterViewHolder> {


    public FilterAdapter() {
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_filter_activity, viewGroup, false);

        return new FilterViewHolder(view, (String)Data.filterTypes.get(i).getFirst(), i);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder filterViewHolder, int i) {
        filterViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        //num events , then two mother and father, and two gender
        return Data.filterTypes.size();
    }
}