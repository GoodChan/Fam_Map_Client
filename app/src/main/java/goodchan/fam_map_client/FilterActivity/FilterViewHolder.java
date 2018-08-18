package goodchan.fam_map_client.FilterActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.util.logging.Logger;

import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.R;

public class FilterViewHolder extends RecyclerView.ViewHolder {
    TextView titleTextView;
    TextView subTextView;
    Switch filterSwitch;
    public int index = 0;

    private static Logger logger;

    static {
        logger = Logger.getLogger("Client");
    }

    public FilterViewHolder(@NonNull View itemView, String title, int i) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.filter_title);
        subTextView = itemView.findViewById(R.id.filter_subtext);
        filterSwitch = itemView.findViewById(R.id.filter_switch);
        index = i;
        setText(title);
        filterSwitch.setChecked((Boolean)Data.filterTypes.get(index).getSecond());

        filterSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Boolean)Data.filterTypes.get(index).getSecond() == false) {
                    Data.filterTypes.get(index).setSecond(true);
                    logger.info("filter set to true");
                    Data.filter();
                }
                else {
                    Data.filterTypes.get(index).setSecond(false);
                    logger.info("filter set to false");
                    Data.filter();
                }
            }
        });
    }
    public void bind(int i) {
        index = i;
        filterSwitch.setChecked((Boolean)Data.filterTypes.get(index).getSecond());
        setText((String)Data.filterTypes.get(i).getFirst());
    }

    private void setText(String s) {
        titleTextView.setText(s);
        subTextView.setText("Filter by " + s);
    }
}
