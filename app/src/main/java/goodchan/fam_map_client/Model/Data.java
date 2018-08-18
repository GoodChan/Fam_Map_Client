package goodchan.fam_map_client.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.logging.Logger;

import goodchan.fam_map_client.Responses.*;

public class Data {

    //private static final Data ourInstance = new Data();

    private static Logger logger;

    static {
        logger = Logger.getLogger("Client");
    }

    public static boolean loggedIn = false;
    public static String authToken = "";

    //personID to Events
    public static HashMap<String, TreeSet<Event>> events = new HashMap<>();
    public static HashMap<String, TreeSet<Event>> unFilteredEvents = new HashMap<>(); //filtered
    //personID to Person
    public static HashMap<String, Person> people = new HashMap<String, Person>();
    public static ArrayList<String> eventTypes = new ArrayList<String>();

    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static ArrayList<Event> unFilteredEventsList = new ArrayList<>(); //filtered

    //MarkerID to an event
    public static HashMap<String, Event> eventIdToEvent = new HashMap<>();
    public static HashMap<String, Event> markerToEvent = new HashMap<>();

    public static HashMap<String, ArrayList<Pair>> familyConnections = new HashMap<>();

    //eventType to color of line
    public static HashMap<String, Float> colors = new HashMap<>();
    public static boolean spouseLines = true;
    public static boolean familyTreeLines = true;
    public static boolean lifeStoryLines = true;
    public static HashMap<String, Event> personIdToBirth = new HashMap<>(); //TODO will filter screw this up
    public static Person userPerson;
    public static ArrayList<Pair> filterTypes = new ArrayList<>();

    public static Person rootPerson;
    public static ArrayList<Person> maternalPeople = new ArrayList<Person>();
    public static ArrayList<Person> paternalPeople = new ArrayList<Person>();
    public static HashMap<String, Person> findChild = new HashMap<String, Person>(); //personId to person(child). only one child
    private static ArrayList<String> alreadyExists = new ArrayList<>();

    public static int spouseLine = android.graphics.Color.BLUE;
    public static int familyTreeLine = android.graphics.Color.GREEN;
    public static int lifeLine = android.graphics.Color.RED;



    static class Color {
        static float color = 26;

        private static Float getColor() {
            Float toReturn = color;
            color = color + 36;
            if (color > 360) {
                color = 0;
            }
            return toReturn;
        }
    }

    public static void filter() {
        FilterThread filterThread = new FilterThread();
        filterThread.run();
    }

    public static class FilterThread extends Thread {

        public void run() {
            events = new HashMap<>();
            eventsList = new ArrayList<>();
            Boolean femaleFilter = false;
            Boolean maleFilter = false;
            Boolean motherSide = false;
            Boolean fatherSide = false;
            ArrayList<String> peopleToKeep = new ArrayList<>();

            ArrayList<String> toFilter = new ArrayList<>();

            //Mother's Side
            if ((Boolean)filterTypes.get(alreadyExists.size()).getSecond()) {
                motherSide = true;
                peopleToKeep.add(rootPerson.getPersonID());
                addFamily(rootPerson.getMother(), peopleToKeep);
            }
            //Father's Side
            if ((Boolean)filterTypes.get(alreadyExists.size() + 1).getSecond()) {
                fatherSide = true;
                peopleToKeep.add(rootPerson.getPersonID());
                addFamily(rootPerson.getFather(), peopleToKeep);
            }

            //Female Events
            if ((Boolean)filterTypes.get(alreadyExists.size() + 2).getSecond()) {
                femaleFilter = true;
            }
            //Male Events
            if ((Boolean)filterTypes.get(alreadyExists.size() + 3).getSecond()) {
                maleFilter = true;
            }
            if (femaleFilter || maleFilter || fatherSide || motherSide) {
                //filler to make sure they still enter the filter loops
                toFilter.add("                                          ");
            }

            for (int i = 0; i < alreadyExists.size(); ++i ) { //list of existing event filter types
                //get the bool for event filters. if true filter
               if ((boolean)filterTypes.get(i).getSecond()) {
                   String eventTypeToFilter = (String)filterTypes.get(i).getFirst();
                   toFilter.add(eventTypeToFilter.substring(0, (eventTypeToFilter.length() - 6)));
               }
            }
            Set<Map.Entry<String, TreeSet<Event>>> eventsEntryMap = unFilteredEvents.entrySet();
            for (Map.Entry<String, TreeSet<Event>> mapEntry : eventsEntryMap) {
                for (Event e : mapEntry.getValue()) {
                    addFilteredItems(toFilter, mapEntry, e, femaleFilter, maleFilter, motherSide, fatherSide, peopleToKeep);
                }
            }
            for(Event e : unFilteredEventsList) {
                boolean toAdd = true;
                for (int i = 0; i < toFilter.size(); ++i) {
                    if (e.getEventType().toLowerCase().equals(toFilter.get(i).toLowerCase())) {
                        logger.info("found filtered event from eventlist");
                        toAdd = false;
                    }
                    else if (femaleFilter || maleFilter) {
                        Person p = Data.people.get(e.getPerson());
                        if (femaleFilter && p.getGender().toLowerCase().equals("m")) {
                            toAdd = false;
                        }
                        if (maleFilter && p.getGender().toLowerCase().equals("f")) {
                            toAdd = false;
                        }
                    }
                    else if (motherSide || fatherSide) {
                        if(!peopleToKeep.contains(e.getPerson())) {
                            toAdd = false;
                        }
                    }
                    else {
                    }
                }
                if (toAdd) {
                    eventsList.add(e);
                }
            }
        }

