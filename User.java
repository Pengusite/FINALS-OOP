public class User {
    private int userTutoredID;
    private String nameUser;
    private String roleUser;
    private String emailUser;

    //Constructor to create a user
    public User(int userTutoredID, String nameUser, String roleUser, String emailUser) {
        this.userTutoredID = userTutoredID;
        this.nameUser = nameUser;
        this.roleUser = roleUser;
        this.emailUser = emailUser;
    }

    //Display Info
    public void displayInfo() {
        System.out.println("User ID: " + userTutoredID + "\nUsername: " + nameUser + "\nRole: " + roleUser + "\nEmail: " + emailUser);
    }

}
