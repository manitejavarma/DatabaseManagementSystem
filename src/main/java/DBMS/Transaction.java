package DBMS;

import tech.tablesaw.api.Table;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class Transaction {
    String id;
    String user;
    NewCSVManager newCSVManager;
    final String transactionFilePath = "E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv";

    public void generateTransactionId(String user){
        Random rand = new Random();
        this.id  = user + "_" + rand.nextInt(100000000);
        newCSVManager = new NewCSVManager();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void copyTable(String databaseName,String tableName) throws IOException {
        String copyTableName = id+"_"+tableName;
        Table table = Table.read().csv("database/"+databaseName+"/"+tableName+".csv");
        table.write().csv("database/"+databaseName+"/"+tableName+".csv");
    }

    public void insertTransactionLog(String tableName) throws IOException {
        String transactionFilePath = "E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv";
        HashMap<String,String> columnsAndValues = new HashMap<>();
        columnsAndValues.put("username",DBMS.getInstance().getUsername());
        columnsAndValues.put("transactionid",id);
        columnsAndValues.put("table",tableName);
        columnsAndValues.put("transaction_state",TRANSACTION_STATE.PROCESS.name());
        columnsAndValues.put("timestamp", String.valueOf(LocalDate.now()));
        newCSVManager.insertCSV(transactionFilePath,columnsAndValues);
    }

    private void updateLog(String transactionState) throws IOException {
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("transactionid",id);
        HashMap<String,String> set = new HashMap<>();
        set.put("transaction_state",transactionState);
        newCSVManager.updateCSV(transactionFilePath,conditions,set);
    }
}
