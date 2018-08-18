package goodchan.fam_map_client.MainActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;
import goodchan.fam_map_client.Model.Data;
import goodchan.fam_map_client.FilterActivity.FilterActivity;
import goodchan.fam_map_client.Model.Event;
import goodchan.fam_map_client.Model.Person;
import goodchan.fam_map_client.PersonActivity.PersonActivity;
import goodchan.fam_map_client.R;
import goodchan.fam_map_client.SearchActivity.SearchActivity;
import goodchan.fam_map_client.SettingsActivity.SettingsActivity;

public class MainMapFragment extends Fragment implements OnMapReadyCallback {
    private SupportMapFragment mapContainer;
    private GoogleMap mMap;
    private TextView displayMarker;
    private ImageView underMapIcon;
    private ArrayList<Polyline> polyLineList = new ArrayList<>();
    private LinearLayout personActivityLinearLayout;
    private Person curPerson;
    private String personId;
    private Boolean isEventActivity = false;
    private String eventIdToFocus = "";
    private Event eventToFocus;
    private ArrayList<Marker> toDelete = new ArrayList<>();

    //get logger for debugging
    private static Logger logger;
    static {
        logger = Logger.getLogger("Client");
    }


    //general fragment setup
    public MainMapFragment() {
        // Required empty public constructor
    }

    public static MainMapFragment newInstance() {

        return new MainMapFragment();
    }

