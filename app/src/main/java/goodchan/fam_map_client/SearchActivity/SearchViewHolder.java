package goodchan.fam_map_client.SearchActivity;

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
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.PersonActivity.PersonActivity;
import goodchan.fam_map_client.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView familyMemberName;
    TextView familyMemberRelation;
    ImageView familyIcon;
    Context context;
    String eventId;


    public SearchViewHolder(@NonNull View v, Event e, Context context) {
        super(v);

        this.context = context;
        eventId = e.getEventID();
        familyMemberName = v.findViewById(R.id.person_life_event);
        familyMemberRelation = v.findViewById(R.id.person_life_event_name);
        familyIcon = v.findViewById(R.id.person_life_event_icon);
        Person eventPerson = Data.people.get(e.getPerson());

        LinearLayout lifeEventClicked = itemView.findViewById(R.id.person_clickable);
        lifeEventClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                intent.putExtra("eventId", eventId);
                context.startActivity(intent);
            }
        });
        setIconEvent(context);

        setText((e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() +
                "( " + e.getYear() + ")"), (eventPerson.getFirstName() + " " + eventPerson.getLastName()));

    }

    private void setIconEvent(Context context) {
        Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_map_marker)
                .colorRes(R.color.colorPrimaryDark).sizeDp(40);
        familyIcon.setImageDrawable(eventIcon);
    }

    public SearchViewHolder(@NonNull View v, Person p, Context context) {
        super(v);

        familyMemberName = v.findViewById(R.id.person_life_event);
        familyMemberRelation = v.findViewById(R.id.person_life_event_name);
        familyIcon = v.findViewById(R.id.person_life_event_icon);
        final Context CONTEXT = context;
        final String PERSONID = p.getPersonID();

        LinearLayout familyClicked = itemView.findViewById(R.id.person_clickable);
        familyClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), PersonActivity.class);
                intent.putExtra("personId", PERSONID);
                context.startActivity(intent);
            }
        });
        setIconFamily(context);
        setText((p.getFirstName() + " " + p.getLastName()), "");
    }

    private void setIconFamily(Context context) {
        Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_female)
                .colorRes(R.color.colorPrimaryDark).sizeDp(40);
        familyIcon.setImageDrawable(eventIcon);
    }
//
//    public SearchViewHolder(@NonNull View v) {
//        super(itemView);
//    }
//
    private void setText(String firstText, String secondText) {
        familyMemberName.setText(firstText);
        familyMemberRelation.setText(secondText);
    }

    public void bind(Event e) {
        setIconEvent(context);
        Person eventPerson = Data.people.get(e.getPerson());
        final Event EVENT = e;

        LinearLayout lifeEventClicked = itemView.findViewById(R.id.person_clickable);
        lifeEventClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), EventActivity.class);
                intent.putExtra("eventId", EVENT.getEventID());
                context.startActivity(intent);
            }
        });

        setText((e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() +
                "( " + e.getYear() + ")"), (eventPerson.getFirstName() + " " + eventPerson.getLastName()));
    }

    public void bind(Person p) {
        setIconFamily(context);
        setText((p.getFirstName() + " " + p.getLastName()), "");
        LinearLayout familyClicked = itemView.findViewById(R.id.person_clickable);
        final Person PERSON = p;

        familyClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), PersonActivity.class);
                intent.putExtra("personId", PERSON.getPersonID());
                context.startActivity(intent);
            }
        });
        setIconFamily(context);
        setText((p.getFirstName() + " " + p.getLastName()), "");
    }
}
