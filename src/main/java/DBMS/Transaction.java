package DBMS;

import tech.tablesaw.api.Table;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class Transaction {
    String id;
    String user;
    NewCSVManager newCSVManager;
    final String transactionFilePath = "database/transactions.csv";

    Transaction(){
        newCSVManager = new NewCSVManager();
    }
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

    public  void copyTable(String databaseName,String tableName) throws IOException {
        String copyTableName = getCopyTableName(databaseName,tableName);
        Table table = Table.read().csv("database/"+databaseName+"/"+tableName+".csv");
        table.write().csv("database/"+databaseName+"/"+copyTableName+".csv");
    }

    public String getCopyTableName(String databaseName,String tableName) throws IOException {
        String copyTableName = DBMS.getInstance().getTransactionId()+"_"+tableName;
        return copyTableName;
    }



    public void insertTransactionLog(String tableName) throws IOException {
        HashMap<String,String> columnsAndValues = new HashMap<>();
        columnsAndValues.put("username",DBMS.getInstance().getUsername());
        columnsAndValues.put("transactionid",DBMS.getInstance().getTransactionId());
        columnsAndValues.put("table",tableName);
        columnsAndValues.put("transaction_state",TRANSACTION_STATE.PROCESS.name());
        columnsAndValues.put("timestamp", String.valueOf(LocalDate.now()));
        newCSVManager.insertCSV(transactionFilePath,columnsAndValues);
    }

    private void updateLog(String transactionState) throws IOException {
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("transactionid",DBMS.getInstance().getTransactionId());
        HashMap<String,String> set = new HashMap<>();
        set.put("transaction_state",transactionState);
        newCSVManager.updateCSV(transactionFilePath,conditions,set);
    }

    //Citation : https://www.codota.com/code/java/methods/java.io.File/renameTo
    private void replace(File destfile, File origfile) throws IOException {
        destfile.delete();
        if(!origfile.renameTo(destfile)){
            throw new IOException("Unable to rename file" + origfile.getName());
        }
    }

    public void commit(String database, String tableName){
        String copyTableName = DBMS.getInstance().getTransactionId()+"_"+tableName;
        try{
            File mainFile = new File("database/"+database+"/"+tableName+".csv");
            File copyFile = new File("database/"+database+"/"+copyTableName+".csv");

            replace(mainFile,copyFile);
            updateLog(TRANSACTION_STATE.COMMIT.name());

            DBMS dbms = DBMS.getInstance();
            Transaction transaction = new Transaction();
            transaction.generateTransactionId(dbms.getUsername());
            dbms.setTransactionId(transaction.getId());

            dbms.getTables().remove(tableName);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void rollback(String database, String tableName){
        String copyTableName = DBMS.getInstance().getTransactionId()+"_"+tableName;
        try{

            File copyFile = new File("database/"+database+"/"+copyTableName+".csv");
            copyFile.delete();
            updateLog(TRANSACTION_STATE.ROLLBACK.name());

            DBMS dbms = DBMS.getInstance();
            Transaction transaction = new Transaction();
            transaction.generateTransactionId(dbms.getUsername());
            dbms.setTransactionId(transaction.getId());
            dbms.getTables().remove(tableName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
