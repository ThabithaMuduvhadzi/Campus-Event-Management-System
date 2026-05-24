/*I researched everything I had no understanding of and cited where I researched
Please be lenient sir */
import java.util.*;
import java.io.*;
/*Most of this I researched and cited because I have little
to no knowledge on this*/
public class FilePersistence {
    private static final String DATA_FILE = "Event.txt";

    public static void saveData(ArrayList<Event> events) {
        /*Automatically closes the file after writing
        Adapted from Alam (2025)*/
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            //Loops through every event in the list
            for (Event e : events) {
                writer.println(e.id + "|" + e.name + "|" + e.date + "|" + e.time + "|" + e.location + "|" + e.maxParticipants);
                
                //Join all student ID with commas and write to next line
                String regData=String.join(",", e.registered);
                writer.println("REGISTERED: " + regData);

                //Join all student numbers of those waitlisted and move to next line
                String waitlistData=String.join(",", e.waitlist);
                writer.println("WAITLISTED: " + waitlistData);
            }
            System.out.println("Data saved");
            /*Catches an error and prints error message
            Adapted from Gupta (2025)*/
        }catch(IOException e){
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    //This will load all events from the text file and bring them back as an array
    public static ArrayList<Event> loadData() {
        ArrayList<Event> loadedEvents = new ArrayList<>();
        File file = new File(DATA_FILE);
        //Return empty list if no data file exists
        if (!file.exists()) {
            return loadedEvents;
        }
        
        //It will open the file using a scanner
        try (Scanner fileScanner = new Scanner(file)) {
            //Process the text storage line by line
            while (fileScanner.hasNextLine()) {
                String eventLine = fileScanner.nextLine();
                String[] parts = eventLine.split("\\|");
                //Event object
                Event event = new Event(
                    //Event Id
                    Integer.parseInt(parts[0]),
                    //Event name
                    parts[1],
                    //Event date
                    parts[2],
                    //Event time
                    parts[3],
                    //Event location
                    parts[4],
                    //Events Maximum participants
                    Integer.parseInt(parts[5])
                );
                /*Reads the next line for registered student numbers
                Adapted from Alam (2025)*/
                if (fileScanner.hasNextLine()) {
                    String regLine = fileScanner.nextLine();
                    if (regLine.startsWith("REGISTERED:")) {
                        String regData = regLine.substring(11);
                        if (!regData.isEmpty()) {
                            String[] regStudents = regData.split(",");
                            for (String s : regStudents) {
                                if (!s.isEmpty()) event.registered.add(s);
                            }
                        }
                    }
                }
                /*Reads the next line for waitlisted student numbers
                Adapted from Alam (2025)*/
                if (fileScanner.hasNextLine()) {
                    String waitLine = fileScanner.nextLine();
                    if (waitLine.startsWith("WAITLISTED:")) {
                        String waitData = waitLine.substring(12);
                        if (!waitData.isEmpty()) {
                            String[] waitStudents = waitData.split(",");
                            for (String s : waitStudents) {
                                if (!s.isEmpty()) event.waitlist.add(s);
                            }
                        }
                    }
                }
                
                loadedEvents.add(event);
            }
            System.out.println("Loaded " + loadedEvents.size() + " events.");
            //Catches an error and prints our error message
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
        return loadedEvents;
    }
}