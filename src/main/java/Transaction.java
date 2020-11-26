import java.util.Random;

public class Transaction {
    String id;
    String user;

    public void generateTransactionId(String user){
        Random rand = new Random();
       this.id  = user + "_" + rand.nextInt(100000000);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
