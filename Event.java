import java.util.*;

public class Event {
    int id;
    String name;
    String date;
    String time;
    String location;
    int maxParticipants;
    ArrayList<String> registered;
    Queue<String> waitlist;
    //Constructoto intialize a new event 
    public Event(int id, String name, String date, String time, String location, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.location = location;
        this.maxParticipants = maxParticipants;
        //Empty because they will be tracking the registered and waitlisted students
        this.registered = new ArrayList<>();
        this.waitlist = new LinkedList<>();
    }
}