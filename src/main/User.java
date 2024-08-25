package main;
public class User {
    private String username;
    private String displayName;
    private String avatar;
    private int id;

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getID() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setID(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return username + " " + "(" + displayName + ")";
    }
}
