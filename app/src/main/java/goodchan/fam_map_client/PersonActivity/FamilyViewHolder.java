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
import java.util.logging.Logger;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.Model.Pair;
import goodchan.fam_map_client.R;

public class FamilyViewHolder extends RecyclerView.ViewHolder {
    TextView familyMemberName;
    TextView familyMemberRelation;
    ImageView familyIcon;
    private int index;

    private static Logger logger;
    static {
        logger = Logger.getLogger("Client");
    }

    public FamilyViewHolder(@NonNull View itemView, Pair lifeEventPair, Context context, int i, String personId) {
        super(itemView);

        familyMemberName = itemView.findViewById(R.id.person_life_event);
        familyMemberRelation = itemView.findViewById(R.id.person_life_event_name);
        familyIcon = itemView.findViewById(R.id.person_life_event_icon);
        index = i;
        final Context CONTEXT = context;
        final String PERSONID = personId;

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

        Drawable eventIcon = new IconDrawable(context, FontAwesomeIcons.fa_female)
                .colorRes(R.color.colorPrimaryDark).sizeDp(40);
        familyIcon.setImageDrawable(eventIcon);

        setText(lifeEventPair);
    }

    private void setText(Pair lifeEventPair) {
        Person p = (Person)lifeEventPair.getFirst();
        if (p != null) {
            logger.info("in family view holder set Text. p = " + p.getFirstName());
            String name = p.getFirstName() + " " + p.getLastName();
            familyMemberName.setText(name);
        }
        familyMemberRelation.setText((String) lifeEventPair.getSecond());
    }

    public void bind(Pair toBind, int i) {
        index = i;
        setText(toBind);
    }
}