    public static MainMapFragment newInstance(String args) {
        Bundle arguments = new Bundle();
        arguments.putString("eventId", args);
        MainMapFragment mainMapFragment = new MainMapFragment();
        mainMapFragment.setArguments(arguments);
        return mainMapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //gets info on current event if coming from event fragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String args = arguments.getString("eventId");
            eventIdToFocus = args;
            eventToFocus = Data.eventIdToEvent.get(eventIdToFocus);
            if (eventToFocus == null) {
                logger.info("event to focus is null though there were arguments");
            }
            isEventActivity = true;
        }
        else {
            isEventActivity = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vGroup, Bundle bundle) {
        //does standard onCreateView things by inflating and calling super
        super.onCreateView(inflater, vGroup, bundle);

        View view = getLayoutInflater().inflate(R.layout.fragment_main_map_fragment, vGroup, false);
        displayMarker = view.findViewById(R.id.map_text_view);
        underMapIcon = view.findViewById(R.id.under_map_icon);
        setHasOptionsMenu(!isEventActivity);
        mapContainer = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapContainer.getMapAsync(this);

        personActivityLinearLayout = view.findViewById(R.id.person_activity_linear_layout);
        personActivityLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curPerson != null) {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("personId", curPerson.getPersonID());
                    startActivity(intent);
                }
            }
        });

        return view; //inflates object
    }

    @Override
    public void onResume() {
        super.onResume();
        mapContainer.onResume();

        if (mMap != null) {
            onMapReady(mMap);
        }
    }


    //menu methods, onCreate add setHasOptionsMenu(true)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fam_map_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_fam_map:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.filter_fam_map:
                Intent intent2 = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent2);
                return true;
            case R.id.setting_fam_map:
                Intent intent3 = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent3);
                return true;
            default:
                return false;
        }
    }

    //map methods
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //default marker
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        for (Marker m : toDelete) {
            m.remove();
        }
        toDelete.clear();

        for (Event e : Data.eventsList) {
            LatLng eLocation = new LatLng(Double.parseDouble(e.getLatitude()), Double.parseDouble(e.getLongitude()));
            MarkerOptions toAddMarker = new MarkerOptions().position(eLocation)
                    .snippet(e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")")
                    .title(Data.people.get(e.getPerson()).getFirstName() + " " + Data.people.get(e.getPerson()).getLastName())
                    .icon(BitmapDescriptorFactory.defaultMarker(Data.colors.get(e.getEventType())));
            //add marker returns a marker that was added back

            //sets up android bellow the map
            Marker marker = mMap.addMarker(toAddMarker);
            Data.markerToEvent.put(marker.getId(), e);
            Drawable androidIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).
                    colorRes(R.color.map_marker_icon).sizeDp(40);
            underMapIcon.setImageDrawable(androidIcon);
            toDelete.add(marker);

            if ((isEventActivity) && (e.getEventID() == eventIdToFocus)) {
                LatLng eventLatLong = new LatLng(Double.parseDouble(eventToFocus.getLatitude()),
                        Double.parseDouble(eventToFocus.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLatLong));

                curPerson = Data.people.get(e.getPerson());
                if (curPerson.getGender().equals("f")) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.female_icon).sizeDp(40);
                    underMapIcon.setImageDrawable(genderIcon);
                } else {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                    underMapIcon.setImageDrawable(genderIcon);
                }
                displayMarker.setText(toAddMarker.getTitle() + "\n" + toAddMarker.getSnippet());

                if (Data.spouseLines) {
                    setSpouseLines(curPerson, e);
                }
                if (Data.familyTreeLines) {
                    setFamilyTreeLines(curPerson, e, 12);
                }
                if (Data.lifeStoryLines) {
                    setLifeStoryLines(curPerson, e);
                }
            }

            //Bundle intentExtras = getIntent().getExtras();
            if (isEventActivity) {
                displayMarker.setText(e.getEventType() + ": " + e.getCity() + ", " + e.getCountry() + " (" + e.getYear() + ")");
            } else {
                displayMarker.setText("Click on a marker to see event details");
            }
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                removePolylines();

                Event event = Data.markerToEvent.get(marker.getId());
                curPerson = Data.people.get(event.getPerson());
                if (curPerson.getGender().equals("f")) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.female_icon).sizeDp(40);
                    underMapIcon.setImageDrawable(genderIcon);
                } else {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                    underMapIcon.setImageDrawable(genderIcon);
                }
                displayMarker.setText(marker.getTitle() + "\n" + marker.getSnippet());

                //draw lines
                if (Data.spouseLines) {
                    setSpouseLines(curPerson, event);
                }
                if (Data.familyTreeLines) {
                    setFamilyTreeLines(curPerson, event, 12);
                }
                if (Data.lifeStoryLines) {
                    setLifeStoryLines(curPerson, event);
                }
                return false;
            }
        });
    }

        private void removePolylines() {
            for (Polyline poly : polyLineList) {
                poly.remove();
            }
            polyLineList.clear();
        }

        private void setSpouseLines(Person person, Event event) {
            Event spouseBirth = Data.personIdToBirth.get(person.getSpouse());
            if (spouseBirth != null) {
                Polyline spouseLine = mMap.addPolyline(new PolylineOptions()
                        .clickable(false)
                        .add(
                                new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude())),
                                new LatLng(Double.parseDouble(spouseBirth.getLatitude()), Double.parseDouble(spouseBirth.getLongitude()))));
                logger.info("event lat = " + event.getLatitude());
                logger.info("spouse lat = " + spouseBirth.getLatitude());
                spouseLine.setColor(Data.spouseLine);
                polyLineList.add(spouseLine);
            }
        }

        private void setFamilyTreeLines(Person person, Event event, int strokeWidth) {
            if (strokeWidth < 0) {
                strokeWidth = 1;
            }

            Event motherEvent = null;
            if((person.getMother() != null) && (!person.getMother().equals(""))) {
                TreeSet<Event> lifeEvents = Data.events.get(person.getMother());
                if (lifeEvents != null) {
                    Iterator<Event> firstEvent = lifeEvents.iterator();
                    if (firstEvent.hasNext()) {
                        motherEvent = firstEvent.next();
                    }
                    Polyline motherLine = mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude())),
                                    new LatLng(Double.parseDouble(motherEvent.getLatitude()), Double.parseDouble(motherEvent.getLongitude())))
                            .color(Data.familyTreeLine)
                            .width(strokeWidth));
                    polyLineList.add(motherLine);
                    setFamilyTreeLines(Data.people.get(motherEvent.getPerson()), motherEvent, strokeWidth - 10);
                }
            }
            else {
                logger.info("mother not found");
            }
            Event fatherEvent = null;
            if (Data.personIdToBirth.get(person.getFather()) != null) {
                TreeSet<Event> lifeEvents = Data.events.get(person.getFather());
                if (lifeEvents != null) {
                    Iterator<Event> firstEvent = lifeEvents.iterator();
                    if (firstEvent.hasNext()) {
                        fatherEvent = firstEvent.next();
                    }
                    Polyline fatherLine = mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    new LatLng(Double.parseDouble(event.getLatitude()), Double.parseDouble(event.getLongitude())),
                                    new LatLng(Double.parseDouble(fatherEvent.getLatitude()), Double.parseDouble(fatherEvent.getLongitude())))
                            .color(Data.familyTreeLine)
                            .width(strokeWidth));
                    polyLineList.add(fatherLine);
                    setFamilyTreeLines(Data.people.get(fatherEvent.getPerson()), fatherEvent, strokeWidth - 10);
                }
            }
            else {
                logger.info("father not found");
            }
        }

        private void setLifeStoryLines(Person person, Event event) {
            TreeSet<Event> events = Data.events.get(person.getPersonID());
            if (events != null) {
                Event priorEvent = event;
                for (Event e : events) {
                    Polyline lifeLine = mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    new LatLng(Double.parseDouble(priorEvent.getLatitude()), Double.parseDouble(priorEvent.getLongitude())),
                                    new LatLng(Double.parseDouble(e.getLatitude()), Double.parseDouble(e.getLongitude())))
                            .color(Data.lifeLine));
                    logger.info("event lat = " + event.getLatitude());
                    logger.info("spouse lat = " + e.getLatitude());
                    polyLineList.add(lifeLine);
                    priorEvent = e;
                }
            }
            else {
                logger.info("events null");
            }
        }
    }

