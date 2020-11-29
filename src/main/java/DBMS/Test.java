package DBMS;

import tech.tablesaw.api.Table;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws IOException {



        NewCSVManager newCSVManager = new NewCSVManager();

        String username = "mani";//DBMS.ReadInput.readInput("Please enter username");
        String password = "pass";//DBMS.ReadInput.readInput("Please enter password");

        Transaction transaction = new Transaction();
        transaction.generateTransactionId(username);

        SQLToCSV sqlToCSV = new SQLToCSV();


        //Get all data from table
        CreateTableDump(newCSVManager);


        String tableName = "temp";
        if(checkIfAnyTransactionUsingSametable(tableName)){
            System.out.println("there is another transaction using the same table. please try again later. rn doing it :P ");
        }


        //create copy of file

        String copyTableName = transaction.getId()+"_"+tableName;
        Table table = Table.read().csv("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+tableName+".csv");
        table.write().csv("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");
        sqlToCSV.insertQuery(copyTableName);


        //update log
        HashMap<String,String> columnsAndValues = new HashMap<>();
        columnsAndValues.put("username",username);
        columnsAndValues.put("transactionid",transaction.getId());
        columnsAndValues.put("table",tableName);
        columnsAndValues.put("transaction_state",TRANSACTION_STATE.PROCESS.name());
        columnsAndValues.put("timestamp", String.valueOf(LocalDate.now()));
        newCSVManager.insertCSV("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv",columnsAndValues);

        String stmt = "commit";//DBMS.ReadInput.readInput("Commit or rollback");
        if(stmt.equals("commit")){
            try{
                File mainFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+tableName+".csv");
                File copyFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");

                replace(mainFile,copyFile);
                updateLog("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv",newCSVManager,transaction,TRANSACTION_STATE.COMMIT.name());
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(stmt.equals("rollback")){
            File copyFile = new File("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\"+copyTableName+".csv");
            copyFile.delete();
            updateLog("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv",newCSVManager,transaction,TRANSACTION_STATE.ROLLBACK.name());
        }
        transaction.generateTransactionId(username);
    }

    private static void CreateTableDump(NewCSVManager newCSVManager) throws IOException {
        String insertquerystatements = "";
        String tablenametoinsert = "temp";
        ArrayList<ArrayList<String>> data = newCSVManager.getAllDataFromTable("E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\temp.csv");
        for(ArrayList<String> row : data){
            insertquerystatements = insertquerystatements + "insert into table " + tablenametoinsert + " values (";
            insertquerystatements = insertquerystatements + join(row) + ");";
        }
    }

    private static boolean checkIfAnyTransactionUsingSametable(String tableName) throws IOException {
        String filepath = "E:\\Dalhousie\\CSCI 5408 - Data\\project\\sourcecode\\files\\transactions.csv";
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("table",tableName);
        conditions.put("transaction_state",TRANSACTION_STATE.PROCESS.name());
        ArrayList<String> columns = new ArrayList<>();
        columns.add("username");
        columns.add("transactionid");
        columns.add("table");
        columns.add("transaction_state");
        columns.add("timestamp");
        NewCSVManager newCSVManager = new NewCSVManager();
        if(newCSVManager.doesDataExistOnCondition(filepath,conditions,columns)){
            return true;
        }else{
            return false;
        }


    }


    private static void updateLog(String filepath, NewCSVManager newCSVManager,Transaction transaction,String transactionState) throws IOException {
        HashMap<String,String> conditions = new HashMap<>();
        conditions.put("transactionid",transaction.getId());
        HashMap<String,String> set = new HashMap<>();
        set.put("transaction_state",transactionState);
        newCSVManager.updateCSV(filepath,conditions,set);
    }


    //Citation : https://www.codota.com/code/java/methods/java.io.File/renameTo
    protected static void replace(File destfile, File origfile) throws IOException {
        destfile.delete();
        if(!origfile.renameTo(destfile)){
            throw new IOException("Unable to rename file" + origfile.getName());
        }
    }

    private static String join(List<String> namesList) {
        return String.join(",", namesList
                .stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()));
    }
}
