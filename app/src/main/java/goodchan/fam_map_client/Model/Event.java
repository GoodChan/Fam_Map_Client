package goodchan.fam_map_client.Model;

import android.support.annotation.NonNull;

import java.util.UUID;

public class Event extends SuperModel implements Comparable<Event> {
    private String eventID = "";
    private String descendant = "";
    private String personID = "";
    private String latitude = "0.0";
    private String longitude = "0.0";
    private String country = "";
    private String city = "";
    private String eventType = "";
    private String year = "";

    /**
     * Constructs the Event object.
     * @param eventID
     * @param descendant
     * @param person
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     */
    public Event(String eventID, String descendant, String person, String latitude, String longitude, String country, String city, String eventType, String year) {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = person;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event(String descendant, String person, String latitude, String longitude, String country, String city, String eventType, String year) {
        this.eventID = UUID.randomUUID().toString();
        this.descendant = descendant;
        this.personID = person;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPerson() {
        return personID;
    }

    public void setPerson(String person) {
        this.personID = person;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    private final String BIRTH = "birth";
    private final String DEATH = "death";
    //if instance is > pos num, if == 0 return 0, else negative number
    @Override
    public int compareTo(@NonNull Event event) {
        if (this.getEventType().toLowerCase().equals(BIRTH) && event.getEventType().toLowerCase().equals(BIRTH)) {
            return compareYear(event);
        }
        else if (this.getEventType().toLowerCase().equals(DEATH) && event.getEventType().toLowerCase().equals(DEATH)) {
            return compareYear(event);
        }
        else if (this.getEventType().toLowerCase().equals(BIRTH)) { //item is not both births or both deaths
            return -1;
        }
        else if (this.getEventType().toLowerCase().equals(DEATH)) {
            return 1;
        }
        else if (event.getEventType().toLowerCase().equals(BIRTH)) {
            return 1;
        }
        else if (event.getEventType().toLowerCase().equals(DEATH)) {
            return -1;
        }
        else {
            return compareYear(event);
        }
    }

    private int compareYear(Event event) {
        if (Integer.parseInt(this.getYear()) > Integer.parseInt(event.getYear())) {
            return 1;
        }
        else if (Integer.parseInt(this.getYear()) == Integer.parseInt(event.getYear())) {
            if (this.eventType.equals(event.getEventType())) {
                return 0;
            }
            return 1;
        }
        else {
            return -1;
        }
    }
}
