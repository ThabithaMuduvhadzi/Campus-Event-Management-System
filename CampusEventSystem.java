//I cited lines where I researched a lot because I had little knowledge on it
import java.util.*;
public class CampusEventSystem {
    static Scanner input = new Scanner(System.in);
    //An ArrayList that will store all campus event
    static ArrayList<Event> events = new ArrayList<>();
    
    public static void main(String[] args) {
        //Load existing data from file 
        events = FilePersistence.loadData();
        
        while (true) {
            System.out.println("Welcome to Richfield Campus Event Management System!");
            System.out.println("Select your role: ");
            System.out.println("1. Student");
            System.out.println("2. Staff");
            
            int choice = getValidIntInput();
    
            if (choice == 1) {
                studentMenu();
            } else if (choice == 2) {
                staffMenu();
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
    
    //This will validate that the user enters an actual number
    static int getValidIntInput() {
        while (true) {
            try {
                int num = input.nextInt(); 
                input.nextLine();
                return num;
                /*Catches an error
                Adapted from Gupta (2025)*/
            }catch (InputMismatchException e) {
                System.out.print("Invalid input please enter a number: ");
            }
        }
    }

    // Used to validate date format
    static boolean isValidDate(String date) {
        if (date == null) 
            return false;
        /*Date length has to have 10 characters dd/mm/yyyy including the hash symbols
        and has to have a hash sysmbol and index 2 and index 5 if not then thats an incorrect date format */
        if (date.length() != 10 || date.charAt(2) != '/' || date.charAt(5) != '/') 
            return false;
        try {
            //This will split into day, month and year
            String[] parts = date.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);

            if (year < 2026 || year > 2100) 
                return false;
            if (month < 1 || month > 12) 
                return false;
            if (day < 1 || day > 31) 
                return false;
            
            return true;
            /*Catches an error
            Adapted from Gupta (2025)*/
        }catch (NumberFormatException e) {
            return false; 
        }
    }
    //Used to validate time format
    static boolean isValidTime(String time) {
        if (time == null) 
            return false;
        /*Time length has to have 5 characters HH:mm including the the : sysmbol 
        and it also has to have a : sysmbol and index 2 if not then thats an incorrect time format */
        if (time.length() != 5 || time.charAt(2) != ':') 
            return false;

        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
    
            return hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;
           /*Catches an error
           Adapted from Gupta (2025)*/
        }catch (NumberFormatException e) {
            return false;
        }
    }

    // It will verify that a string contains characters
    static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    //STUDENT MENU   
    static void studentMenu() {
        while (true) {
            System.out.println("STUDENT MENU ");
            System.out.println("1. View Events");
            System.out.println("2. Register For Event");
            System.out.println("3. Cancel Registration");
            System.out.println("4. View My Status (Registered/Waitlisted)");
            System.out.println("5. Search Events");
            System.out.println("6. Exit");
        
            int choice = getValidIntInput();

            if (choice == 1) {
                viewAllEvents();
            } else if (choice == 2) {
                registerForEvent();
            } else if (choice == 3) {
                cancelRegistration();
            } else if (choice == 4) {
                viewStatus();
            } else if (choice == 5) {
                searchEvents();
            } else if (choice == 6) {
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
    }

    //REGISTER FOR EVENT   
    static void registerForEvent() {
        if (events.isEmpty()) {
            System.out.println("No events available");
            return;
        }
        
        System.out.print("Enter Student Number: ");
        String studentId = input.nextLine();
        if (!isNotEmpty(studentId)) {
            System.out.println("Student number cannot be empty");
            return;
        }
        
        System.out.print("Event ID: ");
        int eventID = getValidIntInput();
        //Find event by Id
        Event event = findEventById(eventID);
        if (event == null) {
            System.out.println("Event not found");
            return;
        }
        //If user is already registered it will exit   
        if (event.registered.contains(studentId)) {
            System.out.println("You are already registered for this event");
            return;
        }
        //If user is already waitlisted it will exit   
        if (event.waitlist.contains(studentId)) {
            System.out.println("You are already on the waitlist for this event");
            return;
        }
        //Adds theres still space student will be registered successfully
        if (event.registered.size() < event.maxParticipants) {
            event.registered.add(studentId);
            System.out.println("Registration successful");
            //If not they will be notified
        } else {
            event.waitlist.add(studentId);
            System.out.println("Event is full! You have been added to the waitlist");
        }
        //Save changes to find
        FilePersistence.saveData(events);
    }

    //CANCEL REGISTRATION    
    static void cancelRegistration() {
        if (events.isEmpty()) {
            System.out.println("No events found");
            return;
        }
        
        System.out.print("Enter Student Number: ");
        String studentId = input.nextLine();
        if (!isNotEmpty(studentId)) {
            System.out.println("Student number cannot be empty");
            return;
        }
        
        System.out.print("Event ID: ");
        int eventID = getValidIntInput();
        
        Event event = findEventById(eventID);
        if (event == null) {
            System.out.println("Event not found");
            return;
        }
        
        boolean found = false;
        
        if (event.registered.remove(studentId)) {
            found = true;
            System.out.println("Registration cancelled");
                
            if (!event.waitlist.isEmpty()) {
                //Adapted from Molwana (2025)
                new Thread(() ->{
                String promotedStudent = event.waitlist.poll();
                event.registered.add(promotedStudent);
                System.out.println("Student " + promotedStudent + " has been promoted ");
            }).start();
        }
    }
    //If not found in registered remove them from waitlist
    if (!found && event.waitlist.remove(studentId)){
        found = true;
        System.out.println("Removed from waitlist");
    }
        
    if (!found) {
        System.out.println("You are not registered or waitlisted for this event");
    } else {
            FilePersistence.saveData(events);
    }
}

//VIEW STATUS  
static void viewStatus() {
    System.out.print("Enter Student Number: ");
    String studentId = input.nextLine();
    if (!isNotEmpty(studentId)) {
        System.out.println("Student number cannot be empty");
        return;
    }
    //Flags to check if we find student in registered or waitlisted
    boolean registeredFound = false;
    boolean waitlistedFound = false;
    //Look through all saved events
    for (Event e : events) {
        //Check if student is registered for that specific event
        if (e.registered.contains(studentId)) {
            System.out.println("Registered for: " + e.name + " (ID: " + e.id + ")");
            registeredFound = true;
        } 

        //Check is student is waitlisted for a specific event
        if (e.waitlist.contains(studentId)) {
            //Calculate their place in the waitlist queue
            int position = new ArrayList<>(e.waitlist).indexOf(studentId) + 1;                
            System.out.println("Waitlisted for: " + e.name + " (ID: " + e.id + ") at position " + position);
            waitlistedFound = true;
        }
    }
        
    if (!registeredFound && !waitlistedFound) {
        System.out.println("You are not registered or waitlisted for any events.");
    }
}

//STAFF MENU   
static void staffMenu() {
    while (true) {
        System.out.println("STAFF MENU");
        System.out.println("1. Create Event");
        System.out.println("2. Update Event ");
        System.out.println("3. Cancel Event");
        System.out.println("4. View All Events");
        System.out.println("5. Exit");
            
        int choice = getValidIntInput();
            
        if (choice == 1) {
            createEvent();
        } else if (choice == 2) {
            updateEvent();
        } else if (choice == 3) {
            cancelEvent();
        } else if (choice == 4) {
            viewAllEvents();
        } else if (choice == 5) {
            break;
        } else {
            System.out.println("Invalid choice");
        }
    }
}

//CREATING AN EVENT BY STAFF    
static void createEvent() {
    System.out.print("Event ID: ");
    int id = getValidIntInput();
        
    // Prevention of event ID duplication
    if (findEventById(id) != null) {
        System.out.println("Event ID already exists");
        return;
    }

    String name;
    while (true) {
        System.out.println("Event Name: ");
        name = input.nextLine();
        //Validate the non empty event name
        if (isNotEmpty(name)) {
            break;
        } else {
            System.out.println("Event name cant be empty");
        }
    }

    String date;
    while (true) {
        System.out.println("Event Date (dd/mm/yyyy): ");
        date = input.nextLine();
        //Validate the non empty event date
        if (!isNotEmpty(date)) {
            System.out.println("Date can't be empty");
            continue;
        }
        //Validate the date format
        if (isValidDate(date)) {
            break;
        } else {
            System.out.println("Invalid date format. Please use dd/mm/yyyy format");
        }
    }

    String time;
    while (true) {
        System.out.println("Event Time (HH:mm): ");
        time = input.nextLine();
        //Validate the non empty event time
        if (!isNotEmpty(time)) {
            System.out.println("Time can't be empty");
            continue;
        }
        //Validates thetime format 
        if (isValidTime(time)) {
            break;
        } else {
            System.out.println("Invalid time format. Please use HH:mm format");
        }
    }
    System.out.println("Location: ");
    String location = input.nextLine();
    //Validate the non empty event location
    if (!isNotEmpty(location)) {
        System.out.println("Location can't be empty");
        return;
    }

    System.out.println("Maximum Participants: ");
    int max = getValidIntInput();
    //Validates that the number of maximum participants is a positive number
    if (max <= 0) {
        System.out.println("Maximum participants must be positive");
        return;
    }
        
    //Create new event then save the details
    events.add(new Event(id, name, date, time, location, max));
    FilePersistence.saveData(events);
    System.out.println("Event created successfully");
}

//UPDATING EVENT DETAILS  
static void updateEvent() {
    if (events.isEmpty()) {
        System.out.println("No events to update.");
        return;
    }
    
    System.out.println("Event ID: ");
    int id = getValidIntInput();
        
    Event event = findEventById(id);
    if (event == null) {
        System.out.println("Event not found");
        return;
    }
    //Asking for events new name from user
    System.out.println("New Name (leave blank to skip '" + event.name + "'): ");
    String newName = input.nextLine();
    //If condition is met then event gets new name
    if (isNotEmpty(newName)) 
        event.name = newName;
        
    //Asking for events ne time from userr 
    System.out.println("New Time (leave blank to skip '" + event.time + "'): ");
    String newTime = input.nextLine();
    //If conditions are met then event gets the new time
    if (isNotEmpty(newTime) && isValidTime(newTime)) 
        event.time = newTime;
        
    //Asking for events new location from user
    System.out.println("New Location (leave blank to skip '" + event.location + "'): ");
    String newLocation = input.nextLine();
    //If condition is met then event gets the new location
    if (isNotEmpty(newLocation)) 
        event.location = newLocation;
        
    FilePersistence.saveData(events);
    System.out.println("Event updated successfully");
}
    
//CANCEL EVENT   
static void cancelEvent() {
    if (events.isEmpty()) {
        System.out.println("No events to cancel");
        return;
    }
    
    System.out.print("Event ID: ");
    int id = getValidIntInput();
        
    //Find the Event based on the ID
    Event event = findEventById(id);
    if (event == null) {
        System.out.println("Event not found");
        return;
    }
    //Removes an event from the saved events then saves the changes
    events.remove(event);
    FilePersistence.saveData(events);
    System.out.println("Event cancelled successfully");
}

//VIEW ALL EVENTS  
static void viewAllEvents() {
    if (events.isEmpty()) {
        System.out.println("No events found");
        return;
    }
    
    System.out.println("Sort by: 1. Name  2. Date 3. None ");
        
    int sortChoice = getValidIntInput();
    ArrayList<Event> sortedEvents = new ArrayList<>(events);
    
    if (sortChoice == 1) {
        /*Sort by name alphabetically
        Adapted from Irfan (2024)*/
        sortedEvents.sort(Comparator.comparing(e -> e.name));
        /*Sort by date chronologically
        Adapted from Irfan (2024)*/
    } else if (sortChoice == 2) {
        sortedEvents.sort(Comparator.comparing(e -> e.date));
    } else {
        System.out.println("No sorting");
    }

    for (Event e : sortedEvents) {
        System.out.println("ID: " + e.id + " | Name: " + e.name + " | Date: " + e.date + " | Time: " + e.time + " | Location: " + e.location + " | Registered: " + e.registered.size() + "/" + e.maxParticipants + " | Waitlist: " + e.waitlist.size());
    }
}

//SEARCH FOR EVENT  
static void searchEvents() {
    if (events.isEmpty()) {
        System.out.println("No events found.");
        return;
    }
        
    System.out.println("1. Search by Name (partial or full match)");
    System.out.println("2. Search by Date (dd/mm/yyyy)");
        
    int choice = getValidIntInput();
    //Arraylist to hold results 
    ArrayList<Event> results = new ArrayList<>();

    if (choice == 1) {
        System.out.print("Enter Event Name (or partial name): ");
        //Convert to lowercase meaning that if user searches event in uppercase it will be converted to lowercase
        String searchName = input.nextLine().toLowerCase();
        //This ensures that input cant be empty
        if (!isNotEmpty(searchName)) {
            System.out.println("Search name cannot be empty");
            return;
        }
        for (Event e : events) {
            //checks for partial matches to event name
            if (e.name.toLowerCase().contains(searchName)) {
                results.add(e);
            }
        }
    
    }else if (choice == 2) {
        System.out.print("Enter Event Date (dd/mm/yyyy): ");
        String searchDate = input.nextLine();
            
        if (!isNotEmpty(searchDate)) {
            System.out.println("Search date cannot be empty");
            return;
        }
        for (Event e : events) {
            if (e.date.equals(searchDate)) {
                results.add(e);
            }
        }
    }else{
        System.out.println("Invalid choice");
        return;
    }

    if(results.isEmpty()) {
        System.out.println("No matching events found");
    }else {
        System.out.println("Search Results ");
        for (Event e : results) {
            System.out.println("ID: " + e.id + " | Name: " + e.name + " | Date: " + e.date + " | Time: " + e.time + " | Location: " + e.location + " | Registered: " + e.registered.size() + "/" + e.maxParticipants);
        }
    }
}
// A method that will help us find an event by its ID
static Event findEventById(int id) {
    for (Event e : events){
        if (e.id == id){
            return e;
        }
    }
    return null;
}
}
