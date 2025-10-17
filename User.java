/**
 * Superclass representing a generic user (student or tutor).
 */
public class User {
    private int userTutoredID;
    private String nameUser;
    private String roleUser;
    private String emailUser;

    public User(int userTutoredID, String nameUser, String roleUser, String emailUser) {
        this.userTutoredID = userTutoredID;
        this.nameUser = nameUser;
        this.roleUser = roleUser;
        this.emailUser = emailUser;
    }

    public int getUserTutoredID() {
        return userTutoredID;
    }

    public void setUserTutoredID(int userTutoredID) {
        this.userTutoredID = userTutoredID;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public void displayInfo() {
        System.out.println("ID: " + userTutoredID + ", Name: " + nameUser + ", Role: " + roleUser + ", Email: " + emailUser);
    }
}
