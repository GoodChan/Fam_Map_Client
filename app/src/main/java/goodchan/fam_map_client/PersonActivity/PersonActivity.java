package goodchan.fam_map_client.PersonActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import java.util.logging.Logger;

import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.R;

public class PersonActivity extends AppCompatActivity {
    TextView usersName;
    TextView userGender;
    TextView lifeEventsToggle;
    TextView familyToggle;
    Boolean showLifeEvents = false;
    Boolean showFamily = false;
    Person curPerson = new Person("", "Error",
            "", "", "", "", "");

    private RecyclerView lifeEventsRecyclerView;
    private RecyclerView familyRecyclerView;
    LifeEventsAdapter lifeEventsAdapter = new LifeEventsAdapter();
    FamilyAdapter familyAdapter = new FamilyAdapter();

    private static Logger logger;
    static {
        logger = Logger.getLogger("Client");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.info("On create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        String personId = "";

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras != null) {
            personId = (String)intentExtras.get("personId");
            curPerson = Data.people.get(personId);
        }

        usersName = findViewById(R.id.person_users_name);
        userGender = findViewById(R.id.person_gender);
        lifeEventsToggle = findViewById(R.id.person_life_events_toggle);
        familyToggle = findViewById(R.id.person_family_toggle);
        lifeEventsRecyclerView = findViewById(R.id.person_life_events_recycler_view);
        familyRecyclerView = findViewById(R.id.person_family_recycler_view);

        logger.info("before set text");
        usersName.setText(curPerson.getFirstName() + " " + curPerson.getLastName());
        String gender = curPerson.getGender();
        if (gender.toLowerCase().equals("f")) {
            gender = "Female";
        }
        else if (gender.toLowerCase().equals("m")) {
            gender = "Male";
        }
        userGender.setText(gender);

        //Set up recycler views
        RecyclerView.LayoutManager layoutManagerFamily =
                new LinearLayoutManager(this);
        familyRecyclerView.setLayoutManager(layoutManagerFamily);

        lifeEventsRecyclerView.setHasFixedSize(false);
        lifeEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        lifeEventsAdapter.context = this;
        lifeEventsAdapter.personId = personId;
        lifeEventsRecyclerView.setAdapter(lifeEventsAdapter);

        lifeEventsToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info(
                    "onClick of life Event's toggle showLifeEvents = " + showLifeEvents);
                if (showLifeEvents) {
                    showLifeEvents = false;
                    lifeEventsAdapter.clear();
                }
                else {
                    showLifeEvents = true;
                    lifeEventsAdapter.fill();
                }
            }
        });

        familyRecyclerView.setHasFixedSize(false);
        familyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        familyAdapter.context = this;
        familyAdapter.personId = personId;
        familyRecyclerView.setAdapter(familyAdapter);

        familyToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logger.info(
                        "onClick of life Event's toggle showFamily = " + showFamily);
                if (showFamily) {
                    showFamily = false;
                    familyAdapter.clear();
                }
                else {
                    showFamily = true;
                    familyAdapter.fill();
                }
            }
        });
    }
}
