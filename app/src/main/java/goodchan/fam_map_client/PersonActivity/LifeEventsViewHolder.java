package goodchan.fam_map_client.PersonActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import goodchan.fam_map_client.EventActivity.EventActivity;
import goodchan.fam_map_client.Model.Pair;
import goodchan.fam_map_client.R;


public class LifeEventsViewHolder extends RecyclerView.ViewHolder {
    TextView lifeEvent;
    TextView lifeEventName;
    ImageView lifeEventIcon;
    private int index;

    public LifeEventsViewHolder(@NonNull View itemView, Pair lifeEventPair, Context context, int i, String eventId) {
        super(itemView);
        final Context CONTEXT = context;
        final String EVENTID = eventId;
        lifeEvent = itemView.findViewById(R.id.person_life_event);
        lifeEventName = itemView.findViewById(R.id.person_life_event_name);
        lifeEventIcon = itemView.findViewById(R.id.person_life_event_icon);
        index = i;

        LinearLayout lifeEventClicked = itemView.findViewById(R.id.person_clickable);
        lifeEventClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                intent.putExtra("eventId", EVENTID);
                context.startActivity(intent);
            }
        });

        Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker)
                .colorRes(R.color.colorPrimaryDark).sizeDp(40);
        lifeEventIcon.setImageDrawable(eventIcon);

        setText(lifeEventPair);
    }

    private void setText(Pair lifeEventPair) {
        lifeEvent.setText((String)lifeEventPair.getFirst());
        lifeEventName.setText((String)lifeEventPair.getSecond());
    }

    public void bind(Pair toBind, int i) {
        index = i;
        setText(toBind);
    }
}