        private static void addFilteredItems(ArrayList<String> toFilter, Map.Entry<String,
                TreeSet<Event>> mapEntry, Event e, Boolean femaleFilter, Boolean maleFilter, Boolean motherSide, Boolean fatherSide, ArrayList<String> peopleToKeep) {
            boolean toAdd = true;
            for (int i = 0; i < toFilter.size(); ++i) {
                if (e.getEventType().toLowerCase().equals(toFilter.get(i).toLowerCase())) {
                    logger.info("found filtered event");
                    toAdd = false;
                }
                else if (femaleFilter || maleFilter) {
                    Person p = Data.people.get(e.getPerson());
                    if (femaleFilter && p.getGender().toLowerCase().equals("m")) {
                        toAdd = false;
                    }
                    if (maleFilter && p.getGender().toLowerCase().equals("f")) {
                        toAdd = false;
                    }
                }
                else if (motherSide || fatherSide) {
                    if(!peopleToKeep.contains(e.getPerson())) {
                        toAdd = false;
                    }
                }
                else {
                }
            }
            if (toAdd) {
                if (events.containsKey(mapEntry.getKey())) {
                    events.get(mapEntry.getKey()).add(e);
                }
                else {
                    TreeSet<Event> temp = new TreeSet<>();
                    temp.add(e);
                    events.put(mapEntry.getKey(), temp);
                }
            }
        }

        private void addFamily(String personId, ArrayList<String> peopleToKeep) {
            if (Data.people.containsKey(personId)) {
                peopleToKeep.add(personId);
                Person currPerson = Data.people.get(personId);
                String mother = currPerson.getMother();
                String father = currPerson.getFather();
                if ((mother != null) && (!mother.equals(""))) {
                    addFamily(mother, peopleToKeep);
                }
                if ((father != null) && (!father.equals(""))) {
                    addFamily(father, peopleToKeep);
                }
            }
        }

    }

    public static class EventThread extends Thread {
        private ArrayList<Event> eventsArray;

        public EventThread(ArrayList<Event> eventsArray) {
            this.eventsArray = eventsArray;
        }

        public void run() {
            for (Event e : eventsArray) {
                TreeSet<Event> tempArray = new TreeSet<>();
                unFilteredEventsList.add(e);
                eventsList.add(e);
                tempArray.add(e);
                eventIdToEvent.put(e.getEventID(), e);

                setColors(e);

                // adds to a map of personID to birth events
                if (e.getEventType().toLowerCase().equals("birth")) {
                    personIdToBirth.put(e.getPerson(), e);
                }
                logger.info("personIdToBirth size = " + personIdToBirth.size());

                //adds tp events map
                if (events.containsKey(e.getPerson())) {
                    events.get(e.getPerson()).add(e);
                } else {
                    events.put(e.getPerson(), tempArray);
                }
            }
            setFilterArray();
        }
    }

    public static void extractUserDataToRAM(UserResponse userResponse, ArrayList<Event> eventsArray, ArrayList<Person> peopleArray) {
        EventThread eventThread = new EventThread(eventsArray);
        eventThread.run();

        //adds to a personId to person map
        for (Person p : peopleArray) {
            people.put(p.getPersonID(), p);
        }

        //connects families
        for (Person p : peopleArray) {
            String mother = p.getMother();
            //adds child to mother's array
            checkAndAdd(mother, p.getPersonID(), "Child");
            //adds mother to child's array
            checkAndAdd(p.getPersonID(), mother, "Mother");

            String father = p.getFather();
            //adds child to father's array
            checkAndAdd(father, p.getPersonID(), "Child");
            //adds father to child's array
            checkAndAdd(p.getPersonID(), father, "Father");

            checkAndAdd(p.getSpouse(), p.getPersonID(), "Spouse");
        }

        loggedIn = true;
        authToken = userResponse.getAuthToken();
        userPerson = people.get(userResponse.getPersonID());
        unFilteredEvents = events;
        rootPerson = people.get(userResponse.getPersonID());
    }

    private static void checkAndAdd(String personIdArray, String personIdToAdd, String relationship) {
        Pair toAdd = new Pair(people.get(personIdToAdd), relationship);
        if (personIdToAdd == "") {
            return;
        }
        if (familyConnections.containsKey(personIdArray)) {
            familyConnections.get(personIdArray).add(toAdd);
        }
        else {
            ArrayList<Pair> tempArray = new ArrayList<>();
            tempArray.add(toAdd);
            familyConnections.put(personIdArray, tempArray);
        }
    }

    private static void setColors(Event e) {
        if (colors.containsKey(e.getEventType())) {
        }
        else {
            colors.put(e.getEventType(), new Color().getColor());
            eventTypes.add(e.getEventType());
        }
    }

    private static void setFilterArray() {
        alreadyExists = new ArrayList<>();
        for (int i = 0; i < eventTypes.size(); ++i ) {
            String toAdd = eventTypes.get(i);
            if (!alreadyExists.contains(toAdd.toLowerCase())) {
                alreadyExists.add(toAdd.toLowerCase());
                toAdd = toAdd.substring(0, 1).toUpperCase() + toAdd.substring(1);
                toAdd = (toAdd + " Event");
                filterTypes.add(new Pair(toAdd, false));
            }
        }
        // mother and father's side and female male events
        filterTypes.add(new Pair("Mother's Side", false));
        filterTypes.add(new Pair("Father's Side", false));
        filterTypes.add(new Pair("Female Events", false));
        filterTypes.add(new Pair("Male Events",false));
    }
} //lines not food color, default blue
