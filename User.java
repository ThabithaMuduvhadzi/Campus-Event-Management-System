abstract class User {
    String userId;
    
    public User(String userId) {
        this.userId = userId;
    }
    
    public abstract void showMenu();
}

class StudentUser extends User {
    public StudentUser(String userId) {
        super(userId);
    }
    
    @Override
    public void showMenu() {
        System.out.println("STUDENT MENU ");
        System.out.println("1. View Events");
        System.out.println("2. Register For Event");
        System.out.println("3. Cancel Registration");
        System.out.println("4. View My Status");
        System.out.println("5. Search Events");
        System.out.println("6. Exit");
    }
}

class StaffUser extends User {
    public StaffUser(String userId) {
        super(userId);
    }
    
    @Override
    public void showMenu() {
        System.out.println("STAFF MENU ");
        System.out.println("1. Create Event");
        System.out.println("2. Update Event");
        System.out.println("3. Cancel Event");
        System.out.println("4. View All Events");
        System.out.println("5. Exit");
    }
}