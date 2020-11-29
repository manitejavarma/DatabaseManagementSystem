package DBMS;

public class DBMS {
    private String username;
    private String activeDatabase;
    private static DBMS instance;

    public static DBMS getInstance() {
        if (instance == null) {
            instance = new DBMS();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActiveDatabase() {
        return activeDatabase;
    }

    public void setActiveDatabase(String activeDatabase) {
        this.activeDatabase = activeDatabase;
    }
}
